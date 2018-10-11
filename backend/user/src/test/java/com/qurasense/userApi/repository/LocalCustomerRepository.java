package com.qurasense.userApi.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.qurasense.common.repository.local.LocalCustomRepository;
import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.CustomerStatus;
import com.qurasense.userApi.model.CustomerStatusType;
import org.springframework.stereotype.Repository;

@Repository
public class LocalCustomerRepository extends LocalCustomRepository<CustomerInfo> implements CustomerRepository {

    @Override
    protected Class<CustomerInfo> getEntityClass() {
        return CustomerInfo.class;
    }

    @Override
    public String saveCustomerInfo(CustomerInfo aCustomerInfo) {
        aCustomerInfo.setId(genereateId());
        getValues().add(aCustomerInfo);
        return aCustomerInfo.getId();
    }

    @Override
    public CustomerInfo getCustomerInfo(String userId) {
        return Iterables.find(getValues(), (c) -> Objects.equals(c.getUserId(), userId), null);
    }

    @Override
    public List<CustomerInfo> getCustomers() {
        return getValues();
    }

    @Override
    public void updateCustomerStatus(String customerId, CustomerStatusType customerStatusType) {
        CustomerInfo customerInfo = findById(customerId);
        if (Objects.nonNull(customerInfo.getCustomerStatus())) {
            customerInfo.getCustomerStatusHistory().add(customerInfo.getCustomerStatus());
        }
        customerInfo.setCustomerStatus(new CustomerStatus(customerStatusType, new Date()));

    }

    @Override
    public Collection<CustomerInfo> fetchCustomers(List<String> customerIds) {
        return getValues().stream().filter((c)->customerIds.contains(c.getId())).collect(Collectors.toList());
    }

    @Override
    public Collection<CustomerInfo> fetchCustomersByUserIds(List<String> customerUserIds) {
        return null;
    }
}
