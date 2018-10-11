// Copyright (c) 2017, Qurasense. All rights reserved.
part of app_facade;

class GatewayApiMock implements GatewayApi {
  @override
  Future<Null> downloadSamplesZip(List<UserSampleId> request) async {
    return null;
  }

  @override
  Future<Null> downloadCustomersAndHealths() async {
    return null;
  }

  @override
  Future<String> getVersion() async {
    return "mock version";
  }

  @override
  Future<String> signup(UserData ud) async {
    var injector = new TestDataInjector(BackendType.MOCK);
    UserApi userApi = injector.userApi;
    HealthApi healthApi = injector.healthApi;
    User user = new User();
    user.email = ud.email;
    user.role = 'CUSTOMER';
    user.fullName = ud.fullName;
    String userId = await userApi.createUserByAdmin(user, ud.password);

    CustomerInfo ci = new CustomerInfo();
    ci.fullName = ud.fullName;
    ci.email = ud.email;
    ci.dateOfBirth = ud.birthDate;
    ci.addressLine = ud.addressLine;
    ci.city = ud.city;
    ci.state = ud.state;
    ci.zip = ud.zip;
    ci.country = ud.country.name;
    ci.contactTimes = ud.contactTimes;
    ci.contactDay = ud.contactDay;
    ci.userId = userId;
    await userApi.saveCustomerInfo(ci);

    HealthInfo hi = new HealthInfo();
    hi.userId = userId;
    hi.menstruate = ud.menstruate;
    hi.firstDateOfLastPeriod = ud.firstDateOfLastPeriod;
    hi.averagePeriodLength = ud.averagePeriodLength;
    hi.typicalCycleLength = ud.typicalCycleLength;
    hi.sexualActivity = ud.sexualActivity.isNotEmpty ? ud.sexualActivity.first.name : null;
    hi.birthControls = ud.birthControl.map((ew)=>ew.name).toList();
    hi.weight = ud.weightUnit.toLowerCase() == 'lbs' ? MetricUtils.lbsToKg(int.parse(ud.weight)): int.parse(ud.weight);
    hi.weightUnit = ud.weightUnit.toLowerCase() == 'lbs' ? 'IMPERIAL' : 'METRIC';
    hi.height = ud.heightUnit.toLowerCase() == 'ft' ? MetricUtils.feetInchToCm(int.parse(ud.heightFeet), int.parse(ud.heightInch)) : int.parse(ud.heightCm);
    hi.heightUnit = ud.heightUnit.toLowerCase() == 'ft' ? 'IMPERIAL' : 'METRIC';
    await healthApi.saveHealthInfo(hi);
    return userId;
  }

  @override
  Future<Null> ping() {
  }

}