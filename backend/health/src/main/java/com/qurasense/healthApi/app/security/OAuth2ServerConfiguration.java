package com.qurasense.app.security;

import java.util.Map;

import com.qurasense.common.SimpleMicroserviceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class OAuth2ServerConfiguration {

    @Configuration
    @EnableResourceServer
    public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired
        private SimpleMicroserviceRegistry simpleMicroserviceRegistry;

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
                            "/internal/**"
                    ).permitAll();
            if (environment.acceptsProfiles("emulator")) {
                // add fill test data urls at emulator profile
                exp.antMatchers("/createTestData").permitAll();
            }
            exp.antMatchers("/**").authenticated();
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
            User user = new User(authentication.getPrincipal().toString(), "N/A", authentication.getAuthorities());
            return new UsernamePasswordAuthenticationToken(user, "N/A", authentication.getAuthorities());
        }
    }

}
