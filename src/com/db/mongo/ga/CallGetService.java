package com.db.mongo.ga;

import com.db.mongo.ga.CallGetService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class CallGetService {
	static final Logger logger = Logger.getLogger(CallGetService.class);

	public JSONArray getRuleEngines(String email, String funnel) {
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
         sc.init((KeyManager[])null, trustAllCerts, new SecureRandom());
         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      } catch (Exception var15) {
         var15.printStackTrace();
      }

    
      JSONArray ruleengar = null;
      JSONObject obj = null;
      String CheckfreetrailCart_api = ResourceBundle.getBundle("config").getString("GetRuleEnginesofFunnelfromcarrotserver") + "?project_name=" + funnel.replace(" ", "_") + "&user_name=" + email.replace("@", "_");
      System.out.println("CheckfreetrailCart_api = " + CheckfreetrailCart_api);
      logger.info("CheckfreetrailCart_api = " + CheckfreetrailCart_api);
      StringBuffer response = null;
      boolean var9 = false;

      try {
         URL url = new URL(CheckfreetrailCart_api);
         HttpURLConnection con = (HttpURLConnection)url.openConnection();
         con.setRequestMethod("GET");
         con.setDoOutput(true);
         int responseCode = con.getResponseCode();
         BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
         response = new StringBuffer();

         String inputLine;
         while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
         }

         in.close();
         String resp = response.toString();
         System.out.println("resp : " + resp);
         logger.info("responseCode : " + responseCode + "\n" + "ResponseBody== : " + resp);
         obj = new JSONObject(resp);
         ruleengar = obj.getJSONArray("RuleEngine_Name");
         System.out.println("obj.toString() : " + obj.toString());
      } catch (Exception var16) {
         logger.info("Exception ex  upload to server callnewscript: " + var16);
         var16.printStackTrace();
      }

      return ruleengar;
   }

	public static void main(String[] args) {
		try {
			JSONArray rs = (new CallGetService()).getRuleEngines("jobs@bizlem.com", "Test 8 Jan");
			System.out.println("method : " + rs);
		} catch (Exception var3) {
			var3.printStackTrace();
		}

	}

	public static String checkfreetrial(String userid) throws UnsupportedEncodingException {
		try {
			URL url = new URL(
					"http://bluealgo.com:8087/com.carrotruleangularmongo/GetRuleEngine_Name?project_name=Test_8_Jan&user_name=jobs_bizlem.com&source=leadconverter");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "*");
			con.setDoInput(true);
			con.setDoOutput(false);
			System.out.println("checkfreetrial  :: " + con.getResponseCode());
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine  :: " + inputLine);
			}

			in.close();
		} catch (Exception var5) {
			System.out.println(var5.getMessage().toString());
		}

		return null;
	}
}