package com.drevin.Fitnes.Trening.App.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthSecurityConfigTest {

    PublicKey publicKey ;
    PrivateKey privateKey;
    RSAKey rsaKey;


    @BeforeEach
    void init () throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

        rsaKey = new RSAKey
                .Builder((RSAPublicKey) publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void keyPair() {
        assertTrue(publicKey instanceof RSAPublicKey,"publicKey is not instance of RSAPublicKey");
        assertTrue(privateKey instanceof RSAPrivateKey,"privateKey is not instance of RSAPrivateKey");

        assertEquals( ((RSAPublicKey) publicKey).getModulus().bitLength(),2048,"length publicKey is not 2048");
        assertEquals( ((RSAPrivateKey) privateKey).getModulus().bitLength(),2048, "length privateKey is not 2048");

        assertNotNull(publicKey);
        assertNotNull(privateKey);
    }

    @Test
    void RsaKeyTest() throws JOSEException {
        String keyId = UUID.randomUUID().toString();

        RSAKey rsaKey = new RSAKey
                .Builder((RSAPublicKey) publicKey)
                .privateKey(privateKey)
                .keyID(keyId)
                .build();

        assertEquals(keyId,rsaKey.getKeyID());
        assertNotNull(rsaKey.toPublicKey());
        assertNotNull(rsaKey.toPrivateKey());
    }

    @Test
    void BCryptEncDec(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password ="password ";

        String encodePassword = encoder.encode(password);

        assertTrue(encoder.matches(password,encodePassword));
    }

    @Test
    void  selectJWKSet_returnValidJWKSet() throws KeySourceException {

        JWKSource<SecurityContext> jwkSource = (jwkSelector, securityContext) -> {
            List<JWK> jwksList = new ArrayList<>();
            jwksList.add(rsaKey);

            var jwkSet = new JWKSet(jwksList);
            return jwkSelector.select(jwkSet);
        };

        JWKSelector jwkSelector = new JWKSelector(
                new JWKMatcher.Builder()
                        .keyType(KeyType.RSA)
                        .keyID(rsaKey.getKeyID())
                        .build()
        );

        List<JWK> jwkList = jwkSource.get(jwkSelector,null);
        JWKSet jwkSet = new JWKSet(jwkList);

        assertThat(jwkSet.toPublicJWKSet().getKeys()).hasSize(1);
        assertThat(jwkSet.getKeys())
                .extracting(JWK::toPublicJWK)
                .containsExactly(rsaKey.toPublicJWK());

    }

    @Test
    void jwtDecoder_ShouldThrowException_WhenInvalidToken() throws JOSEException {

        JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();

        assertThatThrownBy(() -> jwtDecoder.decode("invalid.token.value"))
                .isInstanceOf(JwtException.class);
    }






}