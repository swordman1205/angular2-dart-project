package com.qurasense.userApi.testdata;

import java.util.Date;
import javax.annotation.PostConstruct;

import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.userApi.model.Organization;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.model.UserRole;
import com.qurasense.userApi.repository.CustomerRepository;
import com.qurasense.userApi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile({"test"})
@Component
public class TestDataInitiator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityRepository entityRepository;

    @PostConstruct
    protected void initTestData() {
        ObjectifyService.run(() -> {
            int deletedCustomerCount = customerRepository.deleteAll();
            int deletedUserCount = userRepository.deleteAllUser();
            int deletedOrganizationCount = entityRepository.deleteAll(Organization.class);
            String encryptedSecret = passwordEncoder.encode("secret");

            User admin = new User("Admin admin", "_admin@qurasense.com", UserRole.ADMIN);
            admin.setCreateTime(new Date());
            admin.setEncryptedPassword(encryptedSecret);
            userRepository.create(admin);
            logger.info("deleted customer: {} , user: {}, organizaion: {}. Created admin", deletedCustomerCount,
                    deletedUserCount, deletedOrganizationCount);
            return null;
        });

    }

}
