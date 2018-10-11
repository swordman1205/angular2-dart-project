// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/site/widgets/site-content/site_content_component.dart';
import 'package:app_web/src/utils/contact_utils.dart';

@Component(
  selector: 'forgot-password',
  host: const {'class': 'page-component'},
  styleUrls: const ['forgot_password_component.css'],
  templateUrl: 'forgot_password_component.html',
  directives: const [
    CORE_DIRECTIVES,
    ROUTER_DIRECTIVES,
    formDirectives,
    materialDirectives,
    SiteContentComponent,
  ],
)
class ForgotPasswordComponent implements OnInit {
  final UserApi _userApi;

  ControlGroup emailForm;
  String emailValue;
  bool emailExists = true;
  bool emailSent = false;

  bool submitted = false;

  ForgotPasswordComponent(this._userApi);

  @override
  ngOnInit() {
    //forms
    emailForm = new FormBuilder().group({
      "email": ["", ContactUtils.validateEmail],
    });
  }

  Future<Null> requestResetPassword() async {
    submitted = true;
    if (!emailForm.valid) return;
    emailExists = await _userApi.checkEmailExist(emailValue);
    if (emailExists) {
      await _userApi.requestResetPassword(emailValue);
      emailSent = true;
    }
  }
}
