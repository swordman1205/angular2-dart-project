package com.qurasense.labApi.trial.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.googlecode.objectify.Key;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.messaging.AbstractMessagePublisher;
import com.qurasense.common.messaging.broadcast.message.CustomerAddedToTrial;
import com.qurasense.common.messaging.broadcast.message.SessionCreated;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.token.TokenGenerator;
import com.qurasense.labApi.trial.model.ParticipantStatus;
import com.qurasense.labApi.trial.model.SessionStatusChangeToken;
import com.qurasense.labApi.trial.model.TrialParticipant;
import com.qurasense.labApi.trial.model.TrialSession;
import com.qurasense.labApi.trial.model.TrialSessionStatus;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Component
public class TrialParticipantServiceImpl implements TrialParticipantService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Resource(name = "broadcastMessagePublisher")
    private AbstractMessagePublisher broadcastMessagePublisher;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Override
    public String createTrialParticipant(TrialParticipant trialParticipant) {
        trialParticipant.setParent(entityRepository.getParent(TrialParticipant.class));
        trialParticipant.setId(idGenerationStrategy.generate(null));
        entityRepository.save(trialParticipant);
        broadcastMessagePublisher.publishMessage(new CustomerAddedToTrial(trialParticipant.getCustomerId()));
        logger.info("customer {} assigned to trial {}", trialParticipant.getCustomerId(), trialParticipant.getTrialId());
        return trialParticipant.getId();
    }

    @Override
    public TrialParticipant findTrialParticipantById(String id) {
        return entityRepository.findByIdWithAncestor(TrialParticipant.class, id);
    }

    @Override
    public List<TrialParticipant> fetchTrialParticipantsByIds(List<String> participantIds) {
        return entityRepository.fetchByIdsWithAncestor(TrialParticipant.class, participantIds);
    }

    @Override
    public List<TrialParticipant> fetchTrialParticipants(String customerId) {
        return ofy().load().type(TrialParticipant.class)
                .ancestor(entityRepository.getParent(TrialParticipant.class))
                .filter("customerId =", customerId)
                .list();
    }


    @Override
    public List<TrialParticipant> fetchTrialParticipants(String customerId, ParticipantStatus status) {
        return ofy().load().type(TrialParticipant.class)
                .ancestor(entityRepository.getParent(TrialParticipant.class))
                .filter("customerId =", customerId)
                .filter("status =", status)
                .list();
    }

    @Override
    public List<TrialParticipant> fetchTrialParticipantsByNurseId(String nurseId, List<String> sessionStatuses) {
        List<TrialParticipant> participants = ofy().load().type(TrialParticipant.class)
                .ancestor(entityRepository.getParent(TrialParticipant.class))
                .filter("nurseId =", nurseId)
                .list();

        Set<TrialSessionStatus> statuses = sessionStatuses.stream()
                .map(sss -> TrialSessionStatus.valueOf(sss))
                .collect(Collectors.toSet());

        return participants.stream().filter(tp -> {
            List<TrialSession> list = ofy().load().type(TrialSession.class)
                    .ancestor(entityRepository.getParent(TrialSession.class))
                    .filter("trialParticipant =", Key.create(TrialParticipant.class, tp.getId()))
                    .list();
            if (statuses.isEmpty()) {
                return !list.isEmpty();
            } else {
                return list.stream().anyMatch(ts -> statuses.contains(ts.getStatus()));
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean updateParticipantStatus(String participantId, String status) {
        ParticipantStatus participantStatus = ParticipantStatus.valueOf(status);
        TrialParticipant participant = findTrialParticipantById(participantId);
        if (Objects.isNull(participant)) {
            return false;
        }
        participant.setStatus(participantStatus);
        entityRepository.save(participant);
        return true;
    }

    @Override
    public boolean assignNurse(String participantId, String nurseId) {
        logger.info("assing participant {} to nurse {}", participantId, nurseId);
        TrialParticipant participant = findTrialParticipantById(participantId);
        if (Objects.isNull(participant)) {
            return false;
        }
        participant.setNurseId(nurseId);
        entityRepository.save(participant);
        return true;
    }

    @Override
    public void addSampleToSession(String customerId, String sampleId) {
        List<TrialParticipant> trialParticipants = fetchTrialParticipants(customerId, ParticipantStatus.APPROVED);
        Validate.isTrue(trialParticipants.size() == 1, "customer must be active in one trial");
        TrialParticipant trialParticipant = trialParticipants.iterator().next();
        List<TrialSession> trialSessions = ofy().load().type(TrialSession.class)
                .ancestor(entityRepository.getParent(TrialSession.class))
                .filter("trialParticipant =", Key.create(TrialParticipant.class, trialParticipant.getId()))
                .filter("status =", TrialSessionStatus.ACTIVE)
                .list();
        if (trialSessions.isEmpty()) {

            String sessionStatusChangeToken = ofy().transact(() -> {
                TrialSession trialSession = new TrialSession();
                trialSession.setParent(entityRepository.getParent(TrialSession.class));
                trialSession.setId(idGenerationStrategy.generate(null));
                trialSession.setCompensated(false);
                trialSession.setStatus(TrialSessionStatus.ACTIVE);
                trialSession.setCreateTime(new Date());
                trialSession.setCreateUserId(customerId);
                trialSession.setTrialParticipantId(trialParticipant.getId());
                trialSession.getSampleIds().add(sampleId);
                entityRepository.save(trialSession);

                SessionStatusChangeToken ssct = new SessionStatusChangeToken();
                ssct.setParent(entityRepository.getParent(SessionStatusChangeToken.class));
                ssct.setId(idGenerationStrategy.generate(null));
                ssct.setToken(tokenGenerator.generateToken());
                ssct.setSessionId(trialSession.getId());
                entityRepository.save(ssct);
                return ssct.getToken();
            });
            logger.info("not found session for customerId: {}, participant: {}. Created new session, sampleId: {}," +
                            " sessionStatusChangeToken: {}.", customerId, trialParticipant.getId(), sampleId, sessionStatusChangeToken);

            broadcastMessagePublisher.publishMessage(new SessionCreated(customerId, sampleId,
                    trialParticipant.getNurseId(), sessionStatusChangeToken));
        } else {
            TrialSession trialSession = trialSessions.iterator().next();
            trialSession.getSampleIds().add(sampleId);
            entityRepository.save(trialSession);
        }
        logger.info("success: customerId: {}, sampleId: {}, participant: {}", customerId, sampleId, trialParticipant.getId());
    }

    @Override
    public boolean sendKits(String participantId) {
        logger.info("send kits called");
        TrialParticipant participant = findTrialParticipantById(participantId);
        if (participant == null) {
            logger.warn("no participant with id {}", participantId);
            return false;
        }
        participant.setHasKits(true);
        participant.setSentKitsTime(new Date());
        entityRepository.save(participant);
        logger.info("participant {} kit fields updated", participantId);
        return true;
    }
}
