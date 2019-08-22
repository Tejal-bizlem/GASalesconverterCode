package com.leadconverter.quartz.scheduler;


import java.text.ParseException;
import java.util.Date;

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

import com.leadconverter.quartz.job.DisplayCurrentTime;

public class SimpleQuartzDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleQuartzDemo obj = new SimpleQuartzDemo();
		obj.runDemo();
	}

	public void runDemo() {
		try {

			// First we must get a reference to a scheduler
	        SchedulerFactory sf = new StdSchedulerFactory();
	        Scheduler sched = sf.getScheduler();

	        JobDetail job3 = JobBuilder.newJob(DisplayCurrentTime.class)
		            .withIdentity("currentTime-Job-3", "newGroup")
		            .build();

	       //run every 20 seconds
			CronTrigger trigger3 = TriggerBuilder.newTrigger()
		            .withIdentity("twentySec", "group2")
		            .withSchedule( CronScheduleBuilder.cronSchedule( "0/30 * * * * ?"))
		            .build();

	         Date ft = sched.scheduleJob(job3, trigger3);
	         sched.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
