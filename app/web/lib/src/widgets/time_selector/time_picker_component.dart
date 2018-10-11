// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:app_web/src/widgets/two_label_toggle/two_label_toggle.dart';

@Component(
  selector: 'time-selector',
  host: const {'class': 'time-selector'},
  styleUrls: const ['time_picker_component.css'],
  styles: const ['''
  '''],
  templateUrl: 'time_picker_component.html',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
    materialNumberInputDirectives,
    TwoLabelToggleComponent,
  ],
)
class TimeSelectorComponent implements OnInit, AfterViewChecked  {
  String _currentTime;
  num hours, minutes;
  String jm;

  @Input()
  String label;

  @Input()
  void set value(String val) {
    if(val != null && val.isNotEmpty) {
      print("value is $val");
      var nowParts = val.split(":");
      hours = int.parse( nowParts[0] );
      minutes = int.parse( nowParts[1] );
      jm = nowParts[2];
    }
  }
  String get value {
    if(hours == null || minutes == null) {
      return null;
    }
    return "${hours.round()}:${minutes.round()}:$jm";
  }

  // output event
  final _valueChanged = new StreamController<String>();
  @Output()
  Stream<String> get valueChanged => _valueChanged.stream;

  @override
  Future<Null> ngOnInit() async { }

  @override
  void ngAfterViewChecked() {
    if(hours != null && minutes != null) {
      var selectedTime = value;
      if(_currentTime != selectedTime) {
        // only emit changed values
        _valueChanged.add(selectedTime);
        _currentTime = selectedTime;
        print("selected time $selectedTime");
      }
    }
  }
}
