package com.qurasense.userApi.security;

import java.util.Date;
import java.util.Objects;

import com.googlecode.objectify.ObjectifyService;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Profile({"emulator", "cloud"})
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        ObjectifyService.run(() -> {
            if (event.getAuthentication().getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails principal = (CustomUserDetails) event.getAuthentication().getPrincipal();
                User user = userRepository.getUserById(principal.getUserId());
                if (Objects.isNull(user)) {
                    logger.warn("not found user with id: {}", principal.getUserId());
                } else {
                    user.setLastLoginTime(new Date());
                    userRepository.update(user);
                }
            } else {
                logger.debug("not user auth: {}", event.getAuthentication().getPrincipal());
            }
            return null;
        });
    }

}
