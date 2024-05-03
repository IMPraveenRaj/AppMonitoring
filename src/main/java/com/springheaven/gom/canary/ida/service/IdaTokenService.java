package com.springheaven.gom.canary.ida.service;

import com.springheaven.gom.canary.common.Exception.GomExecutionException;
import com.springheaven.gom.canary.ida.config.IdaProperties;
import com.springheaven.gom.canary.ida.dto.TokenResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class IdaTokenService {

private final IdaTokenGenerator tokenGenerator;

public  IdaTokenService(IdaTokenGenerator tokenGenerator){
    this.tokenGenerator=tokenGenerator;
}

public String createNewToken(IdaProperties properties) throws GomExecutionException{
    TokenResponse idaToken= tokenGenerator.getIdaToken(properties);

    if(!StringUtils.isBlank(idaToken.getError())){
        String errorMessage = String.format("Failed to create new IDA token %s\n"+ " Resource : %s,ClientId: %s",
                idaToken.getError(),properties.getResource(),properties.getClientId());
        throw  new GomExecutionException(errorMessage);
    }
return  idaToken.getAccessToken();
}

}
