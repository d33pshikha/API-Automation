package com.apitesting.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.apitesting.staticData.Constants;

public class PropertiesLoader {

	public static String testdata_filepath = System.getProperty("user.dir") + Constants.PROPERTIES_FILE_PATH;
	private static Properties prop = null;
	private static FileInputStream fi = null;
 
  public static String readDataFromPropertiesFile(String propertyName, String propertyFilePath) {

    try {
      prop = new Properties();
      fi = new FileInputStream(propertyFilePath);
      prop.load(fi);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return prop.getProperty(propertyName);
  }
  
  public static String getProperty(String propertyName) {
	  if(prop == null ) {
		  readDataFromPropertiesFile(propertyName, testdata_filepath);
	  }
	  return prop.getProperty(propertyName);
  }
}
