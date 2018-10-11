// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/customer/account/customer_account_component.dart';
import 'package:app_web/src/pages/customer/account_details/customer_account_details_component.dart';
import 'package:app_web/src/pages/customer/home/customer_home_component.dart';
import 'package:app_web/src/pages/customer/sample/lostpad/lostpad_component.dart';
import 'package:app_web/src/pages/customer/sample/sample_component.dart';
import 'package:app_web/src/pages/customer/sample_service.dart';

@Component(
  selector: 'customer',
  host: const {'class': 'page page--external'},
  styleUrls: const ['../page_external.css', 'customer_page.css'],
  templateUrl: 'customer_page.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
    SampleComponent
  ],
  providers: const [SampleService],
)
@RouteConfig(const [
  const Route(path: '/account', name: 'Account', component: CustomerAccountComponent),
  const Route(path: '/account-details', name: 'AccountDetails', component: CustomerAccountDetailsComponent),
  const Route(path: '/home', name: 'Home', component: CustomerHomeComponent),
  const Route(path: '/lostpad', name: 'Lostpad', component: LostpadComponent),
  const Route(path: '/sample/...', name: 'Sample', component: SampleComponent),
])
class CustomerPage implements OnInit {
  final SecurityService _securityService;

  User currentUser;
  String active;

  CustomerPage(this._securityService);

  @override
  Future<Null> ngOnInit() async {
    currentUser = await _securityService.getCurrentUser();
  }
}
