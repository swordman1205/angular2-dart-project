package com.qurasense.userApi.repository;

import java.util.List;
import java.util.Optional;

import com.qurasense.userApi.model.User;

public interface UserRepository {
    public static final String USER_KIND = "User";
    List<User> getUsers();

    List<User> getNurses();

    Optional<User> getUserByEmail(String email);

    User getUserById(String id);

    String create(User user);

    void deleteUser(String userId);

    int deleteAllUser();

    User update(User user);

    void updateCurrent(User user);

    void changePassword(User user, String encryptedPassword);

}
