// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

/**
 * Service facade for user_api
 */
abstract class UserApi {

  Future<bool> checkEmailExist(String email);

  Future<String> createUserByAdmin(User user, String password);

  Future<Null> deleteUser(String userId);

  Future<Null> downloadCustomers();

  Future<CustomerInfo> getCustomerInfo(String userId);

  Future<Iterable<CustomerInfo>> getCustomers();

  Future<List<CustomerInfo>> getCustomersByIds(Set<String> list);
  
  Future<List<CustomerInfo>> getCustomersByUserIds(Iterable<String> customerUserIds);

  Future<Iterable<User>> getNurses();

  Future<User> getUser(String userId);

  Future<Iterable<User>> getUsers();

  Future<User> login(String email, String password);

  Future<Null> logout();

  Future<Null> requestResetPassword(String email);

  Future<Null> restorePassword(String token, String youngPassword);

  Future<Null> saveCustomerInfo(CustomerInfo customerInfo);

//  Future<Null> updateCustomerStatus(String customerId, String youngStatus);

  Future<Null> updateUser(User user);

  Future<Null> updateUserByAdmin(User user, String password);

  Future<bool> confirmEmail(String token);

}


