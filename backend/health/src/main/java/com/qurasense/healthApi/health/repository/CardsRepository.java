package com.qurasense.healthApi.health.repository;


import java.util.List;

import com.qurasense.healthApi.health.model.HealthCard;

public interface CardsRepository {
    List<HealthCard> getCards(String userId);
}
