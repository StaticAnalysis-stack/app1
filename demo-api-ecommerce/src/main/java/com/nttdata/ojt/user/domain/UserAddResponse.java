package com.nttdata.ojt.user.domain;

import com.nttdata.ojt.global.domain.AbstractResponse;

public class UserAddResponse  extends AbstractResponse {
	private String emailid;
	private String firstName;
	public String getEmailid() {
		return emailid;
	}
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	

}
