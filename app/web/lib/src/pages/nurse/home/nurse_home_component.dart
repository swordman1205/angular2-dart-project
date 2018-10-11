// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/model/customer_samples.dart';
import 'package:app_web/src/pages/nurse/customer_samples.dart';

@Component(
  selector: 'nurse-home',
  host: const {'class': 'page-component'},
  styles: const ['''
  '''],
  templateUrl: 'nurse_home_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
  ]
)
class NurseHomeComponent implements OnInit {
  final UserApi _userApi;
  final HealthApi _healthApi;
  final SecurityService _securityService;
  final CustomerSamplesService _customerSamplesService;
  final Router _router;

  User currentUser;
  List<CustomerSamples> samplesGroupedByCustomers;
  List<TrialSession> pickedUpHealthSessions;
  int pickedUpSampleCount;

  NurseHomeComponent(this._userApi, this._healthApi, this._securityService, this._customerSamplesService, this._router);

  @override
  Future<Null> ngOnInit() async {
    currentUser = await _securityService.getCurrentUser();

    //pickup cards
    var samples = [];
//    var samples = await _healthApi.getSampleForPickup();
//    if (samples.isNotEmpty) {
//      var customerIds = samples.map((s) => s.customerId).toSet();
//      List<CustomerInfo> customers = await _userApi.getCustomersByIds(customerIds);
//      samplesGroupedByCustomers = new List();
//      for (var customer in customers) {
//        var customerSamples = samples.where((s) => s.customerId == customer.id).toList();
//        samplesGroupedByCustomers.add(new CustomerSamples(customer, customerSamples));
//      }
//    }

    //deliver now cards
//    pickedUpHealthSessions = await _healthApi.getPickedUpHealthSessions(currentUser.id);
//    pickedUpSampleCount = pickedUpHealthSessions.fold(0, (count, hs)=>count + hs.sampleIds.length);
  }

  void pickup(CustomerSamples customerSamples) {
    _customerSamplesService.customerSamples = customerSamples;
    _router.navigate([
      '../Pickup'
    ]);
  }

  Future<Null> deliver() async {
//    var healthSessionIds = pickedUpHealthSessions.map((hs)=>hs.id).toList();
//    print("health sessions $healthSessionIds will be delivered");
//    _healthApi.deliver(pickedUpHealthSessions);
//    pickedUpHealthSessions = [];
  }



}

