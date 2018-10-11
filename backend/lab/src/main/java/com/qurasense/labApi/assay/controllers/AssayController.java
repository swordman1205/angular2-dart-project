package com.qurasense.labApi.assay.controllers;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import com.qurasense.labApi.assay.model.Assay;
import com.qurasense.labApi.assay.service.AssayService;
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
@RequestMapping(value = "/assays")
@Api(value="Assyas", description="Assay api")
public class AssayController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AssayService service;

    @ApiOperation(value = "create assay")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Assay assay) {
        String id = service.createAssay(assay);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).body(id);
    }

    @ApiOperation(value = "Get assay", response = Assay.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Assay> get(@PathVariable("id") String id) {
        Assay assay = service.findAssay(id);
        if (Objects.nonNull(assay)) {
            return ResponseEntity.ok(assay);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "List assays", response = Assay.class, responseContainer = "List",
            authorizations = @Authorization(
                    value = "oauth2",
                    scopes = {
                            @AuthorizationScope(
                                    scope = "Only Admin can list all biomarkers", description = ""
                            )
                    }
            ))
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Assay> list() {
        return service.getAssays();
    }

    @ApiOperation(value = "Update assay", response = Assay.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Assay> update(@PathVariable("id") String id, @RequestBody Assay assay) {
        service.createAssay(assay);
        return null;
    }
}
