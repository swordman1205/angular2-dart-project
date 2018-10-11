package com.qurasense.userApi.testdata;

import java.util.HashMap;
import java.util.Map;

import com.qurasense.userApi.model.UserRole;
import com.qurasense.userApi.repository.CustomerRepository;
import com.qurasense.userApi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class TestDataController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping(value = "/createTestData", method = RequestMethod.GET)
    public Map<String, UserRole> insert() {
        //result in userid:customerid->userrole format
        Map<String, UserRole> result = new HashMap<>();
        customerRepository.deleteAll();
        userRepository.deleteAllUser();
//        String encryptedSecret = passwordEncoder.encode("secret");
//
//        User admin = new User("Admin admin", "_admin@qurasense.com", UserRole.ADMIN);
//        admin.setCreateTime(new Date());
//        admin.setEncryptedPassword(encryptedSecret);
//        userRepository.create(admin);
//
//        User marieCurie = new User("Marie Curie", "_marie@qurasense.com", UserRole.CUSTOMER);
//        marieCurie.setCreateTime(new Date());
//        marieCurie.setEncryptedPassword(encryptedSecret);
//        userRepository.create(marieCurie);
//        CustomerInfo marieCurieInfo = initCustomerInfo(marieCurie);
//        marieCurieInfo.setState("CA");
//        marieCurieInfo.setCity("Palo Alto");
//        marieCurieInfo.setZip("94020");
//        marieCurieInfo.setAddressLine("1450 Page Mill Rd");
//        marieCurieInfo.setCountry("US");
//        marieCurieInfo.setPhone("12345678");
//        marieCurieInfo.setContactDay(ContactDay.MON_FRI);
//        marieCurieInfo.setContactTimes(Arrays.asList(ContactTime.AFTER_NOON));
//        marieCurieInfo.setDateOfBirth(LocalDate.of(1983, 1, 1));
//        customerRepository.saveCustomerInfo(marieCurieInfo);
//        result.put(String.format("%s:%s", marieCurie.getId(), marieCurieInfo.getId()), marieCurie.getRole());
//
//        User liseMeitner = new User("Lise Meitner", "_lise@qurasense.com", UserRole.CUSTOMER);
//        liseMeitner.setEncryptedPassword(encryptedSecret);
//        liseMeitner.setCreateTime(new Date());
//        userRepository.create(liseMeitner);
//        CustomerInfo liseMeitnerInfo = initCustomerInfo(liseMeitner);
//        liseMeitnerInfo.setState("CA");
//        liseMeitnerInfo.setCity("Palo Alto");
//        liseMeitnerInfo.setZip("94020");
//        liseMeitnerInfo.setAddressLine("1450 Page Mill Rd");
//        liseMeitnerInfo.setDateOfBirth(LocalDate.of(1977, 1, 1));
//        liseMeitnerInfo.setCountry("US");
//        liseMeitnerInfo.setPhone("12345678");
//        liseMeitnerInfo.setContactDay(ContactDay.MON_FRI);
//        liseMeitnerInfo.setContactTimes(Arrays.asList(ContactTime.AFTER_NOON));
//        customerRepository.saveCustomerInfo(liseMeitnerInfo);
//        result.put(String.format("%s:%s", liseMeitner.getId(), liseMeitnerInfo.getId()), liseMeitner.getRole());
//
//        User labTechnicial = new User("Lab technicial", "_labtech@qurasense.com", UserRole.LAB_TECH);
//        labTechnicial.setEncryptedPassword(encryptedSecret);
//        labTechnicial.setCreateTime(new Date());
//        userRepository.create(labTechnicial);
//
//        User medicalProfessional = new User("Medical professional", "_medical@qurasense.com", UserRole.MEDICAL);
//        medicalProfessional.setEncryptedPassword(encryptedSecret);
//        medicalProfessional.setCreateTime(new Date());
//        userRepository.create(medicalProfessional);
//
//        User nurse = new User("Nurse nurse", "_nurse@qurasense.com", UserRole.NURSE);
//        nurse.setEncryptedPassword(encryptedSecret);
//        nurse.setCreateTime(new Date());
//        userRepository.create(nurse);
//        result.put(nurse.getId().toString(), nurse.getRole());

        return result;
    }

//    private CustomerInfo initCustomerInfo(User user) {
//        CustomerInfo customerInfo = new CustomerInfo();
//        customerInfo.setEmail(user.getEmail());
//        customerInfo.setFullName(user.getFullName());
//        customerInfo.setUserId(user.getId());
//        customerInfo.setCustomerStatus(CustomerStatusType.APPROVED);
//        return customerInfo;
//    }

}
