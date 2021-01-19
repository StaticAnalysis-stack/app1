package com.nttdata.ojt.user.domain;

import java.io.Serializable;

public class JwtRequest implements Serializable {

	
	
	private String emailid;
	private String password;
	
	//need default constructor for JSON Parsing
	public JwtRequest()
	{
		
	}
	
	

	public String getEmailid() {
		return emailid;
	}



	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}



	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}