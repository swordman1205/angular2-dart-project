// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/choice_selector/choice_selector_component.dart';
import 'package:app_web/src/widgets/date_selector/date_selector_component.dart';
import 'package:app_web/src/widgets/number_picker/number_picker_component.dart';
import 'package:app_web/src/widgets/wizard/wizard_component.dart';

@Component(
  selector: 'signup-step2',
  host: const {'class': 'wizard-component'},
  styleUrls: const [''],
  templateUrl: 'signup_step2_component.html',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
    NumberPickerComponent,
    ChoiceSelectorComponent,
    DateSelectorComponent
  ],
)
class SignupStep2Component extends WizardComponent implements OnInit {

  @Input()
  UserData userData;

  ControlGroup firstDateLastPeriodForm;

  @override
  ngOnInit() {
    navigateToStep('last-period-date');

    firstDateLastPeriodForm = new FormBuilder().group({
      "firstDateOfLastPeriod": [new DateTime.now().subtract(new Duration(days: 25)), validateFirstDateLastPeriod],
    });
  }

  Map<String, dynamic> validateFirstDateLastPeriod(AbstractControl control) {
    DateTime dateTime = control.value;
    if (dateTime == null) {
      return {};
    }
    Map<String, dynamic> result = new Map<String, dynamic>();
    if (dateTime.year == -1) {
      result['invalid'] = true;
    } else {
      if (TimeUtils.durationInMonth(dateTime) > 6) {
        result['tooLongPeriod'] = true;
      }
      if (dateTime.isAfter(new DateTime.now())) {
        result['fromFuture'] = true;
      }
    }
    return result;
  }

  @override
  Future<bool> isStepCompleted(String stepName) async {
    bool valid = false;
    switch(stepName) {
      case 'last-period-date':
        valid = firstDateLastPeriodForm.valid;
        break;
      case 'period-length':
        valid = (userData.averagePeriodLength??0) > 0;
        break;
      case 'cycle-length':
        valid = (userData.typicalCycleLength??0) > 0;
        break;
      default:
        throw "unknown step $stepName";
    }
    print("stepName $stepName userData $userData");
    return valid;
  }
}

