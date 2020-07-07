package com.leadconverter.freetrail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class CheckValidUserforFreetrialAndCart.
 */
public class CheckValidUserforFreetrialAndCart {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String args[]) {
		checkValiditytrialCart("jobs@bizlem.com");

	}

	/**
	 * Check validitytrial cart.
	 *
	 * @param userid the userid
	 * @return the string
	 */
	public static String checkValiditytrialCart(String userid) {
		int expireFlag = 1;
		JSONObject obj = null;// "http://development.bizlem.io:8082/portal/servlet/service/ui.checkValidUser"
		String CheckfreetrailCart_api = ResourceBundle.getBundle("config").getString("CheckValidUsertrialandcart")
				+ "?email=" + userid;
		StringBuffer response = null;
		int responseCode = 0;

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
		/*
		 * end of the fix
		 */

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
			System.out.println("responseCode : " + responseCode + "\n" + "ResponseBody== : " + response);
			String resp = response.toString();
			obj = new JSONObject(resp);
			System.out.println("obj.toString() : " + obj.toString());
			// LogByFileWriter.logger_info("CreateRuleEngine : " + "responseCode :
			// "+responseCode+"\n"+"ResponseBody : "+response);
		} catch (Exception e) {
			System.out.println("Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
			// LogByFileWriter.logger_info("CreateRuleEngine : " + "Exception ex upload to
			// server callnewscript: " + e.getMessage() + e.getStackTrace());
		}
		return obj.toString();
	}

}
