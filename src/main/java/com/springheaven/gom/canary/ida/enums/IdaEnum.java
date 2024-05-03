package com.springheaven.gom.canary.ida.enums;

public enum IdaEnum {

    CLIENT_ID_PARAM("client_id"),

    P_GRANT_TYPE("grant_type"),

    OPENID_SCOPE("openid"),

    USERNAME_PARAM("username"),

    PASS_PARAM("password"),

    RESOURCE_PARAM("resource"),

    SCOPE_PARAM("scope");

    private final  String value;


    IdaEnum(String value){ this.value=value;}

    @Override
    public  String toString(){
        return value;
    }
}
