// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/directives/row_hover_directive.dart';
import 'package:app_web/src/pages/admin/organizations/create_organization/create_organization_component.dart';
import 'package:app_web/src/pipes/customer_address_pipe.dart';
import 'package:app_web/src/pipes/enum_pipe.dart';

@Component(
  selector: 'sessions',
  host: const {'class': 'page-component'},
  styleUrls: const ['sessions_component.css'],
  templateUrl: 'sessions_component.html',
  directives: const [
    RowHoverDirective,
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
    formDirectives,
    CreateOrganizationComponent,
  ],
  pipes: const [COMMON_PIPES, EnumPipe, CustomerAddressPipe],
)
class SessionsComponent implements OnInit {
  final UserApi _userApi;
  final TrialApi _trialApi;
  final SecurityService _securityService;

  List<ParticipantAndCustomer> participantCustomers = [];

  SessionsComponent(this._userApi, this._trialApi, this._securityService);

  @override
  ngOnInit() async {
    var nurseId = await _securityService.getCurrentUserId();
    Iterable<TrialParticipant> participants = await _trialApi.getTrialParticipantByNurseId(nurseId, allowedStatuses: ["ACTIVE", "NURSE_CONFIRMED"]);
    if (participants.isNotEmpty) {
      var customerUserIds = participants.map((p) => p.customerId);
      Iterable<CustomerInfo> customers = await _userApi.getCustomersByUserIds(customerUserIds);

      for (var customer in customers) {
        var participant = participants.where((p) => p.customerId == customer.userId).first;
        participantCustomers.add(new ParticipantAndCustomer(customer, participant));
      }
    }
  }
}

class ParticipantAndCustomer {
  TrialParticipant participant;
  CustomerInfo customer;

  ParticipantAndCustomer(this.customer, this.participant);
}
