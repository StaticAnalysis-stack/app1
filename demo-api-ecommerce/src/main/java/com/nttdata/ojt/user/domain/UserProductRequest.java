package com.nttdata.ojt.user.domain;

import com.nttdata.ojt.global.domain.AbstractRequest;

public class UserProductRequest extends AbstractRequest {
		private int id;
	    
	    private String pCode;
	    
	    private String emailId;
	    
	    private String vCode;
	    
	    private int quantity;

		public UserProductRequest() {
			super();
		}

		public UserProductRequest(int id, String pCode, String emailId, String variantCode, int quantity)  {
			super();
			this.id = id;
			this.pCode = pCode;
			this.emailId = emailId;
			this.vCode = variantCode;
			this.quantity = quantity;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getpCode() {
			return pCode;
		}

		public void setpCode(String pCode) {
			this.pCode = pCode;
		}

		public String getEmailId() {
			return emailId;
		}

		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}

		

		public String getvCode() {
			return vCode;
		}

		public void setvCode(String vCode) {
			this.vCode = vCode;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
	    
		 public boolean isNotEmpty() {
		        if(pCode==null || pCode.equals("") 
		                || emailId==null || emailId.equals("") 
		                || vCode==null || vCode.equals("")
		                || quantity==0
		                )
		        {
		            return false;
		        }
		        return true;
		    }
}
