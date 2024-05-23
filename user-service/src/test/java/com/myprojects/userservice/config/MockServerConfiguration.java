package com.myprojects.userservice.config;

import com.myprojects.userservice.util.MockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockServerConfiguration {

    @Bean(destroyMethod = "close")
    public MockServer keycloakServer() {
        return new MockServer(9080);
    }

}
