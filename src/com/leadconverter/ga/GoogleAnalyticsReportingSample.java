package com.leadconverter.ga;

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
import com.google.api.services.analyticsreporting.v4.model.OrderBy;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.google.api.services.analyticsreporting.v4.model.Segment;

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

/**
 * Sample for the Google Analytics reporting API with java
 *
 * @author Linda Lawton
 */
public class GoogleAnalyticsReportingSample {
	final static Logger logger = Logger.getLogger(AccesAndRefreshToken.class);

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
    private static Credential authorize() throws Exception {
    	InputStream in    = new FileInputStream("E://bizlem//GoogleAnalytics//client_secrets_123.json");
    	//InputStream in    = new FileInputStream("/usr/local/GoogleAnalyticsJsonFile/client_secrets_123.json");
    	
    	
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
        		new InputStreamReader(in));
               //new InputStreamReader(GoogleAnalyticsReportingSample.class.getResourceAsStream("E://bizlem//GoogleAnalytics//client_secrets_service.json")));

        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(AnalyticsReportingScopes.ANALYTICS_READONLY)).setDataStoreFactory(
                dataStoreFactory).build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
    
    private static Credential authorizeTkn(String accessToken) throws Exception {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        /*
        	InputStream in    = new FileInputStream("E://bizlem//GoogleAnalytics//client_secrets_123.json");
            // load client secrets
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
            		new InputStreamReader(in));
                   //new InputStreamReader(GoogleAnalyticsReportingSample.class.getResourceAsStream("E://bizlem//GoogleAnalytics//client_secrets_service.json")));

            // set up authorization code flow
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(AnalyticsReportingScopes.ANALYTICS_READONLY)).setDataStoreFactory(
                    dataStoreFactory).build();
            // authorize
            return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
          */
          return credential;
        }

    public static void main(String[] args) {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            Credential credential = authorize();
            // set up global Analytics instance
            service = new  AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
                    APPLICATION_NAME).build();
            // run commands
            // Fetch Google Analytics Data
            
          //dateRange.setStartDate("2019-01-21");
            //dateRange.setEndDate("2019-01-30");
            String str_start_date="2019-01-21";
            String str_end_date="2019-03-13";
            String view_id="190758260";
            GetReportsResponse response = fetchData(service,str_start_date,str_end_date,view_id);
            
            // Print Results
            //response.getReports().get(0);
            //System.out.println("isEmpty : "+response.getReports().isEmpty()+"  size : "+response.getReports().size());
            System.out.println("response.getReports().get(0) "+response.getReports().get(0));
            logger.info(printResults(response.getReports()));
            // success!
            
            /*
             * Note: This code assumes you have an authorized Analytics service object.
             */

            /*
             * This request gets an existing custom dimension.
             */
            //service.
            /*
             try {
              CustomDimension = analytics.management().customDimensions()
                  .get("123456", "UA-123456-1", "ga:dimension2").execute();
            } catch (GoogleJsonResponseException e) {
              System.err.println("There was a service error: "
                  + e.getDetails().getCode() + " : "
                  + e.getDetails().getMessage());
            }
            */
            return;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }
    
    public static JSONObject getGAReport(String innput,String accessToken,String str_start_date,String str_end_date,String view_id) {
    	JSONObject gareport=null;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            //Credential credential = authorize();
            Credential credential = authorizeTkn(accessToken);
            // set up global Analytics instance
            service = new  AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
                    APPLICATION_NAME).build();
            // run commands
            // Fetch Google Analytics Data
            //String str_start_date="2019-01-21";
            //String str_end_date="2019-01-30";
            GetReportsResponse response = fetchData(service,str_start_date,str_end_date,view_id);
            gareport=printResults(response.getReports());
            //return;
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

        //Segment browserSegment =
        	      //buildSimpleSegment("Sessions with Safari browser", "ga:browser", "Safari");
        
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
                //.setFiltersExpression("ga:dimension2==geetanjali@bizlem.com")
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

    /** Fetching the data from Google Analytics and returning the response*/
    public static GetReportsResponse fetchDataBackUP(AnalyticsReporting service,String str_start_date,String str_end_date) throws IOException{

    	DateRange dateRange = new DateRange();
    	dateRange.setStartDate(str_start_date);
        dateRange.setEndDate(str_end_date);
        
        //dateRange.setStartDate("2019-01-21");
        //dateRange.setEndDate("2019-01-30");

        //https://developers.google.com/analytics/devguides/reporting/core/v4/samples
        // Create the Metrics object.
        Metric sessions = new Metric()
                .setExpression("ga:sessions")
                .setAlias("sessions");

        //Create the Dimensions object.
        Dimension browser = new Dimension()
                .setName("ga:browser");
        //Dimension customdimension1 = new Dimension().setName("ga:dimension202");
        
        Dimension pageTitle = new Dimension().setName("ga:pageTitle");
        
        Dimension userId = new Dimension().setName("ga:dimension1");
        
        Dimension emailId = new Dimension().setName("ga:dimension2");
        
        Dimension sessionCount = new Dimension().setName("ga:sessionCount");
        
        Dimension sessionDurationBucket = new Dimension().setName("ga:sessionDurationBucket");
        
        /*
         * 400 Bad Request
			{
			  "code" : 400,
			  "errors" : [ {
			    "domain" : "global",
			    "message" : "Unknown dimension(s): ga:dimension102\nFor details see https://developers.google.com/analytics/devguides/reporting/core/dimsmets.",
			    "reason" : "badRequest"
			  } ],
			  "message" : "Unknown dimension(s): ga:dimension102\nFor details see https://developers.google.com/analytics/devguides/reporting/core/dimsmets.",
			  "status" : "INVALID_ARGUMENT"
			}
         */
        
        // Create the ReportRequest object. 187146075 187136906  
        //ga:190758260   187146075
        ReportRequest request = new ReportRequest()
                .setViewId("ga:190758260")
                .setDateRanges(Arrays.asList(dateRange))
                .setMetrics(Arrays.asList(sessions))
                .setDimensions(Arrays.asList(browser,pageTitle,userId,emailId,sessionCount,sessionDurationBucket));
                //.setDimensions(Arrays.asList(customdimension1))
                

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        return service.reports().batchGet(getReport).execute();
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
}
