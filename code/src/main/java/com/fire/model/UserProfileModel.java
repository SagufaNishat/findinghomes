package com.fire.model;

import com.google.cloud.firestore.DocumentSnapshot;


public class UserProfileModel {

	public String fName;
	public String lName;
	public String email;
	public String phone;
	public String gender;
	public String pswd;
	
	public UserProfileModel() {}
	
	public UserProfileModel(DocumentSnapshot document) {
		this.fName = document.getString("fName");
		this.lName = document.getString("lName");
		this.email = document.getString("email");
		this.phone = document.getString("phone");
		this.gender = document.getString("gender");
		this.pswd = document.getString("pswd");
	}
	
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPswd() {
		return pswd;
	}
	public void setPswd(String pswd) {
		this.pswd = pswd;
	}
	@Override
	public String toString() {
		return "UserProfileModel [fName=" + fName + ", lName=" + lName + ", email=" + email + ", phone=" + phone
				+ ", gender=" + gender + ", pswd=" + pswd + "]";
	}
	
	
		
}
