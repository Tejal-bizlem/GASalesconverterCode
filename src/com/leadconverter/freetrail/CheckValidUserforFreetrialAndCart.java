package com.leadconverter.freetrail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONObject;

public class CheckValidUserforFreetrialAndCart {
	
	public static void main(String args[]) {
		checkValiditytrialCart("viki_gmail.com") ;
		
	}
	public static String checkValiditytrialCart(String userid) {
		int expireFlag = 1;
		JSONObject obj = null;// "http://development.bizlem.io:8082/portal/servlet/service/ui.checkValidUser"
		String CheckfreetrailCart_api =ResourceBundle.getBundle("config").getString("CheckValidUsertrialandcart") +"?email=" + userid ;
		StringBuffer response = null;
		int responseCode = 0;
		try {
			URL url = new URL(CheckfreetrailCart_api);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			con.setDoOutput(true);
	
			responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println("responseCode : "+responseCode+"\n"+"ResponseBody== : "+response);
			String resp=response.toString();
			obj=new JSONObject(resp);
			System.out.println("obj.toString() : "+obj.toString());
			//LogByFileWriter.logger_info("CreateRuleEngine : " + "responseCode : "+responseCode+"\n"+"ResponseBody : "+response);
		} catch (Exception e) {
			System.out.println("Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
			//LogByFileWriter.logger_info("CreateRuleEngine : " + "Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
		}
		return obj.toString();
	}

}
