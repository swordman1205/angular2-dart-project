package com.qurasense.labApi.trial.service;

import java.util.List;

import com.qurasense.labApi.trial.model.ParticipantStatus;
import com.qurasense.labApi.trial.model.TrialParticipant;

public interface TrialParticipantService {

    String createTrialParticipant(TrialParticipant trialParticipant);

    TrialParticipant findTrialParticipantById(String id);

    List<TrialParticipant> fetchTrialParticipantsByIds(List<String> participantIds);

    List<TrialParticipant> fetchTrialParticipants(String customerId);

    List<TrialParticipant> fetchTrialParticipants(String customerId, ParticipantStatus status);

    List<TrialParticipant> fetchTrialParticipantsByNurseId(String nurseId, List<String> sessionStatuses);

    boolean updateParticipantStatus(String participantId, String status);

    boolean assignNurse(String participantId, String nurseId);

    void addSampleToSession(String customerId, String sampleId);

    boolean sendKits(String participantId);


}
