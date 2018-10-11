package com.qurasense.userApi.controllers;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import com.qurasense.userApi.model.Laboratory;
import com.qurasense.userApi.model.Organization;
import com.qurasense.userApi.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = OrganizationController.URL)
@Api(value="Trials", description="Trial api")
public class OrganizationController {

    public static final String URL= "/organizations";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrganizationService service;

    @ApiOperation(value = "create trial")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Organization organization) {
        String id = service.saveOrganization(organization);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).body(id);
    }

    @ApiOperation(value = "Get trial", response = Organization.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Organization> get(@PathVariable("id") String id) {
        Organization organization = service.findOrganizationById(id);
        if (Objects.nonNull(organization)) {
            return ResponseEntity.ok(organization);
        }
        return ResponseEntity.notFound().build();
    }


    @ApiOperation(value = "List trials", response = Organization.class, responseContainer = "List",
            authorizations = @Authorization(
                    value = "oauth2",
                    scopes = {
                            @AuthorizationScope(
                                    scope = "Only Admin can list all trials", description = ""
                            )
                    }
            ))
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Organization> list() {
        return service.getOrganizations();
    }

    @RequestMapping(value = "/laboratories/", method = RequestMethod.GET)
    public List<Laboratory> listLaboratories() {
        return service.getLaboratories();
    }

    @ApiOperation(value = "Update trial", response = Organization.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Organization> update(@PathVariable("id") String id,
            @RequestBody Organization organization) {
        service.saveOrganization(organization);
        return null;
    }
}
