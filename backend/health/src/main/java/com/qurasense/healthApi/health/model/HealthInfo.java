package com.qurasense.healthApi.health.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.qurasense.common.model.AbstractIdentifiable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import static com.googlecode.objectify.ObjectifyService.ofy;

@ApiModel(description = "user")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class HealthInfo extends AbstractIdentifiable {
    @ApiModelProperty(notes = "user id", required = true)
    @JsonIgnoreProperties
    @Index
    private String userId;
    private Boolean menstruate;
    @ApiModelProperty(notes = "user id", required = true)
    private Date firstDateOfLastPeriod;
    @ApiModelProperty(notes = "Average period length (choose days)", required = true)
    private Long averagePeriodLength;
    @ApiModelProperty(notes = "Average cycle length (choose days)", required = true)
    private Long typicalCycleLength;
    @ApiModelProperty(notes = "How long is your average cycle", required = true, hidden = true)
    private AverageCycleLength averageCycleLength;
    @ApiModelProperty(required = true)
    private SexualActivity sexualActivity;
    @ApiModelProperty(required = true)
    private List<BirthControl> birthControls = new ArrayList<>();
    @ApiModelProperty(notes = "weight (for unit=imperial use feet for unit=metric use cm)", required = true)
    //in datastore it always cm, converted at client side
    private Long weight;
    @ApiModelProperty(notes = "weight unit type (enum: metric/imperial, default=imperial)", required = true)
    private UnitType weightUnit;
    @ApiModelProperty(notes = "height (for unit=imperial use pounds for unit=metric use kg)", required = true)
    //in datastore it always kg, converted at client side
    private Long height;
    @ApiModelProperty(notes = "height unit type (enum: metric/imperial, default=imperial)", required = true)
    private UnitType heightUnit;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    @Deprecated
    /**
     * @deprecated use {@link #getTypicalCycleLength()}
     */
    public AverageCycleLength getAverageCycleLength() {
        return averageCycleLength;
    }

    public void setAverageCycleLength(AverageCycleLength averageCycleLength) {
        this.averageCycleLength = averageCycleLength;
    }

    public SexualActivity getSexualActivity() {
        return sexualActivity;
    }

    public void setSexualActivity(SexualActivity sexualActivity) {
        this.sexualActivity = sexualActivity;
    }

    public List<BirthControl> getBirthControls() {
        return birthControls;
    }

    public void setBirthControls(List<BirthControl> birthControls) {
        this.birthControls = birthControls;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public UnitType getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(UnitType weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public UnitType getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(UnitType heightUnit) {
        this.heightUnit = heightUnit;
    }

    @OnLoad
    void onLoad() {
        if (typicalCycleLength == null) {
            typicalCycleLength = convertAverageCycleLength();
            ofy().save().entity(this).now();
        }
    }

    private Long convertAverageCycleLength() {
        switch (averageCycleLength) {
            case SHORTEST:
                return 20l;
            case SHORT:
                return 23l;
            case MEDIUM:
                return 28l;
            case LONG:
                return 31l;
            case LONGEST:
                return 33l;
            case IRREGULAR:
                return 35l;
        }
        return null;
    }

}
