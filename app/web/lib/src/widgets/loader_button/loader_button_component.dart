// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';

@Component(
  selector: 'loader-button',
  styleUrls: const ['loader_button_component.css'],
  template: ''' 
  <material-button [class]="classes" [class.loading]="loading" (trigger)="btnTrigger()" raised>
    <ng-container *ngIf="!loading">
      {{title}}
      <ng-content></ng-content>
     </ng-container>
   </material-button>
  ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ],
)
class LoaderButtonComponent {
  @Input() String title = '';
  @Input('innerClass') String classes = '';
  @Input() bool loading = false;

  final _trigger = new StreamController<Null>();
  @Output()
  Stream<Null> get trigger => _trigger.stream;

  btnTrigger() {
    if (!loading) {
      _trigger.add(null);
    }
  }
}
