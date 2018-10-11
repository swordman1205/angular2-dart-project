package com.qurasense.labApi.trial.controllers;

import java.util.List;
import java.util.Objects;

import com.qurasense.labApi.trial.model.TrialSession;
import com.qurasense.labApi.trial.service.TrialSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = TrialSessionController.URL)
@Api(value="Trial session", description="Trial session api")
public class TrialSessionController {
    public static final String URL= "/trial/sessions";
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TrialSessionService service;

    @ApiOperation(value = "Get trial session", response = TrialSession.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<TrialSession> get(@PathVariable("id") String id) {
        TrialSession trialSession = service.find(id);
        if (Objects.nonNull(trialSession)) {
            return ResponseEntity.ok(trialSession);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Get trial sessions by participant", response = TrialSession.class, responseContainer = "List")
    @RequestMapping(value = "/participant/{participantId}", method = RequestMethod.GET)
    public ResponseEntity<List<TrialSession>> getTrialSessionsByParticipant(@PathVariable("participantId") String participantId) {
        List<TrialSession> sessions = service.fetchSessionsByParticipant(participantId);
        if (Objects.nonNull(sessions)) {
            return ResponseEntity.ok(sessions);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Get trial sessions by participant", response = TrialSession.class, responseContainer = "List")
    @RequestMapping(value = "/participant/{participantId}/status/{status}", method = RequestMethod.GET)
    public ResponseEntity<List<TrialSession>> getTrialSessionsByParticipant(
            @PathVariable("participantId") String participantId, @PathVariable("status") String sessionStatus) {
        List<TrialSession> sessions = service.fetchSessionsByParticipant(participantId, sessionStatus);
        if (Objects.nonNull(sessions)) {
            return ResponseEntity.ok(sessions);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Update trial sessions status", response = Boolean.class)
    @RequestMapping(value = "/token/{token}/status/{status}", method = RequestMethod.POST)
    public Boolean updateTrialSessionsStatus(@PathVariable("token") String token,
            @PathVariable("status") String sessionStatus) {
        return service.updateSessionByToken(token, sessionStatus);
    }
}
