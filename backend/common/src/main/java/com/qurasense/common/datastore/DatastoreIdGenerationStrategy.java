package com.qurasense.common.datastore;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

public class DatastoreIdGenerationStrategy implements IdGenerationStrategy {

    private final Datastore datastore;

    public DatastoreIdGenerationStrategy( Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public Key generateKey(KeyFactory keyFactory) {
        return datastore.allocateId(keyFactory.newKey());
    }

    @Override
    public String generate(KeyFactory keyFactory) {
        Key key = datastore.allocateId(keyFactory.newKey());
        return key.getName();
    }
}
