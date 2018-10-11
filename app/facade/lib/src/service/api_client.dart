// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

enum HeaderContent { ACCESS_TOKEN, JSON_CONTENT }

const List<HeaderContent> defaultHeaders = const [ HeaderContent.JSON_CONTENT, HeaderContent.ACCESS_TOKEN ];

abstract class ApiClient {
  final Client _httpClient;
  final SecurityService _securityService;

  ApiClient(this._httpClient, this._securityService);

  String buildUrl(String aPath);

  Future<Response> delete(String relativeUrl, {List<HeaderContent> defaults=defaultHeaders, Map<String,String> headers=const {}}) {
    var requestUrl = buildUrl(relativeUrl);
    return _httpClient.delete(requestUrl, headers: mergeHeaders(defaults, headers));
  }

  Future<Response> get(String relativeUrl, {List<HeaderContent> defaults=defaultHeaders, Map<String,String> headers=const {}, Map<String, dynamic> params=const {}}) {
    var requestUrl = buildUrl(relativeUrl);
    var parse = Uri.parse(requestUrl);
    Uri uri;
    if (parse.hasScheme) {
      uri = new Uri(scheme: parse.scheme, host: parse.host, port: parse.port, path: parse.path, queryParameters: params);
    } else {
      uri = new Uri(path: parse.path, queryParameters: params);
    }
    return _httpClient.get(uri, headers: mergeHeaders(defaults, headers));
  }

  Map<String, String> mergeHeaders(List<HeaderContent> defaults, Map<String, String> headers) {
    Map<String,String> headersResult = new Map();
    headersResult.addAll( _buildHeaders(defaults) );
    headersResult.addAll( headers );
    return headersResult;
  }

  Future<Response> post(String relativeUrl, {dynamic body:null, List<HeaderContent> defaults=defaultHeaders, Map<String,String> headers=const {}}) {
    var requestUrl = buildUrl(relativeUrl);
    return _httpClient.post(requestUrl, body:body, headers: mergeHeaders(defaults, headers));
  }

  Future<String> uploadFile(String relativeUrl, MultipartFile file, {List<HeaderContent> defaults=const [ HeaderContent.ACCESS_TOKEN ], Map<String,String> headers=const {}}) async {
    var requestUrl = buildUrl(relativeUrl);
    var uri = Uri.parse(requestUrl);
    var request = new MultipartRequest("POST", uri);
    request.files.add(file);
    request.headers.addAll(mergeHeaders(defaults, headers));

    var response = await _httpClient.send(request);
    if (response.statusCode == 200) {
      var responseBytes = await response.stream.bytesToString();
      return responseBytes;
    }
    var errorJson = await response.stream.bytesToString();
    throw new ErrorInfo(errorJson);
  }

  Map<String, String> _buildHeaders(List<HeaderContent> headerContent) {
    var headers = {};
    for(HeaderContent content in headerContent) {
      switch(content) {
        case HeaderContent.ACCESS_TOKEN:
          headers.addAll( {'Authorization': 'Bearer ${_securityService.getAccessToken()}'} );
          break;
        case HeaderContent.JSON_CONTENT:
          headers.addAll( {'Content-Type': 'application/json'} );
          break;
        default:
          throw "unknown header value $content";
      }
    }
    return headers;
  }

  @deprecated
  Map<String,String> getNoContentWithToken() {
    return _buildHeaders([HeaderContent.ACCESS_TOKEN]);
  }

  @deprecated
  Map<String,String> getJsonContentTypeWithToken() {
    return _buildHeaders([HeaderContent.ACCESS_TOKEN, HeaderContent.JSON_CONTENT]);
  }

  @deprecated
  Map<String,String> getMutltipartContentTypeWithToken() {
    return _buildHeaders([HeaderContent.ACCESS_TOKEN]);
  }
}

