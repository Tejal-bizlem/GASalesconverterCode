package com.google.api;

import com.db.mongo.ga.AnalyticsDataInsertUpdate;
import com.leadconverter.ga.ConnectHttps;
import com.leadconverter.ga.GoogleAnalyticsReportingSample;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AccesAndRefreshToken {
	static final Logger logger = Logger.getLogger(AccesAndRefreshToken.class);

	public static void main(String[] args) {
		String stt = "ssss\\";
		stt = stt.replace("\\", "");
		System.out.println(stt);
		JSONObject respose_json_object = getTokenByRefreshToken(
				"1//0g-Ho67DRVLmUCgYIARAAGBASNwF-L9IrkJIUj170kKrDHhe2bf5ucKr1TFPx1fLS8c4Kk7zXdIXmUdtZZAy39DYHe-1KTa0Zw2g");
		System.out.println("respose_json_object " + respose_json_object.toString());
		String google_analytics_response = null;
		new JSONParser();
		String str_start_date = "2020-01-31";
		String str_end_date = "2020-01-31";
		String accessToken = (String) respose_json_object.get("access_token");
		google_analytics_response = GoogleAnalyticsReportingSample
				.getGAReport("input", accessToken, str_start_date, str_end_date, "206382866").toString();
		System.out.println("google_analytics_response= " + google_analytics_response);
	}

	public static void getGAData(JSONArray ga_users_json_arr) {
		String response_code = null;
		logger.info("Going... to Get GAData For  .." + ga_users_json_arr);
		if (ga_users_json_arr.size() != 0) {
			JSONObject ga_user_json_obj = null;
			logger.info("Going... to Get GAData For GA User ..1");

			for (int i = 0; i < ga_users_json_arr.size(); ++i) {
				ga_user_json_obj = (JSONObject) ga_users_json_arr.get(i);

				try {
					String reftkn = ga_user_json_obj.get("refreshToken").toString().replaceAll("\\\\", "");
					System.out.println("reftkn = " + reftkn);
					logger.info("Going... to Get GAData For GA reftkn .." + reftkn);
					JSONObject respose_json_object = ConnectHttps.getTokenByRefreshToken(reftkn.replaceAll("\\\\", ""));
					response_code = (String) respose_json_object.get("response_code");
					logger.info("Going... to Get GAData For GA respose_json_object .." + respose_json_object);
					logger.info("Going... to Get GAData For GA User .." + response_code);
					if (response_code.equals("200")) {
						String str_start_date = "2020-01-17";
						String str_end_date = "2019-10-04";
						String accessToken = "";
						String username = ga_user_json_obj.get("username").toString();
						String view_name = ga_user_json_obj.get("view_name").toString();
						String view_id = ga_user_json_obj.get("view_id").toString();
						accessToken = (String) respose_json_object.get("access_token");
						logger.info("Going... to Get GAData For GA User : '" + username + "' STARTED....");
						logger.info("GA UserName : " + username + "   GA View Id : " + view_id + "\n"
								+ "Oauth2 Token : " + accessToken);
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd");
						String days_to_fetch_ga = ResourceBundle.getBundle("config").getString("days_to_fetch_ga");
						int days_to_fetch_ga_int = Integer.parseInt(days_to_fetch_ga);
						Date start_date = new Date();
						str_end_date = date_formatter.format(start_date);
						Date endt_date = new Date();
						endt_date.setDate(endt_date.getDate() - days_to_fetch_ga_int);
						str_start_date = date_formatter.format(endt_date);
						logger.info("Start Date(From) : " + str_start_date + "  End Date(To) : " + str_end_date);

						try {
							String google_analytics_response = null;
							JSONParser parser = new JSONParser();
							google_analytics_response = GoogleAnalyticsReportingSample
									.getGAReport("input", accessToken, str_start_date, str_start_date, view_id)
									.toString();
							JSONObject ga_json_obj = (JSONObject) parser.parse(google_analytics_response.toString());
							logger.info("ga_json_obj : " + ga_json_obj.toString());
							JSONArray ga_data_json_arr = (JSONArray) ga_json_obj.get("data");
							if (ga_data_json_arr.size() > 0) {
								AnalyticsDataInsertUpdate.saveTempGAData("google_analytics_data_temp",
										ga_json_obj.get("data").toString(), username);
								AnalyticsDataInsertUpdate.saveRecentTempGAData("google_analytics_recent_data_temp",
										ga_json_obj.get("data").toString(), username);
							}
						} catch (ParseException var22) {
							logger.info("ParseException");
						}

						logger.info("Going... to Get GAData For GA User : '" + username + "' ENDED....");
					} else {
						logger.info(
								"Your Refresh Token is expire !\nYou Need to Login In Google Analytics Account Through LeadAutoConveter UI.");
					}
				} catch (Exception var23) {
					logger.info("exce " + var23);
				}
			}
		} else {
			logger.info(
					"No Google Analytics Account Found !\nYou Need to Login In Google Analytics Account Through LeadAutoConveter UI.");
		}

	}

	public static JSONObject getTokenByRefreshToken(String refresh_token) {
		JSONObject respose_json_object = null;
		HttpURLConnection conn = null;
		String response_code = null;
		String res = "10";
		logger.info("refresh_token=  " + refresh_token);

		try {
			String client_id = ResourceBundle.getBundle("config").getString("client_id");
			res = res + "client_id:: " + client_id;
			String client_secret = ResourceBundle.getBundle("config").getString("client_secret");
			res = res + "1";
			String post_data_content = "grant_type=refresh_token&refresh_token=" + refresh_token + "&client_id="
					+ client_id + "&client_secret=" + client_secret;
			String url_str = ResourceBundle.getBundle("config").getString("oauth2_token_url");
			res = res + "2";
			URL url = new URL(url_str);
			logger.info("url_str = " + url_str);
			logger.info("post_data_content = " + post_data_content);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.0.5) Gecko/2008120122 Firefox/3.0.5");
			conn.setRequestProperty("Content-Length", String.valueOf(post_data_content.length()));
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestMethod("POST");
			conn.setInstanceFollowRedirects(false);
			res = res + "3";
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream()), true);
			res = res + "4";
			pw.print(post_data_content);
			pw.flush();
			pw.close();
			response_code = String.valueOf(conn.getResponseCode());
			res = res + "5";
			if (conn.getResponseCode() != 200) {
				res = res + "6";
				respose_json_object = new JSONObject();
				respose_json_object.put("response_code", response_code);
			} else {
				res = res + "7";
				String newLine = System.getProperty("line.separator");
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder result = new StringBuilder();

				String line;
				for (boolean flag = false; (line = reader.readLine()) != null; flag = true) {
					result.append(flag ? newLine : "").append(line);
				}

				res = res + "result.toString()" + result.toString();
				logger.info("result.toString = " + result.toString());
				respose_json_object = (JSONObject) (new JSONParser()).parse(result.toString());
				respose_json_object.put("response_code", response_code);
				logger.info("result.respose_json_object = " + respose_json_object);
			}

			conn.disconnect();
			System.out.println("res== " + res);
		} catch (Exception var19) {
			System.out.println("exc== " + var19);
			var19.printStackTrace();
			respose_json_object = new JSONObject();
			respose_json_object.put("res", res);
			respose_json_object.put("response_exc", var19.toString());
		} finally {
			conn.disconnect();
		}

		return respose_json_object;
	}
}