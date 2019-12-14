package com.leadconverter.quartz.job;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rest.api.Test;

public class CampaignSheduleAndActivaterJob implements Job{
	final static Logger logger = Logger.getLogger(CampaignSheduleAndActivaterJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		
		
		try {
			
			
		   	 HttpURLConnection conn=null;
		 	 String response_code=null;
		 	 String res="10";
		 
		    TrustManager[] trustAllCerts = new TrustManager[] {
		    	       new X509TrustManager() {
		    	          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		    	            return null;
		    	          }

		    	          public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

		    	          public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

		    	       }
		    	    };

		    	    
		    	    try {
		    	    	SSLContext sc = SSLContext.getInstance("SSL");
						sc.init(null, trustAllCerts, new java.security.SecureRandom());
						HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	    
		    	    // Create all-trusting host name verifier
		    	    HostnameVerifier allHostsValid = new HostnameVerifier() {
		    	        public boolean verify(String hostname, SSLSession session) {
		    	          return true;
		    	        }
		    	    };
		    	    // Install the all-trusting host verifier
		    	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			
			
			
			
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
				 conn = (HttpURLConnection) url.openConnection();
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
