// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/choice_selector/choice_selector_component.dart';
import 'package:app_web/src/widgets/two_label_toggle/two_label_toggle.dart';
import 'package:app_web/src/widgets/wizard/wizard_component.dart';

@Component(
  selector: 'signup-step3',
  host: const {'class': 'wizard-component'},
  styleUrls: const [''],
  templateUrl: 'signup_step3_component.html',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
    ChoiceSelectorComponent,
    TwoLabelToggleComponent,
  ],
)
class SignupStep3Component extends WizardComponent implements OnInit {
  List<EnumWrapper> sexualActivityOptions = Enums.sexualActivity;

  List<EnumWrapper> birthControlOptions = Enums.birthControl;

  String userWeight = "Lbs";
  String userHeight = "Ft";

  @Input()
  UserData userData;

  ControlGroup weightForm;
  ControlGroup heightForm;

  List<String> feets = new List<String>.generate(9, (i) => i.toString()).sublist(3);
  List<String> inchs = new List<String>.generate(13, (i) => i.toString());

  bool get isBirthControlValidated => userData.birthControl.isNotEmpty && userData.birthControl.length > 0;

  @override
  ngOnInit() {
    navigateToStep('sexual-activity');

    //forms
    weightForm = new FormBuilder().group({
      "weight": ["", validateWeight],
    });
    heightForm = new FormBuilder().group({
      "height": ["", validateHeight],
    });
  }

  Map<String, dynamic> validateWeight(AbstractControl control) {
    String value = control.value;
    if (value.isEmpty) {
      return {};
    }
    Map<String, dynamic> result = new Map<String, dynamic>();
    try {
      int weight = int.parse(value);
      int max = weightInPound? 600 : MetricUtils.lbsToKg(600);
      int min = weightInPound? 55 : MetricUtils.lbsToKg(55);
      if (weight < min || weight > max) {
        result['unrealistic'] = true;
      }
    } catch (e) {
      result['notNumber'] = true;
    }
    return result;
  }

  Map<String, dynamic> validateHeight(AbstractControl control) {
    String value = control.value;
    if (value.isEmpty) {
      return {};
    }
    Map<String, dynamic> result = new Map<String, dynamic>();
    try {
      int height = int.parse(value);
      if (height < 50) {
        result['unrealistic'] = true;
      }
    } catch (e) {
      result['notNumber'] = true;
    }

    return result;
  }

  void forceValidateWeight() {
    weightForm.controls["weight"].updateValueAndValidity(emitEvent: true);
  }

  void forceValidateHeight() {
    heightForm.controls["height"].updateValueAndValidity(emitEvent: true);
  }

  @override
  Future<bool> isStepCompleted(String stepName) async {
    switch(stepName) {
      case "sexual-activity":
        return userData.sexualActivity.isNotEmpty;
      case "birth-control":
        return userData.birthControl.isNotEmpty;
      case "weight":
        return weightForm.valid;
      case "height":
        return heightInFeet ? (userData.heightFeet != null && userData.heightInch != null) : heightForm.valid ;
      default:
        return true;
    }
  }

  set weightInPound(bool inPound) {
    userData.weightUnit = (inPound) ? "lbs" : "kg";
  }
  bool get weightInPound => (userData.weightUnit??"lbs").toLowerCase() == "lbs";

  set heightInFeet(bool inFeet) {
    userData.heightUnit = (inFeet) ? "ft" : "cm";
  }
  bool get heightInFeet {
    print("height str: ${userData.heightUnit}");
    return (userData.heightUnit??"ft").toLowerCase() == "ft";
  }
}
