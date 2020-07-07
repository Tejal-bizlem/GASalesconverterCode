package com.rest.api;

import com.db.mongo.ga.AnalyticsDataInsertUpdate;
import com.db.mongo.ga.CallGetService;
import com.db.mongo.ga.GAMongoDAO;
import com.db.mongo.ga.GetTokenForVerifiedWebsites;
import com.google.api.AccesAndRefreshToken;
import com.leadconverter.ga.GoogleAnalyticsReportingSample;
import com.leadconverter.quartz.scheduler.QuartzScheduler;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Path("biz")
public class GoogleAnalayticsService {
	@GET
	@Produces({"text/xml"})
	public String sayHello() {
		String resource = "<? xml version='1.0' ?><hello>hI this is sample xml rsponse</hello>";
		return resource;
	}

	@GET
	@Produces({"text/plain"})
	public String sayHelloPlain() {
		String resource = "hI this is sample text rsponse";
		return resource;
	}

	@GET
	@Produces({"application/json"})
	public String sayHelloJSON() {
		String resource = "{'name':'Akhilesh'}";
		return resource;
	}

	@GET
	@Produces({"text/html"})
	public String sayHelloHTML() {
		String resource = "<h1>Hi Akhiles, This is demo html response</h1>";
		return resource;
	}

	@GET
	@Path("{year}/{month}/{day}")
	public Response getDate(@PathParam("year") int year, @PathParam("month") int month, @PathParam("day") int day) {
		String date = year + "/" + month + "/" + day;
		return Response.status(200).entity("getDate is called, year/month/day : " + date).build();
	}

	@GET
	public Response pingMe() {
		String str = "How are you?";
		return Response.status(200).entity(str).build();
	}

	@GET
	@Path("/birthday")
	public Response printBdayMsg() {
		String str = "Happy Birthday!";
		return Response.status(200).entity(str).build();
	}

	@GET
	@Path("/getGAReport")
	public Response getGAReport() {
		String str = "Happy Birthday!";
		String str_start_date = "2019-01-21";
		String str_end_date = "2019-01-30";
		String accessToken = "";
		String view_id = "";
		String res = GoogleAnalyticsReportingSample
				.getGAReport("input", accessToken, str_start_date, str_end_date, view_id).toString();
		return Response.status(200).entity(res).build();
	}

	@POST
	@Path("/getGARByTkn")
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response getGARByTkn(InputStream incomingData) {
		StringBuilder crunchifyBuilder = new StringBuilder();
		long count = 0L;
		JSONObject json_obj = null;
		String str = null;

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;

			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}

			System.out.println("Data Received: " + crunchifyBuilder.toString());
			JSONParser parser = new JSONParser();
			JSONObject request_json_obj = (JSONObject) parser.parse(crunchifyBuilder.toString());
			String str_start_date = request_json_obj.get("str_start_date").toString();
			String str_end_date = request_json_obj.get("str_end_date").toString();
			String accessToken = request_json_obj.get("accessToken").toString();
			String view_id = request_json_obj.get("accessToken").toString();
			str = GoogleAnalyticsReportingSample
					.getGAReport("input", accessToken, str_start_date, str_end_date, view_id).toString();
		} catch (Exception var15) {
			System.out.println("Error Parsing: - ");
		}

		return Response.status(200).entity(str.toString()).build();
	}

	@POST
	@Path("/getGAR")
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response getGAR(InputStream incomingData) {
		StringBuilder crunchifyBuilder = new StringBuilder();
		long count = 0L;
		JSONObject json_obj = null;
		String str = "Not Success";

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;

			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}

			JSONParser parser = new JSONParser();
			JSONObject request_json_obj = (JSONObject) parser.parse(crunchifyBuilder.toString());
			String str_start_date = request_json_obj.get("str_start_date").toString();
			String str_end_date = request_json_obj.get("str_end_date").toString();
			String accessToken = request_json_obj.get("accessToken").toString();
			String refreshToken = request_json_obj.get("refreshToken").toString();
			String username = request_json_obj.get("username").toString();
			String view_name = request_json_obj.get("view_name").toString();
			String view_id = request_json_obj.get("view_id").toString();
			json_obj = new JSONObject();
			str = AnalyticsDataInsertUpdate.saveGAUserEmailAndRefreshTokenData(username, accessToken, refreshToken,
					view_name, view_id);
			json_obj.put("status", str);
		} catch (Exception var18) {
			System.out.println("Error Parsing: 1 - " + var18.getMessage());
		}

		return Response.status(200).entity(json_obj.toString()).build();
	}

	@GET
	@Path("/insertGADataInMongoDB")
	public Response insertGADataInMongoDB() {
		JSONArray ga_users_json_arr = AnalyticsDataInsertUpdate.findGAUserCredentials();
		AccesAndRefreshToken.getGAData(ga_users_json_arr);
		GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
		String str = "Insert GAData In MongoDB!";
		return Response.status(200).entity(str).build();
	}

	@GET  
	@Path("/fetchGADataInsertInMongoDB")
	public Response fetchGADataInsertInMongoDB() {
		JSONArray ga_users_json_arr = AnalyticsDataInsertUpdate.findGAUserCredentials();
		AccesAndRefreshToken.getGAData(ga_users_json_arr);
		GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
		GAMongoDAO.fetchGADataForRuleEngine("google_analytics_data_temp");
		String str = "Insert GAData In MongoDB!";
		return Response.status(200).entity(str).build();
	}

	@GET
	@Path("/callScheduler/{hr}/{minute}/{flag}")
	public Response callScheduler(@PathParam("hr") String hr, @PathParam("minute") String minute,
			@PathParam("flag") String flag) {
		String date = hr + "/" + minute + "/" + flag;
		String hr1 = "1";
		String flag1 = "true";
		QuartzScheduler qtzscheduler = new QuartzScheduler();
		qtzscheduler.runJobs(hr1, flag1);
		String str = "Scheduler Called!" + date;
		return Response.status(200).entity(str).build();
	}

	@GET
	@Path("/callGAdataFetch")
	public String callGAFetch() {
		JSONObject jsresp = new JSONObject();
		String resp = "";

		try {
			Date d = new Date();
			resp = d.toString();
			try {
			JSONArray ga_users_json_arr = AnalyticsDataInsertUpdate.findGAUserCredentials();
			AccesAndRefreshToken.getGAData(ga_users_json_arr);
			jsresp.put("tokenarray", ga_users_json_arr);
			}catch (Exception e) {
				// TODO: handle exception
			}
			try {
			GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
			}catch (Exception e) {
				// TODO: handle exception
			}try {
			GAMongoDAO.fetchGADataForRuleEngine("google_analytics_data_temp");
			}catch (Exception e) {
				// TODO: handle exception
			}
		
			resp = jsresp.toString();
			return resp;
		} catch (Exception var5) {
			return resp + var5.toString();
		}
	}

	@GET
	@Path("/saveurlviewdata")
	public String saveurlview() {
		String resp = "";

		try {
			Date d = new Date();
			resp = d.toString();
			GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
			GAMongoDAO.fetchGADataForRuleEngine("google_analytics_data_temp");
			return resp;
		} catch (Exception var3) {
			return resp + var3.toString();
		}
	}
	
	// https://test.bluealgo.com:8088/GASalesConvTest/rest/biz/callfirerule
//test.bluealgo.com:8087/GASalesConverter/rest/biz/callfirerule
	// https://test.bluealgo.com:8088/GASalesConverter/rest/biz/callGAdataFetch
	//  http://test.bluealgo.com:8087/GASalesConverter/rest/biz/callfirerule
	@GET
	@Path("/callfirerule")
	public String fireurlview() {
		String resp = "";

		try {
//			Date d = new Date();
//			resp = d.toString();
//
//			try {
//				org.json.JSONArray rulengarr = null;
//				StringBuilder var10000 = new StringBuilder("resp ");
//				new CallGetService();
//				resp = var10000.append(CallGetService.checkfreetrial("")).toString();
//			} catch (Exception var4) {
//				var4.printStackTrace();
//			}
			GAMongoDAO.fetchGADataForRuleEngine("google_analytics_data_temp");
			return resp;
		} catch (Exception var5) {
			return resp + var5.toString();
		}
	}

	@GET
	@Path("/getGAdatatotemp")
	public String savegadatatemp() {
		String resp = "";

		try {
			Date d = new Date();
			resp = d.toString();
			JSONArray ga_users_json_arr = AnalyticsDataInsertUpdate.findGAUserCredentials();
			AccesAndRefreshToken.getGAData(ga_users_json_arr);
			resp = ga_users_json_arr.toString();
			return resp;
		} catch (Exception var4) {
			return resp + var4.toString();
		}
	}

	@GET
	@Path("/VerifyWebsiteInGASendAlert12/{GAEmail}/{websiteurl}/{ToEmail}")
	public String callVerifyWebsite(@PathParam("GAEmail") String GAEmail, @PathParam("websiteurl") String websiteurl,
			@PathParam("ToEmail") String ToEmail) {
		String resp = "1";
		resp = GetTokenForVerifiedWebsites.VerifyWebsites(GAEmail, websiteurl, ToEmail);
		return resp;
	}
// /updateTrackingdata
	// https://test.bluealgo.com:8088/GASalesConvTest/rest/biz/updateTrackingdata
	@POST
	@Path("/updateTrackingdata")
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public String updateTrackingdata(InputStream incomingData) {
		StringBuilder crunchifyBuilder = new StringBuilder();
		Object var3 = null;

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;

			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}

			System.out.println("Data Received: " + crunchifyBuilder.toString());
			org.json.JSONObject obj = new org.json.JSONObject(crunchifyBuilder.toString());
			System.out.println("obj : " + obj);
			org.json.JSONArray dataarr = obj.getJSONArray("data");

			for (int i = 0; i < dataarr.length(); ++i) {
				String SubFunnelName = "";
				String FunnelName = "";
				String SubscriberEmail = "";
				String campaignname = "";
				String Campaign_id = "";
				String Created_By = "";
				String group = "";
				String ip = "";
				String opentime = "";
				org.json.JSONObject subobj = dataarr.getJSONObject(i);
				if (subobj.has("FunnelName")) {
					FunnelName = subobj.getString("FunnelName");
					subobj.put("ChildFunnelName", FunnelName);
				}

				if (subobj.has("SubFunnelName")) {
					SubFunnelName = subobj.getString("SubFunnelName");
				}

				if (subobj.has("SubscriberEmail")) {
					SubscriberEmail = subobj.getString("SubscriberEmail");
				}

				if (subobj.has("campaignname")) {
					campaignname = subobj.getString("campaignname");
				}

				if (subobj.has("Campaign_id")) {
					Campaign_id = subobj.getString("Campaign_id");
				}

				if (subobj.has("Created_By")) {
					Created_By = subobj.getString("Created_By");
				}
				String mainfunnel=FunnelName;

				if (FunnelName.contains("_EC_") || FunnelName.contains("_EnC_") || FunnelName.contains("_IC_")
						|| FunnelName.contains("_WC_") || FunnelName.contains("_CC_")) {
					FunnelName = FunnelName.substring(0, FunnelName.lastIndexOf("_"));
					FunnelName = FunnelName.substring(0, FunnelName.lastIndexOf("_"));
					subobj.remove("FunnelName");
					subobj.put("FunnelName", FunnelName);
				}
				 group=subobj.getString("group");
				 
				 try {
					 GAMongoDAO.insertmailopendata(SubscriberEmail, SubFunnelName, mainfunnel, Campaign_id, Created_By, group);
				 }catch (Exception e) {
					// TODO: handle exception
				}
				 
/*  {"SubFunnelName":"Explore","Category":"Explore","Campaign_id":"FetchmyPaymentTemp","ip":"165.225.106.207","SubscriberEmail":"mahesh.darjee@asianpaints.com","FunnelName":"RealEstateAll","Created_By":"tanyasharma2615@gmail.com","ChildFunnelName":"RealEstateAll","opentime":"2020-05-23 16:11:23","campaignname":"FetchmyPaymentTemp","group":"bizlem"} */
				String insertedstatus = GAMongoDAO.insertruleDataforMonitoring(subobj.toString(), SubscriberEmail,
						SubFunnelName, FunnelName, Campaign_id, Created_By);
				System.out.println("insertedstatus " + insertedstatus + "_" + i);
			}

			return "true";
		} catch (Exception var20) {
			var20.printStackTrace();
			return "false";
		}
	}
}