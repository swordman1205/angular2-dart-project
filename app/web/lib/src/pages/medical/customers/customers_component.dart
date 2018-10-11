// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/directives/row_hover_directive.dart';
import 'package:app_web/src/pipes/age_pipe.dart';
import 'package:app_web/src/pipes/enum_pipe.dart';

@Component(
  selector: 'customers',
  host: const {'class': 'page-component'},
  styleUrls: const ['customers_component.css'],
  templateUrl: 'customers_component.html',
    directives: const [
      CORE_DIRECTIVES,
      materialDirectives,
      ROUTER_DIRECTIVES,
      RowHoverDirective,
    ],
  pipes: const[AgePipe, EnumPipe, COMMON_PIPES]
)
class CustomersComponent implements OnInit {
  final UserApi _userApi;
  final GatewayApi _gatewayApi;
  final TrialApi _trialApi;
  final HealthApi _healthApi;

  List<CustomerDetail> customerDetails;
  String action;

  CustomersComponent(this._userApi, this._gatewayApi, this._trialApi, this._healthApi);

  @override
  Future<Null> ngOnInit() async {
    Iterable<CustomerInfo> customers = await _userApi.getCustomers();
    customerDetails = new List<CustomerDetail>();
    for (CustomerInfo ci in customers) {
      var healthInfo = await _healthApi.getHealthInfo(ci.userId);
      var customerDetail = new CustomerDetail(ci, healthInfo);
      customerDetails.add(customerDetail);
      var trialParticipation = await _trialApi.getCustomerTrialParticipation(ci.userId);
      if (trialParticipation.length > 0) {
        var participant = trialParticipation.first;
        var trial = await _trialApi.getTrialById(participant.trialId);
        customerDetail.trial = trial;
        customerDetail.participant = participant;
        var sessions = await _trialApi.getSessionsForParticipant(participant.id);
        customerDetail.sessionCompleted = sessions.where((s) => s.status != 'ACTIVE').length;
        var activeSessions = sessions.where((s) => s.status == 'ACTIVE');
        if (activeSessions.length > 1) {
          throw "Paritcipant can have only one active session";
        } else if (activeSessions .length == 1) {
          customerDetail.activeSession = activeSessions.first;
        }
      }
    }
  }

  DateTime calculateNextPeriod(HealthInfo healthInfo) {
    return PeriodUtils.calculateNextPeriod(healthInfo);
  }

  Future<Null> downloadCustomers() async {
    _userApi.downloadCustomers();
  }

  Future<Null> downloadCustomersAndHealths() async {
    _gatewayApi.downloadCustomersAndHealths();
  }
}

class CustomerDetail {
  CustomerInfo customer;
  Trial trial;
  TrialParticipant participant;
  HealthInfo health;
  int sessionCompleted;
  TrialSession activeSession;
  CustomerDetail(this.customer, this.health);
}
