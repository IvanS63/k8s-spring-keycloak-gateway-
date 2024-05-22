CREATE TABLE user_profiles
(
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    keycloak_user_id   VARCHAR(36),
    first_name         VARCHAR(128) NOT NULL,
    last_name          VARCHAR(128) NOT NULL,
    email              VARCHAR(128) NOT NULL,
    creation_date      TIMESTAMP    NOT NULL
);
