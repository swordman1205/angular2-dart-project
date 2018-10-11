package com.qurasense.userApi.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.messaging.AbstractMessagePublisher;
import com.qurasense.common.messaging.messages.CommunicationMessage;
import com.qurasense.common.messaging.messages.RestorePasswordMessage;
import com.qurasense.common.messaging.messages.SuccessSignupMessage;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.token.TokenGenerator;
import com.qurasense.userApi.messaging.RestHealthMessagePublisher;
import com.qurasense.userApi.model.ContactStatus;
import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.EmailConfirmationToken;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.model.UserRole;
import com.qurasense.userApi.repository.CustomerRepository;
import com.qurasense.userApi.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestHealthMessagePublisher restHealthMessagePublisher;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Resource(name = "communicationMessagePublisher")
    private AbstractMessagePublisher cloudMessageManager;

    @Resource(name = "broadcastMessagePublisher")
    private AbstractMessagePublisher broadcastMessagePublisher;

    protected LoadingCache<String, String> forgotPasswordTokenCache = CacheBuilder.newBuilder()
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    throw new IllegalStateException();
                }
            });;

    @Override
    public boolean isUserEmailExists(String email) {
        if(!EmailValidator.getInstance().isValid(email)) {
            return false;
        }
        StopWatch sw = new StopWatch();
        sw.start("find user by email");
        Optional<User> userByEmail = userRepository.getUserByEmail(email);
        sw.stop();
        logger.info(sw.prettyPrint());
        return userByEmail.isPresent();
    }

    @Override
    public Map<String, String> register(String aFullName, String aEmail, String aPassword, CustomerInfo customerInfo) {
        StopWatch sw = new StopWatch();
        sw.start("check email exists");
        if (isUserEmailExists(aEmail)) {
            String msg = String.format("can`t register user with email %s - email already exists", aEmail);
            throw new IllegalStateException(msg);
        }
        sw.stop();
        User user = new User();
        user.setEmail(aEmail);
        user.setFullName(aFullName);
        user.setRole(UserRole.CUSTOMER);//only customer could be saved
        user.setEncryptedPassword(passwordEncoder.encode(aPassword));
        user.setCreateTime(new Date());
        Map<String, String> result = new HashMap<>();
        sw.start("create user and customer info");
        String emailConfirmationTokenString = tokenGenerator.generateToken();
        ofy().transact(() -> {
            String userId = userRepository.create(user);
            customerInfo.setUserId(userId);
            customerInfo.setEmailStatus(ContactStatus.UNCONFIRMED);
            customerInfo.setCreateTime(new Date());
            customerInfo.setCreateUserId(userId);
            String customerId = customerRepository.saveCustomerInfo(customerInfo);
            logger.info("created user {} and customer {}", userId, customerId);
            EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken();
            emailConfirmationToken.setId(idGenerationStrategy.generate(null));
            emailConfirmationToken.setParent(entityRepository.getParent(EmailConfirmationToken.class));
            emailConfirmationToken.setToken(emailConfirmationTokenString);
            emailConfirmationToken.setCreateTime(new Date());
            emailConfirmationToken.setCreateUserId(userId);
            emailConfirmationToken.setCustomerId(customerId);
            entityRepository.save(emailConfirmationToken);
            result.put("userId", userId);
            result.put("customerId", customerId);
        });
        sw.stop();

        sw.start("publishing communication message");
        String url = String.format("https://%s/#/site/emailConfirm/%s", "trial.qurasense.com", emailConfirmationTokenString);
        cloudMessageManager.publishMessage(new SuccessSignupMessage(aFullName, aEmail, CommunicationMessage.ChannelType.EMAIL, url));
        sw.stop();
        logger.info(sw.prettyPrint());
        return result;
    }

    @Override
    public void requestRestorePassword(String email) {
        String token = tokenGenerator.generateToken();
        forgotPasswordTokenCache.put(token, email);

        String url = String.format("https://%s/#/site/restorePassword/%s", "trial.qurasense.com", token);
        cloudMessageManager.publishMessage(new RestorePasswordMessage(url, email, CommunicationMessage.ChannelType.EMAIL));
    }

    @Override
    public void restorePassword(String token, String aPassword) {
        try {
            String email = forgotPasswordTokenCache.get(token);
            Optional<User> user = userRepository.getUserByEmail(email);
            userRepository.changePassword(user.get(), passwordEncoder.encode(aPassword));
            forgotPasswordTokenCache.invalidate(token);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public boolean confirmEmail(String token) {
        EmailConfirmationToken emailConfirmationToken = ofy().load().type(EmailConfirmationToken.class)
                .ancestor(entityRepository.getParent(EmailConfirmationToken.class))
                .filter("token =", token)
                .first()
                .now();
        if (emailConfirmationToken == null) {
            return false;
        }
        CustomerInfo customerInfo = customerRepository.getCustomerInfo(emailConfirmationToken.getCreateUserId());
        customerInfo.setEmailStatus(ContactStatus.CONFIRMED);
        customerRepository.saveCustomerInfo(customerInfo);
        return true;
    }
}
