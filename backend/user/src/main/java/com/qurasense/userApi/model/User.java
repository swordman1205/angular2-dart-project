package com.qurasense.userApi.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.cloud.datastore.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.annotation.Parent;
import com.qurasense.common.model.AbstractIdentifiable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Entity
@ApiModel(description = "user")
@JsonIgnoreProperties(ignoreUnknown = true, value = "encryptedPassword")
public class User extends AbstractIdentifiable {

    @Parent
    @ApiModelProperty(hidden = true)
    private Key userList;

    @ApiModelProperty(notes = "fullname", required = true)
    private String fullName;
    @Index
    @ApiModelProperty(notes = "email", required = true)
    private String email;
    @Index
    @ApiModelProperty(notes = "role", required = true)
    private UserRole role;
    @ApiModelProperty(notes = "createUser time", required = true)
    private Date createTime;
    @ApiModelProperty(notes = "last login time", required = false)
    private Date lastLoginTime;
    //password is ignored by jackson
    @ApiModelProperty(hidden = true)
    private String encryptedPassword;
    @ApiModelProperty(notes = "can user login", required = true)
    private Boolean active;

    public User() {
    }

    public User(String fullName, String email, UserRole role) {
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public User(String fullName, String email, String id, UserRole role, Date createTime) {
        this.fullName = fullName;
        this.email = email;
        setId(id);
        this.role = role;
        this.createTime = createTime;
    }

    public User(String fullName, String email, String id, UserRole role, String aEncryptedPassword, Date createTime,
                Date lastLoginTime) {
        this.fullName = fullName;
        this.email = email;
        this.createTime = createTime;
        this.lastLoginTime = lastLoginTime;
        setId(id);
        this.role = role;
        this.encryptedPassword = aEncryptedPassword;
    }

    public Key getUserList() {
        return userList;
    }

    public void setUserList(Key userList) {
        this.userList = userList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OnSave
    void emailLowerCase() {
        this.email = email.toLowerCase();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @OnLoad
    void onLoad() {
        if (active == null) {
            if (role == UserRole.CUSTOMER) {
                active = false;
            } else {
                active = true;
            }
            ofy().save().entity(this).now();
        }
    }
}
