package com.qurasense.communication.swu.provider.builder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.qurasense.common.SimpleMicroserviceRegistry;
import com.qurasense.common.messaging.broadcast.message.SessionCreated;
import com.qurasense.common.shared.CustomerShare;
import com.qurasense.common.shared.SampleShare;
import com.qurasense.common.shared.UserShare;
import com.qurasense.communication.swu.Recipient;
import com.qurasense.communication.swu.provider.NoReplySenderProviderAdapter;
import com.qurasense.communication.swu.provider.SwuDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

@Component
public class SessionCreatedProviderBuilder {

    @Autowired
    private SimpleMicroserviceRegistry microserviceRegistry;

    @Value("${communicaltion.researchAddress}")
    private String researchAddress;

    public SwuDataProvider build(SessionCreated message) {
        WebClient userClient = WebClient.create(microserviceRegistry.getUserUrl());
        Mono<CustomerShare> customerShareMono = userClient
                .get()
                .uri(builder -> builder.path("/internal/customer")
                        .queryParam("userId", message.getUserId())
                        .build())
                .retrieve()
                .bodyToMono(CustomerShare.class);

        Mono<UserShare> nurseUserShareMono = userClient
                .get()
                .uri(builder -> builder.path("/internal/user")
                        .queryParam("userId", message.getNurseId())
                        .build())
                .retrieve()
                .bodyToMono(UserShare.class);

        WebClient healthClient = WebClient.create(microserviceRegistry.getHealthUrl());
        Mono<SampleShare> sampleShareMono = healthClient.get()
                .uri(builder -> builder.path("/internal/sample")
                    .queryParam("sampleId", message.getSampleId())
                    .build())
                .retrieve()
                .bodyToMono(SampleShare.class);

        Tuple3<CustomerShare, SampleShare, UserShare> tuple =
                Mono.zip(customerShareMono, sampleShareMono, nurseUserShareMono).block();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("kitId", tuple.getT2().getKitId());
        emailData.put("userId", message.getUserId());
        emailData.put("fullName", tuple.getT1().getFullName());
        emailData.put("phone", tuple.getT1().getPhone());
        emailData.put("homeAddress", tuple.getT1().getAddressLine());
        emailData.put("city", tuple.getT1().getCity());
        emailData.put("zip", tuple.getT1().getZip());
        emailData.put("state", tuple.getT1().getState());
        emailData.put("callIntervals", tuple.getT1().getCallIntervals());
        emailData.put("sessionStatusChangeToken", message.getSessionStatusChangeToken());

        Recipient recipient = new Recipient(tuple.getT3().getFullName(), tuple.getT3().getEmail());
        return new NoReplySenderProviderAdapter("tem_8XMqYMqv4BPBpXY3vdTCPYJD", emailData, recipient,
                Arrays.asList(new Recipient("research", researchAddress)));
    }

}
