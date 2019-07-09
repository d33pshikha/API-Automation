package com.apitesting.utilities;

import static com.jayway.restassured.RestAssured.given;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jayway.restassured.response.Response;

public class APIMethods {

	
	private static final Logger log = LogManager.getLogger();

	public static Response method_POST(String requestBody, String requestURL) {

		// Printing Request Details
		log.debug("REQUEST-URL:POST-" + requestURL);
		log.debug("REQUEST-BODY:" + requestBody);

		// Extracting response after status code validation
		Response response = given().header("Content-Type", "application/json").request().body(requestBody)
				.post(requestURL).then().extract().response();

		// printing response
		log.info("RESPONSE:" + response.asString());

		return response;

	}
	
	public static Response method_GET(String requestURL) {

		
		// Printing Request Details
		log.debug("REQUEST-URL:POST-" + requestURL);
		
		// Extracting response after status code validation
		Response response = given().header("Content-Type", "application/json").request().get(requestURL).then()
				.extract().response();
		// printing response
		log.info("RESPONSE:" + response.asString());

		return response;

	}
	
	public static Response method_PUT(String requestURL) {

		
		// Printing Request Details
		log.debug("REQUEST-URL:POST-" + requestURL);
		
		// Extracting response after status code validation
		Response response = given().header("Content-Type", "application/json").request().put(requestURL).then()
				.extract().response();


		// printing response
		log.info("RESPONSE:" + response.asString());

		return response;

	}

}
