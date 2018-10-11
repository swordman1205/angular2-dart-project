// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';

abstract class WizardFlowComponent {
  final _wizardChange = new StreamController<String>();
  final _completedWizards = new Set<String>();
  final List<Wizard> wizards;
  final List<String> _wizardNames;
  final List<String> _stepNames;
  int completionPercent = 0;
  int currentWizard = 0;
  int allowedWizard = 0;

  WizardFlowComponent(this.wizards)
      : _wizardNames = wizards.map((w) => w.name).toList(),
        _stepNames = wizards.fold(<String>[], (List<String> res, Wizard w) => res..addAll(w.steps));

  @Output()
  Stream<String> get wizardChange => _wizardChange.stream;

  List<String> get wizardNames => _wizardNames;

  List<String> get stepNames => _stepNames;

  navigatedTo(int wizard, int step) {
    // silence is golden
  }

  List<String> stepsOf(int wizardNumber) {
    if(!_isValidWizardNumber(wizardNumber)) {
      throw "invalid wizard number $wizardNumber";
    }
    return wizards[wizardNumber].steps;
  }

  String wizardOf(int wizardNumber) {
    if(!_isValidWizardNumber(wizardNumber)) {
      throw "invalid wizard number $wizardNumber";
    }
    return wizards[wizardNumber].name;
  }

  bool isCurrentWizard(String wizardName) => wizardNames.indexOf(wizardName) == currentWizard;

  bool isAllowedWizard(String wizardName) => allowedWizard >= wizardNames.indexOf(wizardName);

  void stateChange(String event) {
    List<String> eventInfo = event.split(":");
    var originWizard = eventInfo[0];
    var eventType = eventInfo[1];
    List<String> eventDetails = [];
    if(eventInfo.length > 2) {
      eventDetails = eventInfo.sublist(2);
    }
    _handleWizardEvent(originWizard, eventType, eventDetails);
  }

  void navigateToWizard(var wizard) {
    var wizardNumber = (wizard is String) ? wizardNames.indexOf(wizard) : wizard as int;
    if(!_isValidWizardNumber(wizardNumber)) {
      throw "invalid wizard number $wizardNumber";
    }

    if(currentWizard == wizardNumber) {
      return;
    }
    // TODO check we are not jumping ahead
    /*
    if(wizardNumber > currentWizard && !_completed.contains(currentWizard)) {
      print("current wizard is not completed, cannot jump ahread")
    }
    */
    currentWizard = wizardNumber;
    if(allowedWizard == null || allowedWizard < wizardNumber) {
      allowedWizard = wizardNumber;
    }
    _wizardChange.add("wizardChange:${currentWizard}");
  }

  void _handleWizardEvent(String originWizard, String eventType, List<String> eventDetails) {
    print("wizard: $originWizard fired event: $eventType (details $eventDetails)");
    switch(eventType) {
      case "confirm":
        _completedWizards.add(originWizard);
        int nextWizard = _nextWizard();
        _updateCompletionPercent(stepsOf(nextWizard).first);
        navigateToWizard(nextWizard);
        navigatedTo(nextWizard, 0);
        break;
      case "previousWizard":
        int prevWizard = currentWizard - 1;
        if (prevWizard < 0) {
          throw 'negative prev wizard';
        }
        _updateCompletionPercent(stepsOf(prevWizard).last);
        navigateToWizard(prevWizard);
        navigatedTo(prevWizard, wizards[prevWizard].steps.length - 1);
        break;
      case "active":
        int wizardNumber = wizardNames.indexOf(originWizard);
        int currentStep = int.parse(eventDetails.first, onError: (e) => null);
        navigateToWizard(wizardNumber);
        navigatedTo(wizardNumber, currentStep);
        break;
      case "inactive":
        currentWizard = -1;
        break;
      case "stepChange":
        var currentStepName = eventDetails.first;
        _updateCompletionPercent(currentStepName);
        navigatedTo(currentWizard, wizards[currentWizard].steps.indexOf(currentStepName));
        break;
      default:
        throw "unknown eventType $eventType";
    }
  }

  void _updateCompletionPercent(stepName) {
    var currentStepIndex = stepNames.indexOf(stepName);
    if(currentStepIndex < 0) {
      throw "step $stepName not found in wizard";
    }
    int maxStepNumber = stepNames.length - 1;
    double percentDouble = (100 * currentStepIndex) / maxStepNumber;
    print("maxStepNumber: $maxStepNumber, currentStepIndex: $currentStepIndex, progress: $percentDouble");
    if (percentDouble > 100) {
      throw "too big progress, step: $stepName, index: $currentStepIndex";
    }
    completionPercent = percentDouble.toInt();
  }

  int _nextWizard() {
    if(currentWizard < (wizards.length - 1)) {
      return currentWizard+1;
    }
    return currentWizard;
  }

  bool _isValidWizardNumber(int wizardNumber) => wizardNumber >= 0 && wizardNumber < wizards.length;

  bool isAllWizardCompleted() {
    return _completedWizards.length == wizards.length;
  }

  bool isWizardCompleted(int wizard) {
    return _completedWizards.contains(_wizardNames[wizard]);
  }
}

class Wizard {
  final String name;
  final List<String> steps;

  Wizard(this.name, this.steps);
}
