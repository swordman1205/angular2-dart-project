// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:html';
import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_web/src/pages/site/email_confirm/email_confirm_component.dart';
import 'package:app_web/src/pages/site/forgot_password/forgot_password_component.dart';
import 'package:app_web/src/pages/site/home/home_component.dart';
import 'package:app_web/src/pages/site/login/login_component.dart';
import 'package:app_web/src/pages/site/register/register_component.dart';
import 'package:app_web/src/pages/site/restore_password/restore_password_component.dart';
import 'package:app_web/src/pages/site/session_status_change/session_status_change_component.dart';
import 'package:app_web/src/pages/site/sorry/sorry_component.dart';
import 'package:app_web/src/pages/site/thanks_register/thanks_register_component.dart';
import 'package:app_web/src/pages/site/navbar_service.dart';
import 'package:app_web/src/pages/site/widgets/navbar/navbar_component.dart';

@Component(
  selector: 'site',
  host: const {'class': 'page page--external'},
  styleUrls: const ['../page_external.css', 'site_page.css'],
  templateUrl: 'site_page.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
    NavBarComponent
  ],
  providers: const [
    NavBarService,
  ]
)
@RouteConfig(const [
  const Route(path: '/forgotPassword', name: 'ForgotPassword', component: ForgotPasswordComponent),
  const Route(path: '/home', name: 'Home', component: HomeComponent),
  const Route(path: '/login', name: 'Login', component: LoginComponent),
  const Route(path: '/sorry', name: 'Sorry', component: SorryComponent),
  const Route(path: '/thanks-register', name: 'ThanksRegister', component: ThanksRegisterComponent),
  const Route(path: '/register/...', name: 'Register', component: RegisterComponent),
  const Route(path: '/restorePassword/:token', name: 'RestorePassword', component: RestorePasswordComponent),
  const Route(path: '/emailConfirm/:token', name: 'EmailConfirm', component: EmailConfirmComponent),
  const Route(path: '/sessionStatusChange/:token/:status', name: 'SessionStatusChange', component: SessionStatusChangeComponent),
])
class SitePage implements DoCheck {
  final NavBarService navBarService;

  SitePage(this.navBarService);

  String get theme => navBarService.navBarTheme;

  @override
  void ngDoCheck() {
    navBarService.currentPage = (window.location.hash).substring("#/site/".length);
  }
}
