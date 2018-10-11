// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:convert';
import 'dart:html' as html;

import 'package:angular/di.dart';
import 'package:app_facade/app_facade.dart';

@Injectable()
class UserApiImpl extends UserApiBase {
  final SecurityService _securityService;
  final ApiClient _client;

  UserApiImpl(ApiClient client, SecurityService securityService) :
        _securityService = securityService,
        _client = client,
        super(client, securityService);

  // https://webdev.dartlang.org/angular/guide/forms
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

  @override
  Future<Null> downloadCustomers() async {
    var future = await html.HttpRequest.request(_client.buildUrl("/user_api/report/customers"),
        method: "GET",
        requestHeaders: _client.getNoContentWithToken(),
        responseType: "blob");
    if (future.status == 200) {
      print("success download");
      var blob = new html.Blob([future.response], 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
      var url = html.Url.createObjectUrlFromBlob(blob);
      var anchor = new html.AnchorElement();
      anchor.href = url;
      anchor.download = 'customers.xlsx';
      anchor.click();
    }
    else throw "customers download error";
  }
}
