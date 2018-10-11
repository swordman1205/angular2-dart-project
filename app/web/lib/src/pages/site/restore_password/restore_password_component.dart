// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/site/widgets/site-content/site_content_component.dart';
import 'package:app_web/src/widgets/password_set/password_set_component.dart';

@Component(
  selector: 'restore-password',
  host: const {'class': 'page-component'},
  styleUrls: const ['restore_password_component.css'],
  templateUrl: 'restore_password_component.html',
  directives: const [
    CORE_DIRECTIVES,
    ROUTER_DIRECTIVES,
    formDirectives,
    materialDirectives,
    PasswordSetComponent,
    SiteContentComponent,
  ],
)
class RestorePasswordComponent {
  final UserApi _userApi;
  final RouteParams _routeParams;

  String youngPassword;

  bool passwordRestored = false;
  bool submitted = false;

  RestorePasswordComponent(this._userApi, this._routeParams);

  Future<Null> restorePassword(bool isValid) async {
    submitted = true;
    if (isValid) {
      String token = _routeParams.get("token");
      await _userApi.restorePassword(token, youngPassword);
      passwordRestored = true;
    }
  }

  resetSubmitStatus() {
    submitted = false;
    passwordRestored = false;
  }
}
