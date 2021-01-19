package com.nttdata.ojt.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.nttdata.ojt.AbstractTest;
import com.nttdata.ojt.LocalToken;
import com.nttdata.ojt.product.controller.ProductController;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;

import static org.hamcrest.Matchers.*;

class ProductApiTest extends AbstractTest {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	public String getToken() {
		String token = LocalToken.getToken(getPort(), "admin@nttdata.com", "Admin@123");
		return token;
	}

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/add_dummy_product.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/revert_by_deleting.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testProductsAvailableNotEmpty() {
		/*
		 * Testing if the product list is not emptyy
		 */
		RestAssured.given().header("Authorization", "Bearer " + getToken()).when().get(ProductApiEndpoint.LIST_PRODUCTS)
				.then().body("products", not(empty()));
	}

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/revert_by_deleting.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testProductCreated() {
		JSONObject param = new JSONObject();
		param.put("pCode", "testPCode");
		param.put("pDesc", "description");
		param.put("pName", "name");
		param.put("imageUrl", "www.test.com");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.CREATE_PRODUCT);
		int status = response.getStatusCode();
		
		System.out.println("bodyTag :"+response.body().asString());
		assertEquals(org.apache.http.HttpStatus.SC_OK, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(true, completelyServed);
		assertEquals(false, hasError);
	}

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/add_dummy_product.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)

	@Sql(scripts = "classpath:scripts/revert_by_deleting.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testProductAlreadyExist() {
		JSONObject param = new JSONObject();
		param.put("pCode", "testPCode");
		param.put("pDesc", "description");
		param.put("pName", "name");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.CREATE_PRODUCT);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_BAD_REQUEST, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(false, completelyServed);
		assertEquals(true, hasError);
	}

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testEmptyAttributeNameOfProductCreateApi() {
		JSONObject param = new JSONObject();
		param.put("pCode", "testPCode");
		param.put("pDesc", "description");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.CREATE_PRODUCT);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_BAD_REQUEST, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(false, completelyServed);
		assertEquals(true, hasError);
	}

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/add_dummy_product.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/add_dummy_user.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/add_dummy_variant.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	
	@Sql(scripts = "classpath:scripts/revert_by_deleting_variant.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/revert_by_deleting_product_assigned.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/revert_by_deleting.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/revert_by_deleting_user.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testAssigningProduct() {
		JSONObject param = new JSONObject();
		param.put("pCode", "testPCode");
		param.put("emailId", "testMail");
		param.put("quantity", 12);
		param.put("vCode", "testVCode");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.ASSIGN_PRODUCT);
		
		int status = response.getStatusCode();
		logger.warn("status code is"+status);
		assertEquals(org.apache.http.HttpStatus.SC_OK, status);
		System.out.print("bodyTag : "+response.body().asString());
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(true, completelyServed);
		assertEquals(false, hasError);
	}
	
	
	
	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/add_dummy_product.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/add_dummy_user.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/add_dummy_variant_for_fail.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	
	@Sql(scripts = "classpath:scripts/revert_by_deleting_variant.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/revert_by_deleting_product_assigned.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/revert_by_deleting.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/revert_by_deleting_user.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testAssigningProductStockFail() {
		JSONObject param = new JSONObject();
		param.put("pCode", "testPCode");
		param.put("emailId", "testMail");
		param.put("quantity", 12);
		param.put("vCode", "testVCode");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.ASSIGN_PRODUCT);
		
		int status = response.getStatusCode();
		logger.warn("status code is"+status);
		assertEquals(org.apache.http.HttpStatus.SC_BAD_REQUEST, status);
		System.out.print("bodyTag : "+response.body().asString());
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(false, completelyServed);
		assertEquals(true, hasError);
	}
	
	

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testAttributeMissingAssigningProduct() {
		JSONObject param = new JSONObject();
		param.put("pCode", "testPCode");
		param.put("emailId", "testMail");
		param.put("vCode", "vcode");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.ASSIGN_PRODUCT);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_BAD_REQUEST, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(false, completelyServed);
		assertEquals(true, hasError);
	}

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/add_dummy_product.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)

	@Sql(scripts = "classpath:scripts/add_dummy_product_assign.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)

	@Sql(scripts = "classpath:scripts/revert_by_deleting_product_assigned.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)

	@Sql(scripts = "classpath:scripts/revert_by_deleting.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testGetOrdersFetchesTheUserOrders() {
		RestAssured.given().header("Authorization", "Bearer " + getToken()).when()
				.get(ProductApiEndpoint.GET_ORDERS + "?emailId=testMail").then()
				.statusCode(org.apache.http.HttpStatus.SC_OK).body("products", not(empty()))
				.body("completelyServed", equalTo(true)).body("hasError", equalTo(false));
	}

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testGetOrdersMissingAttr() {
		RestAssured.given().header("Authorization", "Bearer " + getToken()).when()
				.get(ProductApiEndpoint.GET_ORDERS + "?emailId=").then()
				.statusCode(org.apache.http.HttpStatus.SC_BAD_REQUEST).body("products", nullValue())
				.body("completelyServed", equalTo(false)).body("hasError", equalTo(true));
	}

	@Test
	
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/add_dummy_product.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)

	@Sql(scripts = "classpath:scripts/revert_by_deleting_product_variant.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)

	@Sql(scripts = "classpath:scripts/revert_by_deleting.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testAssignProductVariant() {
		JSONObject param = new JSONObject();
		param.put("pCode", "testPCode");
		param.put("vCode", "testVariantCode");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.ASSIGN_VARIANT);
		int status = response.getStatusCode();
		System.out.println(response.body().asString());
		assertEquals(org.apache.http.HttpStatus.SC_OK, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(true, completelyServed);
		assertEquals(false, hasError);
	}

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testAssignProductVariantButProductNotPresent() {
		JSONObject param = new JSONObject();
		param.put("pCode", "testPCode");
		param.put("vCode", "testVariantCode");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.ASSIGN_VARIANT);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_OK, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(false, completelyServed);
		assertEquals(true, hasError);
	}

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testAssignProductVariantMissingAttr() {
		JSONObject param = new JSONObject();
		param.put("vCode", "testVariantCode");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.ASSIGN_VARIANT);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_BAD_REQUEST, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(false, completelyServed);
		assertEquals(true, hasError);
	}

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:scripts/add_dummy_product.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)

	@Sql(scripts = "classpath:scripts/add_dummy_product_variant.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)

	@Sql(scripts = "classpath:scripts/revert_by_deleting.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)	
	void testDeleteProductVariant() {
		JSONObject param = new JSONObject();
		param.put("pCode", "testPCode");
		param.put("vCode", "testVariantCode");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.DELETE_VARIANT);
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
	void testDeleteProductVariantMissingAttr() {
		JSONObject param = new JSONObject();
		param.put("pCode", "testPCode");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.DELETE_VARIANT);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_BAD_REQUEST, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(false, completelyServed);
		assertEquals(true, hasError);
	}

	@Test
	@Sql(scripts = "classpath:userScripts/addUser.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:userScripts/removeUser.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void testDeleteProductVariantNotFound() {
		JSONObject param = new JSONObject();
		param.put("pCode", "testPCode1");
		param.put("vCode", "testVariantCode1");
		Response response = RestAssured.given().header("Authorization", "Bearer " + getToken())
				.contentType(ContentType.JSON).body(param.toJSONString()).post(ProductApiEndpoint.DELETE_VARIANT);
		int status = response.getStatusCode();
		assertEquals(org.apache.http.HttpStatus.SC_OK, status);
		boolean completelyServed = response.jsonPath().getBoolean("completelyServed");
		boolean hasError = response.jsonPath().getBoolean("hasError");
		assertEquals(false, completelyServed);
		assertEquals(true, hasError);
	}

}
