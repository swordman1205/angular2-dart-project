// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:app_facade/app_facade.dart';

@Component(
  selector: 'number-selector',
  styles: const ['''
  '''],
  template: ''' 
  <template [ngIf]="numberOptions != null">
    <material-dropdown-select
      [buttonText]="numberSelectionLabel"
      [selection]="numberSelection"
      [options]="numberOptions"
      [popupMatchInputWidth]="true"
      [itemRenderer]="itemRenderer"
      required>
    </material-dropdown-select>
  </template>
  ''',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
    materialNumberInputDirectives
  ],
)
class NumberSelectorComponent implements OnInit, AfterViewChecked {
  int _currentNumber;

  @Input()
  int min;

  @Input()
  int max;

  @Input()
  String label;

  @Input()
  void set value(int val) {
    if(int != null) {
      if(val < min || val > max) {
        throw "value $val is not between $min and $max";
      }
      numberSelection.select(  new EnumWrapper("$val", "$val") );
    }
  }

  // number selection
  StringSelectionOptions<EnumWrapper> numberOptions;
  SelectionModel<EnumWrapper> numberSelection = new SelectionModel<EnumWrapper>.withList(allowMulti: false);
  ItemRenderer<EnumWrapper> itemRenderer = new CachingItemRenderer<EnumWrapper>((enumWrapper) => "${enumWrapper.caption}");
  String get numberSelectionLabel => numberSelection.selectedValues.length > 0 ? itemRenderer(numberSelection.selectedValues.first) : label;

  // output event
  final _valueChanged = new StreamController<int>();
  @Output()
  Stream<int> get valueChanged => _valueChanged.stream;

  @override
  Future<Null> ngOnInit() async {
    if(numberOptions == null) {
      // generate list of values
      if(min == null || max == null) {
        throw "min and max values must be provided";
      }
      if(min > max) {
        throw "min value must be less than max";
      }
      var numberSelectionValues = new List<EnumWrapper>();
      for(int i=min; i<=max; i++) {
        numberSelectionValues.add(new EnumWrapper("$i", "$i"));
      }
      numberOptions = new StringSelectionOptions<EnumWrapper>(numberSelectionValues);
    }
  }

  @override
  ngAfterViewChecked() {
    if(numberSelection.isNotEmpty) {
      var selectionValue = numberSelection.selectedValues.first.name;
      int selectedNumber = int.parse(selectionValue);
      if(_currentNumber != selectedNumber) {
        // only emit changed values
        _valueChanged.add(selectedNumber);
        _currentNumber = selectedNumber;
        print("selected number $selectedNumber");
      }
    }
  }
}

