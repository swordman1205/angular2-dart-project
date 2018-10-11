package com.qurasense.userApi.controllers;

import java.util.Collection;
import java.util.List;

import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.CustomerStatusType;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="qurasense customers", description="qurasense customers")
public class CustomerController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CustomerService customerService;

    @ApiOperation(value = "Get current customer info", response = CustomerInfo.class)
    @RequestMapping(value = "/user/{userId}/info", method = RequestMethod.GET)
    public CustomerInfo getCustomerInfo(User aUser, @PathVariable("userId") String userId) {
        return customerService.getCustomerInfo(userId);
    }

    @ApiOperation(value = "Submit customer info")
    @RequestMapping(value = "/user/{userId}/info", method = RequestMethod.POST)
    public void submitUserInfo(User aUser, @PathVariable("userId") String userId,  @RequestBody CustomerInfo customerInfo) {
        customerInfo.setUserId(userId);
        customerService.saveCustomerInfo(aUser, customerInfo);
    }

    @ApiOperation(value = "Submit customer status")
    @RequestMapping(value = "/customer/{customerId}/status/{youngStatus}", method = RequestMethod.POST)
    public void submitCustomerStatus(User aUser,
            @PathVariable("customerId") String customerId,
            @PathVariable("youngStatus") CustomerStatusType customerStatusType) {
        customerService.updateCustomerStatus(customerId, customerStatusType);
    }

    @ApiOperation(value = "List customers", response = User.class, responseContainer = "List")
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public List<CustomerInfo> getCustomers() {
        return customerService.getCustomers();
    }

    @ApiOperation(value = "get customers by ids", response = CustomerInfo.class, responseContainer = "List")
    @RequestMapping(value = "/customers/ids", method = RequestMethod.POST)
    public Collection<CustomerInfo> fetchCustomers(@RequestBody List<String> customerIds) {
        return customerService.fetchCustomers(customerIds);
    }

    @ApiOperation(value = "get customers by ids", response = CustomerInfo.class, responseContainer = "List")
    @RequestMapping(value = "/customers/userIds", method = RequestMethod.GET)
    public Collection<CustomerInfo> fetchCustomersByUserIds(@RequestParam("customerUserIds") List<String> customerUserIds) {
        return customerService.fetchCustomersByUserIds(customerUserIds);
    }

}
