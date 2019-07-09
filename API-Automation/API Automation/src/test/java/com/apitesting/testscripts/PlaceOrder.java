package com.apitesting.testscripts;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.apitesting.utilities.APIMethods;
import com.apitesting.utilities.JSON_Utilities;
import com.apitesting.utilities.PropertiesUtility;
import com.apitesting.staticData.Constants;
import com.jayway.restassured.response.Response;
import static org.hamcrest.Matchers.notNullValue;
import java.text.DecimalFormat;
import java.util.List;

import static org.hamcrest.Matchers.is;

public class PlaceOrder extends PropertiesUtility {

	private static final Logger log = LogManager.getLogger();
	DecimalFormat f = new DecimalFormat("##.00");
	
	@Test(priority = 1)
	public void verify_placeOrder() {

		// Request Details
		String requestURL = url + orderEndpoint ;
		System.out.println("url " + url );
		System.out.println("orp " + orderEndpoint );
		
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + Constants.ORDER_JSON_FILEPATH);
		Response response = APIMethods.method_POST(requestBody, requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON Status Code validation
		response.then().statusCode(HttpStatus.SC_CREATED);

		// capturing created order ID
		String orderID = response.then().extract().path("id").toString();
		log.info("Created New Order - ID - " + orderID);

		// JSON response Pay load validations
		response.then().body("id", notNullValue());
		response.then().body("fare.amount", notNullValue());
		response.then().body("fare.currency", is(Constants.CURRENCY));
		// Driving Distance Validation
		Assert.assertEquals(response.jsonPath().getList("drivingDistancesInMeters").size(), Constants.STOPS);
		
	}

	@Test(priority = 2)
	public void verify_placeAdvanceOrder_9To5() {

		// Request Details
		String requestURL = url+orderEndpoint;
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + Constants.ADVANCE_ORDER_JSON_FILEPATH);
		Response response = APIMethods.method_POST(requestBody, requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON Status Code validation
		response.then().statusCode(HttpStatus.SC_CREATED);

		// capturing created order ID
		String orderID = response.then().extract().path("id").toString();
		log.info("Created New Order - ID - " + orderID);

		// JSON response Pay load validations
		response.then().body("id", notNullValue());
		response.then().body("fare.amount", notNullValue());
		response.then().body("fare.currency", is(Constants.CURRENCY));
		Assert.assertEquals(response.jsonPath().getList("drivingDistancesInMeters").size(), Constants.STOPS);
		List<Integer> distanceList = response.jsonPath().get("drivingDistancesInMeters");
		
		float totalDistance = 0;
		for(float x : distanceList) {
			totalDistance = totalDistance + x;
		}
		float fare = (((totalDistance-Constants.FIRST_KM)/Constants.DISTANCE_AFTER_2KM)*Constants.FARE_AFTER_2KM)+Constants.FARE_2KM;
		response.then().body("fare.amount", is(String.valueOf(f.format(fare))));

	}

	@Test(priority = 2)
	public void verify_placeAdvanceOrder_Not9To5() {

		// Request Details
		String requestURL = url+orderEndpoint;
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + Constants.ADVANCE_ORDER_JSON_FILEPATH_NOT9TO5);
		Response response = APIMethods.method_POST(requestBody, requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		// JSON Status Code validation
		response.then().statusCode(HttpStatus.SC_CREATED);

		// capturing created order ID
		String orderID = response.then().extract().path("id").toString();
		log.info("Created New Order - ID - " + orderID);

		// JSON response Pay load validations
		response.then().body("id", notNullValue());
		response.then().body("fare.amount", notNullValue());
		response.then().body("fare.currency", is(Constants.CURRENCY));
		Assert.assertEquals(response.jsonPath().getList("drivingDistancesInMeters").size(), Constants.STOPS);
		List<Integer> distanceList = response.jsonPath().get("drivingDistancesInMeters");
		
		float totalDistance = 0;
		for(float x : distanceList) {
			totalDistance = totalDistance + x;
		}
		float fare = (((totalDistance-Constants.FIRST_KM)/Constants.DISTANCE_AFTER_2KM)*Constants.FARE_AFTER_2KM_NIGHT)+Constants.FARE_2KM_NIGHT;
		response.then().body("fare.amount", is(String.valueOf(f.format(fare))));

	}
	
	@Test(priority = 4)
	public void verify_placeOrder_InvalidJson() {

		// Request Details
		String requestURL = url + orderEndpoint ;
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + Constants.ADVANCE_ORDER_JSON_FILEPATH)
				.replace("stops", "stop");
		Response response = APIMethods.method_POST(requestBody, requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
		
	}
	
	@Test(priority = 5)
	public void verify_PastDate_AdvanceOrder() {

		// Request Details
		String requestURL = url + orderEndpoint ;
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + Constants.ADVANCE_ORDER_PAST_DATE_JSON_FILEPATH)
				.replace("stops", "stop");
		Response response = APIMethods.method_POST(requestBody, requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
		
	}
	
	@Test(priority = 6)
	public void verify_placeOrder_singleEntry() {

		// Request Details
		String requestURL = url + orderEndpoint ;
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + Constants.ORDER_SINGLE_ENTRY_JSON_FILEPATH)
				.replace("stops", "stop");
		Response response = APIMethods.method_POST(requestBody, requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
		
	}

}
