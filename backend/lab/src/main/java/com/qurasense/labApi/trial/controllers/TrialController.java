package com.qurasense.labApi.trial.controllers;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import com.qurasense.labApi.trial.model.Trial;
import com.qurasense.labApi.trial.model.TrialSession;
import com.qurasense.labApi.trial.service.TrialService;
import com.qurasense.labApi.trial.service.TrialSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = TrialController.URL)
@Api(value="Trials", description="Trial api")
public class TrialController {
    public static final String URL= "/trials";
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TrialService service;

    @Autowired
    private TrialSessionService trialSessionService;

    @ApiOperation(value = "create trial")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Trial trial) {
        String id = service.createTrial(trial);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).body(id);
    }

    @ApiOperation(value = "Get trial", response = Trial.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Trial> get(@PathVariable("id") String id) {
        Trial trial = service.findTrialById(id);
        if (Objects.nonNull(trial)) {
            return ResponseEntity.ok(trial);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "List trials", response = Trial.class, responseContainer = "List",
            authorizations = @Authorization(
                    value = "oauth2",
                    scopes = {
                            @AuthorizationScope(
                                    scope = "Only Admin can list all trials", description = ""
                            )
                    }
            ))
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Trial> list() {
        return service.getTrials();
    }

    @ApiOperation(value = "Update trial", response = Trial.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Trial> update(@PathVariable("id") String id, @RequestBody Trial trial) {
        service.createTrial(trial);
        return null;
    }

    @ApiOperation(value = "Get trial sessions", response = TrialSession.class, responseContainer = "List")
    @RequestMapping(value = "/{trialId}/sessions", method = RequestMethod.GET)
    public ResponseEntity<List<TrialSession>> fetchTrialSessions(@PathVariable("trialId") String trialId) {
        List<TrialSession> trialSessions = trialSessionService.fetchSessions(trialId);
        if (Objects.nonNull(trialSessions)) {
            return ResponseEntity.ok(trialSessions);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Get trial sessions", response = TrialSession.class, responseContainer = "List")
    @RequestMapping(value = "/{trialId}/sessions/status/{status}", method = RequestMethod.GET)
    public ResponseEntity<List<TrialSession>> fetchTrialSessions(@PathVariable("trialId") String trialId,
            @PathVariable("status") String sessionStatus) {
        List<TrialSession> trialSessions = trialSessionService.fetchSessions(trialId, sessionStatus);
        if (Objects.nonNull(trialSessions)) {
            return ResponseEntity.ok(trialSessions);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Update trial sessions status", response = boolean.class)
    @RequestMapping(value = "/{trialId}/sessions/status/{status}", method = RequestMethod.POST)
    public boolean updateTrialSessionsStatus(@PathVariable("trialId") String trialId,
            @PathVariable("status") String sessionStatus) {
        return trialSessionService.updateSessions(trialId, sessionStatus);
    }

}
