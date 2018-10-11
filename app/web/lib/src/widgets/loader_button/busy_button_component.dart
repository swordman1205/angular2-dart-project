// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';

@Component(
  selector: 'busy-button',
  styleUrls: const ['loader_button_component.css'],
  template: ''' 
  <material-button [class]="classes" [class.loading]="loading" (trigger)="btnTrigger()" raised>
    <ng-container *ngIf="!loading">
      <ng-content></ng-content>
     </ng-container>
   </material-button>
  ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ],
)
class BusyButtonComponent {
  @Input('innerClass') String classes = '';
  bool loading = false;

  @Input('busy')
  set busy(Future<dynamic> value) {
    if (value == null) {
      return;
    }
    value.then((_)=>loading = false);
  }

  final _trigger = new StreamController<Null>();
  @Output()
  Stream<Null> get trigger => _trigger.stream;

  btnTrigger() {
    if (!loading) {
      loading = true;
      _trigger.add(null);
    }
  }
}
