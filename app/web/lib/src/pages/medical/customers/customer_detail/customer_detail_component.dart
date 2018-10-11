// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/medical/customers/customer_trial/customer_trial_component.dart';
import 'package:app_web/src/pipes/age_pipe.dart';
import 'package:app_web/src/pipes/enum_pipe.dart';

@Component(
  selector: 'customer-detail',
  host: const {'class': 'page-component'},
  styleUrls: const ['customer_detail_component.css'],
  templateUrl: 'customer_detail_component.html',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
    materialNumberInputDirectives,
    CustomerTrialComponent,
    ROUTER_DIRECTIVES,
  ],
  pipes: const [COMMON_PIPES, AgePipe, EnumPipe]
)
class CustomerDetailComponent implements OnInit {
  final UserApi _userApi;
  final TrialApi _trialApi;
  final HealthApi _healthApi;
  final RouteParams _routeParams;
  final Router _router;

  HealthInfo healthInfo;
  TrialParticipant trialParticipant;
  TrialSession trialSession;
  CustomerInfo customer;
  String customerStatusError;

  CustomerDetailComponent(this._userApi, this._trialApi, this._healthApi, this._routeParams, this._router);

  @override
  Future<Null> ngOnInit() async {
    var customerId = _routeParams.get('id');
    if (customerId != null) {
      customer = await _userApi.getCustomerInfo(customerId);
      healthInfo = await _healthApi.getHealthInfo(customerId);
      var participation = await _trialApi.getCustomerTrialParticipation(customerId);
      if(participation.length == 1) {
        // assume only member of one trial
        trialParticipant = participation.first;
        var sessions = await _trialApi.getSessionsForParticipant(trialParticipant.id, status: "ACTIVE");
        if(sessions.isNotEmpty)
          trialSession = sessions.first;
      }
    }
  }

  String formatExpectedNextPeriod(HealthInfo healthInfo) {
    if (healthInfo == null) {
      return '';
    }
    var calculateNextPeriod = PeriodUtils.calculateNextPeriod(healthInfo);
    String formattedDate = new DatePipe().transform(calculateNextPeriod, 'MM.dd.y');
    var daysTillPeriod = TimeUtils.durationBetween(TimeUtils.atDayStart(new DateTime.now()), calculateNextPeriod).inDays;
    return "$formattedDate ($daysTillPeriod)";
  }

  void sendKits() {
    _trialApi.sendKits(trialParticipant.id);
  }

  void openApproveForm() {
    _router.navigate([ "../CustomerApprove", {'id': customer.userId.toString()}]);
  }
}
