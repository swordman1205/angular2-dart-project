// Copyright (c) 2017, Qurasense. All rights reserved.
part of app_facade;

abstract class GatewayApi {
  Future<Null> ping();

  Future<Null> downloadSamplesZip(List<UserSampleId> request);

  Future<String> getVersion();

  Future<String> signup(UserData userDate);

  Future<Null> downloadCustomersAndHealths();

}

class UserSampleId {
  String userId;
  String sampleId;
  UserSampleId(this.userId, this.sampleId);
  Map<String, String> toJson() {
    Map<String, String> map = new Map();
    map["userId"] = userId.toString();
    map["sampleId"] = sampleId.toString();
    return map;
  }
}

enum BackendType {
  MOCK, EMULATOR, PRODUCTION
}