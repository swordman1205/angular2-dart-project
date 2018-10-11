// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/cards/enrich_profile_card_component.dart';
import 'package:app_web/src/cards/padaction_card_component.dart';
import 'package:app_web/src/cards/send_email_profile_card_component.dart';

@Component(
  selector: 'customer-home',
  host: const {'class': 'page-component'},
  styleUrls: const ['customer_home_component.css'],
  styles: const [''' 
  '''],
  templateUrl: 'customer_home_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    SendEmailProfileCardComponent,
    EnrichProfileCardComponent,
    PadActionCardComponent
  ],
)
class CustomerHomeComponent implements OnInit {
  final HealthApi _healthApi;
  final SecurityService _securityService;

  User currentUser;
  HealthInfo healthInfo;

  CustomerHomeComponent(this._healthApi, this._securityService);

  @override
  Future<Null> ngOnInit() async {
    currentUser = await _securityService.getCurrentUser();
    healthInfo = await _healthApi.getHealthInfo(currentUser.id);
  }
}
