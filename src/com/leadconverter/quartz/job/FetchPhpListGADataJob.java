package com.leadconverter.quartz.job;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class FetchPhpListGADataJob implements Job{
	final static Logger logger = Logger.getLogger(FetchPhpListGADataJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
		    String urlStr=ResourceBundle.getBundle("config").getString("CampaignStatisticsApi");
			String data = "";
			urlStr = urlStr.replace(" ", "%20");
			URL url;
			InputStream ins = null;
			StringBuilder requestString = new StringBuilder(urlStr);
	
			try {
				//System.out.println("Hello Quartz! FetchPhpListGADataJob");
				//logger.info("Fetch PhpList GAData Job Is Called!");
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
				//System.out.println("conn.getResponseCode() : "+conn.getResponseCode());
				//System.out.println("sb.toString() : "+sb.toString());
			    logger.info("Fetch PhpList GAData Job Is Called Response Code : "+conn.getResponseCode());
				conn.disconnect();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}catch (Exception ec){
	
		}
	}
}
