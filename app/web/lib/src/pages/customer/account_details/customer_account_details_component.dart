import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular/src/common/directives/core_directives.dart';
import 'package:angular/src/core/metadata.dart';
import 'package:angular_components/angular_components.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/account/account_component.dart';
// Copyright (c) 2017, Qurasense. All rights reserved.


@Component(
  selector: 'customer-account-details',
  host: const {},
  styleUrls: const [''],
  styles: const ['''
  :host {
    margin-bottom: 50px;
    width: calc(100% - 60px);
  }
  @media screen and (min-width: 992px) {
    :host {
      display: block;
      margin: 0 auto 50px;
    }
  }
  '''],
  template: ''' 
    <template [ngIf]="currentUser != null">
      <account [user]="currentUser"></account>
    </template>
  ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    AccountComponent,
  ],
)
class CustomerAccountDetailsComponent implements OnInit {
  final SecurityService _securityService;

  User currentUser;

  CustomerAccountDetailsComponent(this._securityService);

  @override
  Future<Null> ngOnInit() async {
  currentUser = await _securityService.getCurrentUser();
  }

}
