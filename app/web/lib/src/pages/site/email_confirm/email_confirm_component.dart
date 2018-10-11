// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/site/navbar_service.dart';
import 'package:app_web/src/pages/site/widgets/site-content/site_content_component.dart';
import 'package:app_web/src/widgets/password_set/password_set_component.dart';
import 'package:app_web/src/widgets/trial_process/trial_process_component.dart';

@Component(
  selector: 'email-confirm',
  host: const {'class': 'page-component'},
  styleUrls: const ['email_confirm_component.css'],
  templateUrl: 'email_confirm_component.html',
  directives: const [
    CORE_DIRECTIVES,
    ROUTER_DIRECTIVES,
    formDirectives,
    materialDirectives,
    SiteContentComponent,
    TrialProcessComponent,
  ],
)
class EmailConfirmComponent implements OnInit {
  final UserApi _userApi;
  final RouteParams _routeParams;
  final NavBarService navBarService;

  bool confirmed;

  EmailConfirmComponent(this._userApi, this._routeParams, this.navBarService);

  @override
  Future<Null> ngOnInit() async {
    String token = _routeParams.get("token");
    confirmed = await _userApi.confirmEmail(token);
    navBarService.navBarTheme = 'red';
    navBarService.currentPage = 'home';
  }

  ngOnDestroy() {
    navBarService.navBarTheme = '';
    navBarService.currentPage = '';
  }

}
