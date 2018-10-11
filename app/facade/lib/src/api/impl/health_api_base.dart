// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

abstract class HealthApiBase implements HealthApi {
  final ApiClient _client;
  final SecurityService _securityService;

  HealthApiBase(this._client, this._securityService);

  @override
  Future<Null> submitLastPeriodDate(String userId, DateTime lastPeriodDate) async {
    var response = await _client.post("/health_api/$userId/period",
        body: JSON.encode({'lastPeriodDate': lastPeriodDate.millisecondsSinceEpoch}));
    if (response.statusCode != 200) {
      throw "submit period error";
    }
  }

  @override
  Future<DateTime> getLastPeriodDate(String userId) async {
    var response = await _client.get("/health_api/$userId/lastPeriod");
    if (response.statusCode == 200) {
      Map<String, dynamic> map = JSON.decode( response.body);
      return new DateTime.fromMillisecondsSinceEpoch(map['lastPeriodDate']);
    }
    throw "error while health test";
  }

  @override
  Future<HealthInfo> getHealthInfo(String userId) async {
    var response = await _client.get("/health_api/$userId/healthInfo");
    if (response.statusCode == 200) {
      Map<String, dynamic> map = JSON.decode(response.body);
      return MappingUtils.fromJsonHealthInfo(map);
    } else if (response.statusCode == 404) {
      return null;
    } else {
      throw "error loading health info";
    }
  }

  @override
  Future<Null> saveHealthInfo(HealthInfo healthInfo) async {
    var encoded = JSON.encode(healthInfo, toEncodable: MappingUtils.toJsonHealthInfo);
    var response = await _client.post("/health_api/${healthInfo.userId}/healthInfo", body: encoded);
    if (response.statusCode == 200) {
      return;
    }
    throw new ErrorInfo(response.body);
  }



  @override
  Future<Null> createCard(HealthCard card) async {
    var response = await _client.post("/health_api/${card.userId}/cards/${card.type}",
        defaults: [HeaderContent.ACCESS_TOKEN],
        body: {'note': card.note}
    );
    if (response.statusCode == 200) {
      return;
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<Iterable<HealthCard>> getCards(String userId) async {
    var response = await _client.get("/health_api/$userId/cards");
    if (response.statusCode == 200) {
      List<dynamic> cardsJson = JSON.decode(response.body);
      return cardsJson.map((json) => MappingUtils.fromJsonHealthCard(json)).toList();
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<String> saveHealthRecord(HealthRecord healthRecord) async {
    var encoded = JSON.encode(healthRecord, toEncodable: MappingUtils.toJsonHealthRecord);
    print('creating health customer, encoded string is: $encoded');
    var response = await _client.post("/health_api/healthRecord", body: encoded);
    if (response.statusCode == 200) {
      return response.body;
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<HealthRecord> getHealthRecordByCustomerUserId(String userId) async {
    var response = await _client.get("/health_api/healthRecord/customer/user/$userId");
    if (response.statusCode == 200) {
      Map<String, dynamic> map = JSON.decode(response.body);
      return MappingUtils.fromJsonHealthRecord(map);
    } else if (response.statusCode == 404) {
      print("not found health customer for user: $userId");
      return null;
    }
    throw new ErrorInfo(response.body);
  }

}