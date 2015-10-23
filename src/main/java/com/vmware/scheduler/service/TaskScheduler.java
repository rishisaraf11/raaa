/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.service;

import com.vmware.scheduler.domain.Scheduler;
import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.repo.TaskRepository;
import java.util.PriorityQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskScheduler {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    QueryScheduler queryScheduler;
    @Autowired
    RestService restService;

    @Scheduled(fixedDelay = 1000)
    public void doSchedule() throws InterruptedException {
        //fire query get latest record going to run in next 10 mins

        PriorityQueue<Scheduler> taskSet = queryScheduler.getTaskQueue();
        if (!taskSet.isEmpty()) {
            // check if it can be executed
            Scheduler remove = taskSet.remove();
            Task task = taskRepository.findOne(remove.getTaskId());
            restService.execute(task.getPayload());
        }
    }
}
