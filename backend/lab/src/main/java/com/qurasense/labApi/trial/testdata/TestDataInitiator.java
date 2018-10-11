package com.qurasense.labApi.trial.testdata;

import javax.annotation.PostConstruct;

import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.labApi.trial.model.Trial;
import com.qurasense.labApi.trial.model.TrialParticipant;
import com.qurasense.labApi.trial.model.TrialSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"test"})
@Component("trialTestDataInitiator")
public class TestDataInitiator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityRepository entityRepositoryRepository;

    @PostConstruct
    protected void initTestData() {
        ObjectifyService.run(() -> {
            int deletedTrialCount = entityRepositoryRepository.deleteAll(Trial.class);
            int deletedParticipantCount = entityRepositoryRepository.deleteAll(TrialParticipant.class);
            int deletedSessionCount = entityRepositoryRepository.deleteAll(TrialSession.class);
            logger.info("deleted {} trials, {} participants, {} sessions", deletedTrialCount, deletedParticipantCount, deletedSessionCount);
            return null;
        });
    }

}
