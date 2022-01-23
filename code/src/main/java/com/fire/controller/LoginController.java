package com.fire.controller;

import java.util.concurrent.ExecutionException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fire.FirestoreselfApplication;
import com.fire.model.LoginModel;
import com.fire.model.UserProfileModel;
import com.fire.service.AuthService;
import com.fire.service.CryptService;
import com.fire.service.FirestoreService;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
public class LoginController {

	@Autowired
	FirestoreService fireService;
	
	@Autowired
	CryptService cryptService;
	
	@Autowired
	AuthService authService;
		
	@GetMapping({"/","/login"})
	public String login(Model model, HttpServletRequest request, @RequestParam(required=false,name="error") String error,
			@RequestParam(required=false,name="register") String register) throws UnirestException { // read cookie, ifExists redirect to home
		model.addAttribute("loginObj", new LoginModel());
		if(error!=null && error.equals("fail")) {
			model.addAttribute("error","Incorrect Email or password");
		}
		if(register!=null && register.equals("OK")) {
			model.addAttribute("error","Registered successfully! Login to continue.");
		}
		String cookieVal = authService.getCookieValIfExists(FirestoreselfApplication.UserIdCookie, request);
		if(cookieVal!=null) { 
			
			return "redirect:/home";
		}
		
		return "login";
	}
	
	@PostMapping("/login")
	public String auth(@ModelAttribute("loginObj") LoginModel loginObj, Model model, HttpServletResponse response) 
			throws InterruptedException, ExecutionException, UnirestException { 
		if(authService.isAuthUser(loginObj, fireService, response, cryptService)) {
			return "redirect:/home";
		}
		
		return "redirect:/login?error=fail";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletResponse response) {
		Cookie cookie = new Cookie(FirestoreselfApplication.UserIdCookie, null);
		cookie.setMaxAge(0);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		return "redirect:/login";
	}
	
}
