package com.qurasense.labApi.trial.itest.cases;

import com.qurasense.labApi.itest.LabApiTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LabApiTestApplication.class)
public class LabTestHappyFlow {

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
