// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/src/router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/nurse/customer_samples.dart';
import 'package:app_web/src/widgets/choice_selector/choice_selector_component.dart';
import 'package:app_web/src/widgets/date_selector/date_selector_component.dart';
import 'package:app_web/src/widgets/kitid_selector/kitid_selector_component.dart';
import 'package:app_web/src/widgets/number_picker/number_picker_component.dart';
import 'package:app_web/src/widgets/time_selector/time_picker_component.dart';
import 'package:app_web/src/widgets/two_label_toggle/two_label_toggle.dart';
import 'package:app_web/src/widgets/wizard/wizard_component.dart';

@Component(
  selector: 'pickup-step1',
  host: const {'class': 'wizard-component'},
  styleUrls: const [''],
  templateUrl: 'pickup_step1_component.html',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
    KitIdSelectorComponent,
    NumberPickerComponent,
    TimeSelectorComponent,
    DateSelectorComponent,
    ChoiceSelectorComponent,
    TwoLabelToggleComponent,
  ],
)
class PickupStep1Component extends WizardComponent implements OnInit {
  final HealthApi _healthApi;
  final CustomerSamplesService _customerSamplesService;
  final Router _router;

  ControlGroup tubeIdForm;

  PickupStep1Component(this._healthApi, this._customerSamplesService, this._router);

  @Input()
  TrialSession healthSession;
  String paidLabel;
  List<EnumWrapper> stripIdOptions;
  List<EnumWrapper> stripIdSelectedOptions = new List();

  @override
  Future<bool> isStepCompleted(String stepName) async {
    bool valid = false;
    switch(stepName) {
      default:
        valid = true;
    }
    return valid;
  }

  @override
  ngOnInit() {
    //convert to enumWrapper - choice selector works only with it for now
    stripIdOptions = _customerSamplesService.customerSamples.samples.map((s) => new EnumWrapper(s.id.toString(), s.kitId)).toList();
    // at start first step should be opened
    navigateToStep('strips');

    //forms
    tubeIdForm = new FormBuilder().group({
      "tubeId": [null, validateTubeId],
    });
  }

  Map<String, dynamic> validateTubeId(AbstractControl control) {
    String value = control.value;
    if (value == null) {
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
    print('not implemented');
//    super.confirm();
//    healthSession.sampleIds = stripIdSelectedOptions.map((ew) => ew.name).toList();
//    healthSession.stripIds = stripIdSelectedOptions.map((ew) => ew.caption).toList();
//    healthSession.paidStatus = paidLabel == "paid" ? "PAID" : "NOT_PAID";
//    await _healthApi.pickup(healthSession);
//    _router.navigate([
//      '../Home'
//    ]);
  }
}

