// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/site/widgets/site-content/site_content_component.dart';

@Component(
  selector: 'login',
  host: const {'class': 'page-component'},
  styleUrls: const ['login_component.css'],
  templateUrl: 'login_component.html',
  directives: const [
    CORE_DIRECTIVES,
    ROUTER_DIRECTIVES,
    materialDirectives,
    SiteContentComponent,
    formDirectives,
  ],
)
class LoginComponent {
  final UserApi _userApi;
  final Router _router;
  String errorMessage = "";
  bool submitted = false;
  ControlGroup loginForm;

  dynamic user = {};

  LoginComponent(this._userApi, this._router) {
    loginForm = new FormBuilder().group({
      "email": [""],
      "password": [""],
    });
  }

  Future<Null> login() async {
    submitted = true;
    if (!loginForm.valid) return;
    errorMessage = "";
    try {
      var loginUser = await _userApi.login(loginForm.controls['email'].value, loginForm.controls['password'].value);
      var navigateTo = getNavigateTo(loginUser.role);
      _router.navigate([ navigateTo ]);
    } catch (e) {
      errorMessage = e.toString();
      print(e);
    }
  }

  String getNavigateTo(String role) {
    if (role == 'ADMIN') {
      return '/Admin/Users';
    } else if (role == 'CUSTOMER') {
      return '/Customer/Home';
    } else if (role == 'MEDICAL') {
      return '/Medical/Customers';
    } else if (role == 'LAB_TECH') {
      return '/Lab/Home';
    } else if (role == 'NURSE') {
      return '/Nurse/Sessions';
    }
    throw 'unknown role';
  }
}
