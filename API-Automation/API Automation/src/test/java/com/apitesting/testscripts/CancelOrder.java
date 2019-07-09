package com.apitesting.testscripts;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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

public class CancelOrder extends PropertiesUtility {

	private static final Logger log = LogManager.getLogger();
	String orderID;
	Response response;
	String requestURL;

	@Test(priority = 4)
	public void verify_CancelOrder_violatedFlow() {
		// Place Order
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

		// Request Details
		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_take;
		response = APIMethods.method_PUT(requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_OK);
		response.then().body("status", is(Constants.ORDER_STATUS_ONGOING));

		// Request Details
		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_complete;
		response = APIMethods.method_PUT(requestURL);

		// JSON response Pay load validations
		response.then().body("status", is(Constants.ORDER_STATUS_COMPLETED));

		// Request Details
		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_cancel;
		response = APIMethods.method_PUT(requestURL);

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);

	}

	@Test(priority = 1)
	public void verify_CancelOrder() {

		// Place order
		requestURL = url + orderEndpoint;
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + Constants.ORDER_JSON_FILEPATH);
		Response response = APIMethods.method_POST(requestBody, requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON Status Code validation
		response.then().statusCode(HttpStatus.SC_CREATED);

		// capturing created order ID
		orderID = response.then().extract().path("id").toString();
		log.info("Created New Order - ID - " + orderID);

		// Request Details for Cancel Endpoint

		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_cancel;
		response = APIMethods.method_PUT(requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_OK);
		response.then().body("status", is(Constants.ORDER_STATUS_CANCELLED));
		response.then().body("cancelledAt", notNullValue());

	}

	@Test(priority = 2)
	public void verify_onGoingToCancelOrder() {

		// Place order
		requestURL = url + orderEndpoint;
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + Constants.ORDER_JSON_FILEPATH);
		Response response = APIMethods.method_POST(requestBody, requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON Status Code validation
		response.then().statusCode(HttpStatus.SC_CREATED);

		// capturing created order ID
		orderID = response.then().extract().path("id").toString();
		log.info("Created New Order - ID - " + orderID);

		// Request Details
		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_take;
		response = APIMethods.method_PUT(requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON response Pay load validations
		response.then().body("status", is(Constants.ORDER_STATUS_ONGOING));

		// Request Details for Cancel Endpoint

		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_cancel;
		response = APIMethods.method_PUT(requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_OK);
		response.then().body("status", is(Constants.ORDER_STATUS_CANCELLED));
		response.then().body("cancelledAt", notNullValue());

	}

	@Test(priority = 3)
	public void verify_cancelOrder_invalidData() {

		orderID = String.valueOf(Instant.now().getEpochSecond());

		// Request Details
		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_cancel;
		response = APIMethods.method_PUT(requestURL);

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_NOT_FOUND);

	}

}
