package com.qurasense.labApi.itest.oauth2;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class OAuthHelper {
    // For use with MockMvc
    public RequestPostProcessor bearerToken(String aUserId, String aRole) {
        return mockRequest -> {
            OAuth2AccessToken token = createAccessToken(aUserId, aRole);
            mockRequest.addHeader("Authorization", "Bearer " + token.getValue());
            return mockRequest;
        };
    }

    @Autowired
    ClientDetailsService clientDetailsService;
    @Autowired
    AuthorizationServerTokenServices tokenservice;

    OAuth2AccessToken createAccessToken(String aUserId, String aRole) {
        // Look up authorities, resourceIds and scopes based on clientId
        String clientId = "myclientwith";
        ClientDetails client = clientDetailsService.loadClientByClientId(clientId);
        HashSet< SimpleGrantedAuthority > authorities = Sets.newHashSet(new SimpleGrantedAuthority(aRole));
        Set<String> resourceIds = Sets.newHashSet("oauth2-resource");
        Set<String> scopes = client.getScope();

        // Default values for other parameters
        Map<String, String> requestParameters = Collections.emptyMap();
        boolean approved = true;
        String redirectUrl = null;
        Set<String> responseTypes = Collections.emptySet();
        Map<String, Serializable> extensionProperties = Collections.emptyMap();

        // Create request
        OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientId, authorities, approved, scopes,
                resourceIds, redirectUrl, responseTypes, extensionProperties);

        // Create OAuth2AccessToken
        User userPrincipal = new User(aUserId, "", true, true, true, true, authorities);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
        OAuth2Authentication auth = new OAuth2Authentication(oAuth2Request, authenticationToken);
        return tokenservice.createAccessToken(auth);
    }
}