part of app_facade;


class CustomerInfo {
  
  
  String id = null;
  

  
  DateTime createTime = null;
  

  
  String createUserId = null;
  

  
  String userId = null;
  

  
  String nurseId = null;
  

  
  String fullName = null;
  

  
  String email = null;
  

  
  String phone = null;
  

  
  DateTime dateOfBirth = null;
  

  
  String addressLine = null;
  

  
  String city = null;
  

  
  String state = null;
  

  
  String zip = null;
  

  
  String country = null;
  

  
  List<String> contactTimes = [];
  //enum contactTimesEnum {  EARLY_MORNING,  MORNING,  NOON,  AFTER_NOON,  EVENING,  LATE_EVENING,  };

  
  String contactDay = null;
  //enum contactDayEnum {  MON_FRI,  SAT_SUN,  ALL_DAYS,  };

  
  CustomerStatus customerStatus = null;
  

  
  List<CustomerStatus> customerStatusHistory = [];
  

  
  String emailStatus = null;
  //enum emailStatusEnum {  CONFIRMED,  UNCONFIRMED,  };
  CustomerInfo();

  @override
  String toString()  {
    return 'CustomerInfo[id=$id, createTime=$createTime, createUserId=$createUserId, userId=$userId, nurseId=$nurseId, fullName=$fullName, email=$email, phone=$phone, dateOfBirth=$dateOfBirth, addressLine=$addressLine, city=$city, state=$state, zip=$zip, country=$country, contactTimes=$contactTimes, contactDay=$contactDay, customerStatus=$customerStatus, customerStatusHistory=$customerStatusHistory, emailStatus=$emailStatus, ]';
  }
}

