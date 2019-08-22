package com.leadconverter.ga;

/*
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
*/
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util








import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;

public class HelloAnalyticsReporting {
  private static final String APPLICATION_NAME = "Lead Auto Converter";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final String KEY_FILE_LOCATION = "E://bizlem//GoogleAnalytics//client_secrets_service.json";
  private static final String VIEW_ID = "187146075";//187094993 187250921 187146075
  public static void main(String[] args) {
    try {
      AnalyticsReporting service = initializeAnalyticsReporting();
      
      

      GetReportsResponse response = getReport(service);
      printResponse(response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Initializes an Analytics Reporting API V4 service object.
   *
   * @return An authorized Analytics Reporting API V4 service object.
   * @throws IOException
   * @throws GeneralSecurityException
   */
  private static AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    GoogleCredential credential = GoogleCredential
        .fromStream(new FileInputStream(KEY_FILE_LOCATION))
        .createScoped(AnalyticsReportingScopes.all());

    // Construct the Analytics Reporting service object.
    return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME).build();
  }

  /**
   * Queries the Analytics Reporting API V4.
   *
   * @param service An authorized Analytics Reporting API V4 service object.
   * @return GetReportResponse The Analytics Reporting API V4 response.
   * @throws IOException
   */
  private static GetReportsResponse getReport(AnalyticsReporting service) throws IOException {
	  
	 // final List<String> SCOPES =
	           // Arrays.asList(SheetsScopes.SPREADSHEETS,SheetsScopes.DRIVE);
    // Create the DateRange object.
    DateRange dateRange = new DateRange();
    dateRange.setStartDate("2019-01-21");
    dateRange.setEndDate("2019-01-30");
    //dateRange.setStartDate("7DaysAgo");
    //dateRange.setEndDate("today");

    
    /*
    Get apiQuery = analytics.data().ga().get("ga:" + profile.getId(), // Table ID ="ga"+ProfileID
    		
    		 "2012-03-21", // Start date
    		
    		 "2012-05-04", // End date
    		
    		 "ga:visits"); // Metrics
   */
    
    // Create the Metrics object.
    Metric sessions = new Metric()
        .setExpression("ga:sessions")
        .setAlias("sessions");
    
  //Create the Dimensions object.
    Dimension browser = new Dimension()
            .setName("ga:browser");

    Dimension pageTitle = new Dimension().setName("ga:pageTitle");
    
    Dimension userId = new Dimension().setName("ga:dimension1");

    // Create the ReportRequest object.
    ReportRequest request = new ReportRequest()
        .setViewId(VIEW_ID)
        .setDateRanges(Arrays.asList(dateRange))
        .setMetrics(Arrays.asList(sessions))
        .setDimensions(Arrays.asList(browser,pageTitle,userId));

    ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
    requests.add(request);

    // Create the GetReportsRequest object.
    GetReportsRequest getReport = new GetReportsRequest()
        .setReportRequests(requests);

    // Call the batchGet method.
    GetReportsResponse response = service.reports().batchGet(getReport).execute();

    // Return the response.
    return response;
  }

  /**
   * Parses and prints the Analytics Reporting API V4 response.
   *
   * @param response An Analytics Reporting API V4 response.
   */
  private static void printResponse(GetReportsResponse response) {
    
	System.out.println("response size " + response.size());
    
	for (Report report: response.getReports()) {
    
      ColumnHeader header = report.getColumnHeader();
      
      List<String> dimensionHeaders = header.getDimensions();
      List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
      
      List<ReportRow> rows = report.getData().getRows();

      if (rows == null) {
         System.out.println("No data found for " + VIEW_ID);
         return;
      }

      for (ReportRow row: rows) {
        List<String> dimensions = row.getDimensions();
        List<DateRangeValues> metrics = row.getMetrics();

        for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
          System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
       	}

        for (int j = 0; j < metrics.size(); j++) {
          System.out.print("Date Range (" + j + "): ");
          DateRangeValues values = metrics.get(j);
          for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
            System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
          }
        }
      }
    }
  }
  /** Authorizes the installed application to access user's private data.
   *  client_secrets.json can be downloaded from Google developer console.
   *
   *  Make sure to enable the Google Analytics reporting api and create Oauth2 credentials.
   * 
  private static Credential authorize() throws Exception {
      // load client secrets
      GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
              new InputStreamReader(GoogleAnalyticsReportingSample.class.getResourceAsStream("/client_secrets.json")));

      // set up authorization code flow
      GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
              httpTransport, JSON_FACTORY, clientSecrets,
              Collections.singleton(AnalyticsReportingScopes.ANALYTICS_READONLY)).setDataStoreFactory(
              dataStoreFactory).build();
      // authorize
      return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }
  */

}

