package com.catalogue.web.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate(
            RestTemplateBuilder builder,
            @Value("${management.service.username}") String username,
            @Value("${management.service.password}") String password) {
        
        // Use HttpComponentsClientHttpRequestFactory for PATCH support
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        
        RestTemplate restTemplate = builder
                .requestFactory(() -> factory)
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
        
        // Add Basic Authentication interceptor
        restTemplate.getInterceptors().add(
                new BasicAuthenticationInterceptor(username, password)
        );
        
        return restTemplate;
    }
}

