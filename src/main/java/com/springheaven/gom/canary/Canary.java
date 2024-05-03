package com.springheaven.gom.canary;

import com.springheaven.gom.canary.service.StepRepository;
import com.springheaven.gom.canary.testrunner.OverallResult;
import com.springheaven.gom.canary.testrunner.StepResult;
import com.springheaven.gom.canary.testrunner.StepResultWrapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

@Component
public class Canary {
    private static final Logger logger= LoggerFactory.getLogger(Canary.class);

    @Autowired
    private StepRepository stepRepository;
@PostConstruct
    public void init(){
//initialize the configuration  eg> crateate db connection set access certification
    }

    /**
     * logic for running all the tests and gathering the final result*/

    public String runAllTests(){
        OverallResult.Builder resultBuilder= OverallResult.builder();

        try{
            resultBuilder.setStart(Instant.now().toString());

            ArrayList<StepResultWrapper> results=getAllTestResults();

            resultBuilder.addAllStepResult(results);
            if(results.stream().anyMatch(StepResultWrapper::isFailure)){
                return resultBuilder.setEnd(Instant.now().toString()).buildUnSuccessfulResult().toJson();

            }
            return resultBuilder.setEnd(Instant.now().toString()).buildSuccessfulResult().toJson();


        }catch (Exception e){

            logger.error("Exception while running canary--{}",e.getMessage());
            return resultBuilder.setEnd(Instant.now().toString()).buildUnSuccessfulResult().toJson();

        }

    }
    private ArrayList<StepResultWrapper> getAllTestResults()throws IOException{
        ArrayList<StepResultWrapper> results=new ArrayList<>();
        //TODO : add your steps to run here
        results.add((StepResultWrapper) stepRepository.firstStep());
        results.add((StepResultWrapper) stepRepository.secondStep());
        results.add((StepResultWrapper) stepRepository.thirdStep());



        return  results;
    }
}
