// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';

@Component(
  selector: 'password-set',
  templateUrl: 'password_set_component.html',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
  ],
)
class PasswordSetComponent implements OnInit {
  String passwordValue = '';
  String passwordConfirmValue = '';
  String passwordErrorMessage;

  ControlGroup passwordForm;

  // Input show error
  @Input() bool showError = true;

  // output password
  final _passwordChange = new StreamController<String>();
  @Output()
  Stream<String> get passwordChange => _passwordChange.stream;

  // output password on enter
  final _enterEmitter = new StreamController<String>();
  @Output()
  Stream<String> get onEnter => _enterEmitter.stream;

  @override
  ngOnInit() {
    //forms
    passwordForm = new FormBuilder().group({
      "password": ["", validatePassword],
      "passwordConfirm": ["", validatePasswordConfirm],
    });
  }

  Map<String, dynamic> validatePassword(AbstractControl control) {
    String value = control.value;
    if (value.isEmpty) {
      return {};
    }
    if (value.length < 6) {
      return {'tooShort': true};
    }
    return {};
  }

  Map<String, dynamic> validatePasswordConfirm(AbstractControl control) {
    String value = control.value;
    if (passwordValue.isEmpty && value.isEmpty) {
      return {};
    }
    if (value != passwordValue) {
      return {'notMatch': true};
    }
    return {};
  }

  bool get valid => passwordForm.valid;

  void updatePassword(e) {
    if (e.keyCode == 13) return;
    if (passwordForm.valid) {
      print("update password to value, lengh: ${passwordValue?.length}");
      _passwordChange.add(passwordValue);
    } else {
      print("update password to null");
      _passwordChange.add(null);
    }
  }

  void tryFireEnter() {
    if (passwordForm.valid) {
      _enterEmitter.add(passwordValue);
    }
  }

}
