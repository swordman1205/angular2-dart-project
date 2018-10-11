import 'dart:io';
import 'package:app_facade/app_facade.dart';
import 'dart:async';

class TestDataHolder {
  // assays
  static String ASSAY_HBA1C;

  // users
  static String USER_LABTECH;
  static String USER_NURSE;
  static String USER_MEDICAL;

  // customers
  static String CUSTOMER_LISE;
  static String CUSTOMER_MARIE;

  // organizations
  static String ORG_CLINICAL;
  static String ORG_COLLECTOR;
  static String ORG_LAB;

  // samples
  static String SAMPLE_MARIE;

  // trials
  static String TRIAL_SANDIEGO;
}

class TestDataCreator {
  static Future<TestDataInjector> createTestData(BackendType backendType, [String backendUrl]) async {
    var injector = new TestDataInjector(backendType, url:backendUrl);

    // wait till all api available
    if (backendType != BackendType.MOCK) {
      bool ready = false;
      while (!ready) {
        try {
          await injector.gatewayApi.ping();
          ready = true;
        } catch (error) {
          print(error);
          sleep(const Duration(seconds:1));
        }
      }
    }

    await injector.userApi.login("_admin@qurasense.com", "secret");

    await _createAssays(injector.assayApi);
    await _createOrganizations(injector.accountApi);
    await _createUsers(injector.userApi);
    await _createTrials(injector.trialApi);
    await _createCustomers(injector.gatewayApi, injector.healthApi);
    await _createTrialParticipants(injector.userApi, injector.trialApi);
    await _createSamples(injector.userApi, injector.sampleApi);

    injector.userApi.login("_sara@qurasense.com", "secret");
    return injector;
  }

  static Future<Null> _createAssays(AssayApi api) async {
    var assay = new Assay();
    assay.name = "HbA1c";
    assay.unit = "%";
    assay.rangeMin = 4.2;
    assay.rangeMax = 6.0;

    TestDataHolder.ASSAY_HBA1C = await api.saveAssay(assay);
    print("created assays");
  }

  static Future<Null> _createOrganizations(AccountApi api) async {
    var labOrg = new Organization();
    labOrg.type = "LABORATORY";
    labOrg.name = "US Specialty Labs";
    TestDataHolder.ORG_LAB = await api.saveOrganization(labOrg);
    /*
    Laboratory_responsible: Bryan Andrus
    Laboratory_responsible_email: bryan@usspecialtylabs.com
    Laboratory_address: 11578 Sorrento Valley Road, Suite 27
    Laboratory_zip: 92121
    Laboratory_city: San Diego
    Laboratory_country: USA (Choose from a drop-down of all countries)
    Laboratory_state: CA (If laboratory_country = USA, add state. Chose State from a drop-down of all US States)
    Laboratory_type: sample_type_strip (choose from a list of available sample_types (_strip/_tube/_swap)
    Labotatory_type_strip_assay: Pull from asaay_list viewing all Assays on the platform, and choose multiple assays (check mark choose all 23 assays on the platform)
    Laboratory_type: sample_type_tube (choose from a list of available sample_types (_strip/_tube/_swap)
    Labotatory_type_tube_assay: Pull from asaay_list viewing all Assays on the platform, and choose multiple assays (check mark choose all 23 assays on the platform)
    */

    var collectorOrg = new Organization();
    collectorOrg.type = "COLLECTOR";
    collectorOrg.name = "Coast Phlebotomy";
    TestDataHolder.ORG_COLLECTOR = await api.saveOrganization(collectorOrg);
    /*
    Collector_responsible: John Donovan
    Collector_responsible_email: jdonovan@coastphlebotomy.com
    Collector_address: 3500 Barranca Parkway Suite 305
    Collector_zip: 92606
    Collector_city: Irvine
    Collector_country: USA (Chose from a drop-down of all countries)
    Collector_state: CA (If laboratory_country = USA, add state. Chose State from a drop-down of all US States)
    Collector_type: sample_type_tube (choose from a list of sample_type _strip/_tube/_swap)
    Collector_type: sample_type_strip (choose from a list of sample_type _strip/_tube/_swap)
    */

    var clinicalOrg = new Organization();
    clinicalOrg.type = "COLLECTOR";
    clinicalOrg.name = "Qurasense";
    TestDataHolder.ORG_CLINICAL = await api.saveOrganization(clinicalOrg);
    /*
    Clinical_name: Qurasense
    Clinical_responsible: Sara Naseri
    Clinical_responsible_email: sara@qurasense.com
    Clinical_address: 200 Page Mill Road, Suite 100
    Collector_zip: 94306
    Collector_city: Palo Alto
    Collector_country: USA (Choose from a drop-down of all countries)
    Collector_state: CA (If laboratory_country = USA, add state. Chose State from a drop-down of all US States)
    */
    print("created organizations");
  }

  static Future<Null> _createUsers(UserApi userApi) async {
    var labUser = _createUser("Joel Zinda", "_joel@usspecialtylabs.com", "LAB_TECH");
    TestDataHolder.USER_LABTECH = await userApi.createUserByAdmin(labUser, "secret");
    // TODO organization member
    // Laboratory_connection: US Specialty (Chosen in a drop-down with options pulled from Laboratory_list)

    var nurseUser = _createUser("John Donovan", "_jdonovan@coastphlebotomy.com", "NURSE");
    TestDataHolder.USER_NURSE = await userApi.createUserByAdmin(nurseUser, "secret");
    // TODO organization member
    //  Collector_connection: Coast Phlebotomy (Chosen in a drop-down with options pulled from Collector_list)

    var medicalUser = _createUser("Sara Naseri", "_sara@qurasense.com", "MEDICAL");
    TestDataHolder.USER_MEDICAL = await userApi.createUserByAdmin(medicalUser, "secret");
    // TODO organization member
    //  Collector_connection: Qurasense (Chosen in a drop-down with options pulled from Clinical_list)

    print("created users");
  }

  static Future<Null> _createTrials(TrialApi api) async {
    var trial = new Trial();
    trial.name = "San Diego comparative trial";
    trial.compensationAmount = 25;
    // TODO trial.clinicalOrganizationId = TestDataHolder.ORG_CLINICAL;
    TestDataHolder.TRIAL_SANDIEGO = await api.saveTrial(trial);
    print("created trials");
  }

  static Future<Null> _createCustomers(GatewayApi api, HealthApi healthApi) async {
    {
      UserData marieData = new UserData();
      marieData.fullName = "Marie Curie";
      marieData.email = "_marie@qurasense.com";
      marieData.password = "secret";
      marieData.phone = "12345678";

      marieData.birthDate = new DateTime(1990, DateTime.APRIL, 29);
      marieData.addressLine = "1450 Page Mill Rd";
      marieData.city = "Palo Alto";
      marieData.state = 'CA';
      marieData.zip = "94020";
      marieData.country = CountryEnum.countries.first;
      marieData.contactTimes = ['AFTER_NOON'];
      marieData.contactDay = "MON_FRI";

      marieData.menstruate = true;
      marieData.firstDateOfLastPeriod = new DateTime.now().subtract(new Duration(days: 25));
      marieData.averagePeriodLength = 5;
      marieData.typicalCycleLength = 28;
      marieData.sexualActivity = [Enums.sexualActivity.first];
      marieData.birthControl = [Enums.birthControl.first];
      marieData.weight = "53";
      marieData.weightUnit = "lbs";
      marieData.heightFeet = "5";
      marieData.heightInch = "0";
      marieData.heightUnit = "ft";

      TestDataHolder.CUSTOMER_MARIE = await api.signup(marieData);
    }

    {
      UserData liseData = new UserData();
      liseData.fullName = "Lise Meitner";
      liseData.email = "_lise@qurasense.com";
      liseData.password = "secret";
      liseData.phone = "12345678";

      liseData.birthDate = new DateTime(1989, DateTime.APRIL, 29);
      liseData.addressLine = "1450 Page Mill Rd";
      liseData.city = "Palo Alto";
      liseData.state = 'CA';
      liseData.zip = "94020";
      liseData.country = CountryEnum.countries.first;
      liseData.contactTimes = ['AFTER_NOON'];
      liseData.contactDay = "MON_FRI";

      liseData.menstruate = true;
      liseData.firstDateOfLastPeriod =
          new DateTime.now().subtract(new Duration(days: 25));
      liseData.averagePeriodLength = 5;
      liseData.typicalCycleLength = 28;
      liseData.sexualActivity = [Enums.sexualActivity.first];
      liseData.birthControl = [Enums.birthControl.first];
      liseData.weight = "53";
      liseData.weightUnit = "lbs";
      liseData.heightFeet = "5";
      liseData.heightInch = "0";
      liseData.heightUnit = "ft";

      TestDataHolder.CUSTOMER_LISE = await api.signup(liseData);
    }

    print("created customers");
  }

  static Future<Null> _createTrialParticipants(UserApi userApi, TrialApi trialApi) async {
    var participantId = await trialApi.assignCustomerToTrial(TestDataHolder.CUSTOMER_MARIE, TestDataHolder.TRIAL_SANDIEGO);
    await trialApi.assignNurseToParticipant(participantId, TestDataHolder.USER_NURSE);
    await trialApi.updateParticipantStatus(participantId, "APPROVED");

    print("created trial participants");
  }

  static Future<Null> _createSamples(UserApi userApi, SampleApi sampleApi) async {
    //wait till user service receive message about user was approved
    bool loggedIn = false;
    while (!loggedIn) {
      try {
        await userApi.login("_marie@qurasense.com", "secret");
        loggedIn = true;
      } catch (error) {
        print(error);
        sleep(const Duration(seconds:1));
      }
    }

    TestDataHolder.SAMPLE_MARIE = await _registerSampleUse(sampleApi, TestDataHolder.CUSTOMER_MARIE);
    await _registerSampleFinish(sampleApi, TestDataHolder.SAMPLE_MARIE);
    await _registerSampleFeedback(sampleApi, TestDataHolder.SAMPLE_MARIE);

    print("created samples");
  }

  static Future<String> _registerSampleUse(SampleApi sampleApi, String customerId) async {
    var sample = new StripSample();
    sample.kitId = "123";
    sample.createUserId = customerId;
    sample.customerId = customerId;
    sample.createTime = new DateTime.now();
    sample.padStartTime = (new DateTime.now()).subtract(new Duration(days: 1));
    sample.periodStartDate = (new DateTime.now()).subtract(new Duration(days: 2));
    sample.stepNumber = 2;
    return sampleApi.saveStripSample(sample);
  }

  static Future<String> _registerSampleFinish(SampleApi sampleApi, String sampleId) async {
    var sample = await sampleApi.getSampleById(sampleId);
    sample.padRemoveTime = new DateTime.now();
    sample.padRemovePictureBlobName = "remove-pad-blob";
    sample.padRemovePictureTime = new DateTime.now();
    sample.stripRemovePictureBlobName = "remove-strip-blob";
    sample.stripRemovePictureTime = new DateTime.now();
    sample.bleedIntensity = "MEDIUM";
    //enum bleedIntensityEnum {  SPOOTING,  LIGHT,  MEDIUM,  HEAVY,  };
    sample.stepNumber = 3;
    return sampleApi.saveStripSample(sample);
  }

  static Future<Null> _registerSampleFeedback(SampleApi sampleApi, String sampleId) async {
    var sample = await sampleApi.getSampleById(sampleId);
    sample.primaryActivities = ["SITTING", "WALKING"];
    //enum primaryActivitiesEnum {  SITTING,  SLEEPING,  SPORTS,  RUNNING,  BIKING,  WALKING,  };
    sample.padSaturation = "EASY";
    //enum saturateKnowingEnum {  EASY,  MEDIUM,  HARD,  };
    sample.padComfort = "OKAY";
    //enum comfortLevelEnum {  TERRIBLE,  OKAY,  GREAT,  };
    sample.padFeedback = "Awesome product";
    sample.stepNumber = 5;
    /* TODO
    DateTime createTime = null;
    DateTime updateTime = null;
    */
    return sampleApi.saveStripSample(sample);
  }

//  static Future<Null> _createHealthRecord(HealthApi healthApi, String customerId, String createUserId) async {
//    HealthRecord record = new HealthRecord();
//    record.customerUserId = customerId;
//    record.createUserId = createUserId;
//
//    await healthApi.saveHealthRecord(record);
//  }

  static User _createUser(String fullName, String email, String role) {
    var user = new User();
    user.fullName = fullName;
    user.email = email;
    user.role = role;
    return user;
  }
}