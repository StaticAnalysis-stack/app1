package com.nttdata.ojt.user.entity;

public class UserDTO {
	private String emailid;
	private String password;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNumber;

	

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public boolean isNotEmpty() {
		if(emailid==null || password==null || password==null  || firstName==null  || middleName==null  || lastName==null|| mobileNumber==null) {
			return false;
		}
		return true;
	}
	
}