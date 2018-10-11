// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class GatewayApiTestDataImpl extends GatewayApiBase implements GatewayApi {

  GatewayApiTestDataImpl(ApiClient client) : super(client);

  @override
  Future<Null> downloadSamplesZip(List<UserSampleId> request) async {
    return null;
  }

  @override
  Future<Null> downloadCustomersAndHealths() async {
    return null;
  }

}