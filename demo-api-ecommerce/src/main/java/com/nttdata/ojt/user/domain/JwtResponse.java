package com.nttdata.ojt.user.domain;

import java.io.Serializable;

public class JwtResponse implements Serializable   {

	private final String token;
	private String firstName;
	private String middleName;
	private String lastName;
	public JwtResponse(String token, String firstName, String middleName, String lastName) {
		super();
		this.token = token;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getToken() {
		return token;
	}

	
}