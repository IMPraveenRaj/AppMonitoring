package com.springheaven.gom.canary.testrunner;

public class StepResult implements Result{

    private String details;
    private int result;

    public StepResult (String details, int result){
        this.details=details;
        this.result=result;
    }

    public static StepResult failed(String failureDetail){
        return new StepResult(failureDetail,-1);
    }

    public static StepResult successful(String details){
        return new StepResult(details,0);
    }

    public String getDetails(){
        return details;
    }

    public int getResult(){
        return result;
    }

}
