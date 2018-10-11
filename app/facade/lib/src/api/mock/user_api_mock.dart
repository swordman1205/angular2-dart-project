// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

User _currentUser;

class UserApiMock implements UserApi {
  List<User> _users = [];
  Map<String, String> _userPassword = {};

  List<CustomerInfo> _customers = [];

  UserApiMock() {
    User user = new User();
    user.fullName = "Admin admin";
    user.email = "_admin@qurasense.com";
    user.role = "ADMIN";
    createUserByAdmin(user, "secret");
  }

  @override
  Future<bool> checkEmailExist(String email) async {
    return _users.where((u) => u.email == email).length > 0;
  }

  @override
  Future<Null> deleteUser(userId) {
    _users.removeWhere((u) => u.id == userId);
  }

  Future<User> getCurrentUser() async {
    return _currentUser;
  }

  @override
  Future<User> getUser(userId) async {
    return _users.firstWhere((u) => u.id == userId, orElse: () => null);
  }

  @override
  Future<Iterable<User>> getUsers() async {
    var currentUser =  await getCurrentUser();
    if(currentUser.role != "ADMIN") {
      throw "only admin can view users";
    }
    return _users;
  }

  @override
  Future<Iterable<User>> getNurses() async {
    return _users.where((u) => u.role == 'NURSE');
  }

  @override
  Future<Iterable<CustomerInfo>> getCustomers() async {
    var currentUser = await getCurrentUser();
    if(!["ADMIN", "MEDICAL"].contains(currentUser.role)) {
      throw "only admin or medical can view customers";
    }
    return _customers;
  }

  @override
  Future<User> login(String email, String password) async {
    print('start to login email: $email, [Mock]');
    var emailUser = _users.firstWhere((u) => u.email == email, orElse: () => null);
    if(emailUser == null) {
      throw "unknown user: $email";
    }
    if(password != _userPassword[emailUser.id]) {
      throw "wrong password $password";
    }
    var currentUser = emailUser;
    if (PlatformUtils.isMobile() && currentUser.role != 'CUSTOMER') {
      throw 'Sorry, only customer can login from mobile device. [Mock]';
    }
    _currentUser = currentUser;
    return currentUser;
  }

  @override
  Future<Null> logout() {
    _currentUser = null;
    return null;
  }

  @override
  Future<CustomerInfo> getCustomerInfo(userId) async {
    return _customers.firstWhere((c) => c.userId == userId, orElse: () => null);
  }

  @override
  Future<Null> saveCustomerInfo(CustomerInfo customerInfo) {
    if (customerInfo.id == null) {
      customerInfo.id = _nextId();
      _customers.add(customerInfo);
    } else {
      _customers.removeWhere((c) => c.id == customerInfo.id);
      _customers.add(customerInfo);
    }
  }

  @override
  Future<Null> updateUserByAdmin(User user, String password) {
    updateUser(user);
    _userPassword[user.id] = password;
  }

  @override
  Future<String> createUserByAdmin(User user, String password) async {
    user.id = _nextId();
    _users.add(user);
    _userPassword[user.id] = password;
    return user.id;
  }

  @override
  Future<Null> downloadCustomers() {
    // TODO: implement downloadCustomers
  }

  @override
  Future<Null> requestResetPassword(String email) {
    // TODO: implement requestResetPassword
  }

  @override
  Future<Null> restorePassword(String token, String password) {
    // TODO: implement restorePassword
  }
  @override
  Future<dynamic> updateCustomerStatus(String customerId, String youngStatusType) {
    var customerInfo = _customers.firstWhere((c) => c.id == customerId);
    var statusObject = new CustomerStatus();
    statusObject.time = new DateTime.now();
    statusObject.type = youngStatusType;
    if (customerInfo.customerStatus != null) {
      customerInfo.customerStatusHistory.add(customerInfo.customerStatus);
    }
    customerInfo.customerStatus = statusObject;
  }

  @override
  Future<List<CustomerInfo>> getCustomersByIds(Set<String> list) async {
    return _customers.where((c)=>list.contains(c.id)).toList();
  }

  @override
  Future<Null> updateUser(User user) {
    if (user.id == null) {
      throw "can`t update null id user";
    }
    var foundUser = _users.firstWhere((u) => u.id == user.id, orElse: () => null);
    if (foundUser != null) {
      _users.remove(foundUser.email);
    }
    _users.add(user);
  }

  @override
  Future<bool> confirmEmail(String token) {
    throw "not implemented in mock";
  }

  @override
  Future<List<CustomerInfo>> getCustomersByUserIds(Iterable<String> customerUserIds) {
    _customers.where((c)=>customerUserIds.contains(c.userid));
  }
}
