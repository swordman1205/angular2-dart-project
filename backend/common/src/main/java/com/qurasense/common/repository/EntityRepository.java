package com.qurasense.common.repository;

import java.util.List;

import com.qurasense.common.stereotypes.Identifiable;
import com.qurasense.common.stereotypes.Nameable;

public interface EntityRepository {
    <T extends Identifiable> int deleteAll(Class<T> aClass);

    <T extends Identifiable> String save(T aEntity);

    /**
     *
     * @param aClass
     * @param <T>
     * @return
     */
    <T extends Identifiable> List<T> findAll(Class<T> aClass);

    <T extends Identifiable> T findById(Class<T> aClass, String aId);

    <T extends Identifiable> List<T> fetchByIds(Class<T> aClass, List<String> ids);

    <T extends Identifiable> T findByIdWithAncestor(Class<T> aClass, String aId);

    <T extends Identifiable> List<T> fetchByIdsWithAncestor(Class<T> aClass, List<String> ids);

    <T extends Identifiable> boolean deleteById(Class<T> entityClass, String aId);

    <T extends Identifiable> void delete(T aEntity);

    <T extends Nameable> T findByName(Class<T> aClass, String name);

    com.google.cloud.datastore.Key getParent(Class aClass);

}
