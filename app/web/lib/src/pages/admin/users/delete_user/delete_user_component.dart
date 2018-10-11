// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/account/account_component.dart';

@Component(
  selector: 'delete-user',
  host: const {},
  styleUrls: const [''],
  styles: const ['''
  '''],
  templateUrl: 'delete_user_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives
  ],
)
class DeleteUserComponent implements OnInit {
  final _actionEvents = new StreamController<bool>();
  final UserApi _userApi;
  String userDeleteErrorMessage;

  @Input()
  User user;

  @Output()
  Stream<bool> get onAction => _actionEvents.stream;

  DeleteUserComponent(this._userApi);

  @override
  Future<Null> ngOnInit() async {
    print("init user delete ${user.fullName}");
  }

  Future<Null> doAction() async {
    try {
      await _userApi.deleteUser(user.id);
      hideDeleteUserModal();
      _actionEvents.add(true);
    } catch (e) {
      userDeleteErrorMessage = e.message;
    }
  }

  void cancelAction() {
    hideDeleteUserModal();
    _actionEvents.add(false);
  }

  void hideDeleteUserModal() {
    userDeleteErrorMessage = null;
    user = null;
  }
}
