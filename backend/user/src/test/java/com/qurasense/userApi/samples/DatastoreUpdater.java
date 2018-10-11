package com.qurasense.userApi.samples;


import com.google.cloud.NoCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.datastore.*;
import com.qurasense.common.datastore.DataStoreUtils;

import java.util.Date;

public class DatastoreUpdater {

    public static void doUpdate(Datastore datastore) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("User")
                .setFilter(StructuredQuery.PropertyFilter.hasAncestor(
                        datastore.newKeyFactory().setKind("UserList").newKey("default")))
                .setFilter(StructuredQuery.PropertyFilter.eq("email", "s@s.com"))
                .build();
        QueryResults<Entity> queryResults = datastore.run(query);
        Entity entity = queryResults.next();
        Key key = entity.getKey();
        Entity updated = Entity.newBuilder(key)
                .set("createTime", DataStoreUtils.getValue(new Date()))
                .build();
        datastore.put(updated);
        System.out.println("successfully updated");
    }

    public static void main(String... args) throws Exception {
        // Instantiates a client
        DatastoreOptions options = DatastoreOptions.newBuilder()
                .setProjectId(DatastoreOptions.getDefaultProjectId())
                .setHost("localhost:8380")
                .setCredentials(NoCredentials.getInstance())
                .setRetrySettings(ServiceOptions.getNoRetrySettings())
                .build();
        doUpdate(options.getService());
    }
}
