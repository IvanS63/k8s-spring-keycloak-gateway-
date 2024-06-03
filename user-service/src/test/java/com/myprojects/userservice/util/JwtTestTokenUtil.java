package com.myprojects.userservice.util;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.security.interfaces.RSAPrivateKey;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * <p>This util class simulates token generation process in Keycloak format.
 */
@Slf4j
public class JwtTestTokenUtil {
    private static final String ISSUER = "some-test-issuer";
    private static final String CLIENT_SUBJECT = "ab126fa7-ac2a-434f-9864-936ee72b8e37";

    private final JWSSigner signer;

    /**
     * Creates JSON Web Signature signer based on given RSA private key.
     *
     * <p>The given private key must match the public key defined in
     * 'spring.security.oauth2.resourceserver.jwt.public-key-location' property
     */
    public JwtTestTokenUtil(RSAPrivateKey privateKey) {
        this.signer = new RSASSASigner(privateKey);
    }

    /**
     * Generates user access token with given userId
     */
    public String generateUserToken(String userId) {
        return generateToken(userId, ISSUER,
                Map.of("user_id", userId),
                List.of());
    }

    /**
     * Generates access token with custom claims
     */
    public String generateToken(Map<String, String> claims) {
        return generateToken(CLIENT_SUBJECT, ISSUER, claims, List.of());
    }

    @SneakyThrows
    private String generateToken(String subject, String issuer,
            Map<String, String> claims,
            List<String> roles) {
        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(issuer)
                .expirationTime(tomorrow());

        claims.forEach(claimsBuilder::claim);

        if (!roles.isEmpty()) {
            claimsBuilder.claim("realm_access", Map.of("roles", roles));
        }

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).build(),
                claimsBuilder.build());

        signedJWT.sign(signer);

        String token = signedJWT.serialize();
        log.info("Generated JWT token: {}", token);
        return token;
    }

    private static Date tomorrow() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
