// Copyright (c) 2017, Qurasense. All rights reserved.
part of app_facade;

abstract class SampleApi {
  Future<String> saveStripSample(StripSample sample);

  Future<String> saveTubeSample(TubeSample sample);

  Future<StripSample> getSampleById(String sampleId);

  Future<Iterable<SampleDevice>> getSamples(String userId);

  Future<Iterable<SampleDevice>> getSampleByIds(List<String> sampleIds);

  Future<String> uploadRemovePicture(String userId, String sampleId, MultipartFile file);

  Future<String> uploadPullPicture(String userId, String sampleId, MultipartFile file);

  Future<StripSample> getLatestNotFinishedSample(String userId);

}