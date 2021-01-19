package com.nttdata.ojt;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;

public class LocalToken {
	public static String getToken(int port,String emailid,String password) {
		
		String prt= Integer.toString(port); 
		RestAssured.baseURI = "http://localhost:"+prt;

		RequestSpecification request = RestAssured.given();

		//String payload = "{\r\n" + "  \"emailid\": \"sagar@gmail.com\",\r\n" + "  \"password\": \"password\"\r\n" + "}";
		JSONObject payload = new JSONObject();
		payload.put("emailid",emailid);
		payload.put("password", password);
		request.header("Content-Type", "application/json");

		Response responseFromGenerateToken = request.body(payload.toJSONString()).post("/users/authenticate");

		String jsonString = responseFromGenerateToken.getBody().asString();

		String tokenGenerated = JsonPath.from(jsonString).get("token");

		return tokenGenerated;
	}
	
}
