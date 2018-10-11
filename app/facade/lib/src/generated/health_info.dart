part of app_facade;


class HealthInfo {
  
  
  String id = null;
  

  
  String userId = null;
  

  
  bool menstruate = null;
  

  
  DateTime firstDateOfLastPeriod = null;
  

  
  int averagePeriodLength = null;
  

  
  int typicalCycleLength = null;
  

  
  String sexualActivity = null;
  //enum sexualActivityEnum {  NONE,  SINGLE_PARTNER,  MULTIPLE_PARTNERS,  };

  
  List<String> birthControls = [];
  //enum birthControlsEnum {  CONDOM,  SPIRAL_HORMONE,  SPIRAL_COBBER,  PILL,  IMPLANT,  PATCH,  VAGINAL_RING,  INFERTILE,  NONE,  };

  
  int weight = null;
  

  
  String weightUnit = null;
  //enum weightUnitEnum {  METRIC,  IMPERIAL,  };

  
  int height = null;
  

  
  String heightUnit = null;
  //enum heightUnitEnum {  METRIC,  IMPERIAL,  };
  HealthInfo();

  @override
  String toString()  {
    return 'HealthInfo[id=$id, userId=$userId, menstruate=$menstruate, firstDateOfLastPeriod=$firstDateOfLastPeriod, averagePeriodLength=$averagePeriodLength, typicalCycleLength=$typicalCycleLength, sexualActivity=$sexualActivity, birthControls=$birthControls, weight=$weight, weightUnit=$weightUnit, height=$height, heightUnit=$heightUnit, ]';
  }
}

