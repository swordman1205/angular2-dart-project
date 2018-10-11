// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class ApiClientTestDataImpl extends ApiClient {
  var _urlBuilder;

  ApiClientTestDataImpl(Client httpClient, SecurityService securityService, this._urlBuilder) : super(httpClient, securityService);

  @override
  String buildUrl(String aPath) {
    return _urlBuilder.buildUrl(aPath);
  }

}