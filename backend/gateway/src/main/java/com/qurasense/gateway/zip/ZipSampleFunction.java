package com.qurasense.gateway.zip;

import java.util.List;

import com.qurasense.common.SimpleMicroserviceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

/**
 * Just example to be able use handler functions.
 */
@Component
public class ZipSampleFunction implements HandlerFunction {

    @Autowired
    private SimpleMicroserviceRegistry microserviceRegistry;

    @Override
    public Mono<ByteArrayResource> handle(ServerRequest request)  {
        List<ZipSampleController.SampleAndUserId> sampleAndUserIds =
                request.bodyToFlux(ZipSampleController.SampleAndUserId.class).collectList().block();
        try {
            ZipBuilder zipBuilder = ZipBuilder.create();
            for (ZipSampleController.SampleAndUserId sampleAndUserId : sampleAndUserIds) {
                WebClient healthClient = WebClient.create(microserviceRegistry.getHealthUrl());
                final List<String> authorization = request.headers().header("Authorization");
                byte[] pullPictureBytes = healthClient.get()
                        .uri("/{userId}/sample/{sampleId}/pullPicture", sampleAndUserId.getUserId(), sampleAndUserId.getSampleId())
                        .header("Authorization", authorization.iterator().next())
                        .exchange()
                        .block()
                        .bodyToMono(byte[].class)
                        .block();

                zipBuilder.addFile(pullPictureBytes, sampleAndUserId, "pullPicture.jpeg");
                byte[] removePictureBytes = "TODO".getBytes();
                zipBuilder.addFile(removePictureBytes, sampleAndUserId, "removePicture.jpeg");
                byte[] healthXlsxBytes = "TODO".getBytes();
                zipBuilder.addFile(healthXlsxBytes, sampleAndUserId, "health.xlsx");
                byte[] userXlsxBytes = "TODO".getBytes();
                zipBuilder.addFile(userXlsxBytes, sampleAndUserId, "user.xlsx");
            }
            byte[] zip = zipBuilder.build();
            return Mono.just(new ByteArrayResource(zip));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

    }
}
