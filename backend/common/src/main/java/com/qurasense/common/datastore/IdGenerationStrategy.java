package com.qurasense.common.datastore;

import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

public interface IdGenerationStrategy {

    Key generateKey(KeyFactory keyFactory);

    String generate(KeyFactory keyFactory);
}
