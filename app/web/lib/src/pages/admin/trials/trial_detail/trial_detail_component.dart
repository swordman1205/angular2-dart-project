// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/account/account_component.dart';

@Component(
  selector: 'trial-detail',
  host: const {},
  styleUrls: const [''],
  styles: const ['''
  '''],
  templateUrl: 'trial_detail_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ],
)
class TrialDetailComponent implements OnInit {
  final RouteParams _routeParams;
  final TrialApi _trialApi;
  Trial trial;

  TrialDetailComponent(this._routeParams, this._trialApi);

  @override
  Future<Null> ngOnInit() async {
    var id = _routeParams.get('id');
    if (id != null) trial = await (_trialApi.getTrialById(id));
  }
}
