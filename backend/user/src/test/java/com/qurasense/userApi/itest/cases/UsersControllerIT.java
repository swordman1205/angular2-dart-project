package com.qurasense.userApi.itest.cases;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.qurasense.userApi.UserApiApplication;
import com.qurasense.userApi.model.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UserApiApplication.class)
@ComponentScan(basePackages = {"com.qurasense.userApi"}/*, excludeFilters=
        {@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value= EmulatorUserRepository.class)}*/)
@Ignore
public class UsersControllerIT {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/users/");
        Map<String, String> params = new HashMap<>();
        params.put("username", "lars@qurasense.com");
        params.put("password", "1");
        Map map = template.withBasicAuth("lars@qurasense.com", "1")
                .postForObject("http://localhost:" + port + "/login", params, Map.class);
        System.out.println(map);
    }

    @Test
    public void getUsers() throws Exception {
        ResponseEntity<String> respString = template.getForEntity(base.toString(), String.class);
        assertEquals(HttpStatus.OK, respString.getStatusCode());
        String users = respString.getBody();
        assertNotNull(users);

        //TODO: why fails on deserialize?
//        ParameterizedTypeReference<List<User>> parameterizedTypeReference = new ParameterizedTypeReference<List<User>>() {};
//        ResponseEntity<List<User>> response = template.exchange(base.toString(), HttpMethod.POST, null, parameterizedTypeReference);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        List<User> users = response.getBody();
//        assertNotNull(users);
    }

    @Test
    public void getUser() throws Exception {
        ResponseEntity<User> response = template.getForEntity(String.format("%s1", base.toString()), User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user = response.getBody();
        assertNotNull(user);
    }

}
