package com.springheaven.gom.canary.ida.config;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdAnywhereDetails {
    private String clientId;
    private String resourceUri;
    private String tokenUrl;
    private String userName;
    private String password;
    private String timeOut;

}
