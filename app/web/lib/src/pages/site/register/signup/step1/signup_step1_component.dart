// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/src/router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/date_selector/date_selector_component.dart';
import 'package:app_web/src/widgets/wizard/wizard_component.dart';

@Component(
  selector: 'signup-step1',
//  viewProviders: const [FORM_BINDINGS],
  host: const {'class': 'wizard-component'},
  styleUrls: const [''],
  templateUrl: 'signup_step1_component.html',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
    DateSelectorComponent
  ],
  pipes: const [COMMON_PIPES],
)
class SignupStep1Component extends WizardComponent implements OnInit {
  bool showMenstruatingDialog = false;

  @Input()
  UserData userData;

  ControlGroup fullNameForm;
  ControlGroup birthDateForm;

  DateTime maxDate;

  @override
  ngOnInit() {
    // at start first step should be opened
    navigateToStep('menstruate');

    // init forms
    fullNameForm = new FormBuilder().group({
      "fullName": ["", validateFullName],
    });
    birthDateForm = new FormBuilder().group({
      "birthDate": [null, validateBirthday],
    });

    DateTime now = new DateTime.now();
    maxDate = new DateTime(now.year - 18, now.month, now.day).subtract(new Duration(days: 1));
  }

  final Router _router;
  String errorMessage;

  SignupStep1Component(this._router);

  Map<String, dynamic> validateBirthday(AbstractControl control) {
    DateTime dateTime = control.value;
    if (dateTime == null) {
      return {};
    }
    Map<String, dynamic> result = new Map<String, dynamic>();
    if (dateTime.year == -1) {
      result['invalid'] = true;
    } else {
      if (TimeUtils.durationInYears(dateTime) < 18) {
        result['tooYoung'] = true;
      }
      if (dateTime.isAfter(new DateTime.now())) {
        result['fromFuture'] = true;
      }
    }

    return result;
  }

  Map<String, dynamic> validateFullName(AbstractControl control) {
    String value = control.value;
    if (value.isEmpty) {
      return {};
    }
    Iterable<String> nameParts = value.split(" ").where((s)=>s.isNotEmpty);
    Map<String, dynamic> result = new Map<String, dynamic>();
    if (nameParts.length < 2) {
      result['minTwoWords'] = true;
    }
    if (nameParts.any((part) => part.length < 2)) {
      result['minTwoLetter'] = true;
    }
    print("fullNameForm: ${fullNameForm?.valid}");
    return result;
  }

  @override
  Future<bool> isStepCompleted(String stepName) async {
    bool valid = false;
    switch(stepName) {
      case 'menstruate':
        valid = userData.menstruate??false;
        break;
      case 'fullname':
        valid = fullNameForm.valid;
        break;
      case 'birthdate':
        valid = birthDateForm.valid;
        break;
      default:
        throw "unknown step $stepName";
    }
    print("step $stepName is ${valid ? 'valid' : 'invalid'}");
    return valid;
  }

  void set isMenstruating(bool menstruating) {
    userData.menstruate = menstruating;
    showMenstruatingDialog = !menstruating;
    if(menstruating) {
      navigateToNext();
    } else {
      print('nav to sorry');
      _router.navigate([ "/Site/Sorry" ]);
    }
  }
}
