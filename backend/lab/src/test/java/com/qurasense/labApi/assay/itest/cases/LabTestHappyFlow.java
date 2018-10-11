package com.qurasense.labApi.assay.itest.cases;

import com.qurasense.labApi.assay.commands.CreateAssayCommand;
import com.qurasense.labApi.assay.model.Assay;
import com.qurasense.labApi.assay.model.AssayUnit;
import com.qurasense.labApi.assay.service.AssayService;
import com.qurasense.labApi.itest.LabApiTestApplication;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LabApiTestApplication.class)
public class LabTestHappyFlow {
    @Autowired
    private AssayService service;


    @Autowired
    private CommandGateway commandGateway;

    @Test
    @WithMockUser(username="admin", authorities={"ADMIN"})
    public void createAssay() {
        assertThat(service.getAssays(), hasSize(0));

        Assay assay = new Assay("123","HbA1c", AssayUnit.PERCENT, 4.2, 6.0);
        commandGateway.send(new CreateAssayCommand(assay.getId(), assay.getName(), "unitType",
                assay.getRangeMin(), assay.getRangeMax()));

        service.createAssay(assay);

        assertThat(service.getAssays(), hasSize(1));
    }

    /**
     * Note
     An e-mail is send to the sample collector organization the first time a sample collection process is initiated by the user.

     As a comparative sample is registered for  collection, every nurse assigned to the organization can view the users in their home screen
     */

    @Test
    public void assignAssayToLaberotary() {

    }

    @Test
    public void createTrial() {

    }

    @Test
    public void assignTrialParticpant() {

    }

    @Test
    public void createSampleTestJobFromTrial() {

    }
}
