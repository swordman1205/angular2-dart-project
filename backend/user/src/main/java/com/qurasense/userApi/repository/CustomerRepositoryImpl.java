package com.qurasense.userApi.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.KeyFactory;
import com.googlecode.objectify.cmd.QueryKeys;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.repository.datastore.DatastoreCustomRepository;
import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.CustomerStatus;
import com.qurasense.userApi.model.CustomerStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Repository
@Profile({"emulator", "cloud"})
public class CustomerRepositoryImpl extends DatastoreCustomRepository<CustomerInfo> implements CustomerRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Datastore datastore;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    private com.google.cloud.datastore.Key ancestor;
    private KeyFactory customerKeyFactory;

    @PostConstruct
    protected void init() {
        customerKeyFactory = datastore.newKeyFactory().setKind(CustomerInfo.class.getSimpleName());
        ancestor = datastore.newKeyFactory().setKind("UserInfoList").newKey("default");
    }

    @Override
    public int deleteAll() {
        QueryKeys<CustomerInfo> customerKeys = ofy().load().type(CustomerInfo.class).ancestor(ancestor).keys();
        List<com.googlecode.objectify.Key<CustomerInfo>> keyList = customerKeys.list();
        logger.info("Deleting {} customer info.", keyList.size());
        ofy().delete().keys(keyList).now();
        return keyList.size();
    }

    @Override
    public CustomerInfo getCustomerInfo(String userId) {
        CustomerInfo result = ofy().load().type(CustomerInfo.class)
                .ancestor(ancestor)
                .filter("userId =", userId)
                .first()
                .now();
        if (Objects.isNull(result)) {
            throw new IllegalStateException("no customer with userId:" + userId);
        }
        return result;
    }

    @Override
    public List<CustomerInfo> getCustomers() {
        return ofy().load().type(CustomerInfo.class).ancestor(ancestor).order("-createTime").list();
    }

    @Override
    public void updateCustomerStatus(String customerId, CustomerStatusType customerStatusType) {
        CustomerInfo customerInfo = ofy().load().type(CustomerInfo.class).parent(ancestor).id(customerId).now();
        if (Objects.nonNull(customerInfo.getCustomerStatus())) {
            customerInfo.getCustomerStatusHistory().add(customerInfo.getCustomerStatus());
        }
        customerInfo.setCustomerStatus(new CustomerStatus(customerStatusType, new Date()));
        ofy().save().entity(customerInfo).now();
    }

    @Override
    public String saveCustomerInfo(CustomerInfo aCustomerInfo) {
        aCustomerInfo.setCustomerInfoList(ancestor);
        if (aCustomerInfo.getId() == null) {
            aCustomerInfo.setId(idGenerationStrategy.generate(customerKeyFactory));
        }
        save(aCustomerInfo);
        return aCustomerInfo.getId();
    }

    @Override
    public Collection<CustomerInfo> fetchCustomers(List<String> customerIds) {
        Map<String, CustomerInfo> result = ofy().load().type(CustomerInfo.class).parent(ancestor).ids(customerIds);
        return result.values();
    }

    @Override
    public Collection<CustomerInfo> fetchCustomersByUserIds(List<String> customerUserIds) {
        return customerUserIds.stream()
                .map(userId -> ofy().load()
                    .type(CustomerInfo.class)
                    .ancestor(ancestor)
                    .filter("userId =", userId)
                    .first()
                    .now())
                .collect(Collectors.toList());
    }

}
