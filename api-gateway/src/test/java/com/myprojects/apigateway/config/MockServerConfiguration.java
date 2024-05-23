package com.myprojects.apigateway.config;

import com.myprojects.apigateway.util.MockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockServerConfiguration {

    @Bean(destroyMethod = "close")
    public MockServer keycloakServer() {
        return new MockServer(9080);
    }

    @Bean(destroyMethod = "close")
    public MockServer userService() {
        return new MockServer(9081);
    }

}
