// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class HealthApiMock implements HealthApi {
  List<HealthInfo> _healthInfos = [];
  List<HealthCard> _healthCards = [];
  List<StripSample> _samples = [];
  List<HealthRecord> _healthRecords = [];

  @override
  Future<DateTime> getLastPeriodDate(String userId) async {
    return new DateTime.now();
  }

  @override
  Future<Null> submitLastPeriodDate(String userId, DateTime lastPeriodDate) {
    print("mock submited: $lastPeriodDate");
  }

  @override
  Future<HealthInfo> getHealthInfo(String userId) async {
    return _healthInfos.firstWhere((h) => h.userId == userId, orElse: () => null);
  }

  @override
  Future<Null> saveHealthInfo(HealthInfo healthInfo) {
    if (healthInfo.id == null) {
      healthInfo.id = _nextId();
    } else {
      _healthInfos.removeWhere((h) => h.id == healthInfo.id);
    }
    _healthInfos.add(healthInfo);
  }

  @override
  Future<Null> createCard(HealthCard card) {
    _healthCards.add(card);
  }

  @override
  Future<Iterable<HealthCard>> getCards(String userId) async {
    return _healthCards.where((hc) => hc.userId == userId);
  }

  @override
  Future<List<StripSample>> getSamples(String userId) async {
    return _samples.where((s) => s.userId == userId).toList();
  }

  @override
  Future<List<StripSample>> getAllSamples() async {
    return _samples;
  }

  @override
  Future<StripSample> getLatestNotFinishedSample(String userId) async {
    var list = _samples.where((s)=>s.customerId == userId && s.status == 'INPROGRESS').toList();
    return list.isNotEmpty ? list.first : null;
  }

  @override
  Future<Null> saveStripSample(String userId, StripSample sampleData) {
    if (sampleData.id == null) {
      sampleData.id = _nextId();
    } else {
      _samples.removeWhere((s) => s.id == sampleData.id);
    }
    _samples.add(sampleData);
  }

  @override
  Future<String> uploadPullPicture(String userId, String sampleId, MultipartFile file) {
    return new Future(() {
      return "uploadBlobName";
    });
  }
  @override
  Future<String> uploadRemovePicture(String userId, String sampleId, MultipartFile file) {
    return new Future(() {
      return "removeBlobName";
    });
  }

  @override
  Future<String> saveHealthRecord(HealthRecord healthRecord) async {
    if (healthRecord.id == null) {
      healthRecord.id = _nextId();
    } else {
      _healthRecords.removeWhere((h) => h.id == healthRecord.id);
    }
    _healthRecords.add(healthRecord);
    return healthRecord.id;
  }

  @override
  Future<HealthRecord> getHealthRecordByCustomerUserId(String userId) async {
    return _healthRecords.firstWhere((hc) => hc.customerUserId == userId);
  }
}
