package com.rest.api;

import org.json.simple.JSONArray;

import com.db.mongo.ga.AnalyticsDataInsertUpdate;
import com.db.mongo.ga.GAMongoDAO;
import com.google.api.AccesAndRefreshToken;

public class GetGAData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//logger.info("Fetch GoogleAnalytics GAData Job Called !");
    	//This Method Call get data from google analytics and save it to "google_analytics_data_temp" collection
		JSONArray ga_users_json_arr=AnalyticsDataInsertUpdate.findGAUserCredentials();
		AccesAndRefreshToken.getGAData(ga_users_json_arr);
		//This Method Call get data from "google_analytics_data_temp" collection. Do the statistics and save it 
		// to "google_analytics_url_view_collection" collection
		GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
		//This Method Call used to create JSON object for RuleEngine
		//GAMongoDAO.fetchGADataForRuleEngine("google_analytics_data_temp");

	}

}
