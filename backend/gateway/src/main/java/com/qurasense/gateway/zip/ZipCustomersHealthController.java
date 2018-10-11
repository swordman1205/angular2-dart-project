package com.qurasense.gateway.zip;

import java.io.IOException;

import com.qurasense.common.SimpleMicroserviceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
public class ZipCustomersHealthController {

    private final Logger logger = LoggerFactory.getLogger(ZipCustomersHealthController.class);

    @Autowired
    private SimpleMicroserviceRegistry microserviceRegistry;

    @PostMapping(value = "/aggregate/customersHealths", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<Resource> downloadZip(
            @RequestHeader("Authorization") String token) throws IOException {
        logger.info("requesting zip with customer and health");
        ZipBuilder zipBuilder = ZipBuilder.create();
        WebClient userClient = WebClient.create(microserviceRegistry.getUserUrl());
        Mono<byte[]> customerReportMono = userClient.get()
                .uri("/report/customers")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(byte[].class);

        WebClient healthClient = WebClient.create(microserviceRegistry.getHealthUrl());
        Mono<byte[]> healthReportMono = healthClient.get()
                .uri("/report/healths")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(byte[].class);

        return Mono.zip(customerReportMono, healthReportMono)
            .flatMap(block -> {
                try {
                    zipBuilder.addFile(block.getT1(), "customers.xlsx");
                    zipBuilder.addFile(block.getT2(), "healths.xlsx");
                    byte[] zip = zipBuilder.build();
                    return Mono.just(new ByteArrayResource(zip));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            });
    }


}
