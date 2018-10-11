// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class UserApiTestDataImpl extends UserApiBase {
  ApiClient _client;
  SecurityService _securityService;

  UserApiTestDataImpl(this._client, this._securityService) : super(_client, _securityService);
  @override
  Future<Null> downloadCustomers() async {
    return null;
  }

  @override
  Future<User> login(String email, String password) async {
    Map<String, String> formParams = {
      "username": email,
      "password": password,
      "grant_type": "password"
    };
    Map<String, String> headers = {
      "Authorization": "Basic YnJvd3Nlcjpicm93c2Vyc2VjcmV0",
      "Accept": "application/json",
      "Content-Type": "application/x-www-form-urlencoded"
    };

    var response;
    try {
      response = await _client.post("/user_api/oauth/token", headers: headers, body: formParams);
    } catch (e) {
      throw "connection error";
    }

    if (response.statusCode == 400) {
      Map<String, dynamic> map = JSON.decode(response.body);
      var error = map['error'];
      var message = map['error_description'];
      print("authentication error: $error, message: $message");
      throw message;
    } else if (response.statusCode != 200) {
      print(response.body);
      throw "login error, REST access error";
    }
    var responseData = JSON.decode(response.body);
    var userId = responseData['userId'].toString();
    var accessToken = responseData['access_token'].toString();
    var refreshToken = responseData['refresh_token'].toString();
    _securityService.setTokens(userId,accessToken,refreshToken);
    User currentUser = await _securityService.getCurrentUser();
    return currentUser;
  }
}