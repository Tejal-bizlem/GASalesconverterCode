package com.rest.api;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.db.mongo.ga.AnalyticsDataInsertUpdate;
import com.db.mongo.ga.MongoSetup;
import com.google.api.AccesAndRefreshToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.DynamicSegment;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.OrFiltersForSegment;
import com.google.api.services.analyticsreporting.v4.model.OrderBy;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.Segment;
import com.google.api.services.analyticsreporting.v4.model.SegmentDefinition;
import com.google.api.services.analyticsreporting.v4.model.SegmentDimensionFilter;
import com.google.api.services.analyticsreporting.v4.model.SegmentFilter;
import com.google.api.services.analyticsreporting.v4.model.SegmentFilterClause;
import com.google.api.services.analyticsreporting.v4.model.SimpleSegment;
import com.google.api.AccesAndRefreshToken;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.leadconverter.ga.GoogleAnalyticsReportingSample;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Test {
	final static Logger logger = Logger.getLogger(Test.class);
	
	/**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "DAIMTO-GoogleAnalyticsReportingSample/1.0";

    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File("E://bizlem//GoogleAnalytics//", ".store//reporting_sample");
    
   // private static final java.io.File DATA_STORE_DIR =
           // new java.io.File("/usr/local/GoogleAnalyticsJsonFile/", ".store/reporting_sample");
    
    ///usr/local/GoogleAnalyticsJsonFile
    
    private static final java.io.File DATA_STORE_DIR1 =
            new java.io.File("E://bizlem//GoogleAnalytics//client_secrets_service.json");
    
    //"E://bizlem//GoogleAnalytics//client_secrets_service.json"

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static AnalyticsReporting service ;

    /** Authorizes the installed application to access user's private data.
     *  client_secrets.json can be downloaded from Google developer console.
     *
     *  Make sure to enable the Google Analytics reporting api and create Oauth2 credentials.
     * */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DateFormat date_formatter_with_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    JSONArray ga_users_json_arr=AnalyticsDataInsertUpdate.findGAUserCredentials();
		getGAData(ga_users_json_arr);
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
	    		    str_start_date="2019-02-01";
	 	    	    str_end_date="2019-05-23";     
	    		    System.out.println("Start Date(From) : "+str_start_date+"  End Date(To) : "+str_end_date);
	    			logger.info("Start Date(From) : "+str_start_date+"  End Date(To) : "+str_end_date);
	    			//last_run_date=AnalyticsDataInsertUpdate.getGADataTimeStamp();
	    			logger.info("Going... to Get Last Run Date, Last Run Date is : "+last_run_date);
	    			logger.info("Setting... Next Run Date, Next Run Date is : "+date_formatter_with_timestamp.format(start_date));
	    			//AnalyticsDataInsertUpdate.insertGADataTimeStamp(date_formatter_with_timestamp.format(start_date));
	    			try{
	    				String google_analytics_response=null;
	    				JSONParser parser =new JSONParser();
	    			    google_analytics_response=getGAReport("input",accessToken,str_start_date,str_end_date,view_id).toString();
	    	            //System.out.println("Data Received: 1 ");
	    	            JSONObject ga_json_obj= (JSONObject) parser.parse(google_analytics_response.toString());
	    	            //System.out.println("Json Response "+ga_json_obj.get("data"));
	    	            JSONArray ga_data_json_arr=(JSONArray) ga_json_obj.get("data");
	    	            if(ga_data_json_arr.size()>0){
	    	               //JsonSortingMethods.sortJsonArray(ga_json_obj.get("data").toString(),username);
	    	               AnalyticsDataInsertUpdate.saveTempGAData("google_analytics_data_temp",ga_json_obj.get("data").toString(),username);
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
	
	
		
		public static JSONObject getGAReport(String innput,String accessToken,String str_start_date,String str_end_date,String view_id) {
	    	JSONObject gareport=null;
	        try {
	            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
	            //Credential credential = authorize();
	            Credential credential = authorizeTkn(accessToken);
	            // set up global Analytics instance
	            service = new  AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
	                    APPLICATION_NAME).build();
	            GetReportsResponse response = fetchData(service,str_start_date,str_end_date,view_id);
	            gareport=printResults(response.getReports());
	            //return;
	            
	            //multipleSegmentsRequest(service);
	        } catch (IOException e) {
	            System.err.println(e.getMessage());
	        } catch (Throwable t) {
	            t.printStackTrace();
	        }
	        return gareport;
	        //System.exit(1);
	    }
		/** Fetching the data from Google Analytics and returning the response*/
	    public static GetReportsResponse fetchData(AnalyticsReporting service,String str_start_date,String str_end_date,String view_id) throws IOException{
            GetReportsRequest getReport =null;
	    	GetReportsResponse getResponse=null;
	    	//str_start_date="2019-02-01";
	    	//str_end_date="2019-05-23";
	    	try{
	    	DateRange dateRange = new DateRange();
	    	dateRange.setStartDate(str_start_date);
	        dateRange.setEndDate(str_end_date);
	        // Create the Metrics object.
	        Metric sessions = new Metric()
	                .setExpression("ga:sessions")
	                .setAlias("sessions");
	        Metric bounces = new Metric()
	                .setExpression("ga:bounces")
	                .setAlias("bounces");
	        
	        Metric timeOnPage = new Metric()
	                .setExpression("ga:timeOnPage")
	                //.setExpression("ga:timeOnPage")
	                .setAlias("timeOnPage");
	        
	        //Create the Dimensions object.
	        Dimension browser = new Dimension()
	                .setName("ga:browser");
	        
	        Dimension userEmailId = new Dimension().setName("ga:dimension2");
	        //Dimension source = new Dimension().setName("ga:source");
	        Dimension pagePath = new Dimension().setName("ga:pagePath");
	        Dimension sessionCount = new Dimension().setName("ga:sessionCount");
	        Dimension sessionDurationBucket = new Dimension().setName("ga:sessionDurationBucket");
	        
	        //Dimension campaign = new Dimension().setName("ga:campaign");
	        //Dimension medium = new Dimension().setName("ga:medium");
	        Dimension sourceMedium = new Dimension().setName("ga:sourceMedium");
	        //Dimension pageTitle = new Dimension().setName("ga:pageTitle");
	        Dimension hostname = new Dimension().setName("ga:hostname");
	        //Dimension date = new Dimension().setName("ga:date");
	        Dimension dateHourMinute = new Dimension().setName("ga:dateHourMinute");
	        Dimension country = new Dimension().setName("ga:country");
	        Dimension channelGrouping = new Dimension().setName("ga:channelGrouping");
	        //ga:country==United
	        
	     // Create the segment dimension.
	        Dimension segmentDimensions = new Dimension()
	            .setName("ga:segment");

	        Segment browserSegment =
	        	      buildSimpleSegment("Sessions with Safari browser", "ga:browser", "Safari");
	        
	        // Create the ReportRequest object. 187146075 187136906  
	        //ga:190758260   187146075 ga:190758260
	        
	        OrderBy order_by=new OrderBy();
	        //order_by.setFieldName("ga:pagePath"); //
	        //order_by.setFieldName("ga:sessionCount");//ga:dateHourMinute
	        //order_by.setFieldName("ga:dateHourMinute");//ga:sessionCount
	        order_by.setFieldName("ga:sessionCount");
	        //order_by.setSortOrder(arg0);
	        List<OrderBy> list_order_by=new ArrayList<OrderBy>();
	        list_order_by.add(order_by);
	        
	        ReportRequest request = new ReportRequest()
	                .setViewId("ga:"+view_id)
	                .setDateRanges(Arrays.asList(dateRange))
	                .setMetrics(Arrays.asList(bounces,timeOnPage))
	                .setDimensions(Arrays.asList(userEmailId,pagePath,sessionCount,sessionDurationBucket,channelGrouping,country,sourceMedium,hostname,dateHourMinute))
	                .setFiltersExpression("ga:dimension2!=NULL")//purva.sawant@bizlem.com  akhilesh@bizlem.com
	                //.setFiltersExpression("ga:dimension2!=NULL")
	                .setOrderBys(list_order_by); //"ga:dimension2!=NULL,ga:source==google"
	        
	        ReportRequest request1 = new ReportRequest();
	        
	        //request1.setOrderBys(arg0)
	        
	        
	        
	        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
	        requests.add(request);

	        // Create the GetReportsRequest object.
	        getReport = new GetReportsRequest()
	                .setReportRequests(requests);

	        // Call the batchGet method.
	        getResponse=service.reports().batchGet(getReport).execute();
	        }catch(Exception ex){
	        	logger.info("Inside fetchData() Method Got Error : "+ex.getMessage());
	        	System.out.println("Inside fetchData() Method Got Error : "+ex.getMessage());
	        }
	        return getResponse;

	    	
	    }

		private static Credential authorizeTkn(String accessToken) throws Exception {
	        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
	        return credential;
	    }
		/** Printing the results. */
	    @SuppressWarnings("unchecked")
		private static JSONObject printResults(List<Report> reports) {
	    	//JSONParser parser =new JSONParser();
			
	    	 JSONArray ga_json_arr=new JSONArray();
	    	 JSONObject ga_json_obj=null;
	    	 JSONObject ga_final_json_obj=new JSONObject();
	    	
	        for (Report report : reports) {
	        	
	        	
	            ColumnHeader header = report.getColumnHeader();
	            List<String> dimensionHeaders = header.getDimensions();
	            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
	            List<ReportRow> rows = report.getData().getRows();
	            if(rows!=null){
		            System.out.println("Reports Size : " + reports.size());
		            logger.info("Reports Size : " + reports.size());
		            logger.info("Rows Size : " + rows.size());
		            logger.info("Metric Headers Size : " + metricHeaders.size());//
		          //logger.info("Metric Headers Name : " + metricHeaders.get(0).getName());
		            logger.info("Dimension Headers Size " + dimensionHeaders.size());
		            ga_final_json_obj.put("total", rows.size());
		            ga_final_json_obj.put("dimensionHeaderSize", dimensionHeaders.size());
		            ga_final_json_obj.put("metricHeaderSize", metricHeaders.size());
		            for (ReportRow row : rows) {
		            	ga_json_obj=new JSONObject();
		            	
		                List<String> dimensions = row.getDimensions();
		                List<DateRangeValues> metrics = row.getMetrics();
		                //System.out.println("--------------dimensionHeaders Data -------------");
		                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
		                    //System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
		                    ga_json_obj.put(dimensionHeaders.get(i), dimensions.get(i));
		                }
		                //System.out.println("--------------metricHeaders Data -------------");
		                for (int j = 0; j < metrics.size(); j++) {
		                    //System.out.print("Date Range (" + j + "): ");
		                    DateRangeValues values = metrics.get(j);
		                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
		                        //System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
		                        ga_json_obj.put(metricHeaders.get(k).getName(), values.getValues().get(k));
		                    }
		                }
		                ga_json_arr.add(ga_json_obj);
		                //break;
		            }
	            }else{
	            	System.out.println("No Records Found From Google Analytics");
	            	//break;
	            }
	            ga_final_json_obj.put("data", ga_json_arr);
	        }
	        return ga_final_json_obj;
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    private static void multipleSegmentsRequest(AnalyticsReporting analyticsreporting) throws IOException {
			  // Create the DateRange object.
			  DateRange dateRange = new DateRange();
			  dateRange.setStartDate("2019-05-23");
			  dateRange.setEndDate("2019-05-23");

			  // Create the Metrics object.
			  Metric sessions = new Metric().setExpression("ga:sessions").setAlias("sessions");

			  Dimension browser = new Dimension().setName("ga:browser");

			  //Dimension segmentDimensions = new Dimension().setName("ga:segment");
			  
			  Dimension userEmailId = new Dimension().setName("ga:dimension2");

			  Segment browserSegment =
			      buildSimpleSegment("Sessions with Safari browser", "ga:browser", "Safari");

			  Segment countrySegment =
			      buildSimpleSegment("Sessions from United States", "ga:country", "United States");

			  // Create the ReportRequest object.
			  ReportRequest request =
			      new ReportRequest()
			          .setViewId("190758260")
			          .setDateRanges(Arrays.asList(dateRange))
			          .setDimensions(Arrays.asList(browser,userEmailId))
			          .setMetrics(Arrays.asList(sessions));
			//.setSegments(Arrays.asList(browserSegment, countrySegment))
	          

			  // Create the GetReportsRequest object.
			  GetReportsRequest getReport = new GetReportsRequest().setReportRequests(Arrays.asList(request));

			  // Call the batchGet method.
			  GetReportsResponse response = analyticsreporting.reports().batchGet(getReport).execute();

			  System.out.println(printResults(response.getReports()));
			}
	    private static Segment buildSimpleSegment(
			    String segmentName, String dimension, String dimensionFilterExpression) {
			  // Create Dimension Filter.
			  SegmentDimensionFilter dimensionFilter =
			      new SegmentDimensionFilter()
			          .setDimensionName(dimension)
			          .setOperator("EXACT")
			          .setExpressions(Arrays.asList(dimensionFilterExpression));

			  // Create Segment Filter Clause.
			  SegmentFilterClause segmentFilterClause =
			      new SegmentFilterClause().setDimensionFilter(dimensionFilter);

			  // Create the Or Filters for Segment.
			  OrFiltersForSegment orFiltersForSegment =
			      new OrFiltersForSegment().setSegmentFilterClauses(Arrays.asList(segmentFilterClause));

			  // Create the Simple Segment.
			  SimpleSegment simpleSegment =
			      new SimpleSegment().setOrFiltersForSegment(Arrays.asList(orFiltersForSegment));

			  // Create the Segment Filters.
			  SegmentFilter segmentFilter = new SegmentFilter().setSimpleSegment(simpleSegment);

			  // Create the Segment Definition.
			  SegmentDefinition segmentDefinition =
			      new SegmentDefinition().setSegmentFilters(Arrays.asList(segmentFilter));

			  // Create the Dynamic Segment.
			  DynamicSegment dynamicSegment =
			      new DynamicSegment().setSessionSegment(segmentDefinition).setName(segmentName);

			  // Create the Segments object.
			  Segment segment = new Segment().setDynamicSegment(dynamicSegment);
			  return segment;
			}
	    
	    public static void DateDifferentExample(String dateStart,String dateStop){
			//HH converts hour in 24 hours format (0-23), day calculation
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
			Date d1 = null;
			Date d2 = null;
		
			try {
				d1 = format.parse(dateStart);
				d2 = format.parse(dateStop);
		
				//in milliseconds
				long diff = d2.getTime() - d1.getTime();
		
				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000) % 24;
				long diffDays = diff / (24 * 60 * 60 * 1000);
		
				System.out.print(diffDays + " days, ");
				System.out.print(diffHours + " hours, ");
				System.out.print(diffMinutes + " minutes, ");
				System.out.print(diffSeconds + " seconds.");
		
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

}
