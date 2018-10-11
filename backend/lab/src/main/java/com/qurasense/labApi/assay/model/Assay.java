package com.qurasense.labApi.assay.model;

import com.googlecode.objectify.annotation.Entity;
import com.qurasense.common.model.AbstractNameable;

@Entity
public class Assay extends AbstractNameable {
    private AssayUnit unit;
    private double rangeMin;
    private double rangeMax;

    public Assay() {
    }

    public Assay(String id, String name, AssayUnit unit, double rangeMin, double rangeMax) {
        setId(id);
        setName(name);
        this.unit = unit;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    public AssayUnit getUnit() {
        return unit;
    }

    public void setUnit(AssayUnit unit) {
        this.unit = unit;
    }

    public double getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(double rangeMin) {
        this.rangeMin = rangeMin;
    }

    public double getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(double rangeMax) {
        this.rangeMax = rangeMax;
    }
}
