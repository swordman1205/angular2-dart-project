package com.qurasense.labApi.trial.service;

import java.util.List;

import com.qurasense.labApi.trial.model.TrialSession;

public interface TrialSessionService {
    List<TrialSession> fetchSessions(String trialId);

    List<TrialSession> fetchSessions(String trialId, String sessionStatus);

    boolean updateSessions(String trialId, String sessionStatus);

    List<TrialSession> fetchSessionsByParticipant(String participantId);

    List<TrialSession> fetchSessionsByParticipant(String participantId, String sessionStatus);

    boolean updateSessionByToken(String token, String sessionStatus);

    TrialSession find(String id);
}
