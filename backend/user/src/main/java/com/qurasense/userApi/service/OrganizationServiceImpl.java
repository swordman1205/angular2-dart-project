package com.qurasense.userApi.service;

import java.util.Collections;
import java.util.List;

import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.userApi.model.Laboratory;
import com.qurasense.userApi.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Override
    public String saveOrganization(Organization organization) {
        if (organization.getId() == null) {
            organization.setId(idGenerationStrategy.generate(null));
        }
        return entityRepository.save(organization);
    }

    @Override
    public Organization findOrganizationById(String id) {
        return entityRepository.findById(Organization.class, id);
    }

    @Override
    public List<Organization> getOrganizations() {
        return entityRepository.findAll(Organization.class);
    }

    @Override
    public List<Laboratory> getLaboratories() {
        return Collections.emptyList();
    }
}
