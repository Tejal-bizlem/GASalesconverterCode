package com.db.mongo.ga;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.bson.Document;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

/**
 * A sort of quick and dirty Mongo manager for this exercise.
 */
public class MongoSetupForIP
{
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//addCampaignDetails(new Document());
		  //AddSubscriberInList("");
		  //RemoveSubscribersFromList("");
		  //removeSubscribersFromList(new Document());
		  //addSubscriberInList(new Document());
		MongoSetupForIP mongoip=new MongoSetupForIP();
		mongoip.setup();
		DBCollection coll=mongoip.getCollection("phplisttest","CampDetails");
		
		DBCursor c1 = coll.find();
		Iterator<DBObject> sessionCountCursor = c1.iterator();
		System.out.println("-------------------------------------------------------------");
		try {
			while(sessionCountCursor.hasNext()) {
				DBObject doc1=sessionCountCursor.next();
				System.out.println(doc1);
				//System.out.println("id  :  "+id);
				}
		} finally {
			((DBCursor) sessionCountCursor).close();
		}
				
	}
	
    Mongo  mongo = null;
    DB     database = null;
    String databaseName;

    public MongoSetupForIP()
    {
        setup();
    }

    public MongoSetupForIP( String database )
    {
        setup();
        this.databaseName = database;
    }

    @SuppressWarnings("deprecation")
	private void setup()
    {
        try
        {
        	String mongodb_ip=ResourceBundle.getBundle("config").getString("mongodb_ip");
            //mongo = new Mongo("35.236.154.164");
        	mongo = new Mongo(mongodb_ip);
        }
        catch(Exception e )
        {
            System.out.println( );
        }
    }

    public String getDatabaseName() { return this.databaseName; }
    public void setDatabaseName( String databaseName ) { this.databaseName = databaseName; }

    public DB getDatabase() { return this.database; }

    public DBCollection getCollection( String collection )
    {
        return this.getCollection( this.databaseName, collection );
    }

    public DBCollection getCollection( String database, String collection )
    {
        this.database = mongo.getDB( database );
        return this.database.getCollection( collection );
    }
}
