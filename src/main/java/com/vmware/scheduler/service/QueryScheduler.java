/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.service;

import com.vmware.scheduler.comparator.TaskCronComparator;
import com.vmware.scheduler.domain.Scheduler;
import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.domain.TaskJob;
import com.vmware.scheduler.repo.SchedulerRepository;
import com.vmware.scheduler.repo.TaskRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.PriorityQueue;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.annotation.PostConstruct;

@Component
public class QueryScheduler {

    //need to change to priorityBlockingQueue
    PriorityQueue<Scheduler> taskQueue = new PriorityQueue<Scheduler>(new TaskCronComparator());
    TaskRepository taskRepository;
    SchedulerRepository schedulerRepository;

    @Autowired
    public QueryScheduler(TaskRepository taskRepository,
            SchedulerRepository schedulerRepository) {
        this.taskRepository = taskRepository;
        this.schedulerRepository = schedulerRepository;
    }

    @PostConstruct
    public void scheduledCronJob() {
        List<Task> tasks = taskRepository.findTasksByActiveAndExpressionType(true, "cron");
        tasks.forEach( task -> {
            this.scheduleCronTask(task.getExpression(), task.getId());
        });
    }

    @Scheduled(fixedDelay = 50000)
    public void doSchedule() throws InterruptedException {
       //fire query get latest record going to run in next 10 mins
        LocalDateTime  dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        int minute,hour;
        LocalDateTime dateTime2;
        if(dateTime.getMinute()+10>=60) {
            minute = (dateTime.getMinute() + 10) % 60;
            hour = (dateTime.getHour() + 1);
            dateTime2 = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), hour, minute);
        }else {
            dateTime2 = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute()+10);
        }

        String time1 = dateTime.format(formatter);
        String time2 = dateTime2.format(formatter);

        List<Scheduler> futureTasks = schedulerRepository.getUpcomingTask(time1,time2);
        taskQueue.clear();
        taskQueue.addAll(futureTasks);
    }

    public PriorityQueue<Scheduler> getTaskQueue() {
        return taskQueue;
    }

    public void scheduleCronTask(String cronExp, String taskId) {
        org.quartz.Scheduler sched = com.vmware.scheduler.service.SchedulerFactory.getScheduler();
        JobDetail job = newJob(TaskJob.class).withIdentity(new JobKey(taskId)).build();

        try {
            JobDetail jobDetail = sched.getJobDetail(job.getKey());
            if (jobDetail != null) {
                return;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        job.getJobDataMap().put("taskId", taskId);

        Trigger trigger = newTrigger().withIdentity(taskId)
                //.startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp)).build();

        try {
            sched.start();
            sched.scheduleJob(job, trigger);
            Thread.sleep(2000);
            //sched.shutdown(true);
        }catch (Exception e){
            System.out.println("Exception: Problem in scheduling job with cron expression!!!");
            e.printStackTrace();
        }
    }
}
