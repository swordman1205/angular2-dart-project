part of app_facade;


class User {
  
  
  String id = null;
  

  
  String fullName = null;
  

  
  String email = null;
  

  
  String role = null;
  //enum roleEnum {  ADMIN,  CUSTOMER,  MEDICAL,  LAB_TECH,  NURSE,  };

  
  DateTime createTime = null;
  

  
  DateTime lastLoginTime = null;
  

  
  bool active = null;
  
  User();

  @override
  String toString()  {
    return 'User[id=$id, fullName=$fullName, email=$email, role=$role, createTime=$createTime, lastLoginTime=$lastLoginTime, active=$active, ]';
  }
}

