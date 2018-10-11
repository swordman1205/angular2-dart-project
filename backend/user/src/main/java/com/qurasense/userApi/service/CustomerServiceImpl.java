package com.qurasense.userApi.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Transaction;
import com.qurasense.common.MessagesRetriever;
import com.qurasense.common.shared.CustomerShare;
import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.CustomerStatusType;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.repository.CustomerRepository;
import com.qurasense.userApi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Datastore datastore;

    @Autowired
    private MessagesRetriever messagesRetriever;

    @Override
    public CustomerInfo getCustomerInfo(String userId) {
        return customerRepository.getCustomerInfo(userId);
    }

    @Override
    public void saveCustomerInfo(User aUser, CustomerInfo customerInfo) {
        customerInfo.setUserId(aUser.getId());
        customerRepository.saveCustomerInfo(customerInfo);
    }

    @Override
    public CustomerInfo internalCreateCustomer(User aUser) {
        Transaction transaction = datastore.newTransaction();
        try {
            String id = userRepository.create(aUser);
            aUser.setId(id);

            CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setUserId(id);
            customerRepository.saveCustomerInfo(customerInfo);

            transaction.commit();
            return customerInfo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCustomerStatus(String customerId, CustomerStatusType customerStatusType) {
        customerRepository.updateCustomerStatus(customerId, customerStatusType);
    }

    @Override
    public Collection<CustomerInfo> fetchCustomers(List<String> customerIds) {
        return customerRepository.fetchCustomers(customerIds);
    }

    @Override
    public Collection<CustomerInfo> fetchCustomersByUserIds(List<String> customerUserIds) {
        return customerRepository.fetchCustomersByUserIds(customerUserIds);
    }

    @Override
    public CustomerShare getCustomerShare(String userId) {
        CustomerInfo customerInfo = customerRepository.getCustomerInfo(userId);
        CustomerShare result = new CustomerShare();
        result.setAddressLine(customerInfo.getAddressLine());
        result.setCity(customerInfo.getCity());
        result.setEmail(customerInfo.getEmail());
        result.setFullName(customerInfo.getFullName());
        result.setPhone(customerInfo.getPhone());
        result.setState(customerInfo.getState());
        result.setZip(customerInfo.getZip());

        String contactTimes = customerInfo.getContactTimes().stream()
                .map(messagesRetriever::getCaption)
                .collect(Collectors.joining(","));
        String contactDay = messagesRetriever.getCaption(customerInfo.getContactDay());
        result.setCallIntervals(String.format("%s %s", contactDay, contactTimes));
        return result;
    }

    @Override
    public List<CustomerInfo> getCustomers() {
        return customerRepository.getCustomers();
    }
}
