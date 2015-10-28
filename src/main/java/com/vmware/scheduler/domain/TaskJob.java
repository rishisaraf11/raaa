package com.vmware.scheduler.domain;

import com.vmware.scheduler.service.JobQueue;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by djoshi on 10/28/2015.
 */
public class TaskJob implements Job {


    public static final Logger logger = LoggerFactory.getLogger(TaskJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String taskId = jobExecutionContext.getJobDetail().getJobDataMap().get("taskId").toString();
        System.out.println("TaskId: "+ taskId);
        try {
            JobQueue.queue.put(taskId);
        } catch (InterruptedException e) {
            logger.error("Exception: Problem in JobQueue.");
            e.printStackTrace();
        }
    }
}
