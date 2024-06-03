package com.myprojects.userservice;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.myprojects.userservice.config.DatabaseContainerInitializer;
import com.myprojects.userservice.config.MockServerConfiguration;
import com.myprojects.userservice.config.TestSecurityConfiguration;
import com.myprojects.userservice.repository.UserProfileRepository;
import com.myprojects.userservice.util.JwtTestTokenUtil;
import com.myprojects.userservice.util.MockServer;
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
@ContextConfiguration(initializers = DatabaseContainerInitializer.class,
        classes = {MockServerConfiguration.class, TestSecurityConfiguration.class})
public class BaseIT {

    @LocalServerPort
    private int port;

    @Autowired
    protected MockServer keycloakServer;
    @Autowired
    protected UserProfileRepository userProfileRepository;
    @Autowired
    private ListableBeanFactory beanFactory;
    @Autowired
    protected JwtTestTokenUtil tokenUtil;

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

    @BeforeEach
    public void cleanDatabase() {
        userProfileRepository.deleteAll();
    }

}
