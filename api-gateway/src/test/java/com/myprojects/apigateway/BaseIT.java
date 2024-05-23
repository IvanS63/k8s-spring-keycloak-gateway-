package com.myprojects.apigateway;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.myprojects.apigateway.config.MockServerConfiguration;
import com.myprojects.apigateway.util.MockServer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = {MockServerConfiguration.class})
public class BaseIT {

    @LocalServerPort
    private int port;

    @Autowired
    protected MockServer keycloakServer;
    @Autowired
    protected MockServer userService;
    @Autowired
    private ListableBeanFactory beanFactory;

    @BeforeEach
    void resetMocks() {
        beanFactory.getBeansOfType(MockServer.class)
                .values()
                .forEach(MockServer::reset);
    }

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.port = port;
    }
}
