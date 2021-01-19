package com.nttdata.ojt.product.variant;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.nttdata.ojt.AbstractTest;
import com.nttdata.ojt.LocalToken;
import com.nttdata.ojt.global.domain.ResponseError;
import com.nttdata.ojt.product.ProductApiEndpoint;
import com.nttdata.ojt.product.exceptions.ProductAlreadyPresentException;
import com.nttdata.ojt.product.variant.endpoint.VariantEndPoint;
import com.nttdata.ojt.product.variant.entity.Variant;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;

public class VariantApiTest extends AbstractTest{
	
	private static final String SC_CREATED = "INSERTED SUCCESSFULLY";
	
	
	public String getToken() {
		String token = LocalToken.getToken(getPort(), "admin@nttdata.com", "Admin@123");
		return token;
	}

		
	
	
	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts= "classpath:variantScripts/add_dummy_product.sql",executionPhase=ExecutionPhase.BEFORE_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/revert_by_deleting.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/revert_by_deleting_product_variant.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/1st_revert.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/revert_by_deleting_variant_product.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testVariantAddStatusCode() {
		JSONObject param = new JSONObject();
		param.put("vCode", "testVariantCode");
		param.put("vDesc", "description");
		param.put("vColor", "tesCode");
		param.put("vStock", 10);
		param.put("vSize", "remappingFunction");
		param.put("pCode", "testpCode");

		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken()).contentType(ContentType.JSON).body(param.toJSONString())
				.post(VariantEndPoint.CREATE_VARIANT);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_OK, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(true, completelyServed);
		assertEquals(false, hasError);
	}
	

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts= "classpath:variantScripts/1st.sql",executionPhase=ExecutionPhase.BEFORE_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/add_dummy_product.sql",executionPhase=ExecutionPhase.BEFORE_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/revert_by_deleting.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/revert_by_deleting_product_variant.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/1st_revert.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/revert_by_deleting_variant_product.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testVariantAlreadyPresent() {
		JSONObject param = new JSONObject();
		param.put("vCode", "testVariantCode");
		param.put("vDesc", "description");
		param.put("vColor", "tesCode");
		param.put("vStock", 10);
		param.put("vSize", "remappingFunction");
		param.put("pCode", "testpCode");

		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken()).contentType(ContentType.JSON).body(param.toJSONString())
				.post(VariantEndPoint.CREATE_VARIANT);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_BAD_REQUEST, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(false, completelyServed);
		assertEquals(true, hasError);
	}
	
	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts= "classpath:variantScripts/add_dummy_product.sql",executionPhase=ExecutionPhase.BEFORE_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/revert_by_deleting.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/revert_by_deleting_product_variant.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/1st_revert.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/revert_by_deleting_variant_product.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testVariantAttributeMissing() {
		JSONObject param = new JSONObject();
		param.put("vCode", "testVariantCode");
		param.put("vDesc", "description");
		param.put("vColor", "tesCode");
		param.put("vStock", 10);
		param.put("pCode", "testpCode");

		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken()).contentType(ContentType.JSON).body(param.toJSONString())
				.post(VariantEndPoint.CREATE_VARIANT);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_BAD_REQUEST, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(false, completelyServed);
		assertEquals(true, hasError);
	}


	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts= "classpath:variantScripts/1st.sql",executionPhase=ExecutionPhase.BEFORE_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/1st_revert.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testVariantNotEmpty() {
			   
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken()).contentType(ContentType.JSON)
				.get(VariantEndPoint.GET_VARIANTS);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_OK, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(true, completelyServed);
		assertEquals(false, hasError);
	}	
		
	
	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts= "classpath:variantScripts/1st.sql",executionPhase=ExecutionPhase.BEFORE_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/add_dummy_variant_product.sql",executionPhase=ExecutionPhase.BEFORE_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/1st_revert.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts= "classpath:variantScripts/revert_by_deleting_variant_product.sql",executionPhase=ExecutionPhase.AFTER_TEST_METHOD )
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testGetVariantByPcode() {
		RestAssured.given().header("Authorization", "Bearer " + getToken()).when()
				.get(VariantEndPoint.GET_PRODUCT_VARIANTS + "?pCode=testpCode").then()
				.statusCode(org.apache.http.HttpStatus.SC_OK).body("variant", not(empty()))
				.body("completelyServed", equalTo(true)).body("hasError", equalTo(false));
	}
	
	
	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testGetVariantByPcodeParameterMissing() {
		RestAssured.given().header("Authorization", "Bearer " + getToken()).when()
				.get(VariantEndPoint.GET_PRODUCT_VARIANTS + "?pCode=").then()
				.statusCode(org.apache.http.HttpStatus.SC_BAD_REQUEST).body("variant", not(empty()))
				.body("completelyServed", equalTo(false)).body("hasError", equalTo(true));
	
	
	}
	
	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testGetVariantByPcodeNotPresent() {
		RestAssured.given().header("Authorization", "Bearer " + getToken()).when()
				.get(VariantEndPoint.GET_PRODUCT_VARIANTS + "?pCode=test").then()
				.statusCode(org.apache.http.HttpStatus.SC_BAD_REQUEST).body("variant", not(empty()))
				.body("completelyServed", equalTo(false)).body("hasError", equalTo(true));
	
	
	}
	
}


