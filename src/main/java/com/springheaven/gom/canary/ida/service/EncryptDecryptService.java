package com.springheaven.gom.canary.ida.service;



import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptDecryptService {

    private final StandardPBEStringEncryptor encryptor;

    public EncryptDecryptService(@Value("local-secret") String token){
        encryptor= new StandardPBEStringEncryptor();
        encryptor.setPassword(token);
    }

    public String getEncText(String text){ return  encryptor.encrypt(text);}


    public String getDecText(String text){ return  encryptor.decrypt(text);}


}
