// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/utils/contact_utils.dart';
import 'package:app_web/src/widgets/choice_selector/choice_selector_component.dart';
import 'package:app_web/src/widgets/password_set/password_set_component.dart';
import 'package:app_web/src/widgets/two_label_toggle/two_label_toggle.dart';
import 'package:app_web/src/widgets/wizard/wizard_component.dart';
import 'package:app_web/src/widgets/terms_of_use/terms_of_use_component.dart';
import 'package:app_web/src/widgets/privacy_policy/privacy_policy_component.dart';
import 'package:app_web/src/widgets/loader_button/loader_button_component.dart';

@Component(
  selector: 'signup-step4',
  host: const {'class': 'wizard-component'},
  styleUrls: const [''],
  templateUrl: 'signup_step4_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    formDirectives,
    ChoiceSelectorComponent,
    PasswordSetComponent,
    TwoLabelToggleComponent,
    LoaderButtonComponent,
    TermsOfUseComponent,
    PrivacyPolicyComponent
  ],
)
class SignupStep4Component extends WizardComponent implements OnInit {
  final UserApi _userApi;
  final GatewayApi _gatewayApi;
  final Router _router;
  String userWeight = "Lbs";
  String userHeight = "Ft";
  String agreement;

  List<EnumWrapper> contactTimeOptions = Enums.contactTime;
  List<EnumWrapper> contactTimeSelectedOptions = [];

  List<EnumWrapper> contactDaysOptions = Enums.contactDays;
  List<EnumWrapper> contactDaysSelectedOptions = [];
  List<EnumWrapper> countryList = CountryEnum.countries;

  ControlGroup addressForm;
  ControlGroup emailForm;
  ControlGroup phoneForm;

  @ViewChild('password')
  PasswordSetComponent passwordSetComponent;

  bool emailExists = false;
  String registrationErrorMessage;

  bool loading = false;

  bool showToC = false;
  bool showPrivacy = false;

  SignupStep4Component(this._userApi, this._gatewayApi, this._router);

  @Input()
  UserData userData;

  @override
  ngOnInit() {
    navigateToStep('address');

    //forms
    addressForm = new FormBuilder().group({
      "addressLine": ["", validateAddress],
      "city": ["", validateAddress],
//      "state": ["", validateAddress],
      "zip": ["", validateAddress]
    });
    emailForm = new FormBuilder().group({
      "email": ["", ContactUtils.validateEmail],
    });
    phoneForm = new FormBuilder().group({
      "phone": ["", ContactUtils.validatePhone],
    });

    // Initialize contactDaysSelectedOptions and contactTimeSelectedOptions
    initContactOptions();
  }

  initContactOptions() {
    contactDaysSelectedOptions.add(contactDaysOptions.last);
    contactTimeOptions.forEach((EnumWrapper option) => contactTimeSelectedOptions.add(option));
  }

  Map<String, dynamic> validateAddress(AbstractControl control) {
    String value = control.value;
    if (value.isEmpty) {
      return {};
    }
    if (value.length < 2) {
      return {'tooShort': true};
    }
    return {};
  }

  @override
  Future<bool> isStepCompleted(String stepName) async {
    switch(stepName) {
      case "address":
        return addressForm.valid;
      case "email":
        await checkEmailExist();
        return emailForm.valid && !emailExists;
      case "phone":
        return phoneForm.valid;
      case "contact-hours":
        return contactTimeSelectedOptions.isNotEmpty && contactDaysSelectedOptions.isNotEmpty;
      case "password":
        return passwordSetComponent.valid;
      case "accept-terms":
        return agreementConfirmed;
      default:
        return true;
    }
  }

  Future<Null> checkEmailExist() async {
    loading = true;
    if (userData.email?.isEmpty ?? true) {
      loading = false;
      return;
    }
    emailExists = await _userApi.checkEmailExist(userData.email);
    loading = false;
  }

  @override
  Future<Null> navigateToNext() async {
    print('password length is: ${userData.password?.length}');
    super.navigateToNext();
  }

  @override
  void navigateToStep(String stepName) {
    registrationErrorMessage = null;
    super.navigateToStep(stepName);
  }

  @override
  Future<Null> confirm() async {
    registrationErrorMessage = null;
    print('password length is: ${userData.password?.length}');

    loading = true;

    userData.contactTimes = contactTimeSelectedOptions.map((ew)=>ew.name).toList();
    if (contactDaysSelectedOptions.isNotEmpty) {
      userData.contactDay = contactDaysSelectedOptions.map((ew)=>ew.name).first;
    }

    await _gatewayApi.signup(userData);

    super.confirm();

    loading = false;

    if ( agreementConfirmed ) {
      _router.navigate([ "/Site/ThanksRegister"]);
    }
  }

  bool get agreementConfirmed => agreement == "Yes";
}
