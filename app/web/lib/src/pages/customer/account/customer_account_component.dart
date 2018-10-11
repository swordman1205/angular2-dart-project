// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/cards/enrich_profile_card_component.dart';
import 'package:app_web/src/cards/padaction_card_component.dart';
import 'package:app_web/src/cards/send_email_profile_card_component.dart';

@Component(
  selector: 'customer-account',
  host: const {'class': 'page-component'},
  styleUrls: const [''],
  styles: const ['''
  :host {
    box-shadow: 0px -1px 4px rgba(0, 0, 0, 0.25);
    height: 100%;
  }
  @media screen and (max-width: 991px) {
    :host {
      display: flex;
      flex-direction: column;
      width: 100%;
      overflow: auto;
      padding-bottom: 15px;
      margin-bottom: 65px;
    }
  }
  .account-actions {
    padding: 0 30px;
  }
  '''],
  template: ''' 
    <template [ngIf]="currentUser != null">
      <div class="card-list">
        <send-email-profile-card [customerUser]="currentUser"></send-email-profile-card>
        <enrich-profile-card></enrich-profile-card>
    </div>
    <div class="account-actions">
        <material-button class="btn btn-out btn-purple" (trigger)="logout()">Logout</material-button>
        <material-button class="btn btn-out btn-purple">Delete</material-button>
      </div>
    </template>
  ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    EnrichProfileCardComponent,
    PadActionCardComponent,
    SendEmailProfileCardComponent,
  ],
)
class CustomerAccountComponent implements OnInit  {

  final SecurityService _securityService;
  final UserApi _userApi;
  final Router _router;

  User currentUser;

  CustomerAccountComponent(this._securityService, this._userApi, this._router);

  @override
  Future<Null> ngOnInit() async {
    currentUser = await _securityService.getCurrentUser();
  }

  Future<Null> logout() async {
    await _userApi.logout();
    _router.navigate(["/Site/Home"]);
  }

}
