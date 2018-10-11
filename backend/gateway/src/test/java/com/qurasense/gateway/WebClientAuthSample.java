package com.qurasense.gateway;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientAuthSample {

    public static void main(String[] args) {
        WebClient userClient = WebClient.create("localhost:8082");

        ParameterizedTypeReference<Map<String, String>> oauthType = new ParameterizedTypeReference<Map<String, String>>() {};

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "_admin@qurasense.com");
        formData.add("password", "secret");
        formData.add("grant_type", "password");
        Map<String, String> oauthResponse = userClient.post()
                .uri("/oauth/token")
                .header("Authorization", "Basic YnJvd3Nlcjpicm93c2Vyc2VjcmV0==")
                .syncBody(formData)
                .retrieve()
                .bodyToMono(oauthType)
                .block();

        System.out.println("received oauth: " + oauthResponse);
    }

}
