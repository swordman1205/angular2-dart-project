package com.qurasense.userApi.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.messaging.AbstractMessagePublisher;
import com.qurasense.common.messaging.broadcast.message.UserDeleted;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.shared.UserShare;
import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.model.UserRole;
import com.qurasense.userApi.model.UserWithPassword;
import com.qurasense.userApi.repository.UserRepository;
import com.qurasense.userApi.security.SecurityService;
import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Autowired
    private EntityRepository entityRepository;

    @Resource(name = "broadcastMessagePublisher")
    private AbstractMessagePublisher broadcastMessagePublisher;

    @Override
    public Optional<User> getUserByEmail(String email) {
        if(!isValidEmail(email)) {
            throw new IllegalArgumentException("invalid email> " + email);
        }
        return userRepository.getUserByEmail(email);
    }

    @Override
    public User getUserById(String id) {
        return userRepository.getUserById(id);
    }

    @Override
    public UserShare getUserShare(String userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new IllegalStateException("not found user with id:" + userId);
        }
        UserShare userShare = new UserShare();
        userShare.setEmail(user.getEmail());
        userShare.setFullName(user.getFullName());
        return userShare;
    }

    @Override
    public void setActive(String userId) {
        User user = userRepository.getUserById(userId);
        logger.info("setting user as active, id:{} fullName: {}, role: {} ", user.getId(), user.getFullName(), user.getRole());
        user.setActive(true);
        userRepository.update(user);
    }

    private boolean isValidEmail(String email) {
        EmailValidator ev = EmailValidator.getInstance();
        return ev.isValid(email);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public List<User> getNurses() {
        return userRepository.getNurses();
    }

    @Override
    public String create(UserWithPassword user) {
        Validate.isTrue(user.getId() == null, "User should not contain id while createUser");
        User toCreate = new User();
        toCreate.setEmail(user.getEmail());
        toCreate.setFullName(user.getFullName());
        toCreate.setRole(user.getRole());
        toCreate.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
        toCreate.setCreateTime(new Date());
        toCreate.setId(idGenerationStrategy.generate(null));
        if (toCreate.getRole() == UserRole.CUSTOMER) {
            customerService.internalCreateCustomer(toCreate);
        } else {
            String id = userRepository.create(toCreate);
            toCreate.setActive(true);
            toCreate.setId(id);
        }
        user.setId(toCreate.getId());
        return toCreate.getId();
    }

    @Override
    public void delete(String userId) {
        if (securityService.isCurrentUser(userId) && securityService.isCurrentUserHasRole(UserRole.ADMIN)) {
            throw new IllegalStateException("admin can`t delete himself");
        }
        User user = userRepository.getUserById(userId);
        logger.info("deleting user id: {}, role: {}", user.getId(), user.getRole());
        UserRole userRole = user.getRole();
        if (userRole == UserRole.CUSTOMER) {
            CustomerInfo customerInfo = customerService.getCustomerInfo(userId);
            entityRepository.delete(customerInfo);
            userRepository.deleteUser(userId);
        } else {
            userRepository.deleteUser(userId);
        }
        broadcastMessagePublisher.publishMessage(new UserDeleted(userId, userRole.name()));
    }

    @Override
    public User update(UserWithPassword userWithPassword) {
        String encodedPassword = passwordEncoder.encode(userWithPassword.getPassword());
        userWithPassword.setEncryptedPassword(encodedPassword);
        return userRepository.update(userWithPassword);
    }

    @Override
    public void update(User user) {
        userRepository.updateCurrent(user);
    }
}
