package com.qurasense.userApi.model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.cloud.datastore.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.Parent;
import com.qurasense.common.model.AbstractCreateStamp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "customerInfo")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class CustomerInfo extends AbstractCreateStamp {

    @Parent
    @ApiModelProperty(hidden = true)
    private Key customerInfoList;
    @Index
    @ApiModelProperty(notes = "user id", required = true)
    private String userId;
    @ApiModelProperty(notes = "assigned nurse id", required = true)
    private String nurseId;
    @ApiModelProperty(notes = "full customer name", required = true)
    private String fullName;
    @ApiModelProperty(notes = "customer email", required = true)
    private String email;
    @ApiModelProperty(notes = "customer phone", required = true)
    private String phone;
    @ApiModelProperty(notes = "date of birth", required = true)
    private LocalDate dateOfBirth;
    @ApiModelProperty(notes = "address line", required = true)
    private String addressLine;
    @ApiModelProperty(notes = "city", required = true)
    private String city;
    @ApiModelProperty(notes = "state", required = true)
    private String state;
    @ApiModelProperty(notes = "zip", required = true)
    private String zip;
    @ApiModelProperty(notes = "country", required = true)
    private String country;
    @ApiModelProperty(notes = "contact times", required = true)
    private List<ContactTime> contactTimes = new ArrayList<>();
    @ApiModelProperty(notes = "contact day", required = true)
    private ContactDay contactDay;
    @ApiModelProperty(notes = "customer status", required = true)
    private CustomerStatus customerStatus;
    @ApiModelProperty(notes = "old status collection", required = true)
    private List<CustomerStatus> customerStatusHistory = new ArrayList<>();
    @ApiModelProperty(notes = "email status", required = true)
    private ContactStatus emailStatus;

    public String getNurseId() {
        return nurseId;
    }

    public void setNurseId(String nurseId) {
        this.nurseId = nurseId;
    }

    public Key getCustomerInfoList() {
        return customerInfoList;
    }

    public void setCustomerInfoList(Key customerInfoList) {
        this.customerInfoList = customerInfoList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public List<ContactTime> getContactTimes() {
        return contactTimes;
    }

    public void setContactTimes(List<ContactTime> contactTimes) {
        this.contactTimes = contactTimes;
    }

    public ContactDay getContactDay() {
        return contactDay;
    }

    public void setContactDay(ContactDay contactDay) {
        this.contactDay = contactDay;
    }

    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(CustomerStatus customerStatus) {
        this.customerStatus = customerStatus;
    }

    public List<CustomerStatus> getCustomerStatusHistory() {
        return customerStatusHistory;
    }

    public void setCustomerStatusHistory(List<CustomerStatus> customerStatusHistory) {
        this.customerStatusHistory = customerStatusHistory;
    }

    @OnLoad
    void defaultEmailStatus() {
        if (Objects.isNull(emailStatus)) {
            emailStatus = ContactStatus.UNCONFIRMED;
        }
    }

    public ContactStatus getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(ContactStatus emailStatus) {
        this.emailStatus = emailStatus;
    }
}
