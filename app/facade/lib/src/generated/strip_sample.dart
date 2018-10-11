part of app_facade;


class StripSample {
  
  
  String id = null;
  

  
  DateTime createTime = null;
  

  
  String createUserId = null;
  

  
  String kitId = null;
  

  
  String customerId = null;
  

  
  DateTime padStartTime = null;
  

  
  DateTime padRemoveTime = null;
  

  
  DateTime periodStartDate = null;
  

  
  String padRemovePictureBlobName = null;
  

  
  DateTime padRemovePictureTime = null;
  

  
  String stripRemovePictureBlobName = null;
  

  
  DateTime stripRemovePictureTime = null;
  

  
  DateTime padCollectedTime = null;
  

  
  String bleedIntensity = null;
  //enum bleedIntensityEnum {  SPOOTING,  LIGHT,  MEDIUM,  HEAVY,  };

  
  List<String> primaryActivities = [];
  //enum primaryActivitiesEnum {  SITTING,  SLEEPING,  SPORTS,  RUNNING,  BIKING,  WALKING,  };

  
  String padSaturation = null;
  //enum padSaturationEnum {  EASY,  MEDIUM,  HARD,  };

  
  String padComfort = null;
  //enum padComfortEnum {  TERRIBLE,  OKAY,  GREAT,  };

  
  String padFeedback = null;
  

  
  DateTime updateTime = null;
  

  
  int stepNumber = null;
  

  
  String status = null;
  //enum statusEnum {  INPROGRESS,  FINISHED,  PICKUPED,  DELIVERED,  };
  StripSample();

  @override
  String toString()  {
    return 'StripSample[id=$id, createTime=$createTime, createUserId=$createUserId, kitId=$kitId, customerId=$customerId, padStartTime=$padStartTime, padRemoveTime=$padRemoveTime, periodStartDate=$periodStartDate, padRemovePictureBlobName=$padRemovePictureBlobName, padRemovePictureTime=$padRemovePictureTime, stripRemovePictureBlobName=$stripRemovePictureBlobName, stripRemovePictureTime=$stripRemovePictureTime, padCollectedTime=$padCollectedTime, bleedIntensity=$bleedIntensity, primaryActivities=$primaryActivities, padSaturation=$padSaturation, padComfort=$padComfort, padFeedback=$padFeedback, updateTime=$updateTime, stepNumber=$stepNumber, status=$status, ]';
  }
}

