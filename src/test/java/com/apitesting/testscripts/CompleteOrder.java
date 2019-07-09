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
import com.apitesting.utilities.CommonUtil;
import com.apitesting.utilities.JSON_Utilities;
import com.apitesting.utilities.MasterTest;
import com.jayway.restassured.response.Response;

public class CompleteOrder extends MasterTest {

	private static final Logger log = LogManager.getLogger();
	String orderID;
	Response response;
	String requestURL;

	@Test(priority = 1)
	public void verify_CompleteOrder_validData() {
		//place order 
		response = CommonUtil.placeOrder(Constants.ORDER_JSON_FILEPATH);

		// JSON Status Code validation
		response.then().statusCode(HttpStatus.SC_CREATED);

		// capturing created order ID
		orderID = response.then().extract().path("id").toString();
		log.info("Created New Order - ID - " + orderID);

		// Request Details for ongoing
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

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_OK);
		response.then().body("status", is(Constants.ORDER_STATUS_COMPLETED));
		response.then().body("completedAt", notNullValue());

	}

	@Test(priority = 2)
	public void verify_completeOrder_invalidData() {

		orderID = String.valueOf(Instant.now().getEpochSecond());
		// Request Details
		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_complete;
		response = APIMethods.method_PUT(requestURL);

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_NOT_FOUND);

	}

	@Test(priority = 3)
	public void verify_voilatedFlow_cancelToComplete() {

		//place order
		response = CommonUtil.placeOrder(Constants.ORDER_JSON_FILEPATH);

		// JSON Status Code validation
		response.then().statusCode(HttpStatus.SC_CREATED);

		// capturing created order ID
		orderID = response.then().extract().path("id").toString();
		log.info("Created New Order - ID - " + orderID);

		// Request Details for Cancel Endpoint

		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_cancel;
		response = APIMethods.method_PUT(requestURL);

		// Request Details for Complete Endpoint
		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_complete;
		response = APIMethods.method_PUT(requestURL);

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);

	}

	@Test(priority = 4)
	public void verify_CompleteOrder_assigningToComplete() {
		//place order
		response = CommonUtil.placeOrder(Constants.ORDER_JSON_FILEPATH);

		// JSON Status Code validation
		response.then().statusCode(HttpStatus.SC_CREATED);

		// capturing created order ID
		orderID = response.then().extract().path("id").toString();
		log.info("Created New Order - ID - " + orderID);

		// Request Details for Complete Endpoint
		requestURL = url + orderEndpoint + "/" + orderID + orderEndpoint_complete;
		response = APIMethods.method_PUT(requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);

	}

}
