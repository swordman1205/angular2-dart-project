package com.qurasense.userApi.samples;


import com.google.cloud.NoCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.datastore.*;

public class DatastoreCleaner {

    public static void doClean(Datastore datastore) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("User")
                .build();
        QueryResults<Entity> queryResults = datastore.run(query);
        while(queryResults.hasNext()) {
            Entity next = queryResults.next();
            if (next.contains("fullName")
                    && next.contains("email")
                    && next.contains("role")
                    && next.contains("encryptedPassword")
                    && next.contains("createTime")
                    && next.contains("lastLoginTime")) {
                System.out.println("email is ok:" + next.getString("email"));
            } else {
                System.out.println("id is not full - clean:" + next.toString());
                datastore.delete(next.getKey());
            }
        }
        System.out.println("finish clean");
    }

    public static void main(String... args) throws Exception {
        // Instantiates a client
        DatastoreOptions options = DatastoreOptions.newBuilder()
                .setProjectId(DatastoreOptions.getDefaultProjectId())
                .setHost("localhost:8380")
                .setCredentials(NoCredentials.getInstance())
                .setRetrySettings(ServiceOptions.getNoRetrySettings())
                .build();
        doClean(options.getService());
    }
}
