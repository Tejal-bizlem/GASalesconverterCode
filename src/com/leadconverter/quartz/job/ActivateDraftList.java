package com.leadconverter.quartz.job;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.ResourceBundle;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ActivateDraftList implements Job {
	final static Logger logger = Logger.getLogger(ActivateDraftList.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {

			HttpURLConnection conn = null;
			String response_code = null;
			String res = "10";

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}

			} };

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

			// String urlStr=ResourceBundle.getBundle("config").getString("processQueue");
			// subscribermanger.activate_draft_list
			String urlStr = ResourceBundle.getBundle("config").getString("activate_draft_list");

			String data = "";
			urlStr = urlStr.replace(" ", "%20");
			URL url;
			InputStream ins = null;
			StringBuilder requestString = new StringBuilder(urlStr);
			Date d = new Date();
			logger.info("ActivateDraftList GAData Job Called ! at " + d);

			try {
				// logger.info("ProcessQueue Job Is Called!");
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
				logger.info("ActivateDraftList Job Is Called Response Code : " + conn.getResponseCode() + " Body : "
						+ sb.toString());
				// System.out.println("conn.getResponseCode() : "+conn.getResponseCode());
				// System.out.println("sb.toString() : "+sb.toString());
				conn.disconnect();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception ec) {
			logger.info("ec at " + ec);
		}
	}
}
