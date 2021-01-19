package com.nttdata.ojt.global.domain;

public class ResponseError {
	private String errorMessage = "Error";

	public ResponseError() {}
	public ResponseError(String msg) { errorMessage = msg; }
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	
}
