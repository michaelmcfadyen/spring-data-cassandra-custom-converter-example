package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

@Configuration
public class RouterConfig {

    private final TestRepo testRepo;

    public RouterConfig(TestRepo testRepo) {
        this.testRepo = testRepo;
    }

    @Bean
    public RouterFunction<ServerResponse> router() {
        return RouterFunctions.route()
                .GET("/test", serverRequest ->
                        testRepo.findAll()
                                .collectList()
                                .flatMap(list -> ServerResponse.ok().bodyValue(list)))
                .POST("/test", serverRequest ->
                        testRepo.insert(new TestDto(UUID.randomUUID().toString(), Locale.CANADA, Currency.getInstance(Locale.CANADA)))
                                .then(ServerResponse.ok().build()))
                .build();
    }


}
