package com.qurasense.labApi.trial.controllers;

import java.util.List;
import java.util.Objects;

import com.qurasense.labApi.trial.model.TrialParticipant;
import com.qurasense.labApi.trial.service.TrialParticipantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = TrialParticipantController.URL)
@Api(value="Trial participant", description="Trial participants api")
public class TrialParticipantController {
    public static final String URL= "/trial/participants";
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TrialParticipantService service;

    @ApiOperation(value = "Get trial participant", response = TrialParticipant.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<TrialParticipant> get(@PathVariable("id") String id) {
        TrialParticipant trialParticipant = service.findTrialParticipantById(id);
        if (Objects.nonNull(trialParticipant)) {
            return ResponseEntity.ok(trialParticipant);
        }
        return ResponseEntity.notFound().build();
    }


    @ApiOperation(value = "Get trial participants", response = TrialParticipant.class, responseContainer = "List")
    @RequestMapping(value = "/byIds", method = RequestMethod.GET)
    public ResponseEntity<List<TrialParticipant>> getTrialParticipantsByIds(@RequestParam("participantId") List<String> participantIds) {
        List<TrialParticipant> trialParticipant = service.fetchTrialParticipantsByIds(participantIds);
        if (Objects.nonNull(trialParticipant)) {
            return ResponseEntity.ok(trialParticipant);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Get trial participant", response = TrialParticipant.class, responseContainer = "List")
    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<List<TrialParticipant>> getTrialParticipantsByCustomerId(@PathVariable("customerId") String customerId) {
        List<TrialParticipant> trialParticipant = service.fetchTrialParticipants(customerId);
        if (Objects.nonNull(trialParticipant)) {
            return ResponseEntity.ok(trialParticipant);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Get trial participants by nurse id", response = TrialParticipant.class, responseContainer = "List")
    @RequestMapping(value = "/nurse/{nurseId}", method = RequestMethod.GET)
    public ResponseEntity<List<TrialParticipant>> getTrialParticipantsByNurseId(@PathVariable("nurseId") String customerId,
            @RequestParam("sessionStatuses") List<String> sessionStatuses) {
        List<TrialParticipant> trialParticipant = service.fetchTrialParticipantsByNurseId(customerId, sessionStatuses);
        if (Objects.nonNull(trialParticipant)) {
            return ResponseEntity.ok(trialParticipant);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Assign customer to trial", response = String.class)
    @RequestMapping(value = "/customer/{customerId}/trial/{trialId}", method = RequestMethod.POST)
    public String assignCustomerToTrial(@PathVariable("customerId") String customerId,
            @PathVariable("trialId") String trialId) {
        TrialParticipant trialParticipant = new TrialParticipant();
        trialParticipant.setCustomerId(customerId);
        trialParticipant.setTrialId(trialId);
        return service.createTrialParticipant(trialParticipant);
    }

    @ApiOperation(value = "Assign customer to trial", response = String.class)
    @RequestMapping(value = "/{participantId}/status/{status}", method = RequestMethod.POST)
    public boolean updateParticipantStatus(@PathVariable("participantId") String participantId,
            @PathVariable("status") String status) {
        return service.updateParticipantStatus(participantId, status);
    }

    @ApiOperation(value = "Assign customer to trial", response = Boolean.class)
    @RequestMapping(value = "/{participantId}/nurse/{nurseId}", method = RequestMethod.POST)
    public boolean assignNurseToParticipant(@PathVariable("participantId") String participantId,
            @PathVariable("nurseId") String nurseId) {
        return service.assignNurse(participantId, nurseId);
    }

    @ApiOperation(value = "Send kits", response = Boolean.class)
    @RequestMapping(value = "/{participantId}/sendKits", method = RequestMethod.POST)
    public boolean sendKits(@PathVariable("participantId") String participantId) {
        return service.sendKits(participantId);
    }

}
