/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.controller;

import com.vmware.scheduler.controller.Model.TaskRoot;
import com.vmware.scheduler.domain.ExecutionStatus;
import com.vmware.scheduler.domain.Scheduler;
import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.domain.TaskType;
import com.vmware.scheduler.domain.*;
import com.vmware.scheduler.repo.CmdRepository;
import com.vmware.scheduler.repo.SchedulerRepository;
import com.vmware.scheduler.repo.TaskRepository;
import com.vmware.scheduler.service.QueryScheduler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    CmdRepository cmdRepository;

    @RequestMapping(method = RequestMethod.POST)
    public Task createTask(@RequestBody Map<String, Object> taskDetails) {
        Task task = new Task(TaskType.valueOf(taskDetails.get("type").toString()), taskDetails.get("name").toString());
        task.setExpressionType(taskDetails.get("expressionType").toString());
        task.setActive(true);
        Map<String, Object> runInfo = task.getRunInfo();
        if (TaskType.REST.equals(task.getTaskType())) {
            runInfo.put("method",taskDetails.get("method").toString());
            runInfo.put("url",taskDetails.get("url").toString());
            runInfo.put("payload", taskDetails.get("payload"));
            runInfo.put("headers", taskDetails.get("headers"));
            runInfo.put("params", taskDetails.get("params"));
        }

        if ("cron".equals(taskDetails.get("expressionType").toString())) {
            task.setExpression(taskDetails.get("expression").toString());
        } else {
            LocalDateTime dateTime = LocalDateTime.parse(taskDetails.get("date").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            task.setDate(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        Task persisted = taskRepository.save(task);
        if(taskDetails.get("date")==null || taskDetails.get("date").toString().isEmpty() ){
            //return new Exception();
        }else {
            LocalDateTime dateTime = LocalDateTime.parse(taskDetails.get("date").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            Scheduler scheduler = new Scheduler(persisted.getId(),"India", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),ExecutionStatus.NOT_SCHEDULED);
            schedulerRepository.save(scheduler);
        }
        //if task need to be executed in next 10 mins;
//        queryScheduler.getTaskQueue().add(persisted);
        return persisted;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TaskRoot> getAllTasks() {
        List<Task> taskList = taskRepository.findAll(new Sort(Sort.Direction.ASC, "date"));
        List<TaskRoot> responseList = new ArrayList();
        taskList.forEach( task -> {
            List<Scheduler> schedulers = schedulerRepository.findByTaskId(task.getId(),new Sort(Sort.Direction.DESC,"date"));
            responseList.add(new TaskRoot(task.getId(),task.getName(), task.getTaskType(),schedulers.get(0).getExecutionStatus(),
                    schedulers.get(0).getTimeStamp(), task.isActive(), 100, Arrays.asList(40l, 50l)));
        });
        return responseList;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{taskId}")
    public Task getTask(@PathVariable String taskId) {
        return taskRepository.findOne(taskId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{taskId}/schedule")
    public Scheduler scheduleTask(@PathVariable String taskId, @RequestBody Map<String, String> scheduleDetails) {
        Scheduler scheduler = new Scheduler(taskId,scheduleDetails.get("timeZone"),scheduleDetails.get("timeStamp"),ExecutionStatus.SCHEDULED);
        Scheduler persisted = schedulerRepository.save(scheduler);
//        System.out.println(RestService.execute(taskRepository.findOne(taskId).getRunInfo()));
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

    @RequestMapping(method = RequestMethod.POST, value="/cmd")
    public Command cmdTask(@RequestBody Map<Object, Object> cmdDetails) {
        Command cmd = new Command((Map)cmdDetails.get("payload"));//,cmdDetails.get("user").toString(),cmdDetails.get("pwd").toString(),cmdDetails.get("cmd").toString());
        cmd.execute();
        Command persisted = cmdRepository.save(cmd);
        return persisted;
    }
}
