package com.qurasense.common.datastore;

import java.util.UUID;

import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

public class UUIDIdGenerationStrategy implements IdGenerationStrategy {
    @Override
    public Key generateKey(KeyFactory keyFactory) {
        String string = UUID.randomUUID().toString();
        return keyFactory.newKey(string);
    }

    @Override
    public String generate(KeyFactory keyFactory) {
        return generate();
    }

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
