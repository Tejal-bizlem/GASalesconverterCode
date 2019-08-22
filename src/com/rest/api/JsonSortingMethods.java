package com.rest.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.db.mongo.ga.AnalyticsDataInsertUpdate;


public class JsonSortingMethods {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String jsonArrStr = "[ { \"ga:sessionCount\": \"102\", \"Name\": \"Fargo Chan\" },{ \"ga:sessionCount\": \"135\", \"Name\": \"Fargo Chan\" },{ \"ga:sessionCount\": \"1375\", \"Name\": \"Fargo Chan\" },{ \"ga:sessionCount\": \"4432\", \"Name\": \"Aaron Luke\" },{ \"ga:sessionCount\": \"2452\", \"Name\": \"Dilip Singh\" },{ \"ga:sessionCount\": \"2502\", \"Name\": \"Fargo Chan\" },{ \"ga:sessionCount\": \"2521\", \"Name\": \"Fargo Chan\" },{ \"ga:sessionCount\": \"1020\", \"Name\": \"Fargo Chan\" }]";
		
		//sortJsonArray(jsonArrStr);
		
		String str="phplist1013 / email";
		str=str.substring(0, str.indexOf("/")-1);
		if(str.contains("phplist")){
		   System.out.println("Str : "+str);
		}
	}
	
	public static JSONArray sortJsonArray(String jsonArrStr,String ga_user){
		  //I assume that we need to create a JSONArray object from the following string
		    JSONArray jsonArr = new JSONArray(jsonArrStr);
		    JSONArray sortedJsonArray = new JSONArray();
		    System.out.println("jsonArr : "+jsonArr);

		    ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
		    for (int i = 0; i < jsonArr.length(); i++) {
		        jsonValues.add(jsonArr.getJSONObject(i));
		    }
		    Collections.sort( jsonValues, new Comparator<JSONObject>() {
		        //You can change "Name" with "ID" if you want to sort by ID
		        private static final String KEY_NAME = "ga:sessionCount";

		        @Override
		        public int compare(JSONObject a, JSONObject b) {
		        	Integer valA = null;
		        	Integer valB = null;
		            
		            try {
		            	valA = new Integer(Integer.parseInt((String) a.get(KEY_NAME)));
		            	valB = new Integer(Integer.parseInt((String) b.get(KEY_NAME)));
		            } 
		            catch (JSONException e) {
		                //do something
		            }

		            return valA.compareTo(valB);
		            //if you want to change the sort order, simply use the following:
		            //return -valA.compareTo(valB);
		        }
		    });

		    for (int i = 0; i < jsonArr.length(); i++) {
		        sortedJsonArray.put(jsonValues.get(i));
		    }
		    //System.out.println("sortedJsonArray : "+sortedJsonArray);
		    
		    //AnalyticsDataInsertUpdate.saveTempGAData(sortedJsonArray.toString(),ga_user);
		    getUniqueRecordsForJsonFields(sortedJsonArray.toString());
		    return sortedJsonArray;
	}
	
	public static void getUniqueRecordsForJsonFields(String jsonArrStr){
		  //I assume that we need to create a JSONArray object from the following string
			String country=null;
			String pagePath=null;
			String sessionCount=null;
			String dimension2=null;
			String channelGrouping=null;
			String sessiondurationBucket=null;
			String hostname=null;
			String timeOnPage=null;
			String bounces=null;
			String sourceMedium=null;
			String dateHourMinute=null;
			TreeSet<Integer> sessionCountUniqueValues = new TreeSet<Integer>();//ga:sessionCount
			TreeSet<String> pagePathUniqueValues = new TreeSet<String>();//ga:sourceMedium
			TreeSet<String> sourceMediumUniqueValues = new TreeSet<String>();
			Set<String> values = new TreeSet<String>();

		    
		    JSONArray jsonArr = new JSONArray(jsonArrStr);
		    //System.out.println("jsonArr : "+jsonArr);
		    
		    for (int i = 0; i < jsonArr.length(); i++) {
		        JSONObject ga_obj=jsonArr.getJSONObject(i);
		        sessionCountUniqueValues.add(Integer.parseInt(ga_obj.getString("ga:sessionCount")));
		        pagePathUniqueValues.add(ga_obj.getString("ga:pagePath"));
		        String source=ga_obj.getString("ga:sourceMedium");
		        if(source.contains("phplist")){
		        	sourceMediumUniqueValues.add(ga_obj.getString("ga:sourceMedium"));
				}
		        
		        //System.out.println("ga:pagePath : "+ga_obj.getString("ga:pagePath"));
		    }
		    System.out.println("The last element is: " + sessionCountUniqueValues.first());
		    System.out.println("The last element is: " + sessionCountUniqueValues.last());

		    printGAFieldsIntegerUniqueValues(sessionCountUniqueValues);
		    //System.out.println("TreeSet : "+values.size());
		    //System.out.println("TreeSet : "+values.size());
		    //printGAFieldsIntegerUniqueValues(sessionCountUniqueValues);
		    //printGAFieldsStringUniqueValues(pagePathUniqueValues);
		    //printGAFieldsStringUniqueValues(sourceMediumUniqueValues);
	}
	
	public static void printGAFieldsStringUniqueValues(TreeSet<String> FieldsUniqueValues){
		// create ascending iterator
	      Iterator iterator;
	      iterator = FieldsUniqueValues.iterator();

	      // displaying the Tree set data
	      System.out.println("Tree set data in ascending order: ");     
	      while (iterator.hasNext()) {
	         System.out.println(iterator.next() + " ");
	      }
		
	}
	public static void printGAFieldsIntegerUniqueValues(TreeSet<Integer> FieldsUniqueValues){
		// create ascending iterator
	      Iterator iterator;
	      iterator = FieldsUniqueValues.iterator();
         
	      // displaying the Tree set data
	      System.out.println("Tree set data in ascending order: ");     
	      while (iterator.hasNext()) {
	           System.out.println(iterator.next() + " ");
	    	  //AnalyticsDataInsertUpdate.findUniqueUrlsBasedSessionID(String.valueOf(iterator.next()));
	      }
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void sortBackJsonArray(String jsonArrStr){
		  //I assume that we need to create a JSONArray object from the following string
		    		    JSONArray jsonArr = new JSONArray(jsonArrStr);
		    JSONArray sortedJsonArray = new JSONArray();
		    System.out.println("jsonArr : "+jsonArr);

		    ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
		    for (int i = 0; i < jsonArr.length(); i++) {
		        jsonValues.add(jsonArr.getJSONObject(i));
		        //System.out.println("jsonArr.getJSONObject(i) : "+jsonArr.getJSONObject(i));
		    }
		    Collections.sort( jsonValues, new Comparator<JSONObject>() {
		        //You can change "Name" with "ID" if you want to sort by ID
		        private static final String KEY_NAME = "ga:sessionCount";

		        @Override
		        public int compare(JSONObject a, JSONObject b) {
		            String valA = new String();
		            String valB = new String();

		            try {
		                valA = (String) a.get(KEY_NAME);
		                valB = (String) b.get(KEY_NAME);
		            } 
		            catch (JSONException e) {
		                //do something
		            }

		            return valA.compareTo(valB);
		            //if you want to change the sort order, simply use the following:
		            //return -valA.compareTo(valB);
		        }
		    });

		    for (int i = 0; i < jsonArr.length(); i++) {
		        sortedJsonArray.put(jsonValues.get(i));
		       // System.out.println("sortedJsonArray : "+jsonValues.get(i));
		        
		    }
		    System.out.println("sortedJsonArray : "+sortedJsonArray);
	}
	

}
