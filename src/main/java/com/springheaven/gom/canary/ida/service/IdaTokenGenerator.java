package com.springheaven.gom.canary.ida.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.springheaven.gom.canary.ida.config.IdaProperties;
import com.springheaven.gom.canary.ida.dto.TokenResponse;
import com.springheaven.gom.canary.ida.enums.IdaEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class IdaTokenGenerator {

private final EncryptDecryptService decryptService;

private  final ApplicationContext context;

private final ObjectMapper mapper;



public IdaTokenGenerator(EncryptDecryptService decryptService,ApplicationContext context,
                         @Qualifier("commonObjectMapper") ObjectMapper mapper){

    this.decryptService=decryptService;
    this.context=context;
    this.mapper=mapper;
}


public TokenResponse createErrorToken(String error){
    TokenResponse response= new TokenResponse();
    response.setError(error);
    return  response;
}

private List<NameValuePair> createRequestParams(IdaProperties idaProperties){

    List<NameValuePair> requestBody= new ArrayList<>();

    requestBody.add(new BasicNameValuePair(IdaEnum.CLIENT_ID_PARAM.toString(), idaProperties.getClientId()));
    requestBody.add(new BasicNameValuePair(IdaEnum.USERNAME_PARAM.toString(), idaProperties.getUserName()));
    requestBody.add(new BasicNameValuePair(IdaEnum.PASS_PARAM.toString(), idaProperties.getEncToken()));
    requestBody.add(new BasicNameValuePair(IdaEnum.P_GRANT_TYPE.toString(), IdaEnum.PASS_PARAM.toString()));
    requestBody.add(new BasicNameValuePair(IdaEnum.RESOURCE_PARAM.toString(), idaProperties.getResource()));
    requestBody.add(new BasicNameValuePair(IdaEnum.SCOPE_PARAM.toString(), IdaEnum.OPENID_SCOPE.toString()));
    return  requestBody;
}

public TokenResponse getIdaToken(IdaProperties properties){


    final String restURL= properties.getUrl();
    //construct Body
    List<NameValuePair> requestBody= createRequestParams(properties);

    try(CloseableHttpClient httpClient= context.getBean("commonHttpClient",CloseableHttpClient.class)){

        HttpPost request = new HttpPost(restURL);
        request.setEntity(new UrlEncodedFormEntity(requestBody));
        request.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
        request.setHeader("Content-Type",MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        try(CloseableHttpResponse response =httpClient.execute(request)){
            int statusCode=response.getStatusLine().getStatusCode();
            String responseStr= EntityUtils.toString(response.getEntity());
            if(statusCode !=200 || !responseStr.contains(("access_token"))){
                return  createErrorToken(responseStr);
            }
            return mapper.readValue(responseStr,TokenResponse.class);
        }


    }catch (IOException e){

        log.error("Error during Getting IDA token -{}",e.getLocalizedMessage());
        String error = String.format("Error while getting IDA token for %s- resource %s  and client-id is %s",restURL,
                properties.getResource(),properties.getClientId());
        return  createErrorToken(error);
    }

}

}
