// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
//import 'dart:js';


@Component(
  selector: 'two-label-toggle',
  styleUrls: const ['two-label-toggle.css'],
  template: '''
    <div class="twoLabelToggle" (click)="toggle()">
      <span class="twoLabelToggle_btn firstLabel" [class.active]="isSelected(firstLabel)">
        <span class="text">{{firstLabel}}</span>
      </span>
      <span class="twoLabelToggle_btn secondLabel" [class.active]="isSelected(secondLabel)">
        <span class="text">{{secondLabel}}</span>
      </span>
    </div>
    ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ],
)
class TwoLabelToggleComponent implements OnInit {
  @Input()
  String firstLabel;

  @Input()
  String secondLabel;

  @Input()
  String selected;

  final _selectedRequest = new StreamController<String>();
  @Output()
  Stream<String> get selectedChange => _selectedRequest.stream;

  void toggle() {
    if (selected == firstLabel) {
      selected = secondLabel;
      _selectedRequest.add(secondLabel);
    } else {
      selected = firstLabel;
      _selectedRequest.add(firstLabel);
    }

  }

  bool isSelected(String toCompare) {
    return selected == toCompare;
  }

  @override
  Future<Null> ngOnInit() async {
    if (selected == null) {
      selected = firstLabel;
    }
  }

}
