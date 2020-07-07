//package com.db.mongo.ga;
//
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBCollection;
//
//// TODO: Auto-generated Javadoc
///**
// * The Class GetActiveUsers.
// */
//public class GetActiveUsers {
//
//	/** The mongo. */
////	private static MongoSetup mongo = new MongoSetup("salesautoconvert");
//	
//	/** The collection. */
//	private static DBCollection collection = null;
//	
//	/** The Constant logger. */
//	final static Logger logger = Logger.getLogger(GetActiveUsers.class);
//
//	/**
//	 * Gets the active usres.
//	 *
//	 * @param remoteUser the remote user
//	 * @return the active usres
//	 */
//	public static String getActiveUsres(String remoteUser) {
//		String resp = "start";
//		JSONObject active_users_fulljson = new JSONObject();
//		try {
//
//			JSONArray active_users_jsarr = new JSONArray();
//			collection = mongo.getCollection("subscribers_details");
//			BasicDBObject doc = null;
//			BasicDBObject whereQuery = new BasicDBObject();
//			whereQuery.put("RemoteUser", remoteUser);
//
//			List distinctValues = null;
////		distinctValues= collection.distinct("SubscriberId");
//			distinctValues = collection.distinct("SubscriberId", whereQuery);
//			
//			// distinctValues=
//			// collection.distinct("SubscriberId",{"RemoteUser":"viki@gmail.com"});
//			// db.subscribers_details.distinct("SubscriberId",
//			// {"RemoteUser":"viki@gmail.com"})
//
//			for (int i = 0; i < distinctValues.size(); i++) {
//
//				active_users_jsarr.put(distinctValues.get(i));
//			}
//			resp = resp + "active_users_jsarr: " + active_users_jsarr;
//
//			active_users_fulljson.put("RemoteUser", remoteUser);
//			active_users_fulljson.put("ActiveUsersCount", distinctValues.size());
//			active_users_fulljson.put("ActiveUsers", active_users_jsarr);
////			resp = resp + "active_users_fulljson:: " + active_users_fulljson.toString();
//			System.out.println("active_users_fulljson: " + active_users_fulljson);
//		} catch (Exception e) {
//			return e.toString();
//		}
//		return active_users_fulljson.toString();
//	}
//
//	/**
//	 * The main method.
//	 *
//	 * @param args the arguments
//	 */
//	public static void main(String args[]) {
//		getActiveUsres("");
//
//	}
//}
