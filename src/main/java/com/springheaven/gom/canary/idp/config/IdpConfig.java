package com.springheaven.gom.canary.idp.config;


import com.springheaven.gom.canary.common.service.SSLHelperService;
import com.springheaven.gom.canary.ida.config.IdaProperties;
import com.springheaven.gom.canary.ida.service.EncryptDecryptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.swing.text.html.Option;
import java.util.Optional;

@Configuration
@Slf4j
public class IdpConfig {

    @Bean("idpIdaProperties")
    @ConfigurationProperties(prefix = "idanywhere.idp")
    public IdaProperties createIdaProperties(){
        return new IdaProperties();
    }


    public Optional<SSLContext> createSslContext(IdpApiConfig apiConfig, EncryptDecryptService decryptService, SSLHelperService sslHelperService){
        final String keyStoreFile = apiConfig.getJksFilePath();
        final String encToken= apiConfig.getJksEncToken();
        return  sslHelperService.createSslContext(keyStoreFile,encToken,decryptService);
    }


    public CloseableHttpClient createHttpClient(IdpApiConfig apiConfig, EncryptDecryptService decryptService, SSLHelperService sslHelperService){

        final String keyStoreFile = apiConfig.getJksFilePath();
        final String encToken= apiConfig.getJksEncToken();
        RequestConfig config= RequestConfig.custom().setConnectTimeout(apiConfig.getTimeout()*1000).
                setConnectionRequestTimeout(apiConfig.getTimeout()*1000).
                setSocketTimeout(apiConfig.getTimeout()*1000).build();
        Optional<SSLContext> sslContext= sslHelperService.createSslContext(keyStoreFile,encToken,decryptService);
        if(sslContext.isEmpty()){
            throw new BeanCreationException("SSL context not created for keyStore file:"+ keyStoreFile);

        }
        return HttpClientBuilder.create().setDefaultRequestConfig(config).setSSLContext(sslContext.get()).build();


    }

}
