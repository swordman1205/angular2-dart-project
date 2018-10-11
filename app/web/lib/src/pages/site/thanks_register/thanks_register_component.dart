// Copyright (c) 2017, Qurasense. All rights reserved.

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_web/src/pages/site/widgets/site-content/site_content_component.dart';
import 'package:app_web/src/widgets/trial_process/trial_process_component.dart';
import 'package:app_web/src/pages/site/navbar_service.dart';

@Component(
  selector: 'thanks-register',
  host: const {'class': 'page-component'},
  styleUrls: const ['thanks_register_component.css'],
  templateUrl: 'thanks_register_component.html',
  directives: const [
    CORE_DIRECTIVES,
    ROUTER_DIRECTIVES,
    materialDirectives,
    SiteContentComponent,
    TrialProcessComponent,
  ],
)
class ThanksRegisterComponent implements OnInit, OnDestroy {
  final Injector _injector;
  NavBarService navBarService;

  ThanksRegisterComponent(this._injector) {
    navBarService = _injector.get(NavBarService);
  }

  ngOnInit() {
    navBarService.navBarTheme = 'red bg-light';
    navBarService.currentPage = 'home';
  }

  ngOnDestroy() {
    navBarService.navBarTheme = '';
    navBarService.currentPage = '';
  }
}
