package com.nttdata.ojt.product.domain.response;

import com.nttdata.ojt.global.domain.AbstractResponse;

public class AdminAuthenticateResponse extends AbstractResponse {
	private boolean isAuthentic = false;

	public boolean isAuthentic() {
		return isAuthentic;
	}

	public void setAuthentic(boolean isAuthentic) {
		this.isAuthentic = isAuthentic;
	}

	public AdminAuthenticateResponse(boolean isAuthentic) {
		super();
		this.isAuthentic = isAuthentic;
	}
	
}
