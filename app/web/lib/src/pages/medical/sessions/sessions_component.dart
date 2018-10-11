// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/directives/row_hover_directive.dart';
import 'package:app_web/src/pipes/age_pipe.dart';
import 'package:app_web/src/pipes/enum_pipe.dart';
import 'package:app_web/src/pipes/short_id_pipe.dart';

@Component(
    selector: 'sessions',
    host: const {'class': 'page-component'},
    styleUrls: const ['sessions_component.css'],
    templateUrl: 'sessions_component.html',
    directives: const [
      CORE_DIRECTIVES,
      materialDirectives,
      ROUTER_DIRECTIVES,
      RowHoverDirective,
    ],
    pipes: const[AgePipe, EnumPipe, ShortIdPipe, COMMON_PIPES]
)
class SessionsComponent implements OnInit {
  final TrialApi _trialApi;
  final UserApi _userApi;
  Iterable<TrialSession> sessions;
  Map<String, CustomerInfo> customersMap = {};
  SessionsComponent(this._trialApi, this._userApi);

  @override
  Future<Null> ngOnInit() async {
    var trials = await _trialApi.getTrials();
    if (trials.length != 1) {
      throw "There should be one trial";
    }
    var trial = trials.first;
    sessions = await _trialApi.getTrialSessions(trial.id);
    if (sessions.isNotEmpty) {
      Iterable<TrialParticipant> trialParticipants = await _trialApi.getTrialParticipantsByIds(sessions.map((ts)=>ts.trialParticipantId));
      Iterable<CustomerInfo> customers = await _userApi.getCustomersByUserIds(trialParticipants.map((tp)=>tp.customerId));
      customersMap = {};
      for (var trialParticipant in trialParticipants) {
        var customer = customers.firstWhere((ci)=>ci.userId == trialParticipant.customerId);
        customersMap[trialParticipant.id] = customer;
      }
    }
  }

}
