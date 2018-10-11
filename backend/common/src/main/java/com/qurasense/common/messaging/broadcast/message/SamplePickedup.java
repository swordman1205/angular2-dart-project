package com.qurasense.common.messaging.broadcast.message;

public class SamplePickedup implements BroadcastMessage {

    private Long userId;
    private Long sampleId;
    private Long nurseId;

    public SamplePickedup() {
    }

    public SamplePickedup(Long userId, Long sampleId, Long nurseId) {
        this.userId = userId;
        this.sampleId = sampleId;
        this.nurseId = nurseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public Long getNurseId() {
        return nurseId;
    }

    public void setNurseId(Long nurseId) {
        this.nurseId = nurseId;
    }
}
