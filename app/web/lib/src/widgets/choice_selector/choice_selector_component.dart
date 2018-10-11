// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:app_facade/app_facade.dart';

@Component(
  selector: 'choice-selector',
  styleUrls: const ['choice_selector_component.css'],
  styles: const ['''
  '''],
  template: ''' 
    <div *ngIf="dots == false" class="choice-selector choice-buttons-wrapper">
        <span *ngFor="let option of options" class="selector_option" [class.active]="isSelected(option)" (click)="toggle(option)">
            {{option.caption}}
        </span>
    </div> 
    <div *ngIf="dots == true" class="choice-selector choice-dots-wrapper">
        <span *ngFor="let option of options" class="selector_option" [class.active]="isSelected(option)" (click)="toggle(option)">
          <span class="dot"></span>
          <span class="option-name">{{option.caption}}</span>
        </span>
    </div> 
  ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ],
)
class ChoiceSelectorComponent {

  @Input()
  List<EnumWrapper> options;

  @Input()
  List<EnumWrapper> selected;

  // output select, emit latest selected item
  final _selectEmitter = new StreamController<EnumWrapper>();
  @Output()
  Stream<EnumWrapper> get select => _selectEmitter.stream;

  @Input()
  bool multiple = false;

  @Input()
  bool dots = false;

  @Input()
  String toggleChoice = '';

  @Input()
  String componentId;

  bool isValidOption(EnumWrapper option) {
    return options.any((ew)=>ew.name == option.name);
  }

  bool isValidState() {
    for (EnumWrapper sel in selected) {
      if (!options.any((ew)=>ew.name == sel.name)) {
        return false;
      }
    }
    return true;
  }

  String _getComponentId() {
    return componentId ?? "no component id";
  }

  void toggle(EnumWrapper option) {
    if (!isValidState()) {
      throw "${_getComponentId()}: invalid state, options: ${options}, selected: ${selected}";
    }
    var optionNames = options.map((ew)=>ew.name).toList();
    if (!isValidOption(option)) {
      throw "${_getComponentId()}: invalid option toggled : ${option.name}";
    }
    if (!optionNames.contains(option.name)) {
      print("${_getComponentId()}: invalid option toggled : ${option.name}");
      return;
    }
    if ( multiple ) {
      if (selected.contains(option)) {
        selected.remove(option);
      } else {
        if (option.caption == toggleChoice) {
          selected.clear();
        } else {
          EnumWrapper noneOption = getNoneOption();
          if (selected.contains(noneOption)) {
            selected.remove(noneOption);
          }
        }
        selected.add(option);
      }
    } else {
      selected.clear();
      selected.add(option);
    }
    _selectEmitter.add(option);
    print("${_getComponentId()} selected: ${selected.map((ew) => ew.name)}, option names: ${optionNames}");
  }

  bool isSelected(EnumWrapper option) {
    return selected.contains(option);
  }

  EnumWrapper getNoneOption() {
    return options.firstWhere((EnumWrapper option) => option.caption == toggleChoice, orElse: () => null);
  }
}