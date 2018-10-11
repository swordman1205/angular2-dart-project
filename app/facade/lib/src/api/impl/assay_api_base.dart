// Copyright (c) 2017, Qurasense. All rights reserved.
part of app_facade;

class AssayApiBase implements AssayApi {
  final ApiClient _client;

  AssayApiBase(this._client);

  @override
  Future<Assay> getAssayById(String id) async {
    final assayResponse = await _client.get("/lab_api/assays/$id");
    Map<String, dynamic> map = JSON.decode(assayResponse.body);
    Assay assay = MappingUtils.fromJsonAssay(map);
    return assay;
  }

  @override
  Future<Iterable<Assay>> getAssays() async {
    var assaysResponse = await _client.get("/lab_api/assays/");
    List<dynamic> assaysJson = JSON.decode(assaysResponse.body);
    return assaysJson.map((json) => MappingUtils.fromJsonAssay(json)).toList();
  }

  @override
  Future<String> saveAssay(Assay assay) async {
    var encoded = JSON.encode(assay, toEncodable: MappingUtils.toJsonAssay);
    var response = await _client.post("/lab_api/assays/", body: encoded);
    if (response.statusCode != 201) {
      print(response.body);
      print(response.statusCode);
      throw new ErrorInfo(response.body);
    }
    return response.body;
  }
}

