package com.apitesting.utilities;

import org.testng.annotations.BeforeSuite;


public class PropertiesUtility {
	
	
	public static String url;
	public static String orderEndpoint;
	public static String orderEndpoint_take;
	public static String orderEndpoint_complete;
	public static String orderEndpoint_cancel;
	
	@BeforeSuite
	public static void readProperties() {
		
		 url = PropertiesLoader.getProperty("url");
		 orderEndpoint = PropertiesLoader.getProperty("orderEndpoint");
		 orderEndpoint_take = PropertiesLoader.getProperty("orderEndpoint.take");
		 orderEndpoint_complete = PropertiesLoader.getProperty("orderEndpoint.complete");
		 orderEndpoint_cancel = PropertiesLoader.getProperty("orderEndpoint.cancel");
	}

}
