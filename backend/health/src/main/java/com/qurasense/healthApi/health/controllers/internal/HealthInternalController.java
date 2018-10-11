package com.qurasense.healthApi.health.controllers.internal;

import com.qurasense.common.shared.HealthShare;
import com.qurasense.healthApi.health.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/internal")
@ApiIgnore
public class HealthInternalController {
    @Autowired
    private HealthService healthService;

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public HealthShare getHealthShare(@RequestParam String healthId) {
        return healthService.findHealthShare(healthId);
    }

}
