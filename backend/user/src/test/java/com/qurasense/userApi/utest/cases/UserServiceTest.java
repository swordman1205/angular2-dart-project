package com.qurasense.userApi.utest.cases;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.userApi.UserApiApplication;
import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.model.UserRole;
import com.qurasense.userApi.model.UserWithPassword;
import com.qurasense.userApi.repository.LocalCustomerRepository;
import com.qurasense.userApi.repository.LocalUserRepository;
import com.qurasense.userApi.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApiApplication.class)
@ComponentScan(basePackages = {"com.qurasense.userApi"}/*, excludeFilters=
        {@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value= EmulatorUserRepository.class)}*/)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private LocalUserRepository userRepository;

    @Autowired
    private LocalCustomerRepository localCustomerRepository;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @After
    public void tearDown() {
        userRepository.initMockUsers();
    }

    @Test
    @WithMockUser(username="admin", authorities={"ADMIN"})
    public void testAdminGetExistingUserByEmail() {
        Optional<User> userOption = userService.getUserByEmail("zufar@qurasense.com");
        Assert.assertTrue(userOption.isPresent());
        Assert.assertEquals(userOption.get().getEmail(), "zufar@qurasense.com");
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="1", authorities={"CUSTOMER"})
    public void testWrongUserGetExistingUserByEmail() {
        Optional<User> userOption = userService.getUserByEmail("zufar@qurasense.com");
    }

    @Test(expected = NoSuchElementException.class)
    @WithMockUser(username="1", authorities={"ADMIN"})
    public void testAdminGetNonExistingUserByEmail() {
        Optional<User> userOption = userService.getUserByEmail("donald.trumo@qurasense.com");
        Assert.assertFalse(userOption.isPresent());
        userOption.get();
    }

    @Test(expected = IllegalArgumentException.class)
    @WithMockUser(username="1", authorities={"ADMIN"})
    public void testGetUserByInvalidEmail() {
        userService.getUserByEmail("zufar&qurasense.com");
    }

    @Test
    @WithMockUser(username="1", authorities={"ADMIN"})
    public void testAdminGetUserList() {
        List<User> users = userService.getUsers();
        Assert.assertNotNull(users);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="1", roles={"CUSTOMER"})
    public void testUserGetUserList() {
        List<User> users = userService.getUsers();
    }

//    @Test
//    @WithMockUser(username="1", authorities={"ADMIN"})
//    public void testAdminGetCustomersList() {
//        List<CustomerInfo> customers = userService.getCustomers();
//        Assert.assertNotNull(customers);
//    }
//
//    @Test
//    @WithMockUser(username="1", authorities={"MEDICAL"})
//    public void testMedicialGetCustomersList() {
//        List<CustomerInfo> customers = userService.getCustomers();
//        Assert.assertNotNull(customers);
//    }
//
//    @Test(expected = AccessDeniedException.class)
//    @WithMockUser(username="1", authorities={"CUSTOMER"})
//    public void testCustomerGetCustomersList() {
//        List<CustomerInfo> customers = userService.getCustomers();
//        Assert.assertNotNull(customers);
//    }

    @Test
    @WithMockUser(username="1", authorities={"ADMIN"})
    public void testAdminCreateCustomer() {
        UserWithPassword user = new UserWithPassword();
        user.setFullName("Jonh Doe");
        user.setEmail("jonhDoe@qurasense.com");
        user.setPassword("secret");
        user.setRole(UserRole.CUSTOMER);
        userService.create(user);
        User loadedCustomer = userRepository.getUserById(user.getId());
        Assert.assertNotNull(loadedCustomer);
        Assert.assertNotNull(loadedCustomer.getCreateTime());
        Assert.assertNotNull(localCustomerRepository.getCustomerInfo(user.getId()));
    }

    @Test
    @WithMockUser(username="1", authorities={"ADMIN"})
    public void testAdminCreateMedical() {
        UserWithPassword user = new UserWithPassword();
        user.setFullName("Jonh Doe");
        user.setEmail("jonhDoe@qurasense.com");
        user.setPassword("secret");
        user.setRole(UserRole.MEDICAL);
        userService.create(user);
        User loadedMedical = userRepository.getUserById(user.getId());
        Assert.assertNotNull(loadedMedical);
        Assert.assertNotNull(loadedMedical.getCreateTime());
        Assert.assertNull(localCustomerRepository.getCustomerInfo(user.getId()));
    }

    @Test
    @WithMockUser(username="1", authorities={"ADMIN"})
    public  void testAdminGetUserById() {
        User user = new User("Jonh Doe", "jonhDoe@qurasense.com", UserRole.CUSTOMER);
        user.setId("2");
        user.setEncryptedPassword("$2a$10$G/8MOZKl2I04PdzB7L1qc.Sx1UXQK7vtPxKcAJPddOwk.J.55wg4q");
        userRepository.create(user);
        User userOptional = userService.getUserById(user.getId());
        Assert.assertTrue(userOptional != null);
    }

    @Test
    @WithMockUser(username="10", authorities={"ADMIN"})
    public void testAdminUserDelete() {
        User user = new User("Jonh Doe", "jonhDoe@qurasense.com", UserRole.CUSTOMER);
        user.setEncryptedPassword("$2a$10$G/8MOZKl2I04PdzB7L1qc.Sx1UXQK7vtPxKcAJPddOwk.J.55wg4q");
        String userId = idGenerationStrategy.generate(null);
        user.setId(userId);
        userRepository.create(user);

        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId(idGenerationStrategy.generate(null));
        customerInfo.setUserId(userId);
        localCustomerRepository.saveCustomerInfo(customerInfo);

        userService.delete(user.getId());
    }

    @Test
    @WithMockUser(username="1", authorities={"CUSTOMER"})
    public void testUserDeleteCurrent() {
//        User user = userRepository.getUserById(1l);
        userService.delete("1");
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="12", authorities={"CUSTOMER"})
    public void testUserDeleteUser() {
        User user = new User("Jonh Doe", "jonhDoe@qurasense.com", UserRole.CUSTOMER);
        user.setId("1");
        user.setEncryptedPassword("$2a$10$G/8MOZKl2I04PdzB7L1qc.Sx1UXQK7vtPxKcAJPddOwk.J.55wg4q");
        userRepository.create(user);
        userService.delete(user.getId());
    }

    @Test
    @WithMockUser(username="12", authorities={"ADMIN"})
    public void testAdminUserUpdate() {
        User user = new User("Jonh Doe", "jonhDoe@qurasense.com", UserRole.CUSTOMER);
        user.setId("1");
        user.setEncryptedPassword("$2a$10$G/8MOZKl2I04PdzB7L1qc.Sx1UXQK7vtPxKcAJPddOwk.J.55wg4q");
        userRepository.create(user);

        UserWithPassword userWithPassword = new UserWithPassword();
        userWithPassword.setId(user.getId());
        userWithPassword.setFullName("John NotDoe");
        userWithPassword.setPassword("newsecret");
        User updated = userService.update(userWithPassword);

        Assert.assertFalse(Objects.equals(user.getFullName(), updated.getFullName()));
    }

}
