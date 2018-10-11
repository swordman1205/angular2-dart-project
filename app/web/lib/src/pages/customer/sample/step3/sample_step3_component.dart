// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/choice_selector/choice_selector_component.dart';
import 'package:app_web/src/widgets/wizard/wizard_component.dart';

@Component(
  selector: 'sample-step3',
  host: const {'class': 'wizard-component'},
  styleUrls: const [''],
  templateUrl: 'sample_step3_component.html',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
    ChoiceSelectorComponent
  ],
)
class SampleStep3Component extends WizardComponent implements OnInit {
  final SampleApi _sampleApi;
  final SecurityService _securityService;
  final Router _router;

  bool showFeedbackModal = false;

  List<EnumWrapper> primaryActivitiesOptions = Enums.primaryActivities;
  List<EnumWrapper> primaryActivitiesSelectedOptions = new List();

  List<EnumWrapper> knowSaturatedOptions = Enums.padSaturation;
  List<EnumWrapper> knowSaturatedSelectedOptions = new List();

  List<EnumWrapper> comfortLevelOptions = Enums.padComfort;
  List<EnumWrapper> comfortLevelSelectedOptions = new List();

  SampleStep3Component(this._sampleApi, this._securityService, this._router);

  @Input()
  StripSample sampleData;

  @override
  Future<bool> isStepCompleted(String stepName) async {
    bool valid = false;
    switch(stepName) {
      case 'primary-activities':
        valid = primaryActivitiesSelectedOptions.isNotEmpty;
        break;
      case 'additional-feedback':
        valid = showFeedbackModal == true;
        break;
      default:
        valid = true;
        break;
    }
    return valid;
  }

  @override
  ngOnInit() {
    for (String primaryActivity in sampleData.primaryActivities) {
      EnumWrapper enumWrapper = Enums.primaryActivities.firstWhere((ew) => ew.name == primaryActivity);
      primaryActivitiesSelectedOptions.add(enumWrapper);
    }
    if (sampleData.padSaturation != null) {
      var enumWrapper = Enums.padSaturation.firstWhere((ew) => ew.name == sampleData.padSaturation);
      knowSaturatedSelectedOptions = [enumWrapper];
    }
    if (sampleData.padComfort != null) {
      var enumWrapper = Enums.padComfort.firstWhere((ew) => ew.name == sampleData.padComfort);
      comfortLevelSelectedOptions = [enumWrapper];
    }
    navigateToStep('primary-activities');
  }

  void showConfirmDialog(MouseEvent event) {
    showFeedbackModal = true;
  }

  @override
  Future<Null> confirm() async {
    showFeedbackModal = false;
    sampleData.status = 'FINISHED';
    if (primaryActivitiesSelectedOptions.isNotEmpty) {
      sampleData.primaryActivities = primaryActivitiesSelectedOptions.map((ew) => ew.name).toList();
    }
    if (knowSaturatedSelectedOptions.isNotEmpty) {
      sampleData.padSaturation = knowSaturatedSelectedOptions.first.name;
      if (!Enums.padSaturation.any((ew) => ew.name == sampleData.padSaturation)) {
        throw "incorrect saturate value: ${sampleData.padSaturation}";
      }
    }
    if (comfortLevelSelectedOptions.isNotEmpty) {
      sampleData.padComfort = comfortLevelSelectedOptions.first.name;
    }

    await _sampleApi.saveStripSample(sampleData);
    _router.navigate([ "/Customer/Home"]);
    super.confirm();
  }
}
