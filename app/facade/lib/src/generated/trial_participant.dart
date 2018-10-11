part of app_facade;


class TrialParticipant {
  
  
  String id = null;
  

  
  String customerId = null;
  

  
  String nurseId = null;
  

  
  String status = null;
  //enum statusEnum {  APPROVED,  UNAPPROVED,  REJECTED,  };

  
  Trial trial = null;
  

  
  DateTime sentKitsTime = null;
  

  
  bool hasKits = null;
  

  
  String trialId = null;
  
  TrialParticipant();

  @override
  String toString()  {
    return 'TrialParticipant[id=$id, customerId=$customerId, nurseId=$nurseId, status=$status, trial=$trial, sentKitsTime=$sentKitsTime, hasKits=$hasKits, trialId=$trialId, ]';
  }
}

