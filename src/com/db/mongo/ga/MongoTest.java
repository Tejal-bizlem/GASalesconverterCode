package com.db.mongo.ga;

import java.util.Arrays;
import java.util.List;

//import leadconverter.doctiger.LogByFileWriter;
//import leadconverter.mongo.CampaignSheduleMongoDAO;
//import leadconverter.mongo.ConnectionHelper;
//
//import org.apache.sling.commons.json.JSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		findCampaignToScheduleFromCampaignListDetails();
	}
	public static void findCampaignToScheduleFromCampaignListDetails(){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCursor<Document> campaignListDetailsCursor=null;
	    Document campaign_list_details_doc=null;
	    Bson category_filter=null;
	    //JSONObject campaign_list_json_obj=new JSONObject();
	    String Campaign_Id=null;
	    String List_Id=null;
	    String List_Id_Delete=null;
	    String campaign_status=null;
	    String Campaign_Date=null;
	    int count=0;
	    //CampaignSheduleMongoDAO csmdao=null;
	    try {
	    	//csmdao=new CampaignSheduleMongoDAO();
	    	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_list_details");
            
            
	        Document unwind = new Document("$unwind", "$ListCampaignArr");
	        Document match = new Document("$match", new Document(
		                "ListCampaignArr.campaign_status", "draft").append("ListStatus", "active"));
	            
	        Document project = new Document("$project", new Document(
		                "_id", 0).append("ListCampaignArr", 1));
	        List<Document> pipeline = Arrays.asList(unwind, match, project);
	        AggregateIterable<Document> output=collection.aggregate(pipeline);
	        campaignListDetailsCursor=output.iterator();
	        
	        System.out.println("Campaign Found To Schedule : "+campaignListDetailsCursor.hasNext());
	        //LogByFileWriter.logger_info("CampaignSheduleMongoDAO: "+"Campaign Found To Schedule : "+campaignListDetailsCursor.hasNext());
            if(campaignListDetailsCursor.hasNext()){
				while(campaignListDetailsCursor.hasNext()) {
					campaign_list_details_doc=(Document) campaignListDetailsCursor.next().get("ListCampaignArr");
						Campaign_Id=campaign_list_details_doc.getString("Campaign_Id");
						List_Id=campaign_list_details_doc.getString("List_Id");
						campaign_status=campaign_list_details_doc.getString("campaign_status");
						Campaign_Date=campaign_list_details_doc.getString("Campaign_Date");
						System.out.println(campaign_list_details_doc);
						System.out.println("CampaignSheduleMongoDAO: "+"Campaign_Id : "+Campaign_Id
								+" Campaign_Date : "+Campaign_Date+" campaign_status : "+campaign_status
								+" campaign_status : "+campaign_status);
						//List_Id_Delete=findListIdForCampaignFromCampaignDetails(Campaign_Id);
						//csmdao.activateCampaign(Campaign_Id,List_Id,List_Id,Campaign_Date,campaign_status,"NoExplore");
						//updateCampaignStatusInCampaignListDetails(collection,Campaign_Id,List_Id,"submitted");
						//LogByFileWriter.logger_info("CampaignSheduleMongoDAO: "+"No Campaign Found To Schedule ");
						count++;
				}
            }else{
            	//System.out.println("No Campaign Found To Schedule ");
            	//LogByFileWriter.logger_info("CampaignSheduleMongoDAO: "+"No Campaign Found To Schedule ");
            }
            
            System.out.println("No. Campaign Found To Schedule : "+count);
            //LogByFileWriter.logger_info("CampaignSheduleMongoDAO: "+"Total number Of Campaign Found To Schedule In CampaignListDetails Collection : "+count);
     	   } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
            //LogByFileWriter.logger_info("CampaignSheduleMongoDAO: "+"Error : "+ex.getMessage());
		}
	    
	    
	}

}
