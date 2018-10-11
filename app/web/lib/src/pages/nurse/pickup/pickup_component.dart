// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/nurse/pickup/step1/pickup_step1_component.dart';
import 'package:app_web/src/widgets/wizard/wizard_flow_component.dart';

@Component(
  selector: 'pickup',
  host: const {},
  styleUrls: const ['pickup_component.css'],
  styles: const [''' 
  '''],
  templateUrl: 'pickup_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    PickupStep1Component,
  ],
)
class PickupComponent extends WizardFlowComponent implements OnInit {
  final SecurityService _securityService;

  TrialSession healthSession;

  @ViewChild('pickup1')
  PickupStep1Component pickupStep1Component;
  StreamSubscription<KeyboardEvent> enterSubscription;

  PickupComponent(this._securityService): super([
    new Wizard("Patient samples pickup", ['strips', 'tube', 'compensation', 'feedback']),
  ]);

  @override
  Future<Null> ngOnInit() async {
    String userId = await _securityService.getCurrentUserId();
    healthSession = new TrialSession();
    healthSession.pickupNurseId = userId;
    navigateToWizard(0);

    //add enter listener
    print("key listener added");
    enterSubscription = document.onKeyDown.listen((KeyboardEvent event) {
      if (event.keyCode == 13) {
        print('current wizard: $currentWizard');
        if (currentWizard == 0) {
          pickupStep1Component.navigateToNext();
        }
      }
    });
  }
}
