package com.leadconverter.quartz.scheduler;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.leadconverter.quartz.job.CallRuleEngineJob;
import com.leadconverter.quartz.job.FetchPhpListGADataJob;
import com.leadconverter.quartz.job.ProcessQueueJob;

public class QuartzSchedulerForSingleJob {

	public static void main(String[] args) {
		QuartzSchedulerForSingleJob qtzscheduler = new QuartzSchedulerForSingleJob();
		qtzscheduler.runJobs("45","true");
	}

	public void runJobs(String sec,String flag) {
		try {
				// First we must get a reference to a scheduler
		        SchedulerFactory sf = new StdSchedulerFactory();
		        Scheduler scheduler = sf.getScheduler();
		        if(flag.equals("true"))
		        {
		        
		            //String schedule_time_str="0/"+sec+" * * * * ?";
			        //String schedule_time_str="* * 0/2 * * ?";
			        
			        String schedule_time_str="0 45/5 * ? * * *";
			    	JobDetail callRuleEngineJob5 = JobBuilder.newJob(ProcessQueueJob.class)
		                     .withIdentity("callRuleEngineJob5", "RuleEngine")
		                     .build();
			        //run every 20 seconds
			        CronTrigger callRuleEngineJobTrigger5 = TriggerBuilder.newTrigger()
			                              .withIdentity("callRuleEngineJobTrigger5", "RuleEngine")
			                              .withSchedule( CronScheduleBuilder.cronSchedule(schedule_time_str))
			                              .build();
			    	Date  callRuleEngineJobDate= scheduler.scheduleJob(callRuleEngineJob5, callRuleEngineJobTrigger5);
			    	scheduler.start();
			    	
		        }
		        else if(flag.equals("false"))
		        {
		        	
		            scheduler.shutdown();
		            System.out.println("------------scheduler stop --------------------");
		           
		        }

		    } catch (SchedulerException e) {
			      e.printStackTrace();
		    } catch (ParseException e) {
			      e.printStackTrace();
		    }
	}

}
