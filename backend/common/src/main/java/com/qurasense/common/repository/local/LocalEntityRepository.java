package com.qurasense.common.repository.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.cloud.datastore.Key;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.repository.EntityValidator;
import com.qurasense.common.stereotypes.Identifiable;
import com.qurasense.common.stereotypes.Nameable;

public class LocalEntityRepository implements EntityRepository {
    private Map<Class<?>, List<?>> valuesMap = new HashMap<>();

    private  <T> List<T> getValues(Class<? extends T> aClass) {
        return (List<T>) valuesMap.computeIfAbsent(aClass, (c) -> new ArrayList<T>());
    }

    @Override
    public <T extends Identifiable> int deleteAll(Class<T> aClass) {
        List<? extends Identifiable> values = getValues(aClass);
        int result = values.size();
        values.clear();
        return result;
    }

    @Override
    public <T extends Identifiable> String save(T aEntity) {
        EntityValidator.validateOnEntitySave(aEntity);
        getValues((Class<T>) aEntity.getClass()).add(aEntity);
        return aEntity.getId();
    }

    @Override
    public <T extends Identifiable> List<T> findAll(Class<T> aClass) {
        return getValues(aClass);
    }

    @Override
    public <T extends Identifiable> T findById(Class<T> aClass, String aId) {
        return getValues(aClass).stream().filter(e -> Objects.equals(e.getId(), aId)).findFirst().orElseGet(null);
    }

    @Override
    public <T extends Identifiable> List<T> fetchByIds(Class<T> aClass, List<String> ids) {
        return getValues(aClass).stream().filter(e -> ids.contains(e.getId())).collect(Collectors.toList());
    }

    public <T extends Identifiable> T findByIdWithAncestor(Class<T> aClass, String aId) {
        return findById(aClass, aId);
    }

    @Override
    public <T extends Identifiable> List<T> fetchByIdsWithAncestor(Class<T> aClass, List<String> ids) {
        return fetchByIds(aClass, ids);
    }

    @Override
    public <T extends Identifiable> boolean deleteById(Class<T> aClass, String aId) {
        return getValues(aClass).removeIf(e -> Objects.equals(e.getId(), aId));
    }

    @Override
    public <T extends Identifiable> void delete(T aEntity) {
        deleteById(aEntity.getClass(), aEntity.getId());
    }

    @Override
    public <T extends Nameable> T findByName(Class<T> aClass, String name) {
        return getValues(aClass).stream()
                .filter(e -> Objects.equals(((Nameable)e).getName(), name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Key getParent(Class aClass) {
        throw new UnsupportedOperationException("not implemented");
    }
}
