package com.db.mongo.ga;

import static com.mongodb.client.model.Filters.eq;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;







import org.bson.BSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leadconverter.freetrail.FreeTrialandCart;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.InsertOneModel;
import com.rest.api.Test;

import org.apache.log4j.Logger;


/**
 * The point of this code is to explore creating and maintaining arrays in Mongo documents.
 */
public class AnalyticsDataInsertUpdate
{
	final static Logger logger = Logger.getLogger(AnalyticsDataInsertUpdate.class);
    private static MongoSetup   mongo      = new MongoSetup( "phplisttest" );
    //private static MongoSetupForIP   mongo      = new MongoSetupForIP( "phplisttest" );
    private static DBCollection collection = null;

    private static void cleanup()
    {
        collection = mongo.getCollection( "google_analytics_data" );
    }
    
    
    public static JSONArray findUniqueUrlsBasedSessionID(String session_id) {
		DBCollection temp_collection = null;
		BasicDBObject doc=null;
	    JSONArray campaignJsonArr=new JSONArray();
	    System.out.println("Inside  findSubscribersBasedOncampaignNameAndDimension2: "); 
	    try {
	    	temp_collection = mongo.getCollection( "google_analytics_data_temp" );
	    	DBCursor cursor = temp_collection.find(new BasicDBObject("sessionCount",session_id));
	    	while(cursor.hasNext()){
	        	 doc=(BasicDBObject) cursor.next();
	        	 System.out.println("session_id : "+session_id+" : pagePath: "+doc.getString("pagePath"));   
	       }
	        //phplist797
	        
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			
		}
        return campaignJsonArr;
    }
    
    public static JSONArray findGAUserCredentials()
    {
    	collection = mongo.getCollection( "google_analytics_user_credentials" );
    	DBCursor cursor = collection.find();
    	BasicDBObject doc=null;
    	JSONArray ga_users_json_arr=new JSONArray();
    	JSONObject ga_user_json_obj=null;
    	String username=null;
    	String free_trail_status=null;
       // System.out.println(cursor.size());
        logger.info("Total Number of GoogleAnalytics User Found : "+cursor.size());
        while(cursor.hasNext()){
        	 doc=(BasicDBObject) cursor.next();
        	    ga_user_json_obj=new JSONObject();
        		ga_user_json_obj.put("username", doc.getString("username").toString());
        		ga_user_json_obj.put("refreshToken", doc.getString("refreshToken").toString());
        		ga_user_json_obj.put("accessToken", doc.getString("accessToken").toString());
        		ga_user_json_obj.put("view_name", doc.getString("view_name").toString());
        		ga_user_json_obj.put("view_id", doc.getString("view_id").toString());
        		
        		ga_users_json_arr.add(ga_user_json_obj);
        		/*
        		username=doc.getString("username").toString();
        		free_trail_status=new FreeTrialandCart().checkFreeTrialExpirationStatus(username);
        		//System.out.println(campaign_details_doc);
        		if(free_trail_status.equals("0")){
        			ga_users_json_arr.add(ga_user_json_obj);
        		}else{
        			System.out.println("Freetrail Expired for User : "+username);
        		}
        		*/
        		
       }
       return ga_users_json_arr;
    }
    
    private static boolean chkGAData(DBCollection collection,String email,BasicDBObject document)
    {
    	boolean gadata_availability=false;
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put( "subscriber_email", email );
        
        DBCursor cursor = collection.find(whereQuery);
        
        //System.out.println(cursor.size());
        //logger.info("Inside chkGAData Method, Number of Records Found : "+cursor.size());
        if(cursor.size()==0){
        	BasicDBObject ga = new BasicDBObject();
    		ga.put( "subscriber_email", email );
    		ArrayList< DBObject > array = new ArrayList< DBObject >();
            array.add( document );
            ga.put( "addresses", array );
            //addGAData(email,account);
            insertGAData(ga);
        	
        }else{
        	while (cursor.hasNext()) {
            	DBObject db_obj=cursor.next();
                //System.out.println(db_obj);
                //logger.info("db_obj : "+db_obj);
                gadata_availability=db_obj.containsField("addresses");
                
                //System.out.println("gadata_availability : "+gadata_availability);
                //logger.info("GA User Availability Check: "+gadata_availability);
            	if(gadata_availability==true){
            		// update ga in collection
            		updateGAData(email,document);
            	}else{
            	 // update collection
            		//System.out.println("insert ga in collection");
            		logger.info("insert ga in collection");
            		BasicDBObject ga = new BasicDBObject();
            		ga.put( "subscriber_email", email );
            		ArrayList< DBObject > array = new ArrayList< DBObject >();
                    array.add( document );
                    ga.put( "addresses", array );
                    //addGAData(email,account);
                    insertGAData(ga);
                }
            }	
        	
        }
        /*
        
        */
		return gadata_availability;
    }
    
    
    public static void insertGADataTimeStamp(String timestamp)
    {   
    	    collection = mongo.getCollection( "google_analytics_timestamp" );
    	    collection.remove(new BasicDBObject());
            BasicDBObject timestampDoc = new BasicDBObject();
	    	timestampDoc.put( "timestamp", timestamp );
	    	collection.insert(timestampDoc);
    }
    public static String getGADataTimeStamp()
    {   
    	    String timestamp=null;
    	    DateFormat date_formatter_with_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	    collection = mongo.getCollection( "google_analytics_timestamp" );
    	    DBCursor cursor = collection.find();
            //System.out.println(cursor.size());
            if(cursor.size()>0)
            while(cursor.hasNext()){
            	DBObject db_obj=cursor.next();
            	timestamp=(String) db_obj.get("timestamp");
            }else{
            	timestamp=date_formatter_with_timestamp.format(new Date());
            }
			return timestamp;
    }
    
    private static void insertGAData(BasicDBObject gadata)
    {
    	collection.insert(gadata);
    }
    
    private static void addGAData( String subscriber_email,BasicDBObject gadata_arr )
    {
    	BasicDBObject updateDoc = new BasicDBObject();
    	updateDoc.append("$set", gadata_arr);
    			
    	BasicDBObject searchQuery = new BasicDBObject().append("email", subscriber_email);
    	collection.update(searchQuery, updateDoc);
    }
    
    private static void updateGAData( String subscriber_email,BasicDBObject gadata )
    {
    	BasicDBObject match = new BasicDBObject();
        match.put( "subscriber_email", subscriber_email );

        BasicDBObject update = new BasicDBObject();
        update.put( "$push", new BasicDBObject( "addresses", gadata ) );

        collection.update( match, update );
    }
    
    private static void findRecords( String subscriber_email)
    {
    	BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put( "subscriber_email", subscriber_email );
        //bhushan.pawar@bizlem.com
        DBCursor cursor = collection.find(whereQuery);
        
        System.out.println(cursor.size());
    }

    public static void main( String[] args )
    {   
    	JSONArray ga_users_json_arr=findGAUserCredentials();
    	JSONObject ga_user_json_obj=null;
    	String username=null;
    	String refreshToken=null;
    	String accessToken=null;
    	String view_name=null;
    	String view_id=null;
    	for(int i=0;i<ga_users_json_arr.size();i++){
    		ga_user_json_obj=(JSONObject) ga_users_json_arr.get(i);
    		username=ga_user_json_obj.get("username").toString();
    		refreshToken=ga_user_json_obj.get("refreshToken").toString();
    		accessToken=ga_user_json_obj.get("accessToken").toString();
    		view_name=ga_user_json_obj.get("view_name").toString();
    		view_id=ga_user_json_obj.get("view_id").toString();
    		System.out.println("accessToken : "+accessToken+"\n"+"refreshToken : "+refreshToken+"\n"+"view_id : "+view_id);
    		logger.info("accessToken : "+accessToken+"\n"+"refreshToken : "+refreshToken+"\n"+"view_id : "+view_id);
    	}
    	 
    	/*
    	cleanup();
    	String email="tejal@bizlem.com";
    	
    	BasicDBObject document = new BasicDBObject();
        document.put( "email",    "ghi-789" );
        document.put( "sessions",   "1" );
        document.put( "browser", "Chrome" );
        document.put( "sessionCount",   "11" );
        document.put( "sessionDurationBucket",  "17" );
        document.put( "pageTitle",  "MailTangy :: Contact Us" );
        document.put( "urlclickstatistics_url",  "MailTangy :: Contact Us" );
        
    	boolean ga_status=chkGAData(collection,email,document);
    	
    	BasicDBObject ga = new BasicDBObject();
        ga.put( "email",    "ak@y.com" );
        ga.put( "name",   "ak" );
        */
       // collection.insert(ga);
        //findRecords("kapoorsaurabh@emaar.com");
    	
    	
    }
    
    public static String saveTempGAData(String collection_name,String ga_json_obj,String username )
    {
    	System.out.println("Going... To Save GAData In MongoDB Collection : 'google_analytics_data_temp'");
    	logger.info("Going... To Save GAData In MongoDB Collection : 'google_analytics_data_temp'");
    	MongoSetup   mongo      = new MongoSetup( "phplisttest" );
    	DBCollection temp_collection = mongo.getCollection( collection_name );
    	             temp_collection.remove(new BasicDBObject());
    	JSONParser parser =new JSONParser();
    	logger.info("Total ga_json_obj :ga_json_obj '"+ga_json_obj);
    	//logger.info("Total Number Of GAData Found For User : '"+username+"' is : "+total);
        JSONArray data_json_arr = null;
        BasicDBObject document =null;
        ArrayList<BasicDBObject> documents = new ArrayList<BasicDBObject>();
		try {
			data_json_arr = (JSONArray) parser.parse(ga_json_obj);
		    for(int i=0;i<data_json_arr.size();i++){
	        	JSONObject data_json_obj=(JSONObject) data_json_arr.get(i);
	        	document = new BasicDBObject();
	        	document.put( "ga_username",username);
	        	document.put( "country",data_json_obj.get("ga:country").toString());
	            document.put( "pagePath",data_json_obj.get("ga:pagePath").toString());
	            document.put( "sessionCount",data_json_obj.get("ga:sessionCount").toString());
	            document.put( "dimension2",data_json_obj.get("ga:dimension2").toString());
	            document.put( "channelGrouping",data_json_obj.get("ga:channelGrouping").toString());
	            document.put( "sessionDurationBucket",data_json_obj.get("ga:sessionDurationBucket").toString());
	            document.put( "hostname",data_json_obj.get("ga:hostname").toString());
	            document.put( "url",(data_json_obj.get("ga:hostname").toString()+data_json_obj.get("ga:pagePath").toString()));
	            document.put( "timeOnPage",data_json_obj.get("timeOnPage").toString());
	            document.put( "bounces",data_json_obj.get("bounces").toString());
	            document.put( "sourceMedium",data_json_obj.get("ga:sourceMedium").toString());
	            document.put( "dateHourMinute",data_json_obj.get("ga:dateHourMinute").toString());
	            documents.add(document);
	        }
	        temp_collection.insert(documents);
	        //temp_collection.update(query, update, upsert, multi)
	        //temp_collection.createInd
		} catch (Exception ex) {
			logger.info("Inside saveTempGAData() Method Got Error : "+ex.getMessage());
			System.out.println("Inside saveTempGAData() Method Got Error : "+ex.getMessage());
		}
        return "true";
    }
    public static String saveRecentTempGAData(String collection_name,String ga_json_obj,String username )
    {
    	System.out.println("Going... To Save GAData In MongoDB Collection : 'google_analytics_recent_data_temp'");
    	logger.info("Going... To Save GAData In MongoDB Collection : 'google_analytics_recent_data_temp'");
    	MongoSetup   mongo      = new MongoSetup( "phplisttest" );
    	DBCollection recent_temp_collection = mongo.getCollection( collection_name );
    	//recent_temp_collection.remove(new BasicDBObject());
    	//ArrayList<InsertOneModel<BasicDBObject>> recent_view_set_doc_arrlist =new  ArrayList<InsertOneModel<BasicDBObject>>();
    	//BulkWriteOptions options = new BulkWriteOptions();
        //options.ordered(false);
    	JSONParser parser =new JSONParser();
    	logger.info("Total ga_json_obj : '"+ga_json_obj);
        JSONArray data_json_arr = null;
        BasicDBObject document =null;
        ArrayList<BasicDBObject> documents = new ArrayList<BasicDBObject>();
        	        
		try {
			data_json_arr = (JSONArray) parser.parse(ga_json_obj);
		    for(int i=0;i<data_json_arr.size();i++){
	        	JSONObject data_json_obj=(JSONObject) data_json_arr.get(i);
	        	document = new BasicDBObject();
	        	document.put( "ga_username",username);
	        	document.put( "country",data_json_obj.get("ga:country").toString());
	            document.put( "pagePath",data_json_obj.get("ga:pagePath").toString());
	            document.put( "sessionCount",data_json_obj.get("ga:sessionCount").toString());
	            document.put( "dimension2",data_json_obj.get("ga:dimension2").toString());
	            document.put( "channelGrouping",data_json_obj.get("ga:channelGrouping").toString());
	            document.put( "sessionDurationBucket",data_json_obj.get("ga:sessionDurationBucket").toString());
	            document.put( "hostname",data_json_obj.get("ga:hostname").toString());
	            document.put( "url",(data_json_obj.get("ga:hostname").toString()+data_json_obj.get("ga:pagePath").toString()));
	            document.put( "timeOnPage",data_json_obj.get("timeOnPage").toString());
	            document.put( "bounces",data_json_obj.get("bounces").toString());
	            document.put( "sourceMedium",data_json_obj.get("ga:sourceMedium").toString());
	            document.put( "dateHourMinute",data_json_obj.get("ga:dateHourMinute").toString());
	            
	            //recent_view_set_doc_arrlist.add(new InsertOneModel<BasicDBObject>(document));
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  	
	            String ga_date_hour_minute = data_json_obj.get("ga:dateHourMinute").toString();
				String final_date_str=ga_date_hour_minute.substring(0, 4)+"-"+ga_date_hour_minute.substring(4, 6)+"-"+ga_date_hour_minute.substring(6, 8);
			  	String date_str=final_date_str+" "+ga_date_hour_minute.substring(8, 10)+":"+ga_date_hour_minute.substring(10, 12)+":00";
			  	Date final_date = sdf.parse(final_date_str);
			    //System.out.println(" final_date_str_temp1 date : "+dateFormat.parse(date_str));
			  	document.put( "dateHourMinuteInDateFormat",final_date);
			  	document.put( "recordInsertionDate",new Date());
			    
			    documents.add(document);
	        }
		    recent_temp_collection.insert(documents);
	        
		} catch (Exception ex) {
			logger.info("Inside saveTempGAData() Method Got Error : "+ex.getMessage());
			System.out.println("Inside saveTempGAData() Method Got Error : "+ex.getMessage());
		}
        return "true";
    }
    
    public static String saveGAData(JSONObject ga_json_obj,String username )
    {
    	//System.out.println("Inside saveGAData ga_json_obj : "+ga_json_obj);
    	logger.info("Going... To Save GAData In MongoDB Collection : 'google_analytics_data'");
    	//System.out.println("Inside saveGAData");
    	cleanup();
    	JSONParser parser =new JSONParser();
    	String total=ga_json_obj.get("total").toString();;
        String dimensionHeaderSize=ga_json_obj.get("dimensionHeaderSize").toString();
        
        //System.out.println("Inside saveGAData : total = "+total);
        logger.info("Total Number Of GAData Found For User : '"+username+"' is : "+total);
        //System.out.println("Inside saveGAData : dimensionHeaderSize = "+dimensionHeaderSize);
        //logger.info("Inside saveGAData : dimensionHeaderSize = "+dimensionHeaderSize);
        
        JSONArray data_json_arr = null;
		try {
			data_json_arr = (JSONArray) parser.parse(ga_json_obj.get("data").toString());
		
	        for(int i=0;i<data_json_arr.size();i++){
	        	JSONObject data_json_obj=(JSONObject) data_json_arr.get(i);
	        	String campaign=data_json_obj.get("ga:campaign").toString();
	        	String pagePath=data_json_obj.get("ga:pagePath").toString();
	        	String sessionCount=data_json_obj.get("ga:sessionCount").toString();
	        	String dimension2=data_json_obj.get("ga:dimension2").toString();
	        	String medium=data_json_obj.get("ga:medium").toString();
	        	String sessiondurationBucket=data_json_obj.get("ga:sessiondurationBucket").toString();
	        	//String pageTitle=data_json_obj.get("ga:pageTitle").toString();
	        	String timeOnPage=data_json_obj.get("timeOnPage").toString();
	        	String bounces=data_json_obj.get("bounces").toString();
	        	String source=data_json_obj.get("ga:source").toString();
	        	String hostname=data_json_obj.get("ga:hostname").toString();
	        	//String date=data_json_obj.get("ga:date").toString();
	        	String dateHourMinute=data_json_obj.get("ga:dateHourMinute").toString();
	        	//Dimension dateHourMinute = new Dimension().setName("ga:dateHourMinute");
	        	
	        	BasicDBObject document = new BasicDBObject();
	            document.put( "campaign",    campaign );
	            document.put( "hostname",    hostname );
	            //document.put( "date",    date );
	            document.put( "dateHourMinute",    dateHourMinute );
	            document.put( "pagePath",   pagePath );
	            document.put( "sessionCount", sessionCount );
	            document.put( "dimension2",    dimension2 );
	            document.put( "medium",   medium );
	            document.put( "sessiondurationBucket", sessiondurationBucket );
	            //document.put( "pageTitle",    pageTitle );
	            document.put( "timeOnPage",   timeOnPage );
	            document.put( "bounces", bounces );
	            document.put( "source",    source );
	            
	        	boolean ga_status=chkGAData(collection,username,document);
	        }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "true";
    }
    
    public static String saveGAUserEmailAndRefreshTokenData(String username,String accessToken,String refreshToken, String view_name,String view_id)
    {
    	logger.info("Inside saveGAUserEmailAndRefreshTokenData");
    	collection = mongo.getCollection( "google_analytics_user_credentials" );
    	
    	JSONArray data_json_arr = null;
		try {
		        BasicDBObject document = new BasicDBObject();
	            document.put( "username",    username );
	            document.put( "accessToken",   accessToken );
	            document.put( "refreshToken", refreshToken );
	            document.put( "view_name",    view_name );
	            document.put( "view_id",   view_id );
	            boolean ga_status=chkGAUserEmailAndRefreshTokenData(collection,username,document);
	            logger.info("Inside saveGAUserEmailAndRefreshTokenData ga_status : "+ga_status);
	            logger.info("Inside saveGAUserEmailAndRefreshTokenData ga_status : "+ga_status);
	        } catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
		    }
         
		return "Success";
    	
    }
    private static boolean chkGAUserEmailAndRefreshTokenData(DBCollection collection,String username,BasicDBObject ga_user_data)
    {
    	boolean gadata_availability=false;
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put( "username", username );
        
        DBCursor cursor = collection.find(whereQuery);
        
        //System.out.println(cursor.size());
        if(cursor.size()==0){
        	insertGAData(ga_user_data);
        }else{
        	while (cursor.hasNext()) {
            	DBObject db_obj=cursor.next();
                //System.out.println(db_obj);
                gadata_availability=db_obj.containsField("username");
                
                System.out.println("gadata_availability : "+gadata_availability);
                logger.info("gadata_availability : "+gadata_availability);
            	if(gadata_availability==true){
            		//System.out.println("update ga in collection");
            		// update ga in collection
            		updateGAUserEmailAndRefreshTokenData(username,ga_user_data);
            	}else{
            	 // update collection
            		//addGAData(email,account);
                    insertGAData(ga_user_data);
                }
            }	
        	
        }
        return gadata_availability;
    }
    private static void updateGAUserEmailAndRefreshTokenData( String username,BasicDBObject ga_user_data )
    {
    	BasicDBObject match = new BasicDBObject();
        match.put( "username", username );

        BasicDBObject update = new BasicDBObject();
        update.put( "$push", ga_user_data );

        collection.update( match, update );
    }
}
