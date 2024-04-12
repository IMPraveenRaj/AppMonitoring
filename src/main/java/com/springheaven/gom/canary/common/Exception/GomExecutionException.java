package com.springheaven.gom.canary.common.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GomExecutionException extends  Exception{

private final String message;

public GomExecutionException(String message){
    super(message);
    this.message=message;
}
}
