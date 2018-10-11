package com.qurasense.userApi.service;

import java.util.Collection;
import java.util.List;

import com.qurasense.common.shared.CustomerShare;
import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.CustomerStatusType;
import com.qurasense.userApi.model.User;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CustomerService {

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and principal.username == #userId)")
    CustomerInfo getCustomerInfo(String userId);

    @PreAuthorize("hasAuthority('CUSTOMER') and principal.username == #aUser.id")
    void saveCustomerInfo(User aUser, CustomerInfo customerInfo);

    /**
     * internal create user and customer info in transaction
     * @param aUser customer user
     * @return customer info
     */
    CustomerInfo internalCreateCustomer(User aUser);

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MEDICAL')")
    void updateCustomerStatus(String customerId, CustomerStatusType customerStatusType);

    @PreAuthorize("hasAuthority('NURSE') or hasAuthority('MEDICAL')")
    Collection<CustomerInfo> fetchCustomers(List<String> customerIds);

    @PreAuthorize("hasAuthority('NURSE') or hasAuthority('MEDICAL')")
    Collection<CustomerInfo> fetchCustomersByUserIds(List<String> customerUserIds);

    CustomerShare getCustomerShare(String userId);

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MEDICAL')")
    List<CustomerInfo> getCustomers();


}
