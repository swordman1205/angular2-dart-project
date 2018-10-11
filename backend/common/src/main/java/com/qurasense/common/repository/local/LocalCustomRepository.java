package com.qurasense.common.repository.local;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Iterables;
import com.qurasense.common.datastore.UUIDIdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.repository.EntityValidator;
import com.qurasense.common.stereotypes.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class can be used as parent for create test custom repositories. Everwhere where possible should be used EntityRepository.
 * @param <T> custom repository entity
 */
public abstract class LocalCustomRepository<T extends Identifiable> {

    @Autowired
    private EntityRepository entityRepository;

    private UUIDIdGenerationStrategy idGenerationStrategy = new UUIDIdGenerationStrategy();

    protected abstract Class<T> getEntityClass();

    protected List<T> getValues() {
        return entityRepository.findAll(getEntityClass());
    }

    protected String genereateId() {
        return idGenerationStrategy.generate();
    }

    public List<T> findAll() {
        return getValues();
    }

    public int deleteAll() {
        return entityRepository.deleteAll(getEntityClass());
    }

    public T findById(String aId) {
        return Iterables.find(getValues(), (c) -> Objects.equals(c.getId(), aId));
    }

    public boolean deleteById(String aId) {
        return entityRepository.deleteById(getEntityClass(), aId);
    }

    public String save(T aEntity) {
        EntityValidator.validateOnEntitySave(aEntity);
        getValues().removeIf(e->Objects.equals(aEntity.getId(), e.getId()));
        getValues().add(aEntity);
        return aEntity.getId();
    }

}
