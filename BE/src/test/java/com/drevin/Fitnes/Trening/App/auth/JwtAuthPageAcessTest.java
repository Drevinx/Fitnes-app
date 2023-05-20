package com.drevin.Fitnes.Trening.App.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JwtAuthPageAcessTest {


    @Autowired
    @Qualifier("webTestClientBuilder")
    private WebTestClient.Builder webTestClientBuilder;

    private WebTestClient webTestClient;

    @Bean
    public WebTestClient.Builder webTestClientBuilder(){
        return WebTestClient.bindToServer();
    }


    @BeforeEach
    void init () throws NoSuchAlgorithmException {
        webTestClient = webTestClientBuilder.build();
    }

    @Test
    void loginPageIsAccessible() {
        webTestClient.get()
                .uri("http://localhost:8080/login")
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void registerPageIsAccessible() {
        webTestClient.get()
                .uri("http://localhost:8080/register")
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    public void testProtectedPageRequiresAuthentication() {
        webTestClient.get()
                .uri("http://localhost:8080/home")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void testStaticFilesAreAccessible() {
        // must be in static/css/style.css
        webTestClient.get()
                .uri("http://localhost:8080/css/style.css")
                .exchange()
                .expectStatus().isOk();
    }

}
