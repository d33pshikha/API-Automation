package com.apitesting.testscripts;

import static org.hamcrest.Matchers.is;
import java.time.Instant;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.apitesting.staticData.Constants;
import com.apitesting.utilities.APIMethods;
import com.apitesting.utilities.JSON_Utilities;
import com.apitesting.utilities.PropertiesUtility;
import com.jayway.restassured.response.Response;

public class TakeTheOrderEndpoint extends PropertiesUtility {

	private static final Logger log = LogManager.getLogger();
	String orderID;
	Response response;
	String requestURL;

	@Test(priority = 1)
	public void verify_takeOrderToEndPoint_validData() {

		requestURL = url + orderEndpoint;
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + Constants.ORDER_JSON_FILEPATH);
		response = APIMethods.method_POST(requestBody, requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON Status Code validation
		response.then().statusCode(HttpStatus.SC_CREATED);

		// capturing created order ID
		orderID = response.then().extract().path("id").toString();
		log.info("Created New Order - ID - " + orderID);

		// Request Details for Fetch Order Details Endpoint

		String requestURL = url + orderEndpoint + "/" + orderID;
		Response response = APIMethods.method_GET(requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_OK);
		response.then().body("status", is(Constants.ORDER_STATUS_ASSIGNING));

		// Request Details
		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_take;
		response = APIMethods.method_PUT(requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_OK);
		response.then().body("status", is(Constants.ORDER_STATUS_ONGOING));

	}

	@Test(priority = 2)
	public void verify_takeOrderToEndPoint_invalidData() {

		orderID = String.valueOf(Instant.now().getEpochSecond());

		// Request Details
		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_take;
		response = APIMethods.method_PUT(requestURL);

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_NOT_FOUND);

	}
	
}
