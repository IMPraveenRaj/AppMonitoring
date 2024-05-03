package com.springheaven.gom.canary.ida.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class IdaServiceConfig {

    @Bean("httpComponentsClientHttpRequestFactory")
    public HttpComponentsClientHttpRequestFactory createFactory(){
        return null;
    }

    @Bean("idaRestTemplate")
    public RestTemplate createRestTemplate(){
        return new RestTemplate();
    }
}
