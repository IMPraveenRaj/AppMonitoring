package com.springheaven.gom.canary.service;

import com.springheaven.gom.canary.common.Exception.GomExecutionException;
import com.springheaven.gom.canary.ida.service.IdaTokenService;
import com.springheaven.gom.canary.idp.config.IdpApiConfig;
import com.springheaven.gom.canary.idp.service.IpdTokenService;
import com.springheaven.gom.canary.testrunner.Result;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StepRepositoryTest {


    StepRepository stepRepository;

    @Mock
    RestTemplate restTemplate;
    @Mock
    IdaTokenService tokenService;
    @Mock
    Environment env;
    @Mock
    IpdTokenService idptokenService;
    @Mock
    IdpApiConfig apiConfig;
    @Mock
    ApplicationContext context;

    @Mock
    CloseableHttpClient httpClient;

    @Mock
    CloseableHttpResponse httpResponse;


    @BeforeEach
    public void setupStepRepository() {

        stepRepository = new StepRepository(restTemplate, tokenService, env, idptokenService, apiConfig, context);

    }


    @Test
    @DisplayName("testing the first step valid scenario")
    public void fistStep_Test() {

        Result result = stepRepository.firstStep();

        assertEquals(0, result.getResult());

    }


    @Test
    @DisplayName("testing the first step valid scenario")
     void fistStep_failure() throws GomExecutionException {

        Mockito.when(idptokenService.createNewToken()).thenThrow(GomExecutionException.class);
        Result result = stepRepository.firstStep();
        assertEquals(-1, result.getResult());
    }


    @Test
    @DisplayName("Testing the http result ")
    void test_getHttpResult() throws IOException {
        StatusLine mockStatusLine = mock(StatusLine.class);
        HttpPost post= mock(HttpPost.class);
        HttpEntity entity=mock(HttpEntity.class);
        String message= "test_String";

        Mockito.when(context.getBean("idpHttpClient",CloseableHttpClient.class))
                .thenReturn(httpClient);
        Mockito.when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockStatusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(entity);
        when(EntityUtils.toString(entity)).thenReturn("\"{\\\"success\\\":true}\"");


        stepRepository.getHttpResult(message);






    }





}