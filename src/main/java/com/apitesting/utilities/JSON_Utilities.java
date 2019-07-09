package com.apitesting.utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSON_Utilities {

	public static String jsonToString(String filepath) {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;

		try {
			Object obj = parser.parse(new FileReader(filepath));
			jsonObject = (JSONObject) obj;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return jsonObject.toString();
	}

	public static JSONArray jsonArray(String requestBody) {
		JSONParser jsonParser = new JSONParser();
		JSONObject obj = null;
		JSONArray reqArr = null;
		try {
			obj = (JSONObject) jsonParser.parse(requestBody);
			reqArr = (JSONArray) obj.get("stops");

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return reqArr;

	}
}