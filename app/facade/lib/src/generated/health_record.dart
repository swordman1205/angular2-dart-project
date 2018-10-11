part of app_facade;


class HealthRecord {
  
  
  String id = null;
  

  
  DateTime createTime = null;
  

  
  String createUserId = null;
  

  
  String customerId = null;
  

  
  String customerUserId = null;
  

  
  String trialId = null;
  
  HealthRecord();

  @override
  String toString()  {
    return 'HealthRecord[id=$id, createTime=$createTime, createUserId=$createUserId, customerId=$customerId, customerUserId=$customerUserId, trialId=$trialId, ]';
  }
}

