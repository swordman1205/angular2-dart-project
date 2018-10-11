package com.qurasense.userApi.service;

import java.util.Map;

import com.qurasense.userApi.model.CustomerInfo;

public interface RegistrationService {

    boolean isUserEmailExists(String email);

    /**
     * @param aFullName
     * @param aEmail
     * @param aPassword
     * @param customerInfo
     * @return Map with created `customerId` and `userId`
     */
    Map<String, String> register(String aFullName, String aEmail, String aPassword, CustomerInfo customerInfo);

    void requestRestorePassword(String email);

    void restorePassword(String token, String aPassword);

    boolean confirmEmail(String token);
}
