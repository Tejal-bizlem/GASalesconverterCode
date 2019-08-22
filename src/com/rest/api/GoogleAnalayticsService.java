package com.rest.api;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.db.mongo.ga.AnalyticsDataInsertUpdate;
//import com.db.mongo.ga.GAMONGOTest;
import com.db.mongo.ga.GAMongoDAO;
import com.db.mongo.ga.GetActiveUsers;
import com.db.mongo.ga.GetTokenForVerifiedWebsites;
import com.google.api.AccesAndRefreshToken;
import com.leadconverter.ga.GoogleAnalyticsReportingSample;
import com.leadconverter.quartz.scheduler.*;
@Path("biz")
public class GoogleAnalayticsService {
	
	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayHello(){
		String resource="<? xml version='1.0' ?>" +
	    "<hello>hI this is sample xml rsponse</hello>";
		return resource;
		
	}
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHelloPlain(){
		String resource="hI this is sample text rsponse";
		return resource;
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public String sayHelloJSON(){
		String resource="{'name':'Akhilesh'}";
		return resource;
		
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHelloHTML(){
		String resource="<h1>Hi Akhiles, This is demo html response</h1>";
		return resource;
		
	}
	
	@GET
	@Path("{year}/{month}/{day}")
	public Response getDate(
			@PathParam("year") int year,
			@PathParam("month") int month, 
			@PathParam("day") int day) {
 
	   String date = year + "/" + month + "/" + day;
 
	   return Response.status(200)
		.entity("getDate is called, year/month/day : " + date)
		.build();
 	}
	
	@GET
	public Response pingMe(){
	   String str="How are you?";
	   return Response.status(200).entity(str).build();
	}
	
	//Simple URI matching
	@GET
	@Path("/birthday")   
	public Response printBdayMsg(){
	   String str="Happy Birthday!";
	   return Response.status(200).entity(str).build();
	}
	
	@GET
	@Path("/getGAReport")   
	public Response getGAReport(){
	   String str="Happy Birthday!";
	   String str_start_date="2019-01-21";
       String str_end_date="2019-01-30";
       String accessToken="";
       String view_id="";
       
	   String res=GoogleAnalyticsReportingSample.getGAReport("input",accessToken,str_start_date,str_end_date,view_id).toString();
	   return Response.status(200).entity(res).build();
	}
	
	
	
	@POST
	@Path("/getGARByTkn")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGARByTkn(InputStream incomingData) {
		//Bean bean=new Bean();
		StringBuilder crunchifyBuilder = new StringBuilder();
		long count=0;
		JSONObject json_obj=null;
		String str=null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
			System.out.println("Data Received: " + crunchifyBuilder.toString());
			JSONParser parser =new JSONParser();
			JSONObject request_json_obj= (JSONObject) parser.parse(crunchifyBuilder.toString());
			String str_start_date=request_json_obj.get("str_start_date").toString();
            String str_end_date=request_json_obj.get("str_end_date").toString();
            String accessToken=request_json_obj.get("accessToken").toString();
            String view_id=request_json_obj.get("accessToken").toString();
            
			//String str_start_date="2019-01-21";
            //String str_end_date="2019-01-30";
			
            str=GoogleAnalyticsReportingSample.getGAReport("input",accessToken,str_start_date,str_end_date,view_id).toString();
			
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		return Response.status(200).entity(str.toString()).build();
	}
	
	@POST
	@Path("/getGAR")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGAR(InputStream incomingData) {
		//Bean bean=new Bean();
		StringBuilder crunchifyBuilder = new StringBuilder();
		long count=0;
		JSONObject json_obj=null;
		String str="Not Success";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
			System.out.println("Data Received: " + crunchifyBuilder.toString());
			JSONParser parser =new JSONParser();
			JSONObject request_json_obj= (JSONObject) parser.parse(crunchifyBuilder.toString());
			String str_start_date=request_json_obj.get("str_start_date").toString();
            String str_end_date=request_json_obj.get("str_end_date").toString();
            String accessToken=request_json_obj.get("accessToken").toString();
            String refreshToken=request_json_obj.get("refreshToken").toString();
            String username=request_json_obj.get("username").toString();
            String view_name=request_json_obj.get("view_name").toString();
            String view_id=request_json_obj.get("view_id").toString();
			
			//String str_start_date="2019-01-21";
            //String str_end_date="2019-01-30";
            json_obj=new JSONObject();
            str=AnalyticsDataInsertUpdate.saveGAUserEmailAndRefreshTokenData(username,accessToken,refreshToken,view_name,view_id);
            json_obj.put("status", str);
            /*
            str=GoogleAnalyticsReportingSample.getGAReport("input",accessToken,str_start_date,str_end_date,view_id).toString();
            System.out.println("Data Received: 1 ");
            JSONObject ga_json_obj= (JSONObject) parser.parse(str.toString());
            System.out.println("Data Received: 2 ");
            AnalyticsDataInsertUpdate.saveGAData(ga_json_obj,username);
            System.out.println("Data Received: 3");
            //str=ga_json_obj.get("data").toString();
            System.out.println("Data Received: 4 ");
            */
		} catch (Exception e) {
			System.out.println("Error Parsing: 1 - "+e.getMessage());
		}
		return Response.status(200).entity(json_obj.toString()).build();
	}
	@GET
	@Path("/insertGADataInMongoDB")   
	public Response insertGADataInMongoDB(){
		/*
		JSONArray ga_users_json_arr=AnalyticsDataInsertUpdate.findGAUserCredentials();
		AccesAndRefreshToken.getGAData(ga_users_json_arr);
		
    	String str="Insert GAData In MongoDB!";
	    return Response.status(200).entity(str).build();
	    */
	    JSONArray ga_users_json_arr=AnalyticsDataInsertUpdate.findGAUserCredentials();
		AccesAndRefreshToken.getGAData(ga_users_json_arr);
		GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
		
		String str="Insert GAData In MongoDB!";
	    return Response.status(200).entity(str).build();
	}
	@GET
	@Path("/fetchGADataInsertInMongoDB")   
	public Response fetchGADataInsertInMongoDB(){
		JSONArray ga_users_json_arr=AnalyticsDataInsertUpdate.findGAUserCredentials();
		AccesAndRefreshToken.getGAData(ga_users_json_arr);
		//This Method Call get data from "google_analytics_data_temp" collection. Do the statistics and save it 
		// to "google_analytics_url_view_collection" collection
		GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
		//This Method Call used to create JSON object for RuleEngine
		GAMongoDAO.fetchGADataForRuleEngine("google_analytics_data_temp");
		
		String str="Insert GAData In MongoDB!";
	    return Response.status(200).entity(str).build();
	}
	//after restart tomcat call api manually : http://prod.bizlem.io:8052/GALeadConverter/rest/biz/callScheduler/1/23/true
	// http://prod.bizlem.io:8052/GALeadConverter/rest/biz/callScheduler
	// true is for start scheduler start false is for stop schedular
	//http://bizlem.io:8087/GALeadConverter/rest/biz/callScheduler/1/23/true
	@GET
	@Path("/callScheduler/{hr}/{minute}/{flag}")   
	public Response callScheduler(@PathParam("hr") String hr,
			@PathParam("minute") String minute, 
			@PathParam("flag") String flag){
		String date = hr + "/" + minute + "/" + flag;
		String hr1="1";
		String flag1="true";
		QuartzScheduler qtzscheduler = new QuartzScheduler();
		qtzscheduler.runJobs(hr1,flag1);
		
		//SimpleQuartzDemo simple_qtzscheduler = new SimpleQuartzDemo();
		//simple_qtzscheduler.runDemo();
		String str="Scheduler Called!"+date;
	    return Response.status(200).entity(str).build();
	}

	// api  VerifyWebsiteInGASendAlert12 
		// http://prod.bizlem.io:8052/GALeadConverter/rest/biz/VerifyWebsiteInGASendAlert12/bizlembizlem1234@gmail.com/viki@gmail.com
			@GET
			@Path("/VerifyWebsiteInGASendAlert12/{GAEmail}/{Email}")   
			public String callVerifyWebsite(@PathParam("GAEmail") String GAEmail,
					@PathParam("Email") String Email){
			
				String resp="1";
//				String GAemail = request.getParameter("GAEmail");
//				String email = request.getParameter("Email");
				
				resp=GetTokenForVerifiedWebsites.VerifyWebsites( GAEmail, Email);
				return resp;
			}
			
		
			// http://prod.bizlem.io:8052/GALeadConverter/rest/biz/getActiveUsers/viki@gmail.com
			//http://prod.bizlem.io:8052/GALeadConverter/rest/biz/callruleng/viki@gmail.com
				@GET
				@Path("/getActiveUsers/{Email}")   
				public String getActiveUsres(@PathParam("Email") String Email){
				
					String resp="";
					resp=GetActiveUsers.getActiveUsres(  Email);
				//	JSONArray ga_users_json_arr=AnalyticsDataInsertUpdate.findGAUserCredentials();
				//	resp="-----    findGAUserCredentials ---------"+ga_users_json_arr.toString();
					//AccesAndRefreshToken.getGAData(ga_users_json_arr);
					
			//	JSONArray jsa=	GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
					//This Method Call used to create JSON object for RuleEngine
						return resp+"jsa== ";
					
				}
				// http://prod.bizlem.io:8052/GALeadConverter/rest/biz/callruleng/viki@gmail.com
				@GET
				@Path("/callruleng/{Email}")   
				public String callruleeng(@PathParam("Email") String Email){
				
					JSONArray jsa=		GAMongoDAO.fetchGADataForRuleEngine("google_analytics_data_temp");
					
					return "jsa== "+jsa.toString();
					
				}
				
				//     http://prod.bizlem.io:8052/GALeadConverter/rest/biz/saveforruleng/viki@gmail.com
				@GET
				@Path("/saveforruleng/{Email}")   
				public String savedataruleeng(@PathParam("Email") String Email){
				
					JSONArray jsa=		GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
					
					return "jsa== "+jsa.toString();
					
				}
//			     http://prod.bizlem.io:8052/GALeadConverter/rest/biz/callGAFetch/viki@gmail.com
							@GET
							@Path("/callGAFetch/{Email}")   
							public String callGAFetch(@PathParam("Email") String Email){
							
								JSONArray ga_users_json_arr=AnalyticsDataInsertUpdate.findGAUserCredentials();
								AccesAndRefreshToken.getGAData(ga_users_json_arr);
							
								
								return "jsa== "+ga_users_json_arr.toString();
								
							}
				
			
}
