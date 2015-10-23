/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.service;

import java.util.PriorityQueue;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vmware.scheduler.comparator.TaskCronComparator;
import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.repo.TaskRepository;

@Component
public class QueryScheduler {

    PriorityQueue<Task> taskQueue = new PriorityQueue<>(new TaskCronComparator());
    @Autowired
    TaskRepository taskRepository;

    @Scheduled(fixedDelay = 10000)
    public void doSchedule() throws InterruptedException {
       //fire query get latest record going to run in next 10 mins
//        BasicQuery  query = new BasicQuery()
    }

    public PriorityQueue<Task> getTaskQueue() {
        return taskQueue;
    }


}
