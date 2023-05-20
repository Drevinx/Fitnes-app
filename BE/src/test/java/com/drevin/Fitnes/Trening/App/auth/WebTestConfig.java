package com.drevin.Fitnes.Trening.App.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.reactive.server.WebTestClient;

@Configuration
public class WebTestConfig {

    @Bean
    public WebTestClient.Builder webTestClientBuilder() {
        return WebTestClient.bindToServer();
    }
}
