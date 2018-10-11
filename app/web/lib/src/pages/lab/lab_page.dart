// Copyright (c) 2017, Qurasense. All rights reserved.

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_web/src/pages/lab/home/lab_home_component.dart';

@Component(
  selector: 'app',
  host: const {'class': 'page'},
  styleUrls: const ['../page_internal.css'],
  styles: const ['''
  '''],
  template: '''
      <div class="page-content">
        <router-outlet></router-outlet>
      </div>
    ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
  ]
)
@RouteConfig(const [
  const Route(path: '/home', name: 'Home', component: LabHomeComponent),
])
class LabPage {
}
