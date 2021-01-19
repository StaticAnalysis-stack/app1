package com.example.medicinal.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("spring.datasource")
public class DBConfiguration {
	
	private String driverClassName;
	private String username;
	private String url;
	
	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	@Profile("dev")
	@Bean
	public String devDatabase() {
		System.out.println("Hello dev");
		System.out.println(username);
		System.out.println(url);
		return "dev";
	}

	@Profile("test")
	@Bean
	public String testDatabase() {
		System.out.println("Hello test");
		System.out.println(username);
		System.out.println(url);
		return "test";
	}

}
