part of app_facade;


class Trial {
  
  
  String id = null;
  

  
  DateTime createTime = null;
  

  
  String createUserId = null;
  

  
  String name = null;
  

  
  int compensationAmount = null;
  
  Trial();

  @override
  String toString()  {
    return 'Trial[id=$id, createTime=$createTime, createUserId=$createUserId, name=$name, compensationAmount=$compensationAmount, ]';
  }
}

