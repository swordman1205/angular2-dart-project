package com.qurasense.userApi.testdata;

import java.util.List;
import javax.annotation.PostConstruct;

import com.googlecode.objectify.ObjectifyService;
import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.repository.CustomerRepository;
import com.qurasense.userApi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile({"sanitize"})
@Component
public class DataSanitizer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    protected void initTestData() {
        String encryptedSecret = passwordEncoder.encode("secret");
        ObjectifyService.run(() -> {
            List<User> users = userRepository.getUsers();

            for (User user: users) {
                if (user.getEmail().startsWith("_")) {
                    continue;
                }
                logger.info("sanitizing {}", user.getEmail());
                user.setEmail(String.format("_%s", user.getEmail()));
                user.setEncryptedPassword(encryptedSecret);
                userRepository.update(user);
            }

            List<CustomerInfo> customers = customerRepository.getCustomers();
            for (CustomerInfo customerInfo: customers) {
                if (customerInfo.getEmail().startsWith("_")) {
                    continue;
                }
                logger.info("sanitizing customer {}", customerInfo.getEmail());
                customerInfo.setEmail(String.format("_%s", customerInfo.getEmail()));
                customerRepository.saveCustomerInfo(customerInfo);
            }

            logger.info("sanitized {} user", users.size());
            return null;
        });

    }

}
