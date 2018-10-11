part of app_facade;

// Copyright (c) 2017, Qurasense. All rights reserved.

class AccountApiBase implements AccountApi {
  final ApiClient _client;

  AccountApiBase(this._client);

  @override
  Future<bool> addOrganizationMember(User user, Organization organization) async {
    // TODO: implement addOrganizationMember
  }

  @override
  Future<Iterable<User>> getOrganizationMembers(Organization organization) async {
    // TODO: implement getOrganizationMembers
  }

  @override
  Future<Iterable<Organization>> getOrganizations() async {
    var organizationResponse = await _client.get("/user_api/organizations/");
    List<dynamic> organizationsJson = JSON.decode(organizationResponse.body);
    return organizationsJson.map((json) => MappingUtils.fromJsonOrganization(json)).toList();
  }

  @override
  Future<Iterable<Organization>> getOrganizationsOfType(String type) async {
    // TODO: implement getOrganizationsOfType
  }

  @override
  Future<bool> removeOrganizationMember(User user, Organization organization) async {
    // TODO: implement removeOrganizationMember
  }

  @override
  Future<String> saveOrganization(Organization organization) async {
    var encoded = JSON.encode(organization, toEncodable: MappingUtils.toJsonOrganization);
    var response = await _client.post("/user_api/organizations/", body: encoded);
    if (response.statusCode != 201) {
      throw new ErrorInfo(response.body);
    }
    return response.body;
  }

  @override
  Future<Organization> getOrganizationById(String id) async {
    final organizationResponse = await _client.get("/user_api/organizations/$id");
    Map<String, dynamic> map = JSON.decode(organizationResponse.body);
    return MappingUtils.fromJsonOrganization(map);
  }

}