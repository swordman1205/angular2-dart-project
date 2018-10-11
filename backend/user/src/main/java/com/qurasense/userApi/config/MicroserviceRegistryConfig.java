package com.qurasense.userApi.config;

import com.qurasense.common.SimpleMicroserviceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicroserviceRegistryConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public SimpleMicroserviceRegistry simpleMicoserviceRegistry() {
        logger.info("setting up microservice registry");
        return new SimpleMicroserviceRegistry();
    }
}
