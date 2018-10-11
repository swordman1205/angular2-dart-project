package com.qurasense.gateway.testdata;

import java.io.IOException;
import java.util.Map;

import com.qurasense.common.SimpleMicroserviceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
public class TestDataController {

    private final Logger logger = LoggerFactory.getLogger(TestDataController.class);

    @Autowired
    private SimpleMicroserviceRegistry microserviceRegistry;

    @GetMapping(value = "/createTestData")
    @ResponseBody
    public Mono<String> createTestData() throws IOException {
        logger.info("creating test data");
        WebClient userClient = WebClient.create(microserviceRegistry.getUserUrl());
        WebClient healthClient = WebClient.create(microserviceRegistry.getHealthUrl());
        ParameterizedTypeReference<Map<String, String>> parameterizedTypeReference
                = new ParameterizedTypeReference<Map<String, String>>() {};
        Mono<Map<String, String>> createdUsersMono = userClient
                .get()
                .uri("/createTestData")
                .retrieve()
                .bodyToMono(parameterizedTypeReference);
        return createdUsersMono.flatMap(createdUsersMap ->
            healthClient
                    .post()
                    .uri("/createTestData")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(createdUsersMap), parameterizedTypeReference)
                    .retrieve()
                    .bodyToMono(Void.class)
        ).flatMap((some) -> Mono.just("OK"));
    }

}
