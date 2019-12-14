package com.db.mongo.ga;


import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;



import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;


/**
 * The point of this code is to explore creating and maintaining arrays in Mongo documents.
 */
public class AnalyticsDataInsertUpdate
{
	final static Logger logger = Logger.getLogger(AnalyticsDataInsertUpdate.class);
    //private static MongoSetup   mongo      = new MongoSetup( "salesautoconvert" );
    //private static MongoSetupForIP   mongo      = new MongoSetupForIP( "salesautoconvert" );
    //private static DBCollection collection = null;

   /* private static void cleanup()
    {
    	MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		try {
			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");

			MongoCollection<Document> collection =  database.getCollection("google_analytics_data");
		}catch (Exception e) {

			System.out.println("Exc = "+e);
		}
//        collection = mongo.getCollection( "google_analytics_data" );
    }*/
    
    
    public static JSONArray findUniqueUrlsBasedSessionID(String session_id) {
    	MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> temp_collection = null;
		//BasicDBObject doc=null;
	    JSONArray campaignJsonArr=new JSONArray();
	    System.out.println("Inside  findSubscribersBasedOncampaignNameAndDimension2: "); 
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

	    	
	    	
	    	temp_collection = database.getCollection( "google_analytics_data_temp" );
	    	FindIterable<Document> docs = temp_collection.find(new Document().append("sessionCount",session_id)); //SELECT * FROM sample;
	        for (Document doc : docs) {
	        	System.out.println("session_id : "+session_id+" : pagePath: "+doc.getString("pagePath"));
	        }
	    	/*DBCursor cursor = temp_collection.find(new BasicDBObject("sessionCount",session_id));
	    	while(cursor.hasNext()){
	        	 doc=(BasicDBObject) cursor.next();
	        	 System.out.println("session_id : "+session_id+" : pagePath: "+doc.getString("pagePath"));   
	       }*/
	        //phplist797
	        
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			if(null != mongoClient) {
			mongoClient.close();
			mongoClient = null;
			}
		}
        return campaignJsonArr;
    }
    
    public static JSONArray findGAUserCredentials()
    {
    		MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		JSONArray ga_users_json_arr= null;
		String uri = null;
    	try {
    	ga_users_json_arr=new JSONArray();
    	System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
		System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
		System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
		uri = "mongodb://localhost:27017/?ssl=true";

		//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

		connectionString = new MongoClientURI(uri);
		mongoClient = new MongoClient(connectionString);
		database = mongoClient.getDatabase("salesautoconvert");
		collection = database.getCollection( "google_analytics_user_credentials" );
		//JSONObject ga_user_json_obj=null;
    	String username=null;
    	String free_trail_status=null;
       // System.out.println(cursor.size());
        
 
		FindIterable<Document> docs = collection.find(); //SELECT * FROM sample;
		//logger.info("inside GoogleAnalytics User Found : ");
		JSONObject ga_user_json_obj=null;
		String rftoken=null;
        for (Document doc : docs) {
        	
        	logger.info("inside GoogleAnalytics User Found : ");
        	
        	try {
				ga_user_json_obj=new JSONObject();
				if(doc.getString("username").equals("bluealgo.ga@gmail.com")) {
				ga_user_json_obj.put("username", doc.getString("username"));
				logger.info("inside GoogleAnalytics User Found : "+doc.getString("username")+" refreshToken:: "+doc.getString("refreshToken"));
				rftoken=doc.getString("refreshToken");
				logger.info("inside GoogleAnalytics User Found : "+rftoken);
				ga_user_json_obj.put("refreshToken",rftoken.replace("\\", ""));
				ga_user_json_obj.put("accessToken", doc.getString("accessToken"));
				ga_user_json_obj.put("view_name", doc.getString("view_name"));
				ga_user_json_obj.put("view_id", doc.getString("view_id"));
				logger.info("inside GoogleAnalytics Userga_user_json_obj  "+ga_user_json_obj);
				ga_users_json_arr.add(ga_user_json_obj);
				}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.info("inside GoogleAnalytics User Found for loop: "+e.getMessage());
			}
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
        logger.info("inside GoogleAnalytics User ga_users_json_arr : "+ga_users_json_arr);
        logger.info("Total Number of GoogleAnalytics User Found : "+ga_users_json_arr.size());
    	/*DBCursor cursor = collection.find();
    
    	BasicDBObject doc=null;
    	
    	       while(cursor.hasNext()){
        	 doc=(BasicDBObject) cursor.next();
        	    
       }*/
    	}catch (Exception e) {
    		 e.printStackTrace();
		}
    	finally {
			if(null != mongoClient) {
			mongoClient.close();
			mongoClient = null;
			}}
       return ga_users_json_arr;
    }
    

    
    public static void insertGADataTimeStamp(String timestamp)
    {   MongoClientURI connectionString = null;
	MongoClient mongoClient = null;
	MongoDatabase database = null;
	MongoCollection<Document> collection = null;
	JSONArray ga_users_json_arr=new JSONArray();
	try {
	String uri = "mongodb://localhost:27017/?ssl=true";
	System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
	System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
	System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
	System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
	//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

	connectionString = new MongoClientURI(uri);
	mongoClient = new MongoClient(connectionString);
	database = mongoClient.getDatabase("salesautoconvert");
	collection = database.getCollection( "google_analytics_timestamp" );	
	//collection = mongo.getCollection( "google_analytics_timestamp" );
				collection.deleteMany(new Document());
				Document timestampDoc = new Document();
				timestampDoc.put( "timestamp", timestamp );
				collection.insertOne(timestampDoc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	finally {
		if(null != mongoClient) {
		mongoClient.close();
		mongoClient = null;
		}}
    }
   
    
    public static String saveTempGAData(String collection_name,String ga_json_obj,String username )
    {
    	System.out.println("Going... To Save GAData In MongoDB Collection : 'google_analytics_data_temp'");
    	logger.info("Going... To Save GAData In MongoDB Collection : 'google_analytics_data_temp'");
    	
    	MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> temp_collection = null;
		try {
			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");

		 temp_collection = database.getCollection(collection_name);
//    	MongoSetup   mongo      = new MongoSetup( "salesautoconvert" );
//    	DBCollection temp_collection = mongo.getCollection( collection_name );
    //	temp_collection.deleteMany(new Document());   remove collection data
    	JSONParser parser =new JSONParser();
    	logger.info("Total ga_json_obj :ga_json_obj '"+ga_json_obj);
    	//logger.info("Total Number Of GAData Found For User : '"+username+"' is : "+total);
        JSONArray data_json_arr = null;
        Document document =null;
        ArrayList<Document> documents = new ArrayList<Document>();
		try {
			data_json_arr = (JSONArray) parser.parse(ga_json_obj);
		    for(int i=0;i<data_json_arr.size();i++){
	        	JSONObject data_json_obj=(JSONObject) data_json_arr.get(i);
	        	document = new Document();
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
	        temp_collection.insertMany(documents);
	        //temp_collection.update(query, update, upsert, multi)
	        //temp_collection.createInd
		} catch (Exception ex) {
			logger.info("Inside saveTempGAData() Method Got Error : "+ex);
			System.out.println("Inside saveTempGAData() Method Got Error : "+ex.getMessage());
		}
    }catch (Exception ex) {
			logger.info("Inside saveTempGAData() Method Got Error : "+ex.getMessage());
			
		}
    
    finally {
    	if(null != mongoClient) {
    		mongoClient.close();
    		mongoClient = null;
    	}
    }
		
        return "true";
    }
    public static String saveRecentTempGAData(String collection_name,String ga_json_obj,String username )
    {
    	System.out.println("Going... To Save GAData In MongoDB Collection : 'google_analytics_recent_data_temp'");
    	logger.info("Going... To Save GAData In MongoDB Collection : 'google_analytics_recent_data_temp'");
//    	MongoSetup   mongo      = new MongoSetup( "salesautoconvert" );
//    	DBCollection recent_temp_collection = mongo.getCollection( collection_name );
    	//recent_temp_collection.remove(new BasicDBObject());
    	//ArrayList<InsertOneModel<BasicDBObject>> recent_view_set_doc_arrlist =new  ArrayList<InsertOneModel<BasicDBObject>>();
    	//BulkWriteOptions options = new BulkWriteOptions();
        //options.ordered(false);
    	MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> recent_temp_collection = null;
    	try {
    		
    			String uri = "mongodb://localhost:27017/?ssl=true";
    			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
    			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
    			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
    			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
    			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

    			connectionString = new MongoClientURI(uri);
    			mongoClient = new MongoClient(connectionString);
    			database = mongoClient.getDatabase("salesautoconvert");

    			recent_temp_collection = database.getCollection(collection_name);
    	
    	JSONParser parser =new JSONParser();
    	logger.info("Total ga_json_obj : '"+ga_json_obj);
        JSONArray data_json_arr = null;
        Document document =null;
        ArrayList<Document> documents = new ArrayList<Document>();
        	        
		try {
			data_json_arr = (JSONArray) parser.parse(ga_json_obj);
		    for(int i=0;i<data_json_arr.size();i++){
	        	JSONObject data_json_obj=(JSONObject) data_json_arr.get(i);
	        	document = new Document();
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
		    recent_temp_collection.insertMany(documents);
	        
		} catch (Exception ex) {
			logger.info("Inside saveTempGAData() Method Got Error : "+ex.getMessage());
			System.out.println("Inside saveTempGAData() Method Got Error : "+ex.getMessage());
		}}catch (Exception e) {
			logger.info("Inside saveTempGAData() Method Got Error : "+e);
		}

        finally {
        	if(null != mongoClient) {
        		mongoClient.close();
        		mongoClient = null;
        	}
        }
        return "true";
    }
    
   /* public static String saveGAData(JSONObject ga_json_obj,String username )
    {
    	//System.out.println("Inside saveGAData ga_json_obj : "+ga_json_obj);
    	logger.info("Going... To Save GAData In MongoDB Collection : 'google_analytics_data'");
    	//System.out.println("Inside saveGAData");
    	//cleanup();
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
    */
    public static String saveGAUserEmailAndRefreshTokenData(String username,String accessToken,String refreshToken, String view_name,String view_id)
    {
    	MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoDatabase phplistdatabase = null;
		MongoCollection<Document> collection = null;	
		MongoCollection<Document> phplistcollection = null;	
    	JSONArray data_json_arr = null;
		try {
			logger.info("Inside saveGAUserEmailAndRefreshTokenData");
			String uri = "mongodb://localhost:27017/?ssl=true";
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();

			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			phplistdatabase=mongoClient.getDatabase("phplisttest");
			//recent_temp_collection = database.getCollection(collection_name);
	    	collection = database.getCollection( "google_analytics_user_credentials" );
	    	phplistcollection = database.getCollection( "google_analytics_user_credentials" );
		        BasicDBObject document = new BasicDBObject();
	            document.put( "username",    username );
	            document.put( "accessToken",   accessToken );
	            refreshToken= refreshToken.replaceAll("\\", "");
	            document.put( "refreshToken", refreshToken );
	            document.put( "view_name",    view_name );
	            document.put( "view_id",   view_id );
	            Bson updtfilter = and(eq("username", username));
				collection.updateOne(updtfilter, new Document("$set", document), new UpdateOptions().upsert(true));
				phplistcollection.updateOne(updtfilter, new Document("$set", document), new UpdateOptions().upsert(true));
				 logger.info("Inside saveGAUserEmailAndRefreshTokenData ga_status  token inserted: "+document);
	            //https://bizlem.io:8088/manager/html/
	            //boolean ga_status=chkGAUserEmailAndRefreshTokenData(collection,username,document);
	           /* logger.info("Inside saveGAUserEmailAndRefreshTokenData ga_status : "+ga_status);
	            logger.info("Inside saveGAUserEmailAndRefreshTokenData ga_status : "+ga_status);
	       */ } catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
		    } finally {
		    	if (mongoClient != null) {
					ConnectionHelper.closeConnection(mongoClient);
					mongoClient = null;
				}
			}
         
		return "Success";
    	
    }
   /* private static boolean chkGAUserEmailAndRefreshTokenData(MongoCollection<Document> collection,String username,BasicDBObject ga_user_data)
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
*/  
    /*private static void updateGAUserEmailAndRefreshTokenData( String username,BasicDBObject ga_user_data )
    {
    	BasicDBObject match = new BasicDBObject();
        match.put( "username", username );

        BasicDBObject update = new BasicDBObject();
        update.put( "$push", ga_user_data );

        collection.update( match, update );
    }*/
}
