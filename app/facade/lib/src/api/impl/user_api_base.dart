// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

abstract class UserApiBase implements UserApi {
  final ApiClient _client;
  final SecurityService _securityService;

  UserApiBase(this._client, this._securityService);

  @override
  Future<bool> checkEmailExist(String email) async {
    var response = await _client.get("/user_api/registration/$email/exists", defaults: []);
    return response.body.toLowerCase() == true.toString();
  }

  @override
  Future<String> createUserByAdmin(User user, String password) async {
    var encoded = JSON.encode(user, toEncodable: (u) => MappingUtils.toJsonUserWithPassword(u, password));
    var response = await _client.post("/user_api/user/create", body: encoded);
    if (response.statusCode == 200) {
      return response.body;
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<Null> deleteUser(userId) async {
    final response = await _client.delete("/user_api/user/$userId");
    if (response.statusCode != 200) {
      print(response.body);
      throw new ErrorInfo(response.body);
    }
  }

  @override
  Future<CustomerInfo> getCustomerInfo(userId)  async {
    var response = await _client.get("/user_api/user/$userId/info");
    if (response.statusCode == 200) {
      Map<String, dynamic> map = JSON.decode(response.body);
      return MappingUtils.fromJsonCustomerInfo(map);
    }
    throw "error while fetch user info";
  }

  @override
  Future<Iterable<CustomerInfo>> getCustomers() async {
    final customersResponse = await _client.get("/user_api/customers");
    List<dynamic> customersJson = JSON.decode(customersResponse.body);
    return customersJson.map((json) => MappingUtils.fromJsonCustomerInfo(json)).toList();
  }

  @override
  Future<List<CustomerInfo>> getCustomersByIds(Set<String> list) async {
    var response = await _client.post("/user_api/customers/ids",
        body: JSON.encode(list.toList())
    );
    if (response.statusCode != 200) {
      throw new ErrorInfo(response.body);
    }
    List<dynamic> usersJson = JSON.decode(response.body);
    return usersJson.map((json) => MappingUtils.fromJsonCustomerInfo(json)).toList();
  }

  @override
  Future<List<CustomerInfo>> getCustomersByUserIds(Iterable<String> customerUserIds) async {
    var response = await _client.get("/user_api/customers/userIds", params: {'customerUserIds': customerUserIds});
    if (response.statusCode != 200) {
      throw new ErrorInfo(response.body);
    }
    List<dynamic> usersJson = JSON.decode(response.body);
    return usersJson.map((json) => MappingUtils.fromJsonCustomerInfo(json)).toList();
  }

  @override
  Future<Iterable<User>> getNurses() async {
    var usersResponse = await _client.get("/user_api/users/nurse");
    List<dynamic> usersJson = JSON.decode(usersResponse.body);
    return usersJson.map((json) => MappingUtils.fromJsonUser(json)).toList();
  }

  @override
  Future<User> getUser(userId) async {
    final userResponse = await _client.get("/user_api/user/$userId");
    Map<String, dynamic> map = JSON.decode(userResponse.body);
    User user = MappingUtils.fromJsonUser(map);
    return user;
  }

  @override
  Future<Iterable<User>> getUsers() async {
    var usersResponse = await _client.get("/user_api/users");
    List<dynamic> usersJson = JSON.decode(usersResponse.body);
    return usersJson.map((json) => MappingUtils.fromJsonUser(json)).toList();
  }

  @override
  Future<Null> logout() async {
//    _client.closeSession();
    _securityService.clearTokens();
    var response = await _client.post("/user_api/logout", defaults: []);
    if (response.statusCode != 200) {
      print("error on logout");
    }
  }

  @override
  Future<Null> requestResetPassword(String email) async {
    var response = await _client.get("/user_api/registration/$email/requestRestorePassword", defaults: []);
    if (response.statusCode != 200) {
      throw 'error while restore password request';
    }
  }

  @override
  Future<Null> restorePassword(String token, String youngPassword) async {
    var response = await _client.post("/user_api/registration/restorePassword/$token",
        defaults: [],
        body: {'password': youngPassword});
    if (response.statusCode != 200) {
      throw 'error while restore password';
    }
  }

  @override
  Future<Null> saveCustomerInfo(CustomerInfo customerInfo) async {
    var encoded = JSON.encode(customerInfo, toEncodable: MappingUtils.toJsonCustomerInfo);
    var response = await _client.post("/user_api/user/${customerInfo.userId}/info", body: encoded);
    if (response.statusCode == 200) {
      return;
    }
    throw "current user info submit error";
  }

  @override
  Future<Null> updateCustomerStatus(String customerId, String youngStatus) async {
    var response = await _client.post("/user_api/customer/$customerId/status/$youngStatus");
    if (response.statusCode != 200) {
      throw new ErrorInfo(response.body);
    }
  }

  @override
  Future<Null> updateUser(User user) async {
    var encoded = JSON.encode(user, toEncodable: (User u) => {
      'id': u.id,
      'fullName': u.fullName,
      'email': u.email
    });
    var response = await _client.post("/user_api/user/${user.id}", body: encoded);
    if (response.statusCode == 200) {
      return;
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<Null> updateUserByAdmin(User user, String password) async {
    var encoded = JSON.encode(user, toEncodable: (u) => MappingUtils.toJsonUserWithPassword(u, password));
    var response = await _client.post("/user_api/admin/user/${user.id}", body: encoded);
    if (response.statusCode == 200) {
      return;
    }
    throw new ErrorInfo(response.body);
  }

  @override
  Future<bool> confirmEmail(String token) async {
    var response = await _client.post("/user_api/registration/email/$token/confirm", defaults: []);
    if (response.statusCode == 200) {
      return response.body.toLowerCase() == true.toString();
    }
    throw new ErrorInfo(response.body);
  }
}
