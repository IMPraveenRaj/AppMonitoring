package com.springheaven.gom.canary.idp.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "idp.api")
public class IdpApiConfig {

    private String url;
    private String jksFilePath;
    private String jksEncToken;
    private String onBehalfOf;
    private String requestTag;
    private Boolean Distinct;
    private Integer limit;
    private Boolean returnMetaData;
    private String jpmcClId;
    private String jpmcClIdVal;
    private Integer timeout;

}
