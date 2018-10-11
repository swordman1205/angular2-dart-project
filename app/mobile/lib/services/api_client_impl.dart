import 'package:app_facade/app_facade.dart';
import 'package:http/src/client.dart';

class ApiClientImpl extends ApiClient {
  final UrlBuilder _urlBuilder;

  ApiClientImpl(Client httpClient, SecurityService securitySevice, this._urlBuilder) : super(httpClient, securitySevice);

  @override
  String buildUrl(String aPath) {
    return _urlBuilder.buildUrl(aPath);
  }
}