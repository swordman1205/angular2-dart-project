part of app_facade;


class CustomerStatus {
  
  
  String type = null;
  //enum typeEnum {  APPROVED,  UNAPPROVED,  REJECTED,  };

  
  DateTime time = null;
  
  CustomerStatus();

  @override
  String toString()  {
    return 'CustomerStatus[type=$type, time=$time, ]';
  }
}

