package com.qurasense.healthApi.health.repository;

import java.util.Date;
import javax.annotation.PostConstruct;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"emulator", "cloud"})
public class PeriodRepositoryImpl implements PeriodRepository {
    private static final String PERIOD_KIND = "Period";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Datastore datastore;

    private KeyFactory periodKeyFactory;

    @PostConstruct
    protected void init() {
        periodKeyFactory = datastore.newKeyFactory().setKind(PERIOD_KIND);
    }

    @Override
    public Date getLastPeriodDate(Long aUserId) {
        logger.info("get last period userid:{}", aUserId);
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(PERIOD_KIND)
                .setFilter(StructuredQuery.PropertyFilter.eq("userId", aUserId))
                .setOrderBy(StructuredQuery.OrderBy.desc("createTime"))
                .setLimit(1)
                .build();
        QueryResults<Entity> queryResults = datastore.run(query);
        if (queryResults.hasNext()) {
            Entity entity = queryResults.next();
            logger.info("finded entity id: {} , {}", entity.getKey().getId(), entity);
            return entity.getTimestamp("date").toSqlTimestamp();
        }
        throw new IllegalStateException("no period for user: " + aUserId);
    }

    @Override
    public Long savePeriod(Long userId, Date aDate, Date createTime) {
        FullEntity<IncompleteKey> fullEntity = FullEntity.newBuilder(periodKeyFactory.newKey())
                .set("userId", userId)
                .set("date", Timestamp.of(aDate))
                .set("createTime", Timestamp.of(createTime))
                .build();
        Entity entity = datastore.add(fullEntity);
        Long id = entity.getKey().getId();
        logger.info("persisted period, userid:{} date:{}, period id: {}", userId, aDate, id);
        return id;
    }
}
