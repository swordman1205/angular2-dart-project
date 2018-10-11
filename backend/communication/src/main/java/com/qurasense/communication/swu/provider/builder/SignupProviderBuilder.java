package com.qurasense.communication.swu.provider.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.qurasense.common.DateUtils;
import com.qurasense.common.SimpleMicroserviceRegistry;
import com.qurasense.common.messaging.broadcast.message.SignupMessage;
import com.qurasense.common.shared.CustomerShare;
import com.qurasense.common.shared.HealthShare;
import com.qurasense.communication.swu.Recipient;
import com.qurasense.communication.swu.provider.NoReplySenderProviderAdapter;
import com.qurasense.communication.swu.provider.SwuDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Component
public class SignupProviderBuilder {

    @Autowired
    private SimpleMicroserviceRegistry microserviceRegistry;

    @Value("${communicaltion.researchAddress}")
    private String researchAddress;

    public SwuDataProvider build(SignupMessage message) {
        WebClient userClient = WebClient.create(microserviceRegistry.getUserUrl());
        Mono<CustomerShare> customerShareMono = userClient
                .get()
                .uri(builder -> builder.path("/internal/customer")
                        .queryParam("userId", message.getUserId())
                        .build())
                .retrieve()
                .bodyToMono(CustomerShare.class);

        WebClient healthClient = WebClient.create(microserviceRegistry.getHealthUrl());
        Mono<HealthShare> healthShareMono = healthClient.get()
                .uri(builder -> builder.path("/internal/health")
                        .queryParam("healthId", message.getHealthId())
                        .build())
                .retrieve()
                .bodyToMono(HealthShare.class);

        Tuple2<CustomerShare, HealthShare> block = Mono.zip(customerShareMono, healthShareMono).block();
        CustomerShare customerShare = block.getT1();
        HealthShare healthShare = block.getT2();

        Map<String, Object> emailData = new HashMap<>();
        emailData.put("userId", message.getUserId());
        emailData.put("fullName", customerShare.getFullName());
        emailData.put("phone", customerShare.getPhone());
        emailData.put("homeAddress", customerShare.getAddressLine());
        emailData.put("city", customerShare.getCity());
        emailData.put("zip", customerShare.getZip());
        emailData.put("state", customerShare.getState());
        emailData.put("callIntervals", customerShare.getCallIntervals());

        emailData.put("averageCycleLength", healthShare.getTypicalCycleLength());
        emailData.put("averagePeriodLength", healthShare.getAveragePeriodLength());
        emailData.put("birthControls", healthShare.getBirthControls().stream().collect(Collectors.joining(",")));
        emailData.put("firstDateOfLastPeriod", DateUtils.formatDateTime(healthShare.getFirstDateOfLastPeriod()));
        emailData.put("height", healthShare.getHeight());
        emailData.put("heightUnit", healthShare.getHeightUnit());
        emailData.put("weight", healthShare.getWeight());
        emailData.put("weightUnit", healthShare.getWeightUnit());
        emailData.put("menstruate", healthShare.getMenstruate());
        emailData.put("sexualActivity", healthShare.getSexualActivity());

        Recipient recipient = new Recipient("Qurasense research", researchAddress);

        return new NoReplySenderProviderAdapter("tem_BVtVJ33FQ4CBqmY3bRCVv4XP", emailData, recipient);
    }

}
