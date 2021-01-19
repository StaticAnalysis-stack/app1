package com.test.nttdata.ojt.user;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import com.nttdata.ojt.AbstractTest;
import com.nttdata.ojt.LocalToken;
import com.nttdata.ojt.user.UserApiEndpoint;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.minidev.json.JSONObject;

public class UserAPITest extends AbstractTest {

	public String getToken() {
		String token = LocalToken.getToken(getPort(), "admin@nttdata.com", "Admin@123");
		return token;
	}
	
	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/add_dummy_user.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/revert_by_deletingUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testUserAvailableNotEmpty() {
		/*
		 * Testing if the User list is not emptyy
		 */
		
	RestAssured.given().header("Authorization","Bearer "+getToken()).when().get(UserApiEndpoint.GETALLUSER).then().body("users", not(empty()));
	}

	
	@Test
	@Sql(scripts = "classpath:userScripts/add_dummy_user.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/revert_by_deletingUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testUserAlreadyExist() {
		JSONObject param = new JSONObject();
		
		param.put("emailid", "vishaldeshmukh9571@gmail.com");
		param.put("password", "Password@1");
		param.put("firstName", "Vishal");
		param.put("middleName", "Atul");
		param.put("lastName", "Deshmukh");
		param.put("mobileNumber", "9168231771");
		Response response = RestAssured.given().contentType(ContentType.JSON).body(param.toJSONString())
				.post(UserApiEndpoint.ADDUSER);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_BAD_REQUEST, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(false, completelyServed);
		assertEquals(true, hasError);
		
	}
	
	
	  @Test
	  @Sql(scripts = "classpath:userScripts/revert_by_deletingUser.sql",
	  executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	  
	  void testUserCreated()
	  { 
	  JSONObject param = new JSONObject();
	  param.put("emailid",	  "vishaldeshmukh9571@gmail.com");
	  param.put("password", "Password@1");
	  param.put("firstName", "Vishal");
	  param.put("middleName", "Atul");
	  param.put("lastName", "Deshmukh");
	  param.put("mobileNumber", "9168231771"); 
	  Response response = RestAssured.given().contentType(ContentType.JSON).body(param.toJSONString())
		.post(UserApiEndpoint.ADDUSER); int status = response.getStatusCode();
	  assertEquals(org.apache.http.HttpStatus.SC_ACCEPTED, status); 
	  boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
	  boolean hasError = response.jsonPath().getBoolean("hasError");
	  assertEquals(true, completelyServed); assertEquals(false, hasError);
	  }
	  
	  
		@Test
		void testEmptyAttributeNameOfUserCreateApi() {
			JSONObject param = new JSONObject();
			param.put("emailid",	  "vishaldeshmukh9571@gmail.com");
			  param.put("password", "Password@1");
			  param.put("firstName", "Vishal");
			  param.put("middleName", "Atul");
			  param.put("mobileNumber", "9168231771"); 
			  Response response = RestAssured.given().contentType(ContentType.JSON).body(param.toJSONString())
					.post(UserApiEndpoint.ADDUSER);
			int status = response.getStatusCode();
			assertEquals(org.apache.http.HttpStatus.SC_BAD_REQUEST, status);
			boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
			boolean hasError = response.jsonPath().getBoolean("hasError");
			assertEquals(false, completelyServed);
			assertEquals(true, hasError);
		}
		
		@Test
		@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = "classpath:userScripts/add_dummy_product.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = "classpath:userScripts/revert_by_deleting_product_assigned.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		@Sql(scripts = "classpath:userScripts/revert_by_deleting.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		void testAssigningProduct() {
			JSONObject param = new JSONObject();
			param.put("pCode", "testPCode");
			param.put("emailId", "admin@nttdata.com");
			param.put("quantity", 12);
			param.put("vCode", "vcode");
			Response response = RestAssured.given().contentType(ContentType.JSON).body(param.toJSONString()).header("Authorization","Bearer "+getToken())
					.post(UserApiEndpoint.ASSIGNPRODUCT);
			int status = response.getStatusCode();
			assertEquals(org.apache.http.HttpStatus.SC_OK, status);
			boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
			boolean hasError = response.jsonPath().getBoolean("hasError");
			assertEquals(true, completelyServed);
			assertEquals(false, hasError);
		}
		
		@Test
		@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		void testAttributeMissingAssigningProduct() {
			JSONObject param = new JSONObject();
			param.put("pCode", "testPCode");
			param.put("emailId", "testMail");
			param.put("variantCode", "vcode");
			Response response = RestAssured.given().contentType(ContentType.JSON).body(param.toJSONString()).header("Authorization","Bearer "+getToken())
					
					.post(UserApiEndpoint.ASSIGNPRODUCT);
			int status = response.getStatusCode();
			assertEquals(org.apache.http.HttpStatus.SC_BAD_REQUEST, status);
			boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
			boolean hasError = response.jsonPath().getBoolean("hasError");
			assertEquals(false, completelyServed);
			assertEquals(true, hasError);
		}
		
		@Test
		@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = "classpath:userScripts/add_dummy_product_assign.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		void testDeleteUserProduct() {
			JSONObject param = new JSONObject();
			param.put("pCode", "testPCode");
			param.put("emailId", "admin@nttdata.com");
			param.put("quantity", 10);
			param.put("vCode", "vcode");
			Response response = RestAssured.given().contentType(ContentType.JSON).body(param.toJSONString()).header("Authorization","Bearer "+getToken())
					.post(UserApiEndpoint.DELETEASSIGNPRODUCT);
			int status = response.getStatusCode();
			assertEquals(org.apache.http.HttpStatus.SC_OK, status);
			boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
			boolean hasError = response.jsonPath().getBoolean("hasError");
			assertEquals(true, completelyServed);
			assertEquals(false, hasError);
		}
}
