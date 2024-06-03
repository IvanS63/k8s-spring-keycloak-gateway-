package com.myprojects.userservice.config.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                    .requestMatchers(new AntPathRequestMatcher("/users", HttpMethod.POST.name()))
                    .permitAll()
                    .anyRequest()
                    .authenticated());
            http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
            http.httpBasic(Customizer.withDefaults());
            return http.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
