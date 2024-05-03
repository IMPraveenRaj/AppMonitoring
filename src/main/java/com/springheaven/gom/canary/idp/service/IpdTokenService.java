package com.springheaven.gom.canary.idp.service;

import com.springheaven.gom.canary.common.Exception.GomExecutionException;
import com.springheaven.gom.canary.ida.config.IdaProperties;

import com.springheaven.gom.canary.ida.service.IdaTokenService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class IpdTokenService {
    private final IdaTokenService tokenService;

    private final IdaProperties properties;

    public IpdTokenService(IdaTokenService tokenService, @Qualifier("idpIdaProperties") IdaProperties properties) {
        this.tokenService = tokenService;
        this.properties = properties;
    }

    public String createNewToken() throws GomExecutionException {


        return tokenService.createNewToken(properties);

    }

}
