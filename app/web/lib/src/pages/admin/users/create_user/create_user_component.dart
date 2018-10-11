// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/password_set/password_set_component.dart';

@Component(
  selector: 'create-user',
  styleUrls: const ['create_user_component.css'],
  templateUrl: 'create_user_component.html',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
    materialNumberInputDirectives,
    PasswordSetComponent,
    ROUTER_DIRECTIVES,
  ],
  pipes: const [COMMON_PIPES],
)
class CreateUserComponent implements OnInit {
  final _actionEvents = new StreamController<bool>();
  final UserApi _userApi;
  final Router _router;
  String _password;

  @Output()
  Stream<bool> get onAction => _actionEvents.stream;

  ItemRenderer<EnumWrapper> itemRenderer =
    new CachingItemRenderer<EnumWrapper>((enumWrapper) => "${enumWrapper.caption}");

  //ADMIN, CUSTOMER, MEDICAL, LAB_TECH;
  StringSelectionOptions<EnumWrapper> userRoleOptions =
    new StringSelectionOptions<EnumWrapper>(Enums.roles);
  SelectionModel<EnumWrapper> userRoleSelection =
    new SelectionModel<EnumWrapper>.withList(allowMulti: false);
  String get userRoleSelectedLabel =>
      userRoleSelection.selectedValues.length > 0
          ? itemRenderer(userRoleSelection.selectedValues.first)
          : 'Select user role';

  User user = new User();
  bool showRoleRequired = false;
  bool showEmailExists = false;

  CreateUserComponent(this._userApi, this._router);

  void set password(String value) {
    _password = value;
  }

  String get password => _password;

  @override
  Future<Null> ngOnInit() async {
    if (user.role != null) {
      userRoleSelection.select(userRoleOptions.optionsList.firstWhere((ew) => ew.name == user.role, orElse: () => null));
    }
  }

  void cancelAction() {
    _actionEvents.add(false);
  }

  Future<Null> doAction() async {
    await _validate();
    user.role = userRoleSelection.selectedValues.first.name;
    if (user.id != null) {
      _userApi.updateUserByAdmin(user, password);
    } else {
      _userApi.createUserByAdmin(user, password);
    }
    _actionEvents.add(true);
    _router.navigate([ "../Users" ]);
  }

  Future<Null> _validate() async {
    if (userRoleSelection.selectedValues.isEmpty) {
      showRoleRequired = true;
      throw 'role required';
    }
    showRoleRequired = false;
    if (user.id == null) {
      bool emailExists = await _userApi.checkEmailExist(user.email);
      if (emailExists) {
        showEmailExists = true;
        throw 'email exists';
      }
    }
    showEmailExists = false;
  }
}
