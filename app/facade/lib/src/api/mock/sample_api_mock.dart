// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class SampleApiMock implements SampleApi {
  Map<String, SampleDevice> _samples = {};

  @override
  Future<StripSample> getSampleById(String sampleId) async {
    return _samples[sampleId];
  }

  @override
  Future<Iterable<StripSample>> getSamples(String customerId) async {
    return _samples.values.where((ss)=>ss.customerId == customerId);
  }

  @override
  Future<String> saveStripSample(StripSample sample) async {
    if (sample.id == null) {
      sample.id = _nextUid();
      var injector = new TestDataInjector(BackendType.MOCK);
      await injector.trialApi.addSampleToSession(sample.customerId, sample.id);
    }
    _samples[sample.id] = sample;
    return sample.id;
  }
  @override
  Future<String> saveTubeSample(TubeSample sample) async {
    if (sample.id == null) {
      sample.id = _nextUid();
      var injector = new TestDataInjector(BackendType.MOCK);
      await injector.trialApi.addSampleToSession(sample.customerId, sample.id);
    }
    _samples[sample.id] = sample;
    return sample.id;
  }

  @override
  Future<StripSample> getLatestNotFinishedSample(String userId) async {
    return _samples.values.firstWhere((ss)=>ss.customerId == userId && ss.status == 'INPROGRESS', orElse: () => null);
  }

  @override
  Future<String> uploadPullPicture(String userId, String sampleId, MultipartFile file) async {
    return _nextUid();
  }

  @override
  Future<String> uploadRemovePicture(String userId, String sampleId, MultipartFile file) async {
    return _nextUid();
  }

  @override
  Future<Iterable<Object>> getSampleByIds(List<String> sampleIds) async {
    return _samples.values.where((s) => sampleIds.contains(s.id));
  }

}