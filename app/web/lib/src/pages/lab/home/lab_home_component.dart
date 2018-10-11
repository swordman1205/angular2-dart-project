// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';

@Component(
  selector: 'app-home',
  host: const {'class': 'page-component'},
  styles: const ['''
  '''],
  template: '''
    <template [ngIf]="currentUser != null">
      <h2>Laboratory</h2>
      <p>hello: {{currentUser.email}}</p>
    </template>
    ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
  ]
)
class LabHomeComponent implements OnInit {
  final SecurityService _securityService;

  User currentUser;

  LabHomeComponent(this._securityService);

  @override
  Future<Null> ngOnInit() async {
    currentUser = await _securityService.getCurrentUser();
  }
}
