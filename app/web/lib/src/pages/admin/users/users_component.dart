// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/directives/row_hover_directive.dart';
import 'package:app_web/src/pages/admin/users/create_user/create_user_component.dart';
import 'package:app_web/src/pages/admin/users/delete_user/delete_user_component.dart';

@Component(
  selector: 'users',
  host: const {'class': 'page-component'},
  styleUrls: const ['users_component.css'],
  templateUrl: 'users_component.html',
  directives: const [
    RowHoverDirective,
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
    DeleteUserComponent,
    CreateUserComponent,
  ],
  pipes: const [COMMON_PIPES],
)
class UsersComponent implements OnInit {
  final UserApi _userApi;
  String action;
  User userForDelete;
  Iterable<User> users;

  UsersComponent(this._userApi);

  void createUser() {
    userForDelete = null;
    action = "CREATE";
  }

  void deleteUser(User user) {
    userForDelete = user;
    action = "DELETE";
  }

  @override
  Future<Null> ngOnInit() async {
    users = await _userApi.getUsers();
    userForDelete = null;
    action = null;
  }

  Future<Null> onAction(bool result) async {
    print("executed $action with result $result");
    if(result) {
      users = await _userApi.getUsers();
    }
    userForDelete = null;
    action = null;
  }
}
