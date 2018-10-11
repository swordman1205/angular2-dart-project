package com.qurasense.labApi.app.pubsub;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.pubsub.v1.PubsubMessage;
import com.qurasense.common.messaging.broadcast.message.SampleRegistered;
import com.qurasense.common.messaging.broadcast.message.UserDeleted;
import com.qurasense.common.messaging.broadcast.subscriber.BroadcastMessageReceiver;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.labApi.trial.model.TrialParticipant;
import com.qurasense.labApi.trial.service.TrialParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LabBroadcastMessageReceiver extends BroadcastMessageReceiver {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TrialParticipantService trialParticipantService;

    @Autowired
    private EntityRepository entityRepository;

    @Override
    protected void process(String jsonString, String type, PubsubMessage message) throws IOException {
        if (SampleRegistered.class.getName().equals(type)) {
            processSampleRegistered(jsonString);
        } else if (UserDeleted.class.getName().equals(type)) {
            processUserDeleted(jsonString);
        } else {
            logger.info("ignore broadcast event, type: {}", type);
        }
    }

    private void processUserDeleted(String jsonString) throws IOException {
        UserDeleted userDeleted = objectMapper.readValue(jsonString, UserDeleted.class);
        if (!"CUSTOMER".equalsIgnoreCase(userDeleted.getRole())) {
            logger.info("ignore user deleted, role: {}", userDeleted.getRole());
            return;
        }
        List<TrialParticipant> participants = trialParticipantService.fetchTrialParticipants(userDeleted.getUserId());
        for (TrialParticipant tp: participants) {
            entityRepository.delete(tp);
            logger.info("deleted participant, userId: {} participantId: {}", userDeleted.getUserId(), tp.getId());
        }
    }

    private void processSampleRegistered(String jsonString) throws IOException {
        SampleRegistered sampleRegistered = objectMapper.readValue(jsonString, SampleRegistered.class);
        trialParticipantService.addSampleToSession(sampleRegistered.getCustomerId(), sampleRegistered.getSampleId());
    }

}
