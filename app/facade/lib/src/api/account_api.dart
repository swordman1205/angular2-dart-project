// Copyright (c) 2017, Qurasense. All rights reserved.
part of app_facade;

abstract class AccountApi {
  Future<bool> addOrganizationMember(User user, Organization organization);

  Future<Iterable<User>> getOrganizationMembers(Organization organization);

  Future<Iterable<Organization>> getOrganizations();

  Future<Iterable<Organization>> getOrganizationsOfType(String type);

  Future<bool> removeOrganizationMember(User user, Organization organization);

  Future<String> saveOrganization(Organization organization);

  Future<Organization> getOrganizationById(String id);
}

