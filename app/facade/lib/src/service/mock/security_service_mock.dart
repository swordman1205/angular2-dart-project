part of app_facade;

class SecurityServiceMock implements SecurityService {
  @override
  void clearTokens() {
    throw "should not be used in mock";
  }

  @override
  String getAccessToken() {
    throw "should not be used in mock";
  }

  @override
  Future<User> getCurrentUser() async {
    return _currentUser;
  }

  @override
  Future<String> getCurrentUserId() async {
    return _currentUser?.id;
  }

  @override
  void setTokens(String userId, String accessToken, String refreshToken) {
    throw "should not be used in mock";
  }
}
