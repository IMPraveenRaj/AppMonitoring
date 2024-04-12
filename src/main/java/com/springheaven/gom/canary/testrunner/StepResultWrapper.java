package com.springheaven.gom.canary.testrunner;

import java.util.HashMap;
import java.util.Map;

public class StepResultWrapper implements  Result{

    private String name;
    private Map<String,String> params;

    private int result;

    private String beginTime;

    private String details;

    private String endTime;


    private  StepResultWrapper(String name,Map<String,String> params,int result,String details,String beginTime,String endTime){
        this.name=name;
        this.params=params;
        this.result=result;
        this.details=details;
        this.beginTime=beginTime;
        this.endTime=endTime;
    }

    public String getDetails(){
        return details;
    }

    public int getResult(){
        return result;
    }

    public boolean isFailure(){
        return result == -1;
    }

    public static  Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private String name;

        private Map<String,String> input= new HashMap<>();

        private String start;

        private String end;

        public Builder setName(String name){
            if(name.equalsIgnoreCase("firstStep")){
                this.name="IDA_TOKEN_CREATION";
            } else if (name.equalsIgnoreCase("secondStep")) {
                this.name="FA_VALUED_HOLDING_UDM";
            }
            else {
                this.name="FA_TRANS_UDM";

            }

            return this;
        }

        public Builder setInput(Map<String,String> input){
            this.input=input;
            return this;
        }

        public Builder addInputParam(String key,String value){
            this.input.put(key, value);
            return this;
        }

        public Builder setStart(String start){
            this.start=start;
            return this;
        }

        public Builder setEnd(String end){
            this.end=end;
            return this;
        }

        public StepResultWrapper buildSuccessfulResult(StepResult result){
            return  new StepResultWrapper(name,input,result.getResult(),result.getDetails(),start,end);
        }


        public StepResultWrapper buildUnSuccessfulResult(String exceptionMessage){
            return  new StepResultWrapper(name,input,-1,exceptionMessage,start,end);
        }

    }

}
