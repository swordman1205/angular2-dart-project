package com.qurasense.common.repository.datastore;

import com.googlecode.objectify.Key;
import com.qurasense.common.model.AbstractIdentifiable;
import com.qurasense.common.repository.EntityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class DatastoreCustomRepository <E extends AbstractIdentifiable> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected void save(E aEntity) {
        EntityValidator.validateOnEntitySave(aEntity);
        Key<E> now = ofy().save().entity(aEntity).now();
        logger.info("entity {} was saved, id: {}", aEntity.getClass(), now.getName());
    }

}
