package com.demo.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DemoGatewayConfig {
    @Bean
    //@Profile("localhostRouter-noEureka")
    public RouteLocator configLocalNoEureka(RouteLocatorBuilder builder) {
        /**
         * Especifico las reglas con las que va a funcionar mi Gateway
         */
        return builder
                .routes()
                .route(r -> r
                        .path("/api/v1/dragon-ball/*")
                        .uri("http://localhost:8081")
                )
                .route(r -> r
                        .path("/api/v1/game-of-thrones/*")
                        .uri("http://localhost:8083")
                )
                .build();
    }
}