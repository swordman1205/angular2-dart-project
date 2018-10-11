// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/customer/sample/step1/sample_step1_component.dart';
import 'package:app_web/src/pages/customer/sample/step2/sample_step2_component.dart';
import 'package:app_web/src/pages/customer/sample/step3/sample_step3_component.dart';
import 'package:app_web/src/pages/customer/sample_service.dart';
import 'package:app_web/src/widgets/wizard/wizard_flow_component.dart';
import 'package:app_web/src/pages/customer/sample/sample_child_component.dart';

@Component(
  selector: 'sample',
  host: const {},
  styleUrls: const ['sample_component.css'],
  styles: const [''' 
  '''],
  templateUrl: 'sample_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    SampleStep1Component,
    SampleStep2Component,
    SampleStep3Component
  ],
)
@RouteConfig(const [
  const Redirect(path: '/', redirectTo: const ['Step', const { 'order': 1, 'step': 1 }], useAsDefault: true),
  const Route(path: '/step/:order/:step', name: 'Step', component: SampleChildComponent),
  const Redirect(path: '/**', redirectTo: const ['Step', const { 'order': 1, 'step': 1 }]),
])
class SampleComponent extends WizardFlowComponent implements OnInit, OnReuse, AfterContentChecked, OnDestroy {
  final SecurityService _securityService;
  final SampleApi _sampleApi;
  final SampleService _sampleService;

  StripSample sampleData;

  List<int> currentStepList = new List<int>.filled(3, 0);

  @ViewChild('sample1')
  SampleStep1Component sampleStep1Component;
  @ViewChild('sample2')
  SampleStep2Component sampleStep2Component;
  @ViewChild('sample3')
  SampleStep3Component sampleStep3Component;
  StreamSubscription<KeyboardEvent> enterSubscription;

  bool isFocused = false;
  Element focusedEl;

  int inputCount = 0;
  StreamSubscription keyboardShowListener;

  List<Element> inputs, inactiveSteps;

  SampleComponent(this._securityService, this._sampleApi, this._sampleService): super([
    new Wizard("Register and use", ['kit-id', 'time-started', 'day-period']),
    new Wizard("Sample information", ['is-pad-saturated', 'remove-take-picture', 'pull-sample-out-of-pad', 'remove-plastic-layer', 'time-pad-remove', 'how-much-bleed']),
    new Wizard("Feedback", ['primary-activities', 'knowing-saturated', 'comfort', 'additional-feedback'])
  ]);

  List<int> getWizardPath() {
    if (window.location.hash.startsWith("#/customer/sample/step/")) {
      return (window.location.hash).substring("#/customer/sample/step/".length).split('/').map((String url) => int.parse(url, onError: (e) => null)).toList();
    }
    return null;
  }

  @override
  navigatedTo(int wizard, int step) {
    var wizardPath = getWizardPath();
    if (wizardPath == null) {
      return;
    }
    if (wizard != wizardPath[0] - 1 || step != wizardPath[1] - 1) {
      window.location.hash = '#/customer/sample/step/${wizard + 1}/${ step + 1 }';
    }
  }

  checkNavigation(int wizard, int step) {
    navigateToWizard(wizard);
    currentStepList[wizard] = step;
  }

  @override
  ngAfterContentChecked() {
    inputs = querySelectorAll('sample input, sample textarea');
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
            return true;
          });
        });
        el.onBlur.listen(blurHandler);
      }

      int _originalSize = window.innerWidth + window.innerHeight;
      keyboardShowListener = window.onResize.skipWhile((Event e) => !this.isFocused).listen((Event e) {
        if (window.innerWidth + window.innerHeight != _originalSize) {
          Element step = querySelector('sample-step${currentWizard + 1}');
          int scrollHeight = focusedEl.getBoundingClientRect().top - step.querySelector('form').getBoundingClientRect().top;
          step.scrollTop = scrollHeight;
        }
      });
    }
  }

  void blurHandler(Event event) {
    isFocused = false;
    focusedEl = null;
    for (int j = 0; j < inactiveSteps.length; j++) {
      inactiveSteps[j].style.display = 'block';
    }
  }

  @override
  ngOnDestroy() {
    if (keyboardShowListener != null) {
      keyboardShowListener.cancel();
    }
    if (enterSubscription != null) {
      enterSubscription.cancel();
    }
  }

  @override
  routerOnReuse(ComponentInstruction next, ComponentInstruction prev) {
    int wizard = getWizardPath()[0] - 1;
    int step = getWizardPath()[1] - 1;
    checkNavigation(wizard, step);
  }

  @override
  Future<Null> ngOnInit() async {
    String userId = await _securityService.getCurrentUserId();
    sampleData = await _sampleApi.getLatestNotFinishedSample(userId);
    if (sampleData == null) {
      sampleData = new StripSample();
      sampleData.primaryActivities = new List();
      sampleData.status = 'INPROGRESS';
      sampleData.stepNumber = 1;
      sampleData.createUserId = userId;
      sampleData.customerId = userId;
    }
    _sampleService.sample = sampleData;
    var wizardIndex = sampleData.stepNumber - 1;//wizard index start form 0
    checkNavigation(wizardIndex, 0);
    navigatedTo(wizardIndex, 0);

    //add enter listener
    print("key listener added");
    enterSubscription = document.onKeyDown.listen((KeyboardEvent event) {
      if (event.keyCode == 13) {
        print('current wizard: $currentWizard');
        if (currentWizard == 0) {
          if (sampleStep1Component == null) {
            print("ignore enter, sampleStep1Component not yet injected");
          } else {
            sampleStep1Component.jumpToNextStep();
          }
        } else if (currentWizard == 1) {
          sampleStep2Component.jumpToNextStep();
        } else if (currentWizard == 2) {
          if (sampleStep3Component.hasNext) {
            sampleStep3Component.jumpToNextStep();
          } else {
            print("ignore enter at last step of last wizard(additional feedback)");
            //sampleStep3Component.showConfirmDialog();
          }
        }
      }
    });
  }
}
