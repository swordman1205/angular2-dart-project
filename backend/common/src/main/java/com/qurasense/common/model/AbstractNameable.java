package com.qurasense.common.model;

import com.qurasense.common.stereotypes.Nameable;

public abstract class AbstractNameable extends AbstractIdentifiable implements Nameable {

    private String name;

    public AbstractNameable() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
