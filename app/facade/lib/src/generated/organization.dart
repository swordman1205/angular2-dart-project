part of app_facade;


class Organization {
  
  
  String id = null;
  

  
  String name = null;
  

  
  String type = null;
  //enum typeEnum {  LABORATORY,  COLLECTOR,  CLINICAL,  MANUFACTURER,  LOGISTICS,  };
  Organization();

  @override
  String toString()  {
    return 'Organization[id=$id, name=$name, type=$type, ]';
  }
}

