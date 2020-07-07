package com.db.mongo.ga;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AnalyticsDataInsertUpdate {
	static final Logger logger = Logger.getLogger(AnalyticsDataInsertUpdate.class);

	public static JSONArray findUniqueUrlsBasedSessionID(String session_id) {
		MongoDatabase database = null;
		MongoCollection<Document> temp_collection = null;
		JSONArray campaignJsonArr = new JSONArray();
		System.out.println("Inside  findSubscribersBasedOncampaignNameAndDimension2: ");

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			temp_collection = database.getCollection("google_analytics_data_temp");
			FindIterable<Document> docs = temp_collection.find((new Document()).append("sessionCount", session_id));
			Iterator var6 = docs.iterator();

			while (var6.hasNext()) {
				Document doc = (Document) var6.next();
				System.out.println("session_id : " + session_id + " : pagePath: " + doc.getString("pagePath"));
			}
		} catch (Exception var10) {
			var10.printStackTrace();
			throw new RuntimeException(var10);
		} finally {
			temp_collection = null;
			database = null;
		}

		return campaignJsonArr;
	}

	public static JSONArray findGAUserCredentials() {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		JSONArray ga_users_json_arr = null;

		try {
			ga_users_json_arr = new JSONArray();
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection("google_analytics_user_credentials");
			String username = null;
			String free_trail_status = null;
			FindIterable<Document> docs = collection.find();
			JSONObject ga_user_json_obj = null;
			String rftoken = null;
			Iterator var9 = docs.iterator();

			while (var9.hasNext()) {
				Document doc = (Document) var9.next();
				logger.info("inside GoogleAnalytics User Found : ");

				try {
					ga_user_json_obj = new JSONObject();
					ga_user_json_obj.put("username", doc.getString("username"));
					logger.info("inside GoogleAnalytics User Found : " + doc.getString("username") + " refreshToken:: "
							+ doc.getString("refreshToken"));
					rftoken = doc.getString("refreshToken");
					logger.info("inside GoogleAnalytics User Found : " + rftoken);
					ga_user_json_obj.put("refreshToken", rftoken.replace("\\", ""));
					ga_user_json_obj.put("accessToken", doc.getString("accessToken"));
					ga_user_json_obj.put("view_name", doc.getString("view_name"));
					ga_user_json_obj.put("view_id", doc.getString("view_id"));
					logger.info("inside GoogleAnalytics Userga_user_json_obj  " + ga_user_json_obj);
					ga_users_json_arr.add(ga_user_json_obj);
					if (doc.getString("username").equals("bluealgo.ga@gmail.com")) {
						JSONObject ga_user_json_objLACviewid = new JSONObject();
						ga_user_json_objLACviewid.put("username", doc.getString("username"));
						rftoken = doc.getString("refreshToken");
						ga_user_json_objLACviewid.put("refreshToken", rftoken.replace("\\", ""));
						ga_user_json_objLACviewid.put("accessToken", doc.getString("accessToken"));
						ga_user_json_objLACviewid.put("view_name", doc.getString("view_name"));
						ga_user_json_objLACviewid.put("view_id", "206382866");
						ga_users_json_arr.add(ga_user_json_objLACviewid);
					}
				} catch (Exception var15) {
					logger.info("inside GoogleAnalytics User Found for loop: " + var15.getMessage());
				}
			}

			logger.info("inside GoogleAnalytics User ga_users_json_arr : " + ga_users_json_arr);
			logger.info("Total Number of GoogleAnalytics User Found : " + ga_users_json_arr.size());
		} catch (Exception var16) {
			var16.printStackTrace();
		} finally {
			database = null;
			collection = null;
		}

		return ga_users_json_arr;
	}

	public static void insertGADataTimeStamp(String timestamp) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		new JSONArray();

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection("google_analytics_timestamp");
			collection.deleteMany(new Document());
			Document timestampDoc = new Document();
			timestampDoc.put("timestamp", timestamp);
			collection.insertOne(timestampDoc);
		} catch (Exception var8) {
			var8.printStackTrace();
		} finally {
			database = null;
			collection = null;
		}

	}

	public static String saveTempGAData(String collection_name, String ga_json_obj, String username) {
		System.out.println("Going... To Save GAData In MongoDB Collection : 'google_analytics_data_temp'");
		logger.info("Going... To Save GAData In MongoDB Collection : 'google_analytics_data_temp'");
		MongoDatabase database = null;
		MongoCollection temp_collection = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			temp_collection = database.getCollection(collection_name);
			JSONParser parser = new JSONParser();
			logger.info("Total ga_json_obj :ga_json_obj '" + ga_json_obj);
			JSONArray data_json_arr = null;
			Document document = null;
			ArrayList documents = new ArrayList();

			try {
				data_json_arr = (JSONArray) parser.parse(ga_json_obj);

				for (int i = 0; i < data_json_arr.size(); ++i) {
					JSONObject data_json_obj = (JSONObject) data_json_arr.get(i);
					document = new Document();
					document.put("ga_username", username);
					document.put("country", data_json_obj.get("ga:country").toString());
					document.put("pagePath", data_json_obj.get("ga:pagePath").toString());
					document.put("sessionCount", data_json_obj.get("ga:sessionCount").toString());
					document.put("dimension2", data_json_obj.get("ga:dimension2").toString().trim());
					document.put("channelGrouping", data_json_obj.get("ga:channelGrouping").toString());
					document.put("sessionDurationBucket", data_json_obj.get("ga:sessionDurationBucket").toString());
					document.put("hostname", data_json_obj.get("ga:hostname").toString());
					document.put("url",
							data_json_obj.get("ga:hostname").toString() + data_json_obj.get("ga:pagePath").toString());
					document.put("timeOnPage", data_json_obj.get("timeOnPage").toString());
					document.put("bounces", data_json_obj.get("bounces").toString());
					document.put("sourceMedium", data_json_obj.get("ga:sourceMedium").toString());
					document.put("dateHourMinute", data_json_obj.get("ga:dateHourMinute").toString());
					documents.add(document);
				}

				temp_collection.insertMany(documents);
			} catch (Exception var15) {
				logger.info("Inside saveTempGAData() Method Got Error : " + var15);
				System.out.println("Inside saveTempGAData() Method Got Error : " + var15.getMessage());
			}
		} catch (Exception var16) {
			logger.info("Inside saveTempGAData() Method Got Error : " + var16.getMessage());
		} finally {
			database = null;
			temp_collection = null;
		}

		return "true";
	}

	public static String saveRecentTempGAData(String collection_name, String ga_json_obj, String username) {
		System.out.println("Going... To Save GAData In MongoDB Collection : 'google_analytics_recent_data_temp'");
		logger.info("Going... To Save GAData In MongoDB Collection : 'google_analytics_recent_data_temp'");
		MongoDatabase database = null;
		MongoCollection recent_temp_collection = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			recent_temp_collection = database.getCollection(collection_name);
			JSONParser parser = new JSONParser();
			logger.info("Total ga_json_obj : '" + ga_json_obj);
			JSONArray data_json_arr = null;
			Document document = null;
			ArrayList documents = new ArrayList();

			try {
				data_json_arr = (JSONArray) parser.parse(ga_json_obj);

				for (int i = 0; i < data_json_arr.size(); ++i) {
					JSONObject data_json_obj = (JSONObject) data_json_arr.get(i);
					document = new Document();
					document.put("ga_username", username);
					document.put("country", data_json_obj.get("ga:country").toString());
					document.put("pagePath", data_json_obj.get("ga:pagePath").toString());
					document.put("sessionCount", data_json_obj.get("ga:sessionCount").toString());
					document.put("dimension2", data_json_obj.get("ga:dimension2").toString().trim());
					document.put("channelGrouping", data_json_obj.get("ga:channelGrouping").toString());
					document.put("sessionDurationBucket", data_json_obj.get("ga:sessionDurationBucket").toString());
					document.put("hostname", data_json_obj.get("ga:hostname").toString());
					document.put("url",
							data_json_obj.get("ga:hostname").toString() + data_json_obj.get("ga:pagePath").toString());
					document.put("timeOnPage", data_json_obj.get("timeOnPage").toString());
					document.put("bounces", data_json_obj.get("bounces").toString());
					document.put("sourceMedium", data_json_obj.get("ga:sourceMedium").toString());
					document.put("dateHourMinute", data_json_obj.get("ga:dateHourMinute").toString());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String ga_date_hour_minute = data_json_obj.get("ga:dateHourMinute").toString();
					String final_date_str = ga_date_hour_minute.substring(0, 4) + "-"
							+ ga_date_hour_minute.substring(4, 6) + "-" + ga_date_hour_minute.substring(6, 8);
					String date_str = final_date_str + " " + ga_date_hour_minute.substring(8, 10) + ":"
							+ ga_date_hour_minute.substring(10, 12) + ":00";
					Date final_date = sdf.parse(final_date_str);
					document.put("dateHourMinuteInDateFormat", final_date);
					document.put("recordInsertionDate", new Date());
					documents.add(document);
				}

				recent_temp_collection.insertMany(documents);
			} catch (Exception var21) {
				logger.info("Inside saveTempGAData() Method Got Error : " + var21.getMessage());
				System.out.println("Inside saveTempGAData() Method Got Error : " + var21.getMessage());
			}
		} catch (Exception var22) {
			logger.info("Inside saveTempGAData() Method Got Error : " + var22);
		} finally {
			recent_temp_collection = null;
			database = null;
		}

		return "true";
	}

	public static String saveGAUserEmailAndRefreshTokenData(String username, String accessToken, String refreshToken,
			String view_name, String view_id) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		Object var7 = null;

		try {
			logger.info("Inside saveGAUserEmailAndRefreshTokenData");
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection("google_analytics_user_credentials");
			BasicDBObject document = new BasicDBObject();
			document.put("username", username);
			document.put("accessToken", accessToken);
			refreshToken = refreshToken.replaceAll("\\", "");
			document.put("refreshToken", refreshToken);
			document.put("view_name", view_name);
			document.put("view_id", view_id);
			Bson updtfilter = Filters.and(new Bson[]{Filters.eq("username", username)});
			collection.updateOne(updtfilter, new Document("$set", document), (new UpdateOptions()).upsert(true));
			logger.info("Inside saveGAUserEmailAndRefreshTokenData ga_status  token inserted: " + document);
		} catch (Exception var13) {
			var13.printStackTrace();
		} finally {
			collection = null;
			database = null;
		}

		return "Success";
	}
}