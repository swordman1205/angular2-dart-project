package com.qurasense.userApi.repository;

import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.KeyFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.QueryKeys;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.repository.datastore.DatastoreCustomRepository;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Repository
@Profile({"emulator", "cloud"})
public class UserRepositoryImpl extends DatastoreCustomRepository<User> implements UserRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Datastore datastore;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    private com.google.cloud.datastore.Key ancestor;
    private KeyFactory userKeyFactory;

    @PostConstruct
    protected void init() {
        userKeyFactory = datastore.newKeyFactory().setKind(User.class.getSimpleName());
        ancestor = datastore.newKeyFactory().setKind("UserList").newKey("default");
    }

    private LoadType<User> loadType() {
        return ofy().load().type(User.class);
    }

    @Override
    public List<User> getUsers() {
        return ofy().load().type(User.class).ancestor(ancestor).list();
    }

    @Override
    public List<User> getNurses() {
        return ofy().load().type(User.class).ancestor(ancestor).filter("role =", UserRole.NURSE).list();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(loadType().ancestor(ancestor).filter("email =", email.toLowerCase()).first().now());
    }

    @Override
    public User getUserById(String id) {
        return ofy().load().type(User.class).parent(ancestor).id(id).now();
    }

    @Override
    public String create(User user) {
        user.setUserList(ancestor);
        user.setId(idGenerationStrategy.generate(userKeyFactory));
        save(user);
        return user.getId();
    }

    @Override
    public void deleteUser(String userId) {
        ofy().delete().type(User.class).parent(ancestor).id(userId).now();
    }

    @Override
    public int deleteAllUser() {
        QueryKeys<User> userKeys = ofy().load().type(User.class).ancestor(ancestor).keys();
        List<Key<User>> keyList = userKeys.list();
        logger.info("Deleting {} users.", keyList.size());
        ofy().delete().keys(keyList).now();
        return keyList.size();
    }

    @Override
    public User update(User user) {
        user.setUserList(ancestor);
        save(user);
        return user;
    }

    @Override
    public void updateCurrent(User user) {
        User loaded = getUserById(user.getId());
        loaded.setEmail(user.getEmail());
        loaded.setFullName(user.getFullName());
        save(user);
        logger.info("updated current user id:{}", user.getId());
    }

    @Override
    public void changePassword(User user, String encryptedPassword) {
        User loaded = getUserById(user.getId());
        loaded.setEncryptedPassword(encryptedPassword);
        update(user);
    }
}
