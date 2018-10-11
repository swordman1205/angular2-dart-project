package com.qurasense.userApi.security.config;

import com.google.common.base.Joiner;
import com.qurasense.userApi.security.CustomUserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        CustomUserDetails cud = (CustomUserDetails) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put("userId", cud.getUserId());
        additionalInfo.put("roles", Joiner.on(",").join(authentication.getAuthorities()));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

}