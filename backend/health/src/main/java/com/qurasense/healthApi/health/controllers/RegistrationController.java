package com.qurasense.healthApi.health.controllers;

import com.qurasense.healthApi.health.service.RegistrationService;
import com.qurasense.healthApi.health.service.RegistrationService.RegistrationData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="qurasense user registration", description="qurasense user registration")
public class RegistrationController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RegistrationService registrationService;

    @ApiOperation(value = "register new customer health info")
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public void register(@RequestBody RegistrationData registrationData) {
        logger.info("received registraion data for customer, userId: {}", registrationData.getHealthInfo().getUserId());
        registrationService.register(registrationData);
    }

}
