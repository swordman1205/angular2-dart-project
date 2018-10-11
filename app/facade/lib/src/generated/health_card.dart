part of app_facade;


class HealthCard {
  
  
  String id = null;
  

  
  String userId = null;
  

  
  String type = null;
  //enum typeEnum {  ENRICH_PROFILE,  PAD_ACTION,  };

  
  DateTime createDate = null;
  

  
  String note = null;
  
  HealthCard();

  @override
  String toString()  {
    return 'HealthCard[id=$id, userId=$userId, type=$type, createDate=$createDate, note=$note, ]';
  }
}

