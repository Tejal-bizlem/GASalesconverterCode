package com.db.mongo.ga;


import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import java.net.UnknownHostException;




import java.util.Iterator;

import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoDBUpsert {

	public static void upsertTrue() throws UnknownHostException {

		MongoClient m1 = new MongoClient("localhost");

		DB db = m1.getDB("test");

		DBCollection coll = db.getCollection("car");

		BasicDBObject o1 = new BasicDBObject();

		o1.append("$set", new BasicDBObject("name", "Innova"));
//{ "_id" : { "$oid" : "5cefdd2765f41c9c88956d38"} , "speed" : 56 , "name" : "Innova"}
		BasicDBObject query = new BasicDBObject().append("speed", 56);

		WriteResult c1 = coll.update(query, o1, true, false);

		DBCursor carcursor = coll.find();

		try {
			while (carcursor.hasNext()) {
				System.out.println(carcursor.next());
			}
		} finally {
			carcursor.close();
		}

	}

	public static void upsertBulkUnorderedDocsForUpdate()
			throws UnknownHostException {

		// Get a new connection to the db assuming that it is running

		MongoClient mongoClient = new MongoClient("localhost");

		// //use test as a datbase,use your database here
		DB db = mongoClient.getDB("test");

		// //fetch the collection object ,car is used here,use your own
		DBCollection coll = db.getCollection("car");

		// intialize and create a unordered bulk
		BulkWriteOperation b1 = coll.initializeUnorderedBulkOperation();

		BasicDBObject o1 = new BasicDBObject();

		o1.append("$setOnInsert",
				new BasicDBObject("name", "innova").append("speed", 54));
		o1.append("$set", new BasicDBObject("cno", "H456"));

		b1.find(new BasicDBObject("name", "Zen")).upsert().update(o1);

		b1.execute();

		DBCursor c1 = coll.find();

		System.out.println("---------------------------------");

		try {
			while (c1.hasNext()) {
				System.out.println(c1.next());
			}
		} finally {
			c1.close();
		}

	}

	public static void upsertBulkUnordereDocsForUpdateOne()
			throws UnknownHostException {

		// Get a new connection to the db assuming that it is running

		MongoClient mongoClient = new MongoClient("localhost");

		// use test as a database,use your database here
		DB db = mongoClient.getDB("test");

		// fetch the collection object ,car is used here,use your own
		DBCollection coll = db.getCollection("car");

		// intialize and create a unordered bulk
		BulkWriteOperation b1 = coll.initializeUnorderedBulkOperation();

		BasicDBObject o1 = new BasicDBObject();

		o1.append(
				"$setOnInsert",
				new BasicDBObject("name", "Xylo").append("speed", 67).append(
						"cno", "H654"));

		b1.find(new BasicDBObject("name", "Xylo")).upsert().updateOne(o1);

		b1.execute();

		DBCursor c1 = coll.find();

		System.out.println("---------------------------------");

		try {
			while (c1.hasNext()) {
				System.out.println(c1.next());
			}
		} finally {
			c1.close();
		}

	}

	public static void upsertBulkForUpdateOneWithOperators()
			throws UnknownHostException {

		// Get a new connection to the db assuming that it is running

		MongoClient mongoClient = new MongoClient("localhost");

		// //use test as a datbase,use your database here
		DB db = mongoClient.getDB("test");

		// //fetch the collection object ,car is used here,use your own
		DBCollection coll = db.getCollection("car");

		// intialize and create a unordered bulk
		BulkWriteOperation b1 = coll.initializeOrderedBulkOperation();

		BasicDBObject o1 = new BasicDBObject();

		// insert if document not found and set the fields with updated value
		o1.append("$setOnInsert", new BasicDBObject("cno", "H123"));
		o1.append("$set", new BasicDBObject("speed", "63"));

		b1.find(new BasicDBObject("name", "Santro").append("speed", 654))
				.upsert().updateOne(o1);

		b1.execute();

		DBCursor c1 = coll.find();

		System.out.println("---------------------------------");

		try {
			while (c1.hasNext()) {
				System.out.println(c1.next());
			}
		} finally {
			c1.close();
		}

	}

	public static void upsertBulkUnorderedDocsForReplaceOne()
			throws UnknownHostException {

		// Get a new connection to the db assuming that it is running

		MongoClient mongoClient = new MongoClient("localhost");

		// //use test as a datbase,use your database here
		DB db = mongoClient.getDB("test");

		// fetch the collection object ,car is used here,use your own
		DBCollection coll = db.getCollection("car");

		// intialize and create a unordered bulk
		BulkWriteOperation b1 = coll.initializeOrderedBulkOperation();

		// insert query
		BasicDBObject o1 = new BasicDBObject("name", "Qualis").append("speed",
				76).append("color", "Palebrown");

		b1.find(new BasicDBObject("name", "Qualis")).upsert().replaceOne(o1);

		b1.execute();

		DBCursor c1 = coll.find();

		System.out.println("---------------------------------");

		try {
			while (c1.hasNext()) {
				System.out.println(c1.next());
			}
		} finally {
			c1.close();
		}

	}
	public static void IncInMongoDB()
			throws UnknownHostException {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    try {
	        mongoClient=ConnectionHelper.getConnection();
	        database=mongoClient.getDatabase("test");
	        collection=database.getCollection("url");
	        Document search_query_url_view_doc_object = new Document();
	        Document url_view_doc_object = new Document();
	        //Document doc_inc = new Document();
	        
	        Document set_doc =new Document();
	        Document set_on_insert_doc =new Document();
	        Document inc_doc =new Document();
	        Document update_doc =new Document();
	        url_view_doc_object.put("emailid", "akhil@bizlem");
	        url_view_doc_object.put("source", "direct");
	        url_view_doc_object.put("campid", "30");
	        url_view_doc_object.put("url","abc.com121");
	        //url_view_doc_object.put("top",101);
	        //url_view_doc_object.put("ttop",101);
	        //url_view_doc_object.put("avtop",102);
	        
	        set_doc.put("top",121);
	        set_doc.put("ttop",181);
	        set_doc.put("avtop",302);
	        set_on_insert_doc.put("strat_date","16-05");
	        set_doc.put("end_date","20-05");
	        
	        inc_doc.put("count",1);
	                 
		    update_doc.put("$inc",inc_doc);
		    update_doc.put("$set",set_doc);
		    update_doc.put("$setOnInsert",set_on_insert_doc);
		    
	        
	        
	        
	        search_query_url_view_doc_object.put("emailid", "akhil@bizlem");
	        search_query_url_view_doc_object.put("source", "direct");
	        search_query_url_view_doc_object.put("campid", "30");
	        search_query_url_view_doc_object.put("url","abc.com121");
	        createURLViewData(database,"url",search_query_url_view_doc_object,url_view_doc_object,update_doc);
	        
	    }catch(Exception ex){
	    	System.out.println(ex.getMessage());
	    }
	}
	public static void createURLViewData(MongoDatabase database,String coll_name,Document search_query,Document doc,Document doc_inc) {
		// google_analytics_url_view_collection
		MongoCollection<Document> url_view_collection = null;
		try{
			url_view_collection=database.getCollection(coll_name);
		    Bson filter = search_query;
		    Bson update =  new Document("$set",doc);
		    UpdateOptions options = new UpdateOptions().upsert(true);
		    Bson update_inc =  new Document("$inc",doc_inc);
		    /*
		    Document modifiedObject =new Document();
		             modifiedObject.put("$inc", new BasicDBObject().append("No_OF_Clicks", 5));
		    
		    
		    Document update_doc =new Document();
		    
		    Document inc_doc =new Document();
		             inc_doc.put("f1", "v1");
		             inc_doc.put("f1", "v1");
		    Document inc_query_doc =new Document();
		             inc_query_doc.put("$inc",inc_doc);
		             
		    Document set_doc =new Document();
		             set_doc.put("f1", "v1");
		             set_doc.put("f1", "v1");
		    Document set_query_doc =new Document();
		             set_query_doc.put("$set",set_doc);
		             
		    update_doc.put("$inc",inc_doc);
		    update_doc.put("$set",set_doc);
		    
		    
		    
		    {
		        "$inc": { "likeCount": 1 },
		        "$push": { "likes": ObjectId("54bb2244a3a0f26f885be2a4") }
		    }
		    */
		    
		    url_view_collection.updateOne(filter, doc_inc, options);
		} catch (Exception e) {
	        System.out.println("Exception : "+e.getMessage());
		} finally {
			FindIterable<Document> c1 = url_view_collection.find();

			MongoCursor<Document> sessionCountCursor = c1.iterator();
			System.out.println("-------------------------------------------------------------");
			try {
				while(sessionCountCursor.hasNext()) {
					Document doc1=sessionCountCursor.next();
					System.out.println(doc1);
					}
			} finally {
				sessionCountCursor.close();
			}
		}
     }

	public static void main(String[] args) throws UnknownHostException {
		IncInMongoDB();
		// invoke all the methods
//		upsertTrue();
//		upsertBulkUnorderedDocsForUpdate();
//		upsertBulkUnordereDocsForUpdateOne();
//		upsertBulkForUpdateOneWithOperators();
//		upsertBulkUnorderedDocsForReplaceOne();

	}

}

