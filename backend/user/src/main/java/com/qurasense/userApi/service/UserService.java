package com.qurasense.userApi.service;

import java.util.List;
import java.util.Optional;

import com.qurasense.common.shared.UserShare;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.model.UserWithPassword;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

    @PreAuthorize("hasAuthority('ADMIN')")
    Optional<User> getUserByEmail(String email);

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MEDICAL') or (principal.username == #id)")
    User getUserById(String id);

    @PreAuthorize("hasAuthority('ADMIN')")
    List<User> getUsers();

    @PreAuthorize("hasAuthority('ADMIN')")
    String create(UserWithPassword user);

    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('CUSTOMER') and principal.username == #userId)")
    void delete(String userId);

    @PreAuthorize("hasAuthority('ADMIN')")
    User update(UserWithPassword userWithPassword);

    @PreAuthorize("principal.username == #user.id")
    void update(User user);

    @PreAuthorize("hasAuthority('MEDICAL')")
    List<User> getNurses();

    UserShare getUserShare(String userId);

    void setActive(String userId);
}
