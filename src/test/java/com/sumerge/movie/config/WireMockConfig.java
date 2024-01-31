package com.sumerge.movie.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WireMockConfig {
    @Bean
    public WireMockServer wireMockServer(){
        WireMockServer server= new WireMockServer(8080);
        server.start();
        return server;
    }
}
