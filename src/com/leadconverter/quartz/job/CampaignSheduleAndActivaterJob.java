package com.leadconverter.quartz.job;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rest.api.Test;

public class CampaignSheduleAndActivaterJob implements Job{
	final static Logger logger = Logger.getLogger(CampaignSheduleAndActivaterJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			//campaignactivator.activate_campaign
		    String urlStr=ResourceBundle.getBundle("config").getString("campaignactivator_url");
			String data = "";
			urlStr = urlStr.replace(" ", "%20");
			URL url;
			InputStream ins = null;
			StringBuilder requestString = new StringBuilder(urlStr);
	
			try {
				//System.out.println("Campaign Shedule And Activater Job Called!");
				
				url = new URL(requestString.toString());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/text");
				ins = conn.getInputStream();
				int ch;
			    StringBuffer sb = new StringBuffer();
			    while ((ch = ins.read()) != -1) {
			        sb.append((char) ch);
			    }
				//System.out.println("Campaign Shedule And Activater Job Called Response Code: "+conn.getResponseCode());
				//System.out.println("sb.toString() : "+sb.toString());
			    logger.info("Campaign Shedule And Activater Job Called Response Code : "+conn.getResponseCode());
				conn.disconnect();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}catch (Exception ec){
	
		}
	}
}
