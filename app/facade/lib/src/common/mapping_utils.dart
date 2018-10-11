// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class MappingUtils {

  static Assay fromJsonAssay(Map<String, dynamic> map) {
    var assay = new Assay();
    assay.id = map["id"];
    assay.name = map["name"];
    assay.rangeMin = map["rangeMin"];
    assay.rangeMax = map["rangeMax"];
    return assay;
  }

  static CustomerInfo fromJsonCustomerInfo(Map<String, dynamic> map) {
    CustomerInfo customerInfo = new CustomerInfo();
    customerInfo.id = map['id'];
    customerInfo.createUserId = map['createUserId'];
    customerInfo.createTime = TimeUtils.parseDateTime(map['createTime']);
    customerInfo.userId = map['userId'];
    customerInfo.nurseId = map['nurseId'];
    customerInfo.fullName = map['fullName'];
    customerInfo.email = map['email'];
    customerInfo.phone = map['phone'];

    customerInfo.addressLine = map['addressLine'];
    customerInfo.city = map['city'];
    customerInfo.state = map['state'];
    customerInfo.zip = map['zip'];
    customerInfo.country = map['country'];
    if (map['dateOfBirth'] != null) {
      customerInfo.dateOfBirth = DateTime.parse(map['dateOfBirth'] + " 00:00:00").toLocal();
    }
    customerInfo.contactTimes = map['contactTimes'];
    customerInfo.contactDay = map['contactDay'];
    customerInfo.customerStatus = fromJsonCustomerStatus(map['customerStatus']);
    customerInfo.emailStatus = map['emailStatus'];

    return customerInfo;
  }

  static CustomerStatus fromJsonCustomerStatus(Map<String, dynamic> map) {
    if (map == null) {
      return null;
    }
    CustomerStatus customerStatus = new CustomerStatus();
    customerStatus.type = map['type'];
    customerStatus.time = TimeUtils.parseDateTime(map['time']);
    return customerStatus;
  }

  static HealthCard fromJsonHealthCard(Map<String, dynamic> map) {
    HealthCard healthCard = new HealthCard();
    healthCard.id = map['id'];
    healthCard.userId = map['userId'];
    healthCard.type = map['type'];
    healthCard.createDate = DateTime.parse(map['createDate']);
    healthCard.note = map['note'];
    return healthCard;
  }

  static HealthRecord fromJsonHealthRecord(Map<String, dynamic> map) {
    HealthRecord healthRecord = new HealthRecord();
    healthRecord.id = map['id'];
    healthRecord.createTime = TimeUtils.parseDateTime(map['createTime']);
    healthRecord.createUserId = map['createUserId'];
//    healthRecord.nurseId = map['nurseId'];
    healthRecord.customerId = map['customerId'];
    healthRecord.trialId = map['trialId'];
    healthRecord.customerUserId = map['customerUserId'];
    return healthRecord;
  }

  static HealthInfo fromJsonHealthInfo(Map<String, dynamic> map) {
    HealthInfo healthInfo = new HealthInfo();
    healthInfo.id = map['id'];
    healthInfo.userId = map['userId'];
    healthInfo.menstruate = map['menstruate'];
    healthInfo.firstDateOfLastPeriod = TimeUtils.parseDateTime(map['firstDateOfLastPeriod']);
    healthInfo.averagePeriodLength = map['averagePeriodLength'];
    healthInfo.typicalCycleLength = map['typicalCycleLength'];
    healthInfo.sexualActivity = map['sexualActivity'];
    healthInfo.birthControls = map['birthControls'];
    healthInfo.weight = map['weight'];
    healthInfo.weightUnit = map['weightUnit'];
    healthInfo.height = map['height'];
    healthInfo.heightUnit = map['heightUnit'];
    return healthInfo;
  }

  static TrialSession fromJsonHealthSession(Map<String, dynamic> map) {
    TrialSession trialSession = new TrialSession();
    trialSession.sampleIds = map['sampleIds'];
    trialSession.pickupNurseId = map['pickupNurseId'];
//    healthSession.tubeId = map['tubeId'];
    trialSession.feedback = map['feedback'];
//    healthSession.paidStatus = map['paidStatus'];
//    healthSession.stripIds = map['stripIds'];
    trialSession.status = map['status'];
    return trialSession;
  }

  static StripSample fromJsonSample(Map<String, dynamic> map) {
    StripSample stripSample = new StripSample();
    stripSample.id = map['id'];
    stripSample.customerId = map['customerId'];
    stripSample.createUserId = map['createUserId'];
    stripSample.primaryActivities = map['primaryActivities'];
    stripSample.periodStartDate = TimeUtils.parseDateTime(map['periodStartDate']);
    stripSample.padStartTime = TimeUtils.parseDateTime(map['padStartTime']);
    stripSample.padRemoveTime = TimeUtils.parseDateTime(map['padRemoveTime']);
    stripSample.createTime = TimeUtils.parseDateTime(map['createTime']);
    stripSample.bleedIntensity = map['bleedIntensity'];
    stripSample.padComfort = map['padComfort'];
    stripSample.kitId = map['kitId'];
    stripSample.stripRemovePictureBlobName = map['stripRemovePictureBlobName'];
    stripSample.padRemovePictureBlobName = map['padRemovePictureBlobName'];
    stripSample.stripRemovePictureTime = TimeUtils.parseDateTime(map['stripRemovePictureTime']);
    stripSample.padRemovePictureTime = TimeUtils.parseDateTime(map['padRemovePictureTime']);
    stripSample.updateTime = TimeUtils.parseDateTime(map['updateTime']);
    stripSample.padSaturation = map['padSaturation'];
    stripSample.padFeedback = map['padFeedback'];
    stripSample.stepNumber = map['stepNumber'];
    stripSample.status = map['status'];
    return stripSample;
  }

  static TubeSample fromJsonTubeSample(Map<String, dynamic> map) {
    TubeSample tubeSample = new TubeSample();
    tubeSample.id = map['id'];
    tubeSample.customerId = map['customerId'];
    tubeSample.createUserId = map['createUserId'];
    tubeSample.createTime = TimeUtils.parseDateTime(map['createTime']);
    tubeSample.kitId = map['kitId'];
    return tubeSample;
  }

  static User fromJsonUser(Map<String, dynamic> map) {
    User user = new User();
    user.email = map['email'];
    user.fullName = map['fullName'];
    user.role = map['role'];
    user.id = map['id'];
    user.createTime = DateTime.parse(map['createTime']).toLocal();
    if (map['lastLoginTime'] != null) {
      user.lastLoginTime = DateTime.parse(map['lastLoginTime']).toLocal();
    }
    return user;
  }

  static Trial fromJsonTrial(Map<String, dynamic> map) {
    Trial trial = new Trial();
    trial.createUserId = map['createUserId'];
    trial.id = map['id'];
    trial.compensationAmount = map['compensationAmount'];
    trial.createTime = TimeUtils.parseDateTime(map['createTime']);
    trial.name = map['name'];
    return trial;
  }

  static TrialParticipant fromJsonTrialParticipant(Map<String, dynamic> map) {
    TrialParticipant trialParticipant = new TrialParticipant();
    trialParticipant.id = map['id'];
    trialParticipant.status = map['status'];
    trialParticipant.trialId = map['trialId'];
    trialParticipant.customerId = map['customerId'];
    trialParticipant.nurseId = map['nurseId'];
    trialParticipant.hasKits = map['hasKits'];
    trialParticipant.sentKitsTime = TimeUtils.parseDateTime(map['sentKitsTime']);
    return trialParticipant;
  }

  static TrialSession fromJsonTrialSession(Map<String, dynamic> map) {
    TrialSession trialSession = new TrialSession();
    trialSession.id = map['id'];
    trialSession.status = map['status'];
    trialSession.pickupNurseId = map['pickupNurseId'];
    trialSession.compensated = map['compensated'];
    trialSession.feedback = map['feedback'];
    trialSession.sampleIds = map['sampleIds'];
    trialSession.trialParticipantId = map['trialParticipantId'];
    trialSession.createUserId = map['createUserId'];
    trialSession.createTime = TimeUtils.parseDateTime(map['createTime']);
    return trialSession;
  }

  static Organization fromJsonOrganization(Map<String, dynamic> map) {
    Organization organization = new Organization();
    organization.id = map['id'];
    organization.name = map['name'];
    organization.type = map['type'];
    return organization;
  }

  static Map<String, dynamic> toJsonAssay(Assay a) => {
    "id": a.id,
    "name": a.name,
    "rangeMin": a.rangeMin,
    "rangeMax": a.rangeMax
  };

  static Map toJsonTrial(Trial t) => {
    "id": t.id,
    "name": t.name,
    "createTime": TimeUtils.formatDateTime(t.createTime),
    "compensationAmount": t.compensationAmount,
    "createUserId": t.createUserId,
//    "participants": t.participants.map((tp)=>toJsonTrialParticipant(tp)).toList()
  };

  static Map toJsonTrialParticipant(TrialParticipant tp) => {
    "id": tp.id,
    "customerId": tp.customerId,
    "hasKits": tp.hasKits,
    "nurseId": tp.nurseId,
    "trialId": tp.trialId,
    "status": tp.status,
//    "sessions": tp.sessions.map((ts)=>toJsonTrialSession(ts)).toList()
  };

  static Map toJsonOrganization(Organization o) => {
    "id": o.id,
    "name": o.name,
    "type": o.type,
  };

  static Map toJsonCustomerInfo(CustomerInfo ui) => {
    'id': ui.id,
    'createTime': TimeUtils.formatDateTime(ui.createTime),
    'createUserId': ui.createUserId,
    'userId': ui.userId,
    'fullName': ui.fullName,
    'email': ui.email,
    'phone': ui.phone,
    'dateOfBirth': TimeUtils.formatDate(ui.dateOfBirth),
    'addressLine': ui.addressLine,
    'city': ui.city,
    'state': ui.state,
    'zip': ui.zip,
    'country': ui.country,
    'contactTimes': ui.contactTimes,
    'contactDay': ui.contactDay,
    'customerStatus': toJsonCustomerStatus(ui.customerStatus),
    'emailStatus': ui.emailStatus
  };

  static Map toJsonCustomerStatus(CustomerStatus cs) => {
    'type': cs.type,
    'time': TimeUtils.formatDateTime(cs.time)
  };

  static Map toJsonHealthRecord(HealthRecord hc) => {
    "id": hc.id,
    "trialId": hc.trialId,
    "customerId": hc.customerId,
    "createTime": TimeUtils.formatDateTime(hc.createTime),
    "createUserId": hc.createUserId,
    "customerUserId": hc.customerUserId,
  };

  static Map toJsonHealthInfo(HealthInfo hi) => {
    "id": hi.id,
    "userId": hi.userId,
    "menstruate": hi.menstruate,
    "firstDateOfLastPeriod": hi.firstDateOfLastPeriod.millisecondsSinceEpoch,
    "averagePeriodLength": hi.averagePeriodLength,
    "typicalCycleLength": hi.typicalCycleLength,
    "sexualActivity": hi.sexualActivity,
    "birthControl": hi.birthControls,
    "weight": hi.weight,
    "weightUnit": hi.weightUnit,
    "height": hi.height,
    "heightUnit": hi.heightUnit
  };

  static Map toJsonTrialSession(TrialSession ts) => {
//    "id": ts.id,
//    "stripIds": hc.stripIds,
    "sampleIds": ts.sampleIds,
//    "paidStatus": hc.paidStatus,
//    "createTime": TimeUtils.formatDateTime(ts.createTime),
    "pickupNurseId": ts.pickupNurseId,
    "feedback": ts.feedback,
//    "tubeId": hc.tubeId,
    "status": ts.status
  };

  static Map toJsonSample(StripSample stripSample) => {
    "id": stripSample.id,
    "customerId": stripSample.customerId,
    "createUserId": stripSample.createUserId,
    "kitId": stripSample.kitId,
    "periodStartDate": stripSample.periodStartDate?.millisecondsSinceEpoch,
    "padStartTime": stripSample.padStartTime?.millisecondsSinceEpoch,
    "padRemoveTime": stripSample.padRemoveTime?.millisecondsSinceEpoch,
    "padRemovePictureBlobName" : stripSample.padRemovePictureBlobName,
    "stripRemovePictureBlobName": stripSample.stripRemovePictureBlobName,
    "padRemovePictureTime": stripSample.padRemovePictureTime?.millisecondsSinceEpoch,
    "stripRemovePictureTime": stripSample.stripRemovePictureTime?.millisecondsSinceEpoch,
    "bleedIntensity": stripSample.bleedIntensity,
    "primaryActivities": stripSample.primaryActivities,
    "padSaturation": stripSample.padSaturation,
    "padComfort": stripSample.padComfort,
    "createTime": TimeUtils.formatDateTime(stripSample.createTime),
    "updateTime": TimeUtils.formatDateTime(stripSample.updateTime),
    "padFeedback": stripSample.padFeedback,
    "stepNumber": stripSample.stepNumber,
    "status": stripSample.status
  };

  static Map toJsonTubeSample(TubeSample tubeSample) => {
    "id": tubeSample.id,
    "customerId": tubeSample.customerId,
    "createUserId": tubeSample.createUserId,
    "kitId": tubeSample.kitId,
    "createTime": TimeUtils.formatDateTime(tubeSample.createTime)
  };

  static Map toJsonUserWithPassword(User u, String password) => {
    'id': u.id,
    'fullName': u.fullName,
    'role': u.role,
    'email': u.email,
    'password': password
  };

  static Map toJsonUserData(UserData ud) => {
    //user
    "fullName": ud.fullName,
    "email": ud.email,
    "password": ud.password,

    //customer
    "phone": ud.phone,
    "birthDate": TimeUtils.formatDateTime(ud.birthDate),
    "addressLine": ud.addressLine,
    "city": ud.city,
    "state": ud.state,
    "zip": ud.zip,
    "country": ud.country.name,
    "contactTimes": ud.contactTimes,
    "contactDay": ud.contactDay,

    //health
    "menstruate": ud.menstruate,
    "firstDateOfLastPeriod": TimeUtils.formatDateTime(ud.firstDateOfLastPeriod),
    "averagePeriodLength": ud.averagePeriodLength,
    "typicalCycleLength": ud.typicalCycleLength,
    "sexualActivity": ud.sexualActivity.isNotEmpty ? ud.sexualActivity.first.name : null,
    "birthControl": ud.birthControl.map((ew)=>ew.name).toList(),
    "weightUnit": ud.weightUnit.toLowerCase() == 'lbs' ? 'IMPERIAL' : 'METRIC',
    "weight": ud.weightUnit.toLowerCase() == 'lbs' ? MetricUtils.lbsToKg(int.parse(ud.weight)): int.parse(ud.weight),
    "heightUnit": ud.heightUnit.toLowerCase() == 'ft' ? 'IMPERIAL' : 'METRIC',
    "height": ud.heightUnit.toLowerCase() == 'ft' ? MetricUtils.feetInchToCm(int.parse(ud.heightFeet), int.parse(ud.heightInch)) : int.parse(ud.heightCm),
  };
}