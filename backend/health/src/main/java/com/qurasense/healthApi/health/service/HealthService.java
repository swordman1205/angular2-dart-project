package com.qurasense.healthApi.health.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.qurasense.common.MessagesRetriever;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.shared.HealthShare;
import com.qurasense.healthApi.health.model.HealthCard;
import com.qurasense.healthApi.health.model.HealthInfo;
import com.qurasense.healthApi.health.repository.CardsRepository;
import com.qurasense.healthApi.health.repository.HealthInfoRepository;
import com.qurasense.healthApi.health.repository.PeriodRepository;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class HealthService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PeriodRepository healthRepository;

    @Autowired
    private HealthInfoRepository healthInfoRepository;

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private MessagesRetriever messagesRetriever;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @PreAuthorize("hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and isCurrentUser(#userId))")
    public void savePeriod(Long userId, Date aDate) {
        healthRepository.savePeriod(userId, aDate, new Date());
    }

    @PreAuthorize("hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and isCurrentUser(#userId))")
    public Date getLastPeriod(Long userId) {
        return healthRepository.getLastPeriodDate(userId);
    }

    @PreAuthorize("hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and isCurrentUser(#userId))")
    public HealthInfo findHealth(String userId) {
        return healthInfoRepository.findHealth(userId);
    }

    @PreAuthorize("hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and isCurrentUser(#aHealth.userId))")
    public void saveHealth(HealthInfo aHealth) {
        entityRepository.save(aHealth);
    }

    @PreAuthorize("hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and isCurrentUser(#userId))")
    public List<HealthCard> getCards(String userId) {
        return cardsRepository.getCards(userId);
    }

    @PreAuthorize("hasAuthority('MEDICAL')")
    public String saveHealthCard(HealthCard healthCard) {
        List<HealthCard> cards = getCards(healthCard.getUserId());
        Validate.isTrue(!cards.stream().anyMatch(c->Objects.equals(c.getType(), healthCard.getType())),
                "Card with type: %s already exist for userId %s", healthCard.getType(), healthCard.getUserId());
        return entityRepository.save(healthCard);
    }

    public HealthShare findHealthShare(String healthId) {
        HealthInfo healthInfo = ofy().load().type(HealthInfo.class).id(healthId).now();
        HealthShare hs = new HealthShare();
        hs.setTypicalCycleLength(healthInfo.getTypicalCycleLength());
        hs.setAveragePeriodLength(healthInfo.getAveragePeriodLength());
        List<String> bc = healthInfo.getBirthControls()
                .stream()
                .map((b) -> messagesRetriever.getCaption(b))
                .collect(Collectors.toList());
        hs.setBirthControls(bc);
        hs.setFirstDateOfLastPeriod(healthInfo.getFirstDateOfLastPeriod());
        hs.setHeight(healthInfo.getHeight());
        hs.setHeightUnit(messagesRetriever.getCaption(healthInfo.getHeightUnit()));
        hs.setMenstruate(healthInfo.getMenstruate());
        hs.setSexualActivity(messagesRetriever.getCaption(healthInfo.getSexualActivity()));
        hs.setWeight(healthInfo.getWeight());
        hs.setWeightUnit(messagesRetriever.getCaption(healthInfo.getWeightUnit()));
        return hs;
    }
}
