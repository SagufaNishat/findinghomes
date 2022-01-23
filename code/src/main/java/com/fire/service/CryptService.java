package com.fire.service;

import java.net.URLEncoder;

import org.springframework.stereotype.Service;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class CryptService {
    
	//GCP FUNCTIONS
	
    public String encrypt(String plainText) throws UnirestException {
    	
    	return Unirest.get("https://us-central1-firestorefb.cloudfunctions.net/function-1?pswd="+URLEncoder.encode(plainText)+"&type=encrypt")
    			.asString().getBody();
    }

    public String decrypt(String encryptedText) throws UnirestException {
    	
    	return Unirest.get("https://us-central1-firestorefb.cloudfunctions.net/function-1?pswd="+URLEncoder.encode(encryptedText)+"&type=decrypt")
    			.asString().getBody();
    }
}
