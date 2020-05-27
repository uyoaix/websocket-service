package com.yufei.learn.websocket.config;

import com.yufei.learn.websocket.service.UserService;
import com.yufei.learn.websocket.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author yufei.wang
 * @date 2020/05/24 19:30
 */
@Configuration
@EnableResourceServer
public class ResourceServerSecurityConfig extends ResourceServerConfigurerAdapter {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecuret;

    @Value("${security.oauth2.resource.token-info-uri}")
    private String tokenInfoUri;

    private final UserService userService;

    private final RestTemplate restTemplate;

    public ResourceServerSecurityConfig(UserService userService,
                                        RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/user/**"
                ).authenticated()
                .anyRequest().permitAll();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(userService);

        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(userAuthenticationConverter);

        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setRestTemplate(restTemplate);
        tokenServices.setClientId(clientId);
        tokenServices.setClientSecret(clientSecuret);
        tokenServices.setCheckTokenEndpointUrl(tokenInfoUri);
        tokenServices.setAccessTokenConverter(accessTokenConverter);

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });

        resources.tokenServices(tokenServices);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
