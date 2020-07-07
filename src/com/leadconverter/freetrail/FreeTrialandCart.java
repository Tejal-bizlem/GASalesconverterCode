//package com.leadconverter.freetrail;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import org.apache.log4j.Logger;
//import org.json.JSONObject;
//
//import com.db.mongo.ga.GAMongoDAO;
//import com.leadconverter.quartz.scheduler.QuartzScheduler;
//
//
//
//// TODO: Auto-generated Javadoc
///**
// * The Class FreeTrialandCart.
// */
//public class FreeTrialandCart {
//	
//	/** The Constant logger. */
//	final static Logger logger = Logger.getLogger(QuartzScheduler.class);
//    //ResourceBundle bundle = ResourceBundle.getBundle("config");
////	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
//
//	/**
//     * The main method.
//     *
//     * @param args the arguments
//     */
//    public static void main(String[] args) {
//		//String free_trail_status=new FreeTrialandCart().checkfreetrial("akhileshyadav3145.biz@gmail.com");
//		//System.out.println("free_trail_status : "+free_trail_status);
//		//"viki@gmail.com"
//		long free_trail_subscribers_count=Long.parseLong(ResourceBundle.getBundle("config").getString("free_trail_subscribers_count"));
//		String free_trail_status=new FreeTrialandCart().checkfreetrial("viki@gmail.com");
//		System.out.println("free_trail_status : "+free_trail_status);
//		
//	}
//	
//	/**
//	 * Check free trial expiration status.
//	 *
//	 * @param userid the userid
//	 * @return the string
//	 */
//	public String checkFreeTrialExpirationStatus(String userid) {
//		String free_trail_status="1";
//		int expireFlag = 1;
//		long free_trail_subscribers_count=Long.parseLong(ResourceBundle.getBundle("config").getString("free_trail_subscribers_count"));
//		try {
//			long subscribers_count=GAMongoDAO.getSubscriberCountForLoggedInUserForFreeTrail("subscribers_details",userid);
//			free_trail_status=new FreeTrialandCart().checkfreetrial(userid);
//			logger.info("Free Trail Status : "+free_trail_status);
//			//long subscribers_count=2000;
//			//String free_trail_status="0";
//			System.out.println("free_trail_status : "+free_trail_status+"  subscribers_count : "+subscribers_count);
//			if(subscribers_count<=free_trail_subscribers_count&&free_trail_status.equals("0")){
//				System.out.println("Free Train is Active");
//				logger.info("Free Trail Status : Free Train is Active");
//				expireFlag=0;
//			}else if(free_trail_status.equals("1")){
//				System.out.println("Free Train Date Expired");
//				logger.info("Free Trail Status : Free Train Date Expired");
//				expireFlag=1;
//			}else if(subscribers_count>free_trail_subscribers_count){
//				System.out.println("Subscriber Count is More");
//				logger.info("Free Trail Status : Subscriber Count is More");
//				expireFlag=1;
//			}
//		} catch (Exception ex) {
//			logger.info("Error checkFreeTrialExpirationStatus     : "+ex);
//		}
//		//return Integer.toString(expireFlag);
//		return free_trail_status;
//	}
//
//	/**
//	 * Checkfreetrial.
//	 *
//	 * @param userid the userid
//	 * @return the string
//	 */
//	public String checkfreetrial(String userid) {
//		int expireFlag = 0;
//		String free_trial_api = null;
//		 Character x = '_'; 
//		try {
//			userid = com.db.mongo.ga.GFG.replaceLastChar(userid,x);
//			free_trial_api = ResourceBundle.getBundle("config").getString("free_trial_api") + userid + "/LeadAutoConvFrTrial";
//			URL url = new URL(free_trial_api);
//			System.out.println("Step 1");
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			//System.out.println("Step 2");
//			conn.setRequestMethod("GET");
//			conn.connect();
//			System.out.println("Step 3");
//			InputStream in = conn.getInputStream();
//			System.out.println("Step 4");
//			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//			String text = reader.readLine();
//			System.out.println("Step 5");
//			//System.out.println(text);
//			JSONObject obj = new JSONObject(text);
//			expireFlag = obj.getInt("expireFlag");
//			System.out.println("f= "+Integer.toString(expireFlag));
//			conn.disconnect();
//		} catch (Exception ex) {
//			logger.info("Error checkfreetrial     : "+ex);
//		}
//		return Integer.toString(expireFlag);
//	}
//}
//
