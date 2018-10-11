package com.qurasense.userApi.controllers.internal;

import com.qurasense.common.shared.CustomerShare;
import com.qurasense.common.shared.UserShare;
import com.qurasense.userApi.service.CustomerService;
import com.qurasense.userApi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/internal")
@ApiIgnore
public class InternalController {

    private Logger logger = LoggerFactory.getLogger(InternalController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public CustomerShare getCustomerShare(@RequestParam("userId") String userId) {
        logger.info("request customer share for user: {}", userId);
        return customerService.getCustomerShare(userId);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public UserShare getUserShare(@RequestParam("userId") String userId) {
        logger.info("request user share for user: {}", userId);
        return userService.getUserShare(userId);
    }

}
