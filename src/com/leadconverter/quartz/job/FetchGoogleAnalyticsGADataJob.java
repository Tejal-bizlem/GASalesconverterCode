package com.leadconverter.quartz.job;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.db.mongo.ga.AnalyticsDataInsertUpdate;
import com.db.mongo.ga.GAMongoDAO;
import com.google.api.AccesAndRefreshToken;

public class FetchGoogleAnalyticsGADataJob implements Job{
	final static Logger logger = Logger.getLogger(FetchGoogleAnalyticsGADataJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
		    	//System.out.println("Hello Quartz! FetchGoogleAnalyticsGADataJob");
		    	logger.info("Fetch GoogleAnalytics GAData Job Called !");
		    	//This Method Call get data from google analytics and save it to "google_analytics_data_temp" collection
				JSONArray ga_users_json_arr=AnalyticsDataInsertUpdate.findGAUserCredentials();
				AccesAndRefreshToken.getGAData(ga_users_json_arr);
				//This Method Call get data from "google_analytics_data_temp" collection. Do the statistics and save it 
				// to "google_analytics_url_view_collection" collection
				GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
				//This Method Call used to create JSON object for RuleEngine
				GAMongoDAO.fetchGADataForRuleEngine("google_analytics_data_temp");
				
			}catch (Exception ex){
				logger.info("Fetch GoogleAnalytics GAData Job Error : "+ex.getMessage());
		    }
	}
}
