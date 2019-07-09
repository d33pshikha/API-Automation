package com.apitesting.utilities;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jayway.restassured.response.Response;

public class CommonUtil extends MasterTest{
	
	private static final Logger log = LogManager.getLogger();
	public static Response placeOrder(String JSON_FilePath) {

		// Request Details
		String requestURL = url + orderEndpoint ;
			
		String requestBody = JSON_Utilities
				.jsonToString(System.getProperty("user.dir") + JSON_FilePath);
		
		Response response = APIMethods.method_POST(requestBody, requestURL);

		// printing response
		log.info("RESPONSE:" + response.asString());

		return response;
		
	}

}
