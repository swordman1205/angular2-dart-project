package com.qurasense.userApi.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import com.qurasense.common.repository.local.LocalCustomRepository;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.model.UserRole;
import org.springframework.stereotype.Repository;

@Repository
public class LocalUserRepository extends LocalCustomRepository<User> implements UserRepository {

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @PostConstruct
    public void initMockUsers() {
        List<User> users = Arrays.asList(
                new User("Qurasense Admin","admin@qurasense.com", "1", UserRole.ADMIN, new Date()),
                new User("Lars Tackmann","lars@qurasense.com", "2", UserRole.CUSTOMER, new Date()),
                new User("Zufar Muhamadeev","zufar@qurasense.com", "3", UserRole.CUSTOMER, new Date()));

        //each local user has "1" password
        users.forEach(u->u.setEncryptedPassword("$2a$10$G/8MOZKl2I04PdzB7L1qc.Sx1UXQK7vtPxKcAJPddOwk.J.55wg4q"));
        users.forEach(this::save);
    }

    @Override
    public List<User> getUsers() {
        return getValues();
    }

    @Override
    public List<User> getNurses() {
        return getValues().stream().filter((u) -> Objects.equals(u.getRole(), UserRole.NURSE)).collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return getValues().stream().filter(u -> Objects.equals(u.getEmail(), email)).findFirst();
    }

    @Override
    public User getUserById(String id) {
        return getValues().stream().filter(u -> Objects.equals(u.getId(), id)).findFirst().get();
    }

    @Override
    public String create(User user) {
        return save(user);
    }

    @Override
    public void deleteUser(String userId) {
        deleteById(userId);
    }

    @Override
    public int deleteAllUser() {
        return deleteAll();
    }

    @Override
    public User update(User user) {
        save(user);
        return user;
    }

    @Override
    public void updateCurrent(User user) {
        update(user);
    }

    @Override
    public void changePassword(User user, String encryptedPassword) {
        User loaded = findById(user.getId());
        loaded.setEncryptedPassword(encryptedPassword);
        save(loaded);
    }

}