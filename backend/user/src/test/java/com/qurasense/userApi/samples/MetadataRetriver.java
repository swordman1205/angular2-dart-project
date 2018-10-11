package com.qurasense.userApi.samples;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MetadataRetriver {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Metadata-Flavor", "Google");
//        headers.set

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> exchange = restTemplate.exchange("https://www.googleapis.com/compute/v1/projects/qurasense-dev-1", HttpMethod.GET, entity, String.class);
        System.out.println(exchange.getBody());
    }
}
