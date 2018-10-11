package com.qurasense.healthApi.health.controllers;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.qurasense.healthApi.health.model.HealthCard;
import com.qurasense.healthApi.health.model.HealthCardType;
import com.qurasense.healthApi.health.model.HealthInfo;
import com.qurasense.healthApi.health.model.LastPeriodDate;
import com.qurasense.healthApi.health.service.HealthService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="qurasense health", description="qurasense health api")
public class HealthController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HealthService healthService;

    @ApiOperation(value = "submit user period")
    @RequestMapping(value = "/{userId}/period", method = RequestMethod.POST)
    public void period(@PathVariable("userId") Long userId, @RequestBody LastPeriodDate datePost) {
        healthService.savePeriod(userId, datePost.getLastPeriodDate());
    }

    @ApiOperation(value = "Get user last period", response = LastPeriodDate.class)
    @RequestMapping(value = "/{userId}/lastPeriod", method = RequestMethod.GET)
    public LastPeriodDate getLastPeriod(@PathVariable("userId") Long userId) {
        Date lastPeriod = healthService.getLastPeriod(userId);
        LastPeriodDate datePost = new LastPeriodDate();
        datePost.setLastPeriodDate(lastPeriod);
        return datePost;
    }

    @ApiOperation(value = "Get health info", response = HealthInfo.class)
    @RequestMapping(value = "/{userId}/healthInfo", method = RequestMethod.GET)
    public ResponseEntity<HealthInfo> getHealthInfo(@PathVariable("userId") String userId) {
        HealthInfo health = healthService.findHealth(userId);
        if (Objects.nonNull(health)) {
            return ResponseEntity.ok(health);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Submit health info")
    @RequestMapping(value = "/{userId}/healthInfo", method = RequestMethod.POST)
    public void saveHealthInfo(@PathVariable("userId") String userId, @RequestBody HealthInfo aHealthInfo) {
        healthService.saveHealth(aHealthInfo);
    }

    @ApiOperation(value = "Get cards for specific user", response = HealthCard.class, responseContainer = "List")
    @RequestMapping(value = "/{userId}/cards", method = RequestMethod.GET)
    public List<HealthCard> getCards(@PathVariable("userId") String userId) {
        return healthService.getCards(userId);
    }

    @ApiOperation(value = "Create card for specific user")
    @RequestMapping(value = "/{userId}/cards/{type}", method = RequestMethod.POST)
    public String createCard(@PathVariable("userId") String userId,
                                     @PathVariable("type") HealthCardType type,
                                     @RequestParam(name = "note", required = false) String aNote) {
        logger.info("incoming request to create card: userId={}, type={}, note={}", userId, type, aNote);
        HealthCard healthCard = new HealthCard(userId, type, LocalDate.now(), aNote);
        return healthService.saveHealthCard(healthCard);
    }

}
