// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/directives/row_hover_directive.dart';
import 'package:app_web/src/pages/admin/trials/create_trial/create_trial_component.dart';

@Component(
  selector: 'trials',
  host: const {'class': 'page-component'},
  styleUrls: const ['trials_component.css'],
  templateUrl: 'trials_component.html',
  directives: const [
    RowHoverDirective,
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
    formDirectives,
    CreateTrialComponent,
  ],
  pipes: const [COMMON_PIPES],
)
class TrialsComponent implements OnInit {
  final TrialApi _trailApi;
  String action;
  Iterable<Trial> trials;

  TrialsComponent(this._trailApi);

  void createTrial() {
    action = "CREATE";
  }

  @override
  Future<Null> ngOnInit() async {
    trials = await _trailApi.getTrials();
    action = null;
  }

  Future<Null> onAction(bool result) async {
    print("executed $action with result $result");
    if(result) {
      trials = await _trailApi.getTrials();
    }
    action = null;
  }
}
