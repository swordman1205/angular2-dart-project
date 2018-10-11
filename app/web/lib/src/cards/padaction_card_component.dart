// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';

@Component(
  selector: 'pad-action-card',
  styleUrls: const ['cards.css'],
  styles: const ['''
  '''],
  template: ''' 
    <div class="register-card card mt-30 mt-xs-15">
      <div *ngIf="sampleData == null">
        <div class="card-content">
          <p>Did your period just begin? Register your kit(s) and start sample collection.</p>
        </div>
         <!--<img class="card-image" src="images/cards/pad-action.png">-->
        <material-button class="btn btn-in btn-purple" raised [routerLink]="['/Customer/Sample/Step', { order: 1, step: 1 }]">
          Register
        </material-button>
      </div>
      <div *ngIf="sampleData != null">
        <div class="card-content">
          <p>You have started sample registration.</p>
        </div>
         <!--<img class="card-image" src="images/cards/pad-action.png">-->
        <material-button class="btn btn-in btn-purple btn--wide" raised [routerLink]="['/Customer/Sample/Step', { order: 1, step: 1 }]">
          Continue sample registration
        </material-button>
      </div>
    </div>
  ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES
  ],
)
class PadActionCardComponent implements OnInit  {

  final SampleApi _sampleApi;
  final SecurityService _securityService;

  StripSample sampleData;

  PadActionCardComponent(this._sampleApi, this._securityService);

  @override
  Future<Null> ngOnInit() async {
    String userId = await _securityService.getCurrentUserId();
    sampleData = await _sampleApi.getLatestNotFinishedSample(userId);
  }
}
