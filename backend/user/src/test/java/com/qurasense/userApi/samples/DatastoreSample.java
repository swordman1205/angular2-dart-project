package com.qurasense.userApi.samples;


import com.google.cloud.NoCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public class DatastoreSample {
    // https://stackoverflow.com/questions/38577163/how-to-connect-to-a-running-bigtable-emulator-from-java
    // https://github.com/GoogleCloudPlatform/google-cloud-java/blob/master/TESTING.md#testing-code-that-uses-datastore
    public static void main(String... args) throws Exception {
        // Instantiates a client
        DatastoreOptions options = DatastoreOptions.newBuilder()
                .setProjectId(DatastoreOptions.getDefaultProjectId())
                .setHost("localhost:8380")
                .setCredentials(NoCredentials.getInstance())
                .setRetrySettings(ServiceOptions.getNoRetrySettings())
                .build();
        Datastore datastore = options.getService();

        // The kind for the new entity
        String kind = "Task";
        // The name/ID for the new entity
        String name = "sampletask1";
        // The Cloud Datastore key for the new entity
        Key taskKey = datastore.newKeyFactory().setKind(kind).newKey(name);

        // Prepares the new entity
        Entity task = Entity.newBuilder(taskKey)
                .set("description", "Buy milk")
                .build();

        // Saves the entity
        datastore.put(task);

        System.out.printf("Saved %s: %s%n", task.getKey().getName(), task.getString("description"));

        //Retrieve entity
        Entity retrieved = datastore.get(taskKey);

        System.out.printf("Retrieved %s: %s%n", taskKey.getName(), retrieved.getString("description"));

    }
}
