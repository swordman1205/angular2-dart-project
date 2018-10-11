package com.qurasense.communication.samples;

import com.qurasense.common.SimpleMicroserviceRegistry;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientSample {

    public static void main(String[] args) {
        SimpleMicroserviceRegistry smr = new SimpleMicroserviceRegistry();
        smr.init();
        WebClient userClient = WebClient.create(smr.getUserUrl());
        userClient.get().uri("/ping").retrieve().bodyToMono(Void.class).block();
        System.out.println("ok");
    }

}
