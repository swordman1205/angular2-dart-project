// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_web/src/pages/admin/admin_page.dart';
import 'package:app_web/src/pages/customer/customer_page.dart';
import 'package:app_web/src/pages/lab/lab_page.dart';
import 'package:app_web/src/pages/medical/medical_page.dart';
import 'package:app_web/src/pages/nurse/nurse_page.dart';
import 'package:app_web/src/pages/site/site_page.dart';

@Component(
  selector: 'app',
  styleUrls: const [],
  template: '''
      <router-outlet></router-outlet>
    ''',
  directives: const [
    ROUTER_DIRECTIVES,
  ],
  providers: const [
    materialProviders
  ],
)
@RouteConfig(const [
  const Redirect(path: '/', redirectTo: const ['Site/Home']),
  const Route(path: '/admin/...', name: 'Admin', component: AdminPage),
  const Route(path: '/customer/...', name: 'Customer', component: CustomerPage),
  const Route(path: '/app/...', name: 'Lab', component: LabPage),
  const Route(path: '/medical/...', name: 'Medical', component: MedicalPage),
  const Route(path: '/nurse/...', name: 'Nurse', component: NursePage),
  const Route(path: '/site/...', name: 'Site', component: SitePage),
])
class AppComponent {
}
