package com.qurasense.userApi.repository;

import java.util.Collection;
import java.util.List;

import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.CustomerStatusType;

public interface CustomerRepository {

    String saveCustomerInfo(CustomerInfo aCustomerInfo);

    int deleteAll();

    CustomerInfo getCustomerInfo(String userId);

    List<CustomerInfo> getCustomers();

    void updateCustomerStatus(String customerId, CustomerStatusType customerStatus);

    Collection<CustomerInfo> fetchCustomers(List<String> customerIds);

    Collection<CustomerInfo> fetchCustomersByUserIds(List<String> customerUserIds);
}
