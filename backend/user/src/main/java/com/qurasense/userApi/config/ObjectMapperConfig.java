package com.qurasense.userApi.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class ObjectMapperConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @Primary
    public ObjectMapper jsonObjectMapper() {
        return Jackson2ObjectMapperBuilder
                .json()
                .applicationContext(applicationContext)
                .modulesToInstall(JavaTimeModule.class)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .serializationInclusion(Include.NON_NULL)
                .build();
    }
}