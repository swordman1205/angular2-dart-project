package com.qurasense.labApi.trial.utest.cases;

import com.qurasense.labApi.itest.LabApiTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LabApiTestApplication.class)
public class LabErrorFlowTest {
    @Test
    public void createAssayFailsIfAlreadyExists() {

    }
    /*
    @Autowired
    private LabService service;
    @Mock
    private EventBus eventBus;

    @Test
    public void createAssayFailsIfAlreadyExists() {
        assertThat(service.getAssays(), hasSize(0));

        Assay assay = new Assay("123","HbA1c", AssayUnit.PERCENT, 4.2, 6.0);
        service.createAssay(assay);

        Assay assay = new Assay("456","HbA1c", AssayUnit.PERCENT, 4.2, 6.0);
        service.createAssay(assay);

        assertThat(service.getAssays(), hasSize(1));
    }

    @Test
    public void assignAssayFailsIfLabDoesNotExists() {
        Assay assay = service.getAssays().get(0);
        assertThat(assay, is(notNullValue()));
        String labId = "123";
        // TODO faling with laberoatory not created
        // first we fail because app does not exists
        service.addAssayLaboratory(labId, assay.getId());

        service.
    }
    */
}

