package com.db.mongo.ga;

import static com.mongodb.client.model.Filters.*;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.leadconverter.freetrail.CheckValidUserforFreetrialAndCart;
import com.leadconverter.freetrail.FreeTrialandCart;
import com.mongodb.*;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSON;
import com.rest.api.Test;

public class GAMongoDAO {

	final static Logger logger = Logger.getLogger(GAMongoDAO.class);
	private static MongoClient mongoClient;

	public static void main(String[] args) throws Exception {

		// Execution Start From This Main Method
		// GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
		// GAMongoDAO.fetchGADataForRuleEngine("google_analytics_data_temp");
		// JSONArray jsa =
		// GAMongoDAO.fetchGADataForRuleEngine("google_analytics_data_temp");
//		System.out.println("jsa== "+jsa);
		// db.google_analytics_url_view_collection.find({"Funnel_Name" :
		// "funneltest13","SubFunnel_Name" : "Explore","Campaign_Id":"1480"}).pretty();
		// http://bizlem.io:8087/GALeadConverter/rest/biz/callruleng/1526/Explore/Aug2219New
//		JSONObject jsm=	GAMongoDAO.fetchGADataForMonitoring("google_analytics_recent_data_temp", "1480", "Explore", "funneltest13");
//		System.out.println("jsm== "+jsm);

		// saveGADataForRecentView("google_analytics_data_temp");
		// saveGADataForCampaignView("google_analytics_data_temp");

		// org.json.JSONObject
		// recent_arr=fetchRecentGADataForRuleEngine("google_analytics_recent_data_temp","phplist1051
		// / email","purva.sawant@bizlem.com");
		// System.out.println("recent_arr : "+recent_arr);

		// org.json.JSONObject
		// most_recent_arr=fetchMostRecentGADataForRuleEngine("google_analytics_recent_data_temp","phplist1051
		// / email","purva.sawant@bizlem.com");
		// System.out.println("most_recent_arr : "+most_recent_arr);
		// fetchRecentGADataForRuleEngineTest("google_analytics_recent_data_temp","phplist1051
		// / email","purva.sawant@bizlem.com");

		/*
		 * org.json.JSONObject rule_json_object_1=new org.json.JSONObject();
		 * rule_json_object_1.put("Name", "Akhilesh"); rule_json_object_1.put("SurName",
		 * "Yadav"); System.out.println("Before : "+rule_json_object_1);
		 * org.json.JSONObject rule_json_object_2=new org.json.JSONObject();
		 * rule_json_object_2.put("Address", "Mulund"); rule_json_object_2.put("Phone",
		 * "9819384655"); rule_json_object_2.put("Name", "Akhilesh Uday");
		 * 
		 * System.out.println(mergeJSONObject(rule_json_object_1,rule_json_object_2));
		 */

		/*
		 * Document doc=new Document(); doc.put("Name", "Akhilesh Yadav");
		 * System.out.println(doc); doc.put("Name", "Akhilesh Udaypratap Yadav");
		 * System.out.println(doc);
		 */
		// createURLViewData("temp_google_analytics_subscriber_data","xy@bizlem.com");

		/*
		 * BasicDBObject newDocument = new BasicDBObject(); newDocument.append("$set",
		 * new BasicDBObject().append("clients", 110));
		 * 
		 * BasicDBObject searchQuery = new BasicDBObject().append("hosting", "hostB");
		 * 
		 * collection.update(searchQuery, newDocument);
		 * 
		 * 
		 * BasicDBObject newDocument = new BasicDBObject().append("$inc", new
		 * BasicDBObject().append("total clients", 99));
		 * 
		 * collection.update(new BasicDBObject().append("hosting", "hostB"),
		 * newDocument);
		 * 
		 */

		// findUniqueUrl("google_analytics_data_temp","");
		// findUniqueCampaignForSubscriber("google_analytics_data_temp","");
		// saveSubscriberTempData("google_analytics_data_temp","akhilesh@bizlem.com");
		// GetCampaignDetailsBasedOnCampaignIdAndSubscriberIdFromMongo("448","nisha.ramisetty@dwtc.com");
		// GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId("google_analytics_data_temp","phplist1051
		// / email","purva.sawant@bizlem.com");
		// findUniqueSubscriber("google_analytics_data_temp");
		// findUniqueCampaign("google_analytics_data_temp");

		// pagePath sessionCount dimension2 channelGrouping sessionDurationBucket
		// sessiondurationBucket hostname timeOnPage bounces sourceMedium dateHourMinute
	}

	public static long getSubscriberCountForLoggedInUserForFreeTrail(String coll_name, String logged_in_user_email) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> campaign_collection = null;
		long subscriber_count = 0;
		MongoClientURI connectionString = null;
		try {
//	        mongoClient=ConnectionHelper.getConnection();
			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("phplisttresourcesest");
			collection = database.getCollection(coll_name);
			Bson filter = eq("CreatedBy", logged_in_user_email);
			subscriber_count = collection.count(filter);
			System.out.println("subscriber_count : " + subscriber_count);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			// ConnectionHelper.closeConnection(mongoClient);

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);

		}
		return subscriber_count;
	}

	public static JSONArray fetchGADataForRuleEngine(String coll_name) {
		MongoCollection<Document> temp_collection = null;
		MongoCollection<Document> google_analytics_url_view_collection = null;
		JSONArray pagePathJsonArr = null;
		JSONArray funnelListJsonArr = new JSONArray();
		String pagePath = null;
		String subscriber_email = null;
		ArrayList<Double> timeOnPageArrList = null;
		ArrayList<Double> avgTimeOnPageArrList = null;
		ArrayList<Integer> sessionCountArrList = null;
		ArrayList<String> dateHourMinuteArrList = null;
		ArrayList<Integer> sessionDurationBucket = null;
		ArrayList<String> hostNameArrList = null;
		TreeSet<String> UniqueSubscribers = new TreeSet<String>();
		org.json.JSONArray finalJsonArrayForRuleEngine = new org.json.JSONArray();

		String source = null;
		Map<String, String> SubscriberMoveMap = null;

		String Source = null;
		String Subscriber_Email = null;
		String location = null;
		String campaign_id = null;
		String ga_user = null;
		String hostname = null;
		String sourceMedium = null;
		String url = null;
		double TotalTimeOnPage = 0;
		int NoOfUrlClicks = 0;
		int TotalSesionDuration = 0;
		int SessionCount = 0;
		String Created_By = null;
		String Funnel_Name = null;
		String Parentfunnel = null;
		String group = null;
		String Category = null;

		String lastclick = null;
		double AvgTimeOnPage = 0;
		double MinTimeOnPage = 0;
		double MaxTimeOnPage = 0;
		int LastSessionCount = 0;
		double AvgSesionDuration = 0;
		int MinSesionDuration = 0;
		int MaxSesionDuration = 0;
		String firstclick = null;
		ArrayList<Integer> LastSessionCountArrList = null;
		ArrayList<Double> AvgSesionDurationArrList = null;
		logger.info("Calling fetchGADataForRuleEngine : +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=");
		logger.info("Calling fetchGADataForRuleEngine : ");
		
		MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> google_analytics_data_temp = null;
		org.json.JSONObject rulejssave = null;
		@SuppressWarnings("unused")
		JSONArray ruleurlarr = new JSONArray();
		org.json.JSONObject rule_json_object = null;
		int totalclicks = 0;
		org.json.JSONArray rulengarr = null;
		try {

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			google_analytics_data_temp = database.getCollection(coll_name);
			google_analytics_url_view_collection = database.getCollection("google_analytics_url_view_collection");

			DistinctIterable<String> dimension2Di = google_analytics_data_temp.distinct("dimension2", String.class);
			MongoCursor<String> dimension2Cursor = dimension2Di.iterator();
			// int count=0;
			try {
				while (dimension2Cursor.hasNext()) {
					subscriber_email = dimension2Cursor.next().toString();
					if (subscriber_email.contains("@") && !subscriber_email.equals("akhilesh@bizlem.com")) {

						UniqueSubscribers.add(subscriber_email);

					}
				}
			} finally {
				dimension2Cursor.close();
			}
//	        UniqueSubscribers.add("mohit.raj@bizlem.com");
//	        UniqueSubscribers.add("vivek@bizlem.com");
			// System.out.println("Total Number of Subscribers Found : " +
			// UniqueSubscribers);
			logger.info(UniqueSubscribers.size() + "Total Number of Subscribers Found  : " + UniqueSubscribers);
			if (UniqueSubscribers.size() > 0) {
				for (String temp_subscriber_email : UniqueSubscribers) {
					// System.out.println("temp_subscriber_email : "+temp_subscriber_email);
					logger.info("Subscriber Email : " + temp_subscriber_email);
					Bson filter1 = and(eq("dimension2", temp_subscriber_email));
					DistinctIterable<String> sourceMediumDi = google_analytics_data_temp.distinct("sourceMedium",
							filter1, String.class);
					MongoCursor<String> sourceMediumCursor = sourceMediumDi.iterator();

					try {
						while (sourceMediumCursor.hasNext()) {
							sourceMedium = sourceMediumCursor.next().toString();
							if (sourceMedium.contains("email")) {
								campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1)
										.replace("email", "").trim();
								source = "Email";
							} else if (sourceMedium.contains("(direct)")) {
								source = "Direct";
								campaign_id = "NULL";
							}else if (sourceMedium.contains("(not set)")) {
								source = "not set";
								campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1)
										.replace("not set", "").trim();
								
								logger.info("source=direct ;" + source + " :: campaign_id= " + campaign_id);
							} 
							campaign_id=campaign_id.trim();
							logger.info("Campaign Source For Subscriber : " + sourceMedium);
							logger.info("campaign_id For Subscriber : " + campaign_id);
							rule_json_object = new org.json.JSONObject();
							rulejssave = new org.json.JSONObject();
							// rule_json_object.put("Subscriber_Email",temp_subscriber_email);
							// rule_json_object.put("sourceMedium",sourceMedium);
							Bson filter2 = null;
							// System.out.println("-----------Unique sourceMedium : "+sourceMedium+" :
							// sourceMedium Unique-------------------------");
							logger.info("Campaign Source For Subscriber : " + sourceMedium);
							Bson page_path_filter = and(eq("dimension2", temp_subscriber_email),
									eq("sourceMedium", sourceMedium));
							Bson filter = and(eq("dimension2", temp_subscriber_email),
									eq("sourceMedium", sourceMedium));
							pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(google_analytics_data_temp,
									page_path_filter);
							logger.info("Campaign Source For pagePathJsonArr ::: : " + pagePathJsonArr);
							LastSessionCountArrList = new ArrayList<Integer>();
							AvgSesionDurationArrList = new ArrayList<Double>();
							logger.info("Total Url Found In Campaign : " + pagePathJsonArr.size());
							JSONArray urlarr = new JSONArray();
							for (int i = 0; i < pagePathJsonArr.size(); i++) {
								// Creating New Document Object For URL VIEW
								pagePath = (String) pagePathJsonArr.get(i);

								Bson host_name_filter = and(eq("pagePath", pagePath),
										eq("dimension2", temp_subscriber_email), eq("sourceMedium", sourceMedium));
								hostname = GetHostNameBasedOnCampaignIdAndSubscriberId(google_analytics_data_temp,
										host_name_filter);

								logger.info("hostname= " + hostname);
								if (pagePath.length() == 1) {
									pagePath = hostname;
									logger.info("pagePath= " + pagePath);
								} else {
									if (pagePath.contains("/?")) {
										pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
										// System.out.println(path1);
									} else if (pagePath.contains("?")) {
										pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
									} else if (pagePath.contains("/")) {
										pagePath = hostname + pagePath;
									}
								}

								// System.out.println("pagePath : "+pagePath);
								logger.info("URL Found In Campaign : " + pagePath);
								timeOnPageArrList = new ArrayList<Double>();
								sessionCountArrList = new ArrayList<Integer>();
								dateHourMinuteArrList = new ArrayList<String>();
								sessionDurationBucket = new ArrayList<Integer>();
								hostNameArrList = new ArrayList<String>();
								avgTimeOnPageArrList = new ArrayList<Double>();

								filter2 = and(eq("sourceMedium", sourceMedium), eq("url", pagePath),
										eq("Subscriber_Email", temp_subscriber_email));
								FindIterable<Document> campaignWisePagePathFi = google_analytics_url_view_collection
										.find(filter2);
								MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
								int count = 0;

								while (campaignWisePagePathCursor.hasNext()) {
									Document campaignWisePagePath = campaignWisePagePathCursor.next();
									if (count == 0) {

										logger.info("campaignWisePagePath = " + campaignWisePagePath);
										Source = campaignWisePagePath.getString("Source");
										Subscriber_Email = campaignWisePagePath.getString("Subscriber_Email");
										campaign_id = campaignWisePagePath.getString("Campaign_id");
										ga_user = campaignWisePagePath.getString("ga_user");
										hostname = campaignWisePagePath.getString("hostname");
										location = campaignWisePagePath.getString("location");
										sourceMedium = campaignWisePagePath.getString("sourceMedium");
										url = campaignWisePagePath.getString("url");
										TotalTimeOnPage = campaignWisePagePath.getDouble("TotalTimeOnPage");
										NoOfUrlClicks = campaignWisePagePath.getInteger("NoOfUrlClicks");
										TotalSesionDuration = campaignWisePagePath.getInteger("TotalSesionDuration");
										SessionCount = campaignWisePagePath.getInteger("SessionCount");
										Created_By = campaignWisePagePath.getString("Created_By");
										Funnel_Name = campaignWisePagePath.getString("Funnel_Name");
//									Parentfunnel
										Parentfunnel = campaignWisePagePath.getString("Parentfunnel");
										group = campaignWisePagePath.getString("group");
										logger.info("Funnel_Name = " + Funnel_Name);

										Category = campaignWisePagePath.getString("Category");
										// Campaign_Id = campaignWisePagePath.getString("Campaign_id");
										lastclick = campaignWisePagePath.getString("lastclick");
										AvgTimeOnPage = campaignWisePagePath.getDouble("AvgTimeOnPage");
										MinTimeOnPage = campaignWisePagePath.getDouble("MinTimeOnPage");
										MaxTimeOnPage = campaignWisePagePath.getDouble("MaxTimeOnPage");
										LastSessionCount = campaignWisePagePath.getInteger("LastSessionCount");
										AvgSesionDuration = campaignWisePagePath.getDouble("AvgSesionDuration");
										MinSesionDuration = campaignWisePagePath.getInteger("MinSesionDuration");
										MaxSesionDuration = campaignWisePagePath.getInteger("MaxSesionDuration");
										firstclick = campaignWisePagePath.getString("firstclick");

										rule_json_object.put("TotalSesionDuration", TotalSesionDuration);

										rule_json_object.put("Source", Source);
										rule_json_object.put("SubscriberEmail", Subscriber_Email);
										rule_json_object.put("CampaignId", campaign_id);
										rule_json_object.put("GAUser", ga_user);
										rule_json_object.put("HostName", hostname);
										rule_json_object.put("SourceMedium", sourceMedium);

										rule_json_object.put("CreatedBy", Created_By);
										rule_json_object.put("FunnelName", Parentfunnel);
										rule_json_object.put("ChildFunnelName", Funnel_Name);
										rule_json_object.put("SubFunnelName", Category);
										rule_json_object.put("Category", Category);
										rule_json_object.put("group", group);

										// for monitoring
										logger.info(
												"NoOfUrlClicks= " + NoOfUrlClicks + " :: totalclicks = " + totalclicks);
										totalclicks = totalclicks + NoOfUrlClicks;
										logger.info("hostname= " + NoOfUrlClicks);
										rulejssave.put("TotalSesionDuration", TotalSesionDuration);
										rulejssave.put("Source", Source);
										rulejssave.put("SubscriberEmail", Subscriber_Email);
										rulejssave.put("location", location);
										rulejssave.put("CampaignId", campaign_id);
										rulejssave.put("GAUser", ga_user);
										rulejssave.put("HostName", hostname);
										rulejssave.put("SourceMedium", sourceMedium);
										// rule_json_object.put("url",url);
										rulejssave.put("CreatedBy", Created_By);
										rulejssave.put("FunnelName", Parentfunnel);
										rulejssave.put("ChildFunnelName", Funnel_Name);
										rulejssave.put("SubFunnelName", Category);
										rulejssave.put("Category", Category);
										rulejssave.put("group", group);

										JSONObject urljs = new JSONObject();
										urljs.put("url", pagePath);
										urljs.put("AvgTimeOnPage", campaignWisePagePath.getDouble("AvgTimeOnPage"));
										System.out.println("URL Found In monitoring urljs: " + urljs);
										logger.info("URL Found In monitoring urljs: " + urljs);
										urlarr.add(urljs);
										System.out.println("URL Found In monitoring urlarr: " + urlarr);
										logger.info("URL Found In monitoring urlarr : " + urlarr);
									}
									AvgTimeOnPage = campaignWisePagePath.getDouble("AvgTimeOnPage");

									logger.info("URL Found In monitoring : " + rulejssave);

									rule_json_object.put(pagePath, AvgTimeOnPage);
									AvgSesionDurationArrList.add(campaignWisePagePath.getDouble("AvgSesionDuration"));
									LastSessionCountArrList.add(campaignWisePagePath.getInteger("LastSessionCount"));
									finalJsonArrayForRuleEngine.put(campaignWisePagePath.toJson());
									++count;
								}
							}

							rulejssave.put("NoOfUrlClicks", totalclicks);
							rulejssave.put("PageUrls", urlarr);
							logger.info("URL Found In monitoring urlarr rulejssave : " + rulejssave);
							Map<String, String> AvgSesionDurationMap = ArrayListOperationsForDoubleValue(
									AvgSesionDurationArrList);
							rule_json_object.put("AvgSesionDuration", AvgSesionDurationMap.get("Average"));
							rulejssave.put("AvgSesionDuration", AvgSesionDurationMap.get("Average"));

							// System.out.println("AvgSesionDurationMap Hi Akhilesh :
							// "+AvgSesionDurationMap.get("Average"));
							// System.out.println("AvgSesionDurationMap Hi Akhilesh :
							// "+AvgSesionDurationMap.get("Average"));
							Map<String, String> LastSessionCountMap = ArrayListOperationsForIntegerValue(
									LastSessionCountArrList);
							rule_json_object.put("SessionCount", LastSessionCountMap.get("Max"));
							rulejssave.put("AvgSesionDuration", AvgSesionDurationMap.get("Average"));

							logger.info("URL Found In monitoring urlarr rulejssave 1111: " + rulejssave);

							org.json.JSONObject recent_gadata_json_obj = fetchRecentGADataForRuleEngine(
									"google_analytics_recent_data_temp", sourceMedium, temp_subscriber_email);

							rulejssave.put("Recent_SessionCount", recent_gadata_json_obj.get("Recent_SessionCount"));
							rulejssave.put("Recent_AvgSesionDuration",
									recent_gadata_json_obj.get("Recent_AvgSesionDuration"));

							org.json.JSONObject most_recent_gadata_json_obj = fetchMostRecentGADataForRuleEngine(
									"google_analytics_recent_data_temp", sourceMedium, temp_subscriber_email);

							rule_json_object = mergeJSONObject(rule_json_object,
									mergeJSONObject(recent_gadata_json_obj, most_recent_gadata_json_obj));
							logger.info("URL Found In monitoring urlarr recent_gadata_json_obj 1111: "
									+ recent_gadata_json_obj);
							logger.info("URL Found In monitoring urlarr most_recent_gadata_json_obj 1111: "
									+ most_recent_gadata_json_obj);
							// rulejssave=mergeJSONObject(rulejssave,);
							// logger.info("URL Found In monitoring urlarr rulejssave 88: " + rulejssave);
							logger.info("URL Found In monitoring urlarr rulejssave: " + rulejssave);
							System.out.println("Aftermergerulejssavefinal : " + rulejssave);
							System.out.println("After Merge rule_json_object : " + rule_json_object);
							funnelListJsonArr.add(rulejssave);
							logger.info("Call  rule_json_object as INPUT : " + rule_json_object
									+ "      ::::::: Created_By ::::" + Created_By + ":: Funnel_Name :: "
									+ Parentfunnel);
//						Created_By = "salesautoconvertuser1@gmail.com";
//						Funnel_Name = "GAURLMergeTail";

//					    String free_trail_status=null;
//					    
//					    free_trail_status=new FreeTrialandCart().checkFreeTrialExpirationStatus(Created_By.replace("_", "@"));
							// System.out.println(campaign_details_doc);
							// callrule engine and fire rule
							// call shopping cart method
							if (Created_By != null && Parentfunnel != null) {
								String validuserresp = CheckValidUserforFreetrialAndCart
										.checkValiditytrialCart(Created_By);
//					    logger.info("free_trail_status = "+free_trail_status);
//					    if(free_trail_status.equals("0")){
								JSONParser parser = new JSONParser();
								JSONObject validjs = (JSONObject) parser.parse(validuserresp);
								logger.info("validjs = " + validjs);
								if (validjs.containsKey("status") && validjs.get("status").equals("true")) {
									// insert analytics data to RuleEngineCalledForSubscriberData for monitoring
									logger.info("insertruleDataforMonitoring inserting = ");
//								GAMongoDAO.insertruleDataforMonitoring(rulejssave.toString(), Subscriber_Email,
//										Category, Funnel_Name, campaign_id, Created_By);
									// end
//								try {
//
//									logger.info("callRuleEngine : " + Funnel_Name);
//									callRuleEngine(rule_json_object.toString(), Funnel_Name, rulejssave);
//
//								} catch (Exception e) {
//									logger.info("exc in callRuleEngine" + e);
//								}

									try {

										GAMongoDAO.insertruleDataforMonitoring(rulejssave.toString(),
												rulejssave.get("SubscriberEmail").toString(),
												rulejssave.get("Category").toString(),
												rulejssave.get("ChildFunnelName").toString(),
												rulejssave.get("CampaignId").toString(),
												rulejssave.get("CreatedBy").toString());
										logger.info("inserted category");
										logger.info("Calling fetchGADataForRuleEngine : --------------------------------");
										// jsruleengnames= new CallGetService().getRuleEngines(Created_By,Funnel_Name);
										rulengarr = new CallGetService().getRuleEngines(Created_By, Parentfunnel);
										logger.info("callRuleEngine : " + Parentfunnel + " ::rulengarr =" + rulengarr);
										if (rulengarr.length() > 0 && rulengarr != null) {
											for (int j = 0; j < rulengarr.length(); j++) {
												String ruleeng = (String) rulengarr.get(j);
												if (!ruleeng.isEmpty() && ruleeng != null) {
													logger.info("ruleeng : " + ruleeng);
													callRuleEngine(rule_json_object.toString(), Parentfunnel, ruleeng,
															Created_By, rulejssave.toString());
												}
											}
										}
									} catch (Exception e) {
										logger.info("exc in callRuleEngine" + e);
									}

								} else {
									System.out.println("Freetrail Expired for User : " + Created_By.replace("_", "@"));
									logger.info("Freetrail Expired for User : " + Created_By.replace("_", "@"));
								}
							}
							// break;
						}
					} finally {
						sourceMediumCursor.close();
					}
					// System.out.println("finalJsonArrayForRuleEngine :
					// "+finalJsonArrayForRuleEngine);

					// break;
				}
			}

			// end for loop

			// remove google_analytics_data_temp collection data

			// google_analytics_data_temp.deleteMany(new Document()); commented by tejal
			logger.info("google_analytics_data_temp removed  : ");

		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
			System.out.println("Exception : " + e.getMessage());
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);
		}
		return funnelListJsonArr;
	}

	private void retrieveSubscriberList(HashMap<String, org.json.JSONArray> templateMp2) {
		System.out.println("Enter Map iterator");
		for (Map.Entry mapElement : templateMp2.entrySet()) {
			String key = (String) mapElement.getKey();

			// Add some bonus marks
			// to all the students and print it
			org.json.JSONArray value = (org.json.JSONArray) mapElement.getValue();
			// HashMap<String,List<String>> map = new HashMap<String,List<String>>();
			List<String> list = new ArrayList<>();
			for (int i = 0; i < value.length(); i++) {
				org.json.JSONObject objects = value.getJSONObject(i);
				list.add("");// retrieve subscriber email
				/*
				 * if(map.containsKey(objects.get(""))) { List<String> list = map.get("");
				 * map.remove("", list); list.add(""); map.put("", list);
				 * 
				 * }else {
				 * 
				 * list.add(""); map.put("", list); //value }
				 */
			}
			// post with list key

		}
	}

	public static org.json.JSONArray fetchGADataForRuleEngine(Document search_query, Document url_view_set_doc,
			Document url_view_inc_doc, Document url_view_set_on_insert_doc) {
		org.json.JSONArray finalJsonArrayForRuleEngine = null;
		org.json.JSONObject rule_json_object = null;

		System.out.println("fetchGADataForRuleEngine Mehtod is called : ");
		try {
			finalJsonArrayForRuleEngine = new org.json.JSONArray();
			rule_json_object = new org.json.JSONObject();
			System.out.println("search_query : " + search_query.toJson());
			System.out.println("url_view_set_doc : " + url_view_set_doc.toJson());
			System.out.println("url_view_inc_doc : " + url_view_inc_doc.toJson());
			System.out.println("url_view_set_on_insert_doc : " + url_view_set_on_insert_doc.toJson());

		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		} finally {
			System.out.println("Finally Excecuted Successfully");
		}
		return finalJsonArrayForRuleEngine;
	}

	public static org.json.JSONObject fetchRecentGADataForRuleEngine(String coll_name, String sourceMedium,
			String temp_subscriber_email) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		JSONArray pagePathJsonArr = null;
		JSONArray funnelListJsonArr = new JSONArray();
		String pagePath = null;
		String hostname = null;
		ArrayList<Double> timeOnPageArrList = null;
		ArrayList<Integer> sessionCountArrList = null;
		ArrayList<String> dateHourMinuteArrList = null;
		ArrayList<Integer> sessionDurationBucket = null;
		ArrayList<String> hostNameArrList = null;
		TreeSet<String> UniqueSubscribers = new TreeSet<String>();
		org.json.JSONArray finalJsonArrayForRuleEngine = new org.json.JSONArray();
		org.json.JSONObject rule_json_object = null;
		String campaign_id = null;
		String ga_user = null;
		String source = null;
		Map<String, String> campaignDetailsMap = null;
		int int_recent_days = 0;
		Date recent_date = null;
		MongoClientURI connectionString = null;

		try {

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();
			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String recent_days_1 = ResourceBundle.getBundle("config").getString("recent_days");
			int_recent_days = Integer.parseInt(recent_days_1);
			// int_recent_days=25;
			Date date_campare1 = new Date();
			date_campare1.setDate(date_campare1.getDate() - int_recent_days);
			recent_date = dateFormat.parse(dateFormat.format(date_campare1));

			if (sourceMedium.contains("phplist")) {
				campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1).replace("phplist", "");
				source = "Email";
			} else if (sourceMedium.contains("(direct)")) {
				source = "Direct";
				campaign_id = "NULL";
			}
			rule_json_object = new org.json.JSONObject();
			// rule_json_object.put("Subscriber_Email",temp_subscriber_email);
			// rule_json_object.put("Source_Medium",sourceMedium);
			Bson filter2 = null;
			System.out.println("-----------Unique sourceMedium : " + sourceMedium
					+ "  : sourceMedium Unique-------------------------");
			Bson page_path_filter = and(gt("dateHourMinuteInDateFormat", recent_date),
					eq("dimension2", temp_subscriber_email), eq("sourceMedium", sourceMedium));
			Bson filter = and(gt("dateHourMinuteInDateFormat", recent_date), eq("sourceMedium", sourceMedium),
					eq("dimension2", temp_subscriber_email));
			pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(collection, page_path_filter);
			if (pagePathJsonArr.size() > 0) {
				for (int i = 0; i < pagePathJsonArr.size(); i++) {
					// Creating New Document Object For URL VIEW
					pagePath = (String) pagePathJsonArr.get(i);
					System.out.println("pagePath : " + pagePath);
					timeOnPageArrList = new ArrayList<Double>();
					sessionCountArrList = new ArrayList<Integer>();
					dateHourMinuteArrList = new ArrayList<String>();
					sessionDurationBucket = new ArrayList<Integer>();
					hostNameArrList = new ArrayList<String>();
					filter2 = and(eq("sourceMedium", sourceMedium), eq("pagePath", pagePath),
							eq("dimension2", temp_subscriber_email));
					FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
					MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
					while (campaignWisePagePathCursor.hasNext()) {
						Document campaignWisePagePath = campaignWisePagePathCursor.next();
						System.out.println("campaignWisePagePath : " + campaignWisePagePath);
						ga_user = campaignWisePagePath.get("ga_username").toString();
						/*
						 * System.out.println("campaignWisePagePath : "+campaignWisePagePath.getString(
						 * "pagePath") +"    timeOnPage : "+campaignWisePagePath.getString("timeOnPage")
						 * +"    dateHourMinute : "+campaignWisePagePath.getString("dateHourMinute"));
						 */
						timeOnPageArrList.add(Double.parseDouble(campaignWisePagePath.get("timeOnPage").toString()));
						sessionCountArrList.add(Integer.parseInt(campaignWisePagePath.get("sessionCount").toString()));
						System.out.println("sessionCount : " + campaignWisePagePath.get("sessionCount").toString());
						dateHourMinuteArrList.add(campaignWisePagePath.get("dateHourMinute").toString());
						sessionDurationBucket
								.add(Integer.parseInt(campaignWisePagePath.get("sessionDurationBucket").toString()));
						hostNameArrList.add(campaignWisePagePath.get("hostname").toString());
					}
					// rule_json_object.put("GA_User",ga_user);
					if (dateHourMinuteArrList.size() > 0) {
						System.out.println("dateHourMinuteArrList : "
								+ dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
					}
					if (hostNameArrList.size() > 0) {
						hostname = hostNameArrList.get(hostNameArrList.size() - 1);
						// System.out.println("hostname : "+hostname);
						// rule_json_object.put("Host_Name",hostname);
					}
					if (pagePath.length() == 1) {
						pagePath = hostname;
						System.out.println(pagePath);
					} else {
						if (pagePath.contains("/?")) {
							pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
							// System.out.println(path1);
						} else if (pagePath.contains("?")) {
							pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
						} else if (pagePath.contains("/")) {
							pagePath = hostname + pagePath;
						}
					}

					Map<String, String> timeOnPageMap = ArrayListOperationsForDoubleValue(timeOnPageArrList);
					rule_json_object.put("Recent_" + pagePath, timeOnPageMap.get("Average").toString());
					// rule_json_object.put(pagePath,timeOnPageMap.get("Sum").toString());
					System.out.println("timeOnPageMap : " + timeOnPageMap);

					Map<String, String> sessionCountMap = ArrayListOperationsForIntegerValue(sessionCountArrList);
					// rule_json_object.put("Session_Count",sessionCountMap.get("Max"));
					// rule_json_object.put("No_OF_Clicks",sessionCountMap.get("Count"));
					// url_view_doc_object.put("Last_Session_Count",Integer.parseInt(sessionCountMap.get("Max")));
					rule_json_object.put("Recent_SessionCount", Integer.parseInt(sessionCountMap.get("Max")));

					Map<String, String> sessionDurationBucketMap = ArrayListOperationsForIntegerValue(
							sessionDurationBucket);
					Map<String, String> AvgSessionDurationBucketMap = GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId(
							collection, filter);
					System.out.println("AvgSessionDurationBucketMap== " + AvgSessionDurationBucketMap);
					System.out.println("recentavgtttttt)" + AvgSessionDurationBucketMap.get("Average"));
					rule_json_object.put("Recent_AvgSesionDuration", AvgSessionDurationBucketMap.get("Average"));
					System.out.println("rule_json_objectooooo= " + rule_json_object);

				}
				finalJsonArrayForRuleEngine.put(rule_json_object);

			} else {
				rule_json_object.put("Recent_" + pagePath, "No most recent url found");
				rule_json_object.put("Recent_SessionCount", "0");
				rule_json_object.put("Recent_AvgSesionDuration", "0");
				// rule_json_object.put("GA_User","");
				// rule_json_object.put("Host_Name","");
				// rule_json_object.put("Recent_"+pagePath,"0");
				// rule_json_object.put("Source",source);
				// rule_json_object.put("RecentTotalSessionCount","0");
				// rule_json_object.put("RecentTotalSesionDuration","0");
				// rule_json_object.put("RecentAvgSesionDuration","0");
				// rule_json_object.put("RecentMinSesionDuration","0");
				// rule_json_object.put("RecentMaxSesionDuration","0");

			}
			System.out.println("rule_json_objectmethod" + rule_json_object);

		} catch (Exception ex) {
			System.out.println("Exception : " + ex.getMessage());
		} finally {
			Bson filter2 = lte("dateHourMinuteInDateFormat", recent_date);
			// collection.deleteMany(filter2);

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);

		}
		return rule_json_object;

	}

	public static org.json.JSONObject fetchMostRecentGADataForRuleEngine(String coll_name, String sourceMedium,
			String temp_subscriber_email) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		JSONArray pagePathJsonArr = null;
		JSONArray funnelListJsonArr = new JSONArray();
		String pagePath = null;
		String hostname = null;
		ArrayList<Double> timeOnPageArrList = null;
		ArrayList<Integer> sessionCountArrList = null;
		ArrayList<String> dateHourMinuteArrList = null;
		ArrayList<Integer> sessionDurationBucket = null;
		ArrayList<String> hostNameArrList = null;
		TreeSet<String> UniqueSubscribers = new TreeSet<String>();
		org.json.JSONArray finalJsonArrayForRuleEngine = new org.json.JSONArray();
		org.json.JSONObject rule_json_object = null;
		String campaign_id = null;
		String ga_user = null;
		String source = null;
		Map<String, String> campaignDetailsMap = null;
		int int_most_recent_days = 0;
		Date most_recent_date = null;
		MongoClientURI connectionString = null;
		try {
//	        mongoClient=ConnectionHelper.getConnection();

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();
			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String recent_days_1 = ResourceBundle.getBundle("config").getString("most_recent_days");
			int_most_recent_days = Integer.parseInt(recent_days_1);
			// int_most_recent_days=25;
			Date date_campare1 = new Date();
			date_campare1.setDate(date_campare1.getDate() - int_most_recent_days);
			most_recent_date = dateFormat.parse(dateFormat.format(date_campare1));

			if (sourceMedium.contains("phplist")) {
				campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1).replace("phplist", "");
				source = "Email";
			} else if (sourceMedium.contains("(direct)")) {
				source = "Direct";
				campaign_id = "NULL";
			}
			rule_json_object = new org.json.JSONObject();
			// rule_json_object.put("Subscriber_Email",temp_subscriber_email);
			// rule_json_object.put("Source_Medium",sourceMedium);
			Bson filter2 = null;
			System.out.println("-----------Unique sourceMedium : " + sourceMedium
					+ "  : sourceMedium Unique-------------------------");
			Bson page_path_filter = and(gt("dateHourMinuteInDateFormat", most_recent_date),
					eq("dimension2", temp_subscriber_email), eq("sourceMedium", sourceMedium));
			Bson filter = and(gt("dateHourMinuteInDateFormat", most_recent_date), eq("sourceMedium", sourceMedium),
					eq("dimension2", temp_subscriber_email));
			pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(collection, page_path_filter);
			if (pagePathJsonArr.size() > 0) {
				for (int i = 0; i < pagePathJsonArr.size(); i++) {
					// Creating New Document Object For URL VIEW
					pagePath = (String) pagePathJsonArr.get(i);
					System.out.println("pagePath : " + pagePath);
					timeOnPageArrList = new ArrayList<Double>();
					sessionCountArrList = new ArrayList<Integer>();
					dateHourMinuteArrList = new ArrayList<String>();
					sessionDurationBucket = new ArrayList<Integer>();
					hostNameArrList = new ArrayList<String>();
					filter2 = and(eq("sourceMedium", sourceMedium), eq("pagePath", pagePath),
							eq("dimension2", temp_subscriber_email));
					FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
					MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
					while (campaignWisePagePathCursor.hasNext()) {
						Document campaignWisePagePath = campaignWisePagePathCursor.next();
						System.out.println("campaignWisePagePath : " + campaignWisePagePath);
						ga_user = campaignWisePagePath.get("ga_username").toString();
						/*
						 * System.out.println("campaignWisePagePath : "+campaignWisePagePath.getString(
						 * "pagePath") +"    timeOnPage : "+campaignWisePagePath.getString("timeOnPage")
						 * +"    dateHourMinute : "+campaignWisePagePath.getString("dateHourMinute"));
						 */
						timeOnPageArrList.add(Double.parseDouble(campaignWisePagePath.get("timeOnPage").toString()));
						sessionCountArrList.add(Integer.parseInt(campaignWisePagePath.get("sessionCount").toString()));
						System.out.println("sessionCount : " + campaignWisePagePath.get("sessionCount").toString());
						dateHourMinuteArrList.add(campaignWisePagePath.get("dateHourMinute").toString());
						sessionDurationBucket
								.add(Integer.parseInt(campaignWisePagePath.get("sessionDurationBucket").toString()));
						hostNameArrList.add(campaignWisePagePath.get("hostname").toString());
					}
					// rule_json_object.put("GA_User",ga_user);
					if (dateHourMinuteArrList.size() > 0) {
						System.out.println("dateHourMinuteArrList : "
								+ dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
					}
					if (hostNameArrList.size() > 0) {
						hostname = hostNameArrList.get(hostNameArrList.size() - 1);
						System.out.println("Host_Name : " + hostname);
						// rule_json_object.put("Host_Name",hostname);
					}
					if (pagePath.length() == 1) {
						pagePath = hostname;
						System.out.println(pagePath);
					} else {
						if (pagePath.contains("/?")) {
							pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
							// System.out.println(path1);
						} else if (pagePath.contains("?")) {
							pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
						} else if (pagePath.contains("/")) {
							pagePath = hostname + pagePath;
						}
					}

					Map<String, String> timeOnPageMap = ArrayListOperationsForDoubleValue(timeOnPageArrList);
					rule_json_object.put("MostRecent_" + pagePath, timeOnPageMap.get("Average").toString());
					System.out.println("timeOnPageMap : " + timeOnPageMap);

					Map<String, String> sessionCountMap = ArrayListOperationsForIntegerValue(sessionCountArrList);
					// rule_json_object.put("Session_Count",sessionCountMap.get("Max"));
					// rule_json_object.put("No_OF_Clicks",sessionCountMap.get("Count"));
					// url_view_doc_object.put("Last_Session_Count",Integer.parseInt(sessionCountMap.get("Max")));MostRecentSessionCount
					rule_json_object.put("MostRecent_SessionCount", Integer.parseInt(sessionCountMap.get("Max")));

					Map<String, String> sessionDurationBucketMap = ArrayListOperationsForIntegerValue(
							sessionDurationBucket);
					// rule_json_object.put("AvgSesionDuration",Double.parseDouble(sessionDurationBucketMap.get("Average")));
					// rule_json_object.put("MinSesionDuration",Integer.parseInt(sessionDurationBucketMap.get("Min")));
					// rule_json_object.put("MaxSesionDuration",Integer.parseInt(sessionDurationBucketMap.get("Max")));
					// rule_json_object.put("TotalSesionDuration",Integer.parseInt(sessionDurationBucketMap.get("Sum")));

					// rule_json_object.put("Source",source);
					Map<String, String> AvgSessionDurationBucketMap = GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId(
							collection, filter);
					// rule_json_object.put("MostRecentTotalSessionCount",AvgSessionDurationBucketMap.get("Count"));
					// rule_json_object.put("MostRecentTotalSessionTime",AvgSessionDurationBucketMap.get("Sum"));
					// rule_json_object.put("MostRecentAvgSessionTime",AvgSessionDurationBucketMap.get("Average"));
					// rule_json_object.put("MostRecentMinSessionTime",AvgSessionDurationBucketMap.get("Min"));
					// rule_json_object.put("MostRecentMaxSessionTime",AvgSessionDurationBucketMap.get("Max"));
					rule_json_object.put("MostRecent_AvgSesionDuration", AvgSessionDurationBucketMap.get("Average"));
				}
				finalJsonArrayForRuleEngine.put(rule_json_object);

			} else {
				rule_json_object.put("MostRecent_" + pagePath, "No most recent url found");
				rule_json_object.put("MostRecent_SessionCount", "0");
				rule_json_object.put("MostRecent_AvgSesionDuration", "0");
				// rule_json_object.put("GA_User","");
				// rule_json_object.put("Host_Name","");
				// rule_json_object.put("MostRecent_"+pagePath,"0");
				// rule_json_object.put("Source",source);
				// rule_json_object.put("MostRecentTotalSessionCount","0");
				// rule_json_object.put("MostRecentTotalSesionDuration","0");
				// rule_json_object.put("MostRecentAvgSesionDuration","0");
				// rule_json_object.put("MostRecentMinSesionDuration","0");
				// rule_json_object.put("MostRecentMaxSesionDuration","0");
			}
			System.out.println(finalJsonArrayForRuleEngine);

		} catch (Exception ex) {
			System.out.println("Exception : " + ex.getMessage());
		} finally {
			Bson filter2 = lte("dateHourMinuteInDateFormat", most_recent_date);
			// collection.deleteMany(filter2);

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);

		}
		return rule_json_object;

	}

	public static JSONArray saveGADataForSubscribersView(String coll_name) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		JSONArray pagePathJsonArr = null;
		JSONArray funnelListJsonArr = new JSONArray();
		String sourceMedium = null;
		String pagePath = null;
		String subscriber_email = null;
		String hostname = null;
		String country = null;
		ArrayList<Double> timeOnPageArrList = null;
		ArrayList<Integer> sessionCountArrList = null;
		ArrayList<String> dateHourMinuteArrList = null;
		ArrayList<Integer> sessionDurationBucket = null;
		ArrayList<String> hostNameArrList = null;
		ArrayList<String> locationArrList = null;
		TreeSet<String> UniqueSubscribers = new TreeSet<String>();
		String campaign_id = null;
		String ga_user = null;
		String source = null;
		// Document url_view_doc_object = null;
		Document url_view_search_query_doc_object = null;
		Document url_view_set_doc = null;
		Document url_view_set_on_insert_doc = null;
		Document url_view_inc_doc = null;
		Document url_view_update_doc = null;

		Map<String, String> campaignDetailsMap = null;
		logger.info("111Going... to save data to google_analytics_url_view_collection from datatemp coll ");
		funnelListJsonArr.add("1");
		MongoClientURI connectionString = null;
		try {
			// mongoClient=ConnectionHelper.getConnection();
			funnelListJsonArr.add("2");
			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			//
			logger.info("subscriber_email dimension2  collection = ");
			//

			DistinctIterable<String> dimension2Di = collection.distinct("dimension2", String.class);
			logger.info("subscriber_email distinct= ");
			MongoCursor<String> dimension2Cursor = dimension2Di.iterator();

			// int count=0;
			try {
				while (dimension2Cursor.hasNext()) {
					subscriber_email = dimension2Cursor.next().toString();
					logger.info("subscriber_email distinct= " + subscriber_email);
					if (subscriber_email.contains("@") && !subscriber_email.equals("akhilesh@bizlem.com")) {
						// System.out.println("subscriber_email : "+ count++ + " : "+subscriber_email);

						UniqueSubscribers.add(subscriber_email);

					}
				}
			} finally {
				dimension2Cursor.close();
			}
			logger.info("Going... Total Number of Subscribers Found  : " + UniqueSubscribers.size()
					+ ":: UniqueSubscribers :: " + UniqueSubscribers);
			funnelListJsonArr.add("3UniqueSubscribers.size()" + UniqueSubscribers.size());
			if (UniqueSubscribers.size() > 0) {
				for (String temp_subscriber_email : UniqueSubscribers) {
					logger.info("temp_subscriber_email : " + temp_subscriber_email);
					Bson filter1 = and(eq("dimension2", temp_subscriber_email));
					DistinctIterable<String> sourceMediumDi = collection.distinct("sourceMedium", filter1,
							String.class);
					MongoCursor<String> sourceMediumCursor = sourceMediumDi.iterator();

					int countsource = 0;
					try {
						while (sourceMediumCursor.hasNext()) {
							countsource = countsource + 1;
							sourceMedium = sourceMediumCursor.next().toString();
							logger.info("sourceMedium : " + sourceMedium + ":: countsource :: " + countsource);
							if (sourceMedium.contains("email")) {
								campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1)
										.replace("email", "").trim();
								source = "Email";
								logger.info("source=\"Email\";" + source + " :: campaign_id= " + campaign_id);
							} else if (sourceMedium.contains("(direct)")) {
								source = "Direct";
								campaign_id = "NULL";
								logger.info("source=direct ;" + source + " :: campaign_id= " + campaign_id);
							}else if (sourceMedium.contains("(not set)")) {
								source = "not set";
								campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1)
										.replace("not set", "").trim();
								
								logger.info("source=direct ;" + source + " :: campaign_id= " + campaign_id);
							} else {
								campaign_id = "NULL";
								logger.info("else else  " + campaign_id);
							}
							campaign_id=campaign_id.trim();

							if (!campaign_id.equals("NULL")) {
								// campaignDetailsMap=GetCampaignDetailsBasedOnCampaignIdAndSubscriberIdFromMongo(campaign_id,temp_subscriber_email);
								logger.info("  campaign_id: " + campaign_id + " ::  temp_subscriber_email: "
										+ temp_subscriber_email);
								try {
									campaignDetailsMap = GetCampaignDetailsBasedOnSubscriberIdFromMongo(campaign_id,
											temp_subscriber_email);
									logger.info(" call GetCampaignDetailsBasedOnSubscriberIdFromMongo: "
											+ campaignDetailsMap);
								} catch (Exception e) {
									// TODO: handle exception
									logger.info("Exc in GetCampaignDetailsBasedOnSubscriberIdFromMongo :" + e);
								}
							} else {
								campaignDetailsMap = new HashMap<String, String>();

								campaignDetailsMap.put("CreatedBy", "NA");
								campaignDetailsMap.put("funnelName", "NA");
								campaignDetailsMap.put("Category", "NA");
								campaignDetailsMap.put("group", "NA");
								campaignDetailsMap.put("CampaignName", "NA");

								campaignDetailsMap.put("Campaign_Id", "NA");

								logger.info(" else after adding campaignDetailsMap : " + campaignDetailsMap);
							}
							Bson filter2 = null;
							logger.info("-----------Unique sourceMedium : " + sourceMedium
									+ "  : sourceMedium Unique-------------------------");
							Bson page_path_filter = and(eq("dimension2", temp_subscriber_email),
									eq("sourceMedium", sourceMedium));
							try {
								pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(collection, page_path_filter);
								logger.info(
										"pagePathJsonArr: " + pagePathJsonArr + " :: size :" + pagePathJsonArr.size());
							} catch (Exception e) {
								logger.info("Exc in GetUrlsBasedOnCampaignIdAndSubscriberId :" + e);
							}
							logger.info("pagePathJsonArr.size(): ");

							for (int i = 0; i < pagePathJsonArr.size(); i++) {
								// Creating New Document Object For URL VIEW
								try {
									url_view_search_query_doc_object = new Document();
									logger.info("subscriber_email: " + temp_subscriber_email + " :: campaign_id :"
											+ campaign_id + ":: sourceMedium :" + sourceMedium + ":: source" + source);
									url_view_search_query_doc_object.put("Subscriber_Email", temp_subscriber_email);
									url_view_search_query_doc_object.put("sourceMedium", sourceMedium);
									url_view_search_query_doc_object.put("Source", source);
									// url_view_search_query_doc_object.put("Campaign_id", campaign_id);

									url_view_set_doc = new Document();
									url_view_set_on_insert_doc = new Document();
									url_view_inc_doc = new Document();
									url_view_update_doc = new Document();

									url_view_set_doc.put("Created_By", campaignDetailsMap.get("CreatedBy").toString());

									url_view_set_doc.put("Funnel_Name",
											campaignDetailsMap.get("funnelName").toString());
									url_view_set_doc.put("group", campaignDetailsMap.get("group").toString());
									if (campaignDetailsMap.containsKey("Parentfunnel")) {
										url_view_set_doc.put("Parentfunnel",
												campaignDetailsMap.get("Parentfunnel").toString());
									}
									url_view_set_doc.put("Category", campaignDetailsMap.get("Category").toString());

									url_view_set_doc.put("Campaign_id",
											campaignDetailsMap.get("Campaign_id").toString());
									url_view_set_doc.put("CampaignName",
											campaignDetailsMap.get("CampaignName").toString());

									url_view_set_doc.put("Subscriber_Email", temp_subscriber_email);

									pagePath = (String) pagePathJsonArr.get(i);
									logger.info("pagePath : " + pagePath);
									timeOnPageArrList = new ArrayList<Double>();
									sessionCountArrList = new ArrayList<Integer>();
									dateHourMinuteArrList = new ArrayList<String>();
									sessionDurationBucket = new ArrayList<Integer>();
									hostNameArrList = new ArrayList<String>();
									locationArrList = new ArrayList<String>();
									filter2 = and(eq("sourceMedium", sourceMedium), eq("pagePath", pagePath),
											eq("dimension2", temp_subscriber_email));
									FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
									MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi
											.iterator();
									while (campaignWisePagePathCursor.hasNext()) {
										Document campaignWisePagePath = campaignWisePagePathCursor.next();
										System.out.println("campaignWisePagePath : " + campaignWisePagePath);
										ga_user = campaignWisePagePath.get("ga_username").toString();
										/*
										 * System.out.println("campaignWisePagePath : "+campaignWisePagePath.getString(
										 * "pagePath") +"    timeOnPage : "+campaignWisePagePath.getString("timeOnPage")
										 * +"    dateHourMinute : "+campaignWisePagePath.getString("dateHourMinute"));
										 */
										timeOnPageArrList.add(
												Double.parseDouble(campaignWisePagePath.get("timeOnPage").toString()));
										sessionCountArrList.add(
												Integer.parseInt(campaignWisePagePath.get("sessionCount").toString()));
										logger.info("sessionCount : "
												+ campaignWisePagePath.get("sessionCount").toString());
										dateHourMinuteArrList
												.add(campaignWisePagePath.get("dateHourMinute").toString());
										sessionDurationBucket.add(Integer.parseInt(
												campaignWisePagePath.get("sessionDurationBucket").toString()));
										hostNameArrList.add(campaignWisePagePath.get("hostname").toString());
										locationArrList.add(campaignWisePagePath.get("country").toString());
									}
									url_view_search_query_doc_object.put("ga_user", ga_user);
									if (dateHourMinuteArrList.size() > 0) {
										logger.info("dateHourMinuteArrList : "
												+ dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
										url_view_set_on_insert_doc.put("firstclick", dateHourMinuteArrList.get(0));
										url_view_set_doc.put("lastclick",
												dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));

									}
									if (hostNameArrList.size() > 0) {
										hostname = hostNameArrList.get(hostNameArrList.size() - 1);
										logger.info("hostname : " + hostname);
										// url_view_set_doc.put("hostname",hostname);
										url_view_search_query_doc_object.put("hostname", hostname);
									}
									if (locationArrList.size() > 0) {
										country = locationArrList.get(locationArrList.size() - 1);
										logger.info("country : " + country);
										// url_view_set_doc.put("hostname",hostname);
										url_view_search_query_doc_object.put("location", country);
									}
									if (pagePath.length() == 1) {
										pagePath = hostname;
										logger.info(pagePath);
									} else {
										if (pagePath.contains("/?")) {
											pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
											// System.out.println(path1);
										} else if (pagePath.contains("?")) {
											pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
										} else if (pagePath.contains("/")) {
											pagePath = hostname + pagePath;
										}
									}
									logger.info("pagePath = " + pagePath);
									url_view_search_query_doc_object.put("url", pagePath);

									Map<String, String> timeOnPageMap = ArrayListOperationsForDoubleValue(
											timeOnPageArrList);
									logger.info("timeOnPageMap : " + timeOnPageMap);

									url_view_set_doc.put("AvgTimeOnPage",
											Double.parseDouble(timeOnPageMap.get("Average")));
									url_view_set_doc.put("MinTimeOnPage", Double.parseDouble(timeOnPageMap.get("Min")));
									url_view_set_doc.put("MaxTimeOnPage", Double.parseDouble(timeOnPageMap.get("Max")));
									url_view_inc_doc.put("TotalTimeOnPage",
											Double.parseDouble(timeOnPageMap.get("Sum")));

									Map<String, String> sessionCountMap = ArrayListOperationsForIntegerValue(
											sessionCountArrList);
									logger.info("sessionCountMap : " + sessionCountMap);

									url_view_set_doc.put("LastSessionCount",
											Integer.parseInt(sessionCountMap.get("Max")));
									url_view_inc_doc.put("NoOfUrlClicks",
											Integer.parseInt(sessionCountMap.get("Count")));

									Bson filter = and(eq("sourceMedium", sourceMedium),
											eq("dimension2", temp_subscriber_email));
									Map<String, String> AvgSessionDurationBucketMap = GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId(
											collection, filter);

									logger.info("AvgSessionDurationBucketMap : " + AvgSessionDurationBucketMap);
									url_view_set_doc.put("AvgSesionDuration",
											Double.parseDouble(AvgSessionDurationBucketMap.get("Average")));
									Map<String, String> sessionDurationBucketMap = ArrayListOperationsForIntegerValueOfSessionDuration(
											sessionDurationBucket, sessionCountArrList);
									logger.info("sessionDurationBucketMap : " + sessionDurationBucketMap);

									// url_view_set_doc.put("AvgSesionDuration",Double.parseDouble(sessionDurationBucketMap.get("Average")));
									url_view_set_doc.put("MinSesionDuration",
											Integer.parseInt(sessionDurationBucketMap.get("Min")));
									url_view_set_doc.put("MaxSesionDuration",
											Integer.parseInt(sessionDurationBucketMap.get("Max")));
									url_view_inc_doc.put("TotalSesionDuration",
											Integer.parseInt(sessionDurationBucketMap.get("Sum")));
									url_view_inc_doc.put("SessionCount",
											Integer.parseInt(sessionDurationBucketMap.get("Count")));
									funnelListJsonArr.add("createFindFromURLViewDataAndUpdate.size()");

									logger.info("calling  createFindFromURLViewDataAndUpdate  : ");

									JSONObject inpjs = new JSONObject();
									inpjs.put("url_view_search_query_doc_object",
											url_view_search_query_doc_object.toString());
									inpjs.put("url_view_update_doc", url_view_update_doc.toString());
									inpjs.put("url_view_inc_doc", url_view_inc_doc.toString());
									inpjs.put("url_view_set_doc", url_view_set_doc.toString());
									inpjs.put("url_view_set_on_insert_doc", url_view_set_on_insert_doc.toString());
									inpjs.put("campaign_id", campaign_id);
									inpjs.put("temp_subscriber_email", temp_subscriber_email);
									logger.info(" inpjs createFindFromURLViewDataAndUpdate : " + inpjs.toString());
									funnelListJsonArr.add("inpjs:: " + inpjs);
									createFindFromURLViewDataAndUpdate(database, "google_analytics_url_view_collection",
											url_view_search_query_doc_object, url_view_update_doc, url_view_inc_doc,
											url_view_set_doc, url_view_set_on_insert_doc, campaign_id,
											temp_subscriber_email);
									logger.info("end pagePath : ");
								} catch (Exception e) {
									// TODO: handle exception
									funnelListJsonArr.add("eexc:: " + e);
									logger.info("exc in createFindFromURLViewDataAndUpdate  : " + e);
								}
								// break;

							}
							// break;
						}
					} finally {
						sourceMediumCursor.close();
					}
					// break;
				}
			}
		} catch (Exception e) {
			funnelListJsonArr.add("exc in subscrberview :: " + e);
			logger.info("exc in subscriber view  : " + e);
			System.out.println("Exception while calling Fetch from GA mohit.raj : " + e.getMessage().toString());
//            e.printStackTrace();

		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);
		}
		return funnelListJsonArr;
	}

	public static void createFindFromURLViewDataAndUpdate(MongoDatabase database, String coll_name,
			Document search_query, Document update_doc, Document url_view_inc_doc, Document url_view_set_doc,
			Document url_view_set_on_insert_doc, String campaign_id, String temp_subscriber_email) {
		// google_analytics_url_view_collection
		MongoCollection<Document> url_view_collection = null;
		Document url_view_update_doc = null;

		// String lastclick = url_view_set_doc.getString("lastclick");

		double TotalTimeOnPage = 0;
		int NoOfUrlClicks = 0;
		double AvgTimeOnPage = 0;
		double MinTimeOnPage = 0;
		double MaxTimeOnPage = 0;

		int TotalSesionDuration = 0;
		int SessionCount = 0;
		// int LastSessionCount = url_view_set_doc.getInteger("LastSessionCount");
		double AvgSesionDuration = 0;
		int MaxSesionDuration = 0;
		int MinSesionDuration = 0;

		int Previous_TotalSesionDuration = 0;
		int Previous_SessionCount = 0;
		double Previous_AvgSesionDuration = 0;
		int Previous_MinSesionDuration = 0;
		int Previous_MaxSesionDuration = 0;

		double Previous_TotalTimeOnPage = 0;
		int Previous_NoOfUrlClicks = 0;
		double Previous_AvgTimeOnPage = 0;
		double Previous_MinTimeOnPage = 0;
		double Previous_MaxTimeOnPage = 0;
		DecimalFormat decimal_formatter = new DecimalFormat("0.00");

		logger.info("calling createFindFromURLViewDataAndUpdate : " + coll_name);
		try {
			url_view_collection = database.getCollection(coll_name);
			Bson filter = search_query;
			// Bson update = new Document("$set",doc);
			MongoCursor<Document> campaignCursor = url_view_collection.find(filter).iterator();
			if (campaignCursor.hasNext() == true) {
				while (campaignCursor.hasNext()) {
					Document doc = campaignCursor.next();
					// System.out.println("------------------doc.toJson()-------------");
					logger.info("url_view_set_doc : " + url_view_set_doc.toJson());
					// System.out.println("Step -------- 0");
					// System.out.println("url_view_inc_doc : "+url_view_inc_doc.toJson());
					TotalTimeOnPage = url_view_inc_doc.getDouble("TotalTimeOnPage");
					NoOfUrlClicks = url_view_inc_doc.getInteger("NoOfUrlClicks");
					// System.out.println("Step -------- 01");
					// AvgTimeOnPage = url_view_set_doc.getDouble("AvgTimeOnPage");
					MinTimeOnPage = url_view_set_doc.getDouble("MinTimeOnPage");
					MaxTimeOnPage = url_view_set_doc.getDouble("MaxTimeOnPage");
					// System.out.println("Step -------- 02");

					SessionCount = url_view_inc_doc.getInteger("SessionCount");
					TotalSesionDuration = url_view_inc_doc.getInteger("TotalSesionDuration");
					// AvgSesionDuration = url_view_set_doc.getDouble("AvgSesionDuration");
					MaxSesionDuration = url_view_set_doc.getInteger("MaxSesionDuration");
					MinSesionDuration = url_view_set_doc.getInteger("MinSesionDuration");

					// System.out.println("Step -------- 1");

					Previous_TotalSesionDuration = doc.getInteger("TotalSesionDuration");
					Previous_SessionCount = doc.getInteger("SessionCount");
					Previous_MinSesionDuration = doc.getInteger("MinSesionDuration");
					Previous_MaxSesionDuration = doc.getInteger("MaxSesionDuration");
					Previous_TotalTimeOnPage = doc.getDouble("TotalTimeOnPage");
					Previous_NoOfUrlClicks = doc.getInteger("NoOfUrlClicks");
					Previous_MinTimeOnPage = doc.getDouble("MinTimeOnPage");
					Previous_MaxTimeOnPage = doc.getDouble("MaxTimeOnPage");
					// System.out.println("Step -------- 2");
					AvgTimeOnPage = (Previous_TotalTimeOnPage + TotalTimeOnPage)
							/ (Previous_NoOfUrlClicks + NoOfUrlClicks);
					url_view_set_doc.put("AvgTimeOnPage", Double.parseDouble(decimal_formatter.format(AvgTimeOnPage)));
					AvgSesionDuration = (Previous_TotalSesionDuration + TotalSesionDuration)
							/ (Previous_SessionCount + SessionCount);
					url_view_set_doc.put("AvgSesionDuration",
							Double.parseDouble(decimal_formatter.format(AvgSesionDuration)));
					if (Previous_MinSesionDuration >= MinSesionDuration) {
						url_view_set_doc.put("MinSesionDuration", MinSesionDuration);
						// url_view_set_doc.put("MinSesionDuration",1);
					}
					if (Previous_MaxSesionDuration <= MaxSesionDuration) {
						url_view_set_doc.put("MaxSesionDuration", MaxSesionDuration);
						// url_view_set_doc.put("MaxSesionDuration",155526455);
					}
					if (Previous_MinTimeOnPage >= MinTimeOnPage) {
						url_view_set_doc.put("MinTimeOnPage", MinTimeOnPage);
					}
					if (Previous_MaxTimeOnPage <= MaxTimeOnPage) {
						url_view_set_doc.put("MaxTimeOnPage", MaxTimeOnPage);
					}
					// System.out.println("Step -------- 3");
					logger.info("url_view_set_doc : " + url_view_set_doc.toJson());
					System.out.println("------------------doc.toJson()-------------");
					url_view_update_doc = new Document();
					url_view_update_doc.put("$inc", url_view_inc_doc); // TotalTimeOnPage NoOfUrlClicks
																		// TotalSesionDuration SessionCount
					url_view_update_doc.put("$set", url_view_set_doc);// lastclick hostname AvgTimeOnPage MinTimeOnPage
																		// MaxTimeOnPage TotalTimeOnPage
					// LastSessionCount AvgSesionDuration MaxSesionDuration MinSesionDuration
					// TotalSesionDuration
					url_view_update_doc.put("$setOnInsert", url_view_set_on_insert_doc); // firstclick
					try {
						logger.info(" if :url_view_update_doc:" + url_view_update_doc.toString());
						createURLViewData(database, "google_analytics_url_view_collection", search_query,
								url_view_update_doc);
					} catch (Exception e) {

						logger.info("exc in createURLViewData:" + e);
					}
					// fetchGADataForRuleEngine(search_query,url_view_set_doc,url_view_inc_doc,url_view_set_on_insert_doc);//Document
				}
			} else {
				url_view_update_doc = new Document();
				url_view_update_doc.put("$inc", url_view_inc_doc); // TotalTimeOnPage NoOfUrlClicks TotalSesionDuration
																	// SessionCount
				url_view_update_doc.put("$set", url_view_set_doc);// lastclick hostname AvgTimeOnPage MinTimeOnPage
																	// MaxTimeOnPage TotalTimeOnPage
				// LastSessionCount AvgSesionDuration MaxSesionDuration MinSesionDuration
				// TotalSesionDuration
				url_view_update_doc.put("$setOnInsert", url_view_set_on_insert_doc); // firstclick
				try {
					logger.info(" else :url_view_update_doc:" + url_view_update_doc.toString());

					createURLViewData(database, "google_analytics_url_view_collection", search_query,
							url_view_update_doc);
				} catch (Exception e) {

					logger.info("exc in createURLViewData:" + e);
				}
				// fetchGADataForRuleEngine(search_query,url_view_set_doc,url_view_inc_doc,url_view_set_on_insert_doc);//Document
			}

		} catch (Exception e) {
			logger.info("Exception in createFindFromURLViewDataAndUpdate : " + e);
		} finally {
			//
		}
	}

	public static void createURLViewData(MongoDatabase database, String coll_name, Document search_query,
			Document update_doc) {
		// google_analytics_url_view_collection
		MongoCollection<Document> url_view_collection = null;
		try {
			logger.info("calling createURLViewData: ");
			url_view_collection = database.getCollection(coll_name);
			Bson filter = search_query;
			// Bson update = new Document("$set",doc);
			UpdateOptions options = new UpdateOptions().upsert(true);
			Document modifiedObject = new Document();
			modifiedObject.put("$inc", new BasicDBObject().append("No_OF_Clicks", 5));
			url_view_collection.updateOne(filter, update_doc, options);
		} catch (Exception e) {
			logger.info("Exception createURLViewData: " + e.getMessage());
		} finally {
			//
		}
	}

	public static void callRuleEngine(String jsonObject, String funnel_name, String ruleeng, String createdby,
			String rulejssave) {
		String rule_engine_response;
		try {

			logger.info("Going... to call rule engine");
			String rule_engine_url = ResourceBundle.getBundle("config").getString("rule_engine_url")
					+ createdby.replace("_", "@") + "_" + funnel_name + "_" + ruleeng + "/fire";
			logger.info("Rule Engine URL : " + rule_engine_url);
			// rule_engine_url=http://carrotrule.bluealgo.com:8082/drools/callrules/
			//http://carrotrule.bluealgo.com:8082/drools/callrules/
			//http://34.74.125.253:8082/drools/callrules/viki@gmail.com_funnel4455_ruleengtest66/fire
			// urlconnect(ResourceBundle.getBundle("config").getString("rule_engine_url"),jsonObject);
			rule_engine_response = urlconnect(rule_engine_url, jsonObject);

			org.json.JSONObject ruleEngineResponseJsonObject = new org.json.JSONObject(rule_engine_response);
			logger.info("ruleEngineResponseJsonObject : " + ruleEngineResponseJsonObject);
			if (ruleEngineResponseJsonObject.has("Output")) {

				String move_categorynew = ruleEngineResponseJsonObject.getString("Output").trim();
				String move_category = getOldcategoryName(move_categorynew);
				String original_categorynew = ruleEngineResponseJsonObject.getString("Category").trim();
				String original_category = getOldcategoryName(original_categorynew);
				int original_category_position = getCategoryPosition(original_category);
				int move_category_position = getCategoryPosition(move_category);
				logger.info("move_category_position :: " + move_category_position);
				logger.info("original_category_position: " + original_category_position);
				if (move_category_position > original_category_position) {
					// System.out.println("Move");
					logger.info("Rule Engine Response (Move TO): " + move_category);
					updateDataSourceforMoveSubscriber(ruleEngineResponseJsonObject.get("SubscriberEmail").toString(),
							ruleEngineResponseJsonObject.get("CreatedBy").toString(),
							ruleEngineResponseJsonObject.get("ChildFunnelName").toString(), move_category,
							ruleEngineResponseJsonObject.get("CampaignId").toString(),
							ruleEngineResponseJsonObject.get("group").toString(), original_category);

					updateMoveCategory(ruleEngineResponseJsonObject.get("SubscriberEmail").toString(), move_category,
							ruleEngineResponseJsonObject.get("ChildFunnelName").toString(),
							ruleEngineResponseJsonObject.get("CampaignId").toString(),
							ruleEngineResponseJsonObject.get("CreatedBy").toString(), original_category);

					logger.info("inserted category");

				} else {
					logger.info("No Need to Move");
				}
			} else {
				// System.out.println("Did not get any OutPut From Rule Engine");
				logger.info("Did not get any OutPut From Rule Engine");
			}
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			// System.out.println("Error in Method callRuleEngine() : "+ex.getMessage());
			logger.info("Error in Method callRuleEngine() : " + ex.getMessage());
		}
	}

	public static void callRuleEngineOLD(String jsonObject, String funnel_name, org.json.JSONObject rulejssave) {
		String rule_engine_response;
		try {

			logger.info("Going... to call rule engine");
			String rule_engine_url = ResourceBundle.getBundle("config").getString("rule_engine_url") + funnel_name + "_"
					+ funnel_name + "_RE/fire";

			logger.info("Rule Engine URL : " + rule_engine_url);
			// rule_engine_response =
			// urlconnect(ResourceBundle.getBundle("config").getString("rule_engine_url"),jsonObject);
			rule_engine_response = "{\"Category\":\"Explore\",\"SubscriberEmail\":\"mohit.raj@bizlem.com\",\"SourceMedium\":\"GAURLTEST / email\",\"MostRecent_bizlem.com/blog.html\":\"9.00\",\"Source\":\"Email\",\"SubFunnelName\":\"Explore\",\"CampaignId\":\"GAURLTEST\",\"bizlem.com/products.html\":\"82\",\"Output\":\"Entice\",\"HostName\":\"bizlem.com\",\"GAUser\":\"bizlembizlem1234@gmail.com\",\"CreatedBy\":\"salesautoconvertuser1@gmail.com\",\"FunnelName\":\"GAURLMergeTail\"}";// urlconnect(rule_engine_url,
																																																																																																																										// jsonObject);
			// System.out.println("Rule Engine Response : "+rule_engine_response);
			// out.println("Rule Engine Response : "+rule_engine_response);
			// LogByFileWriter.logger_info("RuleEngineCall : " + "Rule Engine Response :
			// "+rule_engine_response);
			org.json.JSONObject ruleEngineResponseJsonObject = new org.json.JSONObject(rule_engine_response);
			logger.info("ruleEngineResponseJsonObject : " + ruleEngineResponseJsonObject);
			if (ruleEngineResponseJsonObject.has("Output")) {

				String move_category = ruleEngineResponseJsonObject.getString("Output").trim();
				String original_category = ruleEngineResponseJsonObject.getString("Category").trim();
				int original_category_position = getCategoryPosition(original_category);
				int move_category_position = getCategoryPosition(move_category);
				logger.info("move_category_position :: " + move_category_position);
				logger.info("original_category_position: " + original_category_position);
				if (move_category_position > original_category_position) {
					// String resp= new GAMongoDAO().moveSubscriber( move_category,
					// ruleEngineResponseJsonObject.get("FunnelName").toString(),
					// ruleEngineResponseJsonObject.get("CampaignId").toString(), original_category,
					// ruleEngineResponseJsonObject.get("SubscriberEmail").toString(),ruleEngineResponseJsonObject.get("CreatedBy").toString())
					// ;
//					updateDataSourceforMoveSubscriber(ruleEngineResponseJsonObject.get("SubscriberEmail").toString(), ruleEngineResponseJsonObject.get("CreatedBy").toString(), ruleEngineResponseJsonObject.get("FunnelName").toString(),
//							move_category,  ruleEngineResponseJsonObject.get("CampaignId").toString(),"G1");

//					updateDataSourceforMoveSubscriber("tejal.bizlem@gmail.com", "salesautoconvertuser1@gmail.com",
//							"GAURLMergeTail", move_category, "SalesconvertURLTest", "G1");
//					// tejal.bizlem@gmail.com
//					updateDataSourceforMoveSubscriber("mohit.raj@bizlem.io", "salesautoconvertuser1@gmail.com",
//							"GAURLMergeTail", move_category, "SalesconvertURLTest", "G1");

//					updateDataSourceforMoveSubscriber("viki@bizlem.com", ruleEngineResponseJsonObject.get("CreatedBy").toString(), "funnelnew",
//							move_category,  "campid11");

					logger.info("Rule Engine Response (Move TO): " + move_category);
//					JSONObject ruljs=new JSONObject(jsonObject);

					MongoClientURI connectionString = null;
					MongoClient mongoClient = null;
					MongoDatabase database = null;
					MongoCollection<Document> collection = null;
					try {
//						String uri = "mongodb://localhost:27017/?ssl=true";
//						System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
//						System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
//						System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
//						System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
//						//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();
//
//						connectionString = new MongoClientURI(uri);
//						mongoClient = new MongoClient(connectionString);
//						database = mongoClient.getDatabase("salesautoconvert");
//
//						collection = database.getCollection("RuleEngineCalledForSubscriberData");
////						 Document document =null;
//						logger.info("RuleEngineCalledForSubscriberData  insertruleDataforMonitoring : ");
//						Bson document = new Document("move_category", move_category);
//						// document.put("move_category", move_category);
//
//						logger.info("document= " + document);
//
//						Bson searchQuery = new Document()
//								.append("SubscriberEmail", ruleEngineResponseJsonObject.get("SubscriberEmail"))
//								.append("SubFunnelName", original_category)
//								.append("FunnelName", ruleEngineResponseJsonObject.get("FunnelName"))
//								.append("CampaignId", ruleEngineResponseJsonObject.get("CampaignId"));
////
//						collection.updateOne(searchQuery, new Document("$set", document),
//								new UpdateOptions().upsert(true));

						rulejssave.put("move_category", move_category);

						GAMongoDAO.insertruleDataforMonitoring(rulejssave.toString(),
								ruleEngineResponseJsonObject.get("SubscriberEmail").toString(), original_category,
								ruleEngineResponseJsonObject.get("FunnelName").toString(),
								ruleEngineResponseJsonObject.get("CampaignId").toString(),
								ruleEngineResponseJsonObject.get("CreatedBy").toString());
						logger.info("inserted category");

					} catch (Exception e) {
						logger.info("exc insert mongo: " + e);
					}
					logger.info("original_category_position: " + original_category_position);

					logger.info("call " + ruleEngineResponseJsonObject);

				} else {
					logger.info("No Need to Move");
				}
			} else {
				// System.out.println("Did not get any OutPut From Rule Engine");
				logger.info("Did not get any OutPut From Rule Engine");
			}
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			// System.out.println("Error in Method callRuleEngine() : "+ex.getMessage());
			logger.info("Error in Method callRuleEngine() : " + ex.getMessage());
		}
	}

	public static int getCategoryPosition(String category) {
		int position = 0;
		HashMap<String, Integer> category_map = new HashMap<String, Integer>();
		category_map.put("Explore", 1);
		category_map.put("Entice", 2);
		category_map.put("Inform", 3);
		category_map.put("Warm", 4);
		category_map.put("Connect", 5);

		if (category_map.containsKey(category)) {
			position = category_map.get(category);
			// System.out.println("value for key "+category+" is : " + position);
			logger.info("value for key " + category + " is : " + position);
		}
		return position;

	}

	public static String sendPostRequestToManageSubscribers(String callurl, String urlParameters)
			throws ServletException, IOException {
		// out.println("urlParameters :" + urlParameters);
		// URL url = new URL(callurl + urlParameters.replace("\\", ""));
		logger.info("Going... to move Subscriber");
		URL url = new URL(callurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		//
		OutputStream writer = conn.getOutputStream();

		writer.write(urlParameters.getBytes());
		// out.println("Writer Url : "+writer);
		int responseCode = conn.getResponseCode();
		// System.out.println("POST Response Code :: " + responseCode);
		logger.info("POST Response Code :: " + responseCode);
		StringBuffer buffer = new StringBuffer();
		//
		if (responseCode == 200) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			//
			logger.info(buffer.toString());
			// out.println("Respnse Body : "+buffer.toString());
		} else {
			// System.out.println("POST request not worked");
			logger.info("Move Subscriber POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}

	public static Map<String, String> ArrayListOperationsForDoubleValue(ArrayList<Double> timeOnPageArrList) {
		Map<String, String> timeOnPageMap = new HashMap<String, String>();
		DecimalFormat decimal_formatter = new DecimalFormat("0.00");
		System.out.println(
				"timeOnPageArrList.size() = " + timeOnPageArrList.size() + "timeOnPageArrList" + timeOnPageArrList);
		if (timeOnPageArrList.size() > 0) {
			double sum = 0;
			for (Double i : timeOnPageArrList) {
				sum += i;
			}
			double average = sum / timeOnPageArrList.size();
			timeOnPageMap.put("Min", String.valueOf(Collections.min(timeOnPageArrList)));
			timeOnPageMap.put("Max", String.valueOf(Collections.max(timeOnPageArrList)));
			timeOnPageMap.put("Sum", String.valueOf(decimal_formatter.format(sum)));
			timeOnPageMap.put("Average", String.valueOf(decimal_formatter.format(average)));
			timeOnPageMap.put("Count", String.valueOf(timeOnPageArrList.size()));
		} else {
			timeOnPageMap.put("Min", "0");
			timeOnPageMap.put("Max", "0");
			timeOnPageMap.put("Sum", "0");
			timeOnPageMap.put("Average", "0");
			timeOnPageMap.put("Count", "0");
		}
		System.out.println("timeOnPageArrList.size() = " + timeOnPageMap);
		return timeOnPageMap;
	}

	public static Map<String, String> ArrayListOperationsForIntegerValue(ArrayList<Integer> sessionCountArrList) {
		Map<String, String> sessionCountMap = new HashMap<String, String>();
		// System.out.println("sessionCountArrList.size() = " +
		// sessionCountArrList.size());
		if (sessionCountArrList.size() > 0) {
			int sum = 0;
			for (Integer i : sessionCountArrList) {
				sum += i;
			}
			double average = sum / sessionCountArrList.size();
			sessionCountMap.put("Min", String.valueOf(Collections.min(sessionCountArrList)));
			sessionCountMap.put("Max", String.valueOf(Collections.max(sessionCountArrList)));
			sessionCountMap.put("Sum", String.valueOf(sum));
			sessionCountMap.put("Average", String.valueOf(average));
			sessionCountMap.put("Count", String.valueOf(sessionCountArrList.size()));
		} else {
			sessionCountMap.put("Min", "0");
			sessionCountMap.put("Max", "0");
			sessionCountMap.put("Sum", "0");
			sessionCountMap.put("Average", "0");
			sessionCountMap.put("Count", "0");
		}
		return sessionCountMap;
	}

	public static Map<String, String> ArrayListOperationsForIntegerValueOfSessionDuration(
			ArrayList<Integer> sessionDurationBucket, ArrayList<Integer> sessionCountArrList) {
		Map<String, String> sessionDurationBuckettMap = new HashMap<String, String>();
		HashMap<Integer, Integer> uniqueCountWithSessionDuration = new HashMap<Integer, Integer>();
		int unique_seesion_duration_sum = 0;
		int map_value = 0;
		int unique_seesion_count = 0;
		/* For Loop for iterating ArrayList */
		for (int counter = 0; counter < sessionCountArrList.size(); counter++) {
			uniqueCountWithSessionDuration.put(sessionCountArrList.get(counter), sessionDurationBucket.get(counter));
		}
		for (Map.Entry m : uniqueCountWithSessionDuration.entrySet()) {
			System.out.println(m.getKey() + " " + m.getValue());
			map_value = (Integer) m.getValue();
			unique_seesion_duration_sum += map_value;
		}
		double unique_average = unique_seesion_duration_sum / uniqueCountWithSessionDuration.size();
		System.out.println("unique_average : " + unique_average);
		System.out.println("ArrayListOperationsForIntegerValueOfSessionDuration() uniqueCountWithSessionDuration size= "
				+ uniqueCountWithSessionDuration.size());
		System.out.println("ArrayListOperationsForIntegerValueOfSessionDuration() uniqueCountWithSessionDuration = "
				+ uniqueCountWithSessionDuration);
		System.out.println("ArrayListOperationsForIntegerValueOfSessionDuration() sessionDurationBucket size= "
				+ sessionDurationBucket.size());
		System.out.println("ArrayListOperationsForIntegerValueOfSessionDuration() sessionDurationBucket = "
				+ sessionDurationBucket);
		System.out.println("ArrayListOperationsForIntegerValueOfSessionDuration() sessionCountArrList Size= "
				+ sessionCountArrList.size());
		System.out.println(
				"ArrayListOperationsForIntegerValueOfSessionDuration() sessionCountArrList= " + sessionCountArrList);
		if (sessionDurationBucket.size() > 0) {
			int sum = 0;
			for (Integer i : sessionDurationBucket) {
				sum += i;
			}
			// double average = sum / sessionCountArrList.size();
			double average = sum / Collections.max(sessionCountArrList);
			sessionDurationBuckettMap.put("Min", String.valueOf(Collections.min(sessionDurationBucket)));
			sessionDurationBuckettMap.put("Max", String.valueOf(Collections.max(sessionDurationBucket)));
			// sessionDurationBuckettMap.put("Sum",String.valueOf(sum));
			sessionDurationBuckettMap.put("Sum", String.valueOf(unique_seesion_duration_sum));
			// sessionDurationBuckettMap.put("Average",String.valueOf(average));
			sessionDurationBuckettMap.put("Average", String.valueOf(unique_average));
			sessionDurationBuckettMap.put("LastCount", String.valueOf(sessionDurationBucket.size()));
			sessionDurationBuckettMap.put("Count", String.valueOf(uniqueCountWithSessionDuration.size()));
		} else {
			sessionDurationBuckettMap.put("Min", "0");
			sessionDurationBuckettMap.put("Max", "0");
			sessionDurationBuckettMap.put("Sum", "0");
			sessionDurationBuckettMap.put("Average", "0");
			sessionDurationBuckettMap.put("Count", "0");
			sessionDurationBuckettMap.put("LastCount", "0");
		}
		return sessionDurationBuckettMap;
	}

	public static Map<String, String> GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId(
			MongoCollection<Document> collection, Bson filter) {
		String sessionCount = null;
		ArrayList<Integer> sessionDurationBucketArrList = null;
		Map<String, String> sessionDurationBucketMap = null;
		System.out.println("GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId Unique sessionCount : ");
		try {
			DistinctIterable<String> sessionCountDi = collection.distinct("sessionCount", filter, String.class);
			MongoCursor<String> sessionCountDiCursor = sessionCountDi.iterator();
			try {
				sessionDurationBucketArrList = new ArrayList<Integer>();
				while (sessionCountDiCursor.hasNext()) {
					// pagePath
					sessionCount = sessionCountDiCursor.next().toString();
					System.out.println("GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId Unique sessionCount : "
							+ sessionCount);
					Bson filter3 = eq("sessionCount", sessionCount);
					FindIterable<Document> sessionCountFi = collection.find(filter3);
					MongoCursor<Document> sessionCountCursor = sessionCountFi.iterator();
					try {
						while (sessionCountCursor.hasNext()) {
							Document doc = sessionCountCursor.next();
							sessionDurationBucketArrList.add(Integer.parseInt(doc.getString("sessionDurationBucket")));
							break;
						}
					} finally {
						sessionCountCursor.close();
					}
				}
				// System.out.println("sessionDurationBucketArrList :
				// "+sessionDurationBucketArrList);
				sessionDurationBucketMap = ArrayListOperationsForIntegerValue(sessionDurationBucketArrList);
			} finally {
				sessionCountDiCursor.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {

		}
		return sessionDurationBucketMap;
	}

	public static JSONArray GetUrlsBasedOnCampaignIdAndSubscriberId(MongoCollection<Document> collection, Bson filter) {
		JSONArray pagePathJsonArr = new JSONArray();
		String pagePath = null;
	//	DistinctIterable<Document> pagePathDi = collection.distinct("pagePath", filter, );
		MongoCursor<Document> pagePathCursor = collection.find(filter).iterator();
		HashSet<String> setOfUrl = new HashSet<String>();
		try {
			while (pagePathCursor.hasNext()) {
				Document doc = pagePathCursor.next();
				pagePath = doc.getString("pagePath");
				//pagePath = pagePathCursor.next().toString();
				setOfUrl.add(pagePath);
				System.out.println("pagePath : " + pagePath);
				
			}
			for(String url : setOfUrl){
				pagePathJsonArr.add(url);
				}
			
		} finally {
			if(null != pagePathCursor ) {
				pagePathCursor.close();
				pagePathCursor = null;
			}
			
			setOfUrl.clear();
			setOfUrl = null;
		}
		// System.out.println("pagePathJsonArr : "+pagePathJsonArr);

		return pagePathJsonArr;
	}

	public static String GetHostNameBasedOnCampaignIdAndSubscriberId(MongoCollection<Document> collection,
			Bson filter) {
		JSONArray pagePathJsonArr = new JSONArray();
		String pagePath = null;
		String hostName = null;
		MongoCursor<Document> campaignCursor = collection.find(filter).iterator();

		try {
			if (campaignCursor.hasNext() == true) {
				while (campaignCursor.hasNext()) {
					Document doc = campaignCursor.next();
					hostName = doc.getString("hostname");
				}
			}
		} finally {
			campaignCursor.close();
		}
		// System.out.println("pagePathJsonArr : "+pagePathJsonArr);

		return hostName;
	}

	public static Map<String, String> GetCampaignDetailsBasedOnCampaignIdAndSubscriberIdFromMongo(String campaign_id,
			String subscriber_email) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> subscribers_details_collection = null;
		Map<String, String> campaignDetailsMap = new HashMap<String, String>();
		String SubscriberId = null;
//	    String CreatedBy=null;
//	    String funnelNodeName=null;
//	    String subFunnelNodeName=null;
//	    String Sling_Subject=null;
//	    String Sling_Campaign_Id=null;
//	    String List_Id=null;
//	    String subscriber_userid=null;
		MongoClientURI connectionString = null;
		try {

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("campaign_details");
			subscribers_details_collection = database.getCollection("subscribers_details");
			Bson filter = and(eq("Campaign_Id", campaign_id), eq("subscribers.email", subscriber_email));
			MongoCursor<Document> campaignCursor = collection.find(filter).iterator();

			try {
				if (campaignCursor.hasNext() == true) {
					while (campaignCursor.hasNext()) {
						Document doc = campaignCursor.next();
						campaignDetailsMap.put("CreatedBy", doc.getString("CreatedBy"));
						campaignDetailsMap.put("funnelName", doc.getString("funnelName"));
						campaignDetailsMap.put("SubFunnelName", doc.getString("SubFunnelName"));
						campaignDetailsMap.put("CampaignNodeNameInSling", doc.getString("CampaignNodeNameInSling"));
						campaignDetailsMap.put("CampaignName", doc.getString("CampaignName"));
						campaignDetailsMap.put("Subject", doc.getString("Subject"));

						campaignDetailsMap.put("Type", doc.getString("Type"));
						campaignDetailsMap.put("Campaign_Id", doc.getString("Campaign_Id"));
						campaignDetailsMap.put("List_Id", doc.getString("List_Id"));

						campaignDetailsMap.put("campaign_status", doc.getString("campaign_status"));
						campaignDetailsMap.put("Campaign_Date", doc.getString("Campaign_Date"));
						SubscriberId = getSubscriberIdBasedOnListIdAndEmail(subscribers_details_collection,
								doc.getString("List_Id"), subscriber_email);
						if (SubscriberId != null) {
							campaignDetailsMap.put("subscriber_userid", SubscriberId);
						} else {
							campaignDetailsMap.put("subscriber_userid", "null");
						}
						break;
					}
				} else {
					campaignDetailsMap.put("CreatedBy", "NA");
					campaignDetailsMap.put("funnelName", "NA");
					campaignDetailsMap.put("SubFunnelName", "NA");
					campaignDetailsMap.put("CampaignNodeNameInSling", "NA");
					campaignDetailsMap.put("CampaignName", "NA");
					campaignDetailsMap.put("Subject", "NA");

					campaignDetailsMap.put("Type", "NA");
					campaignDetailsMap.put("Campaign_Id", "NA");
					campaignDetailsMap.put("List_Id", "NA");

					campaignDetailsMap.put("campaign_status", "NA");
					campaignDetailsMap.put("Campaign_Date", "NA");
					campaignDetailsMap.put("subscriber_userid", "NA");
				}

			} finally {
				campaignCursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);
		}

		System.out.println("campaignDetailsMap : " + campaignDetailsMap);
		return campaignDetailsMap;
	}

	public static Map<String, String> GetCampaignDetailsBasedOnSubscriberIdFromMongo(String campaign_id,
			String subscriber_email) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;

		Map<String, String> campaignDetailsMap = new HashMap<String, String>();
		MongoClientURI connectionString = null;
		try {
			// mongoClient=ConnectionHelper.getConnection();

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");

			Bson filter = eq("Campaign_id", campaign_id);
			MongoCursor<Document> campaignCursor = collection.find(filter).iterator();
			String funnelName = null;
			String mainfunnel = null;
			try {
				if (campaignCursor.hasNext() == true) {
					while (campaignCursor.hasNext()) {

						Document doc = campaignCursor.next();
						campaignDetailsMap.put("CreatedBy", doc.getString("CreatedBy"));
						funnelName = doc.getString("funnelName");
						campaignDetailsMap.put("funnelName", funnelName);

						if (funnelName.contains("_EC_") || funnelName.contains("_EnC_") || funnelName.contains("_IC_")
								|| funnelName.contains("_WC_") || funnelName.contains("_CC_")) {
							mainfunnel = funnelName.substring(0, funnelName.lastIndexOf("_"));
							mainfunnel = funnelName.substring(0, mainfunnel.lastIndexOf("_"));

						} else {
							mainfunnel = funnelName;
						}
						campaignDetailsMap.put("Parentfunnel", mainfunnel);
						campaignDetailsMap.put("Category", doc.getString("Category"));

						campaignDetailsMap.put("CampaignName", doc.getString("campaignName"));

						campaignDetailsMap.put("Campaign_id", doc.getString("Campaign_id"));
						campaignDetailsMap.put("group", doc.getString("group"));

						break;
					}
				} else {

					campaignDetailsMap.put("CreatedBy", "NA");
					campaignDetailsMap.put("funnelName", "NA");
					campaignDetailsMap.put("Category", "NA");

					campaignDetailsMap.put("CampaignName", "NA");

					campaignDetailsMap.put("Campaign_id", "NA");

					campaignDetailsMap.put("group", "NA");

				}

			} finally {
				campaignCursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		System.out.println("campaignDetailsMap : " + campaignDetailsMap);
		return campaignDetailsMap;
	}

	public static String getSubscriberIdBasedOnListIdAndEmail(MongoCollection<Document> collection, String ListId,
			String SubscriberEmail) throws ServletException, IOException {
		String SubscriberId = null;
		Bson filter = and(eq("ListId", ListId), eq("SubscriberEmail", SubscriberEmail));
		MongoCursor<Document> campaignCursor = collection.find(filter).iterator();

		try {
			while (campaignCursor.hasNext()) {
				Document doc = campaignCursor.next();
				SubscriberId = doc.getString("SubscriberId");
			}
		} finally {
			campaignCursor.close();
		}
		return SubscriberId;
	}

	public static String getSubscriberIdBasedEmail(MongoCollection<Document> collection, String SubscriberEmail)
			throws ServletException, IOException {
		String SubscriberId = null;
		Bson filter = eq("SubscriberEmail", SubscriberEmail);
		MongoCursor<Document> campaignCursor = collection.find(filter).iterator();

		try {
			while (campaignCursor.hasNext()) {
				Document doc = campaignCursor.next();
				SubscriberId = doc.getString("SubscriberId");
			}
		} finally {
			campaignCursor.close();
		}
		return SubscriberId;
	}

	public static String getRequestForCampaignDetailsFromSling(String url, String urlParameters)
			throws ServletException, IOException {
		URL http_url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) http_url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("GET");
		int responseCode = conn.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);
		StringBuffer buffer = new StringBuffer();
		if (responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			System.out.println("Buffer : " + buffer.toString());
		} else {
			System.out.println("POST request not worked");
		}
		return buffer.toString();
	}

	public static String urlconnect(String urlstr, String json) throws IOException, JSONException {
		JSONObject jsonObject = null;
		StringBuffer response = null;

		try {
			int responseCode = 0;
			String urlParameters = "";
			URL url = new URL(urlstr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			con.setRequestProperty("Content-Type", "application/json");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			// wr.writeBytes(json.toString());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
			writer.write(json.toString());
			writer.close();
			wr.flush();
			wr.close();
			responseCode = con.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();

	}

	public static org.json.JSONObject mergeJSONObject(org.json.JSONObject json_object_1,
			org.json.JSONObject json_object_2) {
		for (String key : org.json.JSONObject.getNames(json_object_2)) {
			json_object_1.put(key, json_object_2.get(key));
		}
		return json_object_1;
	}

	/*
	 * 
	 * 
	 * 
	 * ||||||||||||||||||| ||||||||||||||||||| ||| ||| ||| ||| ||| ||| ||| ||| |||
	 * ||| ||| ||| ||| ||| ||| unk Code ||||||||
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * ------------------------------------------------Code Below Are Junk
	 * Code------------------------------------------------------
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public static JSONArray saveGADataForRecentView(String coll_name) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		JSONArray pagePathJsonArr = null;
		JSONArray funnelListJsonArr = new JSONArray();
		String sourceMedium = null;
		String pagePath = null;
		String subscriber_email = null;
		String hostname = null;
		ArrayList<Double> timeOnPageArrList = null;
		ArrayList<Integer> sessionCountArrList = null;
		ArrayList<String> dateHourMinuteArrList = null;
		ArrayList<Integer> sessionDurationBucket = null;
		ArrayList<String> hostNameArrList = null;
		TreeSet<String> UniqueSubscribers = new TreeSet<String>();
		org.json.JSONArray finalJsonArrayForRuleEngine = new org.json.JSONArray();
		String campaign_id = null;
		String ga_user = null;
		String source = null;
		Map<String, String> campaignDetailsMap = null;
		ArrayList<InsertOneModel<Document>> recent_view_set_doc_arrlist = new ArrayList<InsertOneModel<Document>>();
		// ArrayList<InsertOneModel<Document>> insertList = new
		// ArrayList<InsertOneModel<Document>>();

		Document recent_view_set_doc = null;
		MongoClientURI connectionString = null;

		try {
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();=ConnectionHelper.getConnection();
			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);

			DistinctIterable<String> dimension2Di = collection.distinct("dimension2", String.class);
			MongoCursor<String> dimension2Cursor = dimension2Di.iterator();
			// int count=0;
			try {
				while (dimension2Cursor.hasNext()) {
					subscriber_email = dimension2Cursor.next().toString();
					if (subscriber_email.contains("@")) {
						// System.out.println("subscriber_email : "+ count++ + " : "+subscriber_email);
						UniqueSubscribers.add(subscriber_email);
					}
				}
			} finally {
				dimension2Cursor.close();
			}
			System.out.println("Total Number of Subscribers Found  : " + UniqueSubscribers.size());
			for (String temp_subscriber_email : UniqueSubscribers) {
				System.out.println("temp_subscriber_email : " + temp_subscriber_email);
				Bson filter1 = and(eq("dimension2", temp_subscriber_email));
				DistinctIterable<String> sourceMediumDi = collection.distinct("sourceMedium", filter1, String.class);
				MongoCursor<String> sourceMediumCursor = sourceMediumDi.iterator();

				try {
					while (sourceMediumCursor.hasNext()) {
						sourceMedium = sourceMediumCursor.next().toString();
						if (sourceMedium.contains("phplist")) {
							campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1).replace("phplist",
									"");
							source = "Email";
						} else if (sourceMedium.contains("(direct)")) {
							source = "Direct";
							campaign_id = "NULL";
						}
						// if(campaign_id!=null){
						if (!campaign_id.equals("NULL")) {
							// campaignDetailsMap=GetCampaignDetailsBasedOnCampaignIdAndSubscriberIdFromMongo(campaign_id,temp_subscriber_email);
							campaignDetailsMap = GetCampaignDetailsBasedOnSubscriberIdFromMongo(campaign_id,
									temp_subscriber_email);
						} else {
							campaignDetailsMap = new HashMap<String, String>();
							campaignDetailsMap.put("Created_By", "NA");
							// jsonObject.put("Created_By","viki_gmail.com");
							campaignDetailsMap.put("Funnel_Name", "NA");
							campaignDetailsMap.put("SubFunnel_Name", "NA");
							campaignDetailsMap.put("Category", "NA");
							campaignDetailsMap.put("Campaign_Id", "NA");
							campaignDetailsMap.put("List_Id", "NA");
							campaignDetailsMap.put("Subscriber_Id", "NA");// 7113
							campaignDetailsMap.put("Subscriber_Email", "NA");
						}
						Bson filter2 = null;
						System.out.println("-----------Unique sourceMedium : " + sourceMedium
								+ "  : sourceMedium Unique-------------------------");
						Bson page_path_filter = and(eq("dimension2", temp_subscriber_email),
								eq("sourceMedium", sourceMedium));
						pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(collection, page_path_filter);
						for (int i = 0; i < pagePathJsonArr.size(); i++) {
							// Creating New Document Object For URL VIEW
							recent_view_set_doc = new Document();

							recent_view_set_doc.put("Created_By", campaignDetailsMap.get("CreatedBy").toString());
							// jsonObject.put("Created_By","viki_gmail.com");
							recent_view_set_doc.put("Funnel_Name", campaignDetailsMap.get("funnelName").toString());
							recent_view_set_doc.put("SubFunnel_Name",
									campaignDetailsMap.get("SubFunnelName").toString());
							recent_view_set_doc.put("Category", campaignDetailsMap.get("Type").toString());
							recent_view_set_doc.put("Campaign_Id", campaignDetailsMap.get("Campaign_Id").toString());
							recent_view_set_doc.put("List_Id", campaignDetailsMap.get("List_Id").toString());
							recent_view_set_doc.put("Subscriber_Id",
									campaignDetailsMap.get("subscriber_userid").toString());// 7113
							recent_view_set_doc.put("Subscriber_Email", temp_subscriber_email);

							recent_view_set_doc.put("Subscriber_Email", subscriber_email);
							recent_view_set_doc.put("sourceMedium", sourceMedium);
							recent_view_set_doc.put("Source", source);
							recent_view_set_doc.put("campaign_id", campaign_id);

							pagePath = (String) pagePathJsonArr.get(i);
							System.out.println("pagePath : " + pagePath);
							timeOnPageArrList = new ArrayList<Double>();
							sessionCountArrList = new ArrayList<Integer>();
							dateHourMinuteArrList = new ArrayList<String>();
							sessionDurationBucket = new ArrayList<Integer>();
							hostNameArrList = new ArrayList<String>();
							filter2 = and(eq("sourceMedium", sourceMedium), eq("pagePath", pagePath),
									eq("dimension2", temp_subscriber_email));
							FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
							MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
							while (campaignWisePagePathCursor.hasNext()) {
								Document campaignWisePagePath = campaignWisePagePathCursor.next();
								System.out.println("campaignWisePagePath : " + campaignWisePagePath);
								ga_user = campaignWisePagePath.get("ga_username").toString();
								/*
								 * System.out.println("campaignWisePagePath : "+campaignWisePagePath.getString(
								 * "pagePath") +"    timeOnPage : "+campaignWisePagePath.getString("timeOnPage")
								 * +"    dateHourMinute : "+campaignWisePagePath.getString("dateHourMinute"));
								 */
								timeOnPageArrList
										.add(Double.parseDouble(campaignWisePagePath.get("timeOnPage").toString()));
								sessionCountArrList
										.add(Integer.parseInt(campaignWisePagePath.get("sessionCount").toString()));
								System.out.println(
										"sessionCount : " + campaignWisePagePath.get("sessionCount").toString());
								dateHourMinuteArrList.add(campaignWisePagePath.get("dateHourMinute").toString());
								sessionDurationBucket.add(
										Integer.parseInt(campaignWisePagePath.get("sessionDurationBucket").toString()));
								hostNameArrList.add(campaignWisePagePath.get("hostname").toString());
							}
							recent_view_set_doc.put("ga_user", ga_user);
							if (dateHourMinuteArrList.size() > 0) {
								System.out.println("dateHourMinuteArrList : "
										+ dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
								recent_view_set_doc.put("firstclick", dateHourMinuteArrList.get(0));
								recent_view_set_doc.put("lastclick",
										dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
							}
							if (hostNameArrList.size() > 0) {
								hostname = hostNameArrList.get(hostNameArrList.size() - 1);
								System.out.println("hostname : " + hostname);
								recent_view_set_doc.put("hostname", hostname);
							}
							if (pagePath.length() == 1) {
								pagePath = hostname;
								System.out.println(pagePath);
							} else {
								if (pagePath.contains("/?")) {
									pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
									// System.out.println(path1);
								} else if (pagePath.contains("?")) {
									pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
								} else if (pagePath.contains("/")) {
									pagePath = hostname + pagePath;
								}
							}
							recent_view_set_doc.put("url", pagePath);

							Map<String, String> timeOnPageMap = ArrayListOperationsForDoubleValue(timeOnPageArrList);
							System.out.println("timeOnPageMap : " + timeOnPageMap);
							recent_view_set_doc.put("AvgTimeOnPage", Double.parseDouble(timeOnPageMap.get("Average")));
							recent_view_set_doc.put("MinTimeOnPage", Double.parseDouble(timeOnPageMap.get("Min")));
							recent_view_set_doc.put("MaxTimeOnPage", Double.parseDouble(timeOnPageMap.get("Max")));
							recent_view_set_doc.put("TotalTimeOnPage", Double.parseDouble(timeOnPageMap.get("Sum")));

							Map<String, String> sessionCountMap = ArrayListOperationsForIntegerValue(
									sessionCountArrList);
							System.out.println("sessionCountMap : " + sessionCountMap);
							recent_view_set_doc.put("Last_Session_Count", Integer.parseInt(sessionCountMap.get("Max")));
							recent_view_set_doc.put("No_OF_Clicks", Integer.parseInt(sessionCountMap.get("Count")));

							Map<String, String> sessionDurationBucketMap = ArrayListOperationsForIntegerValue(
									sessionDurationBucket);
							System.out.println("sessionDurationBucketMap : " + sessionDurationBucketMap);
							recent_view_set_doc.put("AvgSesionDuration",
									Double.parseDouble(sessionDurationBucketMap.get("Average")));
							recent_view_set_doc.put("MinSesionDuration",
									Integer.parseInt(sessionDurationBucketMap.get("Min")));
							recent_view_set_doc.put("MaxSesionDuration",
									Integer.parseInt(sessionDurationBucketMap.get("Max")));
							recent_view_set_doc.put("TotalSesionDuration",
									Integer.parseInt(sessionDurationBucketMap.get("Sum")));

							recent_view_set_doc_arrlist.add(new InsertOneModel<Document>(recent_view_set_doc));
						}
						System.out.println("recent_view_set_doc_arrlist : " + recent_view_set_doc_arrlist.size());
						;
						saveRecentSubscriberData(database, "google_analytics_recent_url_view_collection",
								recent_view_set_doc_arrlist, campaign_id, temp_subscriber_email);
						// break;
					}
				} finally {
					sourceMediumCursor.close();
				}
				System.out.println(finalJsonArrayForRuleEngine);
				// break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
			System.out.println("Exception : " + e.getMessage());
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);
		}
		return funnelListJsonArr;
	}

	public static void saveRecentSubscriberData(MongoDatabase database, String coll_name,
			ArrayList<InsertOneModel<Document>> documents, String campaign_id, String temp_subscriber_email) {
		// google_analytics_data_temp

		MongoCollection<Document> google_analytics_recent_url_view_collection = null;
		try {
			google_analytics_recent_url_view_collection = database.getCollection(coll_name);
			BulkWriteOptions options = new BulkWriteOptions();
			options.ordered(false);
			// google_analytics_recent_url_view_collection.insertMany(documents);
			google_analytics_recent_url_view_collection.bulkWrite(documents, options);
			// createURLViewData("temp_google_analytics_subscriber_data",subscriber_email);
		} catch (Exception ex) {
			System.out.println("Method saveSubscriberTempData() Error : " + ex.getMessage());
		} finally {
			System.out.println("Records Saved In Collection 'temp_google_analytics_subscriber_data'");
		}
	}

	public static void fetchRecentGADataForRuleEngineTest(String coll_name, String sourceMedium,
			String temp_subscriber_email) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		int int_recent_days_1 = 0;
		Date tmp_date1 = null;
		org.json.JSONArray finalJsonArrayForRuleEngine = new org.json.JSONArray();
		MongoClientURI connectionString = null;
		try {

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String recent_days_1 = ResourceBundle.getBundle("config").getString("recent_days_1");
			// int int_recent_days_1=Integer.parseInt(recent_days_1);
			int_recent_days_1 = 20;
			Date date_campare1 = new Date();
			date_campare1.setDate(date_campare1.getDate() - int_recent_days_1);
			tmp_date1 = dateFormat.parse(dateFormat.format(date_campare1));
			Bson filter1 = gt("dateHourMinuteInDateFormat", tmp_date1);

			FindIterable<Document> campaignWisePagePathFi = collection.find(filter1);
			MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
			while (campaignWisePagePathCursor.hasNext()) {
				Document campaignWisePagePath = campaignWisePagePathCursor.next();
				finalJsonArrayForRuleEngine.put(campaignWisePagePath.toJson());
				System.out.println(campaignWisePagePath.toJson());
			}
			System.out.println("Size : " + finalJsonArrayForRuleEngine.length());
			System.out.println(finalJsonArrayForRuleEngine);
		} catch (Exception ex) {

		} finally {
			Bson filter2 = lte("dateHourMinuteInDateFormat", tmp_date1);
			collection.deleteMany(filter2);

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);

		}
	}

	public static JSONArray GetAvgSessionDuration(String coll_name, String sourceMedium) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> campaign_collection = null;
		JSONArray funnelListJsonArr = new JSONArray();
		JSONObject availableUrlJsonObj = null;
		String sessionCount = null;
		ArrayList<Integer> sessionDurationBucketArrList = null;
		MongoClientURI connectionString = null;
		try {

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);

			Bson filter1 = and(eq("sourceMedium", sourceMedium));
			DistinctIterable<String> sessionCountDi = collection.distinct("sessionCount", filter1, String.class);

			// DistinctIterable<String> sessionCountDi = collection.distinct("sessionCount",
			// String.class);
			MongoCursor<String> sessionCountDiCursor = sessionCountDi.iterator();
			sessionDurationBucketArrList = new ArrayList<Integer>();
			try {
				while (sessionCountDiCursor.hasNext()) {
					// pagePath
					sessionCount = sessionCountDiCursor.next().toString();

					System.out.println("Unique sessionCount : " + sessionCount);

					Bson filter3 = eq("sessionCount", sessionCount);
					FindIterable<Document> sessionCountFi = collection.find(filter3);
					MongoCursor<Document> sessionCountCursor = sessionCountFi.iterator();

					try {
						while (sessionCountCursor.hasNext()) {
							Document doc = sessionCountCursor.next();
							sessionDurationBucketArrList.add(Integer.parseInt(doc.getString("sessionDurationBucket")));
							break;
						}
					} finally {
						sessionCountCursor.close();
					}
				}
				System.out.println("sessionDurationBucketArrList : " + sessionDurationBucketArrList);
				ArrayListOperationsForIntegerValue(sessionDurationBucketArrList);
			} finally {
				sessionCountDiCursor.close();
			}
			System.out.println("funnelListJsonArr : " + funnelListJsonArr);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);
		}
		return funnelListJsonArr;
	}

	public static void saveSubscriberTempData(String coll_name, String subscriber_email) {
		// google_analytics_data_temp

		MongoClient mongoClient = null;
		MongoDatabase database = null;
		Bson filter1 = null;
		MongoCollection<Document> google_analytics_data_temp_collection = null;
		MongoCollection<Document> temp_google_analytics_subscriber_data_collection = null;
		ArrayList<Document> documents = new ArrayList<Document>();
		MongoClientURI connectionString = null;
		try {
			// mongoClient=ConnectionHelper.getConnection();

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			google_analytics_data_temp_collection = database.getCollection(coll_name);
			filter1 = eq("dimension2", subscriber_email);
			FindIterable<Document> dimension2Fi = google_analytics_data_temp_collection.find(filter1);
			MongoCursor<Document> dimension2FiCursor = dimension2Fi.iterator();
			try {
				while (dimension2FiCursor.hasNext()) {
					Document doc = dimension2FiCursor.next();
					documents.add(doc);
					// System.out.println(doc);
				}
			} finally {
				dimension2FiCursor.close();
			}
			temp_google_analytics_subscriber_data_collection = database
					.getCollection("temp_google_analytics_subscriber_data");
			temp_google_analytics_subscriber_data_collection.drop();
			temp_google_analytics_subscriber_data_collection.insertMany(documents);
			// createURLViewData("temp_google_analytics_subscriber_data",subscriber_email);
		} catch (Exception ex) {
			System.out.println("Method saveSubscriberTempData() Error : " + ex.getMessage());
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);
		}
	}

	public static JSONArray findUniqueCampaignForSubscriber(String coll_name, String Sling_Campaign_Id) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> campaign_collection = null;
		JSONArray pagePathJsonArr = new JSONArray();
		JSONArray funnelListJsonArr = new JSONArray();
		JSONObject availableUrlJsonObj = null;
		String sourceMedium = null;
		String pagePath = null;
		MongoClientURI connectionString = null;
		try {
			// mongoClient=ConnectionHelper.getConnection();

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			DistinctIterable<String> pagePathDi = collection.distinct("pagePath", String.class);
			MongoCursor<String> pagePathCursor = pagePathDi.iterator();
			try {
				while (pagePathCursor.hasNext()) {
					pagePathJsonArr.add(pagePathCursor.next().toString());
				}
			} finally {
				pagePathCursor.close();
			}
			DistinctIterable<String> sourceMediumDi = collection.distinct("sourceMedium", String.class);
			MongoCursor<String> sourceMediumCursor = sourceMediumDi.iterator();
			try {
				Bson filter1 = null;
				while (sourceMediumCursor.hasNext()) {
					TreeSet<Integer> sessionCountUniqueValues = new TreeSet<Integer>();// ga:sessionCount
					TreeSet<String> pagePathUniqueValues = new TreeSet<String>();// ga:sourceMedium
					TreeSet<String> sourceMediumUniqueValues = new TreeSet<String>();
					sourceMedium = sourceMediumCursor.next().toString();
					System.out.println("---------------------------------------------------------Unique sourceMedium : "
							+ sourceMedium);
					ArrayList<Double> timeOnPageArrList = null;
					ArrayList<Integer> sessionCountArrList = null;
					ArrayList<String> dateHourMinuteArrList = null;
					ArrayList<Integer> sessionDurationBucket = null;
					for (int i = 0; i < pagePathJsonArr.size(); i++) {
						pagePath = (String) pagePathJsonArr.get(i);
						System.out.println("pagePath : " + pagePath);
						timeOnPageArrList = new ArrayList<Double>();
						sessionCountArrList = new ArrayList<Integer>();
						dateHourMinuteArrList = new ArrayList<String>();
						sessionDurationBucket = new ArrayList<Integer>();
						filter1 = and(eq("sourceMedium", sourceMedium), eq("pagePath", pagePath));
						FindIterable<Document> campaignWisePagePathFi = collection.find(filter1);
						MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
						while (campaignWisePagePathCursor.hasNext()) {
							Document campaignWisePagePath = campaignWisePagePathCursor.next();
							System.out.println("campaignWisePagePath : " + campaignWisePagePath.getString("pagePath")
									+ "    timeOnPage : " + campaignWisePagePath.getString("timeOnPage")
									+ "    dateHourMinute : " + campaignWisePagePath.getString("dateHourMinute"));
							timeOnPageArrList
									.add(Double.parseDouble(campaignWisePagePath.get("timeOnPage").toString()));
							sessionCountArrList
									.add(Integer.parseInt(campaignWisePagePath.get("sessionCount").toString()));
							dateHourMinuteArrList.add(campaignWisePagePath.get("dateHourMinute").toString());
							sessionDurationBucket.add(
									Integer.parseInt(campaignWisePagePath.get("sessionDurationBucket").toString()));
							// country pagePath sessionCount dimension2 channelGrouping
							// sessionDurationBucket
							// sessiondurationBucket hostname timeOnPage bounces sourceMedium dateHourMinute
						}
						System.out.println("timeOnPageArrList : " + timeOnPageArrList);
						ArrayListOperationsForDoubleValue(timeOnPageArrList);
						System.out.println("sessionCountArrList : " + sessionCountArrList);
						ArrayListOperationsForIntegerValue(sessionCountArrList);
						System.out.println("dateHourMinuteArrList : " + dateHourMinuteArrList);
						if (dateHourMinuteArrList.size() > 0) {
							System.out.println("dateHourMinuteArrList : "
									+ dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
						}
						System.out.println("sessionDurationBucket : " + sessionDurationBucket);
						ArrayListOperationsForIntegerValue(sessionDurationBucket);
					}
					break;
				}
			} finally {
				sourceMediumCursor.close();
			}
			System.out.println("funnelListJsonArr : " + funnelListJsonArr);

		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
			System.out.println("Exception : " + e.getMessage());
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}

			// ConnectionHelper.closeConnection(mongoClient);
		}
		return funnelListJsonArr;
	}

	public static TreeSet<String> findUniqueCampaign(String coll_name) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		String campaign_id = null;
		TreeSet<String> UniqueCampaigns = new TreeSet<String>();// ga:sourceMedium
		MongoClientURI connectionString = null;
		try {

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			collection.drop();
			// remove(new BasicDBObject());
			DistinctIterable<String> sourceMediumDi = collection.distinct("sourceMedium", String.class);
			MongoCursor<String> sourceMediumCursor = sourceMediumDi.iterator();
			int count = 0;
			try {
				while (sourceMediumCursor.hasNext()) {
					campaign_id = sourceMediumCursor.next().toString();
					if (campaign_id.contains("phplist") || campaign_id.contains("direct")) {
						System.out.println("campaign_id : " + campaign_id);
						UniqueCampaigns.add(campaign_id);
					}
				}
			} finally {
				sourceMediumCursor.close();

				// ConnectionHelper.closeConnection(mongoClient);

			}
			System.out.println("UniqueCampaigns : " + UniqueCampaigns);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
		}
		return UniqueCampaigns;
	}

	public static String saveTempSubscriberGAData(ArrayList<Document> documents, String coll_name, String username) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoClientURI connectionString = null;
		try {

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			collection.drop();
			// collection.deleteMany(filter, options)
			collection.insertMany(documents);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);

		}
		return "true";
	}

	public static String saveTempSubscriberGADataOld(ArrayList<Document> documents, String coll_name, String username) {
		MongoSetup mongo = new MongoSetup("salesautoconvert");
		DBCollection temp_google_analytics_subscriber_data = mongo
				.getCollection("temp_google_analytics_subscriber_data");
		temp_google_analytics_subscriber_data.remove(new BasicDBObject());
		JSONArray data_json_arr = null;
		BasicDBObject document = null;
		try {
			// temp_google_analytics_subscriber_data.insert(documents);
			// insert(documents);
		} catch (Exception ex) {
			System.out.println("Inside saveTempGAData() Method Got Error : " + ex.getMessage());
		}
		return "true";
	}

	public static JSONArray getUniquePagePath(String coll_name, String Sling_Campaign_Id) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> campaign_collection = null;
		JSONArray funnelListJsonArr = new JSONArray();
		JSONObject availableUrlJsonObj = null;
		String pagePath = null;
		MongoClientURI connectionString = null;
		try {
			// mongoClient=ConnectionHelper.getConnection();

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			DistinctIterable<String> di = collection.distinct("pagePath", String.class);
			MongoCursor<String> cursor = di.iterator();
			try {
				while (cursor.hasNext()) {
					// pagePath
					pagePath = cursor.next().toString();
					funnelListJsonArr.add(pagePath);
				}
			} finally {
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);

		}
		return funnelListJsonArr;
	}

	public static JSONArray findUniqueUrl(String coll_name, String Sling_Campaign_Id) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> campaign_collection = null;
		JSONArray funnelListJsonArr = new JSONArray();
		JSONObject availableUrlJsonObj = null;
		String pagePath = null;
		MongoClientURI connectionString = null;
		try {
			// mongoClient=ConnectionHelper.getConnection();

			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			// Bson filter1 =eq("funnelNodeName", Sling_Campaign_Id);
			// DistinctIterable<String> fi =
			// collection.distinct("subFunnelNodeName",filter1,String.class);

			DistinctIterable<String> di = collection.distinct("pagePath", String.class);
			MongoCursor<String> cursor = di.iterator();
			/*
			 * FindIterable<Document> fi = collection.find(); MongoCursor<Document> cursor =
			 * fi.iterator();
			 */
			try {
				while (cursor.hasNext()) {
					// pagePath
					pagePath = cursor.next().toString();
					System.out.println("Unique pagePath : " + pagePath);

					int campaign_count = 0;
					String urlclickstatistics_latestclick = null;
					int url_click_count = 0;

					Bson filter3 = eq("pagePath", pagePath);
					FindIterable<Document> ttl_click_fi = collection.find(filter3);
					MongoCursor<Document> ttl_clickcursor = ttl_click_fi.iterator();

					try {
						while (ttl_clickcursor.hasNext()) {
							Document doc = ttl_clickcursor.next();
							System.out.println(doc);
							/*
							 * //System.out.println(doc.getString("urlclickstatistics_latestclick"));
							 * if(url_click_count==0){
							 * urlclickstatistics_latestclick=doc.getString("urlclickstatistics_latestclick"
							 * ); } //System.out.println("---"+ttl_clickcursor.next()); ++url_click_count;
							 */
						}
					} finally {
						ttl_clickcursor.close();
					}
					/*
					 * System.out.println(pagePath); Bson filter2 =eq("pagePath", pagePath);
					 * DistinctIterable<String> campaign_di = collection.distinct("id",filter2,
					 * String.class); MongoCursor<String> campaign_cursor = campaign_di.iterator();
					 * //System.out.println("---"+campaign_cursor.hasNext());
					 * 
					 * try { while(campaign_cursor.hasNext()) { campaign_cursor.next();
					 * //System.out.println("---"+campaign_cursor.next()); ++campaign_count;
					 * 
					 * } } finally { campaign_cursor.close(); } availableUrlJsonObj=new
					 * JSONObject(); availableUrlJsonObj.put("pagePath", pagePath);
					 * availableUrlJsonObj.put("MSGS", campaign_count);
					 * availableUrlJsonObj.put("LAST_CLICKED", urlclickstatistics_latestclick);
					 * availableUrlJsonObj.put("CLICKS", url_click_count);
					 * funnelListJsonArr.add(availableUrlJsonObj);
					 */
				}
			} finally {
				cursor.close();
			}
			System.out.println("funnelListJsonArr : " + funnelListJsonArr);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);
		}
		return funnelListJsonArr;
	}

	public static JSONArray createURLViewDataTest(String coll_name, String subscriber_email) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> url_view_collection = null;
		JSONArray pagePathJsonArr = null;
		JSONArray funnelListJsonArr = new JSONArray();
		String sourceMedium = null;
		String pagePath = null;
		// String subscriber_email=null;
		String hostname = null;
		ArrayList<Double> timeOnPageArrList = null;
		ArrayList<Integer> sessionCountArrList = null;
		ArrayList<String> dateHourMinuteArrList = null;
		ArrayList<Integer> sessionDurationBucket = null;
		ArrayList<String> hostNameArrList = null;
		String campaign_id = null;
		String ga_user = null;
		String source = null;

		ArrayList<Document> finalDocumentsArrayForURLView = new ArrayList<Document>();
		Document url_view_doc_object = null;
		MongoClientURI connectionString = null;
		try {
			// mongoClient=ConnectionHelper.getConnection();
			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			url_view_collection = database.getCollection("google_analytics_url_view_collection");
			System.out.println("temp_subscriber_email : " + subscriber_email);
			Bson filter1 = and(eq("dimension2", subscriber_email));
			DistinctIterable<String> sourceMediumDi = collection.distinct("sourceMedium", filter1, String.class);
			MongoCursor<String> sourceMediumCursor = sourceMediumDi.iterator();

			try {
				while (sourceMediumCursor.hasNext()) {
					sourceMedium = sourceMediumCursor.next().toString();
					if (sourceMedium.contains("phplist")) {
						campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1).replace("phplist", "");
						source = "Email";
					} else if (sourceMedium.contains("(direct)")) {
						source = "Direct";
						campaign_id = "NULL";
					}
					Bson filter2 = null;
					System.out.println("-----------Unique sourceMedium : " + sourceMedium
							+ "  : sourceMedium Unique-------------------------");
					Bson page_path_filter = and(eq("dimension2", subscriber_email), eq("sourceMedium", sourceMedium));
					pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(collection, page_path_filter);
					for (int i = 0; i < pagePathJsonArr.size(); i++) {
						// New Json Object Will be created Here ONLY
						url_view_doc_object = new Document();
						url_view_doc_object.put("Subscriber_Email", subscriber_email);
						url_view_doc_object.put("sourceMedium", sourceMedium);
						url_view_doc_object.put("Source", source);
						url_view_doc_object.put("campaign_id", campaign_id);

						pagePath = (String) pagePathJsonArr.get(i);
						System.out.println("pagePath : " + pagePath);
						timeOnPageArrList = new ArrayList<Double>();
						sessionCountArrList = new ArrayList<Integer>();
						dateHourMinuteArrList = new ArrayList<String>();
						sessionDurationBucket = new ArrayList<Integer>();
						hostNameArrList = new ArrayList<String>();
						filter2 = and(eq("sourceMedium", sourceMedium), eq("pagePath", pagePath),
								eq("dimension2", subscriber_email));
						FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
						MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
						while (campaignWisePagePathCursor.hasNext()) {
							Document campaignWisePagePath = campaignWisePagePathCursor.next();
							System.out.println("campaignWisePagePath : " + campaignWisePagePath);
							ga_user = campaignWisePagePath.get("ga_username").toString();
							/*
							 * System.out.println("campaignWisePagePath : "+campaignWisePagePath.getString(
							 * "pagePath") +"    timeOnPage : "+campaignWisePagePath.getString("timeOnPage")
							 * +"    dateHourMinute : "+campaignWisePagePath.getString("dateHourMinute"));
							 */
							timeOnPageArrList
									.add(Double.parseDouble(campaignWisePagePath.get("timeOnPage").toString()));
							sessionCountArrList
									.add(Integer.parseInt(campaignWisePagePath.get("sessionCount").toString()));
							System.out.println("sessionCount : " + campaignWisePagePath.get("sessionCount").toString());
							dateHourMinuteArrList.add(campaignWisePagePath.get("dateHourMinute").toString());
							sessionDurationBucket.add(
									Integer.parseInt(campaignWisePagePath.get("sessionDurationBucket").toString()));
							hostNameArrList.add(campaignWisePagePath.get("hostname").toString());
						}
						url_view_doc_object.put("ga_user", ga_user);
						if (hostNameArrList.size() > 0) {
							hostname = hostNameArrList.get(hostNameArrList.size() - 1);
							url_view_doc_object.put("hostname", hostname);
							System.out.println("hostname : " + hostname);
						}
						if (dateHourMinuteArrList.size() > 0) {
							System.out.println("dateHourMinuteArrList : "
									+ dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
							url_view_doc_object.put("firstclick", dateHourMinuteArrList.get(0));
							url_view_doc_object.put("lastclick",
									dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
						}
						if (pagePath.length() == 1) {
							pagePath = hostname;
							System.out.println(pagePath);
						} else {
							if (pagePath.contains("/?")) {
								pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
								// System.out.println(path1);
							} else if (pagePath.contains("?")) {
								pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
							} else if (pagePath.contains("/")) {
								pagePath = hostname + pagePath;
							}
						}
						url_view_doc_object.put("url", pagePath);

						Map<String, String> timeOnPageMap = ArrayListOperationsForDoubleValue(timeOnPageArrList);
						System.out.println("timeOnPageMap : " + timeOnPageMap);
						// rule_json_object.put(pagePath,timeOnPageMap.get("Average").toString());
						url_view_doc_object.put("AvgTimeOnPage", timeOnPageMap.get("Average").toString());
						url_view_doc_object.put("MinTimeOnPage", timeOnPageMap.get("Min").toString());
						url_view_doc_object.put("MaxTimeOnPage", timeOnPageMap.get("Max").toString());
						url_view_doc_object.put("TotalTimeOnPage", timeOnPageMap.get("Sum").toString());

						Map<String, String> sessionCountMap = ArrayListOperationsForIntegerValue(sessionCountArrList);
						System.out.println("sessionCountMap : " + sessionCountMap);
						url_view_doc_object.put("Last_Session_Count", sessionCountMap.get("Max"));
						url_view_doc_object.put("No_OF_Clicks", Integer.parseInt(sessionCountMap.get("Count")));

						Map<String, String> sessionDurationBucketMap = ArrayListOperationsForIntegerValue(
								sessionDurationBucket);
						System.out.println("sessionDurationBucketMap : " + sessionDurationBucketMap);
						// rule_json_object.put("Total_Session_Time",sessionDurationBucketMap.get("Sum"));
						url_view_doc_object.put("AvgSesionDuration",
								sessionDurationBucketMap.get("Average").toString());
						url_view_doc_object.put("MinSesionDuration", sessionDurationBucketMap.get("Min").toString());
						url_view_doc_object.put("MaxSesionDuration", sessionDurationBucketMap.get("Max").toString());
						url_view_doc_object.put("TotalSesionDuration", sessionDurationBucketMap.get("Sum").toString());

						finalDocumentsArrayForURLView.add(url_view_doc_object);

						Bson filter = Filters.eq("_id", "");

						Bson update = new Document("$set", url_view_doc_object);

						UpdateOptions options = new UpdateOptions().upsert(true);

						Document modifiedObject = new Document();
						modifiedObject.put("$inc", new BasicDBObject().append("No_OF_Clicks", 5));

						url_view_collection.updateOne(url_view_doc_object, modifiedObject, options);
						// url_view_collection.insertOne(url_view_doc_object);

					}
					Bson filter = and(eq("sourceMedium", sourceMedium), eq("dimension2", subscriber_email));
					Map<String, String> AvgSessionDurationBucketMap = GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId(
							collection, filter);
					System.out.println("AvgSessionDurationBucketMap : " + AvgSessionDurationBucketMap);
					// url_view_doc_object.put("Total_Session_Time",AvgSessionDurationBucketMap.get("Average"));
					// Getting Campaign Details From Sling
					System.out.println("campaign_id : " + campaign_id);
					if (campaign_id != null) {
						/*
						 * String get_campaign_url=ResourceBundle.getBundle("config").getString(
						 * "getCampainsFunnelDetails"); String
						 * get_campaign_url_response=getRequestForCampaignDetailsFromSling(
						 * get_campaign_url+campaign_id,"");
						 * System.out.println("get_campaign_url_response : "+get_campaign_url_response);
						 * org.json.JSONObject res_obj=new
						 * org.json.JSONObject(get_campaign_url_response); System.out.println(res_obj);
						 * rule_json_object.put("campaign_details",res_obj);
						 */
					}

					// finalJsonArrayForRuleEngine.put(rule_json_object);
					// break;
				}
			} finally {
				sourceMediumCursor.close();
			}
			// System.out.println("Total Records Based on URL View :
			// "+finalJsonArrayForRuleEngine.length());
			// System.out.println(finalJsonArrayForRuleEngine);
			// break;
			// saveSubscriberTempData("google_analytics_data_temp",subscriber_email);
			// Saving URL View Data To Collection
			// url_view_collection=database.getCollection("google_analytics_url_view_collection");
			// url_view_collection.insertMany(finalDocumentsArrayForURLView);

//		        Bson filter = Filters.eq("_id", "");
//
//		        Bson update =  new Document("$set",finalDocumentsArrayForURLView);
//		        
//		        UpdateOptions options = new UpdateOptions().upsert(true);
//		        url_view_collection.updateOne(filter, update, options);
			// url_view_collection.createIndex(keys, indexOptions)
			// db.collection.update(doc, doc, {upsert:true})

		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
			System.out.println("Exception : " + e.getMessage());
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);
		}
		return funnelListJsonArr;
	}
	// monitoring data

	public static JSONObject fetchGADataForMonitoring(String coll_name, String campaign_id, String SubFunnel_Name,
			String Funnel_Name) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> google_analytics_data_temp = null;
		MongoCollection<Document> google_analytics_url_view_collection = null;
		JSONArray pagePathJsonArr = null;
		JSONArray funnelListJsonArr = new JSONArray();
		String pagePath = null;
		String subscriber_email = null;
		ArrayList<Double> timeOnPageArrList = null;
		ArrayList<Double> avgTimeOnPageArrList = null;
		ArrayList<Integer> sessionCountArrList = null;
		ArrayList<String> dateHourMinuteArrList = null;
		ArrayList<Integer> sessionDurationBucket = null;
		ArrayList<String> hostNameArrList = null;
		TreeSet<String> UniqueSubscribers = new TreeSet<String>();
		org.json.JSONArray finalJsonArrayForRuleEngine = new org.json.JSONArray();
		org.json.JSONObject rule_json_object = null;
		String source = null;
		Map<String, String> campaignDetailsMap = null;

		String Source = null;
		String Subscriber_Email = null;
//	    String campaign_id=null;
		String ga_user = null;
		String hostname = null;
		String sourceMedium = null;
		String url = null;
		double TotalTimeOnPage = 0;
		int NoOfUrlClicks = 0;
		int TotalSesionDuration = 0;
		int SessionCount = 0;
		String Created_By = null;
//	    String Funnel_Name=null;
//	    String SubFunnel_Name=null;
		String Category = null;
		String Campaign_Id = null;
		String List_Id = null;
		String Subscriber_Id = null;
		String lastclick = null;
		double AvgTimeOnPage = 0;
		double MinTimeOnPage = 0;
		double MaxTimeOnPage = 0;
		int LastSessionCount = 0;
		double AvgSesionDuration = 0;
		int MinSesionDuration = 0;
		int MaxSesionDuration = 0;
		String firstclick = null;
		ArrayList<Integer> LastSessionCountArrList = null;
		ArrayList<Double> AvgSesionDurationArrList = null;
		logger.info("Calling fetchGADataForRuleEngine : ");
		MongoClientURI connectionString = null;
		JSONObject finaljson = new JSONObject();
		JSONArray finajsa = new JSONArray();

		try {
			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			google_analytics_data_temp = database.getCollection("google_analytics_recent_data_temp");
			google_analytics_url_view_collection = database.getCollection("google_analytics_url_view_collection");

			DistinctIterable<String> dimension2Di = google_analytics_data_temp.distinct("dimension2", String.class);
			MongoCursor<String> dimension2Cursor = dimension2Di.iterator();
			// int count=0;
			try {
				while (dimension2Cursor.hasNext()) {
					subscriber_email = dimension2Cursor.next().toString();
					if (subscriber_email.contains("@")) {
						// System.out.println("subscriber_email : "+ count++ + " : "+subscriber_email);
						UniqueSubscribers.add(subscriber_email);
					}
				}
			} finally {
				dimension2Cursor.close();
			}
			System.out.println("Total Number of Subscribers Found  : " + UniqueSubscribers);
			logger.info("Total Number of Subscribers Found  : " + UniqueSubscribers.size());
			for (String temp_subscriber_email : UniqueSubscribers) {
				System.out.println("temp_subscriber_email : " + temp_subscriber_email);

//	        	logger.info("Subscriber Email : "+temp_subscriber_email);
//	        	Bson filter1 =and(eq("dimension2", temp_subscriber_email));
//	        	DistinctIterable<String> sourceMediumDi = google_analytics_data_temp.distinct("sourceMedium", filter1,String.class);
//		        MongoCursor<String> sourceMediumCursor = sourceMediumDi.iterator();

				try {
//		        	while(sourceMediumCursor.hasNext()) {
//						sourceMedium=sourceMediumCursor.next().toString();
////						if(sourceMedium.contains("phplist")){
////							campaign_id=sourceMedium.substring(0, sourceMedium.indexOf("/")-1).replace("phplist", "");
////							source="Email";
////						}else if(sourceMedium.contains("(direct)")){
////							source="Direct";
////							campaign_id="NULL";
////						}
//						logger.info("Campaign Source For Subscriber : "+sourceMedium);
//						rule_json_object=new org.json.JSONObject();
//			        	//rule_json_object.put("Subscriber_Email",temp_subscriber_email);
//						//rule_json_object.put("sourceMedium",sourceMedium);
//						
//						//System.out.println("-----------Unique sourceMedium : "+sourceMedium+"  : sourceMedium Unique-------------------------");
//						logger.info("Campaign Source For Subscriber : "+sourceMedium);
//						Bson page_path_filter =and(eq("dimension2", temp_subscriber_email),eq("sourceMedium", sourceMedium));
//						Bson filter =and(eq("dimension2", temp_subscriber_email),eq("sourceMedium", sourceMedium));
//						pagePathJsonArr=GetUrlsBasedOnCampaignIdAndSubscriberId(google_analytics_data_temp,page_path_filter);
//						LastSessionCountArrList = new ArrayList<Integer>();
//						AvgSesionDurationArrList = new ArrayList<Double>();
//						logger.info("Total Url Found In Campaign : "+pagePathJsonArr.size());

					Bson filter2 = null;
//						for(int i=0;i<pagePathJsonArr.size();i++){ tj
					// Creating New Document Object For URL VIEW
//							pagePath=(String) pagePathJsonArr.get(i);
//							
//							Bson host_name_filter =and(eq("pagePath", pagePath),eq("dimension2", temp_subscriber_email),eq("sourceMedium", sourceMedium));
//							hostname=GetHostNameBasedOnCampaignIdAndSubscriberId(google_analytics_data_temp,host_name_filter);
//							
//							logger.info("hostname= "+hostname);
//							if(pagePath.length()==1){
//								pagePath=hostname;
//								logger.info("pagePath= "+pagePath);
//						    }else{
//						       if(pagePath.contains("/?")){
//						    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("/?"));
//						    	   //System.out.println(path1);
//						       }else if(pagePath.contains("?")){
//						    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("?"));
//						       }else if(pagePath.contains("/")){
//						    	   pagePath=hostname+pagePath;
//						       }
//						    }

					// System.out.println("pagePath : "+pagePath);
//							logger.info("URL Found In Campaign : "+pagePath);
					timeOnPageArrList = new ArrayList<Double>();
					sessionCountArrList = new ArrayList<Integer>();
					dateHourMinuteArrList = new ArrayList<String>();
					sessionDurationBucket = new ArrayList<Integer>();
					hostNameArrList = new ArrayList<String>();
					avgTimeOnPageArrList = new ArrayList<Double>();
					// "campaign_id"
					// eq("sourceMedium", sourceMedium),eq("url", pagePath),
					// //eq("Subscriber_Email", temp_subscriber_email),
					filter2 = and(eq("Subscriber_Email", temp_subscriber_email), eq("Campaign_Id", campaign_id),
							eq("SubFunnel_Name", SubFunnel_Name), eq("Funnel_Name", Funnel_Name));
					logger.info("Total Number of Subscribers Found  : 12");
					try {
						FindIterable<Document> campaignWisePagePathFi = google_analytics_url_view_collection
								.find(filter2);
						MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
						int count = 0;
						System.out.println("campaignWisePagePath : 11");
						rule_json_object = new org.json.JSONObject();
						while (campaignWisePagePathCursor.hasNext()) {
							try {
								logger.info("Total Number of Subscribers Found  : 12333");
								System.out.println("campaignWisePagePath count:qq " + count);
								Document campaignWisePagePath = campaignWisePagePathCursor.next();
								System.out.println("campaignWisePagePath count: " + count);
								System.out.println("campaignWisePagePath : " + campaignWisePagePath);

								if (count == 0) {
									Source = campaignWisePagePath.getString("Source");
									Subscriber_Email = campaignWisePagePath.getString("Subscriber_Email");
//									campaign_id=campaignWisePagePath.getString("campaign_id");
									ga_user = campaignWisePagePath.getString("ga_user");
									hostname = campaignWisePagePath.getString("hostname");
									sourceMedium = campaignWisePagePath.getString("sourceMedium");
									url = campaignWisePagePath.getString("url");
									pagePath = campaignWisePagePath.getString("url");
									TotalTimeOnPage = campaignWisePagePath.getDouble("TotalTimeOnPage");
									NoOfUrlClicks = campaignWisePagePath.getInteger("NoOfUrlClicks");
									TotalSesionDuration = campaignWisePagePath.getInteger("TotalSesionDuration");
									SessionCount = campaignWisePagePath.getInteger("SessionCount");
									Created_By = campaignWisePagePath.getString("Created_By");
									Funnel_Name = campaignWisePagePath.getString("Funnel_Name");
									SubFunnel_Name = campaignWisePagePath.getString("SubFunnel_Name");
									Category = campaignWisePagePath.getString("Category");
									Campaign_Id = campaignWisePagePath.getString("Campaign_Id");
									List_Id = campaignWisePagePath.getString("List_Id");
									Subscriber_Id = campaignWisePagePath.getString("Subscriber_Id");
									lastclick = campaignWisePagePath.getString("lastclick");
									AvgTimeOnPage = campaignWisePagePath.getDouble("AvgTimeOnPage");
									MinTimeOnPage = campaignWisePagePath.getDouble("MinTimeOnPage");
									MaxTimeOnPage = campaignWisePagePath.getDouble("MaxTimeOnPage");
									LastSessionCount = campaignWisePagePath.getInteger("LastSessionCount");
									AvgSesionDuration = campaignWisePagePath.getDouble("AvgSesionDuration");
									MinSesionDuration = campaignWisePagePath.getInteger("MinSesionDuration");
									MaxSesionDuration = campaignWisePagePath.getInteger("MaxSesionDuration");
									firstclick = campaignWisePagePath.getString("firstclick");
									System.out.println("firstclick= " + firstclick);
									System.out.println("rule_json_object= " + rule_json_object);
									rule_json_object.put("TotalSesionDuration", TotalSesionDuration);

									rule_json_object.put("Source", Source);
									rule_json_object.put("SubscriberEmail", Subscriber_Email);
									rule_json_object.put("CampaignId", campaign_id);
									rule_json_object.put("GAUser", ga_user);
									rule_json_object.put("HostName", hostname);
									System.out.println("sourceMedium= ========" + sourceMedium);
									rule_json_object.put("SourceMedium", sourceMedium);
									// rule_json_object.put("url",url);
									rule_json_object.put("CreatedBy", Created_By);
									rule_json_object.put("FunnelName", Funnel_Name);
									rule_json_object.put("SubFunnelName", SubFunnel_Name);
									rule_json_object.put("Category", SubFunnel_Name);
									rule_json_object.put("CampaignId", Campaign_Id);
									rule_json_object.put("ListId", List_Id);
									rule_json_object.put("SubscriberId", Subscriber_Id);
									System.out.println("rule_json_object= " + rule_json_object);
									// rule_json_object.put("url",url);
									// rule_json_object.put("firstclick",firstclick);
									// rule_json_object.put("lastclick",lastclick);

									// rule_json_object.put("NoOfUrlClicks",NoOfUrlClicks);
									// rule_json_object.put("TotalSesionDuration",TotalSesionDuration);
									// rule_json_object.put("SessionCount",SessionCount);

									// rule_json_object.put("AvgTimeOnPage",AvgTimeOnPage);
									// rule_json_object.put("MinTimeOnPage",MinTimeOnPage);
									// rule_json_object.put("MaxTimeOnPage",MaxTimeOnPage);

									// rule_json_object.put("LastSessionCount",LastSessionCount);

									// rule_json_object.put("AvgSesionDuration",AvgSesionDuration);
									// rule_json_object.put("MinSesionDuration",MinSesionDuration);
									// rule_json_object.put("MaxSesionDuration",MaxSesionDuration);

								}
								AvgTimeOnPage = campaignWisePagePath.getDouble("AvgTimeOnPage");
								System.out.println("AvgTimeOnPage" + AvgTimeOnPage);
								System.out.println("After Merge pagePath---- : " + pagePath);
								rule_json_object.put(pagePath, AvgTimeOnPage);
								System.out.println("After Merge rule_json_object : " + rule_json_object);
								AvgSesionDurationArrList.add(campaignWisePagePath.getDouble("AvgSesionDuration"));
								LastSessionCountArrList.add(campaignWisePagePath.getInteger("LastSessionCount"));
								finalJsonArrayForRuleEngine.put(campaignWisePagePath.toJson());
								++count;
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("excc" + e);
							}
						}
//						}tj
					} catch (Exception e) {
						System.out.println("ee-- " + e);
					}

					Map<String, String> AvgSesionDurationMap = ArrayListOperationsForDoubleValue(
							AvgSesionDurationArrList);
					rule_json_object.put("AvgSesionDuration", AvgSesionDurationMap.get("Average"));
					// System.out.println("AvgSesionDurationMap Hi Akhilesh :
					// "+AvgSesionDurationMap.get("Average"));
					// System.out.println("AvgSesionDurationMap Hi Akhilesh :
					// "+AvgSesionDurationMap.get("Average"));
					Map<String, String> LastSessionCountMap = ArrayListOperationsForIntegerValue(
							LastSessionCountArrList);
					rule_json_object.put("SessionCount", LastSessionCountMap.get("Max"));
					// System.out.println("Before Merge rule_json_object : "+rule_json_object);
//					    org.json.JSONObject recent_gadata_json_obj=fetchRecentGADataForRuleEngine("google_analytics_recent_data_temp",sourceMedium,Subscriber_Email);       
//					    org.json.JSONObject most_recent_gadata_json_obj=fetchMostRecentGADataForRuleEngine("google_analytics_recent_data_temp",sourceMedium,Subscriber_Email);
//					    rule_json_object=mergeJSONObject(rule_json_object,mergeJSONObject(recent_gadata_json_obj,most_recent_gadata_json_obj));
					System.out.println("After Merge rule_json_object : " + rule_json_object);
					logger.info("Call  rule_json_object as INPUT : " + rule_json_object
							+ "      ::::::: Created_By ::::" + Created_By + ":: Funnel_Name :: " + Funnel_Name);

					finajsa.add(rule_json_object);
					logger.info("Call  rule_json_object as finajsa : " + finajsa + "      ::::::: Created_By ::::"
							+ Created_By + ":: Funnel_Name :: " + Funnel_Name);

//					    String free_trail_status=null;
//					    
//					    free_trail_status=new FreeTrialandCart().checkFreeTrialExpirationStatus(Created_By.replace("_", "@"));
					// System.out.println(campaign_details_doc);
					// callrule engine and fire rule
					// call shopping cart method

//					  String validuserresp = CheckValidUserforFreetrialAndCart.checkValiditytrialCart(Created_By);
////					    logger.info("free_trail_status = "+free_trail_status);
////					    if(free_trail_status.equals("0")){
//					  JSONParser parser = new JSONParser();
//					  JSONObject validjs = (JSONObject) parser.parse(validuserresp);
//					  logger.info("validjs = "+validjs);
//						if(validjs.containsKey("status")&& validjs.get("status").equals("true")) {
//					    	try {
//					    		logger.info("callRuleEngine : "+Funnel_Name);
//					    	callRuleEngine(rule_json_object.toString(),Funnel_Name);
//					    	}catch (Exception e) {
//					    		logger.info("exc in callRuleEngine"+e);
//							}
//					    }else{
//					    	System.out.println("Freetrail Expired for User : "+Created_By.replace("_", "@"));
//					    	logger.info("Freetrail Expired for User : "+Created_By.replace("_", "@"));
//					    }
					// break;
//					}tj
					finaljson.put("ActiveUsers", finajsa);
				} finally {
//					sourceMediumCursor.close();
				}

				// System.out.println("finalJsonArrayForRuleEngine :
				// "+finalJsonArrayForRuleEngine);

				// break;
			} // tj
		} catch (Exception e) {
			logger.info("exc : " + e);
			e.printStackTrace();
			// throw new RuntimeException(e);

			// System.out.println("Exception : "+e.getMessage());
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);
		}
		return finaljson;
	}

	public static String insertruleDataforMonitoring(String rulejssave, String SubscriberEmail, String subfunnel,
			String Funnel_Name, String campaign_id, String createdby) {
		String mainfunnel = null;
		// insert analytics data to RuleEngineCalledForSubscriberData
		MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		Bson document = null;
		try {

			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();
			String uri = "mongodb://localhost:27017/?ssl=true";
			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");

			collection = database.getCollection("RuleEngineCalledForSubscriberData");
//			 Document document =null;
			logger.info("RuleEngineCalledForSubscriberData  insertruleDataforMonitoring rulejssave : " + rulejssave);
			// Document doc = Document.parse( jsonlist.toString() );
			document = Document.parse(rulejssave);

	
			logger.info("rulejssave= " + rulejssave);
			if (Funnel_Name.contains("_EC_") || Funnel_Name.contains("_EnC_") || Funnel_Name.contains("_IC_")
					|| Funnel_Name.contains("_WC_") || Funnel_Name.contains("_CC_")) {
				mainfunnel = Funnel_Name.substring(0, Funnel_Name.lastIndexOf("_"));
				mainfunnel = Funnel_Name.substring(0, mainfunnel.lastIndexOf("_"));

			} else {
				mainfunnel = Funnel_Name;
			}
			Bson searchQuery = new Document().append("SubscriberEmail", SubscriberEmail).append("Category", subfunnel)
					.append("FunnelName", mainfunnel).append("Campaign_id", campaign_id)
					.append("Created_By", createdby);

			collection.updateOne(searchQuery, new Document("$set", document), new UpdateOptions().upsert(true));
			logger.info("inserted");
		} catch (Exception e) {
			logger.info("exc insertruleDataforMonitoring : " + e);
		} finally {
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return "inserted";
	}

	public static MongoClient getConnection() {

		try {

			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			String uri = "mongodb://localhost:27017/?ssl=true";

			MongoClientURI connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			System.out.println("Connecting to MongoDB  Server new version.......  " + mongoClient.getAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mongoClient;
	}

	public static String updateDataSourceforMoveSubscriber(String subscriberemail, String CreatedBy, String funnelName,
			String category, String campaign_id, String group, String original_category) {

		// update othercategory collectionfor moved subscriber
		String[] monthArray = { "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };

		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> firstcatcollection = null;
		Bson document = null;
		JSONObject subscriberemailjs = new JSONObject();
		String campaignid = null;
		int schedulegap = 1;
		Date set_date = null;
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date dateobj = new Date();
		String Current_Date = df.format(dateobj);
		try {
			set_date = df.parse(Current_Date);
			// SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yy");
			// formatter2.format(dateobj);
		} catch (ParseException e) {
			// TODO Auto-generated catch block

		}
		set_date.setDate(set_date.getDate() + schedulegap);
		String formatdate = df.format(set_date);

		try {
			Iterator datasrcitr = null;
			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			org.json.JSONArray subscriberarr = new org.json.JSONArray();
			JSONObject emailjson = new JSONObject();
			emailjson.put("email", subscriberemail);
			subscriberarr.put(emailjson);
			subscriberemailjs.put("Datasource", subscriberarr);
			subscriberemailjs.put("CreatedBy", CreatedBy);

			collection = database.getCollection("OtherCategoryMails");
			firstcatcollection = database.getCollection("FirstCategoryMails");
			// document = Document.parse(subscriberemailjs.toString());
			Document updateQuery = new Document();
			// Document emaillist = new Document().append("email", subscriberemail);
			Bson filter1 = null;
			Bson filter2 = and(eq("CreatedBy", CreatedBy), eq("funnelName", funnelName), eq("Category", category),
					eq("group", group));
			Bson filter3 = and(eq("CreatedBy", CreatedBy), eq("funnelName", funnelName),
					eq("Category", original_category), eq("group", group));
			logger.info("updateDataSourceforMoveSubscriber   : ");
			FindIterable<Document> filerdata = firstcatcollection.find(filter2);
			MongoCursor<Document> monitordata = filerdata.iterator();

			Document datsrc_doc = null;
			String mainfunnel = null;
			while (monitordata.hasNext()) {

				Document categoryWisedata = monitordata.next();
				logger.info("categoryWisedata   : " + categoryWisedata);
				try {

					org.json.JSONObject data_json_obj = null;
					campaignid = categoryWisedata.get("Campaign_id").toString();

					if (funnelName.contains("_EC_") || funnelName.contains("_EnC_") || funnelName.contains("_IC_")
							|| funnelName.contains("_WC_") || funnelName.contains("_CC_")) {
						mainfunnel = funnelName.substring(0, funnelName.lastIndexOf("_"));
						mainfunnel = funnelName.substring(0, mainfunnel.lastIndexOf("_"));

					} else {
						mainfunnel = funnelName;
					}
					List<Document> datasourcesarr = getContactarrofstartcatg("Explore", "SentExploreContacts",
							CreatedBy, mainfunnel); // old
					org.json.JSONArray dataarr = new org.json.JSONArray();
					boolean moveOut = false;
					datasrcitr = datasourcesarr.listIterator();
					while (datasrcitr.hasNext()) {
						datsrc_doc = (Document) datasrcitr.next();
						logger.info("datsrc_doc.toJson()   : " + datsrc_doc.toJson());
						data_json_obj = new org.json.JSONObject(datsrc_doc.toJson());

						if (data_json_obj.has("Email")
								&& subscriberemail.equalsIgnoreCase(data_json_obj.getString("Email"))) {
							logger.info("value   : " + data_json_obj);
							dataarr.put(data_json_obj);
							moveOut = true;
							break;

						}

//						Set<?> s = data_json_obj.keySet();
//
//						Iterator<?> i = s.iterator();
//						while (i.hasNext()) {
//							String key = i.next().toString();
//							logger.info("key   : " + key + " :: subscriberemail=" + subscriberemail);
//							String value = data_json_obj.getString(key);
//							if (subscriberemail.equalsIgnoreCase(value)) {
//								logger.info("value   : " + value);
//								dataarr.put(data_json_obj);
//								moveOut = true;
//								break;
//
//							}
//						}

						if (moveOut) {

							filter1 = and(eq("CreatedBy", CreatedBy), eq("Campaign_id", campaignid),
									eq("funnelName", funnelName), eq("Category", category), eq("group", group));
							logger.info("moveOut    : " + data_json_obj);
							Document doc = Document.parse(data_json_obj.toString());
							// Document emaillist = new Document(data_json_obj.toString());
							collection.updateOne(filter1, Updates.addToSet("Contacts", doc),
									new UpdateOptions().upsert(true));

							JSONObject email_json_obj = new JSONObject();
							email_json_obj.put("Email", subscriberemail);
							BasicDBObject dbObject = (BasicDBObject) JSON.parse(email_json_obj.toString());
							// out.println("dbObject ="+dbObject);

							BasicDBObject updateQuery3 = new BasicDBObject("$addToSet",
									new BasicDBObject("MoveEmailList", dbObject));
							firstcatcollection.updateOne(filter3, updateQuery3, new UpdateOptions().upsert(true));

							Document emaillist = new Document().append("email", subscriberemail);
//							firstcatcollection.updateOne(filter2, Updates.addToSet("MoveEmailList", emaillist),
//									new UpdateOptions().upsert(true));

							Bson filter4 = and(eq("CreatedBy", CreatedBy), eq("funnelName", mainfunnel),
									eq("Category", category), eq("group", group));

							Bson filtermonth = and(eq("CreatedBy", "Explore"), eq("funnelName", mainfunnel),
									eq("Category", category), eq("group", group));

							firstcatcollection.updateOne(filter4, Updates.addToSet("SentEmailList", emaillist),
									new UpdateOptions().upsert(true));

							Document camp_details_doc = new Document();

							camp_details_doc.put("ActivateDate", formatdate);

							Date date1 = df.parse(Current_Date);

							Calendar calendar = Calendar.getInstance();

							calendar.setTime(date1);
							int monthno = calendar.get(Calendar.WEEK_OF_MONTH);

							// System.out.println("month : "+monthArray[monthno]);

							String Week = GetWeekFromDate(Current_Date);
							String monthofactivedate = monthArray[monthno];
							BasicDBObject updateQuerymonth = new BasicDBObject("$addToSet",
									new BasicDBObject(monthofactivedate, dbObject));
							if (!category.equals("Connect")) {
								firstcatcollection.updateOne(filtermonth, updateQuerymonth,
										new UpdateOptions().upsert(true));
							} else {
								BasicDBObject connmonth = new BasicDBObject("$addToSet",
										new BasicDBObject("connect" + monthofactivedate, dbObject));
								firstcatcollection.updateOne(filtermonth, connmonth, new UpdateOptions().upsert(true));
							}

							BasicDBObject updateQueryweek = new BasicDBObject("$addToSet",
									new BasicDBObject(Week + "leadcount", dbObject));
							firstcatcollection.updateOne(filter4, updateQueryweek, new UpdateOptions().upsert(true));

							firstcatcollection.updateOne(filter2, new Document("$set", camp_details_doc),
									new UpdateOptions().upsert(true));
							BasicDBObject updateQuery4 = new BasicDBObject("$addToSet",
									new BasicDBObject("campaignleadEmailList", dbObject));
							firstcatcollection.updateOne(filter2, updateQuery4, new UpdateOptions().upsert(true));

							break;
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
//					out.println("exc= " + e);
				}
			}

			/*
			 * collection.updateOne(filter1, Updates.addToSet("Subscriberlist", emaillist),
			 * new UpdateOptions().upsert(true));
			 */

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return "success";
	}

	public static String getOldcategoryName(String category) {
		String oldcategory = null;
		HashMap<String, String> category_mapnew = new HashMap<String, String>();
		category_mapnew.put("Unknown", "Explore");
		category_mapnew.put("Cold", "Entice");
		category_mapnew.put("Warm", "Inform");
		category_mapnew.put("Hot", "Warm");
		category_mapnew.put("Connect", "Connect");

		if (category_mapnew.containsKey(category)) {
			oldcategory = category_mapnew.get(category);
			// System.out.println("value for key "+category+" is : " + position);
			logger.info("value for key " + category + " is oldcategory : " + oldcategory);
		}

		return oldcategory;
	}

	public static List<Document> getContactarrofstartcatg(String category, String arrayname, String CreatedBy,
			String mainfunnel) throws IOException {
		// update subFunnelCampCount in main funnel

		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		String schedulegap = null;
		MongoCursor<Document> monitordata = null;

		String scheduletime = null;
		List<Document> contactarr = null;
		try {

			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
			if (mainfunnel != null) {
				Bson updtfiltercount = and(eq("Category", category), eq("funnelName", mainfunnel),
						eq("CreatedBy", CreatedBy));

				FindIterable<Document> filerdata = collection.find(updtfiltercount);
				monitordata = filerdata.iterator();

				Document datsrc_doc = null;

				if (mainfunnel != null && mainfunnel != "") {
					while (monitordata.hasNext()) {
						// out.println("in newsubfuncount =");
						datsrc_doc = monitordata.next();
						contactarr = (List<Document>) datsrc_doc.get(arrayname);
						break;
					}
				}

//		Document subfunnelcountdoc = new Document();
//		subfunnelcountdoc.put("subFunnelCampCount", newsubfuncount);
//		 out.println("subfunnelcountdoc ="+subfunnelcountdoc);
//		collection.updateOne(updtfiltercount, new Document("$set", subfunnelcountdoc), new UpdateOptions().upsert(true));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return contactarr;
	}

	private static String GetWeekFromDate(String date) {
		String Week = null;
		try {
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date date1 = df.parse(date);

			Calendar calendar = Calendar.getInstance();

			calendar.setTime(date1);
			int weekno = calendar.get(Calendar.WEEK_OF_MONTH);
			Week = "week" + Integer.toString(weekno);
		} catch (Exception e) {

			// TODO Auto-generated catch block
			System.out.println(e);
		}
		return Week;
	}

	public static String updateMoveCategory(String SubscriberEmail, String movetocategory, String Funnel_Name,
			String campaign_id, String createdby, String originalcateg) {
		
		// insert analytics data to RuleEngineCalledForSubscriberData
		MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;

		try {

			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();
			String uri = "mongodb://localhost:27017/?ssl=true";
			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");

			collection = database.getCollection("RuleEngineCalledForSubscriberData");

			Document categ_doc = new Document();

			categ_doc.put("MoveToCategory", movetocategory);
			Bson searchQuery = new Document().append("SubscriberEmail", SubscriberEmail)
					.append("Category", originalcateg).append("FunnelName", Funnel_Name)
					.append("Campaign_id", campaign_id).append("Created_By", createdby);

			collection.updateOne(searchQuery, new Document("$set", categ_doc), new UpdateOptions().upsert(true));
			logger.info("move cat inserted");
		} catch (Exception e) {
			logger.info("exc insertruleDataforMonitoring : " + e);
		} finally {
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return "move";
	}

}
