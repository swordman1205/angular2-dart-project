package com.qurasense.userApi.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.qurasense.common.DateUtils;
import com.qurasense.userApi.model.ContactDay;
import com.qurasense.userApi.model.ContactTime;
import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.service.RegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="qurasense user registration", description="qurasense user registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @ApiOperation(value = "register new user")
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public Map<String, String> register(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,

            @RequestParam String phone,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date birthDate,
            @RequestParam String addressLine,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String zip,
            @RequestParam String country,
            @RequestParam List<String> contactTimes,
            @RequestParam String contactDay) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setEmail(email);
        customerInfo.setFullName(fullName);
        customerInfo.setPhone(phone);
        customerInfo.setDateOfBirth(DateUtils.toLocalDate(birthDate));
        customerInfo.setAddressLine(addressLine);
        customerInfo.setCity(city);
        customerInfo.setState(state);
        customerInfo.setZip(zip);
        customerInfo.setCountry(country);
        customerInfo.setContactTimes(contactTimes.stream().map(ContactTime::valueOf).collect(Collectors.toList()));
        customerInfo.setContactDay(ContactDay.valueOf(contactDay));
        return registrationService.register(fullName, email, password, customerInfo);
    }

    @ApiOperation(value = "check is user with such email exist", response = Boolean.class)
    @RequestMapping(value = "/registration/{email}/exists", method = RequestMethod.GET)
    public boolean checkUserByEmail(@PathVariable String email) {
        return registrationService.isUserEmailExists(email);
    }

    @ApiOperation(value = "check is user with such email exist", response = Void.class)
    @RequestMapping(value = "/registration/{email}/requestRestorePassword", method = RequestMethod.GET)
    public void restorePassword(@PathVariable String email) {
        registrationService.requestRestorePassword(email);
    }

    @ApiOperation(value = "check is user with such email exist", response = Void.class)
    @RequestMapping(value = "/registration/restorePassword/{token}", method = RequestMethod.POST)
    public void restorePassword(@PathVariable String token, @RequestParam(name = "password") String aPassword) {
        registrationService.restorePassword(token, aPassword);
    }

    @ApiOperation(value = "check is user with such email exist", response = Void.class)
    @RequestMapping(value = "/registration/email/{token}/confirm", method = RequestMethod.POST)
    public boolean confirmEmail(@PathVariable String token) {
        return registrationService.confirmEmail(token);
    }

}
