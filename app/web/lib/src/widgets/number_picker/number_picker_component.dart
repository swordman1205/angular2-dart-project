// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';

@Component(
  selector: 'number-picker',
  styles: const ['''
  '''],
  template: ''' 
    <material-fab (click)="decrement()"  [disabled]="!canDecrement" class="number-picker-decrement">
      <material-icon icon="remove"></material-icon>
    </material-fab>
    <span class="number-picker-value">{{value}} {{days}}</span>
    <material-fab (click)="increment()" [disabled]="!canIncrement" class="number-picker-increment">
      <material-icon icon="add"></material-icon>
    </material-fab>
  ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ],
)
class NumberPickerComponent implements OnInit {
  String days;

  @Input()
  int value;

  @Input()
  int min;

  @Input()
  int max;

  final _valueChange = new StreamController<int>();
  @Output()
  Stream<int> get valueChange => _valueChange.stream;

  bool get canDecrement => (min == null ? true : value > min);

  bool get canIncrement => (max == null ? true : value < max);

  decrement() {
    if(canDecrement) {
      value--;
      _valueChange.add(value);
      _updateDays();
    }
  }

  increment() {
    if(canIncrement) {
      value++;
      _valueChange.add(value);
      _updateDays();
    }
  }

  void _updateDays() {
    if ( value == 1 ) {
      days = 'day';
    } else {
      days = 'days';
    }
  }

  @override
  ngOnInit() {
    value ??= (min??0);
    _updateDays();
    print("min $min, value $value, max = $max");
  }
}
