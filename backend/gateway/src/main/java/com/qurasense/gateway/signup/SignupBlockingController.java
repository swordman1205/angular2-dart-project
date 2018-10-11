package com.qurasense.gateway.signup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qurasense.common.SimpleMicroserviceRegistry;
import com.qurasense.gateway.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@RestController
public class SignupBlockingController {

    private final Logger logger = LoggerFactory.getLogger(SignupBlockingController.class);

    @Autowired
    private SimpleMicroserviceRegistry microserviceRegistry;

    @Autowired
    private ObjectMapper objectMapper;

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping(value = "/signup")
    public Mono<String> signup(@RequestBody SignupData signupData) throws IOException {
        StopWatch sw = new StopWatch();
        sw.start("user registration");
        logger.info("received signup request, email: {}", signupData.getEmail());

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("fullName", signupData.getFullName());
        params.add("email", signupData.getEmail());
        params.add("password", signupData.getPassword());
        params.add("phone", signupData.getPhone());
        params.add("birthDate", WebConfig.ISO_DATE_FORMATTER.print(signupData.getBirthDate(), Locale.getDefault()));
        params.add("addressLine", signupData.getAddressLine());
        params.add("city", signupData.getCity());
        params.add("state", signupData.getState());
        params.add("zip", signupData.getZip());
        params.add("country", signupData.getCountry());
        params.put("contactTimes", signupData.getContactTimes());
        params.add("contactDay", signupData.getContactDay());

        ParameterizedTypeReference<Map<String, String>> registerType = new ParameterizedTypeReference<Map<String, String>>() {};
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params, userHeaders);

        String userRegisterUrl = microserviceRegistry.getUserUrl() + "/registration";
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(userRegisterUrl, HttpMethod.POST,
                request, registerType);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException("Error while request, status: " + responseEntity.getStatusCode());
        }
        sw.stop();
        sw.start("health registration");

        String userId = responseEntity.getBody().get("userId");
        String customerId = responseEntity.getBody().get("customerId");
        logger.info("created user {} and client: {}", userId, customerId);

        Map<String, Object> healthRecordMap = new HashMap<>();
        healthRecordMap.put("customerUserId", userId);
        healthRecordMap.put("customerId", customerId);

        Map<String, Object> healthInfoMap = new HashMap<>();
        healthInfoMap.put("userId", userId);
        healthInfoMap.put("menstruate", signupData.getMenstruate());
        healthInfoMap.put("firstDateOfLastPeriod", signupData.getFirstDateOfLastPeriod().getTime());
        healthInfoMap.put("averagePeriodLength", signupData.getAveragePeriodLength());
        healthInfoMap.put("typicalCycleLength", signupData.getTypicalCycleLength());
        healthInfoMap.put("sexualActivity", signupData.getSexualActivity());
        healthInfoMap.put("birthControls", signupData.getBirthControl().toArray());
        healthInfoMap.put("weight", signupData.getWeight());
        healthInfoMap.put("weightUnit", signupData.getWeightUnit());
        healthInfoMap.put("height", signupData.getHeight());
        healthInfoMap.put("heightUnit", signupData.getHeightUnit());

        Map<String, Map<String, Object>> healthRegistrationData = new HashMap<>();
        healthRegistrationData.put("healthInfo", healthInfoMap);
        healthRegistrationData.put("healthRecord", healthRecordMap);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String healthRegisterUrl = microserviceRegistry.getHealthUrl() + "/registration";
        HttpEntity<String> entity = new HttpEntity<String>(objectMapper.writer().writeValueAsString(healthRegistrationData), headers);

        restTemplate.exchange(healthRegisterUrl, HttpMethod.POST, entity, Void.class);
        sw.stop();
        logger.info("signup completed: {}", sw.prettyPrint());
        return Mono.just(userId);
    }

}
