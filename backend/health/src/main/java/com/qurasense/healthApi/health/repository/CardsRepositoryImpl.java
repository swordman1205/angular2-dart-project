package com.qurasense.healthApi.health.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.PathElement;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.qurasense.common.datastore.DataStoreUtils;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.healthApi.health.model.HealthCard;
import com.qurasense.healthApi.health.model.HealthCardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"emulator", "cloud"})
public class CardsRepositoryImpl implements CardsRepository {

    @Autowired
    private Datastore datastore;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    private static final String HEALTH_CARD_LIST = "HealthCardList";
    private static final String HEALTH_CARD_KIND = "HealthCard";
    private static final String DEFAULT_CARD_LIS = "default";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private KeyFactory healthCardKeyFactory;

    @PostConstruct
    protected void init() {
        healthCardKeyFactory = datastore.newKeyFactory()
                .addAncestors(PathElement.of(HEALTH_CARD_LIST, DEFAULT_CARD_LIS))
                .setKind(HEALTH_CARD_KIND);
    }

    @Override
    public List<HealthCard> getCards(String userId) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(HEALTH_CARD_KIND)
                .setFilter(StructuredQuery.PropertyFilter.hasAncestor(
                        datastore.newKeyFactory().setKind(HEALTH_CARD_LIST).newKey(DEFAULT_CARD_LIS)))
                .setFilter(StructuredQuery.PropertyFilter.eq("userId", userId))
                .build();
        List<HealthCard> result = new ArrayList<>();
        QueryResults<Entity> queryResults = datastore.run(query);
        while (queryResults.hasNext()) {
            result.add(transformUser(queryResults.next()));
        }
        return result;
    }

    private HealthCard transformUser(Entity entity) {
        return new HealthCard(entity.getKey().getName(),
                entity.getString("userId"),
                HealthCardType.valueOf(entity.getString("type")),
                DataStoreUtils.transformLocalDate(entity.getTimestamp("createDate")),
                entity.getString("note")
        );
    }

}
