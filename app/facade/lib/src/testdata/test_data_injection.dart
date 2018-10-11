// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

/// Simple DI
/// https://medium.com/@develodroid/flutter-iv-mvp-architecture-e4a979d9f47e
class TestDataInjector {
  static TestDataInjector _instance;

  final AccountApi accountApi;
  final AssayApi assayApi;
  final ConsentApi consentApi;
  final GatewayApi gatewayApi;
  final HealthApi healthApi;
  final InventoryApi inventoryApi;
  final SampleApi sampleApi;
  final TrialApi trialApi;
  final UserApi userApi;

  factory TestDataInjector(BackendType backendType, {String url = "http://test.qurasense.com"}) {
    if(_instance == null) {
      if (backendType == null) {
        throw "backend type should be configured at first";
      }
      dynamic client;
      if (backendType != BackendType.MOCK) {
        client = new IOClient();
      }
      var urlBuilder = new TestDataUrlBuilder(url);
      var securityService = (backendType == BackendType.MOCK) ? new SecurityServiceMock() : new SecurityServiceTestDataImpl(client, {}, urlBuilder);
      var apiClient = new ApiClientTestDataImpl(client, securityService, urlBuilder);

      var accountApi = (backendType == BackendType.MOCK) ? new AccountApiMock() : new AccountApiBase(apiClient);
      var assayApi = (backendType == BackendType.MOCK) ? new AssayApiMock() : new AssayApiBase(apiClient);
      var consentApi = (backendType == BackendType.MOCK) ? new ConsentApiMock() : new ConsentApiBase(apiClient);
      var gatewayApi = (backendType == BackendType.MOCK) ? new GatewayApiMock() :  new GatewayApiTestDataImpl(apiClient);
      var healthApi = (backendType == BackendType.MOCK) ?  new HealthApiMock() : new HealthApiTestDataImpl(apiClient, securityService);
      var inventoryApi = (backendType == BackendType.MOCK) ? new InventoryApiMock() : new InventoryApiBase(apiClient);
      var sampleApi = (backendType == BackendType.MOCK) ? new SampleApiMock() : new SampleApiTestDataImpl(apiClient);
      var trialApi = (backendType == BackendType.MOCK) ? new TrialApiMock() : new TrialApiBase(apiClient);
      var userApi = (backendType == BackendType.MOCK) ? new UserApiMock() : new UserApiTestDataImpl(apiClient, securityService);
      
      _instance = new TestDataInjector._internal(accountApi, assayApi, consentApi, gatewayApi, healthApi, inventoryApi, sampleApi, trialApi, userApi);
    }
    return _instance;
  }

  TestDataInjector._internal(this.accountApi, this.assayApi, this.consentApi, this.gatewayApi, this.healthApi, this.inventoryApi, this.sampleApi, this.trialApi, this.userApi);
}