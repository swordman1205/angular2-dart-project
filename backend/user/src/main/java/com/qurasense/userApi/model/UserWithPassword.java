package com.qurasense.userApi.model;

public class UserWithPassword extends User {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
