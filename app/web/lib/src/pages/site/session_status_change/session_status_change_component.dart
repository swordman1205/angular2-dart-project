// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/site/navbar_service.dart';
import 'package:app_web/src/pages/site/widgets/site-content/site_content_component.dart';
import 'package:app_web/src/widgets/trial_process/trial_process_component.dart';

@Component(
  selector: 'session-status-change',
  host: const {'class': 'page-component'},
  styleUrls: const ['session_status_change_component.css'],
  templateUrl: 'session_status_change_component.html',
  directives: const [
    CORE_DIRECTIVES,
    ROUTER_DIRECTIVES,
    formDirectives,
    materialDirectives,
    SiteContentComponent,
    TrialProcessComponent,
  ],
)
class SessionStatusChangeComponent implements OnInit {
  final TrialApi _trialApi;
  final RouteParams _routeParams;
  final NavBarService navBarService;

  bool updated;

  SessionStatusChangeComponent(this._trialApi, this._routeParams, this.navBarService);

  @override
  Future<Null> ngOnInit() async {
    String token = _routeParams.get("token");
    String status = _routeParams.get("status");
    updated = await _trialApi.updateTrialSessionStatusByToken(token, status);
    navBarService.navBarTheme = 'red';
    navBarService.currentPage = 'home';
  }

  ngOnDestroy() {
    navBarService.navBarTheme = '';
    navBarService.currentPage = '';
  }

}
