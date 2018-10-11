package com.qurasense.gateway.version;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("version")
public class VersionProperties {
    private String hash;
    private String buildTime;
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    @Override
    public String toString() {
        return "VersionProperties{" +
                "hash='" + hash + '\'' +
                ", buildTime='" + buildTime + '\'' +
                '}';
    }
}
