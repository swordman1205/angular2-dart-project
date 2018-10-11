package com.qurasense.labApi.trial.utest;

import java.util.Date;

import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.labApi.itest.LabApiEmulatorApplication;
import com.qurasense.labApi.trial.model.ParticipantStatus;
import com.qurasense.labApi.trial.model.Trial;
import com.qurasense.labApi.trial.model.TrialParticipant;
import com.qurasense.labApi.trial.service.TrialParticipantService;
import com.qurasense.labApi.trial.service.TrialService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LabApiEmulatorApplication.class)
@ActiveProfiles("emulator")
@Ignore
public class TrialParticipantServiceEmulatorUnitTestSuite {

    @Autowired
    private TrialParticipantService trialParticipantService;

    @Autowired
    private TrialService trialService;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Autowired
    private EntityRepository entityRepository;

    @Test
    public void testAddSampleToSession() {
        ObjectifyService.run(() -> {
            String adminId = idGenerationStrategy.generate(null);

            Trial trial = new Trial();
            trial.setId(idGenerationStrategy.generate(null));
            trial.setCreateUserId(adminId);
            trial.setCreateTime(new Date());
            trial.setCompensationAmount(25l);
            trial.setName("San Diego");
            entityRepository.save(trial);

            String customerId = idGenerationStrategy.generate(null);

            TrialParticipant tp = new TrialParticipant();
            tp.setCustomerId(customerId);
            tp.setStatus(ParticipantStatus.APPROVED);
            tp.setTrialId(trial.getId());
            tp.setHasKits(false);
            trialParticipantService.createTrialParticipant(tp);

            trialParticipantService.addSampleToSession(customerId, idGenerationStrategy.generate(null));
            return null;
        });
    }

}
