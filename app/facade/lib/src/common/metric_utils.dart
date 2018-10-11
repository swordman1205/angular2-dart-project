// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class MetricUtils {
  static List<int> parseFeetInch(String value) {
    List<String> splitted = value.split("”");
    if (splitted.length == 1) {
      try {
        int feet = int.parse(splitted[0]);
        return [feet, 0];
      } catch (e) {
        throw 'badFeetFormat';
      }
    } else if (splitted.length == 2) {
      try {
        int feet = int.parse(splitted[0]);
        int inch = 0;
        if (splitted[1].trim().isNotEmpty) {
          inch = int.parse(splitted[1]);
          if (inch > 12) {
            throw 'badFeetFormat';
          }
        }
        return [feet, inch];
      } catch (e) {
        throw 'badFeetFormat';
      }
    }
    throw 'badFeetFormat';
  }

  static int feetInchToCm(int feet, int inch) {
    return ((feet * 30.48) + (inch * 2.54)).toInt();
  }

  static int parseFeetInchToCm(String value) {
    var feetInch = parseFeetInch(value);
    return ((feetInch[0] * 30.48) + (feetInch[1] * 2.54)).toInt();
  }

  static int lbsToKg(int lbs) {
    return (lbs * 0.45359237).toInt();
  }

}