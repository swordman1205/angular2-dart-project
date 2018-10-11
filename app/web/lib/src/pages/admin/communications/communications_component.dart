// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/directives/row_hover_directive.dart';

@Component(
  selector: 'communications',
  host: const {'class': 'page-component'},
  styleUrls: const ['communications_component.css'],
  templateUrl: 'communications_component.html',
  directives: const [
    RowHoverDirective,
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
  ],
  pipes: const [COMMON_PIPES],
)
class CommunicationsComponent implements OnInit {
  final UserApi _userApi;
  String action;

  CommunicationsComponent(this._userApi);

  @override
  Future<Null> ngOnInit() async {
    //users = await _userApi.getUsers();
    action = null;
  }

  Future<Null> onAction(bool result) async {
    print("executed $action with result $result");
    if(result) {
      //users = await _userApi.getUsers();
    }
    action = null;
  }
}
