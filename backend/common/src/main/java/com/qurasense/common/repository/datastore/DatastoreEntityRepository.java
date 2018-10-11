package com.qurasense.common.repository.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.cloud.datastore.Datastore;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.QueryKeys;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.repository.EntityValidator;
import com.qurasense.common.stereotypes.Identifiable;
import com.qurasense.common.stereotypes.Nameable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class DatastoreEntityRepository implements EntityRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Datastore datastore;

    protected LoadingCache<Class, com.google.cloud.datastore.Key> parentKeyCache = CacheBuilder.newBuilder()
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build(new CacheLoader<Class, com.google.cloud.datastore.Key>() {
                @Override
                public com.google.cloud.datastore.Key load(Class key) {
                    String parentName = String.format("%sParent", key.getSimpleName());
                    return datastore.newKeyFactory().setKind(parentName).newKey("default");
                }
            });;

    @Override
    public <T extends Identifiable> int deleteAll(Class<T> aClass) {
        QueryKeys<T> userKeys = ofy().load().type(aClass).keys();
        List<Key<T>> keyList = userKeys.list();
        logger.info("Deleting {} {}.", keyList.size(), aClass.getSimpleName());
        ofy().delete().keys(keyList).now();
        return keyList.size();
    }

    @Override
    public <T extends Identifiable> String save(T aEntity) {
        EntityValidator.validateOnEntitySave(aEntity);
        Key<T> now = ofy().save().entity(aEntity).now();
        logger.info("entity {} was saved, id: {}", aEntity.getClass(), now.getName());
        return now.getName();
    }

    @Override
    public <T extends Identifiable> T findById(Class<T> aClass, String aId) {
        T loaded = ofy().load().type(aClass).id(aId).now();
        logger.info("entity {} was loaded, id: {}", aClass, aId);
        return loaded;
    }

    @Override
    public <T extends Identifiable> List<T> fetchByIds(Class<T> aClass, List<String> ids) {
        Map<String, T> map = ofy().load().type(aClass).ids(ids);
        logger.info("entity {} was loaded, ids: {}", aClass, ids);
        return new ArrayList<>(map.values());
    }

    @Override
    public <T extends Identifiable> T findByIdWithAncestor(Class<T> aClass, String aId) {
        T loaded = ofy().load().type(aClass).parent(getParent(aClass)).id(aId).now();
        logger.info("entity {} was loaded, id: {}", aClass, aId);
        return loaded;
    }

    @Override
    public <T extends Identifiable> List<T> fetchByIdsWithAncestor(Class<T> aClass, List<String> ids) {
        Map<String, T> map = ofy().load().type(aClass).parent(getParent(aClass)).ids(ids);
        logger.info("entity {} was loaded, ids: {}", aClass, ids);
        return new ArrayList<>(map.values());
    }

    @Override
    public <T extends Identifiable> boolean deleteById(Class<T> entityClass, String aId) {
        ofy().delete().type(entityClass).id(aId).now();
        logger.info("entity {} was deleted, id: {}", entityClass, aId);
        return false;
    }

    @Override
    public <T extends Identifiable> void delete(T aEntity) {
        ofy().delete().entity(aEntity).now();
        logger.info("entity {} was deleted, id: {}", aEntity.getClass(), aEntity.getId());
    }

    @Override
    public com.google.cloud.datastore.Key getParent(Class aClass) {
        return parentKeyCache.getUnchecked(aClass);
    }

    @Override
    public <T extends Identifiable> List<T> findAll(Class<T> aClass) {
        return ofy().load().type(aClass).list();
    }

    @Override
    public <T extends Nameable> T findByName(Class<T> aClass, String name) {
        return ofy().load().type(aClass)
                .filter("name =", name)
                .first()
                .now();
    }
}
