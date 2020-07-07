package com.db.mongo.ga;

import com.DashboardSales.BizUtil;
import com.leadconverter.freetrail.CheckValidUserforFreetrialAndCart;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClientURI;
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
import com.mongodb.util.JSON;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.Base64.Decoder;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class GAMongoDAO {
	static final Logger logger = Logger.getLogger(GAMongoDAO.class);

	public static void main(String[] args) throws Exception {
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
		TreeSet<String> UniqueSubscribers = new TreeSet();
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
		double AvgTimeOnPage =0;
		double MinTimeOnPage = 0;
		double MaxTimeOnPage = 0;
		int LastSessionCount = 0;
		double AvgSesionDuration = 0;
		int MinSesionDuration = 0;
		int MaxSesionDuration = 0;
		String firstclick = null;
		String formatedDate = null;
		ArrayList<Integer> LastSessionCountArrList = null;
		ArrayList<Double> AvgSesionDurationArrList = null;
		logger.info(
				"Calling fetchGADataForRuleEngine : +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=");
		logger.info("Calling fetchGADataForRuleEngine : ");
		MongoDatabase database = null;
		MongoCollection<Document> google_analytics_data_temp = null;
		JSONObject rulejssave = null;
		new JSONArray();
		JSONObject rule_json_object = null;
		int totalclicks = 0;
		org.json.JSONArray rulengarr = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			google_analytics_data_temp = database.getCollection(coll_name);
			google_analytics_url_view_collection = database.getCollection("google_analytics_url_view_collection");
			DistinctIterable<String> dimension2Di = google_analytics_data_temp.distinct("dimension2", String.class);
			MongoCursor dimension2Cursor = dimension2Di.iterator();

			try {
				while (dimension2Cursor.hasNext()) {
					subscriber_email = ((String) dimension2Cursor.next()).toString();
					if (subscriber_email.contains("@")) {
						UniqueSubscribers.add(subscriber_email);
					}
				}
			} finally {
				dimension2Cursor.close();
			}

			logger.info(UniqueSubscribers.size() + "Total Number of Subscribers Found  : " + UniqueSubscribers);
			if (UniqueSubscribers.size() > 0) {
				Iterator var61 = UniqueSubscribers.iterator();

				while (var61.hasNext()) {
					String temp_subscriber_email = (String) var61.next();
					logger.info("Subscriber Email : " + temp_subscriber_email);
					Bson filter1 = Filters.and(new Bson[] { Filters.eq("dimension2", temp_subscriber_email) });
					DistinctIterable<String> sourceMediumDi = google_analytics_data_temp.distinct("sourceMedium",
							filter1, String.class);
					MongoCursor sourceMediumCursor = sourceMediumDi.iterator();

					try {
						while (sourceMediumCursor.hasNext()) {
							sourceMedium = ((String) sourceMediumCursor.next()).toString();
							logger.info("sourceMedium  : " + sourceMedium);
							if (sourceMedium.contains("email")) {
								campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1)
										.replace("email", "").trim();
								source = "Email";
							} else if (sourceMedium.contains("(direct)")) {
								source = "Direct";
								campaign_id = "NULL";
							} else if (sourceMedium.contains("(not set)")) {
								source = "not set";
								campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1)
										.replace("not set", "").trim();
								logger.info("source=direct ;" + source + " :: campaign_id= " + campaign_id);
							}

							campaign_id = campaign_id.trim();
							logger.info("Campaign Source For Subscriber : " + sourceMedium);
							logger.info("campaign_id For Subscriber : " + campaign_id);
							rule_json_object = new JSONObject();
							rulejssave = new JSONObject();
							Bson filter2 = null;
							logger.info("Campaign Source For Subscriber : " + sourceMedium);
							Bson page_path_filter = Filters
									.and(new Bson[] { Filters.eq("dimension2", temp_subscriber_email),
											Filters.eq("sourceMedium", sourceMedium) });
							Bson filter = Filters.and(new Bson[] { Filters.eq("dimension2", temp_subscriber_email),
									Filters.eq("sourceMedium", sourceMedium) });
							pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(google_analytics_data_temp,
									page_path_filter);
							logger.info("Campaign Source For pagePathJsonArr ::: : " + pagePathJsonArr);
							LastSessionCountArrList = new ArrayList();
							AvgSesionDurationArrList = new ArrayList();
							logger.info("Total Url Found In Campaign : " + pagePathJsonArr.size());
							JSONArray urlarr = new JSONArray();

							org.json.simple.JSONObject urljs;
							for (int i = 0; i < pagePathJsonArr.size(); ++i) {
								pagePath = (String) pagePathJsonArr.get(i);
								// https://www.youtube.com/watch?v=m4F1K3UypvU&feature=youtu.be
								logger.info("pagePath== " + pagePath);

								Bson host_name_filter = Filters.and(new Bson[] { Filters.eq("pagePath", pagePath),
										Filters.eq("dimension2", temp_subscriber_email),
										Filters.eq("sourceMedium", sourceMedium) });
								hostname = GetHostNameBasedOnCampaignIdAndSubscriberId(google_analytics_data_temp,
										host_name_filter);
								logger.info("hostname= " + hostname);
								if (pagePath.length() == 1) {
									pagePath = hostname;
									logger.info("pagePath= " + hostname);
								}

								else if (pagePath.contains("yout")) {

									pagePath = pagePath;

								}else if (pagePath.contains("MailOpen")) {
									pagePath = pagePath;
									rule_json_object.put("Open", "Open");
								} else if (pagePath.contains("/?")) {
									pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
								} else if (pagePath.contains("?")) {
									pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
								} else if (pagePath.contains("/")) {
									pagePath = hostname + pagePath;
								} else {
								}

								logger.info("URL Found In Campaign : " + pagePath);
								new ArrayList();
								new ArrayList();
								new ArrayList();
								new ArrayList();
								new ArrayList();
								new ArrayList();
								filter2 = Filters.and(new Bson[] { Filters.eq("sourceMedium", sourceMedium),
										Filters.eq("url", pagePath),
										Filters.eq("Subscriber_Email", temp_subscriber_email) });
								FindIterable<Document> campaignWisePagePathFi = google_analytics_url_view_collection
										.find(filter2);
								MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();

								for (int count = 0; campaignWisePagePathCursor.hasNext(); ++count) {
									 TotalSesionDuration = 0;
									Document campaignWisePagePath = (Document) campaignWisePagePathCursor.next();
									if (count == 0) {
										try {
											Source = campaignWisePagePath.getString("Source");
										} catch (Exception e) {
											// TODO: handle exception
										}

										Subscriber_Email = campaignWisePagePath.getString("Subscriber_Email");
										campaign_id = campaignWisePagePath.getString("Campaign_id");
										ga_user = campaignWisePagePath.getString("ga_user");
										hostname = campaignWisePagePath.getString("hostname");
										location = campaignWisePagePath.getString("location");
										sourceMedium = campaignWisePagePath.getString("sourceMedium");
										url = campaignWisePagePath.getString("url");
										try {
											TotalTimeOnPage = campaignWisePagePath.getDouble("TotalTimeOnPage");
										} catch (Exception e) {
											// TODO: handle exception
										}
										try {
											NoOfUrlClicks = campaignWisePagePath.getInteger("NoOfUrlClicks");
										} catch (Exception e) {
											// TODO: handle exception
										}
										try {
											 TotalSesionDuration = 0;
											TotalSesionDuration = campaignWisePagePath
													.getInteger("TotalSesionDuration");
										} catch (Exception e) {
											// TODO: handle exception
										}
										try {
											SessionCount = campaignWisePagePath.getInteger("SessionCount");
										} catch (Exception e) {
											// TODO: handle exception
										}
										Created_By = campaignWisePagePath.getString("Created_By");
										Funnel_Name = campaignWisePagePath.getString("Funnel_Name");
										Parentfunnel = campaignWisePagePath.getString("Parentfunnel");
										group = campaignWisePagePath.getString("group");
										logger.info("Funnel_Name = " + Funnel_Name);
										Category = campaignWisePagePath.getString("Category");
										try {
											lastclick = campaignWisePagePath.getString("lastclick");
										} catch (Exception e) {
											// TODO: handle exception
										}
										try {
											AvgTimeOnPage = campaignWisePagePath.getDouble("AvgTimeOnPage");
										} catch (Exception e) {
											// TODO: handle exception
										}
										try {
											MinTimeOnPage = campaignWisePagePath.getDouble("MinTimeOnPage");
										} catch (Exception e) {
											// TODO: handle exception
										}
										try {
											MaxTimeOnPage = campaignWisePagePath.getDouble("MaxTimeOnPage");
										} catch (Exception e) {
											// TODO: handle exception
										}
										try {
											LastSessionCount = campaignWisePagePath.getInteger("LastSessionCount");
										} catch (Exception e) {
											// TODO: handle exception
										}
										try {
											AvgSesionDuration = campaignWisePagePath.getDouble("AvgSesionDuration");
										} catch (Exception e) {
											// TODO: handle exception
										}
										try {
											MinSesionDuration = campaignWisePagePath.getInteger("MinSesionDuration");
										} catch (Exception e) {
											// TODO: handle exception
										}
										try {
											MaxSesionDuration = campaignWisePagePath.getInteger("MaxSesionDuration");
										} catch (Exception e) {
											// TODO: handle exception
										}
										try {
											firstclick = campaignWisePagePath.getString("firstclick");
										} catch (Exception e) {
											// TODO: handle exception
										}
										rule_json_object.put("TotalSesionDuration", TotalSesionDuration);
										rule_json_object.put("TotalTimeOnPage", TotalTimeOnPage);
										rule_json_object.put("Source", Source);
										rule_json_object.put("SubscriberEmail", Subscriber_Email);
										rule_json_object.put("CampaignId", campaign_id);
										rule_json_object.put("Campaign_id", campaign_id);
										rule_json_object.put("GAUser", ga_user);
										rule_json_object.put("HostName", hostname);
										rule_json_object.put("SourceMedium", sourceMedium);
										rule_json_object.put("CreatedBy", Created_By);
										rule_json_object.put("FunnelName", Parentfunnel);
										rule_json_object.put("ChildFunnelName", Funnel_Name);
										rule_json_object.put("SubFunnelName", Category);
										rule_json_object.put("Category", Category);
										rule_json_object.put("group", group);
										logger.info(
												"NoOfUrlClicks= " + NoOfUrlClicks + " :: totalclicks = " + totalclicks+" :: TotalSesionDuration= "+TotalSesionDuration);
										totalclicks += NoOfUrlClicks;
										logger.info("hostname= " + NoOfUrlClicks);
										rulejssave.put("TotalSesionDuration", TotalSesionDuration);
										rulejssave.put("TotalTimeOnPage", TotalTimeOnPage);
										rulejssave.put("Source", Source);
										rulejssave.put("SubscriberEmail", Subscriber_Email);
										rulejssave.put("location", location);
										rulejssave.put("CampaignId", campaign_id);
										rulejssave.put("GAUser", ga_user);
										rulejssave.put("HostName", hostname);
										rulejssave.put("SourceMedium", sourceMedium);
										rulejssave.put("CreatedBy", Created_By);
										rulejssave.put("Created_By", Created_By);
										rulejssave.put("FunnelName", Parentfunnel);
										rulejssave.put("ChildFunnelName", Funnel_Name);
										rulejssave.put("SubFunnelName", Category);
										rulejssave.put("Category", Category);
										rulejssave.put("group", group);
										if (pagePath.equals(hostname)) {
											try {
											SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddHHmm");
											Date date = originalFormat.parse(firstclick);
											SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
											formatedDate = newFormat.format(date);
											rulejssave.put("dateHourMinute", formatedDate);
											}catch (Exception e) {
												// TODO: handle exception
											}
										
										}

										urljs = new org.json.simple.JSONObject();
										if(!pagePath.equals("MailOpen")) {
										urljs.put("url", pagePath);
										}
										try {
										urljs.put("AvgTimeOnPage", campaignWisePagePath.getDouble("AvgTimeOnPage"));
										}catch (Exception e) {
											// TODO: handle exception
										}
										System.out.println("URL Found In monitoring urljs: " + urljs);
										logger.info("URL Found In monitoring urljs: " + urljs);
										urlarr.add(urljs);
										System.out.println("URL Found In monitoring urlarr: " + urlarr);
										logger.info("URL Found In monitoring urlarr : " + urlarr);
									}

									AvgTimeOnPage = campaignWisePagePath.getDouble("AvgTimeOnPage");
									
									rule_json_object.put(pagePath, AvgTimeOnPage);
									logger.info("URL Found In monitoring : " + rulejssave);
									try {
									AvgSesionDurationArrList.add(campaignWisePagePath.getDouble("AvgSesionDuration"));
									}catch (Exception e) {
										// TODO: handle exception
									}try {
									LastSessionCountArrList.add(campaignWisePagePath.getInteger("LastSessionCount"));
									finalJsonArrayForRuleEngine.put(campaignWisePagePath.toJson());
									}catch (Exception e) {
										// TODO: handle exception
									}
									
								}

							}
							logger.info("URL Found In rule_json_object : " + rule_json_object);
							try {
							rulejssave.put("NoOfUrlClicks", totalclicks);
							
							rulejssave.put("PageUrls", urlarr);
							Map<String, String> AvgSesionDurationMap = ArrayListOperationsForDoubleValue(
									AvgSesionDurationArrList);
							rule_json_object.put("AvgSesionDuration", AvgSesionDurationMap.get("Average"));
							rulejssave.put("AvgSesionDuration", AvgSesionDurationMap.get("Average"));
							Map<String, String> LastSessionCountMap = ArrayListOperationsForIntegerValue(
									LastSessionCountArrList);
							rule_json_object.put("SessionCount", LastSessionCountMap.get("Max"));
							rulejssave.put("SessionCount", LastSessionCountMap.get("Max"));
							rulejssave.put("AvgSesionDuration", AvgSesionDurationMap.get("Average"));
							JSONObject recent_gadata_json_obj = fetchRecentGADataForRuleEngine(
									"google_analytics_recent_data_temp", sourceMedium, temp_subscriber_email);
							rulejssave.put("Recent_SessionCount", recent_gadata_json_obj.get("Recent_SessionCount"));
							rulejssave.put("Recent_AvgSesionDuration",
									recent_gadata_json_obj.get("Recent_AvgSesionDuration"));
							JSONObject most_recent_gadata_json_obj = fetchMostRecentGADataForRuleEngine(
									"google_analytics_recent_data_temp", sourceMedium, temp_subscriber_email);
							rule_json_object = mergeJSONObject(rule_json_object,
									mergeJSONObject(recent_gadata_json_obj, most_recent_gadata_json_obj));
							rulejssave.put("MostRecent_AvgSesionDuration",
									rule_json_object.getString("MostRecent_AvgSesionDuration"));
							}catch (Exception e) {
								// TODO: handle exception
								logger.info("excpt: " + e.getMessage());
							}
							logger.info("URL Found In monitoring urlarr rulejssave: " + rulejssave);
						//	funnelListJsonArr.add(rulejssave);
							logger.info("Call  rule_json_object as INPUT : " + rule_json_object
									+ "      ::::::: Created_By ::::" + Created_By + ":: Funnel_Name :: "
									+ Parentfunnel);
							logger.info("inserted category" + Created_By + Parentfunnel);

							try {
								if (!BizUtil.isNullString(Created_By) && !BizUtil.isNullString(Parentfunnel) && !Created_By.equals("NA")) {
									String validuserresp = CheckValidUserforFreetrialAndCart
											.checkValiditytrialCart(Created_By);
									JSONParser parser = new JSONParser();
									urljs = (org.json.simple.JSONObject) parser.parse(validuserresp);
									logger.info("validjs = " + urljs);
									if (urljs.containsKey("status") && urljs.get("status").equals("true")) {
										logger.info("insertruleDataforMonitoring inserting = ");
//										if(rulejssave.has("HostName") && !rulejssave.getString("HostName").equals("MailOpen")) {
										insertruleDataforMonitoring(rulejssave.toString(), Subscriber_Email, Category,
												Funnel_Name, campaign_id, Created_By);
//										}

										String ruleeng;
										try {
											org.json.simple.JSONObject exparminput = new org.json.simple.JSONObject();
											exparminput.put("CreatedBy", Created_By);
											exparminput.put("funnelName", Parentfunnel);
											exparminput.put("subscribeId", Subscriber_Email.trim());
											ruleeng = ResourceBundle.getBundle("config")
													.getString("callextraparameter");
											logger.info(ruleeng + " exparminput = " + exparminput);
											String response = urlconnect(ruleeng, exparminput.toJSONString());
											System.out.println("response = " + response);
											logger.info("response = " + response);
											if (response != "" && response != null) {
												JSONObject jobj = new JSONObject(response);
												Iterator keys = jobj.keys();

												while (keys.hasNext()) {
													String key = "";
													String value = "";
													key = (String) keys.next();
													System.out.println("key " + key + " value " + value);
													value = jobj.getString(key);
													logger.info("key " + key + " value " + value);
													rule_json_object.put(key, value);
												}
											}

											logger.info("rule_json_object = " + rule_json_object);
										} catch (Exception var107) {
											var107.printStackTrace();
										}

										try {
											logger.info(
													"Calling fetchGADataForRuleEngine : --------------------------------");
											rulengarr = (new CallGetService()).getRuleEngines(Created_By,
													Parentfunnel.replace(" ", "_"));
											logger.info(
													"callRuleEngine : " + Parentfunnel + " ::rulengarr =" + rulengarr);
											if (rulengarr.length() > 0 && rulengarr != null) {
												for (int j = 0; j < rulengarr.length(); ++j) {
													ruleeng = (String) rulengarr.get(j);
													if (!ruleeng.isEmpty() && ruleeng != null) {
														logger.info("ruleeng : " + ruleeng);
														callRuleEngine(google_analytics_data_temp,rule_json_object.toString(),
																Parentfunnel.replace(" ", "_"), ruleeng, Created_By,
																rulejssave.toString());
													}
												}
											}
										} catch (Exception var106) {
											logger.info("exc in callRuleEngine" + var106);
										}
									} else {
										System.out.println(
												"Freetrail Expired for User : " + Created_By.replace("_", "@"));
										logger.info("Freetrail Expired for User : " + Created_By.replace("_", "@"));
									}
								} else {
									logger.info("funnel null ");
								}
							} catch (Exception var108) {
								logger.info("exc in validity" + var108);
							}
						}
					} finally {
						sourceMediumCursor.close();
					}
				}
			}
//			google_analytics_data_temp.deleteMany(new Document()); // commented by tejal
			logger.info("google_analytics_data_temp removed  : ");
		} catch (Exception var111) {
			logger.info("gException : " + var111);
			var111.printStackTrace();
			System.out.println("Exception : " + var111.getMessage());
		} finally {
			google_analytics_data_temp = null;
			google_analytics_url_view_collection = null;
			database = null;
		}

		return funnelListJsonArr;
	}

	private void retrieveSubscriberList(HashMap<String, org.json.JSONArray> templateMp2) throws JSONException {
		System.out.println("Enter Map iterator");
		Iterator var3 = templateMp2.entrySet().iterator();

		while (var3.hasNext()) {
			Entry mapElement = (Entry) var3.next();
			String key = (String) mapElement.getKey();
			org.json.JSONArray value = (org.json.JSONArray) mapElement.getValue();
			List<String> list = new ArrayList();

			for (int i = 0; i < value.length(); ++i) {
				value.getJSONObject(i);
				list.add("");
			}
		}

	}

	public static org.json.JSONArray fetchGADataForRuleEngine(Document search_query, Document url_view_set_doc,
			Document url_view_inc_doc, Document url_view_set_on_insert_doc) {
		org.json.JSONArray finalJsonArrayForRuleEngine = null;
		JSONObject rule_json_object = null;
		System.out.println("fetchGADataForRuleEngine Mehtod is called : ");

		try {
			finalJsonArrayForRuleEngine = new org.json.JSONArray();
			new JSONObject();
			System.out.println("search_query : " + search_query.toJson());
			System.out.println("url_view_set_doc : " + url_view_set_doc.toJson());
			System.out.println("url_view_inc_doc : " + url_view_inc_doc.toJson());
			System.out.println("url_view_set_on_insert_doc : " + url_view_set_on_insert_doc.toJson());
		} catch (Exception var10) {
			System.out.println("Exception : " + var10.getMessage());
		} finally {
			System.out.println("Finally Excecuted Successfully");
		}

		return finalJsonArrayForRuleEngine;
	}

	public static JSONObject fetchRecentGADataForRuleEngine(String coll_name, String sourceMedium,
			String temp_subscriber_email) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		JSONArray pagePathJsonArr = null;
		new JSONArray();
		String pagePath = null;
		String hostname = null;
		ArrayList<Double> timeOnPageArrList = null;
		ArrayList<Integer> sessionCountArrList = null;
		ArrayList<String> dateHourMinuteArrList = null;
		ArrayList<Integer> sessionDurationBucket = null;
		ArrayList<String> hostNameArrList = null;
		new TreeSet();
		org.json.JSONArray finalJsonArrayForRuleEngine = new org.json.JSONArray();
		JSONObject rule_json_object = null;
		String campaign_id = null;
		String ga_user = null;
		String source = null;
		Map<String, String> campaignDetailsMap = null;
		int int_recent_days = 0;
		Date recent_date = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String recent_days_1 = ResourceBundle.getBundle("config").getString("recent_days");
			int_recent_days = Integer.parseInt(recent_days_1);
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

			rule_json_object = new JSONObject();
			Bson filter2 = null;
			System.out.println("-----------Unique sourceMedium : " + sourceMedium
					+ "  : sourceMedium Unique-------------------------");
			Bson page_path_filter = Filters.and(new Bson[] { Filters.gt("dateHourMinuteInDateFormat", recent_date),
					Filters.eq("dimension2", temp_subscriber_email), Filters.eq("sourceMedium", sourceMedium) });
			Bson filter = Filters.and(new Bson[] { Filters.gt("dateHourMinuteInDateFormat", recent_date),
					Filters.eq("sourceMedium", sourceMedium), Filters.eq("dimension2", temp_subscriber_email) });
			pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(collection, page_path_filter);
			if (pagePathJsonArr.size() <= 0) {
				rule_json_object.put("Recent_" + pagePath, "No most recent url found");
				rule_json_object.put("Recent_SessionCount", "0");
				rule_json_object.put("Recent_AvgSesionDuration", "0");
			} else {
				int i = 0;

				while (true) {
					if (i >= pagePathJsonArr.size()) {
						finalJsonArrayForRuleEngine.put(rule_json_object);
						break;
					}

					pagePath = (String) pagePathJsonArr.get(i);
					System.out.println("pagePath : " + pagePath);
					timeOnPageArrList = new ArrayList();
					sessionCountArrList = new ArrayList();
					dateHourMinuteArrList = new ArrayList();
					sessionDurationBucket = new ArrayList();
					hostNameArrList = new ArrayList();
					filter2 = Filters.and(new Bson[] { Filters.eq("sourceMedium", sourceMedium),
							Filters.eq("pagePath", pagePath), Filters.eq("dimension2", temp_subscriber_email) });
					FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
					MongoCursor campaignWisePagePathCursor = campaignWisePagePathFi.iterator();

					while (campaignWisePagePathCursor.hasNext()) {
						Document campaignWisePagePath = (Document) campaignWisePagePathCursor.next();
						System.out.println("campaignWisePagePath : " + campaignWisePagePath);
						ga_user = campaignWisePagePath.get("ga_username").toString();
						timeOnPageArrList.add(Double.parseDouble(campaignWisePagePath.get("timeOnPage").toString()));
						sessionCountArrList.add(Integer.parseInt(campaignWisePagePath.get("sessionCount").toString()));
						System.out.println("sessionCount : " + campaignWisePagePath.get("sessionCount").toString());
						dateHourMinuteArrList.add(campaignWisePagePath.get("dateHourMinute").toString());
						sessionDurationBucket
								.add(Integer.parseInt(campaignWisePagePath.get("sessionDurationBucket").toString()));
						hostNameArrList.add(campaignWisePagePath.get("hostname").toString());
					}

					if (dateHourMinuteArrList.size() > 0) {
						System.out.println("dateHourMinuteArrList : "
								+ (String) dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
					}

					if (hostNameArrList.size() > 0) {
						hostname = (String) hostNameArrList.get(hostNameArrList.size() - 1);
					}

					if (pagePath.length() == 1) {
						pagePath = hostname;
						System.out.println(hostname);
					} else if (pagePath.contains("/?")) {
						pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
					} else if (pagePath.contains("?")) {
						pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
					} else if (pagePath.contains("/")) {
						pagePath = hostname + pagePath;
					}

					Map<String, String> timeOnPageMap = ArrayListOperationsForDoubleValue(timeOnPageArrList);
					rule_json_object.put("Recent_" + pagePath, ((String) timeOnPageMap.get("Average")).toString());
					System.out.println("timeOnPageMap : " + timeOnPageMap);
					Map<String, String> sessionCountMap = ArrayListOperationsForIntegerValue(sessionCountArrList);
					rule_json_object.put("Recent_SessionCount", Integer.parseInt((String) sessionCountMap.get("Max")));
					Map<String, String> sessionDurationBucketMap = ArrayListOperationsForIntegerValue(
							sessionDurationBucket);
					Map<String, String> AvgSessionDurationBucketMap = GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId(
							collection, filter);
					System.out.println("AvgSessionDurationBucketMap== " + AvgSessionDurationBucketMap);
					System.out.println("recentavgtttttt)" + (String) AvgSessionDurationBucketMap.get("Average"));
					rule_json_object.put("Recent_AvgSesionDuration", AvgSessionDurationBucketMap.get("Average"));
					System.out.println("rule_json_objectooooo= " + rule_json_object);
					++i;
				}
			}

			System.out.println("rule_json_objectmethod" + rule_json_object);
		} catch (Exception var40) {
			System.out.println("Exception : " + var40.getMessage());
		} finally {
			database = null;
			collection = null;
		}

		return rule_json_object;
	}

	public static JSONObject fetchMostRecentGADataForRuleEngine(String coll_name, String sourceMedium,
			String temp_subscriber_email) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		JSONArray pagePathJsonArr = null;
		new JSONArray();
		String pagePath = null;
		String hostname = null;
		ArrayList<Double> timeOnPageArrList = null;
		ArrayList<Integer> sessionCountArrList = null;
		ArrayList<String> dateHourMinuteArrList = null;
		ArrayList<Integer> sessionDurationBucket = null;
		ArrayList<String> hostNameArrList = null;
		new TreeSet();
		org.json.JSONArray finalJsonArrayForRuleEngine = new org.json.JSONArray();
		JSONObject rule_json_object = null;
		String campaign_id = null;
		String ga_user = null;
		String source = null;
		Map<String, String> campaignDetailsMap = null;
		int int_most_recent_days = 0;
		Date most_recent_date = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String recent_days_1 = ResourceBundle.getBundle("config").getString("most_recent_days");
			int_most_recent_days = Integer.parseInt(recent_days_1);
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

			rule_json_object = new JSONObject();
			Bson filter2 = null;
			System.out.println("-----------Unique sourceMedium : " + sourceMedium
					+ "  : sourceMedium Unique-------------------------");
			Bson page_path_filter = Filters.and(new Bson[] { Filters.gt("dateHourMinuteInDateFormat", most_recent_date),
					Filters.eq("dimension2", temp_subscriber_email), Filters.eq("sourceMedium", sourceMedium) });
			Bson filter = Filters.and(new Bson[] { Filters.gt("dateHourMinuteInDateFormat", most_recent_date),
					Filters.eq("sourceMedium", sourceMedium), Filters.eq("dimension2", temp_subscriber_email) });
			pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(collection, page_path_filter);
			if (pagePathJsonArr.size() <= 0) {
				rule_json_object.put("MostRecent_" + pagePath, "No most recent url found");
				rule_json_object.put("MostRecent_SessionCount", "0");
				rule_json_object.put("MostRecent_AvgSesionDuration", "0");
			} else {
				int i = 0;

				while (true) {
					if (i >= pagePathJsonArr.size()) {
						finalJsonArrayForRuleEngine.put(rule_json_object);
						break;
					}

					pagePath = (String) pagePathJsonArr.get(i);
					System.out.println("pagePath : " + pagePath);
					timeOnPageArrList = new ArrayList();
					sessionCountArrList = new ArrayList();
					dateHourMinuteArrList = new ArrayList();
					sessionDurationBucket = new ArrayList();
					hostNameArrList = new ArrayList();
					filter2 = Filters.and(new Bson[] { Filters.eq("sourceMedium", sourceMedium),
							Filters.eq("pagePath", pagePath), Filters.eq("dimension2", temp_subscriber_email) });
					FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
					MongoCursor campaignWisePagePathCursor = campaignWisePagePathFi.iterator();

					while (campaignWisePagePathCursor.hasNext()) {
						Document campaignWisePagePath = (Document) campaignWisePagePathCursor.next();
						System.out.println("campaignWisePagePath : " + campaignWisePagePath);
						ga_user = campaignWisePagePath.get("ga_username").toString();
						timeOnPageArrList.add(Double.parseDouble(campaignWisePagePath.get("timeOnPage").toString()));
						sessionCountArrList.add(Integer.parseInt(campaignWisePagePath.get("sessionCount").toString()));
						System.out.println("sessionCount : " + campaignWisePagePath.get("sessionCount").toString());
						dateHourMinuteArrList.add(campaignWisePagePath.get("dateHourMinute").toString());
						sessionDurationBucket
								.add(Integer.parseInt(campaignWisePagePath.get("sessionDurationBucket").toString()));
						hostNameArrList.add(campaignWisePagePath.get("hostname").toString());
					}

					if (dateHourMinuteArrList.size() > 0) {
						System.out.println("dateHourMinuteArrList : "
								+ (String) dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
					}

					if (hostNameArrList.size() > 0) {
						hostname = (String) hostNameArrList.get(hostNameArrList.size() - 1);
						System.out.println("Host_Name : " + hostname);
					}

					if (pagePath.length() == 1) {
						pagePath = hostname;
						System.out.println(hostname);
					} else if (pagePath.contains("/?")) {
						pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
					} else if (pagePath.contains("?")) {
						pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
					} else if (pagePath.contains("/")) {
						pagePath = hostname + pagePath;
					}

					Map<String, String> timeOnPageMap = ArrayListOperationsForDoubleValue(timeOnPageArrList);
					rule_json_object.put("MostRecent_" + pagePath, ((String) timeOnPageMap.get("Average")).toString());
					System.out.println("timeOnPageMap : " + timeOnPageMap);
					Map<String, String> sessionCountMap = ArrayListOperationsForIntegerValue(sessionCountArrList);
					rule_json_object.put("MostRecent_SessionCount",
							Integer.parseInt((String) sessionCountMap.get("Max")));
					Map<String, String> sessionDurationBucketMap = ArrayListOperationsForIntegerValue(
							sessionDurationBucket);
					Map<String, String> AvgSessionDurationBucketMap = GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId(
							collection, filter);
					rule_json_object.put("MostRecent_AvgSesionDuration", AvgSessionDurationBucketMap.get("Average"));
					++i;
				}
			}

			System.out.println(finalJsonArrayForRuleEngine);
		} catch (Exception var40) {
			System.out.println("Exception : " + var40.getMessage());
		} finally {
			database = null;
			collection = null;
		}

		return rule_json_object;
	}

	public static JSONArray saveGADataForSubscribersView(String coll_name) {
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
		TreeSet<String> UniqueSubscribers = new TreeSet();
		String campaign_id = null;
		String ga_user = null;
		String source = null;
		Document url_view_search_query_doc_object = null;
		Document url_view_set_doc = null;
		Document url_view_set_on_insert_doc = null;
		Document url_view_inc_doc = null;
		Document url_view_update_doc = null;
		Map<String, String> campaignDetailsMap = null;
		logger.info("111Going... to save data to google_analytics_url_view_collection from datatemp coll ");
		funnelListJsonArr.add("1");

		try {
			funnelListJsonArr.add("2");
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			logger.info("subscriber_email dimension2  collection = ");
			DistinctIterable<String> dimension2Di = collection.distinct("dimension2", String.class);
			logger.info("subscriber_email distinct= ");
			MongoCursor dimension2Cursor = dimension2Di.iterator();

			try {
				while (dimension2Cursor.hasNext()) {
					subscriber_email = ((String) dimension2Cursor.next()).toString();
					logger.info("subscriber_email distinct= " + subscriber_email);
					if (subscriber_email.contains("@")) {
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
				Iterator var29 = UniqueSubscribers.iterator();

				while (var29.hasNext()) {
					String temp_subscriber_email = (String) var29.next();
					logger.info("temp_subscriber_email : " + temp_subscriber_email);
					Bson filter1 = Filters.and(new Bson[] { Filters.eq("dimension2", temp_subscriber_email) });
					DistinctIterable<String> sourceMediumDi = collection.distinct("sourceMedium", filter1,
							String.class);
					MongoCursor<String> sourceMediumCursor = sourceMediumDi.iterator();
					int countsource = 0;

					try {
						while (sourceMediumCursor.hasNext()) {
							++countsource;
							sourceMedium = ((String) sourceMediumCursor.next()).toString();
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
							} else if (sourceMedium.contains("(not set)")) {
								source = "not set";
								campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1)
										.replace("not set", "").trim();
								logger.info("source=direct ;" + source + " :: campaign_id= " + campaign_id);
							} else {
								campaign_id = "NULL";
								logger.info("else else  " + campaign_id);
							}

							campaign_id = campaign_id.trim();
							if (!campaign_id.equals("NULL")) {
								logger.info("  campaign_id: " + campaign_id + " ::  temp_subscriber_email: "
										+ temp_subscriber_email);

								try {
									campaignDetailsMap = GetCampaignDetailsBasedOnSubscriberIdFromMongo(campaign_id,
											temp_subscriber_email);
									logger.info(" call GetCampaignDetailsBasedOnSubscriberIdFromMongo: "
											+ campaignDetailsMap);
								} catch (Exception var69) {
									logger.info("Exc in GetCampaignDetailsBasedOnSubscriberIdFromMongo :" + var69);
								}
							} else {
								campaignDetailsMap = new HashMap();
								((Map) campaignDetailsMap).put("CreatedBy", "NA");
								((Map) campaignDetailsMap).put("funnelName", "NA");
								((Map) campaignDetailsMap).put("Category", "NA");
								((Map) campaignDetailsMap).put("group", "NA");
								((Map) campaignDetailsMap).put("CampaignName", "NA");
								((Map) campaignDetailsMap).put("Campaign_Id", "NA");
								logger.info(" else after adding campaignDetailsMap : " + campaignDetailsMap);
							}

							Bson filter2 = null;
							logger.info("-----------Unique sourceMedium : " + sourceMedium
									+ "  : sourceMedium Unique-------------------------");
							Bson page_path_filter = Filters
									.and(new Bson[] { Filters.eq("dimension2", temp_subscriber_email),
											Filters.eq("sourceMedium", sourceMedium) });

							try {
								pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(collection, page_path_filter);
								logger.info(
										"pagePathJsonArr: " + pagePathJsonArr + " :: size :" + pagePathJsonArr.size());
							} catch (Exception var68) {
								logger.info("Exc in GetUrlsBasedOnCampaignIdAndSubscriberId :" + var68);
							}

							logger.info("pagePathJsonArr.size(): ");

							for (int i = 0; i < pagePathJsonArr.size(); ++i) {
								try {
									url_view_search_query_doc_object = new Document();
									logger.info("subscriber_email: " + temp_subscriber_email + " :: campaign_id :"
											+ campaign_id + ":: sourceMedium :" + sourceMedium + ":: source" + source);
									url_view_search_query_doc_object.put("Subscriber_Email", temp_subscriber_email);
									url_view_search_query_doc_object.put("sourceMedium", sourceMedium);
									url_view_search_query_doc_object.put("Source", source);
									url_view_set_doc = new Document();
									url_view_set_on_insert_doc = new Document();
									url_view_inc_doc = new Document();
									url_view_update_doc = new Document();
									url_view_set_doc.put("Created_By",
											((String) ((Map) campaignDetailsMap).get("CreatedBy")).toString());
									url_view_set_doc.put("Funnel_Name",
											((String) ((Map) campaignDetailsMap).get("funnelName")).toString());
									url_view_set_doc.put("group",
											((String) ((Map) campaignDetailsMap).get("group")).toString());
									if (((Map) campaignDetailsMap).containsKey("Parentfunnel")) {
										url_view_set_doc.put("Parentfunnel",
												((String) ((Map) campaignDetailsMap).get("Parentfunnel")).toString());
									}

									url_view_set_doc.put("Category",
											((String) ((Map) campaignDetailsMap).get("Category")).toString());
									url_view_set_doc.put("Campaign_id",
											((String) ((Map) campaignDetailsMap).get("Campaign_id")).toString());
									url_view_set_doc.put("CampaignName",
											((String) ((Map) campaignDetailsMap).get("CampaignName")).toString());
									url_view_set_doc.put("Subscriber_Email", temp_subscriber_email);
									pagePath = (String) pagePathJsonArr.get(i);
									logger.info("pagePath : " + pagePath);
									timeOnPageArrList = new ArrayList();
									sessionCountArrList = new ArrayList();
									dateHourMinuteArrList = new ArrayList();
									sessionDurationBucket = new ArrayList();
									hostNameArrList = new ArrayList();
									locationArrList = new ArrayList();
									filter2 = Filters.and(new Bson[] { Filters.eq("sourceMedium", sourceMedium),
											Filters.eq("pagePath", pagePath),
											Filters.eq("dimension2", temp_subscriber_email) });
									FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
									MongoCursor campaignWisePagePathCursor = campaignWisePagePathFi.iterator();

									while (campaignWisePagePathCursor.hasNext()) {
										Document campaignWisePagePath = (Document) campaignWisePagePathCursor.next();
										logger.info("campaignWisePagePath : " + campaignWisePagePath);
										
										ga_user = campaignWisePagePath.get("ga_username").toString();
										try {
										timeOnPageArrList.add(
												Double.parseDouble(campaignWisePagePath.get("timeOnPage").toString()));
										}catch (Exception e) {
											// TODO: handle exception
										}
										try {
										sessionCountArrList.add(
												Integer.parseInt(campaignWisePagePath.get("sessionCount").toString()));
										}catch (Exception e) {
											// TODO: handle exception
										}
										try {
										dateHourMinuteArrList
												.add(campaignWisePagePath.get("dateHourMinute").toString());
										}catch (Exception e) {
											// TODO: handle exception
										}
										try {
										logger.info("sessionCount : " + dateHourMinuteArrList);
										sessionDurationBucket.add(Integer.parseInt(
												campaignWisePagePath.get("sessionDurationBucket").toString()));
										}catch (Exception e) {
											// TODO: handle exception
										}
										hostNameArrList.add(campaignWisePagePath.get("hostname").toString());
										locationArrList.add(campaignWisePagePath.get("country").toString());
									}

									url_view_search_query_doc_object.put("ga_user", ga_user);
									if (dateHourMinuteArrList.size() > 0) {
										url_view_set_on_insert_doc.put("firstclick", dateHourMinuteArrList.get(0));
										url_view_set_doc.put("lastclick",
												dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
									}

									if (hostNameArrList.size() > 0) {
										hostname = (String) hostNameArrList.get(hostNameArrList.size() - 1);
										url_view_search_query_doc_object.put("hostname", hostname);
									}

									if (locationArrList.size() > 0) {
										country = (String) locationArrList.get(locationArrList.size() - 1);
										url_view_search_query_doc_object.put("location", country);
									}

									if (pagePath.length() == 1) {
										pagePath = hostname;
									} 
									 else if (pagePath.contains("yout")) {
											pagePath =pagePath;
											logger.info("pagePath youtube = " + pagePath);
										}else if (pagePath.contains("MailOpen")) {
											pagePath = pagePath;
											
										}else if (pagePath.contains("/?")) {
										pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
									} else if (pagePath.contains("?")) {
										pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
									} else if (pagePath.contains("/")) {
										pagePath = hostname + pagePath;
									}

									logger.info("pagePath = " + pagePath);
									url_view_search_query_doc_object.put("url", pagePath);
									Map<String, String> timeOnPageMap = ArrayListOperationsForDoubleValue(
											timeOnPageArrList);
									logger.info("timeOnPageMap = " + timeOnPageMap);
									url_view_set_doc.put("AvgTimeOnPage",
											Double.parseDouble((String) timeOnPageMap.get("Average")));
									url_view_set_doc.put("MinTimeOnPage",
											Double.parseDouble((String) timeOnPageMap.get("Min")));
									url_view_set_doc.put("MaxTimeOnPage",
											Double.parseDouble((String) timeOnPageMap.get("Max")));
									url_view_inc_doc.put("TotalTimeOnPage",
											Double.parseDouble((String) timeOnPageMap.get("Sum")));
									Map<String, String> sessionCountMap = ArrayListOperationsForIntegerValue(
											sessionCountArrList);
									logger.info("sessionCountMap = " + sessionCountMap);
									url_view_set_doc.put("LastSessionCount",
											Integer.parseInt((String) sessionCountMap.get("Max")));
									url_view_inc_doc.put("NoOfUrlClicks",
											Integer.parseInt((String) sessionCountMap.get("Count")));
									Bson filter = Filters.and(new Bson[] { Filters.eq("sourceMedium", sourceMedium),
											Filters.eq("dimension2", temp_subscriber_email) });
									logger.info("filter = " + filter);
									try {
									Map<String, String> AvgSessionDurationBucketMap = GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId(
											collection, filter);
									url_view_set_doc.put("AvgSesionDuration",
											Double.parseDouble((String) AvgSessionDurationBucketMap.get("Average")));
									Map<String, String> sessionDurationBucketMap = ArrayListOperationsForIntegerValueOfSessionDuration(
											sessionDurationBucket, sessionCountArrList);
									url_view_set_doc.put("MinSesionDuration",
											Integer.parseInt((String) sessionDurationBucketMap.get("Min")));
									url_view_set_doc.put("MaxSesionDuration",
											Integer.parseInt((String) sessionDurationBucketMap.get("Max")));
									url_view_inc_doc.put("TotalSesionDuration",
											Integer.parseInt((String) sessionDurationBucketMap.get("Sum")));
									url_view_inc_doc.put("SessionCount",
											Integer.parseInt((String) sessionDurationBucketMap.get("Count")));
									}catch (Exception e) {
										// TODO: handle exception
									}
//									funnelListJsonArr.add("createFindFromURLViewDataAndUpdate.size()");
									logger.info("calling  createFindFromURLViewDataAndUpdate  : ");
									org.json.simple.JSONObject inpjs = new org.json.simple.JSONObject();
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
									logger.info(
											"end saveGADataForSubscribersView method=================== END ====: ");
								} catch (Exception var70) {
									funnelListJsonArr.add("eexc:: " + var70);
									logger.info("exc in createFindFromURLViewDataAndUpdate  : " + var70);
								}
							}
						}
					} finally {
						sourceMediumCursor.close();
					}
				}
			}
		} catch (Exception var73) {
			funnelListJsonArr.add("exc in subscrberview :: " + var73);
			logger.info("exc in subscriber view  : " + var73);
			System.out.println("Exception while calling Fetch from GA mohit.raj : " + var73.getMessage().toString());
		} finally {
			database = null;
			collection = null;
		}

		return funnelListJsonArr;
	}

	public static void createFindFromURLViewDataAndUpdate(MongoDatabase database, String coll_name,
			Document search_query, Document update_doc, Document url_view_inc_doc, Document url_view_set_doc,
			Document url_view_set_on_insert_doc, String campaign_id, String temp_subscriber_email) {
		MongoCollection<Document> url_view_collection = null;
		Document url_view_update_doc = null;
		double TotalTimeOnPage = 0.0D;
		int NoOfUrlClicks = 0;
		double AvgTimeOnPage = 0.0D;
		double MinTimeOnPage = 0.0D;
		double MaxTimeOnPage = 0.0D;
		int TotalSesionDuration = 0;
		int SessionCount = 0;
		double AvgSesionDuration = 0.0D;
		int MaxSesionDuration = 0;
		int MinSesionDuration = 0;
		int Previous_TotalSesionDuration = 0;
		int Previous_SessionCount = 0;
		double Previous_AvgSesionDuration = 0.0D;
		int Previous_MinSesionDuration = 0;
		int Previous_MaxSesionDuration = 0;
		double Previous_TotalTimeOnPage = 0.0D;
		int Previous_NoOfUrlClicks = 0;
		double Previous_AvgTimeOnPage = 0.0D;
		double Previous_MinTimeOnPage = 0.0D;
		double Previous_MaxTimeOnPage = 0.0D;
		DecimalFormat decimal_formatter = new DecimalFormat("0.00");
		logger.info("calling createFindFromURLViewDataAndUpdate : " + coll_name);

		try {
			url_view_collection = database.getCollection(coll_name);
			MongoCursor<Document> campaignCursor = url_view_collection.find(search_query).iterator();
			if (campaignCursor.hasNext()) {
				while (campaignCursor.hasNext()) {
					Document doc = (Document) campaignCursor.next();
					try {
					logger.info("url_view_set_doc : " + url_view_set_doc.toJson());
					TotalTimeOnPage = url_view_inc_doc.getDouble("TotalTimeOnPage");
					NoOfUrlClicks = url_view_inc_doc.getInteger("NoOfUrlClicks");
					MinTimeOnPage = url_view_set_doc.getDouble("MinTimeOnPage");
					MaxTimeOnPage = url_view_set_doc.getDouble("MaxTimeOnPage");
					SessionCount = url_view_inc_doc.getInteger("SessionCount");
					TotalSesionDuration = url_view_inc_doc.getInteger("TotalSesionDuration");
					MaxSesionDuration = url_view_set_doc.getInteger("MaxSesionDuration");
					MinSesionDuration = url_view_set_doc.getInteger("MinSesionDuration");
					Previous_TotalSesionDuration = doc.getInteger("TotalSesionDuration");
					Previous_SessionCount = doc.getInteger("SessionCount");
					Previous_MinSesionDuration = doc.getInteger("MinSesionDuration");
					Previous_MaxSesionDuration = doc.getInteger("MaxSesionDuration");
					Previous_TotalTimeOnPage = doc.getDouble("TotalTimeOnPage");
					Previous_NoOfUrlClicks = doc.getInteger("NoOfUrlClicks");
					Previous_MinTimeOnPage = doc.getDouble("MinTimeOnPage");
					Previous_MaxTimeOnPage = doc.getDouble("MaxTimeOnPage");
					AvgTimeOnPage = (Previous_TotalTimeOnPage + TotalTimeOnPage)
							/ (double) (Previous_NoOfUrlClicks + NoOfUrlClicks);
					url_view_set_doc.put("AvgTimeOnPage", Double.parseDouble(decimal_formatter.format(AvgTimeOnPage)));
					AvgSesionDuration = (double) ((Previous_TotalSesionDuration + TotalSesionDuration)
							/ (Previous_SessionCount + SessionCount));
					url_view_set_doc.put("AvgSesionDuration",
							Double.parseDouble(decimal_formatter.format(AvgSesionDuration)));
					if (Previous_MinSesionDuration >= MinSesionDuration) {
						url_view_set_doc.put("MinSesionDuration", MinSesionDuration);
					}

					if (Previous_MaxSesionDuration <= MaxSesionDuration) {
						url_view_set_doc.put("MaxSesionDuration", MaxSesionDuration);
					}

					if (Previous_MinTimeOnPage >= MinTimeOnPage) {
						url_view_set_doc.put("MinTimeOnPage", MinTimeOnPage);
					}

					if (Previous_MaxTimeOnPage <= MaxTimeOnPage) {
						url_view_set_doc.put("MaxTimeOnPage", MaxTimeOnPage);
					}
					}catch (Exception var47) {
						logger.info("exc createURLViewData:" + var47);
					}
					logger.info("url_view_set_doc : " + url_view_set_doc.toJson());
					System.out.println("------------------doc.toJson()-------------");
					url_view_update_doc = new Document();
					url_view_update_doc.put("$inc", url_view_inc_doc);
					url_view_update_doc.put("$set", url_view_set_doc);
					url_view_update_doc.put("$setOnInsert", url_view_set_on_insert_doc);

					try {
						logger.info(" if :url_view_update_doc:" + url_view_update_doc.toString());
						createURLViewData(database, "google_analytics_url_view_collection", search_query,
								url_view_update_doc);
					} catch (Exception var47) {
						logger.info("exc in createURLViewData:" + var47);
					}
				
				}
			} else {
				url_view_update_doc = new Document();
				url_view_update_doc.put("$inc", url_view_inc_doc);
				url_view_update_doc.put("$set", url_view_set_doc);
				url_view_update_doc.put("$setOnInsert", url_view_set_on_insert_doc);

				try {
					logger.info(" else :url_view_update_doc:" + url_view_update_doc.toString());
					createURLViewData(database, "google_analytics_url_view_collection", search_query,
							url_view_update_doc);
				} catch (Exception var46) {
					logger.info("exc in createURLViewData:" + var46);
				}
			}
		} catch (Exception var48) {
			logger.info("Exception in createFindFromURLViewDataAndUpdate : " + var48);
		}

	}

	public static void createURLViewData(MongoDatabase database, String coll_name, Document search_query,
			Document update_doc) {
		MongoCollection url_view_collection = null;

		try {
			logger.info("calling createURLViewData: ");
			url_view_collection = database.getCollection(coll_name);
			UpdateOptions options = (new UpdateOptions()).upsert(true);
			Document modifiedObject = new Document();
			modifiedObject.put("$inc", (new BasicDBObject()).append("No_OF_Clicks", 5));
			url_view_collection.updateOne(search_query, update_doc, options);
		} catch (Exception var8) {
			logger.info("Exception createURLViewData: " + var8.getMessage());
		}

	}

	public static void callRuleEngine(MongoCollection<Document> gadatatemp,String jsonObject, String funnel_name, String ruleeng, String createdby,
			String rulejssave) {
		try {
			logger.info("Going... to call rule engine");
			String rule_engine_url = ResourceBundle.getBundle("config").getString("rule_engine_url")
					+ createdby.replace("_", "@") + "_" + funnel_name + "_" + ruleeng + "/fire";
			logger.info("Rule Engine URL : " + rule_engine_url);
			String rule_engine_response = urlconnect(rule_engine_url, jsonObject);
			JSONObject ruleEngineResponseJsonObject = new JSONObject(rule_engine_response);
			logger.info("ruleEngineResponseJsonObject : " + ruleEngineResponseJsonObject);
			if (ruleEngineResponseJsonObject.has("Output")
					&& !BizUtil.isNullString(ruleEngineResponseJsonObject.getString("Output"))) {
				String move_categorynew = ruleEngineResponseJsonObject.getString("Output").trim();
				String checkedop = CheckRuleOPISCategOrFunnel(move_categorynew);
				if (checkedop.equals("category")) {
					String move_category = getOldcategoryName(move_categorynew);
					String original_category = ruleEngineResponseJsonObject.getString("Category").trim();
					int original_category_position = getCategoryPosition(original_category);
					int move_category_position = getCategoryPosition(move_category);
					logger.info("move_category_position :: " + move_category_position);
					logger.info("original_category_position: " + original_category_position);
					if (move_category_position > original_category_position) {
						logger.info("Rule Engine Response (Move TO): " + move_category);
						updateDataSourceforMoveSubscriber(
								ruleEngineResponseJsonObject.get("SubscriberEmail").toString(),
								ruleEngineResponseJsonObject.get("CreatedBy").toString(),
								ruleEngineResponseJsonObject.get("ChildFunnelName").toString(), move_category,
								ruleEngineResponseJsonObject.get("group").toString(), original_category);
						updateMoveCategory(ruleEngineResponseJsonObject.get("SubscriberEmail").toString(),
								move_category, ruleEngineResponseJsonObject.get("ChildFunnelName").toString(),
								ruleEngineResponseJsonObject.get("CreatedBy").toString(), original_category,
								ruleEngineResponseJsonObject.get("CampaignId").toString());
						logger.info("inserted category");
						Bson searchQuery = (new Document()).append("dimension2", ruleEngineResponseJsonObject.get("SubscriberEmail").toString())
								.append("sourceMedium", ruleEngineResponseJsonObject.get("SourceMedium").toString());
						gadatatemp.deleteMany(searchQuery);
						logger.info("removed searchQuery"+searchQuery);
						//SourceMedium
					} else {
						logger.info("No Need to Move");
					}
					
				} else {
					moveContacts(move_categorynew, ruleEngineResponseJsonObject.get("SubscriberEmail").toString(),
							ruleEngineResponseJsonObject.get("CreatedBy").toString(),
							ruleEngineResponseJsonObject.get("group").toString());
				}
			} else {
				logger.info("Did not get any OutPut From Rule Engine");
			}
		} catch (Exception var14) {
			logger.info("Error in Method callRuleEngine() : " + var14.getMessage());
		}

	}

	public static void callRuleEngineOLD(String jsonObject, String funnel_name, JSONObject rulejssave) {
		try {
			logger.info("Going... to call rule engine");
			String rule_engine_url = ResourceBundle.getBundle("config").getString("rule_engine_url") + funnel_name + "_"
					+ funnel_name + "_RE/fire";
			logger.info("Rule Engine URL : " + rule_engine_url);
			String rule_engine_response = "{\"Category\":\"Explore\",\"SubscriberEmail\":\"mohit.raj@bizlem.com\",\"SourceMedium\":\"GAURLTEST / email\",\"MostRecent_bizlem.com/blog.html\":\"9.00\",\"Source\":\"Email\",\"SubFunnelName\":\"Explore\",\"CampaignId\":\"GAURLTEST\",\"bizlem.com/products.html\":\"82\",\"Output\":\"Entice\",\"HostName\":\"bizlem.com\",\"GAUser\":\"bizlembizlem1234@gmail.com\",\"CreatedBy\":\"salesautoconvertuser1@gmail.com\",\"FunnelName\":\"GAURLMergeTail\"}";
			JSONObject ruleEngineResponseJsonObject = new JSONObject(rule_engine_response);
			logger.info("ruleEngineResponseJsonObject : " + ruleEngineResponseJsonObject);
			if (ruleEngineResponseJsonObject.has("Output")) {
				String move_category = ruleEngineResponseJsonObject.getString("Output").trim();
				String original_category = ruleEngineResponseJsonObject.getString("Category").trim();
				int original_category_position = getCategoryPosition(original_category);
				int move_category_position = getCategoryPosition(move_category);
				logger.info("move_category_position :: " + move_category_position);
				logger.info("original_category_position: " + original_category_position);
				if (move_category_position > original_category_position) {
					logger.info("Rule Engine Response (Move TO): " + move_category);
					MongoDatabase database = null;
					Object var11 = null;

					try {
						rulejssave.put("move_category", move_category);
						insertruleDataforMonitoring(rulejssave.toString(),
								ruleEngineResponseJsonObject.get("SubscriberEmail").toString(), original_category,
								ruleEngineResponseJsonObject.get("FunnelName").toString(),
								ruleEngineResponseJsonObject.get("CampaignId").toString(),
								ruleEngineResponseJsonObject.get("CreatedBy").toString());
						logger.info("inserted category");
					} catch (Exception var13) {
						logger.info("exc insert mongo: " + var13);
					}

					logger.info("original_category_position: " + original_category_position);
					logger.info("call " + ruleEngineResponseJsonObject);
				} else {
					logger.info("No Need to Move");
				}
			} else {
				logger.info("Did not get any OutPut From Rule Engine");
			}
		} catch (Exception var14) {
			logger.info("Error in Method callRuleEngine() : " + var14.getMessage());
		}

	}

	public static int getCategoryPosition(String category) {
		int position = 0;
		HashMap<String, Integer> category_map = new HashMap();
		category_map.put("Explore", 1);
		category_map.put("Entice", 2);
		category_map.put("Inform", 3);
		category_map.put("Warm", 4);
		category_map.put("Connect", 5);
		if (category_map.containsKey(category)) {
			position = (Integer) category_map.get(category);
			logger.info("value for key " + category + " is : " + position);
		}

		return position;
	}

	public static String sendPostRequestToManageSubscribers(String callurl, String urlParameters)
			throws ServletException, IOException {
		logger.info("Going... to move Subscriber");
		URL url = new URL(callurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		OutputStream writer = conn.getOutputStream();
		writer.write(urlParameters.getBytes());
		int responseCode = conn.getResponseCode();
		logger.info("POST Response Code :: " + responseCode);
		StringBuffer buffer = new StringBuffer();
		if (responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}

			in.close();
			logger.info(buffer.toString());
		} else {
			logger.info("Move Subscriber POST request not worked");
		}

		writer.flush();
		writer.close();
		return buffer.toString();
	}

	public static Map<String, String> ArrayListOperationsForDoubleValue(ArrayList<Double> timeOnPageArrList) {
		Map<String, String> timeOnPageMap = new HashMap();
		DecimalFormat decimal_formatter = new DecimalFormat("0.00");
		System.out.println(
				"timeOnPageArrList.size() = " + timeOnPageArrList.size() + "timeOnPageArrList" + timeOnPageArrList);
		if (timeOnPageArrList.size() > 0) {
			double sum = 0.0D;

			Double i;
			for (Iterator var6 = timeOnPageArrList.iterator(); var6.hasNext(); sum += i) {
				i = (Double) var6.next();
			}

			double average = sum / (double) timeOnPageArrList.size();
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
		Map<String, String> sessionCountMap = new HashMap();
		if (sessionCountArrList.size() > 0) {
			int sum = 0;

			Integer i;
			for (Iterator var4 = sessionCountArrList.iterator(); var4.hasNext(); sum += i) {
				i = (Integer) var4.next();
			}

			double average = (double) (sum / sessionCountArrList.size());
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
			MongoCursor sessionCountDiCursor = sessionCountDi.iterator();

			try {
				sessionDurationBucketArrList = new ArrayList();

				while (sessionCountDiCursor.hasNext()) {
					sessionCount = ((String) sessionCountDiCursor.next()).toString();
					System.out.println("GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId Unique sessionCount : "
							+ sessionCount);
					Bson filter3 = Filters.eq("sessionCount", sessionCount);
					FindIterable<Document> sessionCountFi = collection.find(filter3);
					MongoCursor sessionCountCursor = sessionCountFi.iterator();

					try {
						if (sessionCountCursor.hasNext()) {
							Document doc = (Document) sessionCountCursor.next();
							sessionDurationBucketArrList.add(Integer.parseInt(doc.getString("sessionDurationBucket")));
						}
					} finally {
						sessionCountCursor.close();
					}
				}

				sessionDurationBucketMap = ArrayListOperationsForIntegerValue(sessionDurationBucketArrList);
			} finally {
				sessionCountDiCursor.close();
			}

			return sessionDurationBucketMap;
		} catch (Exception var21) {
			var21.printStackTrace();
			throw new RuntimeException(var21);
		}
	}

	public static JSONArray GetUrlsBasedOnCampaignIdAndSubscriberId(MongoCollection<Document> collection, Bson filter) {
		JSONArray pagePathJsonArr = new JSONArray();
		String pagePath = null;
		MongoCursor<Document> pagePathCursor = collection.find(filter).iterator();
		HashSet setOfUrl = new HashSet();

		try {
			while (pagePathCursor.hasNext()) {
				Document doc = (Document) pagePathCursor.next();
				pagePath = doc.getString("pagePath");
				setOfUrl.add(pagePath);
				System.out.println("pagePath : " + pagePath);
			}

			Iterator var7 = setOfUrl.iterator();

			while (var7.hasNext()) {
				String url = (String) var7.next();
				pagePathJsonArr.add(url);
			}
		} finally {
			if (pagePathCursor != null) {
				pagePathCursor.close();
				pagePathCursor = null;
			}

			setOfUrl.clear();
			setOfUrl = null;
		}

		return pagePathJsonArr;
	}

	public static String GetHostNameBasedOnCampaignIdAndSubscriberId(MongoCollection<Document> collection,
			Bson filter) {
		new JSONArray();
		String pagePath = null;
		String hostName = null;
		MongoCursor campaignCursor = collection.find(filter).iterator();

		try {
			if (campaignCursor.hasNext()) {
				while (campaignCursor.hasNext()) {
					Document doc = (Document) campaignCursor.next();
					hostName = doc.getString("hostname");
				}
			}
		} finally {
			campaignCursor.close();
		}

		return hostName;
	}

	public static Map<String, String> GetCampaignDetailsBasedOnCampaignIdAndSubscriberIdFromMongo(String campaign_id,
			String subscriber_email) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> subscribers_details_collection = null;
		Map<String, String> campaignDetailsMap = new HashMap();
		String SubscriberId = null;
		Object var7 = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection("campaign_details");
			subscribers_details_collection = database.getCollection("subscribers_details");
			Bson filter = Filters.and(new Bson[] { Filters.eq("Campaign_Id", campaign_id),
					Filters.eq("subscribers.email", subscriber_email) });
			MongoCursor campaignCursor = collection.find(filter).iterator();

			try {
				if (campaignCursor.hasNext()) {
					if (campaignCursor.hasNext()) {
						Document doc = (Document) campaignCursor.next();
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
		} catch (Exception var20) {
			var20.printStackTrace();
			throw new RuntimeException(var20);
		} finally {
			database = null;
			collection = null;
		}

		System.out.println("campaignDetailsMap : " + campaignDetailsMap);
		return campaignDetailsMap;
	}

	public static Map<String, String> GetCampaignDetailsBasedOnSubscriberIdFromMongo(String campaign_id,
			String subscriber_email) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		Map<String, String> campaignDetailsMap = new HashMap();
		Object var5 = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
			MongoCursor<Document> campaignCursor = null;
			String funnelName = null;
			String mainfunnel = null;
			String category = null;
			String group = null;
			String campid = null;
			String createdby = null;

			try {
				String decrpted = null;
				decrpted = decrypt(campaign_id);
				String[] lineData = decrpted.split("\\$");
				org.json.JSONArray dataar = new org.json.JSONArray();
				String[] var19 = lineData;
				
				   for(int i = 0; i <lineData.length; i++)
			        {
			            String val = lineData[i];
			            if(val.trim().length() > 0)
			            {
			                System.out.println((new StringBuilder("i = ")).append(val.trim()).toString());
			                dataar.put(val.trim());
			            }
			        }
					logger.info("dataar = "+dataar);

			        for(int l = 0; l < dataar.length(); l++)
			        {
			            if(l == 0)
			            {
			                funnelName = dataar.getString(l);
			                campaignDetailsMap.put("funnelName", funnelName.trim());
			             
			                if(funnelName.contains("_EC_") || funnelName.contains("_EnC_") || funnelName.contains("_IC_") || funnelName.contains("_WC_") || funnelName.contains("_CC_"))
			                {
			                    mainfunnel = funnelName.substring(0, funnelName.lastIndexOf("_"));
			                    mainfunnel = funnelName.substring(0, mainfunnel.lastIndexOf("_"));
			                } else
			                {
			                    mainfunnel = funnelName;
			                }
			                campaignDetailsMap.put("Parentfunnel", mainfunnel);
			            }
			            if(l == 1)
			            {
			                category = dataar.getString(l);
			                campaignDetailsMap.put("Category", dataar.getString(l));
			            }
			            if(l == 2)
			            {
			                campid = dataar.getString(l);
			                campaignDetailsMap.put("Campaign_id", dataar.getString(l));
			            }
			            if(l == 3)
			            {
			                group = dataar.getString(l);
			                campaignDetailsMap.put("group", dataar.getString(l));
			            }
			            if(l == 4)
			            {
			                createdby = dataar.getString(l);
			                campaignDetailsMap.put("CreatedBy", dataar.getString(l));
			            }
			        }
			    	logger.info("campaignDetailsMap = "+campaignDetailsMap);
			    	try {
			        Bson filter = Filters.and(new Bson[] {
			            Filters.eq("Campaign_id", campid), Filters.eq("funnelName", funnelName), Filters.eq("Category", category), Filters.eq("CreatedBy", createdby), Filters.eq("group", group)
			        });
			    	logger.info("filter = "+filter);
			        campaignCursor = collection.find(filter).iterator();
			        if(campaignCursor.hasNext())
			        {
			            if(campaignCursor.hasNext())
			            {
			                Document doc = (Document)campaignCursor.next();
			                campaignDetailsMap.put("CampaignName", doc.getString("campaignName"));
			            }
			        } else
			        {
//			            campaignDetailsMap.put("CreatedBy", "NA");
//			            campaignDetailsMap.put("funnelName", "NA");
//			            campaignDetailsMap.put("Category", "NA");
			            campaignDetailsMap.put("CampaignName", "NA");
//			            campaignDetailsMap.put("Campaign_id", "NA");
//			            campaignDetailsMap.put("group", "NA");
			        }
			    	}catch (Exception e) {
						// TODO: handle exception
					}
				
			} finally {
				campaignCursor.close();
			}
		} catch (Exception var29) {
			var29.printStackTrace();
			throw new RuntimeException(var29);
		} finally {
			database = null;
			collection = null;
		}

		System.out.println("campaignDetailsMap : " + campaignDetailsMap);
		return campaignDetailsMap;
	}

	public static String getSubscriberIdBasedOnListIdAndEmail(MongoCollection<Document> collection, String ListId,
			String SubscriberEmail) throws ServletException, IOException {
		String SubscriberId = null;
		Bson filter = Filters
				.and(new Bson[] { Filters.eq("ListId", ListId), Filters.eq("SubscriberEmail", SubscriberEmail) });
		MongoCursor campaignCursor = collection.find(filter).iterator();

		try {
			while (campaignCursor.hasNext()) {
				Document doc = (Document) campaignCursor.next();
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
		Bson filter = Filters.eq("SubscriberEmail", SubscriberEmail);
		MongoCursor campaignCursor = collection.find(filter).iterator();

		try {
			while (campaignCursor.hasNext()) {
				Document doc = (Document) campaignCursor.next();
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
		org.json.simple.JSONObject jsonObject = null;
		StringBuffer response = null;

		try {

			String urlParameters = "";
			URL url = new URL(urlstr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
			writer.write(json.toString());
			writer.close();
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			response = new StringBuffer();

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
		} catch (Exception var12) {
			var12.printStackTrace();
		}

		return response.toString();
	}

	public static JSONObject mergeJSONObject(JSONObject json_object_1, JSONObject json_object_2) throws JSONException {
		String[] var5;
		int var4 = (var5 = JSONObject.getNames(json_object_2)).length;

		for (int var3 = 0; var3 < var4; ++var3) {
			String key = var5[var3];
			try {
				json_object_1.put(key, json_object_2.get(key));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return json_object_1;
	}

	public static JSONArray saveGADataForRecentView(String coll_name) {
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
		TreeSet<String> UniqueSubscribers = new TreeSet();
		org.json.JSONArray finalJsonArrayForRuleEngine = new org.json.JSONArray();
		String campaign_id = null;
		String ga_user = null;
		String source = null;
		Map<String, String> campaignDetailsMap = null;
		ArrayList<InsertOneModel<Document>> recent_view_set_doc_arrlist = new ArrayList();
		Document recent_view_set_doc = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			DistinctIterable<String> dimension2Di = collection.distinct("dimension2", String.class);
			MongoCursor dimension2Cursor = dimension2Di.iterator();

			try {
				while (dimension2Cursor.hasNext()) {
					subscriber_email = ((String) dimension2Cursor.next()).toString();
					if (subscriber_email.contains("@")) {
						UniqueSubscribers.add(subscriber_email);
					}
				}
			} finally {
				dimension2Cursor.close();
			}

			System.out.println("Total Number of Subscribers Found  : " + UniqueSubscribers.size());

			for (Iterator var25 = UniqueSubscribers.iterator(); var25.hasNext(); System.out
					.println(finalJsonArrayForRuleEngine)) {
				String temp_subscriber_email = (String) var25.next();
				System.out.println("temp_subscriber_email : " + temp_subscriber_email);
				Bson filter1 = Filters.and(new Bson[] { Filters.eq("dimension2", temp_subscriber_email) });
				DistinctIterable<String> sourceMediumDi = collection.distinct("sourceMedium", filter1, String.class);
				MongoCursor sourceMediumCursor = sourceMediumDi.iterator();

				try {
					while (sourceMediumCursor.hasNext()) {
						sourceMedium = ((String) sourceMediumCursor.next()).toString();
						if (sourceMedium.contains("phplist")) {
							campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1).replace("phplist",
									"");
							source = "Email";
						} else if (sourceMedium.contains("(direct)")) {
							source = "Direct";
							campaign_id = "NULL";
						}

						if (!campaign_id.equals("NULL")) {
							campaignDetailsMap = GetCampaignDetailsBasedOnSubscriberIdFromMongo(campaign_id,
									temp_subscriber_email);
						} else {
							campaignDetailsMap = new HashMap();
							((Map) campaignDetailsMap).put("Created_By", "NA");
							((Map) campaignDetailsMap).put("Funnel_Name", "NA");
							((Map) campaignDetailsMap).put("SubFunnel_Name", "NA");
							((Map) campaignDetailsMap).put("Category", "NA");
							((Map) campaignDetailsMap).put("Campaign_Id", "NA");
							((Map) campaignDetailsMap).put("List_Id", "NA");
							((Map) campaignDetailsMap).put("Subscriber_Id", "NA");
							((Map) campaignDetailsMap).put("Subscriber_Email", "NA");
						}

						Bson filter2 = null;
						System.out.println("-----------Unique sourceMedium : " + sourceMedium
								+ "  : sourceMedium Unique-------------------------");
						Bson page_path_filter = Filters
								.and(new Bson[] { Filters.eq("dimension2", temp_subscriber_email),
										Filters.eq("sourceMedium", sourceMedium) });
						pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(collection, page_path_filter);

						for (int i = 0; i < pagePathJsonArr.size(); ++i) {
							recent_view_set_doc = new Document();
							recent_view_set_doc.put("Created_By",
									((String) ((Map) campaignDetailsMap).get("CreatedBy")).toString());
							recent_view_set_doc.put("Funnel_Name",
									((String) ((Map) campaignDetailsMap).get("funnelName")).toString());
							recent_view_set_doc.put("SubFunnel_Name",
									((String) ((Map) campaignDetailsMap).get("SubFunnelName")).toString());
							recent_view_set_doc.put("Category",
									((String) ((Map) campaignDetailsMap).get("Type")).toString());
							recent_view_set_doc.put("Campaign_Id",
									((String) ((Map) campaignDetailsMap).get("Campaign_Id")).toString());
							recent_view_set_doc.put("List_Id",
									((String) ((Map) campaignDetailsMap).get("List_Id")).toString());
							recent_view_set_doc.put("Subscriber_Id",
									((String) ((Map) campaignDetailsMap).get("subscriber_userid")).toString());
							recent_view_set_doc.put("Subscriber_Email", temp_subscriber_email);
							recent_view_set_doc.put("Subscriber_Email", subscriber_email);
							recent_view_set_doc.put("sourceMedium", sourceMedium);
							recent_view_set_doc.put("Source", source);
							recent_view_set_doc.put("campaign_id", campaign_id);
							pagePath = (String) pagePathJsonArr.get(i);
							System.out.println("pagePath : " + pagePath);
							timeOnPageArrList = new ArrayList();
							sessionCountArrList = new ArrayList();
							dateHourMinuteArrList = new ArrayList();
							sessionDurationBucket = new ArrayList();
							hostNameArrList = new ArrayList();
							filter2 = Filters.and(new Bson[] { Filters.eq("sourceMedium", sourceMedium),
									Filters.eq("pagePath", pagePath),
									Filters.eq("dimension2", temp_subscriber_email) });
							FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
							MongoCursor campaignWisePagePathCursor = campaignWisePagePathFi.iterator();

							while (campaignWisePagePathCursor.hasNext()) {
								Document campaignWisePagePath = (Document) campaignWisePagePathCursor.next();
								System.out.println("campaignWisePagePath : " + campaignWisePagePath);
								ga_user = campaignWisePagePath.get("ga_username").toString();
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
										+ (String) dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
								recent_view_set_doc.put("firstclick", dateHourMinuteArrList.get(0));
								recent_view_set_doc.put("lastclick",
										dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
							}

							if (hostNameArrList.size() > 0) {
								hostname = (String) hostNameArrList.get(hostNameArrList.size() - 1);
								System.out.println("hostname : " + hostname);
								recent_view_set_doc.put("hostname", hostname);
							}

							if (pagePath.length() == 1) {
								pagePath = hostname;
								System.out.println(hostname);
							} else if (pagePath.contains("/?")) {
								pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
							} else if (pagePath.contains("?")) {
								pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
							} else if (pagePath.contains("/")) {
								pagePath = hostname + pagePath;
							}

							recent_view_set_doc.put("url", pagePath);
							Map<String, String> timeOnPageMap = ArrayListOperationsForDoubleValue(timeOnPageArrList);
							System.out.println("timeOnPageMap : " + timeOnPageMap);
							recent_view_set_doc.put("AvgTimeOnPage",
									Double.parseDouble((String) timeOnPageMap.get("Average")));
							recent_view_set_doc.put("MinTimeOnPage",
									Double.parseDouble((String) timeOnPageMap.get("Min")));
							recent_view_set_doc.put("MaxTimeOnPage",
									Double.parseDouble((String) timeOnPageMap.get("Max")));
							recent_view_set_doc.put("TotalTimeOnPage",
									Double.parseDouble((String) timeOnPageMap.get("Sum")));
							Map<String, String> sessionCountMap = ArrayListOperationsForIntegerValue(
									sessionCountArrList);
							System.out.println("sessionCountMap : " + sessionCountMap);
							recent_view_set_doc.put("Last_Session_Count",
									Integer.parseInt((String) sessionCountMap.get("Max")));
							recent_view_set_doc.put("No_OF_Clicks",
									Integer.parseInt((String) sessionCountMap.get("Count")));
							Map<String, String> sessionDurationBucketMap = ArrayListOperationsForIntegerValue(
									sessionDurationBucket);
							System.out.println("sessionDurationBucketMap : " + sessionDurationBucketMap);
							recent_view_set_doc.put("AvgSesionDuration",
									Double.parseDouble((String) sessionDurationBucketMap.get("Average")));
							recent_view_set_doc.put("MinSesionDuration",
									Integer.parseInt((String) sessionDurationBucketMap.get("Min")));
							recent_view_set_doc.put("MaxSesionDuration",
									Integer.parseInt((String) sessionDurationBucketMap.get("Max")));
							recent_view_set_doc.put("TotalSesionDuration",
									Integer.parseInt((String) sessionDurationBucketMap.get("Sum")));
							recent_view_set_doc_arrlist.add(new InsertOneModel(recent_view_set_doc));
						}

						System.out.println("recent_view_set_doc_arrlist : " + recent_view_set_doc_arrlist.size());
						saveRecentSubscriberData(database, "google_analytics_recent_url_view_collection",
								recent_view_set_doc_arrlist, campaign_id, temp_subscriber_email);
					}
				} finally {
					sourceMediumCursor.close();
				}
			}
		} catch (Exception var53) {
			var53.printStackTrace();
			System.out.println("Exception : " + var53.getMessage());
		} finally {
			database = null;
			collection = null;
		}

		return funnelListJsonArr;
	}

	public static void saveRecentSubscriberData(MongoDatabase database, String coll_name,
			ArrayList<InsertOneModel<Document>> documents, String campaign_id, String temp_subscriber_email) {
		MongoCollection google_analytics_recent_url_view_collection = null;

		try {
			google_analytics_recent_url_view_collection = database.getCollection(coll_name);
			BulkWriteOptions options = new BulkWriteOptions();
			options.ordered(false);
			google_analytics_recent_url_view_collection.bulkWrite(documents, options);
		} catch (Exception var10) {
			System.out.println("Method saveSubscriberTempData() Error : " + var10.getMessage());
		} finally {
			System.out.println("Records Saved In Collection 'temp_google_analytics_subscriber_data'");
		}

	}

	public static void fetchRecentGADataForRuleEngineTest(String coll_name, String sourceMedium,
			String temp_subscriber_email) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;

		Date tmp_date1 = null;
		org.json.JSONArray finalJsonArrayForRuleEngine = new org.json.JSONArray();
		Object var8 = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String recent_days_1 = ResourceBundle.getBundle("config").getString("recent_days_1");
			int int_recent_days_1 = 20;
			Date date_campare1 = new Date();
			date_campare1.setDate(date_campare1.getDate() - int_recent_days_1);
			tmp_date1 = dateFormat.parse(dateFormat.format(date_campare1));
			Bson filter1 = Filters.gt("dateHourMinuteInDateFormat", tmp_date1);
			FindIterable<Document> campaignWisePagePathFi = collection.find(filter1);
			MongoCursor campaignWisePagePathCursor = campaignWisePagePathFi.iterator();

			while (campaignWisePagePathCursor.hasNext()) {
				Document campaignWisePagePath = (Document) campaignWisePagePathCursor.next();
				finalJsonArrayForRuleEngine.put(campaignWisePagePath.toJson());
				System.out.println(campaignWisePagePath.toJson());
			}

			System.out.println("Size : " + finalJsonArrayForRuleEngine.length());
			System.out.println(finalJsonArrayForRuleEngine);
		} catch (Exception var21) {
			;
		} finally {
			Bson filter2 = Filters.lte("dateHourMinuteInDateFormat", tmp_date1);
			collection.deleteMany(filter2);
			database = null;
			collection = null;
		}

	}

	public static JSONArray GetAvgSessionDuration(String coll_name, String sourceMedium) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> campaign_collection = null;
		JSONArray funnelListJsonArr = new JSONArray();
		org.json.simple.JSONObject availableUrlJsonObj = null;
		String sessionCount = null;
		ArrayList<Integer> sessionDurationBucketArrList = null;
		Object var9 = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			Bson filter1 = Filters.and(new Bson[] { Filters.eq("sourceMedium", sourceMedium) });
			DistinctIterable<String> sessionCountDi = collection.distinct("sessionCount", filter1, String.class);
			MongoCursor<String> sessionCountDiCursor = sessionCountDi.iterator();
			sessionDurationBucketArrList = new ArrayList();

			try {
				while (sessionCountDiCursor.hasNext()) {
					sessionCount = ((String) sessionCountDiCursor.next()).toString();
					System.out.println("Unique sessionCount : " + sessionCount);
					Bson filter3 = Filters.eq("sessionCount", sessionCount);
					FindIterable<Document> sessionCountFi = collection.find(filter3);
					MongoCursor sessionCountCursor = sessionCountFi.iterator();

					try {
						if (sessionCountCursor.hasNext()) {
							Document doc = (Document) sessionCountCursor.next();
							sessionDurationBucketArrList.add(Integer.parseInt(doc.getString("sessionDurationBucket")));
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
		} catch (Exception var34) {
			var34.printStackTrace();
			throw new RuntimeException(var34);
		} finally {
			database = null;
			collection = null;
		}

		return funnelListJsonArr;
	}

	public static void saveSubscriberTempData(String coll_name, String subscriber_email) {
		MongoDatabase database = null;
		Bson filter1 = null;
		MongoCollection<Document> google_analytics_data_temp_collection = null;
		MongoCollection<Document> temp_google_analytics_subscriber_data_collection = null;
		ArrayList documents = new ArrayList();

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			google_analytics_data_temp_collection = database.getCollection(coll_name);
			filter1 = Filters.eq("dimension2", subscriber_email);
			FindIterable<Document> dimension2Fi = google_analytics_data_temp_collection.find(filter1);
			MongoCursor dimension2FiCursor = dimension2Fi.iterator();

			try {
				while (dimension2FiCursor.hasNext()) {
					Document doc = (Document) dimension2FiCursor.next();
					documents.add(doc);
				}
			} finally {
				dimension2FiCursor.close();
			}

			temp_google_analytics_subscriber_data_collection = database
					.getCollection("temp_google_analytics_subscriber_data");
			temp_google_analytics_subscriber_data_collection.drop();
			temp_google_analytics_subscriber_data_collection.insertMany(documents);
		} catch (Exception var19) {
			System.out.println("Method saveSubscriberTempData() Error : " + var19.getMessage());
		} finally {
			database = null;
			google_analytics_data_temp_collection = null;
		}

	}

	public static JSONArray findUniqueCampaignForSubscriber(String coll_name, String Sling_Campaign_Id) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> campaign_collection = null;
		JSONArray pagePathJsonArr = new JSONArray();
		JSONArray funnelListJsonArr = new JSONArray();
		org.json.simple.JSONObject availableUrlJsonObj = null;
		String sourceMedium = null;
		String pagePath = null;
		Object var10 = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			DistinctIterable<String> pagePathDi = collection.distinct("pagePath", String.class);
			MongoCursor pagePathCursor = pagePathDi.iterator();

			try {
				while (pagePathCursor.hasNext()) {
					pagePathJsonArr.add(((String) pagePathCursor.next()).toString());
				}
			} finally {
				pagePathCursor.close();
			}

			DistinctIterable<String> sourceMediumDi = collection.distinct("sourceMedium", String.class);
			MongoCursor sourceMediumCursor = sourceMediumDi.iterator();

			try {
				Bson filter1 = null;
				if (sourceMediumCursor.hasNext()) {
					new TreeSet();
					new TreeSet();
					new TreeSet();
					sourceMedium = ((String) sourceMediumCursor.next()).toString();
					System.out.println("---------------------------------------------------------Unique sourceMedium : "
							+ sourceMedium);
					ArrayList<Double> timeOnPageArrList = null;
					ArrayList<Integer> sessionCountArrList = null;
					ArrayList<String> dateHourMinuteArrList = null;
					ArrayList<Integer> sessionDurationBucket = null;

					for (int i = 0; i < pagePathJsonArr.size(); ++i) {
						pagePath = (String) pagePathJsonArr.get(i);
						System.out.println("pagePath : " + pagePath);
						timeOnPageArrList = new ArrayList();
						sessionCountArrList = new ArrayList();
						dateHourMinuteArrList = new ArrayList();
						sessionDurationBucket = new ArrayList();
						filter1 = Filters.and(new Bson[] { Filters.eq("sourceMedium", sourceMedium),
								Filters.eq("pagePath", pagePath) });
						FindIterable<Document> campaignWisePagePathFi = collection.find(filter1);
						MongoCursor campaignWisePagePathCursor = campaignWisePagePathFi.iterator();

						while (campaignWisePagePathCursor.hasNext()) {
							Document campaignWisePagePath = (Document) campaignWisePagePathCursor.next();
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
						}

						System.out.println("timeOnPageArrList : " + timeOnPageArrList);
						ArrayListOperationsForDoubleValue(timeOnPageArrList);
						System.out.println("sessionCountArrList : " + sessionCountArrList);
						ArrayListOperationsForIntegerValue(sessionCountArrList);
						System.out.println("dateHourMinuteArrList : " + dateHourMinuteArrList);
						if (dateHourMinuteArrList.size() > 0) {
							System.out.println("dateHourMinuteArrList : "
									+ (String) dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
						}

						System.out.println("sessionDurationBucket : " + sessionDurationBucket);
						ArrayListOperationsForIntegerValue(sessionDurationBucket);
					}
				}
			} finally {
				sourceMediumCursor.close();
			}

			System.out.println("funnelListJsonArr : " + funnelListJsonArr);
		} catch (Exception var43) {
			var43.printStackTrace();
			System.out.println("Exception : " + var43.getMessage());
		} finally {
			database = null;
			collection = null;
		}

		return funnelListJsonArr;
	}

	public static TreeSet<String> findUniqueCampaign(String coll_name) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		String campaign_id = null;
		TreeSet UniqueCampaigns = new TreeSet();

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			collection.drop();
			DistinctIterable<String> sourceMediumDi = collection.distinct("sourceMedium", String.class);
			MongoCursor<String> sourceMediumCursor = sourceMediumDi.iterator();
			boolean var7 = false;

			try {
				while (sourceMediumCursor.hasNext()) {
					campaign_id = ((String) sourceMediumCursor.next()).toString();
					if (campaign_id.contains("phplist") || campaign_id.contains("direct")) {
						System.out.println("campaign_id : " + campaign_id);
						UniqueCampaigns.add(campaign_id);
					}
				}
			} finally {
				sourceMediumCursor.close();
			}

			System.out.println("UniqueCampaigns : " + UniqueCampaigns);
		} catch (Exception var17) {
			var17.printStackTrace();
			throw new RuntimeException(var17);
		} finally {
			database = null;
			collection = null;
		}

		return UniqueCampaigns;
	}

	public static String saveTempSubscriberGAData(ArrayList<Document> documents, String coll_name, String username) {
		MongoDatabase database = null;
		MongoCollection collection = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			collection.drop();
			collection.insertMany(documents);
		} catch (Exception var9) {
			var9.printStackTrace();
			throw new RuntimeException(var9);
		} finally {
			database = null;
			collection = null;
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
		return "true";
	}

	public static JSONArray getUniquePagePath(String coll_name, String Sling_Campaign_Id) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> campaign_collection = null;
		JSONArray funnelListJsonArr = new JSONArray();
		org.json.simple.JSONObject availableUrlJsonObj = null;
		String pagePath = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			DistinctIterable<String> di = collection.distinct("pagePath", String.class);
			MongoCursor cursor = di.iterator();

			try {
				while (cursor.hasNext()) {
					pagePath = ((String) cursor.next()).toString();
					funnelListJsonArr.add(pagePath);
				}
			} finally {
				cursor.close();
			}
		} catch (Exception var19) {
			var19.printStackTrace();
			throw new RuntimeException(var19);
		} finally {
			database = null;
			collection = null;
		}

		return funnelListJsonArr;
	}

	public static JSONArray findUniqueUrl(String coll_name, String Sling_Campaign_Id) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> campaign_collection = null;
		JSONArray funnelListJsonArr = new JSONArray();
		org.json.simple.JSONObject availableUrlJsonObj = null;
		String pagePath = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			DistinctIterable<String> di = collection.distinct("pagePath", String.class);
			MongoCursor cursor = di.iterator();

			try {
				while (cursor.hasNext()) {
					pagePath = ((String) cursor.next()).toString();
					System.out.println("Unique pagePath : " + pagePath);

					String urlclickstatistics_latestclick = null;

					Bson filter3 = Filters.eq("pagePath", pagePath);
					FindIterable<Document> ttl_click_fi = collection.find(filter3);
					MongoCursor ttl_clickcursor = ttl_click_fi.iterator();

					try {
						while (ttl_clickcursor.hasNext()) {
							Document doc = (Document) ttl_clickcursor.next();
							System.out.println(doc);
						}
					} finally {
						ttl_clickcursor.close();
					}
				}
			} finally {
				cursor.close();
			}

			System.out.println("funnelListJsonArr : " + funnelListJsonArr);
		} catch (Exception var34) {
			var34.printStackTrace();
			throw new RuntimeException(var34);
		} finally {
			database = null;
			collection = null;
		}

		return funnelListJsonArr;
	}

	public static JSONArray createURLViewDataTest(String coll_name, String subscriber_email) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> url_view_collection = null;
		JSONArray pagePathJsonArr = null;
		JSONArray funnelListJsonArr = new JSONArray();
		String sourceMedium = null;
		String pagePath = null;
		String hostname = null;
		ArrayList<Double> timeOnPageArrList = null;
		ArrayList<Integer> sessionCountArrList = null;
		ArrayList<String> dateHourMinuteArrList = null;
		ArrayList<Integer> sessionDurationBucket = null;
		ArrayList<String> hostNameArrList = null;
		String campaign_id = null;
		String ga_user = null;
		String source = null;
		ArrayList<Document> finalDocumentsArrayForURLView = new ArrayList();
		Document url_view_doc_object = null;
		Object var20 = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection(coll_name);
			url_view_collection = database.getCollection("google_analytics_url_view_collection");
			System.out.println("temp_subscriber_email : " + subscriber_email);
			Bson filter1 = Filters.and(new Bson[] { Filters.eq("dimension2", subscriber_email) });
			DistinctIterable<String> sourceMediumDi = collection.distinct("sourceMedium", filter1, String.class);
			MongoCursor sourceMediumCursor = sourceMediumDi.iterator();

			try {
				while (sourceMediumCursor.hasNext()) {
					sourceMedium = ((String) sourceMediumCursor.next()).toString();
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
					Bson page_path_filter = Filters.and(new Bson[] { Filters.eq("dimension2", subscriber_email),
							Filters.eq("sourceMedium", sourceMedium) });
					pagePathJsonArr = GetUrlsBasedOnCampaignIdAndSubscriberId(collection, page_path_filter);

					for (int i = 0; i < pagePathJsonArr.size(); ++i) {
						url_view_doc_object = new Document();
						url_view_doc_object.put("Subscriber_Email", subscriber_email);
						url_view_doc_object.put("sourceMedium", sourceMedium);
						url_view_doc_object.put("Source", source);
						url_view_doc_object.put("campaign_id", campaign_id);
						pagePath = (String) pagePathJsonArr.get(i);
						System.out.println("pagePath : " + pagePath);
						timeOnPageArrList = new ArrayList();
						sessionCountArrList = new ArrayList();
						dateHourMinuteArrList = new ArrayList();
						sessionDurationBucket = new ArrayList();
						hostNameArrList = new ArrayList();
						filter2 = Filters.and(new Bson[] { Filters.eq("sourceMedium", sourceMedium),
								Filters.eq("pagePath", pagePath), Filters.eq("dimension2", subscriber_email) });
						FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
						MongoCursor campaignWisePagePathCursor = campaignWisePagePathFi.iterator();

						while (campaignWisePagePathCursor.hasNext()) {
							Document campaignWisePagePath = (Document) campaignWisePagePathCursor.next();
							System.out.println("campaignWisePagePath : " + campaignWisePagePath);
							ga_user = campaignWisePagePath.get("ga_username").toString();
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
							hostname = (String) hostNameArrList.get(hostNameArrList.size() - 1);
							url_view_doc_object.put("hostname", hostname);
							System.out.println("hostname : " + hostname);
						}

						if (dateHourMinuteArrList.size() > 0) {
							System.out.println("dateHourMinuteArrList : "
									+ (String) dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
							url_view_doc_object.put("firstclick", dateHourMinuteArrList.get(0));
							url_view_doc_object.put("lastclick",
									dateHourMinuteArrList.get(dateHourMinuteArrList.size() - 1));
						}

						if (pagePath.length() == 1) {
							pagePath = hostname;
							System.out.println(hostname);
						} else if (pagePath.contains("/?")) {
							pagePath = hostname + pagePath.substring(0, pagePath.indexOf("/?"));
						} else if (pagePath.contains("?")) {
							pagePath = hostname + pagePath.substring(0, pagePath.indexOf("?"));
						} else if (pagePath.contains("/")) {
							pagePath = hostname + pagePath;
						}

						url_view_doc_object.put("url", pagePath);
						Map<String, String> timeOnPageMap = ArrayListOperationsForDoubleValue(timeOnPageArrList);
						System.out.println("timeOnPageMap : " + timeOnPageMap);
						url_view_doc_object.put("AvgTimeOnPage", ((String) timeOnPageMap.get("Average")).toString());
						url_view_doc_object.put("MinTimeOnPage", ((String) timeOnPageMap.get("Min")).toString());
						url_view_doc_object.put("MaxTimeOnPage", ((String) timeOnPageMap.get("Max")).toString());
						url_view_doc_object.put("TotalTimeOnPage", ((String) timeOnPageMap.get("Sum")).toString());
						Map<String, String> sessionCountMap = ArrayListOperationsForIntegerValue(sessionCountArrList);
						System.out.println("sessionCountMap : " + sessionCountMap);
						url_view_doc_object.put("Last_Session_Count", sessionCountMap.get("Max"));
						url_view_doc_object.put("No_OF_Clicks",
								Integer.parseInt((String) sessionCountMap.get("Count")));
						Map<String, String> sessionDurationBucketMap = ArrayListOperationsForIntegerValue(
								sessionDurationBucket);
						System.out.println("sessionDurationBucketMap : " + sessionDurationBucketMap);
						url_view_doc_object.put("AvgSesionDuration",
								((String) sessionDurationBucketMap.get("Average")).toString());
						url_view_doc_object.put("MinSesionDuration",
								((String) sessionDurationBucketMap.get("Min")).toString());
						url_view_doc_object.put("MaxSesionDuration",
								((String) sessionDurationBucketMap.get("Max")).toString());
						url_view_doc_object.put("TotalSesionDuration",
								((String) sessionDurationBucketMap.get("Sum")).toString());
						finalDocumentsArrayForURLView.add(url_view_doc_object);
						Bson filter = Filters.eq("_id", "");
						new Document("$set", url_view_doc_object);
						UpdateOptions options = (new UpdateOptions()).upsert(true);
						Document modifiedObject = new Document();
						modifiedObject.put("$inc", (new BasicDBObject()).append("No_OF_Clicks", 5));
						url_view_collection.updateOne(url_view_doc_object, modifiedObject, options);
					}

					Bson filter = Filters.and(new Bson[] { Filters.eq("sourceMedium", sourceMedium),
							Filters.eq("dimension2", subscriber_email) });
					Map<String, String> AvgSessionDurationBucketMap = GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId(
							collection, filter);
					System.out.println("AvgSessionDurationBucketMap : " + AvgSessionDurationBucketMap);
					System.out.println("campaign_id : " + campaign_id);
				}
			} finally {
				sourceMediumCursor.close();
			}
		} catch (Exception var45) {
			var45.printStackTrace();
			System.out.println("Exception : " + var45.getMessage());
		} finally {
			database = null;
			collection = null;
		}

		return funnelListJsonArr;
	}

	public static org.json.simple.JSONObject fetchGADataForMonitoring(String coll_name, String campaign_id,
			String SubFunnel_Name, String Funnel_Name) {
		MongoDatabase database = null;
		MongoCollection<Document> google_analytics_data_temp = null;
		MongoCollection<Document> google_analytics_url_view_collection = null;
		JSONArray pagePathJsonArr = null;
		new JSONArray();
		String pagePath = null;
		String subscriber_email = null;
		ArrayList<Double> timeOnPageArrList = null;
		ArrayList<Double> avgTimeOnPageArrList = null;
		ArrayList<Integer> sessionCountArrList = null;
		ArrayList<String> dateHourMinuteArrList = null;
		ArrayList<Integer> sessionDurationBucket = null;
		ArrayList<String> hostNameArrList = null;
		TreeSet<String> UniqueSubscribers = new TreeSet();
		org.json.JSONArray finalJsonArrayForRuleEngine = new org.json.JSONArray();
		JSONObject rule_json_object = null;
		String source = null;
		Map<String, String> campaignDetailsMap = null;
		String Source = null;
		String Subscriber_Email = null;
		String ga_user = null;
		String hostname = null;
		String sourceMedium = null;
		String url = null;
		double TotalTimeOnPage = 0;
		int NoOfUrlClicks = 0;
		int TotalSesionDuration = 0;
		int SessionCount = 0;
		String Created_By = null;
		String Category = null;
		String Campaign_Id = null;
		String List_Id = null;
		String Subscriber_Id = null;
		String lastclick = null;
		double AvgTimeOnPage = 0.0D;
		double MinTimeOnPage = 0.0D;
		double MaxTimeOnPage = 0.0D;
		int LastSessionCount = 0;
		double AvgSesionDuration = 0.0D;
		int MinSesionDuration = 0;
		int MaxSesionDuration = 0;
		String firstclick = null;
		ArrayList<Integer> LastSessionCountArrList = null;
		ArrayList<Double> AvgSesionDurationArrList = null;
		logger.info("Calling fetchGADataForRuleEngine : ");
		MongoClientURI connectionString = null;
		org.json.simple.JSONObject finaljson = new org.json.simple.JSONObject();
		JSONArray finajsa = new JSONArray();

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			google_analytics_data_temp = database.getCollection("google_analytics_recent_data_temp");
			google_analytics_url_view_collection = database.getCollection("google_analytics_url_view_collection");
			DistinctIterable<String> dimension2Di = google_analytics_data_temp.distinct("dimension2", String.class);
			MongoCursor dimension2Cursor = dimension2Di.iterator();

			try {
				while (dimension2Cursor.hasNext()) {
					subscriber_email = ((String) dimension2Cursor.next()).toString();
					if (subscriber_email.contains("@")) {
						UniqueSubscribers.add(subscriber_email);
					}
				}
			} finally {
				dimension2Cursor.close();
			}

			System.out.println("Total Number of Subscribers Found  : " + UniqueSubscribers);
			logger.info("Total Number of Subscribers Found  : " + UniqueSubscribers.size());
			Iterator var59 = UniqueSubscribers.iterator();

			while (var59.hasNext()) {
				String temp_subscriber_email = (String) var59.next();
				System.out.println("temp_subscriber_email : " + temp_subscriber_email);
				Bson filter2 = null;
				new ArrayList();
				new ArrayList();
				new ArrayList();
				new ArrayList();
				new ArrayList();
				new ArrayList();
				filter2 = Filters.and(new Bson[] { Filters.eq("Subscriber_Email", temp_subscriber_email),
						Filters.eq("Campaign_Id", campaign_id), Filters.eq("SubFunnel_Name", SubFunnel_Name),
						Filters.eq("Funnel_Name", Funnel_Name) });
				logger.info("Total Number of Subscribers Found  : 12");

				try {
					FindIterable<Document> campaignWisePagePathFi = google_analytics_url_view_collection.find(filter2);
					MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
					int count = 0;
					System.out.println("campaignWisePagePath : 11");
					rule_json_object = new JSONObject();

					while (campaignWisePagePathCursor.hasNext()) {
						try {
							logger.info("Total Number of Subscribers Found  : 12333");
							System.out.println("campaignWisePagePath count:qq " + count);
							Document campaignWisePagePath = (Document) campaignWisePagePathCursor.next();
							System.out.println("campaignWisePagePath count: " + count);
							System.out.println("campaignWisePagePath : " + campaignWisePagePath);
							if (count == 0) {
								Source = campaignWisePagePath.getString("Source");
								Subscriber_Email = campaignWisePagePath.getString("Subscriber_Email");
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
								rule_json_object.put("CreatedBy", Created_By);
								rule_json_object.put("FunnelName", Funnel_Name);
								rule_json_object.put("SubFunnelName", SubFunnel_Name);
								rule_json_object.put("Category", SubFunnel_Name);
								rule_json_object.put("CampaignId", Campaign_Id);
								rule_json_object.put("ListId", List_Id);
								rule_json_object.put("SubscriberId", Subscriber_Id);
								System.out.println("rule_json_object= " + rule_json_object);
							}

							AvgTimeOnPage = campaignWisePagePath.getDouble("AvgTimeOnPage");
							System.out.println("AvgTimeOnPage" + AvgTimeOnPage);
							System.out.println("After Merge pagePath---- : " + pagePath);
							rule_json_object.put(pagePath, AvgTimeOnPage);
							System.out.println("After Merge rule_json_object : " + rule_json_object);
							((ArrayList) AvgSesionDurationArrList)
									.add(campaignWisePagePath.getDouble("AvgSesionDuration"));
							((ArrayList) LastSessionCountArrList)
									.add(campaignWisePagePath.getInteger("LastSessionCount"));
							finalJsonArrayForRuleEngine.put(campaignWisePagePath.toJson());
							++count;
						} catch (Exception var76) {
							System.out.println("excc" + var76);
						}
					}
				} catch (Exception var77) {
					System.out.println("ee-- " + var77);
				}

				Map<String, String> AvgSesionDurationMap = ArrayListOperationsForDoubleValue(
						(ArrayList) AvgSesionDurationArrList);
				rule_json_object.put("AvgSesionDuration", AvgSesionDurationMap.get("Average"));
				Map<String, String> LastSessionCountMap = ArrayListOperationsForIntegerValue(
						(ArrayList) LastSessionCountArrList);
				rule_json_object.put("SessionCount", LastSessionCountMap.get("Max"));
				System.out.println("After Merge rule_json_object : " + rule_json_object);
				logger.info("Call  rule_json_object as INPUT : " + rule_json_object + "      ::::::: Created_By ::::"
						+ Created_By + ":: Funnel_Name :: " + Funnel_Name);
				finajsa.add(rule_json_object);
				logger.info("Call  rule_json_object as finajsa : " + finajsa + "      ::::::: Created_By ::::"
						+ Created_By + ":: Funnel_Name :: " + Funnel_Name);
				finaljson.put("ActiveUsers", finajsa);
			}
		} catch (Exception var79) {
			logger.info("exc : " + var79);
			var79.printStackTrace();
		} finally {
			database = null;
			google_analytics_data_temp = null;
			google_analytics_url_view_collection = null;
		}

		return finaljson;
	}

	public static String insertruleDataforMonitoring(String rulejssave, String SubscriberEmail, String subfunnel,
			String Funnel_Name, String campaign_id, String createdby) {
		String mainfunnel = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		Document document = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection("RuleEngineCalledForSubscriberData");
			logger.info("RuleEngineCalledForSubscriberData  insertruleDataforMonitoring rulejssave : " + rulejssave);
			document = Document.parse(rulejssave);
			if (!Funnel_Name.contains("_EC_") && !Funnel_Name.contains("_EnC_") && !Funnel_Name.contains("_IC_")
					&& !Funnel_Name.contains("_WC_") && !Funnel_Name.contains("_CC_")) {
				mainfunnel = Funnel_Name;
			} else {
				mainfunnel = Funnel_Name.substring(0, Funnel_Name.lastIndexOf("_"));
				mainfunnel = Funnel_Name.substring(0, mainfunnel.lastIndexOf("_"));
			}

			Bson searchQuery = (new Document()).append("SubscriberEmail", SubscriberEmail).append("Category", subfunnel)
					.append("FunnelName", mainfunnel).append("Campaign_id", campaign_id)
					.append("Created_By", createdby);
			collection.updateOne(searchQuery, new Document("$set", document), (new UpdateOptions()).upsert(true));
			logger.info("inserted");
		} catch (Exception var14) {
			logger.info("exc insertruleDataforMonitoring : " + var14);
		} finally {
			collection = null;
			database = null;
		}

		return "inserted";
	}

	public static String updateDataSourceforMoveSubscriber(String subscriberemail, String CreatedBy, String funnelName,
			String category, String group, String original_category) {
		String[] monthArray = new String[] { "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct",
				"nov", "dec" };
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> firstcatcollection = null;
		Bson document = null;
		String campaignid = null;
		int schedulegap = 1;
		Date set_date = null;
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date dateobj = new Date();
		String Current_Date = df.format(dateobj);

		try {
			set_date = df.parse(Current_Date);
		} catch (ParseException var60) {
			;
		}

		set_date.setDate(set_date.getDate() + schedulegap);
		String formatdate = df.format(set_date);
		ListIterator datasrcitr = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			new org.json.JSONArray();
			org.json.simple.JSONObject emailjson = new org.json.simple.JSONObject();
			emailjson.put("email", subscriberemail);
			collection = database.getCollection("OtherCategoryMails");
			firstcatcollection = database.getCollection("FirstCategoryMails");
			new Document();
			Bson filter1 = null;
			Bson filter2 = Filters
					.and(new Bson[] { Filters.eq("CreatedBy", CreatedBy), Filters.eq("funnelName", funnelName),
							Filters.eq("Category", category), Filters.eq("group", group) });
			Bson filter3 = Filters
					.and(new Bson[] { Filters.eq("CreatedBy", CreatedBy), Filters.eq("funnelName", funnelName),
							Filters.eq("Category", original_category), Filters.eq("group", group) });
			logger.info("filter3   : " + filter3);
			logger.info("updateDataSourceforMoveSubscriber   : ");
			FindIterable<Document> filerdata = firstcatcollection.find(filter2);
			MongoCursor<Document> monitordata = filerdata.iterator();
			Document datsrc_doc = null;
			String mainfunnel = null;

			while (monitordata.hasNext()) {
				Document categoryWisedata = (Document) monitordata.next();
				logger.info("categoryWisedata   : " + categoryWisedata);

				try {
					String schedulegapformultiplecamp = null;
					JSONObject data_json_obj = null;
					campaignid = categoryWisedata.get("Campaign_id").toString();
					if (!funnelName.contains("_EC_") && !funnelName.contains("_EnC_") && !funnelName.contains("_IC_")
							&& !funnelName.contains("_WC_") && !funnelName.contains("_CC_")) {
						mainfunnel = funnelName;
					} else {
						mainfunnel = funnelName.substring(0, funnelName.lastIndexOf("_"));
						mainfunnel = funnelName.substring(0, mainfunnel.lastIndexOf("_"));
					}

					List<Document> datasourcesarr = getContactarrofstartcatg("Explore", "SentExploreContacts",
							CreatedBy, mainfunnel);
					org.json.JSONArray dataarr = new org.json.JSONArray();
					boolean moveOut = false;
					datasrcitr = datasourcesarr.listIterator();

					while (datasrcitr.hasNext()) {
						datsrc_doc = (Document) datasrcitr.next();
						//logger.info("datsrc_doc.toJson()   : " + datsrc_doc.toJson());
						data_json_obj = new JSONObject(datsrc_doc.toJson());
						if (data_json_obj.has("Email")
								&& subscriberemail.equalsIgnoreCase(data_json_obj.getString("Email").trim())) {
							logger.info("value   : " + data_json_obj);
							dataarr.put(data_json_obj);
							moveOut = true;
						}

						if (moveOut) {
							String res1 = Updateschedulegapdateformultiplecamp(category, CreatedBy, mainfunnel,
									firstcatcollection);
							BasicDBObject querypull = new BasicDBObject();
							querypull.put("CreatedBy", CreatedBy);
							querypull.put("funnelName", mainfunnel);
							BasicDBObject fieldspull = new BasicDBObject("SentEmailList",
									new BasicDBObject("Email", subscriberemail));
							BasicDBObject update = new BasicDBObject("$pull", fieldspull);
							firstcatcollection.updateOne(querypull, update);
							filter1 = Filters.and(new Bson[] { Filters.eq("CreatedBy", CreatedBy),
									Filters.eq("Campaign_id", campaignid), Filters.eq("funnelName", funnelName),
									Filters.eq("Category", category), Filters.eq("group", group) });
							logger.info(res1 + "moveOut    : " + data_json_obj);
							Document doc = Document.parse(data_json_obj.toString());
							collection.updateOne(filter1, Updates.addToSet("Contacts", doc),
									(new UpdateOptions()).upsert(true));
							org.json.simple.JSONObject email_json_obj = new org.json.simple.JSONObject();
							email_json_obj.put("Email", subscriberemail);
							BasicDBObject dbObject = (BasicDBObject) JSON.parse(email_json_obj.toString());
							BasicDBObject updateQuery3 = new BasicDBObject("$addToSet",
									new BasicDBObject("MoveEmailList", dbObject));
							firstcatcollection.updateOne(filter3, updateQuery3, (new UpdateOptions()).upsert(true));
							Document emaillist = (new Document()).append("email", subscriberemail);
							Bson filter4 = Filters.and(new Bson[] { Filters.eq("CreatedBy", CreatedBy),
									Filters.eq("funnelName", mainfunnel), Filters.eq("Category", category),
									Filters.eq("group", group) });
							Bson filtermonth = Filters.and(new Bson[] { Filters.eq("CreatedBy", CreatedBy),
									Filters.eq("funnelName", mainfunnel), Filters.eq("Category", "Explore"),
									Filters.eq("group", group) });
							firstcatcollection.updateOne(filter4, Updates.addToSet("SentEmailList", emaillist),
									(new UpdateOptions()).upsert(true));
							Document camp_details_doc = new Document();
							camp_details_doc.put("ActivateDate", formatdate);
							Date date1 = df.parse(Current_Date);
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(date1);
							int monthno = calendar.get(2);
							logger.info("Current_Date    : " + Current_Date);
							String Week = GetWeekFromDate(Current_Date);
							String monthofactivedate = monthArray[monthno];
							BasicDBObject updateQuerymonth = new BasicDBObject("$addToSet",
									new BasicDBObject(monthofactivedate, dbObject));
							BasicDBObject updateQueryweek;
							if (!category.equals("Connect")) {
								firstcatcollection.updateOne(filtermonth, updateQuerymonth,
										(new UpdateOptions()).upsert(true));
							} else {
								updateQueryweek = new BasicDBObject("$addToSet",
										new BasicDBObject("connect" + monthofactivedate, dbObject));
								firstcatcollection.updateOne(filtermonth, updateQueryweek,
										(new UpdateOptions()).upsert(true));
							}

							updateQueryweek = new BasicDBObject("$addToSet",
									new BasicDBObject(Week + "leadcount", dbObject));
							firstcatcollection.updateOne(filter4, updateQueryweek, (new UpdateOptions()).upsert(true));
							firstcatcollection.updateOne(filter2, new Document("$set", camp_details_doc),
									(new UpdateOptions()).upsert(true));
							BasicDBObject updateQuery4 = new BasicDBObject("$addToSet",
									new BasicDBObject("campaignleadEmailList", dbObject));
							firstcatcollection.updateOne(filter2, updateQuery4, (new UpdateOptions()).upsert(true));
							break;
						}
					}
				} catch (Exception var61) {
					logger.info("Exception    : " + var61);
					var61.printStackTrace();
				}
			}
		} catch (Exception var62) {
			logger.info("Exception    : " + var62);
			var62.printStackTrace();
		} finally {
			database = null;
			collection = null;
			firstcatcollection = null;
		}

		return "success";
	}

	public static String getOldcategoryName(String category) {
		String oldcategory = null;
		HashMap<String, String> category_mapnew = new HashMap();
		category_mapnew.put("Unknown", "Explore");
		category_mapnew.put("Cold", "Entice");
		category_mapnew.put("Warm", "Inform");
		category_mapnew.put("Hot", "Warm");
		category_mapnew.put("Connect", "Connect");
		if (category_mapnew.containsKey(category)) {
			oldcategory = (String) category_mapnew.get(category);
			logger.info("value for key " + category + " is oldcategory : " + oldcategory);
		}

		return oldcategory;
	}

	public static List<Document> getContactarrofstartcatg(String category, String arrayname, String CreatedBy,
			String mainfunnel) throws IOException {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		String schedulegap = null;
		MongoCursor<Document> monitordata = null;
		String scheduletime = null;
		List contactarr = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
			if (mainfunnel != null) {
				Bson updtfiltercount = Filters.and(new Bson[] { Filters.eq("Category", category),
						Filters.eq("funnelName", mainfunnel), Filters.eq("CreatedBy", CreatedBy) });
				FindIterable<Document> filerdata = collection.find(updtfiltercount);
				monitordata = filerdata.iterator();
				Document datsrc_doc = null;
				if (mainfunnel != null && mainfunnel != "" && monitordata.hasNext()) {
					datsrc_doc = (Document) monitordata.next();
					contactarr = (List) datsrc_doc.get(arrayname);
				}
			}
		} catch (Exception var16) {
			;
		} finally {
			database = null;
			collection = null;
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
			int weekno = calendar.get(4);
			Week = "week" + Integer.toString(weekno);
		} catch (Exception var6) {
			System.out.println(var6);
		}

		return Week;
	}

	public static String updateMoveCategory(String SubscriberEmail, String movetocategory, String Funnel_Name,
			String createdby, String originalcateg, String campaign_id) {
		MongoDatabase database = null;
		MongoCollection collection = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection("RuleEngineCalledForSubscriberData");
			Document categ_doc = new Document();
			categ_doc.put("MoveToCategory", movetocategory);
			Bson searchQuery = (new Document()).append("SubscriberEmail", SubscriberEmail)
					.append("Category", originalcateg).append("ChildFunnelName", Funnel_Name)
					.append("Campaign_id", campaign_id).append("Created_By", createdby);
			logger.info("searchQuery= " + searchQuery);
			collection.updateOne(searchQuery, new Document("$set", categ_doc), (new UpdateOptions()).upsert(true));
			logger.info("move cat inserted");
		} catch (Exception var13) {
			logger.info("exc insertruleDataforMonitoring : " + var13);
		} finally {
			database = null;
			collection = null;
		}

		return "move";
	}

	public static String CheckRuleOPISCategOrFunnel(String category) {
		String opcategory = null;
		HashMap<String, String> category_mapnew = new HashMap();
		category_mapnew.put("Unknown", "Explore");
		category_mapnew.put("Cold", "Entice");
		category_mapnew.put("Warm", "Inform");
		category_mapnew.put("Hot", "Warm");
		category_mapnew.put("Connect", "Connect");
		if (category_mapnew.containsKey(category)) {
			opcategory = "category";
		} else {
			opcategory = "funnel";
		}

		logger.info("value for key " + category + " is oldcategory : " + opcategory);
		return opcategory;
	}

	public static String moveContacts(String destinationFunnel, String emailobj, String createdby, String group) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		String res = null;
		destinationFunnel = destinationFunnel.trim();

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
			Bson query = Filters.and(new Bson[] { Filters.eq("funnelName", destinationFunnel),
					Filters.eq("CreatedBy", createdby), Filters.eq("Category", "Explore") });
			org.json.simple.JSONObject data_json_obj = new org.json.simple.JSONObject();
			data_json_obj.put("Email", emailobj);
			if (!isNullString(emailobj)) {
				try {
					BasicDBObject dbObject = (BasicDBObject) JSON.parse(data_json_obj.toString());
					Document newDocument = new Document();
					newDocument.put("updateflag", "1");
					Document updateObj = new Document();
					updateObj.put("$set", newDocument);
					collection.updateOne(query, updateObj);
					BasicDBObject updateQuery1 = new BasicDBObject("$addToSet",
							new BasicDBObject("MasterContacts", dbObject));
					collection.updateOne(query, updateQuery1, (new UpdateOptions()).upsert(true));
					BasicDBObject updateQuery = new BasicDBObject("$addToSet", new BasicDBObject("Contacts", dbObject));
					collection.updateOne(query, updateQuery, (new UpdateOptions()).upsert(true));

					try {
						Bson updtfiltercount = Filters.and(new Bson[] { Filters.eq("funnelName", destinationFunnel),
								Filters.eq("CreatedBy", createdby), Filters.eq("group", group),
								Filters.eq("Category", "Explore") });
						MongoCursor<Document> monitordata = null;
						FindIterable<Document> filerdata = collection.find(updtfiltercount);
						monitordata = filerdata.iterator();
						Document datsrc_doc = null;
						String Trafic = null;
						String newmailsent = null;
						JSONObject js = null;
						int newmailcount = 0;
						if (monitordata.hasNext()) {
							datsrc_doc = (Document) monitordata.next();
							js = new JSONObject(datsrc_doc.toJson());

							if (js.has("MasterContacts")) {
								newmailcount = js.getJSONArray("MasterContacts").length();
							} else {
								newmailcount = js.getJSONArray("Contacts").length();
							}

							newmailsent = Integer.toString(newmailcount);
						}

						Document subfunnelcountdoc = new Document();
						subfunnelcountdoc.put("rawleads", newmailsent);
						collection.updateOne(updtfiltercount, new Document("$set", subfunnelcountdoc),
								(new UpdateOptions()).upsert(true));
					} catch (Exception var28) {
						;
					}
				} catch (Exception var29) {
					var29.printStackTrace();
				}
			}

			res = "Success";
		} catch (Exception var30) {
			res = "Fail";
		} finally {
			database = null;
			collection = null;
		}

		return res;
	}

	public static String Updateschedulegapdateformultiplecamp(String category, String CreatedBy, String mainfunnel,
			MongoCollection<Document> collection) throws IOException {
		Date dateobj = new Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
		String Current_Date = df.format(dateobj);
		String formatdate = null;
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yy");
		String Week = null;
		Date set_date = null;

		try {
			set_date = (new SimpleDateFormat("dd-MM-yy")).parse(Current_Date);
		} catch (ParseException var32) {
			;
		}

		MongoDatabase database = null;
		String schedulegap = null;
		MongoCursor<Document> monitordata = null;
		JSONObject docjs = null;
		String scheduletime = null;
		Object var16 = null;

		try {
			String getgap = null;
			int gapday = 0;
			Bson updtfiltercount = Filters.and(new Bson[] { Filters.eq("Category", category),
					Filters.eq("Parentfunnel", mainfunnel), Filters.eq("CreatedBy", CreatedBy) });
			FindIterable<Document> filerdata = collection.find(updtfiltercount);
			monitordata = filerdata.iterator();
			Document camp_details_doc = null;
			Document datsrc_doc = null;
			String funnelName = null;
			logger.info("=updtfiltercount= " + category + mainfunnel + CreatedBy);

			while (monitordata.hasNext()) {
				try {
					datsrc_doc = (Document) monitordata.next();
					docjs = new JSONObject(datsrc_doc.toJson());
					logger.info("=docjs= " + docjs);
					funnelName = docjs.getString("funnelName");
					logger.info("=funnelName= " + funnelName);
					if ((funnelName.contains("_EC_") || funnelName.contains("_EnC_") || funnelName.contains("_IC_")
							|| funnelName.contains("_WC_") || funnelName.contains("_CC_"))
							&& !docjs.has("ScheduleGapDate")) {
						mainfunnel = funnelName.substring(0, funnelName.lastIndexOf("_"));
						mainfunnel = funnelName.substring(0, mainfunnel.lastIndexOf("_"));
						getgap = docjs.getString("week");
						logger.info("=getgap= " + getgap);

						if (getgap.contains("Week")) {
							gapday = Integer.parseInt(getgap.substring(0, 1));
							gapday *= 7;
						} else if (getgap.contains("day")) {
							gapday = Integer.parseInt(getgap.substring(0, 1));
						} else {
							gapday = 3;
						}

						String campnumber = funnelName.substring(funnelName.lastIndexOf("_") + 1, funnelName.length());
						int setdiff = Integer.parseInt(campnumber) - 1;
						gapday *= setdiff;
						schedulegap = Integer.toString(gapday);
						logger.info(gapday + "=gapdays schedulegapdate = " + schedulegap);
						set_date.setDate(set_date.getDate() + Integer.parseInt(schedulegap));
						scheduletime = getmainscheduletime("Explore", CreatedBy, mainfunnel, collection);
						formatdate = formatter2.format(set_date);
						logger.info("schedulegapdate = " + formatdate + scheduletime);
						if (!category.equals("Explore")) {
							camp_details_doc = new Document();
							camp_details_doc.put("ScheduleGapDate", formatdate + " " + scheduletime);
							camp_details_doc.put("multipleflag", "5");
							Bson updtfilter = Filters.and(new Bson[] { Filters.eq("Category", category),
									Filters.eq("funnelName", funnelName), Filters.eq("CreatedBy", CreatedBy) });
							logger.info("updtfilter = " + updtfilter);
							collection.updateOne(updtfilter, new Document("$set", camp_details_doc),
									(new UpdateOptions()).upsert(true));
						}
					}
				} catch (Exception var33) {
					logger.info("exc== = " + var33);
				}
			}
		} catch (Exception var34) {
			logger.info("exc= " + var34);
		} finally {
			collection = null;
		}

		return "success";
	}

	public static String getmainscheduletime(String category, String CreatedBy, String mainfunnel,
			MongoCollection<Document> collection) throws IOException {
		String schedulegap = null;
		MongoCursor<Document> monitordata = null;
		String scheduletime = null;

		try {
			if (mainfunnel != null) {
				Bson updtfiltercount = Filters.and(new Bson[] { Filters.eq("Category", category),
						Filters.eq("funnelName", mainfunnel), Filters.eq("CreatedBy", CreatedBy) });
				FindIterable<Document> filerdata = collection.find(updtfiltercount);
				monitordata = filerdata.iterator();
				Document datsrc_doc = null;
				if (mainfunnel != null && mainfunnel != "" && monitordata.hasNext()) {
					datsrc_doc = (Document) monitordata.next();
					scheduletime = datsrc_doc.getString("scheduleTime");
				}
			}
		} catch (Exception var13) {
			;
		} finally {
			collection = null;
		}

		return scheduletime;
	}

	public static String decrypt(String strToDecrypt) {
		Decoder decoder = Base64.getDecoder();
		String decoded = new String(decoder.decode(strToDecrypt));
		System.out.println("Base 64 Decoded  String : " + new String(decoded));
		return decoded;
	}
	public static String encrypt(String strToencrypt) {
	
		String encoded = Base64.getEncoder().encodeToString(strToencrypt.getBytes()); 
	
		return encoded;
	}

	public static boolean isNullString(String p_text) {
		return p_text == null || p_text.trim().length() <= 0 || "null".equalsIgnoreCase(p_text.trim());
	}
	
	public static String insertmailopendata( String SubscriberEmail, String subfunnel,
			String Funnel_Name, String campaign_id, String createdby,String group) {
		String mainfunnel = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		Document document = null;

		try {
			database = MongoDbCache.getInstance().getConnection().getDatabase("salesautoconvert");
			collection = database.getCollection("google_analytics_data_temp");
			
			logger.info("opnemailgoogle_analytics_data_tempinserted");
			//RealEstateAll$$Explore$$FetchmyPaymentTemp$$bizlem$$tanyasharma2615@gmail.com
			String toencrypt=Funnel_Name+"$$"+subfunnel+"$$"+campaign_id+"$$"+group+"$$"+createdby;
			String sourceMedium= GAMongoDAO.encrypt(toencrypt);
			JSONObject savedata=new JSONObject();
			Date d=new Date();
			long l=new Date().getTime();
			savedata.put("dimension2", SubscriberEmail);
			savedata.put("country", "");
			savedata.put("sourceMedium", sourceMedium+" / (not set)");
			savedata.put("dateHourMinute", Long.toString(l));
			savedata.put("channelGrouping", "(Other)");
			savedata.put("sessionCount", "0");
			savedata.put("sessionDurationBucket", "0");
			savedata.put("hostname", "MailOpen");
			savedata.put("timeOnPage", "0");
			savedata.put("ga_username", "bluealgo.ga@gmail.com");
			savedata.put("pagePath",  "MailOpen");
			savedata.put("url",  "MailOpen");
//			savedata.put("MailOpen",  "MailOpen");
			savedata.put("bounces", "0");
			document = Document.parse(savedata.toString());
			collection.insertOne(document);
			logger.info("savedata = "+savedata);
//			Bson searchQuery = (new Document()).append("dimension2", SubscriberEmail).append("sourceMedium", sourceMedium);
//			collection.updateOne(searchQuery, new Document("$set", document), (new UpdateOptions()).upsert(true));
			logger.info("google_analytics_data_tempinserted");
		} catch (Exception var14) {
			logger.info("exc google_analytics_data_tempinserted : " + var14);
		} finally {
			collection = null;
			database = null;
		}

		return "inserted";
	}
	
}