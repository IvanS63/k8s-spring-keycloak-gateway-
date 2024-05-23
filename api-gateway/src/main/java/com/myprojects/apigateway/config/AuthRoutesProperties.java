package com.myprojects.apigateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(value = "auth")
public class AuthRoutesProperties {

    private String url;
    private String realm;
    private String clientId;
    private String clientSecret;
}
