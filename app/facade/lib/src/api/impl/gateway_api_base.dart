// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

abstract class GatewayApiBase implements GatewayApi {
  final ApiClient _client;

  GatewayApiBase(this._client);

  @override
  Future<Null> ping() async {
    Stopwatch sw = new Stopwatch();
    sw.start();
    var userApiResponse = await _client.get("/user_api/ping", defaults: []);
    if (userApiResponse.statusCode != 200) {
      throw "error ping user api";
    }
    sw.stop();
    print("ping userapi time(seconds): ${sw.elapsed.inSeconds}");

    sw.reset();
    sw.start();
    var healthApiResponse = await _client.get("/health_api/ping", defaults: []);
    if (healthApiResponse.statusCode != 200) {
      throw "error ping health api";
    }
    sw.stop();
    print("ping healthapi time(seconds): ${sw.elapsed.inSeconds}");

    sw.reset();
    sw.start();
    var labApiResponse = await _client.get("/lab_api/ping", defaults: []);
    if (labApiResponse.statusCode != 200) {
      throw "error ping lab api";
    }
    sw.stop();
    print("ping labapi time(seconds): ${sw.elapsed.inSeconds}");
  }

  @override
  Future<String> getVersion() async {
    var response = await _client.get("/version");
    if (response.statusCode == 200) {
      return response.body;
    }
    throw "error while get version";
  }

  @override
  Future<String> signup(UserData userData) async {
    var encoded = JSON.encode(userData, toEncodable: MappingUtils.toJsonUserData);
    var response = await _client.post("/signup", body: encoded);
    if (response.statusCode == 200) {
      return response.body;
    }
    throw "error while signup: ${response.body}";
  }
}