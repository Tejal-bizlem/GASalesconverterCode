package com.leadconverter.quartz.job;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ActivateDraftList implements Job{
	final static Logger logger = Logger.getLogger(ActivateDraftList.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
		    //String urlStr=ResourceBundle.getBundle("config").getString("processQueue");
			//subscribermanger.activate_draft_list
		    String urlStr=ResourceBundle.getBundle("config").getString("activate_draft_list");
		    
			String data = "";
			urlStr = urlStr.replace(" ", "%20");
			URL url;
			InputStream ins = null;
			StringBuilder requestString = new StringBuilder(urlStr);
	
			try {
				//logger.info("ProcessQueue Job Is Called!");
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
			    logger.info("ActivateDraftList Job Is Called Response Code : "+conn.getResponseCode()+" Body : "+sb.toString());
			    //System.out.println("conn.getResponseCode() : "+conn.getResponseCode());
				//System.out.println("sb.toString() : "+sb.toString());
				conn.disconnect();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}catch (Exception ec){
	
		}
	}
}
