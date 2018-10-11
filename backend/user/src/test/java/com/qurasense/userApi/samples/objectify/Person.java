package com.qurasense.userApi.samples.objectify;

import com.googlecode.objectify.annotation.AlsoLoad;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Person {
    @Id
    private Long id;
    @AlsoLoad("name")
    private String fullName;

    public Person() {
    }

    public Person(String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }
    protected void setId(Long id) {
        this.id = id;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", fullName='" + fullName + '\'' + '}';
    }
}

