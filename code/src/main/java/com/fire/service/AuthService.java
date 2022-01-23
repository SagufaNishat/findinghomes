package com.fire.service;

import java.util.concurrent.ExecutionException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.fire.model.LoginModel;
import com.fire.model.UserProfileModel;
import com.google.cloud.firestore.DocumentSnapshot;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class AuthService {

	public void addCookie(String key, String val, HttpServletResponse response) {
		Cookie cookie = new Cookie(key, val);
	    response.addCookie(cookie);
	}

	public boolean isAuthUser(LoginModel loginObj, FirestoreService fireService, HttpServletResponse response, CryptService cryptService) throws InterruptedException, ExecutionException, UnirestException {
		DocumentSnapshot doc = fireService.getDocument("FmlUserProfiles", loginObj.getEmail());
		if(doc.exists() && (loginObj.getPswd().equals(cryptService.decrypt(doc.getString("pswd"))))) { // is valid user
			addCookie("fmlUname", loginObj.getEmail(), response);
			
			return true;
		}
		
		return false;
	}
	
	public String getCookieValIfExists(String cookieKey, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for(Cookie c: cookies) {
	        	if(c.getName().equals(cookieKey)) {
	        		
	        		return c.getValue();
	        	}
	        }
	    }
	    
		return null;
	}
	
	/*public boolean isValidCookie(String cookieKey, String cookieValue) {
		Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for(Cookie c: cookies) {
	        	//if(c.getName().equals(cookieKey)) {
	        		System.out.println(c.getName() + "=" + c.getValue());
	        		
	        	//	return true;
	        	//}
	        }
	    }
	    
		return false;
	}*/
}
