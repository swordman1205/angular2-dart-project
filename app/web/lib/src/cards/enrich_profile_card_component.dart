// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';

@Component(
  selector: 'enrich-profile-card',
  styleUrls: const ['cards.css'],
  styles: const ['''
  '''],
  template: ''' 
    <div class="card">
      <div class="card-title">Contact Info</div>
      <div class="card-content">
        <p>Please keep your contact information updated.</p>
      </div>
      <!--<div class="card-image">
        <img  src="images/cards/enrich-profile.png">
      </div> -->
      <material-button class="btn btn-in btn-purple" raised [routerLink]="['/Customer/AccountDetails']">
        Edit
      </material-button>
    </div>
  ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES
  ],
)
class EnrichProfileCardComponent implements OnInit  {
  @override
  Future<Null> ngOnInit() async { }
}
