package com.qurasense.communication.swu;

public class Recipient {

    private String name;
    private String address;

    public Recipient(String address) {
        this.address = address;
    }

    public Recipient(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
