package com.db.mongo.ga;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class FindSubscriberListMongoDAO {
public static void main(String[] args) {
		
		//findListIdForSubscriber("viki_gmail.com","FunnelJune0701","Connect","7101");
		//String CreatedBy,String funnelName,String SubFunnelName,String SubscriberId
		
		findCampaignDetailsBasedOnCampaignID("1099","innovatters@gmail.com");//innovatters@gmail.com  akhilesh@bizlem.com
	}
	public static void findCampaignDetailsBasedOnCampaignID(String CampaignId,String SubscriberEmail){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCursor<Document> camapignDetailsCursor=null;
	    MongoCursor<Document> camapignDetailsSubscribersCursor=null;
	    Document campaign_details_doc=null;
	    Document campaign_details_subscribers_doc=null;
	    Document email_doc=null;
	    Bson filter =null;
	    Bson get_subscriber_id_filter=null;
	    String CreatedBy=null;
	    String funnelName=null;
	    String SubFunnelName=null;
	    String List_Id=null;
	    String SubscriberId=null;
	    
	    
	    try {
	    	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_details");
            filter=eq("Campaign_Id", CampaignId);
            
            camapignDetailsCursor = collection.find(filter).iterator();
            if(camapignDetailsCursor.hasNext()){
				while(camapignDetailsCursor.hasNext()) {
					    campaign_details_doc=camapignDetailsCursor.next();
						//System.out.println(campaign_details_doc.toJson());
						CreatedBy=campaign_details_doc.getString("CreatedBy");
					    funnelName=campaign_details_doc.getString("funnelName");
					    SubFunnelName=campaign_details_doc.getString("SubFunnelName");
					    List_Id=campaign_details_doc.getString("List_Id");
					    //SubscriberEmail FunnelName 
					    //get_subscriber_id_filter=and(eq("CreatedBy", CreatedBy.replace("_", "@"))
					    		//,eq("funnelName", funnelName),eq("SubscriberEmail", SubscriberEmail));
					    get_subscriber_id_filter=eq("SubscriberEmail", SubscriberEmail);
	                    Document unwind = new Document("$unwind", "$subscribers");
	                    Document match = new Document("$match", new Document(
	        	                "subscribers.email", SubscriberEmail).append("Campaign_Id", CampaignId));
	                    
	                    Document project = new Document("$project", new Document(
	        	                "_id", 0).append("subscribers", 1));
	                    List<Document> pipeline = Arrays.asList(unwind, match, project);
	                    AggregateIterable<Document> output=collection.aggregate(pipeline);
	                    if(output.first()!=null){
	                        email_doc=(Document) output.first().get("subscribers");
	                        SubscriberId=email_doc.getString("id");
	                        //System.out.println("email_doc : "+email_doc);
	                        System.out.println("Subscribers("+SubscriberEmail+ ") Is Found In List Id : "+List_Id+"  SubscriberId : "+SubscriberId);
	                        //findListIdForSubscriber(CreatedBy,funnelName,SubFunnelName,SubscriberId);
	                    }else{
	                    	System.out.println("Subscribers("+SubscriberEmail+ ") Is Not Found In List Id : "+List_Id);
	                    	System.out.println("Going to fetch SubscriberId from subscribers_details Collection");
	                    	SubscriberId=findSubscriberIdBasedOnSubscriberEmail(database,get_subscriber_id_filter);
	                    	System.out.println("Subscribers("+SubscriberEmail+ ") Is Found and SubscriberId : "+SubscriberId);
	                    	if(SubscriberId!=null){
	                    		List_Id=findListIdForSubscriber(CreatedBy,funnelName,SubFunnelName,SubscriberId);
	                    		if(List_Id!=null){
		                    		// Do your stuff here
		                    	}else{
		                    		System.out.println("No List Id Found For SubscriberId : "+SubscriberId);
		                    	}
	                    	}else{
	                    		System.out.println("No List Id Found For SubscriberId : "+SubscriberId);
	                    	}
	                    	
	                    }
	             }
            }else{
            	System.out.println("No campaign Found For Campaign Id : "+CampaignId);
            }
     	   } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}
	public static String findListIdForSubscriber(String CreatedBy,String funnelName,String SubFunnelName,String SubscriberId){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCursor<Document> sessionCountCursor=null;
	    Bson get_subscriber_id_filter=null;
	    String ListId=null;
	    try {
	    	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_list_details");
            List<String> subscribers_id_arrlist = new ArrayList<String>();
                         subscribers_id_arrlist.add(SubscriberId);
                         //subscribers_id_arrlist.add(SubscriberId1);
            Document all_query = new Document();
                     all_query.put("$all", subscribers_id_arrlist);
                     
            Document search_query = new Document();
                     search_query.put("ListSubscribersArr", all_query);
            //FindIterable<Document> iterable = collection.find().projection(projection);
            get_subscriber_id_filter=and(eq("CreatedBy", CreatedBy)
			    		,eq("FunnelName", funnelName),eq("SubFunnelName", SubFunnelName),eq("ListStatus", "active"),search_query);
            sessionCountCursor = collection.find(get_subscriber_id_filter).iterator();
            System.out.println("Subscriber List Found Status : "+sessionCountCursor.hasNext());
                if(sessionCountCursor.hasNext()){
					while(sessionCountCursor.hasNext()) {
							Document campaign_list_doc=sessionCountCursor.next();
							ListId=campaign_list_doc.getString("ListId");
					}
                }else{
                	System.out.println("SubscriberId : "+SubscriberId+" is not found in list");
                }
     	   } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	    return ListId;
	}
	public static String findSubscriberIdBasedOnSubscriberEmail(MongoDatabase database,Bson get_subscriber_id_filter){
		MongoCollection<Document> subscribers_details_collection = null;
	    MongoCursor<Document> subscribersDetailsCursor=null;
	    String SubscriberId=null;
	    try {
	    	subscribers_details_collection=database.getCollection("subscribers_details");
	    	subscribersDetailsCursor = subscribers_details_collection.find(get_subscriber_id_filter).iterator();
	            while(subscribersDetailsCursor.hasNext()) {
							Document subscribers_details_doc=subscribersDetailsCursor.next();
							SubscriberId=subscribers_details_doc.getString("SubscriberId");
							//System.out.println(subscribers_details_doc);
							//campaign_list_json_obj.put("", campaign_list_doc.getString(""));
				}
     	   } catch (Exception ex) {
             System.out.println("Error : "+ex.getMessage());
		   }
	    return SubscriberId;
	}
}
