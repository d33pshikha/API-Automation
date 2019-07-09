package com.apitesting.testscripts;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.apitesting.staticData.Constants;
import com.apitesting.utilities.APIMethods;
import com.apitesting.utilities.CommonUtil;
import com.apitesting.utilities.JSON_Utilities;
import com.apitesting.utilities.MasterTest;
import com.jayway.restassured.response.Response;

public class PlaceOrder extends MasterTest {

	private static final Logger log = LogManager.getLogger();
	DecimalFormat f = new DecimalFormat("##.00");

	@Test(priority = 1)
	public void verify_placeOrder() {

		Response response = CommonUtil.placeOrder(Constants.ORDER_JSON_FILEPATH);

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
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + Constants.ORDER_JSON_FILEPATH);
		Assert.assertEquals(response.jsonPath().getList("drivingDistancesInMeters").size(),
				(JSON_Utilities.jsonArray(requestBody).size() - 1));

	}

	@Test(priority = 2)
	public void verify_placeAdvanceOrder_9To5() {
		
		Response response = CommonUtil.placeOrder(Constants.ADVANCE_ORDER_JSON_FILEPATH);

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
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + Constants.ORDER_JSON_FILEPATH);
		Assert.assertEquals(response.jsonPath().getList("drivingDistancesInMeters").size(),
				(JSON_Utilities.jsonArray(requestBody).size() - 1));
		List<Integer> distanceList = response.jsonPath().get("drivingDistancesInMeters");

		float totalDistance = 0;
		for (float x : distanceList) {
			totalDistance = totalDistance + x;
		}
		float fare = (((totalDistance - Constants.FIRST_KM) / Constants.DISTANCE_AFTER_2KM) * Constants.FARE_AFTER_2KM)
				+ Constants.FARE_2KM;
		response.then().body("fare.amount", is(String.valueOf(f.format(fare))));

	}

	@Test(priority = 3)
	public void verify_placeAdvanceOrder_Not9To5() {

		Response response = CommonUtil.placeOrder(Constants.ADVANCE_ORDER_JSON_FILEPATH_NOT9TO5);

		// JSON Status Code validation
		response.then().statusCode(HttpStatus.SC_CREATED);

		// capturing created order ID
		String orderID = response.then().extract().path("id").toString();
		log.info("Created New Order - ID - " + orderID);

		// JSON response Pay load validations
		response.then().body("id", notNullValue());
		response.then().body("fare.amount", notNullValue());
		response.then().body("fare.currency", is(Constants.CURRENCY));
		List<Integer> distanceList = response.jsonPath().get("drivingDistancesInMeters");

		float totalDistance = 0;
		for (float x : distanceList) {
			totalDistance = totalDistance + x;
		}
		float fare = (((totalDistance - Constants.FIRST_KM) / Constants.DISTANCE_AFTER_2KM)
				* Constants.FARE_AFTER_2KM_NIGHT) + Constants.FARE_2KM_NIGHT;
		response.then().body("fare.amount", is(String.valueOf(f.format(fare))));

	}

	@Test(priority = 4)
	public void verify_placeOrder_InvalidJson() {

		Response response = CommonUtil.placeOrder(Constants.ORDER_INVALID_JSON_FILEPATH);
	
		// printing response
		log.info("RESPONSE:" + response.asString());

		response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

	}

	@Test(priority = 5)
	public void verify_PastDate_AdvanceOrder() {

		Response response = CommonUtil.placeOrder(Constants.ADVANCE_ORDER_PAST_DATE_JSON_FILEPATH);
		
		// printing response
		log.info("RESPONSE:" + response.asString());

		response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

	}

	@Test(priority = 6)
	public void verify_placeOrder_singleEntry() {

		Response response = CommonUtil.placeOrder(Constants.ORDER_SINGLE_ENTRY_JSON_FILEPATH);
		
		// printing response
		log.info("RESPONSE:" + response.asString());

		response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

	}

}
