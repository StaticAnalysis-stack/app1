package com.nttdata.ojt.global.domain;

import java.util.Locale;

public class AbstractRequest {
	private String locale;

	
	
	
	public AbstractRequest(String locale) {
		super();
		this.locale = locale;
	}
	public AbstractRequest() {
		super();
		this.locale = "en";
	}

	public Locale getLocale() {
		return new Locale(locale);
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
}
