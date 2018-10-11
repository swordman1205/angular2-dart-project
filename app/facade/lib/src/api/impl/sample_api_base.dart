// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

abstract class SampleApiBase implements SampleApi {
  final ApiClient _client;

  SampleApiBase(this._client);

  @override
  Future<StripSample> getSampleById(String sampleId) async {
    Response sampleResponse = await _client.get("/health_api/samples/$sampleId");
    if (sampleResponse.statusCode != 200) {
      throw new ErrorInfo(sampleResponse.body);
    }
    Map<String, dynamic> map = JSON.decode(sampleResponse.body);
    return MappingUtils.fromJsonSample(map);
  }

  @override
  Future<String> saveStripSample(StripSample sample) async {
    var encoded = JSON.encode(sample, toEncodable: (StripSample s) => MappingUtils.toJsonSample(s));
    var response = await _client.post("/health_api/sample/stripSample", body: encoded);
    if (response.statusCode == 200) {
      sample.id = response.body;
      return sample.id;
    } else {
      print(response.statusCode);
      print(response.body);
      throw new ErrorInfo(response.body);
    }
  }

  @override
  Future<String> saveTubeSample(TubeSample sample) async {
    var encoded = JSON.encode(sample, toEncodable: (TubeSample s) => MappingUtils.toJsonTubeSample(s));
    var response = await _client.post("/health_api/sample/tubeSample", body: encoded);
    if (response.statusCode == 200) {
      sample.id = response.body;
      return sample.id;
    } else {
      print(response.statusCode);
      print(response.body);
      throw new ErrorInfo(response.body);
    }
  }

  @override
  Future<StripSample> getLatestNotFinishedSample(String userId) async {
    var response = await _client.get("/health_api/$userId/sample/latestNotFinished");
    if (response.statusCode == 200) {
      Map<String, dynamic> map = JSON.decode(response.body);
      return MappingUtils.fromJsonSample(map);
    } else if (response.statusCode == 404) {
      return null;
    } else if (response.statusCode == 500) {//TODO: remove workaround
      return null;
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<List<SampleDevice>> getSamples(String userId) async {
    var response = await _client.get("/health_api/samples", params: {'customerId': userId});
    if (response.statusCode == 200) {
      List<dynamic> samplesJson = JSON.decode(response.body);
      return samplesJson.map((map) =>
        map['primaryActivities'] != null ? MappingUtils.fromJsonSample(map) : MappingUtils.fromJsonTubeSample(map)
      ).toList();
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<Iterable<SampleDevice>> getSampleByIds(List<String> sampleIds) async {
    var response = await _client.get("/health_api/samplesById", params: {'sampleId': sampleIds});
    if (response.statusCode == 200) {
      List<dynamic> samplesJson = JSON.decode(response.body);
      return samplesJson.map((map) =>
      map['primaryActivities'] != null ? MappingUtils.fromJsonSample(map) : MappingUtils.fromJsonTubeSample(map)
      ).toList();
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<String> uploadRemovePicture(String userId, String sampleId, MultipartFile file) async {
    return _client.uploadFile("/health_api/$userId/sample/$sampleId/removePicture", file);
  }

  @override
  Future<String> uploadPullPicture(String userId, String sampleId, MultipartFile file) async {
    return _client.uploadFile("/health_api/$userId/sample/$sampleId/pullPicture", file);
  }


}