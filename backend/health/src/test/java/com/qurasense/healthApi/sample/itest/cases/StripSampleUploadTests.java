package com.qurasense.healthApi.sample.itest.cases;

import java.util.Date;

import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.healthApi.HealthApiTestApplication;
import com.qurasense.healthApi.sample.model.StripSample;
import com.qurasense.healthApi.sample.repository.SampleRepository;
import com.qurasense.healthApi.sample.storage.StorageService;
import com.qurasense.labApi.itest.oauth2.OAuthHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure = false)
@SpringBootTest(classes = HealthApiTestApplication.class)
public class StripSampleUploadTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private OAuthHelper helper;

    @Autowired
    private StorageService storageService;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Autowired
    private EntityRepository entityRepository;

    private String sampleId;

    @Before
    public void setUp() {
        StripSample stripSample = new StripSample();
        stripSample.setId(idGenerationStrategy.generate(null));
        stripSample.setCreateTime(new Date());
        stripSample.setCreateUserId(idGenerationStrategy.generate(null));
        entityRepository.save(stripSample);
        sampleId = stripSample.getId();
    }

    @Test
    public void userShouldSaveRemovePicture() throws Exception {
        RequestPostProcessor bearerToken = helper.bearerToken("1", "CUSTOMER");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        this.mvc.perform(multipart("/1/sample/" + sampleId + "/removePicture").file(multipartFile).with(bearerToken))
                .andExpect(status().isOk());

        then(this.storageService).should().store("1", multipartFile);
    }

    @Test
    public void medicalShouldSaveUploadedFile() throws Exception {
        RequestPostProcessor bearerToken = helper.bearerToken("1", "MEDICAL");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        this.mvc.perform(multipart("/2/sample/" + sampleId + "/removePicture").file(multipartFile).with(bearerToken))
                .andExpect(status().isOk());

        then(this.storageService).should().store("2", multipartFile);
    }

    @Test
    public void labShouldNotSaveRemovePicture() throws Exception {
        RequestPostProcessor bearerToken = helper.bearerToken("1", "LAB_TECH");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        this.mvc.perform(multipart("/2/sample/" + sampleId + "/removePicture").file(multipartFile).with(bearerToken))
                .andExpect(status().isForbidden());
    }

}

