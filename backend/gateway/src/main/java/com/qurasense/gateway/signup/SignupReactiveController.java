package com.qurasense.gateway.signup;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.qurasense.common.SimpleMicroserviceRegistry;
import com.qurasense.gateway.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

//a lot of problems with this reactive webclient, thats why will use plain old bocking
//@RestController
public class SignupReactiveController {

    private final Logger logger = LoggerFactory.getLogger(SignupReactiveController.class);

    @Autowired
    private SimpleMicroserviceRegistry microserviceRegistry;

//    @PostMapping(value = "/signup")
    public Mono<Long> signup(@RequestBody SignupData signupData) throws IOException {
        logger.info("received signup request, email: {}", signupData.getEmail());
        WebClient userClient = WebClient.create(microserviceRegistry.getUserUrl());
        ParameterizedTypeReference<Map<String, Long>> registerType = new ParameterizedTypeReference<Map<String, Long>>() {};
        Mono<Map<String, Long>> mapMono = userClient.post().uri(builder -> builder.path("registration")
                .queryParam("fullName", signupData.getFullName())
                .queryParam("email", signupData.getEmail())
                .queryParam("password", signupData.getPassword())
                .queryParam("phone", signupData.getPhone())
                .queryParam("birthDate", WebConfig.ISO_DATE_FORMATTER.print(signupData.getBirthDate(), Locale.getDefault()))
                .queryParam("addressLine", signupData.getAddressLine())
                .queryParam("city", signupData.getCity())
                .queryParam("state", signupData.getState())
                .queryParam("zip", signupData.getZip())
                .queryParam("country", signupData.getCountry())
                .queryParam("contactTimes", signupData.getContactTimes().toArray())
                .queryParam("contactDay", signupData.getContactDay()).build())
                .retrieve()
                .bodyToMono(registerType);
        Map<String, Long> responseUser = mapMono.block(Duration.ofSeconds(5));

        Long userId = responseUser.get("userId");
        Long customerId = responseUser.get("customerId");
        logger.info("created user {} and client: {}", userId, customerId);

        Map<String, Object> healthInfoMap = new HashMap<>();
        healthInfoMap.put("userId", userId);
        healthInfoMap.put("menstruate", signupData.getMenstruate());
        healthInfoMap.put("firstDateOfLastPeriod", signupData.getFirstDateOfLastPeriod().getTime());
        healthInfoMap.put("averagePeriodLength", signupData.getAveragePeriodLength());
        healthInfoMap.put("typicalCycleLength", signupData.getTypicalCycleLength());
        healthInfoMap.put("sexualActivity", signupData.getSexualActivity());
        healthInfoMap.put("birthControl", signupData.getBirthControl().toArray());
        healthInfoMap.put("weight", signupData.getWeight());
        healthInfoMap.put("weightUnit", signupData.getWeightUnit());
        healthInfoMap.put("height", signupData.getHeight());
        healthInfoMap.put("heightUnit", signupData.getHeightUnit());

        WebClient healthClient = WebClient.create(microserviceRegistry.getHealthUrl());
        /*ResponseEntity<Void> responseHealth = */
        healthClient.post().uri("/registration")
                .syncBody(healthInfoMap)
                .retrieve()
                .bodyToMono(Void.class)
                .block(Duration.ofSeconds(5));
//        if (responseHealth.getStatusCode() != HttpStatus.OK) {
//            throw new IllegalStateException("error signup at health" + responseHealth.getStatusCode().toString());
//        }

        logger.info("signup completed");
        return Mono.just(userId);
    }

}
