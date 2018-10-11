// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/date_selector/date_selector_component.dart';

@Component(
  selector: 'account',
  host: const {},
  styleUrls: const ['account_component.css'],
  styles: const ['''
  '''],
  templateUrl: 'account_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    formDirectives,
    DateSelectorComponent
  ],
)
class AccountComponent implements OnInit {
  final SecurityService _securityService;
  final UserApi _userApi;
  final Router _router;

  User _currentUser;

  @Input()
  User user;
  User editableUser;
  CustomerInfo info;

  AccountComponent(this._securityService, this._userApi, this._router);

  @override
  Future<Null> ngOnInit() async {
    _currentUser = await _securityService.getCurrentUser();
    if (user == null) {
      throw "must have user to show";
    }
    editableUser = CloneUtils.clone(user);

    if(user.role == "CUSTOMER") {
      info = await _userApi.getCustomerInfo(user.id);
      print(info.dateOfBirth);
    }
  }

  bool get actionable {
    // only allow updating user values if viewed user is current user
    return (_currentUser != null && _currentUser.id == user.id);
  }

  Future<Null> done() async {
    await _userApi.updateUser(editableUser);
    user.fullName = editableUser.fullName;
    user.email = editableUser.email;

    if (user.role == "CUSTOMER") {
      info.email = user.email;
      info.fullName = user.fullName;
      await _userApi.saveCustomerInfo(info);
    }

    _router.navigate([
      '/Customer/Account'
    ]);
  }
}
