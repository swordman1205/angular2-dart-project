// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:html' as html;

import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_facade/src/testdata/test_data_creator.dart';
import 'package:app_web/app_component.dart';
import 'package:app_web/src/api/account_api_impl.dart';
import 'package:app_web/src/api/sample_api_impl.dart';
import 'package:app_web/src/services/api_client_impl.dart';
import 'package:app_web/src/api/assay_api_impl.dart';
import 'package:app_web/src/api/gateway_api_impl.dart';
import 'package:app_web/src/api/health_api_impl.dart';
import 'package:app_web/src/api/inventory_api_impl.dart';
import 'package:app_web/src/api/trial_api_impl.dart';
import 'package:app_web/src/api/user_api_impl.dart';
import 'package:app_web/src/services/security_service_impl.dart';
import 'package:app_web/src/services/url_builder_impl.dart';
import 'package:http/browser_client.dart';
import 'package:http/http.dart';

const BackendType _backendType = BackendType.MOCK;

void main() {
  print("Backend type: $_backendType");
  if (_backendType == BackendType.MOCK) {
    TestDataCreator.createTestData(BackendType.MOCK).then((_) => initBootstrap());
  } else {
    initBootstrap();
  }
}

void initBootstrap() {
  bootstrap(AppComponent, [
    ROUTER_PROVIDERS,
    [
      provide(LocationStrategy, useClass: HashLocationStrategy),
      // same for dev and production
      provide(html.Storage, useValue: html.window.sessionStorage),
      provide(Client, useFactory: () {
        var client = new BrowserClient();
        return client;
      }, deps: []),
      provide(ApiClient, useClass: ApiClientImpl),
      provideUrlBuilder(),
      provideSecurityService(),
      // api
      provideGatewayApi(),
      provideHealthApi(),
      provideSampleApi(),
      provideAssayApi(),
      provideTrialApi(),
      provideInventoryApi(),
      provideUserApi(),
      provideAccountApi()
    ]
  ]);
}

Provider<UrlBuilder> provideUrlBuilder() {
  if (_backendType == BackendType.PRODUCTION) {
    return provide(UrlBuilder, useClass: ProductionUrlBuilder);
  } else {
    return provide(UrlBuilder, useClass: EmulatorUrlBuilder);
  }
}

Provider<GatewayApi> provideGatewayApi() {
  if (_backendType == BackendType.MOCK) {
    return provide(GatewayApi, useValue: new TestDataInjector(BackendType.MOCK).gatewayApi);
  } else {
    return provide(GatewayApi, useClass: GatewayApiImpl);
  }
}

Provider<HealthApi> provideHealthApi() {
  if (_backendType == BackendType.MOCK) {
    return provide(HealthApi, useValue: new TestDataInjector(BackendType.MOCK).healthApi);
  } else {
    return provide(HealthApi, useClass: HealthApiImpl);
  }
}

Provider<SampleApi> provideSampleApi() {
  if (_backendType == BackendType.MOCK) {
    return provide(SampleApi, useValue: new TestDataInjector(BackendType.MOCK).sampleApi);
  } else {
    return provide(SampleApi, useClass: SampleApiImpl);
  }
}

Provider<HealthApi> provideAssayApi() {
  if (_backendType == BackendType.MOCK) {
    return provide(AssayApi, useValue: new AssayApiMock());
  } else {
    return provide(AssayApi, useClass: AssayApiImpl);
  }
}

Provider<TrialApi> provideTrialApi() {
  if (_backendType == BackendType.MOCK) {
    return provide(TrialApi, useValue: new TestDataInjector(_backendType).trialApi);
  } else {
    return provide(TrialApi, useClass: TrialApiImpl);
  }
}

Provider<InventoryApi> provideInventoryApi() {
  if (_backendType == BackendType.MOCK) {
    return provide(InventoryApi, useValue: new TestDataInjector(_backendType).inventoryApi);
  } else {
    return provide(InventoryApi, useClass: InventoryApiImpl);
  }
}

Provider<SecurityService> provideSecurityService() {
  if (_backendType == BackendType.MOCK) {
    return provide(SecurityService, useValue: new SecurityServiceMock());
  } else {
    return provide(SecurityService, useClass: SecurityServiceImpl);
  }
}

Provider<UserApi> provideUserApi() {
  if (_backendType == BackendType.MOCK) {
    return provide(UserApi, useValue: new TestDataInjector(BackendType.MOCK).userApi);
  } else {
    return provide(UserApi, useClass: UserApiImpl);
  }
}

Provider<AccountApi> provideAccountApi() {
  if (_backendType == BackendType.MOCK) {
    return provide(AccountApi, useValue: new TestDataInjector(BackendType.MOCK).accountApi);
  } else {
    return provide(AccountApi, useClass: AccountApiImpl);
  }
}


