package com.springheaven.gom.canary.service;

import com.springheaven.gom.canary.common.Exception.GomExecutionException;
import com.springheaven.gom.canary.testrunner.Result;
import com.springheaven.gom.canary.testrunner.StepResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    private final IdpTokenService ipdIdpTokenService;

    private  final CanaryIdpData canaryIdpData;

    private IdpApiConfig apiConfig;

    private RestTemplate restTemplate;

    private final ApplicationContext context;

    private String restUrl;

    private String message;

    private String tokenCheck;


    public StepRepository(@Qualifier("idaRestTemplate")RestTemplate restTemplate,IdaTokenService tokenService,final
                          Environment env,IdptokenService idptokenService,IdpApiConfig apiConfig,ApplicationContext context){

        this.restTemplate=restTemplate;
        this.env=env;
        this.canaryIdpData=new CanaryIdpData();
        this.ipdIdpTokenService=idptokenService;
        this.apiConfig=apiConfig;
        this.context=context;
    }


    public void setParameters(){
        clEcid='022074473';
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

}
