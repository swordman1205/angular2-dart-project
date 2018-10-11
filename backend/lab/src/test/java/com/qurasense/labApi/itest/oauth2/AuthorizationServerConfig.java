package com.qurasense.labApi.itest.oauth2;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
@SuppressWarnings("static-method")
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
        .authenticationManager(authenticationManager)
        .accessTokenConverter(jwtAccessTokenConverter);
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
        .withClient("myclientwith")
        .authorizedGrantTypes("password")
        .authorities("myauthorities")
        .resourceIds("myresource")
        .scopes("myscope")

        .and()
        .withClient("myclientwithout")
        .authorizedGrantTypes("password")
        .authorities("myauthorities")
        .resourceIds("myresource")
        .scopes(UUID.randomUUID().toString());
    }
}