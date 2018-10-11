part of app_facade;

// Copyright (c) 2017, Qurasense. All rights reserved.

class AccountApiMock implements AccountApi {
  Map<String, Organization> _organizations = {};

  @override
  Future<bool> addOrganizationMember(User user, Organization organization) {
    // TODO: implement addOrganizationMember
  }

  @override
  Future<Iterable<User>> getOrganizationMembers(Organization organization) {
    // TODO: implement getOrganizationMembers
  }

  @override
  Future<Iterable<Organization>> getOrganizations() async {
    return _organizations.values;
  }

  @override
  Future<Iterable<Organization>> getOrganizationsOfType(String type) async {
    return (await getOrganizations()).where((o) => o.type == type);
  }

  @override
  Future<bool> removeOrganizationMember(User user, Organization organization) {
    // TODO: implement removeOrganizationMember
  }

  @override
  Future<String> saveOrganization(Organization organization) async {
    if(organization.id == null) {
      organization.id = _nextUid();
    }
    _organizations[organization.name] = organization;
    return organization.id;
  }

  @override
  Future<Organization> getOrganizationById(String id) async {
    _organizations.values.firstWhere((o) => o.id == id, orElse: null);
  }
}