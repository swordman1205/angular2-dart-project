import 'dart:async';

import 'package:test/test.dart';
import 'package:app_mobile/utils/dependency_injection.dart';
import 'package:app_facade/app_facade.dart';

void main() {
  test('unit test: only customer can login', () async {
    new Injector(backendType: BackendType.MOCK);
    UserApi userApi = new Injector().userApi;
    try {
      await userApi.login("_admin@qurasense.com", "secret");
      expect(false, equals(true), reason: 'should not be here');
    } catch (e) {
      expect(e, equals("Sorry, only customer can login from mobile device. [Mock]"));
    }
    User user = await userApi.login("_lise@qurasense.com", "secret");
    expect(user.email, equals("_lise@qurasense.com"));
  });
}