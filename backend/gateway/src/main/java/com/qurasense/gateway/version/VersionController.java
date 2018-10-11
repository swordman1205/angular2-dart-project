package com.qurasense.gateway.version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Controller
public class VersionController {

    private final Logger logger = LoggerFactory.getLogger(VersionController.class);

    @Autowired
    private VersionProperties versionProperties;

    @GetMapping(value = "/version")
    @ResponseBody
    public Mono<String> getVersion() throws IOException {
        logger.info(versionProperties.toString());
        return Mono.just(String.format("rev: %s, time: %s", versionProperties.getHash(), versionProperties.getBuildTime()));
    }

}
