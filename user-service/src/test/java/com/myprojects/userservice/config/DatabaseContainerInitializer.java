package com.myprojects.userservice.config;

import com.github.dockerjava.api.model.RestartPolicy;
import java.util.Objects;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseContainerInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String POSTGRES_IMAGE = "postgres:13.4";

    public static final PostgreSQLContainer POSTGRES_CONTAINER = new PostgreSQLContainer<>(
            POSTGRES_IMAGE)
            .withUsername("testuser")
            .withPassword("testuser")
            .withDatabaseName("test_db")
            .withReuse(true)
            .withCreateContainerCmdModifier(cmd ->
                    Objects.requireNonNull(cmd.getHostConfig(), "Host config should not be null!")
                            .withRestartPolicy(RestartPolicy.alwaysRestart()))
            .withConnectTimeoutSeconds(30);

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        POSTGRES_CONTAINER.start();
        TestPropertyValues.of(
                "spring.r2dbc.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
                "spring.r2dbc.password=" + POSTGRES_CONTAINER.getPassword(),
                "spring.r2dbc.username=" + POSTGRES_CONTAINER.getUsername(),
                "spring.flyway.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
                "spring.flyway.password=" + POSTGRES_CONTAINER.getPassword(),
                "spring.flyway.username=" + POSTGRES_CONTAINER.getUsername(),
                "spring.datasource.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
                "spring.datasource.password=" + POSTGRES_CONTAINER.getPassword(),
                "spring.datasource.username=" + POSTGRES_CONTAINER.getUsername()
        ).applyTo(configurableApplicationContext.getEnvironment());

    }
}
