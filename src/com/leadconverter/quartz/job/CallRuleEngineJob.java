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

public class CallRuleEngineJob implements Job{
	final static Logger logger = Logger.getLogger(CallRuleEngineJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
		    String urlStr=ResourceBundle.getBundle("config").getString("call_rulengine_url");
			String data = "";
			urlStr = urlStr.replace(" ", "%20");
			URL url;
			InputStream ins = null;
			StringBuilder requestString = new StringBuilder(urlStr);
	
			try {
				//System.out.println("Rule Engine Job Called");
				//logger.info("Rule Engine Job Called");
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
				logger.info("Rule Engine Job Called Response Code : "+conn.getResponseCode());
				conn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}catch (Exception ec){
	
		}
	}
}
