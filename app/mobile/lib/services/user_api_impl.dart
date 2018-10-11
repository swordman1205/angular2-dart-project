// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:convert';

import 'package:app_facade/app_facade.dart';
import 'package:http/http.dart';

class UserApiImpl extends UserApiBase {

  ApiClient _client;
  SecurityService _securityService;
  UserApiImpl(this._client, SecurityService _securityService) : super(_client, _securityService);

  // https://webdev.dartlang.org/angular/guide/forms
  @override
  Future<User> login(String email, String password) async {
    Map<String, String> formParams = {
      "username": email,
      "password": password,
      "grant_type": "password",
      "scope": "mobile"
    };
    Map<String, String> headers = {
      "Authorization": "Basic bW9iaWxlOm1vYmlsZXNlY3JldA==",
      "Accept": "application/json",
      "Content-Type": "application/x-www-form-urlencoded"
    };

    var response;
    try {
      response = await _client.post("/user_api/oauth/token",
          headers: headers, body: formParams);
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

  @override
  Future<Null> downloadCustomers() async {
    //download file
  }

}
