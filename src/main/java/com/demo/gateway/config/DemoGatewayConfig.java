package com.demo.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Set;

@Configuration
public class DemoGatewayConfig {
    @Bean
    @Profile("localhostRouter-noEureka")
    public RouteLocator configLocalNoEureka(RouteLocatorBuilder builder) {
        /*
          Especifico las reglas con las que va a funcionar mi Gateway
         */
        return builder
                .routes()
                .route(r -> r
                        .path("/api/v1/dragon-ball/*")
                        .uri("http://localhost:8082")
                )
                .route(r -> r
                        .path("/api/v1/game-of-thrones/*")
                        .uri("http://localhost:8083")
                )
                .build();
    }

    @Bean
    @Profile("localhostRouter-eureka")
    public RouteLocator configLocalEureka(RouteLocatorBuilder builder) {
        /*
          Especifico las reglas con las que va a funcionar mi Gateway
         */
        return builder
                .routes()
                .route(r -> r
                        .path("/api/v1/dragon-ball/*")
                        .uri("lb://ms-demo-dragon-ball")
                )
                .route(r -> r
                        .path("/api/v1/game-of-thrones/*")
                        .uri("lb://ms-demo-game-of-thrones")
                )
                .build();
    }

    @Bean
    @Profile("localhostRouter-eureka-cb")
    public RouteLocator configLocalEurekaCb(RouteLocatorBuilder builder) {
        /*
          Especifico las reglas con las que va a funcionar mi Gateway
         */
        return builder
                .routes()
                .route(r -> r.path("/api/v1/dragon-ball/*")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec
                                .circuitBreaker(config -> config.setName("failoverCB")
                                        /*
                                          Si el Circuit Breaker detecta fallas dentro del servicio de dragon-ball
                                          redireccionará a la URL de Fallback
                                         */
                                        .setFallbackUri("forward:/api/v1/dragon-ball-failover/characters")
                                        //.setStatusCodes(Set.of("200"))
                                        .setRouteId("dbFailover"))
                        )
                        .uri("lb://ms-demo-dragon-ball")
                )
                .route(r -> r
                        .path("/api/v1/game-of-thrones/*")
                        .uri("lb://ms-demo-game-of-thrones")
                )
                .route(r -> r
                        .path("/api/v1/dragon-ball-failover/characters")
                        .uri("lb://ms-demo-dragon-ball-failover")
                )
                .build();
    }
}