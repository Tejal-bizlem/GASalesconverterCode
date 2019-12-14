package com.db.mongo.ga;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class GetGADataForMonitoringLeads {
	public static void main(String args[]) {

//		String jj = findGAdatapersubscriber("viki@gmail.com", "Explore", "test12", "1520");
//		System.out.println("jj== " + jj);
		JSONObject subscjs=findsubscriberdetails( "mohit.raj@bizlem.com ",  "viki@gmail.com",  "funnel903");
		System.out.println("subscjs"+subscjs);
	

	}

	public static String findGAdatapersubscriber(String CreatedBy, String SubFunnelName, String FunnelName,
			String CampaignId) {
		String resp = "";
		JSONObject subscriber_json_obj = null;

		MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> RuleEngineCalledForSubscriberData = null;
		JSONObject Allsubscrdata = new JSONObject();
		try {

			String uri = "mongodb://localhost:27017/?ssl=true";
	    	System.setProperty("javax.net.ssl.trustStore","/etc/ssl/firstTrustStore");
	    	System.setProperty("javax.net.ssl.trustStorePassword","bizlem123");
	    	System.setProperty ("javax.net.ssl.keyStore","/etc/ssl/MongoClientKeyCert.jks");
	    	System.setProperty ("javax.net.ssl.keyStorePassword","bizlem123");
	    	////MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();
	    	

			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");

			RuleEngineCalledForSubscriberData = database.getCollection("RuleEngineCalledForSubscriberData");

			resp = "start";
			Bson filter2 = null;
			filter2 = and(eq("CreatedBy", CreatedBy), eq("SubFunnelName", SubFunnelName), eq("FunnelName", FunnelName),
					eq("CampaignId", CampaignId));
			FindIterable<Document> filerdata = RuleEngineCalledForSubscriberData.find(filter2);
			MongoCursor<Document> monitordata = filerdata.iterator();

			JSONArray subscriberjsarr = new JSONArray();
			System.out.println("ga_user_json_obj= 99");
			double totalSessiontime = 0;
			String Email = null;
			String recentSessiontime = "0";
			double AlltotalSessiontime = 0;
			double allrecentSessiontime = 0;
			double rst = 0;
			int NoOfUrlClicks=0;
			int TotalClicks=0;
//			resp = resp + "start3 GAEmail: " + GAEmail;
			while (monitordata.hasNext()) {
				resp = resp + "start666";
				try {

					Document campaignWisedata = monitordata.next();
					subscriber_json_obj = new JSONObject();
					Email = campaignWisedata.getString("SubscriberEmail");
					subscriber_json_obj.put("Email", Email);
					totalSessiontime = campaignWisedata.getDouble("TotalSesionDuration");
					subscriber_json_obj.put("totalSessiontime", totalSessiontime);
					AlltotalSessiontime = AlltotalSessiontime + totalSessiontime;
					subscriber_json_obj.put("totalVisits", "0");
					recentSessiontime = campaignWisedata.getString("Recent_AvgSesionDuration");
					rst = Double.parseDouble(recentSessiontime);
					System.out.println("rst=" + rst);
					allrecentSessiontime = allrecentSessiontime + rst;
					subscriber_json_obj.put("recentSessiontime", recentSessiontime);
					subscriber_json_obj.put("recentVisits", "0");
					NoOfUrlClicks=campaignWisedata.getInteger("NoOfUrlClicks");
					TotalClicks=TotalClicks+NoOfUrlClicks;
					try {
					JSONObject subscjs=findsubscriberdetails( Email,  CreatedBy,  FunnelName);
					System.out.println("subscjs"+subscjs);
					subscriber_json_obj.put("Name", subscjs.get("name"));
					subscriber_json_obj.put("Source",  subscjs.get("source"));
					}catch (Exception e) {
						// TODO: handle exception
						subscriber_json_obj.put("Name", "NA");
						subscriber_json_obj.put("Source",  "NA");
					
					}
					subscriberjsarr.add(subscriber_json_obj);
					System.out.println("ga_user_json_obj= " + subscriber_json_obj);
				} catch (Exception e) {
					System.out.println("exc = " + e);
				}
			}
			
			JSONObject totalllead = new JSONObject();
			totalllead.put("totalSessiontime", AlltotalSessiontime);
			totalllead.put("recentSessiontime", allrecentSessiontime);
			totalllead.put("clicked", TotalClicks);
			
			System.out.println("AlltotalSessiontime= " + AlltotalSessiontime);///
			Allsubscrdata.put("CampaignId", CampaignId);
			Allsubscrdata.put("Category", SubFunnelName);
			Allsubscrdata.put("funnelname", FunnelName);
			Allsubscrdata.put("TotalLeadData", totalllead);
			Allsubscrdata.put("ActiveUsers", subscriberjsarr);

//			logger.info("Data for  GoogleAnalytics User Found ga_user_json_obj : " + ga_user_json_obj);
		} catch (Exception e) {
			System.out.println("exc in findGAUserCredentials: " + e);
			Allsubscrdata.put("excmongo", e.toString());
		}
		return resp + ":Allsubscrdata:" + Allsubscrdata.toString();
	}

	public static JSONObject findsubscriberdetails(String subscriberEmail, String CreatedBy, String FunnelName) {

		String resp = "";
		JSONObject subscriber_json_obj = subscriber_json_obj = new JSONObject();
		

		MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> RuleEngineCalledForSubscriberData = null;

		try {

			connectionString = new MongoClientURI("mongodb://localhost:27017");
			mongoClient = new MongoClient(connectionString);
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");

			RuleEngineCalledForSubscriberData = database.getCollection("subscribers_details");

			resp = "start";
			Bson filter2 = null;
			filter2 = and(eq("CreatedBy", CreatedBy),
					eq("EmailAddress", subscriberEmail), eq("FunnelName", FunnelName));//
			FindIterable<Document> filerdata = RuleEngineCalledForSubscriberData.find(filter2);
			MongoCursor<Document> monitordata = filerdata.iterator();
			
			String source = null;
			String name = null;
			while (monitordata.hasNext()) {

				try {

					Document campaignWisedata = monitordata.next();
					System.out.println("email= "+campaignWisedata);
					name = campaignWisedata.getString("FirstName");
					source = campaignWisedata.getString("Source");
					subscriber_json_obj.put("name", name);
					subscriber_json_obj.put("subscriberEmail", subscriberEmail);

					subscriber_json_obj.put("source", source);
				} catch (Exception e) {
					System.out.println("exc = " + e);
				}
			}

//			logger.info("Data for  GoogleAnalytics User Found ga_user_json_obj : " + ga_user_json_obj);
		} catch (Exception e) {
			System.out.println("exc in findGAUserCredentials: " + e);
			subscriber_json_obj.put("excmongo", e.toString());

		}
		return subscriber_json_obj;
	}
}
