import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular/di.dart';

/**
 * Represents a wizard comprised of multiple steps. A wizard may itself be part of a larger wizard flow
 */
abstract class WizardComponent {
  final _stateChange = new StreamController<String>();
  List<String> _stepNames;
  String _currentStep; // the current step in a wizard
  int _currentStepPosition;
  Map<String, List<String>> _dependentSteps = {};

  /// Ordered list of steps in this wizard
  @Input()
  void set steps(List<String> stepNames) => _setStepNames(stepNames);
  List<String> get steps => _stepNames;

  /// True when this wizard is active
  @Input()
  bool active;

  /// Name of wizard
  @Input()
  String name;

  @Input('activeStep') set updateStep(int step) {
    _currentStep = steps[step];
    _currentStepPosition = step;
  }

  int get currentStep => _currentStepPosition;

  @Output()
  Stream<String> get stateChange => _stateChange.stream;
  /// Confirm steps in wizard are completed
  Future<Null> confirm() async {
    if(await isEveryStepCompleted()) {
      _emitEvent("confirm");
    }
  }

  /// Validate if a given step is completed, implemented by each concrete
  /// wizard as they will know if transition conditions are completed
  Future<bool> isStepCompleted(String stepName);

  /// True if a next step exists
  bool get hasNext => !isCurrentStep(steps.last);

  /// True if a previous step exists
  bool get hasPrevious => !isCurrentStep(steps.first);

  String getProgressIcon(String stepName) => isCurrentStep(stepName) ? 'lens' : 'panorama_fish_eye';

  bool isCurrentStep(String stepName) => _currentStep == stepName;

  bool submitted = false;

  Future<bool> isDependentStepCompleted(String stepName) async {
    var dependentSteps = _dependentSteps[stepName];
    for (String step in dependentSteps) {
      if (!await isStepCompleted(step)) {
        return false;
      }
    }
    return true;
  }

  Future<bool> isEveryStepCompleted() async {
    for (String step in steps) {
      if (!await isStepCompleted(step)) {
        return false;
      }
    }
    return true;
  }

    void navigateToPrevious() {
      if(hasPrevious) {
        var stepName = steps[(_currentStepPosition - 1)];
        _setCurrentStep(stepName);
      } else {
        _emitEvent("previousWizard");
      }
    }

    Future<Null> navigateToNext() async {
      if (!await isStepCompleted(_currentStep)) {
        print("step $_currentStep is not complete can`t navigate to next step");
        return;
      }
      submitted = false;
      if(hasNext) {
        var stepName = steps[(_currentStepPosition + 1)];
        _setCurrentStep(stepName);
      } else {
        confirm();
      }
    }

    /// Navigate directly to step in wizard if allowed
    void navigateToStep(String stepName) {
      /*
    if(_currentStep != null && isStepCompleted(stepName)) {
      // allow navigation to already completed step
      _setCurrentStep(stepName);
      return;
    }
    */
      _setCurrentStep(stepName);

//    if(isStepCompleted(_currentStep)) {
//      if(isDependentStepCompleted(stepName)) {
//        // allow navigation to new step if current and all dependent steps are completed
//        _setCurrentStep(stepName);
//      } else {
//        print("cannot navigate to $stepName as dependent steps are not completed");
//      }
//    } else {
//      print("cannot navigate to $stepName as current step $_currentStep is not complete");
//    }
    }

    void toggle() {
      active = !active;
      _emitEvent( active ? "active:$_currentStepPosition" : "inactive" );
    }

    /// Navigate directly to step in wizard
    void _setCurrentStep(String stepName) {
      if(stepName == _currentStep) {
        return;
      }
      for(int i=0; i<steps.length; i++) {
        if(steps[i] == stepName) {
          print("navigating from step $_currentStep to $stepName");
          _currentStep = stepName;
          _currentStepPosition = i;
          _emitEvent("stepChange:$stepName");
          onStepChange(stepName);
        }
      }
    }

    void onStepChange(String stepName) {
      //could be overrided
    }

    void jumpToNextStep() {
      submitted = true;
      navigateToNext();
    }

    void resetSubmitted() {
      submitted = false;
    }

    _emitEvent(String event) => _stateChange.add("$name:$event");

    _setStepNames(List<String> stepNames ) {
      var visitedSteps = <String>[];
      var dependentSteps = new Map<String, List<String>>();
      for(int i=0; i<stepNames.length; i++) {
        var stepName = stepNames[i];
        if(visitedSteps.contains(stepName)) {
          throw "step with name $stepName already exists";
        }
        // the dependent steps are the steps you must have completed in order to be at current step
        dependentSteps[stepName] = new List<String>.from(visitedSteps);
        visitedSteps.add(stepName);
      }
      _stepNames = stepNames;
      _dependentSteps = dependentSteps;
    }
}
