// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/nurse/home/nurse_home_component.dart';
import 'package:app_web/src/pages/nurse/pickup/pickup_component.dart';
import 'package:app_web/src/pages/nurse/customer_samples.dart';
import 'package:app_web/src/pages/nurse/sessions/sessions_component.dart';

@Component(
  selector: 'app',
  host: const {'class': 'page'},
  styleUrls: const ['../page_internal.css'],
  styles: const ['''
    .page-header {
      background-color: #FD5755;
    }
  '''],
  templateUrl: 'nurse_page.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
  ],
  providers: const [CustomerSamplesService],
)
@RouteConfig(const [
  const Route(path: '/home', name: 'Home', component: NurseHomeComponent),
  const Route(path: '/pickup', name: 'Pickup', component: PickupComponent),
  const Route(path: '/sessions', name: 'Sessions', component: SessionsComponent),
])
class NursePage implements OnInit {

  final SecurityService _securityService;

  User currentUser;

  NursePage(this._securityService);

  @override
  Future<Null> ngOnInit() async {
    currentUser = await _securityService.getCurrentUser();
  }

}
