// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class TimeUtils {

  static DateFormat _timeFormat12 = new DateFormat("h:m:a");
  static DateFormat _timeFormat24 = new DateFormat("HH:mm:ss");

  static DateFormat _dateUserFormat = new DateFormat("MM/dd/yyyy");
  static DateFormat _dateTimeBackedFormat = new DateFormat('yyyy-MM-ddTHH:mm:ss.SSS');

/*
DateTime toDateTime(num hour, num minute, String amPm) {
  var value = "${hour.round()}:${minute.round()} $amPm";
  DateTime dateTime = _timeFormat.parse(value);
  print(dateTime);
  return dateTime;
}
*/
  static DateTime parseUserTime(String dateString) {
    return _dateUserFormat.parse(dateString);
  }

  static DateTime parseDateTime(String dateTimeString) {
    if (dateTimeString == null) {
      return null;
    }
    return DateTime.parse(dateTimeString).toLocal();
  }

  static String formatDate(DateTime date) {
    if (date == null) {
      return "";
    }
    return date.toString().substring(0, 10);
  }

  static String formatDateTime(DateTime date) {
    if (date == null) {
      return "";
    }
    return _dateTimeBackedFormat.format(date);
  }

  static DateTime parseTime12(String value) {
    DateTime dateTime = _timeFormat12.parse(value);
    print(dateTime);
    return dateTime;
  }

  static DateTime parseTime24(String value) {
    DateTime dateTime = _timeFormat24.parse(value);
    print(dateTime);
    return dateTime;
  }

  static String formatTime12(DateTime dateTime) {
    return _timeFormat12.format(dateTime);
  }

  static String formatTime24(DateTime dateTime) {
    return _timeFormat24.format(dateTime);
  }

  static DateTime adjustTime12(String timeString, DateTime dateTime) {
    DateTime dateTimeFromTime = parseTime12(timeString);
    return new DateTime(dateTime.year, dateTime.month, dateTime.day,
        dateTimeFromTime.hour, dateTimeFromTime.minute);
  }

  static String transform12To24(String time12) {
    DateTime time = parseTime12(time12);
    return formatTime24(time);
  }

  static String transform24To12(String time24) {
    DateTime time = parseTime24(time24);
    return formatTime24(time);
  }

  static Duration durationBetween(DateTime earlyDate, DateTime lateDate) {
    var durationMillis = lateDate.millisecondsSinceEpoch - earlyDate.millisecondsSinceEpoch;
    return new Duration(milliseconds: durationMillis);
  }

  static int durationInYears(DateTime value) {
    if (value == null) {
      return null;
    }
    var today = new DateTime.now();
    var yearDiff = today.year - value.year;
    if (value.month > today.month ||
        (value.month == today.month && value.day >= today.day)) {
      yearDiff --;
    }
    return yearDiff;
  }

  static int durationInMonth(DateTime value) {
    if (value == null) {
      return null;
    }
    int yearDiff = durationInYears(value);
    var today = new DateTime.now();
    var monthDiff = today.month - value.month;
    if (value.day > today.day ||
        (value.day == today.day && value.hour >= today.hour)) {
      monthDiff --;
    }
    return monthDiff + (yearDiff * 12);
  }

  static DateTime atDayStart(DateTime dateTime) {
    return new DateTime(dateTime.year, dateTime.month, dateTime.day, 0, 0);
  }

//DateTime parseDate(List<int> dateArray) {
//  if (dateArray == null || dateArray.length != 3) {
//    throw("incorrect date array value");
//  }
//  return new DateTime(dateArray[0], dateArray[1], dateArray[2]);
//}

}
