package com.fire.model;

public class LoginModel {

	public String email;
	public String pswd;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPswd() {
		return pswd;
	}
	public void setPswd(String pswd) {
		this.pswd = pswd;
	}
	
	
	@Override
	public String toString() {
		return "LoginModel [email=" + email + ", pswd=" + pswd + "]";
	}
	
}
