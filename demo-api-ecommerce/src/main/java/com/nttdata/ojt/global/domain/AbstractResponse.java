package com.nttdata.ojt.global.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

public class AbstractResponse {
	private boolean hasError = false;
	private boolean isCompletelyServed = false;
	private List<ResponseError> errorList = new ArrayList<>();
	
	public boolean isHasError() {
		return hasError;
	}
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}
	public boolean isCompletelyServed() {
		return isCompletelyServed;
	}
	public void setCompletelyServed(boolean isCompletelyServed) {
		this.isCompletelyServed = isCompletelyServed;
	}
	public List<ResponseError> getErrorList() {
		return errorList;
	}
	public void addError(ResponseError error) {
		this.errorList.add(error);
	}
	
	
	
}
