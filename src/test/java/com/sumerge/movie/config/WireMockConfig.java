package com.sumerge.movie.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@TestConfiguration
public class WireMockConfig {
    @Bean
    public WireMockServer wireMockServer(){
        WireMockServer server= new WireMockServer(new WireMockConfiguration().dynamicPort());
        server.start();
        return server;
    }
}
