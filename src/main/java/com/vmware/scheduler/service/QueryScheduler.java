/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.service;

import com.vmware.scheduler.comparator.TaskCronComparator;
import com.vmware.scheduler.domain.Scheduler;
import com.vmware.scheduler.repo.SchedulerRepository;
import com.vmware.scheduler.repo.TaskRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.PriorityQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QueryScheduler {

    /*public class Pair {
        public Task task;
        public Scheduler scheduler;
    }*/

    //need to change to priorityBlockingQueue
    PriorityQueue<Scheduler> taskQueue = new PriorityQueue<Scheduler>(new TaskCronComparator());
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    SchedulerRepository schedulerRepository;

    @Scheduled(fixedDelay = 50000)
    public void doSchedule() throws InterruptedException {
       //fire query get latest record going to run in next 10 mins
        LocalDateTime  dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime2 = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute()+10);
      /*  Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
      */
        String time1 = dateTime.format(formatter);
        String time2 = dateTime2.format(formatter);

        List<Scheduler> futureTasks = schedulerRepository.getUpcomingTask(time1,time2);
        taskQueue.clear();
        taskQueue.addAll(futureTasks);
    }

    public PriorityQueue<Scheduler> getTaskQueue() {
        return taskQueue;
    }


}
