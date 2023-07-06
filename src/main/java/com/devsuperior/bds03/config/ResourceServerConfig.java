package com.devsuperior.bds03.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.expression.spel.ast.Operator;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private Environment env;

    @Autowired
    private JwtTokenStore tokenStore;

    private static final String[] H2_AND_OAUTH = {"/h2-console/**", "/oauth/token"}; // permite o acesso ao H2 e ao endpoint de autenticação

    private static final String[] OPERATOR_GET = {"/departments/**", "/employees/**"};


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable(); // permite o acesso ao H2
        }

        http.authorizeRequests()
                .antMatchers(H2_AND_OAUTH).permitAll()
                .antMatchers(HttpMethod.GET, OPERATOR_GET).hasAnyRole("OPERATOR", "ADMIN") //operador e admin podem fazer get em departamentos e funcionarios
                .anyRequest().hasAnyRole("ADMIN");
    }
}
