package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Album Service Routes
                .route("album-service", r -> r
                        .path("/api/albums/**")
                        .uri("http://localhost:8081"))

                // Song Service Routes
                .route("song-service", r -> r
                        .path("/api/songs/**")
                        .uri("http://localhost:8082"))

                .build();
    }
}