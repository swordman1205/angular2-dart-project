part of app_facade;

class SampleDevice {
  String id = null;
  DateTime createTime = null;
  String createUserId = null;
  String kitId = null;
  String customerId = null;
}

class StripSample extends SampleDevice {
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
}

class TubeSample extends SampleDevice {

}

