// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:app_facade/app_facade.dart';

const DATE_SELECTOR_VALUE_ACCESSOR = const Provider(NG_VALUE_ACCESSOR,
    useExisting: DateSelectorComponent, multi: true);
const DATE_SELECTOR_VALIDATOR = const Provider(NG_VALIDATORS,
    useExisting: DateSelectorComponent, multi: true);

@Component(
  selector: 'date-selector',
  styleUrls: const ['date_selector_component.css'],
  styles: const ['''
  '''],
  template: '''
    <div class="date-selector-component-form">
      <div class="dateInput" *ngIf="isMobile">
        <input type="date"
          [max]="maxDate"
          placeholder="mm/dd/yyyy"
          [(ngModel)]="dateHtml5"
          (change)="emitHtml5Date()">
      </div>
      <div class="dateMask" *ngIf="!isMobile">
        <input type="text"
          placeholder="mm/dd/yyyy"
          #dateMask
          required/>
        <div [hidden]="!processInput" class="help-box">
          <span>MM/DD/YYYY</span>
        </div>
      </div>
    </div>
  ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    formDirectives
  ],
  providers: const [
    DATE_SELECTOR_VALUE_ACCESSOR,
    const Provider(NG_VALIDATORS,
        useExisting: DeferredValidator, multi: true),
  ]
)
class DateSelectorComponent implements AfterViewInit, ControlValueAccessor<DateTime> {

  static final IPHONE_MARKER = 'iphone';
  static final ANDROID_MARKER = 'android';
  bool processInput = false;

  Function _onChange;
  TouchFunction _onTouched;

  String dateHtml5;
  String _dateMask;

  @ViewChild('dateMask')
  ElementRef inputRef;

  String maxDate;

  @Input('maxDate') set updateMaxDate(DateTime date) {
    maxDate = '${date.year}-${date.month < 10? '0${date.month}' : date.month}-${date.day < 10? '0${date.day}' : date.day}';
  }

  String _finishShortSection(String str) {
    return str.length == 2 ? str + ' / ' : str;
  }

  String _checkValue(String str, int max) {
    if (str.substring(0,1) != '0' || str == '00') {
      int num;
      try {
        num = int.parse(str);
        if (num <= 0 || num > max) num = 1;
      } catch (e) {
        num = 1;
      }
      str = num > max && num < 10 ? '0' + num.toString() : num.toString();
    };
    return str;
  }
  
  bool get isMobile {
    var userAgent = window.navigator.userAgent.toLowerCase();
    var platform = window.navigator.platform.toLowerCase();

    bool iphone = userAgent.contains(IPHONE_MARKER) || platform.contains(IPHONE_MARKER);
    bool android = userAgent.contains(ANDROID_MARKER) || platform.contains(ANDROID_MARKER);
    return iphone || android;
  }

  String get userAgent => window.navigator.userAgent;

  String get platform => window.navigator.platform;

  @override
  ngAfterViewInit() {
    if (isMobile) {
      print("using plain html5 date");
    } else {
      print("using mask input text for date");
      TextInputElement inputElement = inputRef.nativeElement;
      inputElement.value = _dateMask;
      _emitMaskDate();
      inputElement.onInput.listen((e) {
        print('process input');
        processInput = true;
        var input = inputElement.value;
        if (input.endsWith(' /') && input.length > 2) {
          print("match year and day delete");
          input = input.substring(0, input.length - 3);
        }
        List<String> values = input.split('/').map((v) => v.replaceAll(new RegExp(r'\D'), '')).toList();
        if (values.length > 0 && values[0].isNotEmpty) {
          values[0] = _checkValue(values[0], 12);
        }
        if (values.length > 1 && values[1].isNotEmpty) {
          values[1] = _checkValue(values[1], 31);
        }
        String output = '';
        for (int i = 0; i < values.length; i++) {
          if (i < 2) {
            output += _finishShortSection(values[i]);
          } else {
            output += values[i];
          }
        }
        if (output.length > 14) {
          inputElement.value = output.substring(0, 14);
        } else {
          inputElement.value = output;
        }
        _emitMaskDate();
        if (output.length == 14 || output.length == 0) {
          processInput = false;
        }
      });
//      inputElement.onChange.listen((e) {
//        print("mask date changed, new value:" + inputElement.value);
//      });
//      inputElement.onBlur.listen((e) {
//        _emitMaskDate();
//      });
    }
  }

  void _emitMaskDate() {
    if (inputRef == null) {
      print('input not yet inited');
      return null;
    }
    if (_onChange == null) {
      print('_onChange not yet inited');
      return null;
    }
    TextInputElement inputElement = inputRef.nativeElement;
    String value = inputElement.value;
    if (value.isEmpty) {
      _onChange(null);
    } else {
      try {
        List<int> values = value.split('/').map((String part) => int.parse(part.trim())).toList();
        if (values.length == 3) {
          _onChange(values[2].toString().length == 4? new DateTime(values[2], values[0], values[1]) : new DateTime(0, 0, 0));
        } else {
          _onChange(new DateTime(0, 0, 0));
        }
      } catch (e) {}
    }
  }

  void emitHtml5Date() {
    if (_onChange == null) {
      print('_onChange not yet inited');
      return;
    }
    _onChange(TimeUtils.parseDateTime(dateHtml5));
  }

  @override
  void registerOnChange(ChangeFunction<DateTime> f) {
    _onChange = (value) {
      f(value, rawValue: TimeUtils.formatDate(value));
    };
    if (isMobile && dateHtml5 != null) {
      emitHtml5Date();
    }
  }

  @override
  void registerOnTouched(TouchFunction f) {
    _onTouched = f;
  }

  @override
  void writeValue(DateTime date) {
    if (date == null) {
      //nothing to set on null value
    } else if (isMobile) {
      dateHtml5 = TimeUtils.formatDate(date);
      emitHtml5Date();
    } else {
      var monthString = date.month.toString().padLeft(2, '0');
      var dayString = date.day.toString().padLeft(2, '0');
      _dateMask = '${monthString} / ${dayString} / ${date.year}';
      //somtimes writeValue called first, sometimes ngAfterViewInit. Thats why
      if (inputRef != null) {
        TextInputElement inputElement = inputRef.nativeElement;
        inputElement.value = _dateMask;
      }
      _emitMaskDate();
    }

  }

}
