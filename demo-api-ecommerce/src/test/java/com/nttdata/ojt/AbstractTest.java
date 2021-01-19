package com.nttdata.ojt;
import javax.annotation.PostConstruct;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import com.nttdata.ecommerce.EcommerceApplication;

import io.restassured.RestAssured;

@SpringBootTest(classes=EcommerceApplication.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {
	@LocalServerPort
	private int port;
	
	
	public int getPort() {
		return port;
	}


	@PostConstruct
	public void initialize() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
		
	}
	
}
