package com.google.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.db.mongo.ga.AnalyticsDataInsertUpdate;
import com.db.mongo.ga.GAMongoDAO;
import com.leadconverter.ga.GoogleAnalyticsReportingSample;
import com.rest.api.Test;

public class AccesAndRefreshToken {
	final static Logger logger = Logger.getLogger(AccesAndRefreshToken.class);
	//AnalyticsDataInsertUpdate analytics_mongo_dao=new AnalyticsDataInsertUpdate();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JSONArray ga_users_json_arr=AnalyticsDataInsertUpdate.findGAUserCredentials();
		getGAData(ga_users_json_arr);
		//GAMongoDAO.saveGADataForSubscribersView("google_analytics_data_temp");
		
	}
	
	public static void getGAData(JSONArray ga_users_json_arr) {
		String response_code=null;
		if(ga_users_json_arr.size()!=0){
			JSONObject ga_user_json_obj=null;
	    	for(int i=0;i<ga_users_json_arr.size();i++){
	    		ga_user_json_obj=(JSONObject) ga_users_json_arr.get(i);
	    		
	    		String reftkn=ga_user_json_obj.get("refreshToken").toString();
	    		JSONObject respose_json_object=AccesAndRefreshToken.getTokenByRefreshToken(reftkn);
	    		response_code=(String) respose_json_object.get("response_code");
	    		if(response_code.equals("200")){
	    			
	    			String last_run_date=null;
	    			String next_run_date=null;
	    			String str_start_date="2019-01-21";
	                String str_end_date="2019-01-30";
	                String accessToken="";
	                String username=ga_user_json_obj.get("username").toString();//"bizlembizlem1234@gmail.com";
	                String view_name=ga_user_json_obj.get("view_name").toString();//"All Web Site Data";
	                String view_id=ga_user_json_obj.get("view_id").toString();//"190758260";
	                
	                accessToken=(String) respose_json_object.get("access_token");
	                logger.info("Going... to Get GAData For GA User : '"+username+"' STARTED....");
	    		    //System.out.println("response_code : "+response_code+"\n"+"access_token : "+accessToken);
	                logger.info("GA UserName : "+username+"   GA View Id : "+view_id+"\n"+"Oauth2 Token : "+accessToken);
	    		    DateFormat date_formatter_with_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    		    SimpleDateFormat date_formatter=new SimpleDateFormat("yyyy-MM-dd");  //2019-04-17
	    		    String days_to_fetch_ga=ResourceBundle.getBundle("config").getString("days_to_fetch_ga");
	    			int days_to_fetch_ga_int=Integer.parseInt(days_to_fetch_ga);
	    			Date start_date=new Date();
	    			     str_end_date=date_formatter.format(start_date);
	    		    Date endt_date=new Date();
	    		         endt_date.setDate(endt_date.getDate()-days_to_fetch_ga_int);
	    		         str_start_date=date_formatter.format(endt_date);
	    		    // This is static date plz comment it when you run for dynamic date     
	    		    //str_start_date="2019-02-01";
	 	    	    //str_end_date="2019-05-23";     
	    		    System.out.println("Start Date(From) : "+str_start_date+"  End Date(To) : "+str_end_date);
	    			logger.info("Start Date(From) : "+str_start_date+"  End Date(To) : "+str_end_date);
	    			//last_run_date=AnalyticsDataInsertUpdate.getGADataTimeStamp();
	    			logger.info("Going... to Get Last Run Date, Last Run Date is : "+last_run_date);
	    			logger.info("Setting... Next Run Date, Next Run Date is : "+date_formatter_with_timestamp.format(start_date));
	    			//AnalyticsDataInsertUpdate.insertGADataTimeStamp(date_formatter_with_timestamp.format(start_date));
	    			try{
	    				String google_analytics_response=null;
	    				JSONParser parser =new JSONParser();
	    			    google_analytics_response=GoogleAnalyticsReportingSample.getGAReport("input",accessToken,str_start_date,str_end_date,view_id).toString();
	    	            //System.out.println("Data Received: 1 ");
	    	            JSONObject ga_json_obj= (JSONObject) parser.parse(google_analytics_response.toString());
	    	            //System.out.println("Json Response "+ga_json_obj.get("data"));
	    	            JSONArray ga_data_json_arr=(JSONArray) ga_json_obj.get("data");
	    	            if(ga_data_json_arr.size()>0){
	    	               //JsonSortingMethods.sortJsonArray(ga_json_obj.get("data").toString(),username);
	    	               AnalyticsDataInsertUpdate.saveTempGAData("google_analytics_data_temp",ga_json_obj.get("data").toString(),username);
	    	               AnalyticsDataInsertUpdate.saveRecentTempGAData("google_analytics_recent_data_temp",ga_json_obj.get("data").toString(),username);
	    	            }
	    	        }catch(ParseException ex){
	    				//System.out.println("ParseException");
	    				logger.info("ParseException");
	    			}
	    			logger.info("Going... to Get GAData For GA User : '"+username+"' ENDED....");
	    		}else{
	    			//System.out.println("Your Refresh Token is expire !"+"\n"+"You Need to Login In Google Analytics Account Through LeadAutoConveter UI.");
	    			logger.info("Your Refresh Token is expire !"+"\n"+"You Need to Login In Google Analytics Account Through LeadAutoConveter UI.");
	    		}
	    	}
	    }else{
    		//System.out.println("No Google Analytics Account Found !"+"\n"+"You Need to Login In Google Analytics Account Through LeadAutoConveter UI.");
    		logger.info("No Google Analytics Account Found !"+"\n"+"You Need to Login In Google Analytics Account Through LeadAutoConveter UI.");
    	}
		
	}
	@SuppressWarnings("unchecked")
	

	public static JSONObject getTokenByRefreshToken(String refresh_token) {
   	 JSONObject respose_json_object=null;
   	 HttpURLConnection conn=null;
 	 String response_code=null;
 	 String res="10";
   	       	try{
	   			String client_id=ResourceBundle.getBundle("config").getString("client_id");
	   			res=res+"client_id:: "+client_id;
	   			String client_secret=ResourceBundle.getBundle("config").getString("client_secret");
	   			//String redirect_uri="http://localhost:9999/GALeadConverter/googleanalytics.html";
	   			/*
		        String client_id=ResourceBundle.getBundle("config").getString("client_id");
	            String client_secret=ResourceBundle.getBundle("config").getString("client_secret");
	            String redirect_uri=ResourceBundle.getBundle("config").getString("redirect_uri");
  			 	*/	
	   			res=res+"1";
  			    String post_data_content = "grant_type=refresh_token&refresh_token="+refresh_token+"&client_id="+client_id+"&client_secret="+client_secret;      
  			    String url_str=ResourceBundle.getBundle("config").getString("oauth2_token_url");
  			  res=res+"2";
	   			URL url = new URL(url_str);
	   		        conn = (HttpURLConnection) url.openConnection();
	   		        conn.setRequestMethod("POST");
	   		        conn.setDoOutput(true);
	   		        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.0.5) Gecko/2008120122 Firefox/3.0.5");
			   		conn.setRequestProperty("Content-Length", String.valueOf(post_data_content.length()));
			   		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			   		conn.setRequestProperty("Accept", "*/*");
			   		conn.setRequestMethod("POST");
			   		conn.setInstanceFollowRedirects(false); // very important line :)
			   		res=res+"3";
	   	        PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream()), true);
	   	     res=res+"4";
	   	            pw.print(post_data_content);
	   	            pw.flush();
	   	            pw.close();
	   		        
	   	            response_code=String.valueOf(conn.getResponseCode());
	   	         res=res+"5";
	   		        if (conn.getResponseCode() != 200) {
	   		        	res=res+"6";
	   		        	respose_json_object=new JSONObject();
	   		        	respose_json_object.put("response_code", response_code);
	   		        }else{
	   		        	res=res+"7";
	   		        	String newLine = System.getProperty("line.separator");
		   		        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		   		        StringBuilder result = new StringBuilder();
		   		                String line;
		   		                boolean flag = false;
		   		                while ((line = reader.readLine()) != null) {
		   		                    result.append(flag ? newLine : "").append(line);
		   		                   
		   		                    flag = true;
		   		                }
		   		             //respose_json_object
		   		             res=res+"result.toString()"+result.toString();
		   		        respose_json_object= (JSONObject) new JSONParser().parse(result.toString());
		   		        respose_json_object.put("response_code", response_code);
	   		        }
	   		        conn.disconnect();
	   		       
	   		    }
	   		    catch (Exception e ) {
//	   		            e.printStackTrace();
	   		    	respose_json_object=new JSONObject();
	   		     respose_json_object.put("res", res);
	   		     respose_json_object.put("response_exc", e.toString());
	   		        }
	       	return respose_json_object;
   }

}
