package com.springheaven.gom.canary.common.service;


import com.fasterxml.jackson.databind.util.ExceptionUtil;
import com.springheaven.gom.canary.ida.service.EncryptDecryptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Optional;

@Service
@Slf4j
public class SSLHelperService {



    public Optional<SSLContext> createSslContext(String keyStoreFile, String encToken, EncryptDecryptService decryptService){


        final String decToken= decryptService.getDecText(encToken);

        try(InputStream in = getClass().getClassLoader().getResourceAsStream(keyStoreFile)){

            char[] tokenChars= decToken.toCharArray();
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(in,tokenChars);

            KeyManagerFactory keyManagerFactory= KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

            keyManagerFactory.init(keyStore,tokenChars);

            TrustManagerFactory trustManagerFactory= TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            if(trustManagerFactory.getTrustManagers().length==0){
                return Optional.empty();
            }

            X509TrustManager trustManager =(X509TrustManager) trustManagerFactory.getTrustManagers()[0];
            if(trustManager.getAcceptedIssuers().length==0){
                return  Optional.empty();
            }

            SSLContext sslContext= SSLContext.getInstance("TLSV1.3");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),new SecureRandom());
            return  Optional.of(sslContext);




        }catch (IOException | KeyManagementException | KeyStoreException | CertificateException |
                UnrecoverableKeyException | NoSuchAlgorithmException e){
            log.error(ExceptionUtils.getStackTrace(e));
            return  Optional.empty();

        }


    }

}
