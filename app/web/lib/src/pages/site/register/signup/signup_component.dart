// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/site/register/signup/step1/signup_step1_component.dart';
import 'package:app_web/src/pages/site/register/signup/step2/signup_step2_component.dart';
import 'package:app_web/src/pages/site/register/signup/step3/signup_step3_component.dart';
import 'package:app_web/src/pages/site/register/signup/step4/signup_step4_component.dart';
import 'package:app_web/src/widgets/wizard/wizard_flow_component.dart';
import 'package:app_web/src/pages/site/register/signup/signup_child_component.dart';

@Component(
  selector: 'signup',
  host: const { 'class': 'page-article' },
  styleUrls: const ['signup_component.css'],
  styles: const ['''
  '''],
  templateUrl: 'signup_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    SignupStep1Component,
    SignupStep2Component,
    SignupStep3Component,
    SignupStep4Component
  ],
)
@RouteConfig(const [
  const Redirect(path: '/', redirectTo: const ['Step', const { 'order': 1, 'step': 1 }], useAsDefault: true),
  const Route(path: '/step/:order/:step', name: 'Step', component: SignupChildComponent),
  const Redirect(path: '/**', redirectTo: const ['Step', const { 'order': 1, 'step': 1 }]),
])
class SignupComponent extends WizardFlowComponent implements OnInit, AfterContentChecked, OnReuse, OnDestroy {
  UserData userData;

  int deviceWidth = window.innerWidth, deviceHeight = window.innerHeight;

  @ViewChild('signup1')
  SignupStep1Component signupStep1Component;
  @ViewChild('signup2')
  SignupStep2Component signupStep2Component;
  @ViewChild('signup3')
  SignupStep3Component signupStep3Component;
  @ViewChild('signup4')
  SignupStep4Component signupStep4Component;

  StreamSubscription<KeyboardEvent> enterSubscription;

  List<int> currentStepList = new List<int>.filled(4, 0);

  bool isFocused = false;
  Element focusedEl;

  int inputCount = 0;
  StreamSubscription keyboardShowListener;
  StreamSubscription deviceChangeListener;

  List<Element> inputs, inactiveSteps;

  SignupComponent(): super([
    new Wizard('Basic information',       ['menstruate', 'fullname', 'birthdate']),
    new Wizard('About your period',       ['last-period-date', 'period-length', 'cycle-length']),
    new Wizard('General info about you',  ['sexual-activity', 'birth-control', 'weight', 'height']),
    new Wizard('Contact',                 ['address', 'phone', 'contact-hours', 'email', 'password', 'accept-terms'])
  ]);

  String getHeight(int wizard) {
    if (deviceWidth >= 992) {
      return 'auto';
    } else {
      if (currentWizard == wizard) {
        return isFocused? '${deviceHeight - 48}px' : '${deviceHeight - 48 - 40 * 3}px';
      } else {
        return '40px';
      }
    }
  }

  ngAfterContentChecked() {
    inputs = querySelectorAll('signup input');
    if (inputs.length > 0 && inputs.length != inputCount) {
      inputCount = inputs.length;
      for (int i = 0; i < inputs.length; i++) {
        Element el = inputs[i];
        el.onFocus.listen((Event event) {
          new Future(() {
            isFocused = true;
            focusedEl = (event.target as Element);
            inactiveSteps = querySelectorAll('.wizard-component:not(.active)');
            for (int j = 0; j < inactiveSteps.length; j++) {
              inactiveSteps[j].style.display = 'none';
            }
          });
        });
        el.onBlur.listen((Event event) {
          new Future(() {
            isFocused = false;
            focusedEl = null;
            for (int j = 0; j < inactiveSteps.length; j++) {
              inactiveSteps[j].style.display = 'block';
            }
          });
        });
      }

      int _originalSize = window.innerWidth + window.innerHeight;
      keyboardShowListener = window.onResize.skipWhile((Event e) => !this.isFocused).listen((Event e) {
        if (window.innerWidth + window.innerHeight != _originalSize) {
          Element step = querySelector('signup-step${currentWizard + 1}');
          int scrollHeight = focusedEl.getBoundingClientRect().top - step.querySelector('form').getBoundingClientRect().top;
          step.scrollTop = scrollHeight;
        }
      });
    }
  }

  ngOnDestroy() {
    keyboardShowListener.cancel();
    deviceChangeListener.cancel();
    enterSubscription.cancel();
  }

  List<int> getWizardPath() {
    return (window.location.hash).substring("#/site/register/step/".length).split('/').map((String url) => int.parse(url, onError: (e) => null)).toList();
  }

  activateWizard(int wizard) {
    navigateToWizard(wizard);
    int step;
    switch (wizard) {
      case 0:
        step = signupStep1Component.currentStep;
        break;
      case 1:
        step = signupStep2Component.currentStep;
        break;
      case 2:
        step = signupStep3Component.currentStep;
        break;
      case 3:
        step = signupStep4Component.currentStep;
        break;
    }
    navigatedTo(wizard, step);
  }

  @override
  navigatedTo(int wizard, int step) {
    if (wizard != getWizardPath()[0] - 1 || step != getWizardPath()[1] - 1) {
      window.location.hash = '#/site/register/step/${wizard + 1}/${ step + 1 }';
    }
  }

  checkNavigation(int wizard, int step) {
    navigateToWizard(wizard);
    currentStepList[wizard] = step;
  }

  routerOnReuse(ComponentInstruction next, ComponentInstruction prev) {
    int wizard = getWizardPath()[0] - 1;
    int step = getWizardPath()[1] - 1;
    checkNavigation(wizard, step);
  }

  @override
  Future<Null> ngOnInit() async {
    int wizard = getWizardPath()[0] - 1;
    int step = getWizardPath()[1] - 1;
    checkNavigation(wizard, step);
    userData = new UserData();
    userData.country = CountryEnum.find("US");
    userData.heightUnit = 'Ft';
    userData.weightUnit = 'Lbs';
    // add enter listener
    enterSubscription = document.onKeyDown.listen((KeyboardEvent event) async {
      if (event.keyCode == 13) {
        print('current wizard: $currentWizard');
        if (currentWizard == 0) {
          signupStep1Component.jumpToNextStep();
        } else if (currentWizard == 1) {
          signupStep2Component.jumpToNextStep();
        } else if (currentWizard == 2) {
          signupStep3Component.jumpToNextStep();
        } else if (currentWizard == 3) {
          signupStep4Component.jumpToNextStep();
          try {
            bool completed = await signupStep4Component.isStepCompleted("accept-terms");
            print('last step completed, clear enter listener');
            if (completed) {
              enterSubscription.cancel();
            }
          } catch (e) {
            print(e);
          }
        }
      }
    });

    deviceChangeListener = window.onResize.listen((e) {
      deviceWidth = window.innerWidth;
      deviceHeight = window.innerHeight;
    });
  }
}

