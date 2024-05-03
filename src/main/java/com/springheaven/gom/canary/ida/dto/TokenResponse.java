package com.springheaven.gom.canary.ida.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("error")
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;
    @JsonProperty("expires_in")
    private Integer tokenDuration;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("resource")
    private String resource;

}
