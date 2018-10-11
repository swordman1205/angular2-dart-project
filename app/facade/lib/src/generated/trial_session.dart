part of app_facade;


class TrialSession {
  
  
  String id = null;
  

  
  DateTime createTime = null;
  

  
  String createUserId = null;
  

  
  String pickupNurseId = null;
  

  
  String status = null;
  //enum statusEnum {  ACTIVE,  NURSE_CONFIRMED,  NURSE_REJECTED,  PICKED_UP,  DELIVERED_AT_LAB,  };

  
  bool compensated = null;
  

  
  String feedback = null;
  

  
  List<String> sampleIds = [];
  

  
  String trialParticipantId = null;
  
  TrialSession();

  @override
  String toString()  {
    return 'TrialSession[id=$id, createTime=$createTime, createUserId=$createUserId, pickupNurseId=$pickupNurseId, status=$status, compensated=$compensated, feedback=$feedback, sampleIds=$sampleIds, trialParticipantId=$trialParticipantId, ]';
  }
}

