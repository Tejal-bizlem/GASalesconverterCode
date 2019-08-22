package com.db.mongo.ga;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONObject;
//import org.json.simple.JSONArray;
import org.json.JSONArray;

import com.google.api.AccesAndRefreshToken;
import com.leadconverter.ga.GoogleAnalyticsReportingSample;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class GetTokenForVerifiedWebsites {
	private static MongoSetup mongo = new MongoSetup("phplisttest");
	// private static MongoSetupForIP mongo = new MongoSetupForIP( "phplisttest" );
	private static DBCollection collection = null;

	final static Logger logger = Logger.getLogger(GetTokenForVerifiedWebsites.class);

	public static JSONObject findGAUserCredentials(String GAEmail) {
		JSONObject ga_user_json_obj = new JSONObject();

		try {
			collection = mongo.getCollection("google_analytics_user_credentials");
			BasicDBObject doc = null;
			// bizlembizlem1234@gmail.com"

			/*
			 * "_id" : ObjectId("5cd43c92d710b02aafe5a6a8"), "username" :
			 * "bizlembizlem1234@gmail.com", "accessToken" :
			 * "ya29.GlsEB49OuMB_poeXzDXop11ci5rp9TlhIN3hoR8ErMF_t43fsgu48VuseP8BAGVAOflQd5EewQswEtzioX-utZWwCzjL9HTdGYzJreiXNIBTcTEeFxet7ies67qJ",
			 * "refreshToken" :
			 * "1/xlZy_MAfdJ3G-Tg6KmPbJU8_zMT-5Z_qaVg2v98QCIPN8b0hqVCMDgXJezplU4M8",
			 * "view_name" : "All Web Site Data", "view_id" : "190758260"
			 */
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", GAEmail);
			DBCursor cursor = collection.find(whereQuery);
			ga_user_json_obj.put("startmongo", GAEmail);
			while (cursor.hasNext()) {

				doc = (BasicDBObject) cursor.next();
//				ga_user_json_obj = new JSONObject();
				ga_user_json_obj.put("username", doc.getString("username").toString());
				ga_user_json_obj.put("refreshToken", doc.getString("refreshToken").toString());
				ga_user_json_obj.put("accessToken", doc.getString("accessToken").toString());
				ga_user_json_obj.put("view_name", doc.getString("view_name").toString());
				ga_user_json_obj.put("view_id", doc.getString("view_id").toString());

			}
			// System.out.println(cursor.size());
			logger.info("Data for  GoogleAnalytics User Found ga_user_json_obj : " + ga_user_json_obj);
		} catch (Exception e) {
			System.out.println("exc in findGAUserCredentials: " + e);
			ga_user_json_obj.put("excmongo", e.toString());
		}
		return ga_user_json_obj;
	}

	public static String Sleepfor30MIN(long min) {
		String res = null;
		try {

			System.out.println("Start..." + new Date());
			// delay 5 seconds
//          Thread.sleep(12) ;
			TimeUnit.MINUTES.sleep(min);
			System.out.println("End..." + new Date());
			res = "true";
			// delay 0.5 second
			// TimeUnit.MICROSECONDS.sleep(500);

			// delay 1 minute
			// TimeUnit.MINUTES.sleep(1);

		} catch (InterruptedException e) {
			res = e.toString();
			System.err.format("IOException: %s%n", e);
		}
		return res;

	}

	public static String VerifyWebsites(String GAemail, String email) {
		String resp = "";
		String accessToken = null;
		String refreshToken = null;
		String view_id = null;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String str_start_date = formatter.format(date);
		String hostname = null;
//		out.println("GAemail : " + GAemail);
		resp = GAemail;
		resp = resp+"   clint == "+ResourceBundle.getBundle("config").getString("client_id");;
		try {
			JSONObject getmongodata = null;
			JSONObject verifiedjson = null;
			JSONArray verifiedja = new JSONArray();

			String respthread = "true";// GetTokenForVerifiedWebsites.Sleepfor30MIN(5);
//			out.println("respthread : " + respthread);
			resp = resp + ": respthread :" + respthread;
			if (respthread.equals("true")) {
//			String	getmongodat = GetTokenForVerifiedWebsites.findGAUserCredentialsTest(GAemail);
//			resp=resp+"getmongodatest: "+getmongodat;
				getmongodata = GetTokenForVerifiedWebsites.findGAUserCredentials(GAemail);
//				out.println("getmongodata = " + getmongodata);
				resp = resp + ":: getmongodata ::" + getmongodata.toString();
				if (getmongodata.has("refreshToken")) {

//					accessToken 
					refreshToken = getmongodata.get("refreshToken").toString();
					resp = resp + ":: refreshToken ::" + refreshToken;
					org.json.simple.JSONObject respose_json_object = new org.json.simple.JSONObject();
					try {
						respose_json_object = AccesAndRefreshToken.getTokenByRefreshToken(refreshToken);
					} catch (Exception e) {
						resp = resp + ":: refreshTokenexceptions ::" + e.toString();
						// TODO: handle exception
					}
					resp = resp + ":: :::  getTokenByRefreshToken ::" + respose_json_object.toString();
					String response_code = (String) respose_json_object.get("response_code");
					if (response_code.equals("200")) {

						accessToken = (String) respose_json_object.get("access_token");
						resp = resp + ":: :::  accessToken ::" + accessToken;
					}
					view_id = getmongodata.get("view_id").toString();
					resp = resp + ":: :::  view_id ::" + view_id;
					String google_analytics_response = GoogleAnalyticsReportingSample
							.getGAReport("input", accessToken, "2019-07-16", str_start_date, view_id).toString();// str_start_date
					resp = resp + ":: :::  google_analytics_response ::" + google_analytics_response;
					JSONObject google_analytics_responsejson = new JSONObject(google_analytics_response);
					JSONArray GAdataja = google_analytics_responsejson.getJSONArray("data");
					JSONObject getdatabyUserPage = null;

					for (int i = 0; i < GAdataja.length(); i++) {
						getdatabyUserPage = GAdataja.getJSONObject(i);
						// 5210106948678098 ga:dimension2
						if (getdatabyUserPage.has("ga:dimension2") && getdatabyUserPage.get("ga:dimension2") != "") {
							// 5210106948678098@LeadAutoConverter.com
							if (getdatabyUserPage.get("ga:dimension2").equals("vivek@bizlem.com")) {
//					ga:hostname
								hostname = getdatabyUserPage.getString("ga:hostname");
								verifiedjson = new JSONObject();
								verifiedjson.put("hostname", hostname);
								verifiedjson.put("UniqueEmail", getdatabyUserPage.get("ga:dimension2"));
								resp = resp + "::: : verifiedjson ::" + verifiedjson;
								verifiedja.put(verifiedjson);
							}

						}

					}
//					out.println("verifiedjarray= " + verifiedja);

					resp = resp + ":: verifiedjaarry ::" + verifiedja;
					String websites = "";
					for (int i = 0; i < verifiedja.length(); i++) {

						JSONObject jsb = verifiedja.getJSONObject(i);
						if (i == 0) {
							resp = resp + "===== websites::: " + websites;
							websites = jsb.get("hostname").toString();
						} else {
							websites = websites + " " + jsb.get("hostname").toString();
							resp = resp + "===== websites::: " + websites;
						}

					}
					resp = resp + "===== websites:::final " + websites;
					String urlstr = "http://prod.bizlem.io:8085/NewMailDev/getFileAttachServlet";
					/*
					 * {"cc":[],"bcc":[],"attachments":[],"fromPass":"doctiger@123",
					 * "subject":"Testing12 Send Mail From MailTemlate","to":[
					 * "tejal.jabade@bizlem.com"],
					 * "body":"<p>Hello  Tejal ,<\/p>\n\n<p>How are you?<\/p>\n\n <p><strong>This is test mail sent from DocTiger.<\/strong><\/p>\n\n <p><u>hiiiiiiiiii<\/u<\/p>\n\n <p> 1 <\/p>\n\n <p>Thanks<\/p>\n\n<p>&nbsp;<\/p>\n"
					 * ,"fromId":"doctigertest@gmail.com","attachmentPath":""}
					 */

					JSONArray tojs = new JSONArray();
					JSONArray ccjs = new JSONArray();
					JSONArray bccjs = new JSONArray();
					JSONArray attjs = new JSONArray();
					JSONObject sendobj = new JSONObject();
					tojs.put("tejal.jabade@bizlem.com");
					sendobj.put("to", tojs);
					sendobj.put("fromId", "doctigertest@gmail.com");
					sendobj.put("fromPass", "doctiger@123");
//					sendobj.put("fromId", fromId);
//					sendobj.put("fromPass",fromPass);
					sendobj.put("subject", "LeadAutoConverter Mail Alert for Website Verification");
					sendobj.put("cc", ccjs);
					sendobj.put("bcc", bccjs);

					sendobj.put("attachmentPath", "");
					sendobj.put("attachments", attjs);
					sendobj.put("body", "Your following websites " + websites + " are configured");
					resp = resp + "sendobj:: " + sendobj.toString();
					String respmail = GetTokenForVerifiedWebsites.callPostJSonModified(urlstr, sendobj);
					resp = resp + "respmail::: " + respmail;
				}

			}
		} catch (Exception e) {
//			out.println("Exception= " + e);
			resp = resp + ":: Exception ::" + e.toString();

		}
		return resp;

	}

	public static String findGAUserCredentialsTest(String GAEmail) {
		String resp = "";
		JSONObject ga_user_json_obj = new JSONObject();
		
		try {
			resp = "start";
			collection = mongo.getCollection("google_analytics_user_credentials");
			BasicDBObject doc = null;
			// bizlembizlem1234@gmail.com"
			resp = resp + "start1";
			/*
			 * "_id" : ObjectId("5cd43c92d710b02aafe5a6a8"), "username" :
			 * "bizlembizlem1234@gmail.com", "accessToken" :
			 * "ya29.GlsEB49OuMB_poeXzDXop11ci5rp9TlhIN3hoR8ErMF_t43fsgu48VuseP8BAGVAOflQd5EewQswEtzioX-utZWwCzjL9HTdGYzJreiXNIBTcTEeFxet7ies67qJ",
			 * "refreshToken" :
			 * "1/xlZy_MAfdJ3G-Tg6KmPbJU8_zMT-5Z_qaVg2v98QCIPN8b0hqVCMDgXJezplU4M8",
			 * "view_name" : "All Web Site Data", "view_id" : "190758260"
			 */
			resp = resp + "start2";
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", GAEmail);
			resp = resp + "start3 GAEmail: " + GAEmail;
			DBCursor cursor = collection.find(whereQuery);
			resp = resp + "***** start4";
			ga_user_json_obj.put("startmongo", GAEmail);
			resp = resp + "start5";
			while (cursor.hasNext()) {
				resp = resp + "start666";

				doc = (BasicDBObject) cursor.next();
//				ga_user_json_obj = new JSONObject();
				ga_user_json_obj.put("username", doc.getString("username").toString());
				ga_user_json_obj.put("refreshToken", doc.getString("refreshToken").toString());
				ga_user_json_obj.put("accessToken", doc.getString("accessToken").toString());
				ga_user_json_obj.put("view_name", doc.getString("view_name").toString());
				ga_user_json_obj.put("view_id", doc.getString("view_id").toString());

			}
			// System.out.println(cursor.size());
			logger.info("Data for  GoogleAnalytics User Found ga_user_json_obj : " + ga_user_json_obj);
		} catch (Exception e) {
			System.out.println("exc in findGAUserCredentials: " + e);
			ga_user_json_obj.put("excmongo", e.toString());
		}
		return resp + ":ga_user_json_obj:" + ga_user_json_obj.toString();
	}

	public static String callPostJSonModified(String urlstr, JSONObject Obj) {
		// SendMailServletUrl=http://prod.bizlem.io:8085/NewMailDev/getFileAttachServlet

		StringBuffer response = null;
		int responseCode = 0;
		String urlParameters = "";
		try {

			URL url = new URL(urlstr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			con.setRequestProperty("Content-Type", "application/json;");
			// + "charset=UTF-8");
			// con.setRequestProperty("Accept-Charset", "UTF-8");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(Obj.toString());
			wr.flush();
			wr.close();
			System.out.println("Obj " + Obj);
			// DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			// BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr));
			// writer.write(Obj.toString());
			// writer.close();
			// wr.close();

			responseCode = con.getResponseCode();
			System.out.println("responseCode " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			System.out.println("response :: " + response.toString());

			System.out.println("end");

		} catch (Exception e) {
			// return e.getMessage();
		}
		return response.toString();

	}
	
	

}
