import 'package:angular_forms/angular_forms.dart';

class ContactUtils {

  //https://stackoverflow.com/questions/16800540/validate-email-address-in-dart
  static RegExp emailRegExp =  new RegExp("^([-.\\w]*[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})\$");

  static Map<String, dynamic> validateEmail(AbstractControl control) {
    String value = control.value;
    if (value.isEmpty) {
      return {};
    }
    int matchSize = emailRegExp.allMatches(value).length;
    if (matchSize != 1) {
      return {'badFormat': true};
    }
    return {};
  }

  static RegExp phoneDelimiterRegExp = new RegExp(r"[ ()+-]");

  static Map<String, dynamic> validatePhone(AbstractControl control) {
    String value = control.value;
    if (value.isEmpty) {
      return {};
    }
    List<String> chars = value.split("");
    int digitCount = 0;
    Map<String, dynamic> result = new Map<String, dynamic>();
    for(String char in chars) {
      try {
        int.parse(char);
        digitCount++;
      } catch (e) {
        if (phoneDelimiterRegExp.allMatches(char).length != 1) {
          result['wrongSymbols'] = true;
        }
      }
    }
    if (digitCount < 8) {
      result['phoneLength'] = true;
    }

    return result;
  }
}