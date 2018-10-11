package com.qurasense.userApi.itest.cases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.repository.local.LocalCustomRepository;
import com.qurasense.userApi.itest.UserApiTestApplication;
import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.model.UserRole;
import com.qurasense.userApi.model.UserWithPassword;
import com.qurasense.userApi.itest.oauth2.OAuthHelper;
import com.qurasense.userApi.repository.LocalCustomerRepository;
import com.qurasense.userApi.repository.LocalUserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApiTestApplication.class)
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OAuthHelper helper;

    @Autowired
    private LocalUserRepository localUserRepository;

    @Autowired
    private LocalCustomerRepository localCustomerRepository;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Autowired
    private ObjectMapper mapper;

    @After
    public void tearDown() {
        localUserRepository.initMockUsers();
    }

    @Test
    public void getUsersByAdmin() throws Exception {
        RequestPostProcessor bearerToken = helper.bearerToken("1", "ADMIN");
        mvc.perform(MockMvcRequestBuilders.get("/users/").accept(MediaType.APPLICATION_JSON).with(bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void getUsersByCustomer() throws Exception {
        RequestPostProcessor bearerToken = helper.bearerToken("1", "CUSTOMER");
        mvc.perform(MockMvcRequestBuilders.get("/users/").accept(MediaType.APPLICATION_JSON).with(bearerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getCustomersByMedicial() throws Exception {
        RequestPostProcessor bearerToken = helper.bearerToken("1", "MEDICAL");
        mvc.perform(MockMvcRequestBuilders.get("/customers/").accept(MediaType.APPLICATION_JSON).with(bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void getUserByAdmin() throws Exception {
        RequestPostProcessor bearerToken = helper.bearerToken("1", "ADMIN");
        mvc.perform(MockMvcRequestBuilders.get("/user/2").accept(MediaType.APPLICATION_JSON).with(bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void getUserByCustomer() throws Exception {
        RequestPostProcessor bearerToken = helper.bearerToken("1", "CUSTOMER");
        mvc.perform(MockMvcRequestBuilders.get("/user/2").accept(MediaType.APPLICATION_JSON).with(bearerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUserByAdmin() throws Exception {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId(idGenerationStrategy.generate(null));
        customerInfo.setUserId("2");
        localCustomerRepository.saveCustomerInfo(customerInfo);

        RequestPostProcessor bearerToken = helper.bearerToken("1", "ADMIN");
        mvc.perform(MockMvcRequestBuilders.delete("/user/2").accept(MediaType.APPLICATION_JSON).with(bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void deleteUserByCustomer() throws Exception {
        RequestPostProcessor bearerToken = helper.bearerToken("1", "CUSTOMER");
        mvc.perform(MockMvcRequestBuilders.delete("/user/2").accept(MediaType.APPLICATION_JSON).with(bearerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createUserByAdmin() throws Exception {
        UserWithPassword user = new UserWithPassword();
        user.setPassword("secret");
        user.setEmail("h@h.com");
        user.setRole(UserRole.CUSTOMER);
        user.setFullName("H H");
        String json = mapper.writeValueAsString(user);

        RequestPostProcessor bearerToken = helper.bearerToken("1", "ADMIN");
        mvc.perform(MockMvcRequestBuilders.post("/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).accept(MediaType.APPLICATION_JSON).with(bearerToken))
            .andExpect(status().isOk())
            .andExpect(content().string(notNullValue()));
    }

    @Test
    public void createUserByCustomer() throws Exception {
        User user = new User("some", "h@h.com", UserRole.CUSTOMER);
        String json = mapper.writeValueAsString(user);

        RequestPostProcessor bearerToken = helper.bearerToken("1", "CUSTOMER");
        mvc.perform(MockMvcRequestBuilders.post("/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .with(bearerToken))
            .andExpect(status().isForbidden());
    }

}
