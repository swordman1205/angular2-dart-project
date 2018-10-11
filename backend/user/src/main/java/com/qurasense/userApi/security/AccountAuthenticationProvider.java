package com.qurasense.userApi.security;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.qurasense.common.MessagesRetriever;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.model.UserRole;
import com.qurasense.userApi.repository.CustomerRepository;
import com.qurasense.userApi.repository.UserRepository;
import com.qurasense.userApi.security.config.OAuth2ServerConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccountAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final SecurityService securityService;

    private final MessagesRetriever messagesRetriever;

    public AccountAuthenticationProvider(final UserDetailsService userDetailsService,
            final PasswordEncoder passwordEncoder,
            final UserRepository userRepository,
            final SecurityService securityService,
            final ApplicationContext applicationContext,
            MessagesRetriever messagesRetriever) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.securityService = securityService;
        this.messagesRetriever = messagesRetriever;
    }

    @Override
    protected void additionalAuthenticationChecks(final UserDetails userDetails,
            final UsernamePasswordAuthenticationToken token) throws AuthenticationException {
        if (token.getCredentials() == null || userDetails.getPassword() == null) {
            throw new BadCredentialsException(messagesRetriever.getMessage("login.error.nullCredentials"));
        }
        if (!passwordEncoder.matches((String) token.getCredentials(), userDetails.getPassword())) {
            throw new BadCredentialsException(messagesRetriever.getMessage("login.error.badCredentials"));
        }
        if (isCustomer(userDetails)) {
            CustomUserDetails cud = (CustomUserDetails) userDetails;
            User user = userRepository.getUserById(cud.getUserId());
            if (!Objects.equals(user.getActive(), Boolean.TRUE)) {
                throw new LockedException(messagesRetriever.getMessage("login.error.unapproved"));
            }
        } else if (isMobile(token.getDetails())) {
            throw new LockedException(messagesRetriever.getMessage("login.error.mobileOnlyCustomer"));
        }
    }

    private boolean isMobile(Object details) {
        Map<String, String> map = (Map<String, String>) details;
        return OAuth2ServerConfiguration.OAUTH2_SCOPE_MOBILE.equalsIgnoreCase(map.get("scope"));
    }

    private boolean isCustomer(UserDetails userDetails) {
        Set<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toSet());
        return Objects.equals(Sets.newHashSet(UserRole.CUSTOMER.name()), authorities);
    }

    @Override
    protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken token) throws AuthenticationException {
        return userDetailsService.loadUserByUsername(username);
    }

}
