package com.db.mongo.ga;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONObject;
import org.json.simple.JSONArray;

public class CallGetService {

	public org.json.JSONArray getRuleEngines(String email, String funnel) {

		org.json.JSONArray ruleengar=null;
		JSONObject obj = null;
		// project_name=funnel4455&user_name=viki_gmail.com&source=leadconverter
		// http://development.bizlem.io:8087/com.carrotruleangular/GetRuleEngine_Name
		String CheckfreetrailCart_api = ResourceBundle.getBundle("config").getString("GetRuleEnginesofFunnel")
				+ "?project_name=" + funnel + "&user_name=" + email + "&source=leadconverter";
		System.out.println("CheckfreetrailCart_api = " + CheckfreetrailCart_api);
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

			String resp = response.toString();
			System.out.println("responseCode : " + responseCode + "\n" + "ResponseBody== : " + resp);
			obj = new JSONObject(resp);
			ruleengar=obj.getJSONArray("RuleEngine_Name");
			// System.out.println("obj.toString() : "+obj.toString());

		} catch (Exception e) {
			System.out.println("Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());

		}

		return ruleengar;

	}

	public static void main(String args[]) {

		org.json.JSONArray rs = new CallGetService().getRuleEngines("salesautoconvertuser1@gmail.com", "NewTestNov26");
		System.out.println("method : " + rs);
	}
}
