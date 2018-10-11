package com.qurasense.userApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.SimpleCommandLinePropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.qurasense.userApi"})
public class UserApiApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(UserApiApplication.class);
		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
		if (!source.containsProperty("spring.profiles.active") &&
				!System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {
			app.setAdditionalProfiles("emulator");
		}
		SpringApplication.run(UserApiApplication.class, args);
	}

}
