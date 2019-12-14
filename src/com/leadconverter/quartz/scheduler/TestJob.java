package com.leadconverter.quartz.scheduler;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
 
public class TestJob implements Job {
 
 
   public void execute(JobExecutionContext jExeCtx) throws JobExecutionException {
	   Date now = new Date();
     System.out.println("TestJob run successfully..."+now);
   }
 
}