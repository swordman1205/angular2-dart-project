package com.qurasense.userApi.samples;


import com.google.cloud.NoCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.datastore.*;

public class DatastoreTest {

    public static void doTest(Datastore datastore) {
        KeyFactory sampleKeyFactory = datastore.newKeyFactory().setKind("test");
        Key key = datastore.allocateId(sampleKeyFactory.newKey());
        System.out.println(key.getId());

        Entity entity = Entity.newBuilder(key).set("field1", "value1").build();
        datastore.put(entity);
        Entity loaded1 = loadEntity(datastore, key);
        System.out.println("1:");
        System.out.println(loaded1.contains("field1"));

        Entity entity2 = Entity.newBuilder(loaded1).set("field2", "value2").build();
        datastore.update(entity2);

        Entity loaded2 = loadEntity(datastore, key);
        System.out.println("2:");
        System.out.println(loaded2.contains("field1"));
        System.out.println(loaded2.contains("field2"));
    }

    private static Entity loadEntity(Datastore datastore, Key key) {
        return datastore.get(key);
    }

    public static void main(String... args) throws Exception {
        // Instantiates a client
        DatastoreOptions options = DatastoreOptions.newBuilder()
                .setProjectId(DatastoreOptions.getDefaultProjectId())
                .setHost("localhost:8380")
                .setCredentials(NoCredentials.getInstance())
                .setRetrySettings(ServiceOptions.getNoRetrySettings())
                .build();
        doTest(options.getService());
    }
}
