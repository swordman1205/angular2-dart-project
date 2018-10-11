package com.qurasense.healthApi.health.controllers;

import java.util.Objects;

import com.qurasense.healthApi.health.model.HealthRecord;
import com.qurasense.healthApi.health.service.HealthRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="health customer", description="health customer")
public class HealthRecordController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HealthRecordService healthRecordService;

    @ApiOperation(value = "Create health customer")
    @RequestMapping(value = "/healthRecord", method = RequestMethod.POST)
    public String saveHealthCustomer(@RequestBody HealthRecord healthRecord) {
        logger.info("incoming request to create health customer");
        return healthRecordService.saveHealthCustomer(healthRecord);
    }

    @ApiOperation(value = "Get health customer by id", response = HealthRecord.class)
    @RequestMapping(value = "/healthRecord/{healthCustomerId}", method = RequestMethod.GET)
    public HealthRecord getHealthCustomer(@PathVariable String healthCustomerId) {
        logger.info("incoming request to get health customer");
        return healthRecordService.findById(healthCustomerId);
    }

    @ApiOperation(value = "Get health customer by id", response = HealthRecord.class)
    @RequestMapping(value = "/healthRecord/customer/user/{customerUserId}", method = RequestMethod.GET)
    public ResponseEntity<HealthRecord> getHealthCustomerByCustomerUserId(@PathVariable String customerUserId) {
        logger.info("incoming request to find health customer by customer id");
        HealthRecord healthRecord = healthRecordService.findByCustomerUserId(customerUserId);
        if (Objects.nonNull(healthRecord)) {
            return ResponseEntity.ok(healthRecord);
        }
        return ResponseEntity.notFound().build();
    }

}
