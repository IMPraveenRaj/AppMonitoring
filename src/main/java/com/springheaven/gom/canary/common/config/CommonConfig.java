package com.springheaven.gom.canary.common.config;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class CommonConfig {

@Bean("commonHttpClient")
@Scope("prototype")
@Primary
    public CloseableHttpClient createNewHttpClient(){
        return HttpClientBuilder.create().build();
    }



    @Bean("commonObjectMapper")
    @Primary
    public ObjectMapper createCommonObjectMapper(){
    ObjectMapper mapper= new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    return  mapper;

    }






}
