package com.qurasense.userApi.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qurasense.common.SimpleMicroserviceRegistry;
import com.qurasense.common.messaging.QurasenseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class RestHealthMessagePublisher {

    @Qualifier("jsonObjectMapper")
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpleMicroserviceRegistry simpleMicroserviceRegistry;

    private RestTemplate restTemplate = new RestTemplate();
    private String authenticatedUrl;
    private String createdUrl;

    @PostConstruct
    protected void init() {
        authenticatedUrl = simpleMicroserviceRegistry.getHealthBasedUrl("/authenticated");
        createdUrl = simpleMicroserviceRegistry.getHealthBasedUrl("/created");
    }

    public void publish(QurasenseMessage aMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = null;
        try {
            entity = new HttpEntity<String>(objectMapper.writer().writeValueAsString(aMessage), headers);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        String url = aMessage.getType() == QurasenseMessage.QurasenseMessageType.USER_AUTHENTICATED ? authenticatedUrl : createdUrl;
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
