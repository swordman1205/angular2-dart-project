// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/account/account_component.dart';

@Component(
  selector: 'medical-account',
  host: const {},
  styleUrls: const [''],
  styles: const ['''
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
class  MedicalAccountComponent implements OnInit {
  final SecurityService _securityService;

  User currentUser;

  MedicalAccountComponent(this._securityService);

  @override
  Future<Null> ngOnInit() async {
    currentUser = await _securityService.getCurrentUser();
  }

}
