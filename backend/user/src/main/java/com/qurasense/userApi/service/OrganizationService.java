package com.qurasense.userApi.service;

import java.util.List;

import com.qurasense.userApi.model.Laboratory;
import com.qurasense.userApi.model.Organization;

public interface OrganizationService {
    String saveOrganization(Organization organization);

    Organization findOrganizationById(String id);

    List<Organization> getOrganizations();

    List<Laboratory> getLaboratories();
}
