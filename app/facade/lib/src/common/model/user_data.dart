// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class UserData {
  // user data
  String fullName;
  String email;
  String password;
  String phone;

  // customer info data
  DateTime birthDate;
  String addressLine;
  String city;
  String state;
  String zip;
  EnumWrapper country;
  List<String> contactTimes = [];
  String contactDay;

  // health info data
  bool menstruate;
  DateTime firstDateOfLastPeriod;
  int averagePeriodLength = 1;
  int typicalCycleLength = 28;
  List<EnumWrapper> sexualActivity = [];
  List<EnumWrapper> birthControl = [];
  String weight;
  String weightUnit;
  String heightCm;
  String heightFeet = 5.toString();
  String heightInch = 0.toString();
  String heightUnit;

  String toString() {
    return """ 
      menstruate:$menstruate,
      fullName:$fullName,
      birthDate:$birthDate,
     
      firstDateOfLastPeriod:$firstDateOfLastPeriod,
      averagePeriodLength:$averagePeriodLength,
      typicalCycleLength: $typicalCycleLength,
      
      sexualActivity:$sexualActivity,
      birthControl:$birthControl,
      weight:$weight ($weightUnit),
      heightUnit:$heightUnit,
      heightCm:$heightCm,
      heightFeet:$heightFeet,
      heightInch:$heightInch,
      
      addressLine:$addressLine,
      city:$city,
      state:$state.
      zip:$zip,
      country:$country,
      email:$email
    """;
  }
}
