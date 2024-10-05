package com.springheaven.gom.canary.service;

import com.springheaven.gom.canary.common.Exception.GomExecutionException;
import com.springheaven.gom.canary.common.dto.CanaryIdpData;
import com.springheaven.gom.canary.ida.dto.TokenResponse;
import com.springheaven.gom.canary.ida.service.IdaTokenService;
import com.springheaven.gom.canary.idp.config.IdpApiConfig;
import com.springheaven.gom.canary.idp.service.IpdTokenService;
import com.springheaven.gom.canary.testrunner.Result;
import com.springheaven.gom.canary.testrunner.Step;
import com.springheaven.gom.canary.testrunner.StepResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class StepRepository {

    private String dataSet,previousWorkingDayDate;

    private LocalDateTime currentDateTime= LocalDateTime.now();

    private String clEcid,accountCaid;

    TokenResponse accessTokenResponse;


    private final Environment env;

    private final IpdTokenService ipdIdpTokenService;

    private  final CanaryIdpData canaryIdpData;

    private IdpApiConfig apiConfig;

    private RestTemplate restTemplate;

    private final ApplicationContext context;

    private String restUrl;

    private String message;

    private String tokenCheck;


    public StepRepository(@Qualifier("idaRestTemplate")RestTemplate restTemplate, IdaTokenService tokenService, final
                          Environment env, IpdTokenService idptokenService, IdpApiConfig apiConfig, ApplicationContext context){

        this.restTemplate=restTemplate;
        this.env=env;
        this.canaryIdpData=new CanaryIdpData();
        this.ipdIdpTokenService=idptokenService;
        this.apiConfig=apiConfig;
        this.context=context;
    }


    public void setParameters(){
        clEcid="'022074473'";
        accountCaid=env.getProperty("ipd.qs.canary.accountCaid");
    }

    public void setDateTime(){
        if(currentDateTime.getDayOfWeek().toString()=="MONDAY"){
            currentDateTime=currentDateTime.minusHours(72);

        } else if (currentDateTime.getDayOfWeek().toString()=="SUNDAY") {

            currentDateTime=currentDateTime.minusHours(48);
        } else if (currentDateTime.getDayOfWeek().toString()=="SATURDAY") {
            currentDateTime=currentDateTime.minusHours(24);
        }else{
            currentDateTime=currentDateTime.minusHours(24);
        }
        previousWorkingDayDate=currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public LocalDateTime getCurrentDateTime(){
        return currentDateTime;
    }

    @Step
    public Result firstStep(){
        try{
            setParameters();
            log.info("\n\n-------Generating IDA Token fro IDP------");
            String idpToken=ipdIdpTokenService.createNewToken();
            canaryIdpData.setIdpToken(idpToken);
            tokenCheck=idpToken;
            setDateTime();
            log.info("\n\n======IDA Token Generated Successfully");
            return StepResult.successful("Generated IDA token Successfully");
        }catch (GomExecutionException e){
            log.error("\n\n=======Failure: IDA Token GEN========"+e.getMessage());
            return StepResult.failed(e.getMessage());
        }


    }

    @Step
    public Result secondStep() {
        try {
            setParameters();
            log.info("\n\n-------Generating IDA Token fro IDP------");
            String idpToken = ipdIdpTokenService.createNewToken();
            canaryIdpData.setIdpToken(idpToken);
            tokenCheck = idpToken;
            setDateTime();
            log.info("\n\n======IDA Token Generated Successfully");
            return StepResult.successful("Generated IDA token Successfully");
        } catch (GomExecutionException e) {
            log.error("\n\n=======Failure: IDA Token GEN========" + e.getMessage());
            return StepResult.failed(e.getMessage());
        }
    }


    @Step
        public Result thirdStep(){
            try{
                setParameters();
                log.info("\n\n-------Generating IDA Token fro IDP------");
                String idpToken=ipdIdpTokenService.createNewToken();
                canaryIdpData.setIdpToken(idpToken);
                tokenCheck=idpToken;
                setDateTime();
                log.info("\n\n======IDA Token Generated Successfully");
                return StepResult.successful("Generated IDA token Successfully");
            }catch (GomExecutionException e){
                log.error("\n\n=======Failure: IDA Token GEN========"+e.getMessage());
                return StepResult.failed(e.getMessage());
            }


    }

    public  Result getHttpResult(final String messageBody){


        restUrl="https://fa-reporting-isdpservice.jpmchase.net:8845/isdp-rest/querysvc/query";

        try(CloseableHttpClient httpClient= context.getBean("idpHttpClient",CloseableHttpClient.class)){
            HttpPost request=  new HttpPost(restUrl);
            request.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
            request.setHeader("Content-Type",MediaType.APPLICATION_JSON_VALUE);
            request.setHeader("Authorization","Bearer "+canaryIdpData.getIdpToken());
            org.apache.http.HttpEntity entity= new StringEntity(messageBody);
            request.setEntity(entity);
            try(CloseableHttpResponse response=httpClient.execute(request)){
                int statusCode=response.getStatusLine().getStatusCode();
                message = EntityUtils.toString(response.getEntity());
                log.info("IDP response :{}",message);
                if(statusCode==200){
                    return StepResult.successful(" Dataset"+dataSet+"IDp Processed status code is "+String.valueOf(response.getStatusLine().getStatusCode()));
                }else {
                    return  StepResult.failed("status code Received from IDP "+statusCode+"Result"+message);
                }
            }
        }catch (IOException e){
            log.error(ExceptionUtils.getStackTrace(e));
            return StepResult.failed("Exception while getting response from IDP"+e.getMessage());
        }

    }

}
