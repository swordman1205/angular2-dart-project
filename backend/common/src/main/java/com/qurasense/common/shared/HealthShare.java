package com.qurasense.common.shared;

import java.util.Date;
import java.util.List;

public class HealthShare {

    private Boolean menstruate;
    private Date firstDateOfLastPeriod;
    private Long averagePeriodLength;
    private Long typicalCycleLength;
    private String sexualActivity;
    private List<String> birthControls;
    private Long weight;
    private String weightUnit;
    private Long height;
    private String heightUnit;

    public Boolean getMenstruate() {
        return menstruate;
    }

    public void setMenstruate(Boolean menstruate) {
        this.menstruate = menstruate;
    }

    public Date getFirstDateOfLastPeriod() {
        return firstDateOfLastPeriod;
    }

    public void setFirstDateOfLastPeriod(Date firstDateOfLastPeriod) {
        this.firstDateOfLastPeriod = firstDateOfLastPeriod;
    }

    public Long getAveragePeriodLength() {
        return averagePeriodLength;
    }

    public void setAveragePeriodLength(Long averagePeriodLength) {
        this.averagePeriodLength = averagePeriodLength;
    }

    public Long getTypicalCycleLength() {
        return typicalCycleLength;
    }

    public void setTypicalCycleLength(Long typicalCycleLength) {
        this.typicalCycleLength = typicalCycleLength;
    }

    public String getSexualActivity() {
        return sexualActivity;
    }

    public void setSexualActivity(String sexualActivity) {
        this.sexualActivity = sexualActivity;
    }

    public List<String> getBirthControls() {
        return birthControls;
    }

    public void setBirthControls(List<String> birthControls) {
        this.birthControls = birthControls;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public String getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit;
    }
}
