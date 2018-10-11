package com.qurasense.communication;

import java.io.IOException;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.qurasense.communication"})
public class CommunicationApplication {

	public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(CommunicationApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        //waiting data from system input to prevent stop after start
        for (;;) {
            System.in.read();
        }
	}

}
