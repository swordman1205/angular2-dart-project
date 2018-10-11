package com.qurasense.userApi.security;

import com.qurasense.userApi.model.UserRole;

public interface SecurityService {
    String findLoggedInUsername();

    boolean isCurrentUserHasRole(UserRole role);

    boolean isCurrentUser(String userId);
}
