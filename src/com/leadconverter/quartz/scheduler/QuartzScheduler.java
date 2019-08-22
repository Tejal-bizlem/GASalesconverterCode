package com.leadconverter.quartz.scheduler;

import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.leadconverter.quartz.job.ActivateDraftList;
import com.leadconverter.quartz.job.CallRuleEngineJob;
import com.leadconverter.quartz.job.CampaignSheduleAndActivaterJob;
import com.leadconverter.quartz.job.FetchGoogleAnalyticsGADataJob;
import com.leadconverter.quartz.job.FetchPhpListGADataJob;
import com.leadconverter.quartz.job.ProcessQueueJob;

public class QuartzScheduler {
	final static Logger logger = Logger.getLogger(QuartzScheduler.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		QuartzScheduler qtzscheduler = new QuartzScheduler();
		qtzscheduler.runJobs("12","true");
	}

	public void runJobs(String hr,String flag) {
		try {
			    logger.info("******************  Sheduler Started  ************************");
				// First we must get a reference to a scheduler
		        SchedulerFactory sf = new StdSchedulerFactory();
		        Scheduler scheduler = sf.getScheduler();
		        
		        String campaign_shedule_and_activater_timestamp=ResourceBundle.getBundle("config")
		        		.getString("campaign_shedule_and_activater_timestamp");
		        String fetch_google_analytics_timestamp=ResourceBundle.getBundle("config")
		        		.getString("fetch_google_analytics_timestamp");
		        String process_queue_timestamp=ResourceBundle.getBundle("config")
		        		.getString("process_queue_timestamp");
		        		
		        if(flag.equals("true"))
		        {
		          //1. Scheduling And Activating Campaign
		          JobDetail campaignSheduleAndActivaterJob = JobBuilder.newJob(CampaignSheduleAndActivaterJob.class)
		                     .withIdentity("campaignSheduleAndActivaterJob", "campaign")
		                     .build();
	              String campaignSheduleAndActivaterTime="0 0/"+campaign_shedule_and_activater_timestamp+" * ? * * *";
	              CronTrigger campaignSheduleAndActivaterTrigger = TriggerBuilder.newTrigger()
                                  .withIdentity("campaignSheduleAndActivaterTrigger", "campaign")
                                  .withSchedule( CronScheduleBuilder.cronSchedule(campaignSheduleAndActivaterTime))
                                  .build();
        	      Date  campaignSheduleAndActivaterDate= scheduler.scheduleJob(campaignSheduleAndActivaterJob, campaignSheduleAndActivaterTrigger);
        	      scheduler.start();
		        	
		          //2. Fetching GA Data And Call Rule Engine and add subscribers in list
        	      
        	      JobDetail FetchGADataJob = JobBuilder.newJob(FetchGoogleAnalyticsGADataJob.class)
 	                     .withIdentity("FetchGADataJob", "gadata")
 	                     .build();
 		          //String schedule_time_str4="0 0/"+fetch_google_analytics_timestamp+" * ? * * *";
 		    	  String FetchGADataTime="0 45 18 * * ? ";
 		    	  CronTrigger FetchGADataTrigger = TriggerBuilder.newTrigger()
 		                              .withIdentity("FetchGADataTrigger", "gadata")
 		                              .withSchedule( CronScheduleBuilder.cronSchedule(FetchGADataTime))
 		                              .build();
 		    	  Date FetchGADataDate= scheduler.scheduleJob(FetchGADataJob, FetchGADataTrigger);
 		    	  scheduler.start();
 		    	  
 		    	  //3. Activating DraftList  //scheduler runs   date is date set for explore+1 like.. if capaign set today then based on rule output next category campaign will sent tomorrow
		          JobDetail activateDraftListJob = JobBuilder.newJob(ActivateDraftList.class)
                             .withIdentity("activateDraftListJob", "draftlist")
                             .build();
			      String activateDraftListTime="0 0/"+campaign_shedule_and_activater_timestamp+" * ? * * *";
			      CronTrigger activateDraftListTigger = TriggerBuilder.newTrigger()
			                          .withIdentity("activateDraftListTigger", "draftlist")
			                          .withSchedule( CronScheduleBuilder.cronSchedule(activateDraftListTime))
			                          .build();
				  Date  activateDraftListDate= scheduler.scheduleJob(activateDraftListJob, activateDraftListTigger);
				  scheduler.start();	
		        
				  //4. Processing Queue or Sending Mail
		    	  JobDetail processQueueJob = JobBuilder.newJob(ProcessQueueJob.class)
	                     .withIdentity("processQueueJob", "pq")
	                     .build();
		          String processQueueTime="0 0/"+process_queue_timestamp+" * ? * * *";
		          CronTrigger processQueueTrigger = TriggerBuilder.newTrigger()
		                              .withIdentity("processQueueTrigger", "pq")
		                              .withSchedule( CronScheduleBuilder.cronSchedule(processQueueTime))
		                              .build();
		    	  Date  ProcessQueueJobDate= scheduler.scheduleJob(processQueueJob, processQueueTrigger);
		    	  scheduler.start();
		    	}
		        else if(flag.equals("false"))
		        {
		        	logger.info("-----------------Sheduler Stopped-------------------");
		            scheduler.shutdown();
		           
		        }

		    } catch (SchedulerException e) {
			      e.printStackTrace();
		    } catch (ParseException e) {
			      e.printStackTrace();
		    }
	}
}

