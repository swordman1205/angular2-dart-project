// Copyright (c) 2017, Qurasense. All rights reserved.

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_web/src/pages/site/navbar_service.dart';
import 'package:app_web/src/widgets/trial_process/trial_process_component.dart';

@Component(
  selector: 'home',
  host: const {'class': 'page-component'},
  styleUrls: const ['home_component.css'],
  templateUrl: 'home_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
    TrialProcessComponent,
  ],
)
class HomeComponent implements OnInit, OnDestroy {
  final Injector _injector;
  NavBarService navBarService;

  HomeComponent(this._injector) {
    navBarService = _injector.get(NavBarService);
  }

  ngOnInit() {
    navBarService.navBarTheme = 'red';
    navBarService.currentPage = 'home';
  }

  ngOnDestroy() {
    navBarService.navBarTheme = '';
    navBarService.currentPage = '';
  }
}
