import 'package:app_facade/app_facade.dart';
import 'package:app_mobile/services/api_client_impl.dart';
import 'package:app_mobile/services/security_service_impl.dart';
import 'package:app_mobile/services/user_api_impl.dart';
import 'package:app_mobile/utils/mobile_url_builder.dart';
import 'package:http/http.dart' as http;

/// Simple DI
/// https://medium.com/@develodroid/flutter-iv-mvp-architecture-e4a979d9f47e
class Injector {
  static Injector _instance;

  SecurityService securityService;
  UserApi userApi;

  factory Injector({BackendType backendType}) {
    if (_instance == null) {
      var client = new http.Client();
      var mobileUrlBuilder = (backendType == BackendType.PRODUCTION) ? new MobileUrlBuilder("https://trial.qurasense.com") : new MobileUrlBuilder("http://192.168.1.71:8080");
      var securityService = (backendType == BackendType.MOCK) ? new SecurityServiceMock() : new SecurityServiceImpl(client, {}, mobileUrlBuilder);
      var apiClient = new ApiClientImpl(client, securityService, mobileUrlBuilder);
      var userApi = (backendType == BackendType.MOCK) ? new UserApiMock() : new UserApiImpl(apiClient, securityService);

      _instance = new Injector._internal(securityService, userApi);
    }
    return _instance;
  }

  Injector._internal(this.securityService, this.userApi);
}