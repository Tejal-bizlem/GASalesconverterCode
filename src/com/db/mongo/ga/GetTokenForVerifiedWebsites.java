package com.db.mongo.ga;

import com.leadconverter.ga.ConnectHttps;
import com.leadconverter.ga.GoogleAnalyticsReportingSample;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
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
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetTokenForVerifiedWebsites {
	static final Logger logger = Logger.getLogger(GetTokenForVerifiedWebsites.class);

	public static JSONArray findGAUserCredentials(String GAEmail) throws JSONException {
		JSONObject ga_user_json_obj = new JSONObject();
		JSONArray ga_user_jsonarr = new JSONArray();
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");

		try {
			collection = database.getCollection("google_analytics_user_credentials");
			Document doc = null;
			Bson filter = Filters.eq("username", GAEmail);
			FindIterable<Document> filerdata = collection.find(filter);
			MongoCursor monitordata = filerdata.iterator();

			while (monitordata.hasNext()) {
				doc = (Document) monitordata.next();
				ga_user_json_obj.put("username", doc.getString("username").toString());
				ga_user_json_obj.put("refreshToken", doc.getString("refreshToken").toString());
				ga_user_json_obj.put("accessToken", doc.getString("accessToken").toString());
				ga_user_json_obj.put("view_name", doc.getString("view_name").toString());
				ga_user_json_obj.put("view_id", doc.getString("view_id").toString());
				ga_user_jsonarr.put(ga_user_json_obj);
				if (doc.getString("username").equals("bluealgo.ga@gmail.com")) {
					JSONObject ga_user_json_objLACviewid = new JSONObject();
					ga_user_json_obj.put("username", doc.getString("username").toString());
					ga_user_json_obj.put("refreshToken", doc.getString("refreshToken").toString());
					ga_user_json_obj.put("accessToken", doc.getString("accessToken").toString());
					ga_user_json_obj.put("view_name", doc.getString("view_name").toString());
					ga_user_json_obj.put("view_id", "206382866");
					ga_user_jsonarr.put(ga_user_json_objLACviewid);
				}
			}

			logger.info("Data for  GoogleAnalytics User Found ga_user_json_obj : " + ga_user_json_obj);
		} catch (Exception var10) {
			System.out.println("exc in findGAUserCredentials: " + var10);
			ga_user_json_obj.put("excmongo", var10.toString());
		}

		return ga_user_jsonarr;
	}

	public static String Sleepfor30MIN(long min) {
		String res = null;

		try {
			System.out.println("Start..." + new Date());
			TimeUnit.MINUTES.sleep(min);
			System.out.println("End..." + new Date());
			res = "true";
		} catch (InterruptedException var4) {
			res = var4.toString();
			System.err.format("IOException: %s%n", var4);
		}

		return res;
	}

	public static String VerifyWebsites(String GAemail, String websiteurl, String ToEmail) {
		String resp = "";
		String accessToken = null;
		String refreshToken = null;
		String view_id = null;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String str_start_date = formatter.format(date);
		String hostname = null;
		resp = GAemail + "   clint == " + ResourceBundle.getBundle("config").getString("client_id");
		JSONObject sendobj = new JSONObject();

		try {
			JSONObject getmongodata = null;
			JSONObject verifiedjson = null;
			JSONArray verifiedja = new JSONArray();
			new JSONArray();
			String respthread = Sleepfor30MIN(15L);
			resp = resp + ": respthread :" + respthread;
			if (respthread.equals("true")) {
				JSONArray ga_user_jsonarr = findGAUserCredentials(GAemail);

				for (int j = 0; j < ga_user_jsonarr.length(); ++j) {
					try {
						getmongodata = ga_user_jsonarr.getJSONObject(j);
						resp = resp + ":: getmongodata ::" + getmongodata.toString();
						if (getmongodata.has("refreshToken")) {
							refreshToken = getmongodata.get("refreshToken").toString();
							resp = resp + ":: refreshToken ::" + refreshToken;
							org.json.simple.JSONObject respose_json_object = new org.json.simple.JSONObject();

							try {
								respose_json_object = ConnectHttps
										.getTokenByRefreshToken(refreshToken.replaceAll("\\\\", ""));
							} catch (Exception var31) {
								resp = resp + ":: refreshTokenexceptions ::" + var31.toString();
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
									.getGAReport("input", accessToken, str_start_date, str_start_date, view_id)
									.toString();
							resp = resp + ":: :::  google_analytics_response ::" + google_analytics_response;
							JSONObject google_analytics_responsejson = new JSONObject(google_analytics_response);
							JSONArray GAdataja = google_analytics_responsejson.getJSONArray("data");
							JSONObject getdatabyUserPage = null;

							for (int i = 0; i < GAdataja.length(); ++i) {
								getdatabyUserPage = GAdataja.getJSONObject(i);
								if (getdatabyUserPage.has("ga:dimension2")
										&& getdatabyUserPage.get("ga:dimension2") != "" && getdatabyUserPage
												.get("ga:dimension2").equals("52101LeadAutoConverter" + ToEmail)) {
									hostname = getdatabyUserPage.getString("ga:hostname");
									verifiedjson = new JSONObject();
									verifiedjson.put("hostname", hostname);
									verifiedjson.put("UniqueEmail", getdatabyUserPage.get("ga:dimension2"));
									resp = resp + "::: : verifiedjson ::" + verifiedjson;
									verifiedja.put(verifiedjson);
								}
							}

							resp = resp + ":: verifiedjaarry ::" + verifiedja;
							String websites = "";
							if (verifiedja.length() > 0) {
								for (int i = 0; i < verifiedja.length(); ++i) {
									JSONObject jsb = verifiedja.getJSONObject(i);
									if (i == 0) {
										resp = resp + "===== websites::: " + websites;
										websites = jsb.get("hostname").toString();
									} else {
										websites = websites + " " + jsb.get("hostname").toString();
										resp = resp + "===== websites::: " + websites;
									}

									sendobj.put("body",
											"Your website " + websiteurl + " has beed configured successfully ");
								}
							} else {
								sendobj.put("body", "Your website " + websiteurl
										+ " is not configured. Please check Tag Manager Configuration");
							}

							resp = resp + "===== websites:::final " + websites;
							String urlstr = ResourceBundle.getBundle("config").getString("sendmaildoctiger");
							JSONArray tojs = new JSONArray();
							JSONArray ccjs = new JSONArray();
							JSONArray bccjs = new JSONArray();
							JSONArray attjs = new JSONArray();
							tojs.put(ToEmail);
							sendobj.put("to", tojs);
							sendobj.put("fromId", "doctigertest@gmail.com");
							sendobj.put("fromPass", "doctiger@123");
							sendobj.put("subject", "LeadAutoConverter Mail Alert for Website Verification");
							sendobj.put("cc", ccjs);
							sendobj.put("bcc", bccjs);
							sendobj.put("attachmentPath", "");
							sendobj.put("attachments", attjs);
							resp = resp + "sendobj:: " + sendobj.toString();
							String respmail = callPostJSonModified(urlstr, sendobj);
							resp = resp + "respmail::: " + respmail;
						}
					} catch (Exception var32) {
						;
					}
				}
			}
		} catch (Exception var33) {
			resp = resp + ":: Exception ::" + var33.toString();
		}

		return resp;
	}

	public static String callPostJSonModified(String urlstr, JSONObject Obj) {
		StringBuffer response = null;
	
		String var4 = "";

		try {
			URL url = new URL(urlstr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json;");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(Obj.toString());
			wr.flush();
			wr.close();
			System.out.println("Obj " + Obj);
			int responseCode = con.getResponseCode();
			System.out.println("responseCode " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			response = new StringBuffer();

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
			System.out.println("response :: " + response.toString());
			System.out.println("end");
		} catch (Exception var10) {
			;
		}

		return response.toString();
	}
}