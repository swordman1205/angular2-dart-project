package com.qurasense.gateway.signup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SignupData {
    // user
    private String fullName;
    private String email;
    private String password;

    // customer info data
    private String phone;
    private Date birthDate;
    private String addressLine;
    private String city;
    private String state;
    private String zip;
    private String country;
    private List<String> contactTimes = new ArrayList<>();
    private String contactDay;

    // health info data
    private Boolean menstruate;
    private Date firstDateOfLastPeriod;
    private int averagePeriodLength = 1;
    private int typicalCycleLength;
    private String sexualActivity;
    private List<String> birthControl = new ArrayList<>();
    private String weightUnit;
    private String weight;
    private String heightUnit;
    private String height;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getContactTimes() {
        return contactTimes;
    }

    public void setContactTimes(List<String> contactTimes) {
        this.contactTimes = contactTimes;
    }

    public String getContactDay() {
        return contactDay;
    }

    public void setContactDay(String contactDay) {
        this.contactDay = contactDay;
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

    public int getAveragePeriodLength() {
        return averagePeriodLength;
    }

    public void setAveragePeriodLength(int averagePeriodLength) {
        this.averagePeriodLength = averagePeriodLength;
    }

    public int getTypicalCycleLength() {
        return typicalCycleLength;
    }

    public void setTypicalCycleLength(int typicalCycleLength) {
        this.typicalCycleLength = typicalCycleLength;
    }

    public String getSexualActivity() {
        return sexualActivity;
    }

    public void setSexualActivity(String sexualActivity) {
        this.sexualActivity = sexualActivity;
    }

    public List<String> getBirthControl() {
        return birthControl;
    }

    public void setBirthControl(List<String> birthControl) {
        this.birthControl = birthControl;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
