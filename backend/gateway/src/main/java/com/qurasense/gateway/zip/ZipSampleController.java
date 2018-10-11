package com.qurasense.gateway.zip;

import java.io.IOException;
import java.util.ArrayList;

import com.qurasense.common.SimpleMicroserviceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
public class ZipSampleController {

    private final Logger logger = LoggerFactory.getLogger(ZipSampleController.class);

    @Autowired
    private SimpleMicroserviceRegistry microserviceRegistry;

    @PostMapping(value = "/aggregate/sample", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<Resource> downloadZip(
            @RequestHeader("Authorization") String token,
            @RequestBody ArrayList<SampleAndUserId> body) throws IOException {
        ZipBuilder zipBuilder = ZipBuilder.create();
        for (SampleAndUserId sampleAndUserId : body) {
            WebClient healthClient = WebClient.create(microserviceRegistry.getHealthUrl());
            byte[] pullPictureBytes = loadPullPicture(token, sampleAndUserId, healthClient);
            zipBuilder.addFile(pullPictureBytes, sampleAndUserId, "pullPicture.jpeg");
            byte[] removePictureBytes = loadRemovePicture(token, sampleAndUserId, healthClient);
            zipBuilder.addFile(removePictureBytes, sampleAndUserId, "removePicture.jpeg");
            byte[] healthXlsxBytes = "TODO".getBytes();
            zipBuilder.addFile(healthXlsxBytes, sampleAndUserId, "health.xlsx");
            byte[] userXlsxBytes = "TODO".getBytes();
            zipBuilder.addFile(userXlsxBytes, sampleAndUserId, "user.xlsx");
        }
        byte[] zip = zipBuilder.build();
        return Mono.just(new ByteArrayResource(zip));
    }

    private byte[] loadRemovePicture(String token, SampleAndUserId sampleAndUserId, WebClient healthClient) {
        byte[] pullPictureBytes;
        try {
            pullPictureBytes = healthClient.get()
                    .uri("/{userId}/sample/{sampleId}/removePicture", sampleAndUserId.getUserId(), sampleAndUserId.getSampleId())
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        } catch (Exception e) {
            logger.error("error loading remove image from healthapi", e);
            pullPictureBytes = e.toString().getBytes();
        }
        return pullPictureBytes;
    }

    private byte[] loadPullPicture(String token, SampleAndUserId sampleAndUserId, WebClient healthClient) {
        byte[] pullPictureBytes;
        try {
            pullPictureBytes = healthClient.get()
                    .uri("/{userId}/sample/{sampleId}/pullPicture", sampleAndUserId.getUserId(), sampleAndUserId.getSampleId())
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        } catch (Exception e) {
            logger.error("error loading pull image from healthapi", e);
            pullPictureBytes = e.toString().getBytes();
        }
        return pullPictureBytes;
    }

    public static class SampleAndUserId {

        private Long sampleId;
        private Long userId;

        public Long getSampleId() {
            return sampleId;
        }

        public void setSampleId(Long sampleId) {
            this.sampleId = sampleId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }
}
