package com.springheaven.gom.canary.testrunner;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OverallResult {

    private static final Gson gson= new Gson();
    private Map<String,String> input;
    private int result;

    private List<StepResultWrapper> steps;

    private String beginTime;
    private String endTime;

    private OverallResult(Map<String,String> input,int result,List<StepResultWrapper> steps,String beginTime,String endTime){
        this.input=input;
        this.result=result;
        this.steps=steps;
        this.beginTime=beginTime;
        this.endTime=endTime;

    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        private Map<String,String> input= new HashMap<>();

        private List<StepResultWrapper> stepResultWrappers= new ArrayList<>();

        private int result;
        private String beginTime;
        private String endTime;

        public Builder setStart(String start){
           this.beginTime=start;
           return this;
        }
      public Builder setEnd(String end){
            this.endTime=end;
            return  this;
      }

      public Builder addAllStepResult(List<StepResultWrapper> results){
            this.stepResultWrappers.addAll(results);
            return this;
      }

      public OverallResult buildSuccessfulResult(){
            this.result=0;
            return  new OverallResult(input,result,stepResultWrappers,beginTime,endTime);
      }

        public OverallResult buildUnSuccessfulResult(){
            this.result=0;
            return  new OverallResult(input,result,stepResultWrappers,beginTime,endTime);
        }
    }

    public String toJson(){
        return gson.toJson(this);
    }

    public static OverallResult fromJson(String json){
        return gson.fromJson(json,OverallResult.class);
    }

    public Map<String,String> getInput(){return input;}

    public String getBeginTime(){
        return beginTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public int getResult(){
        return  result;
    }

    public List<StepResultWrapper> getSteps(){
        return steps;
    }
}
