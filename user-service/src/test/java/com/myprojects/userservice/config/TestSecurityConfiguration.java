package com.myprojects.userservice.config;

import com.myprojects.userservice.util.JwtTestTokenUtil;
import java.security.interfaces.RSAPrivateKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestSecurityConfiguration {

    @Value("classpath:rsa/rsa_key")
    private RSAPrivateKey privateKey;

    @Bean
    public JwtTestTokenUtil tokenGenerator() {
        return new JwtTestTokenUtil(this.privateKey);
    }
}
