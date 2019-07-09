package com.apitesting.testscripts;

import org.testng.annotations.Test;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apitesting.staticData.Constants;
import com.apitesting.utilities.APIMethods;
import com.apitesting.utilities.CommonUtil;
import com.apitesting.utilities.MasterTest;
import com.jayway.restassured.response.Response;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.time.Instant;

public class FetchOrderDetails extends MasterTest {

	private static final Logger log = LogManager.getLogger();
	String orderID;
	Response response;
	String requestURL;
	
	@Test(priority = 1)
	public void verify_fetchOrderDetails_Valid() {

		// Place order
		response = CommonUtil.placeOrder(Constants.ORDER_JSON_FILEPATH);

		// JSON Status Code validation
		response.then().statusCode(HttpStatus.SC_CREATED);

		// capturing created order ID
		orderID = response.then().extract().path("id").toString();
		log.info("Created New Order - ID - " + orderID);

		// Request Details for Fetch Order Details Endpoint

		requestURL = url + orderEndpoint + "/" + orderID;
		response = APIMethods.method_GET(requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_OK);
		response.then().body("status", is(Constants.ORDER_STATUS_ASSIGNING));
		response.then().body("createdTime", notNullValue());
		response.then().body("orderDateTime", notNullValue());

	}

	// @Test(priority = 2)
	public void verify_fetchOrderDetails_InValid() {

		orderID = String.valueOf(Instant.now().getEpochSecond());

		// Request Details

		String requestURL = url + orderEndpoint;
		Response response = APIMethods.method_GET(requestURL);

		// JSON response Pay load validations
		response.then().statusCode(HttpStatus.SC_NOT_FOUND);

	}

}
