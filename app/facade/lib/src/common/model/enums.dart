// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class Enums {
  static final cardTypes = [
    new EnumWrapper('ENRICH_PROFILE', 'Enrich profile'),
    new EnumWrapper('PAD_ACTION', 'Pad action'),
  ];
  static final roles = [
    new EnumWrapper('ADMIN', 'Admin'),
    new EnumWrapper('CUSTOMER', 'Customer'),
    new EnumWrapper('MEDICAL', 'Medical'),
    new EnumWrapper('LAB_TECH', 'Laboratory technicial'),
    new EnumWrapper('NURSE', 'Nurse')
  ];
  static final bleedIntensity = [
    new EnumWrapper('SPOOTING', 'Spotting'),
    new EnumWrapper('LIGHT', 'Light'),
    new EnumWrapper('MEDIUM', 'Medium'),
    new EnumWrapper('HEAVY', 'Heavy'),
  ];
  static final primaryActivities = new List.unmodifiable([
    new EnumWrapper('SITTING', 'Sitting'),
    new EnumWrapper('SLEEPING', 'Sleeping'),
    new EnumWrapper('SPORTS', 'Sports'),
    new EnumWrapper('RUNNING', 'Running'),
    new EnumWrapper('BIKING', 'Biking'),
    new EnumWrapper('WALKING', 'Walking'),
  ]);
  static final padSaturation = new List.unmodifiable([
    new EnumWrapper('EASY', 'Easy'),
    new EnumWrapper('MEDIUM', 'Medium'),
    new EnumWrapper('HARD', 'Hard'),
  ]);
  static final padComfort = new List.unmodifiable([
    new EnumWrapper('TERRIBLE', 'Terrible'),
    new EnumWrapper('OKAY', 'Okay'),
    new EnumWrapper('GREAT', 'Great'),
  ]);
  //'None', 'Single Partner', 'Multiple Partners'
  static final sexualActivity = [
    new EnumWrapper('NONE', 'None'),
    new EnumWrapper('SINGLE_PARTNER', 'Single Partner'),
    new EnumWrapper('MULTIPLE_PARTNERS', 'Multiple Partners'),
  ];
  //['Condom', 'Spiral/IUD (hormone)', 'Spiral/IUD (cobber)', ' Pill', 'Implant', 'Patch', 'Vaginal ring', 'Infertile'];
  static final birthControl = [
    new EnumWrapper('NONE', 'None'),
    new EnumWrapper('CONDOM', 'Condom'),
    new EnumWrapper('SPIRAL_HORMONE', 'Spiral/IUD (hormone)'),
    new EnumWrapper('SPIRAL_COBBER', 'Spiral/IUD (copper)'),
    new EnumWrapper('PILL', 'Pill'),
    new EnumWrapper('IMPLANT', 'Implant'),
    new EnumWrapper('PATCH', 'Patch'),
    new EnumWrapper('VAGINAL_RING', 'Vaginal ring'),
    new EnumWrapper('INFERTILE', 'Infertile'),
  ];
  //List<String> contactTimeOptions = ['7-9 am', '9 am-noon', 'noon-3 pm', '3-6 pm', '6-9 pm', '9-11 pm'];
  static final contactTime = [
    new EnumWrapper('EARLY_MORNING', '7-9 am'),
    new EnumWrapper('MORNING', '9 am-noon'),
    new EnumWrapper('NOON', 'noon-3 pm'),
    new EnumWrapper('AFTER_NOON', '3-6 pm'),
    new EnumWrapper('EVENING', '6-9 pm'),
    new EnumWrapper('LATE_EVENING', '9-11 pm'),
  ];
  //List<String> contactDaysOptions = ['Mon-Fri', 'Sat-Sun', 'All days'];
  static final contactDays = [
    new EnumWrapper('MON_FRI', 'Mon-Fri'),
    new EnumWrapper('SAT_SUN', 'Sat-Sun'),
    new EnumWrapper('ALL_DAYS', 'All days'),
  ];
  //enum emailStatusEnum {  CONFIRMED,  UNCONFIRMED,  };
  static final contactStatus = [
    new EnumWrapper('CONFIRMED', 'Confirmed'),
    new EnumWrapper('UNCONFIRMED', 'Not confirmed'),
  ];
  //enum customerStatusEnum {  APPROVED,  UNAPPROVED,  REJECTED,  };
  static final customerStatusType = [
    new EnumWrapper('APPROVED', 'Consented'),
    new EnumWrapper('UNAPPROVED', 'Not yet consented'),
    new EnumWrapper('REJECTED', 'Rejected'),
  ];
  //enum sampleStatusEnum {  INPROGRESS,  FINISHED,  PICKUPED,  DELIVERED,  };
  static final sampleStatus = [
    new EnumWrapper('INPROGRESS', 'Inprogress'),
    new EnumWrapper('FINISHED', 'Finished'),
    new EnumWrapper('PICKUPED', 'Picked up'),
    new EnumWrapper('DELIVERED', 'Delivered'),
  ];
  //enum typeEnum {  LABORATORY,  COLLECTOR,  CLINICAL,  MANUFACTURER,  LOGISTICS,  };
  static final organizationType = [
    new EnumWrapper('LABORATORY', 'Laboratory'),
    new EnumWrapper('COLLECTOR', 'Collector'),
    new EnumWrapper('CLINICAL', 'Clinical'),
    new EnumWrapper('MANUFACTURER', 'Manufacturer'),
    new EnumWrapper('LOGISTICS', 'Logistics'),
  ];
  //enum statusEnum {  ACTIVE,  NURSE_CONFIRMED,  NURSE_REJECTED,  PICKED_UP,  DELIVERED_AT_LAB,  };
  static final sessionStatusType = [
    new EnumWrapper('ACTIVE', 'Active'),
    new EnumWrapper('NURSE_CONFIRMED', 'Nurse confirmed'),
    new EnumWrapper('NURSE_REJECTED', 'Nurse rejected'),
    new EnumWrapper('PICKED_UP', 'Picked up'),
    new EnumWrapper('DELIVERED_AT_LAB', 'Delivered at lab'),
  ];

  static final _map = {
    "cardType": cardTypes,
    "role": roles,
    "bleedIntensity": bleedIntensity,
    "primaryActivities": primaryActivities,
    "padSaturation": padSaturation,
    "padComfort": padComfort,
    "contactDays": contactDays,
    "contactTime": contactTime,
    "birthControl": birthControl,
    "sexualActivity": sexualActivity,
    "contactStatus": contactStatus,
    "customerStatusType": customerStatusType,
    "sampleStatus": sampleStatus,
    "organizationType": organizationType,
    "sessionStatusType": sessionStatusType
  };
  static List<EnumWrapper> find(String enumName) {
    if (_map.containsKey(enumName)) {
      return _map[enumName];
    } else {
      throw "unkonwn enum name $enumName";
    }
  }
}

