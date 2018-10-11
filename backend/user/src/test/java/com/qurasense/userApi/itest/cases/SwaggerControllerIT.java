package com.qurasense.userApi.itest.cases;

import com.qurasense.userApi.UserApiApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UserApiApplication.class)
public class SwaggerControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    private String base;

    @Before
    public void setUp() throws Exception {
        base = String.format("http://localhost:%s", port);
    }

    @Test
    public void getSwaggerData() throws Exception {
        ResponseEntity<String> response = template.getForEntity(String.format("%s/%s", base, "/v2/api-docs"), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void getSwaggerPage() throws Exception {
        ResponseEntity<String> response = template.getForEntity(String.format("%s/%s", base, "/swagger-ui.html"), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}
