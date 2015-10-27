/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.controller;

import com.vmware.scheduler.domain.ExecutionStatus;
import com.vmware.scheduler.domain.Scheduler;
import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.domain.TaskType;
import com.vmware.scheduler.repo.SchedulerRepository;
import com.vmware.scheduler.repo.TaskRepository;
import com.vmware.scheduler.service.QueryScheduler;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SchedulerRepository schedulerRepository;

    @Autowired
    QueryScheduler queryScheduler;

    @RequestMapping(method = RequestMethod.POST)
    public Task createTask(@RequestBody Map<String, Object> taskDetails) {
        Task task = new Task(TaskType.valueOf(taskDetails.get("type").toString()), taskDetails.get("name").toString(), taskDetails.get("expressionType").toString());
        task.setMethod(taskDetails.get("method").toString());
        task.setExpression(taskDetails.get("expression").toString());
        task.setHeaders((List)taskDetails.get("headers"));
        task.setParams((List)taskDetails.get("params"));
        task.setUrl(taskDetails.get("url").toString());
        task.setPayload(taskDetails.get("payload").toString());
        task.setDate(taskDetails.get("date").toString());

        Task persisted = taskRepository.save(task);
        /*if(taskDetails.get("time")==null || taskDetails.get("time").toString().isEmpty() ){
            //return new Exception();
        }else {
            Scheduler scheduler = schedulerRepository.save(new Scheduler(persisted.getId(),"India", taskDetails.get("time").toString(),ExecutionStatus.NOT_SCHEDULED));
            persisted.setScheduler(scheduler);
        }*/
        //if task need to be executed in next 10 mins;
//        queryScheduler.getTaskQueue().add(persisted);
        return persisted;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Task> getAllTasks() {
        return taskRepository.findAll(new Sort(Sort.Direction.ASC,"date"));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{taskId}")
    public Task getTask(@PathVariable String taskId) {
        return taskRepository.findOne(taskId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{taskId}/schedule")
    public Scheduler scheduleTask(@PathVariable String taskId, @RequestBody Map<String, String> scheduleDetails) {
        Scheduler scheduler = new Scheduler(taskId,scheduleDetails.get("timeZone"),scheduleDetails.get("timeStamp"),ExecutionStatus.SCHEDULED);
        Scheduler persisted = schedulerRepository.save(scheduler);
//        System.out.println(RestService.execute(taskRepository.findOne(taskId).getPayload()));
        return persisted;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{taskId}/schedule/{scheduleId}")
    public Scheduler getScheduledTask(@PathVariable String taskId, @PathVariable String scheduleId) {
        return schedulerRepository.findOne(scheduleId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/schedule")
    public List<Scheduler> getAllScheduledTask() {
        return schedulerRepository.findAll();
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{taskId}/schedule")
    public List<Scheduler> getAllScheduledEventOfTask(@PathVariable String taskId) {
        return schedulerRepository.findByTaskId(taskId);
    }
}
