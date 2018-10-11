// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/directives/row_hover_directive.dart';

@Component(
  selector: 'manufacturing',
  host: const {'class': 'page-component'},
  styleUrls: const ['inventory_component.css'],
  templateUrl: 'inventory_component.html',
  directives: const [
    RowHoverDirective,
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
  ],
  pipes: const [COMMON_PIPES],
)
class InventoryComponent implements OnInit {
  final InventoryApi _inventoryApi;
  String action;

  InventoryComponent(this._inventoryApi);

  /*
  void createUser() {
    userForDelete = null;
    action = "CREATE";
  }

  void deleteUser(User user) {
    userForDelete = user;
    action = "DELETE";
  }
  */

  @override
  Future<Null> ngOnInit() async {
    action = null;
  }

  Future<Null> onAction(bool result) async {
    print("executed $action with result $result");
  }
}
