// Copyright (c) 2017, Qurasense. All rights reserved.
part of app_facade;

abstract class TrialApi {
  Future<String> addSampleToSession(String customerId, String sampleId);

  Future<String> assignCustomerToTrial(String customerId, String trialId);

  Future<bool> assignNurseToParticipant(String participantId, String nurseId);

  Future<Iterable<TrialParticipant>> getCustomerTrialParticipation(String customerId, {String status});

  Future<Iterable<TrialSession>> getSessionsForCustomer(String customerId);

  Future<Iterable<TrialSession>> getSessionsForParticipant(String participantId, {String status});

  Future<Trial> getTrialById(String id);

  Future<TrialParticipant> getTrialParticipantById(String participantId);

  Future<Iterable<TrialParticipant>> getTrialParticipantByNurseId(String nurseId, {List<String> allowedStatuses});

  Future<Iterable<TrialParticipant>> getTrialParticipants(String trialId);

  Future<Iterable<Trial>> getTrials();

  Future<Iterable<TrialSession>> getTrialSessions(String trialId, {String status});

  Future<String> saveTrial(Trial trial);

  Future<bool> sendKits(String participantId);

  Future<bool> updateParticipantStatus(String participantId, String status);

  Future<bool> updateTrialSessionsStatus(String trialId, String sessionStatus);

  Future<bool> updateTrialSessionStatusByToken(String token, String status);

  Future<TrialSession> getSession(String sessionId);

  Future<Iterable<TrialParticipant>> getTrialParticipantsByIds(Iterable<String> ids);

}