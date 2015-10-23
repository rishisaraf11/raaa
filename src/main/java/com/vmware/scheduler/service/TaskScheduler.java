/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.service;

import java.util.PriorityQueue;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vmware.scheduler.domain.Task;

@Component
public class TaskScheduler {

    @Autowired
    QueryScheduler queryScheduler;
    @Autowired
    RestService restService;

    @Scheduled(fixedDelay = 10000)
    public void doSchedule() throws InterruptedException {
        //fire query get latest record going to run in next 10 mins
        PriorityQueue<Task> taskSet = queryScheduler.getTaskQueue();
        if (taskSet.isEmpty()) {
            // check if it can be executed
            Task remove = taskSet.remove();
            restService.execute(null);

        }
    }
}
