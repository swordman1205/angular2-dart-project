package com.qurasense.labApi.trial.utest;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.labApi.itest.LabApiEmulatorApplication;
import com.qurasense.labApi.trial.model.ParticipantStatus;
import com.qurasense.labApi.trial.model.Trial;
import com.qurasense.labApi.trial.model.TrialParticipant;
import com.qurasense.labApi.trial.model.TrialSession;
import com.qurasense.labApi.trial.model.TrialSessionStatus;
import com.qurasense.labApi.trial.service.TrialParticipantService;
import com.qurasense.labApi.trial.service.TrialSessionService;
import org.junit.Assert;
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
public class TrialSessionServiceEmulatorUnitTestSuite {

    @Autowired
    private TrialParticipantService trialParticipantService;

    @Autowired
    private TrialSessionService trialSessionService;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Autowired
    private EntityRepository entityRepository;

    @Test
    public void testAddSampleToSession() {
        AtomicReference<Trial> trialRef = new AtomicReference<>();
        String adminId = idGenerationStrategy.generate(null);
        String sampleId = idGenerationStrategy.generate(null);
        String customerId = idGenerationStrategy.generate(null);
        ObjectifyService.run(() -> {

            Trial trial = new Trial();
            trial.setId(idGenerationStrategy.generate(null));
            trial.setCreateUserId(adminId);
            trial.setCreateTime(new Date());
            trial.setCompensationAmount(25l);
            trial.setName("San Diego");
            entityRepository.save(trial);

            trialRef.set(trial);

            TrialParticipant tp = new TrialParticipant();
            tp.setCustomerId(customerId);
            tp.setStatus(ParticipantStatus.APPROVED);
            tp.setTrialId(trial.getId());
            tp.setHasKits(false);
            trialParticipantService.createTrialParticipant(tp);

            return null;
        });

        ObjectifyService.run(() -> {
            trialParticipantService.addSampleToSession(customerId, sampleId);
            return null;
        });

        ObjectifyService.run(()->{
            Assert.assertEquals(1, trialSessionService.fetchSessions(trialRef.get().getId()).size());
            List<TrialSession> fetchedActiveSessions = trialSessionService.fetchSessions(trialRef.get().getId(), TrialSessionStatus.ACTIVE.name());
            Assert.assertEquals(1, fetchedActiveSessions.size());
            return null;
        });
    }

}
