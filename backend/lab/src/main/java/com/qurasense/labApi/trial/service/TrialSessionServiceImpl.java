package com.qurasense.labApi.trial.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.googlecode.objectify.Key;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.labApi.trial.model.SessionStatusChangeToken;
import com.qurasense.labApi.trial.model.Trial;
import com.qurasense.labApi.trial.model.TrialParticipant;
import com.qurasense.labApi.trial.model.TrialSession;
import com.qurasense.labApi.trial.model.TrialSessionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class TrialSessionServiceImpl implements TrialSessionService {

    private Logger logger = LoggerFactory.getLogger(TrialSessionServiceImpl.class);

    @Autowired
    private EntityRepository entityRepository;

    @Override
    public TrialSession find(String id) {
        return ofy().load().type(TrialSession.class)
                .parent(entityRepository.getParent(TrialSession.class))
                .id(id)
                .now();
    }

    @Override
    public List<TrialSession> fetchSessions(String trialId) {
        List<TrialParticipant> trialParticipants = ofy().load().type(TrialParticipant.class)
                .ancestor(entityRepository.getParent(TrialParticipant.class))
                .filter("trial =", Key.create(Trial.class, trialId))
                .list();

        return trialParticipants.stream().flatMap((tp) ->
                fetchSessionsByParticipant(tp.getId()).stream()
        ).collect(Collectors.toList());
    }

    @Override
    public List<TrialSession> fetchSessions(String trialId, String sessionStatus) {
        List<TrialParticipant> trialParticipants = ofy().load().type(TrialParticipant.class)
                .ancestor(entityRepository.getParent(TrialParticipant.class))
                .filter("trial =", Key.create(Trial.class, trialId))
                .list();

        return trialParticipants.stream().flatMap((tp) ->
                fetchSessionsByParticipant(tp.getId(), sessionStatus).stream()
        ).collect(Collectors.toList());
    }

    @Override
    public boolean updateSessions(String trialId, String sessionStatus) {
        TrialSessionStatus trialSessionStatus = TrialSessionStatus.valueOf(sessionStatus);
        List<TrialParticipant> trialParticipants = ofy().load().type(TrialParticipant.class)
                .ancestor(entityRepository.getParent(TrialParticipant.class))
                .filter("trial =", Key.create(Trial.class, trialId))
                .list();
        if (trialParticipants.isEmpty()) {
            return false;
        }
        trialParticipants.stream().flatMap((tp) ->
                fetchSessionsByParticipant(tp.getId()).stream()
        ).forEach((ts) -> {
            ts.setStatus(trialSessionStatus);
            entityRepository.save(ts);
        });
        return true;
    }

    @Override
    public List<TrialSession> fetchSessionsByParticipant(String participantId) {
        return ofy().load().type(TrialSession.class)
                .ancestor(entityRepository.getParent(TrialSession.class))
                .filter("trialParticipant =", Key.create(TrialParticipant.class, participantId))
                .list();
    }

    @Override
    public List<TrialSession> fetchSessionsByParticipant(String participantId, String sessionStatus) {
        TrialSessionStatus trialSessionStatus = TrialSessionStatus.valueOf(sessionStatus);
        return ofy().load().type(TrialSession.class)
                .ancestor(entityRepository.getParent(TrialSession.class))
                .filter("trialParticipant =", Key.create(TrialParticipant.class, participantId))
                .filter("status =", trialSessionStatus)
                .list();
    }

    @Override
    public boolean updateSessionByToken(String token, String sessionStatus) {
        TrialSessionStatus trialSessionStatus = TrialSessionStatus.valueOf(sessionStatus);

        List<SessionStatusChangeToken> sessionStatusChangeTokens = ofy().load().type(SessionStatusChangeToken.class)
                .ancestor(entityRepository.getParent(SessionStatusChangeToken.class))
                .filter("token =", token)
                .list();

        Optional<SessionStatusChangeToken> tokenOptional = sessionStatusChangeTokens.stream()
                .filter(t -> Objects.isNull(t.getFinishTime())).findFirst();
        if (!tokenOptional.isPresent()) {
            logger.warn("not found not finished token {}. Don`t update any session", token);
            return false;
        }
        SessionStatusChangeToken tokenEntity = tokenOptional.get();

        TrialSession session = entityRepository.findByIdWithAncestor(TrialSession.class, tokenEntity.getSessionId());
        session.setStatus(trialSessionStatus);
        tokenEntity.setFinishTime(new Date());
        ofy().transact(() -> {
            entityRepository.save(session);
            entityRepository.save(tokenEntity);
        });
        return true;
    }

}
