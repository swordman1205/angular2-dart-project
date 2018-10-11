package com.qurasense.userApi.security;

import com.google.common.collect.Sets;
import com.qurasense.userApi.model.User;
import com.qurasense.userApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String aEmail) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.getUserByEmail(aEmail);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            HashSet<SimpleGrantedAuthority> authorities = Sets.newHashSet(new SimpleGrantedAuthority(user.getRole().name()));
            return new CustomUserDetails(user.getId().toString(), user.getEncryptedPassword(), authorities, user.getId());
        }
        throw new UsernameNotFoundException(aEmail);
    }
}