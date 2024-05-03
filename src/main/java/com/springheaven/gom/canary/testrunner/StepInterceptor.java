package com.springheaven.gom.canary.testrunner;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Aspect
public class StepInterceptor {

    public Result aroundAdvice(ProceedingJoinPoint joinPoint){
        StepResultWrapper stepResultWrapper;
        StepResultWrapper.Builder resultBuilder= StepResultWrapper.builder();
        try{
            resultBuilder.setName(joinPoint.getSignature().getName());
            resultBuilder.setStart(Instant.now().toString());


            CodeSignature methodSignature =(CodeSignature) joinPoint.getSignature();
            String[] sigParamNames= methodSignature.getParameterNames();
            Object[] args= joinPoint.getArgs();
            for(int i=0;i<sigParamNames.length;i++){
                resultBuilder.addInputParam(sigParamNames[i],args[i].toString() );
            }
            StepResult result= (StepResult) joinPoint.proceed();
            stepResultWrapper=resultBuilder.setEnd(Instant.now().toString()).buildSuccessfulResult(result);
        }catch (Throwable throwable){
            stepResultWrapper=resultBuilder.setEnd(Instant.now().toString()).buildUnSuccessfulResult(throwable.getMessage());
        }

        return  stepResultWrapper;
    }

}
