// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

abstract class SecurityServiceBase implements SecurityService {
  final Client _http;
  final UrlBuilder _urlBuilder;
  User _currentUser;
  Map<String, String> storage;

  SecurityServiceBase(this._http, this.storage, this._urlBuilder);

  @override
  void clearTokens() {
    storage.clear();
  }

  @override
  String getAccessToken() {
    return storage["accessToken"];
  }

  @override
  Future<User> getCurrentUser() async {
    if (_hasUserToken && _currentUser == null) {
      var headers = {};
      headers.addAll( {'Authorization': 'Bearer ${getAccessToken()}'} );
      headers.addAll( {'Content-Type': 'application/json'} );
      var userId = storage["userId"];

      final userResponse = await _http.get(_urlBuilder.buildUrl("/user_api/user/$userId"), headers: headers);
      if (userResponse.statusCode == 200) {
        var userJson = userResponse.body;
        Map<String, dynamic> map = JSON.decode(userJson);
        _currentUser = MappingUtils.fromJsonUser(map);
        print("received user from backend");
      } else {
        throw userResponse.body;
      }
    }
    return _currentUser;
  }

  @override
  Future<String> getCurrentUserId() async {
    // this has to use getCurrentUser to force loading of user, do not use sessionStorage directly
    var user = await getCurrentUser();
    return user.id;
  }

  @override
  void setTokens(String userId, String accessToken, String refreshToken) {
    storage["userId"] = userId;
    storage["accessToken"] = accessToken;
    storage["refreshToken"] = refreshToken;
    _currentUser = null;
  }

  bool get _hasUserToken {
    return storage.containsKey("accessToken") && storage.containsKey("userId");
  }
}