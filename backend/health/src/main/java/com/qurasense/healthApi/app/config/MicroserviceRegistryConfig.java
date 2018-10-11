package com.qurasense.app.config;

import com.qurasense.common.SimpleMicroserviceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicroserviceRegistryConfig {

    @Bean
    public SimpleMicroserviceRegistry simpleMicoserviceRegistry() {
        return new SimpleMicroserviceRegistry();
    }
}
