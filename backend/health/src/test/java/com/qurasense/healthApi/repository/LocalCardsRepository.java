package com.qurasense.healthApi.repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.qurasense.common.repository.local.LocalCustomRepository;
import com.qurasense.healthApi.health.model.HealthCard;
import com.qurasense.healthApi.health.repository.CardsRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LocalCardsRepository extends LocalCustomRepository<HealthCard> implements CardsRepository {

    @Override
    public List<HealthCard> getCards(String userId) {
        return getValues().stream().filter(c-> Objects.equals(c.getUserId(), userId)).collect(Collectors.toList());
    }

    @Override
    protected Class<HealthCard> getEntityClass() {
        return HealthCard.class;
    }
}
