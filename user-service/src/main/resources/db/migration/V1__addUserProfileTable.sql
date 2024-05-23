CREATE TABLE user_profile
(
    id                 UUID PRIMARY KEY,
    keycloak_id        VARCHAR(36) NOT NULL,
    first_name         VARCHAR(128) NOT NULL,
    last_name          VARCHAR(128) NOT NULL,
    email              VARCHAR(128) NOT NULL,
    creation_date      TIMESTAMP    NOT NULL
);
