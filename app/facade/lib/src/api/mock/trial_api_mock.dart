// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class TrialApiMock implements TrialApi {
  Map<String, Trial> _trials = {};
  Map<String, TrialParticipant> _participants = {};
  Map<String, TrialSession> _sessions = {};

  @override
  Future<String> addSampleToSession(String customerId, String sampleId) async {
    var participation = await getCustomerTrialParticipation(customerId, status:"APPROVED");
    if(participation.length != 1) {
      throw "customer must be active in one trial";
    }
    var participant = participation.first;
    var sessions = await getSessionsForCustomer(customerId);

    var session = sessions.firstWhere((s) => s.status == "ACTIVE", orElse: () {
      var newSession = new TrialSession();
      newSession.id = _nextId();
      newSession.compensated = false;
      newSession.status = "ACTIVE";
      newSession.trialParticipantId = participant.id;
      _sessions[newSession.id] = newSession;
      return newSession;
    });
    session.sampleIds.add(sampleId);
    return session.id;
  }

  @override
  Future<String> assignCustomerToTrial(String customerId, String trialId) async {
    var participant = new TrialParticipant();
    participant.trialId = trialId;
    participant.customerId = customerId;
    participant.id = _nextUid();
    participant.status = "UNAPPROVED";
    _participants[participant.id] = participant;
    return participant.id;
  }

  @override
  Future<bool> assignNurseToParticipant(String participantId, String nurseId) async {
    var participant = await getTrialParticipantById(participantId);
    participant.nurseId = nurseId;
    return true;
  }

  @override
  Future<Iterable<TrialParticipant>> getCustomerTrialParticipation(String customerId, {String status}) async {
    var participation =  _participants.values.where((p) => p.customerId == customerId);
    if(status != null) {
      return participation.where((p) => p.status == status);
    }
    return participation;
  }

  @override
  Future<Iterable<TrialSession>> getSessionsForCustomer(String customerId) async {
    Iterable<TrialParticipant> participants = await getCustomerTrialParticipation(customerId);
    List<TrialSession> sessions = [];
    for (TrialParticipant tp in participants) {
      var participantSessions = await getSessionsForParticipant(tp.id);
      participantSessions.forEach((ps)=>sessions.add(ps));
    }
    return sessions;
  }

  @override
  Future<Iterable<TrialSession>> getSessionsForParticipant(String participantId, {String status}) async {
    return _sessions.values.where((s)=>s.trialParticipantId == participantId);
  }

  @override
  Future<Trial> getTrialById(String id) async {
    if(!_trials.containsKey(id)) {
      throw "unknown trial $id";
    }
    return _trials[id];
  }

  @override
  Future<TrialParticipant> getTrialParticipantById(String participantId) async {
    return _participants[participantId];
  }

  @override
  Future<Iterable<TrialParticipant>> getTrialParticipantByNurseId(String nurseId, {List<String> allowedStatuses}) async {
    return _participants.values.where((tp) => tp.nurseId == nurseId);
  }

  @override
  Future<Iterable<TrialParticipant>> getTrialParticipants(String trialId) async {
    return _participants.values.where((p) => p.trialId == trialId);
  }

  @override
  Future<Iterable<Trial>> getTrials() async {
    return _trials.values;
  }
  @override
  Future<Iterable<TrialSession>> getTrialSessions(String trialId, {String status}) async {
    var participants = await getTrialParticipants(trialId);

    List<TrialSession> sessions = [];
    for (TrialParticipant tp in participants) {
      var participantSessions = await getSessionsForParticipant(tp.id);
      participantSessions.forEach((ps)=>sessions.add(ps));
    }
    if (status == null) {
      return sessions;
    }
    return sessions.where((TrialSession s) => s.status == status);
  }

  @override
  Future<String> saveTrial(Trial trial) async {
    if (trial.id == null) {
      trial.id = _nextUid();
    }
    _trials[trial.id] = trial;
    return trial.id;
  }

  @override
  Future<bool> sendKits(String participantId) async {
    var participant = await getTrialParticipantById(participantId);
    if (participant == null) {
      return false;
    }
    participant.hasKits = true;
    participant.sentKitsTime = new DateTime.now();
    return true;
  }

  Future<bool> updateParticipantStatus(String participantId, String status) async {
    var participant = await getTrialParticipantById(participantId);
    participant.status = status;
    return true;
  }

  @override
  Future<bool> updateTrialSessionsStatus(String trialId, String sessionStatus) async {
    var sessions = await getTrialSessions(trialId);
    sessions.forEach((s) => s.status = sessionStatus);
  }

  @override
  Future<bool> updateTrialSessionStatusByToken(String token, String status) {
    throw "not implemented in mock";
  }

  @override
  Future<TrialSession> getSession(String sessionId) async {
    return _sessions[sessionId];
  }

  @override
  Future<Iterable<TrialParticipant>> getTrialParticipantsByIds(Iterable<String> ids) async {
    return _participants.values.where((tp) => ids.contains(tp.id));
  }

}