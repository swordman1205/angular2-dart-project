package com.qurasense.userApi.security.config;

import java.util.Map;

import com.google.common.collect.Lists;
import com.qurasense.userApi.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

//import com.qurasense.userApi.model.User;

@Configuration
public class OAuth2ServerConfiguration {

    public static final String OAUTH2_SCOPE_BROWSER = "browser";
    public static final String OAUTH2_SCOPE_MOBILE = "mobile";

    @Configuration
    @EnableResourceServer
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired
        private JwtAccessTokenConverter jwtAccessTokenConverter;

        @Autowired
        private Environment environment;

        @Override
        public void configure(final ResourceServerSecurityConfigurer resources) {
            resources.tokenStore(new JwtTokenStore(jwtAccessTokenConverter));
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry exp = http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/resources/**",
                            //swagger
                            "/swagger-ui.html",
                            "/v2/api-docs",
                            "/webjars/springfox-swagger-ui/**",
                            "/swagger-resources/**",

                            //ping
                            "/ping",

                            //registration
                            "/registration",
                            "/registration/**",

                            //internal
                            "/internal/**",

                            //static
                            "/styles/**",
                            "/styles.css",
                            "/main.dart.js",
                            "/index.html",
                            "/logo.png",
                            "/favicon.png",
                            "/main.ng_placeholder",
                            "/packages/**",
                            "/images/**"
                    ).permitAll();
            if (environment.acceptsProfiles("emulator")) {
                // add fill test data urls at emulator profile
                exp.antMatchers("/createTestData", "/createAdmin").permitAll();
            }
            exp.antMatchers("/**").authenticated();
        }
    }

    @Configuration
    @EnableAuthorizationServer
    public static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private JwtAccessTokenConverter jwtAccessTokenConverter;

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        private CustomTokenEnhancer customTokenEnhancer = new CustomTokenEnhancer();

        @Override
        public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.tokenServices(defaultTokenServices()).authenticationManager(authenticationManager);
        }

//        @Bean
        public TokenEnhancerChain tokenEnhancerChain(){
            final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            tokenEnhancerChain.setTokenEnhancers(Lists.newArrayList(customTokenEnhancer, jwtAccessTokenConverter));
            return tokenEnhancerChain;
        }

//        @Bean
        public JwtTokenStore tokenStore() {
            JwtTokenStore store = new JwtTokenStore(jwtAccessTokenConverter);
            return store;
        }

        @Bean
        @Primary
        public DefaultTokenServices defaultTokenServices(){
            final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setTokenStore(tokenStore());
//            defaultTokenServices.setClientDetailsService(clientDetailsService);
            defaultTokenServices.setTokenEnhancer(tokenEnhancerChain());
            defaultTokenServices.setSupportRefreshToken(true);
            return defaultTokenServices;
        }

        @Override
        public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
            // @formatter:off
            clients
                .inMemory()
                .withClient("browser")
                    .secret("$2a$10$sU8fDnai6G2zWdinijPeCefkOI4X8xSPs7sMHoTlU2CNaolk/o3cG")//$2a$10$sU8fDnai6G2zWdinijPeCefkOI4X8xSPs7sMHoTlU2CNaolk/o3cG//browsersecret
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes(OAUTH2_SCOPE_BROWSER)
                .and()
                .withClient("mobile")
                    .secret("$2a$10$oVFzXiivUidyTnytL2rziuiINWKssToxdpSNobTDtYILsHBeWPTpy")//$2a$10$oVFzXiivUidyTnytL2rziuiINWKssToxdpSNobTDtYILsHBeWPTpy//mobilesecret
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes(OAUTH2_SCOPE_MOBILE);
            // @formatter:on
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.checkTokenAccess("permitAll()");
        }
    }

    private static final String SIGNING_KEY = "s1f41234pwqdqkl4l12ghg9853123sd";

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);
        DefaultAccessTokenConverter datc = (DefaultAccessTokenConverter) jwtAccessTokenConverter.getAccessTokenConverter();
        datc.setUserTokenConverter(new UserAuthenticationConverterImpl());
        return jwtAccessTokenConverter;
    }

    class UserAuthenticationConverterImpl extends DefaultUserAuthenticationConverter {

        @Override
        public Authentication extractAuthentication(Map<String, ?> map) {
            Authentication authentication = super.extractAuthentication(map);
            CustomUserDetails cud = new CustomUserDetails(
                    authentication.getPrincipal().toString(),
                    "N/A",
                    authentication.getAuthorities(),
                    map.get("userId").toString()
            );
            return new UsernamePasswordAuthenticationToken(cud, cud.getPassword(), authentication.getAuthorities());
        }
    }

}
