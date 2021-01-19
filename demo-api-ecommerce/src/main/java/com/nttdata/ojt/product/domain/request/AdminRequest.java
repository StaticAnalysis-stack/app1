package com.nttdata.ojt.product.domain.request;

public class AdminRequest {
	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public AdminRequest() {}
	public AdminRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public boolean isNotEmpty() {
		if(username==null || username.equals("") || password==null || password.equals("") ) {
			return false;
		}
		return true;
	}
	
}
