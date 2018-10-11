// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class TrialApiBase implements TrialApi {
  final ApiClient _client;

  TrialApiBase(this._client);

  @override
  Future<String> addSampleToSession(String customerId, String sampleId) {
    // TODO: implement addSampleToSession
  }

  @override
  Future<String> assignCustomerToTrial(String customerId, String trialId) async {
    Response response = await _client.post("/lab_api/trial/participants/customer/$customerId/trial/$trialId");
    if (response.statusCode == 200) {
      return response.body;
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<bool> assignNurseToParticipant(String participantId, String nurseId) async {
    Response response = await _client.post("/lab_api/trial/participants/$participantId/nurse/$nurseId");
    if (response.statusCode == 200) {
      return response.body.toLowerCase() == 'true';
    }
    print(response.body);
    throw new ErrorInfo(response.body);
  }

  @override
  Future<Iterable<TrialParticipant>> getCustomerTrialParticipation(String customerId, {String status}) async {
    Response trialParticipantResponse;
    if (status == null) {
      trialParticipantResponse = await _client.get("/lab_api/trial/participants/customer/$customerId");
    } else {
      trialParticipantResponse = await _client.get("/lab_api/trial/participants/customer/$customerId/status/$status");
    }

    List<dynamic> trialParticipantsJson = JSON.decode(trialParticipantResponse.body);
    return trialParticipantsJson.map((json) => MappingUtils.fromJsonTrialParticipant(json)).toList();
  }

  @override
  Future<Iterable<TrialSession>> getSessionsForCustomer(String customerId) {
    // TODO: implement getSessionsForCustomer
  }

  @override
  Future<Iterable<TrialSession>> getSessionsForParticipant(String participantId, {String status}) async {
    Response sessionsResponse;
    if (status == null) {
      sessionsResponse = await _client.get("/lab_api/trial/sessions/participant/$participantId/");
    } else {
      sessionsResponse = await _client.get("/lab_api/trial/sessions/participant/$participantId/status/$status");
    }
    if (sessionsResponse.statusCode != 200) {
      throw new ErrorInfo(sessionsResponse.body);
    }
    List<dynamic> trialSessionsJson = JSON.decode(sessionsResponse.body);
    return trialSessionsJson.map((json) => MappingUtils.fromJsonTrialSession(json)).toList();
  }

  @override
  Future<Trial> getTrialById(String id) async {
    Response trialResponse = await _client.get("/lab_api/trials/$id");
    if (trialResponse.statusCode != 200) {
      throw new ErrorInfo(trialResponse.body);
    }
    Map<String, dynamic> map = JSON.decode(trialResponse.body);
    return MappingUtils.fromJsonTrial(map);
  }

  @override
  Future<TrialParticipant> getTrialParticipantById(String participantId) async {
    Response trialParticipantResponse = await _client.get("/lab_api/trial/participants/$participantId");
    if (trialParticipantResponse.statusCode != 200) {
      throw new ErrorInfo(trialParticipantResponse.body);
    }
    Map<String, dynamic> map = JSON.decode(trialParticipantResponse.body);
    return MappingUtils.fromJsonTrialParticipant(map);
  }

  @override
  Future<Iterable<TrialParticipant>> getTrialParticipantByNurseId(String nurseId, {List<String> allowedStatuses}) async {
    Response participantsResponse = await _client.get("/lab_api/trial/participants/nurse/$nurseId",
        params: {'sessionStatuses': allowedStatuses});
    if (participantsResponse.statusCode != 200) {
      throw new ErrorInfo(participantsResponse.body);
    }
    List<dynamic> trialParticipantsJson = JSON.decode(participantsResponse.body);
    return trialParticipantsJson.map((json) => MappingUtils.fromJsonTrialParticipant(json)).toList();
  }

  Future<Iterable<TrialParticipant>> getTrialParticipantsByIds(Iterable<String> participantIds) async {
    Response participantsResponse = await _client.get("/lab_api/trial/participants/byIds",
        params: {'participantId': participantIds});
    if (participantsResponse.statusCode != 200) {
      throw new ErrorInfo(participantsResponse.body);
    }
    List<dynamic> trialParticipantsJson = JSON.decode(participantsResponse.body);
    return trialParticipantsJson.map((json) => MappingUtils.fromJsonTrialParticipant(json)).toList();
  }

  @override
  Future<Iterable<TrialParticipant>> getTrialParticipants(String trialId) {
    // TODO: implement getTrialParticipants
  }

  @override
  Future<Iterable<Trial>> getTrials() async {
    var trialsResponse = await _client.get("/lab_api/trials/");
    List<dynamic> trialsJson = JSON.decode(trialsResponse.body);
    return trialsJson.map((json) => MappingUtils.fromJsonTrial(json)).toList();
  }

  @override
  Future<Iterable<TrialSession>> getTrialSessions(String trialId, {String status}) async {
    Response sessionsResponse;
    if (status == null) {
      sessionsResponse = await _client.get("/lab_api/trials/$trialId/sessions/");
    } else {
      sessionsResponse = await _client.get("/lab_api/trials/$trialId/sessions/status/$status");
    }
    if (sessionsResponse.statusCode != 200) {
      throw new ErrorInfo(sessionsResponse.body);
    }
    List<dynamic> trialParticipantsJson = JSON.decode(sessionsResponse.body);
    return trialParticipantsJson.map((json) => MappingUtils.fromJsonTrialSession(json)).toList();
  }

  @override
  Future<String> saveTrial(Trial trial) async {
    var encoded = JSON.encode(trial, toEncodable: MappingUtils.toJsonTrial);
    var response = await _client.post("/lab_api/trials/", body: encoded);
    if (response.statusCode != 201) {
      throw new ErrorInfo(response.body);
    }
    return response.body;
  }

  @override
  Future<bool> sendKits(String participantId) async {
    Response response = await _client.post("/lab_api/trial/participants/$participantId/sendKits");
    if (response.statusCode == 200) {
      return response.body.toLowerCase() == 'true';
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<bool> updateParticipantStatus(String participantId, String status) async {
    Response response = await _client.post("/lab_api/trial/participants/$participantId/status/$status");
    if (response.statusCode == 200) {
      return response.body.toLowerCase() == 'true';
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<bool> updateTrialSessionsStatus(String trialId, String sessionStatus) async {
    Response sessionsResponse = await _client.post("/lab_api/trials/$trialId/sessions/status/$sessionStatus");
    if (sessionsResponse.statusCode != 200) {
      throw new ErrorInfo(sessionsResponse.body);
    }
    return sessionsResponse.body.toLowerCase() == 'true';
  }

  @override
  Future<bool> updateTrialSessionStatusByToken(String token, String status) async {
    Response sessionsResponse = await _client.post("/lab_api/trial/sessions/token/$token/status/$status", defaults: []);
    if (sessionsResponse.statusCode != 200) {
      throw new ErrorInfo(sessionsResponse.body);
    }
    return sessionsResponse.body.toLowerCase() == 'true';
  }

  /*
  @override
  Future<List<StripSample>> getSampleForPickup() async {
    User nurse = await _securityService.getCurrentUser();
    var response = await _client.get("/health_api/sample/nurse/${nurse.id}/pickup");
    if (response.statusCode == 200) {
      List<dynamic> samplesJson = JSON.decode(response.body);
      return samplesJson.map((map) => MappingUtils.fromJsonSample(map)).toList();
    }
    throw new ErrorInfo(response.body);
  }
  */

  @override
  Future<TrialSession> getSession(String sessionId) async {
    Response sessionResponse = await _client.get("/lab_api/trial/sessions/$sessionId");
    if (sessionResponse.statusCode != 200) {
      throw new ErrorInfo(sessionResponse.body);
    }
    Map<String, dynamic> map = JSON.decode(sessionResponse.body);
    return MappingUtils.fromJsonTrialSession(map);
  }
}