package com.qurasense.common;

import javax.annotation.PostConstruct;

public class SimpleMicroserviceRegistry {

    private String health;
    private String datastoreEmulator;
    private String pubsubEmulator;
    private String user;
    private String lab;

    @PostConstruct
    public void init() {
        if (Boolean.parseBoolean(System.getProperty("qurasense.docker", "false"))) {
            //docker means launched in docker compose
            user = "http://user:8082";
            health = "http://health:8081";
            lab = "http://lab:8083";
            datastoreEmulator = "http://datastore:8380";
            pubsubEmulator = "pubsub:8381";
        } else {
            user = "http://localhost:8082";
            health = "http://localhost:8081";
            lab = "http://localhost:8083";
            datastoreEmulator = "http://localhost:8380";
            pubsubEmulator = "localhost:8381";
        }
    }

    public String getHealthUrl() {
        return health;
    }

    public String getHealthBasedUrl(String path) {
        return String.format("%s%s", health, path);
    }

    public String getUserUrl() {
        return user;
    }

    public String getUserBasedUrl(String path) {
        return String.format("%s%s", user, path);
    }

    public String getLabUrl() {
        return lab;
    }

    public String getLabBasedUrl(String path) {
        return String.format("%s%s", lab, path);
    }

    public String getDatastoreEmulatorHost(){
        return datastoreEmulator;
    }

    public String getPubsubEmulatorHost() {
        return  pubsubEmulator;
    }

}
