// Copyright (c) 2017, Qurasense. All rights reserved.
part of app_facade;

abstract class SecurityService {
  void clearTokens();

  String getAccessToken();

  Future<User> getCurrentUser();

  Future<String> getCurrentUserId();

  void setTokens(String userId, String accessToken, String refreshToken);
}