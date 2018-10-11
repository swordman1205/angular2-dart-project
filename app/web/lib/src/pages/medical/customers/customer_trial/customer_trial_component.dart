// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pipes/age_pipe.dart';
import 'package:app_web/src/pipes/enum_pipe.dart';

@Component(
    selector: 'customer-trial',
    host: const {'class': 'page-component'},
    styleUrls: const ['customer_trial_component.css'],
    templateUrl: 'customer_trial_component.html',
    directives: const [
      CORE_DIRECTIVES,
      formDirectives,
      materialDirectives,
      materialNumberInputDirectives,
      ROUTER_DIRECTIVES,
    ],
    pipes: const [COMMON_PIPES, AgePipe, EnumPipe]
)
class CustomerTrialComponent implements OnInit {
  final UserApi _userApi;
  final TrialApi _trialApi;

  @Input()
  String customerId;

  TrialParticipant trialParticipant;
  // selection lists
  Iterable<User> nurses = [];
  Iterable<Trial> trials = [];
  // selected values
  User nurse;
  Trial trial;

  CustomerTrialComponent(this._userApi, this._trialApi);

  @override
  Future<Null> ngOnInit() async {
    nurses = await _userApi.getNurses();
    trials = await _trialApi.getTrials();

    Iterable<TrialParticipant> participation = await _trialApi.getCustomerTrialParticipation(customerId);
    if (participation.length > 1) {
      throw "currently customer can only be active in one trial";
    } else if (participation.length == 1) {
      trialParticipant = participation.first;
      trial = trials.firstWhere((t)=>t.id == trialParticipant.trialId);
      nurse = nurses.firstWhere((n)=>n.id == trialParticipant.nurseId, orElse: null);
    }
  }

  Future<Null> approveCustomer() async {
    var participantId = await _trialApi.assignCustomerToTrial(customerId, trial.id);
    await _trialApi.assignNurseToParticipant(participantId, nurse.id);
    await _trialApi.updateParticipantStatus(participantId, "APPROVED");
    trialParticipant = await _trialApi.getTrialParticipantById(participantId);
  }

  Future<Null> rejectCustomer() async {
    var participantId = await _trialApi.assignCustomerToTrial(customerId, trial.id);
    if (nurse != null) {
      await _trialApi.assignNurseToParticipant(participantId, nurse.id);
    }
    await _trialApi.updateParticipantStatus(participantId, "REJECTED");
    trialParticipant = await _trialApi.getTrialParticipantById(participantId);
  }
}
