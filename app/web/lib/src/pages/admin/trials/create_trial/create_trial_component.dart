// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'package:app_facade/app_facade.dart';
import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';

@Component(
  selector: 'create-trial',
  //styleUrls: const ['assays_component.css'],
  templateUrl: 'create_trial_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    formDirectives,
    ROUTER_DIRECTIVES,
  ],
  pipes: const [COMMON_PIPES],
)
class CreateTrialComponent implements OnInit {
  final TrialApi _trialApi;
  ControlGroup form;

  final _resultEmitter = new StreamController<bool>();
  @Output()
  Stream<bool> get onAction => _resultEmitter.stream;

  CreateTrialComponent(this._trialApi);

  @override
  ngOnInit() {
    form = new FormBuilder().group({
      "name": [null, Validators.compose([
        Validators.required,
        Validators.minLength(2)])]
    });
  }

  onSubmit() async {
    var trial = prepareTrial();
    var id = await _trialApi.saveTrial(trial);
    trial.id = id;
    print("created trial ${trial.name} with id $id");
    _resultEmitter.add(true);
  }

  Trial prepareTrial() {
    var formModel = form.value;
    var trial = new Trial();
    trial.name = formModel["name"];
    return trial;
  }
}

