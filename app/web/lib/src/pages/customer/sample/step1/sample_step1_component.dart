// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/date_selector/date_selector_component.dart';
import 'package:app_web/src/widgets/kitid_selector/kitid_selector_component.dart';
import 'package:app_web/src/widgets/number_picker/number_picker_component.dart';
import 'package:app_web/src/widgets/time_selector/time_picker_component.dart';
import 'package:app_web/src/widgets/wizard/wizard_component.dart';

@Component(
  selector: 'sample-step1',
  host: const {'class': 'wizard-component'},
  styleUrls: const [''],
  templateUrl: 'sample_step1_component.html',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
    KitIdSelectorComponent,
    NumberPickerComponent,
    TimeSelectorComponent,
    DateSelectorComponent
  ],
)
class SampleStep1Component extends WizardComponent implements OnInit {
  final SampleApi _sampleApi;
  final SecurityService _securityService;

  String beginUsingTime;
  int dayOfPeriod;
  bool showGoodJobRegister = false;

  ControlGroup kitIdForm;

  SampleStep1Component(this._sampleApi, this._securityService);

  @Input()
  StripSample sampleData;

  @override
  Future<bool> isStepCompleted(String stepName) async {
    bool valid = false;
    switch(stepName) {
      case 'kit-id':
        valid = kitIdForm.valid;
        break;
      case 'time-started':
        valid = sampleData.padStartTime != null && sampleData.padStartTime.year != -1;
        break;
      default:
        valid = true;
        break;
    }
    return valid;
  }

  @override
  ngOnInit() {
    // at start first step should be opened
    navigateToStep('kit-id');
    if(sampleData.padStartTime == null) {
      sampleData.padStartTime = new DateTime.now();
    }
    beginUsingTime = TimeUtils.formatTime12(sampleData.padStartTime);
    if(sampleData.padStartTime != null && sampleData.periodStartDate != null) {
      dayOfPeriod = 1;
    } else {
      dayOfPeriod = 1;
    }

    //forms
    kitIdForm = new FormBuilder().group({
      "kitId": ['', validateKitId],
    });
  }

  Map<String, dynamic> validateKitId(AbstractControl control) {
    String value = control.value;
    if (value.isEmpty || value == null) {
      return {};
    }
    Map<String, dynamic> result = new Map<String, dynamic>();
    if (value.length != 6 || new RegExp(r'^\d+$').allMatches(value).length == 0) {
      result['invalidFormat'] = true;
    }
    return result;
  }

  @override
  Future<Null> confirm() async {
    sampleData.padStartTime = TimeUtils.adjustTime12(beginUsingTime, sampleData.padStartTime);
    sampleData.periodStartDate = sampleData.padStartTime.subtract(new Duration(days: dayOfPeriod));
    if (sampleData.stepNumber == null || sampleData.stepNumber < 2) {
      sampleData.stepNumber = 2;
    }
    await _sampleApi.saveStripSample(sampleData);
    showGoodJobRegister = true;
    super.confirm();
  }

}
