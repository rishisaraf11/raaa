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
import com.vmware.scheduler.repo.SchedulerRepository;
import com.vmware.scheduler.repo.TaskRepository;
import com.vmware.scheduler.service.QueryScheduler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

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
        }else if (TaskType.COMMAND.equals(task.getTaskType())){
            runInfo.put("hostIP",taskDetails.get("hostIP").toString());
            runInfo.put("username",taskDetails.get("username").toString());
            runInfo.put("password",taskDetails.get("password").toString());
            runInfo.put("command",taskDetails.get("command").toString());
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
        //queryScheduler.getTaskQueue().add(persisted);
        return persisted;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TaskRoot> getAllTasks() {
        List<Task> taskList = taskRepository.findAll(new Sort(Sort.Direction.ASC, "date"));
        List<TaskRoot> responseList = new ArrayList();
        taskList.forEach(task -> {
            List<Scheduler> schedulers = schedulerRepository.findByTaskId(task.getId(), new Sort(Sort.Direction.DESC, "date"));
            TaskRoot taskRoot = new TaskRoot(task.getId(), task.getName(), task.getTaskType(), task.isActive());
            if (schedulers != null && !schedulers.isEmpty()) {
                taskRoot.withLastExecutionStatus(schedulers.get(0).getExecutionStatus());
                taskRoot.withLastExecutionTime(schedulers.get(0).getTimeStamp());
                taskRoot.withTotalRun(schedulers.size());
                long passCount = schedulers.stream().filter(s -> s.getExecutionStatus().equals(ExecutionStatus.EXECUTED)).count();
                long failCount = schedulers.stream().filter(s -> s.getExecutionStatus().equals(ExecutionStatus.FAILED)).count();
                taskRoot.withRunData(Arrays.asList(passCount, failCount));
            }
            responseList.add(taskRoot);
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

    @RequestMapping(method=RequestMethod.POST,value="/{taskid}/status")
    public void sendMail (@RequestBody Map<Object, Object> mailDetails) {
        Remail remail = new Remail((Map)mailDetails.get("payload"));
        remail.execute();
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

    void scheduleCronTask(String cronExp, String taskName) {
        // define the job and tie it to our HelloJob class
        JobDetail job = newJob()
                .withIdentity("myJob", "group1")
                .build();

        // Trigger the job to run now, and then every 40 seconds
        Trigger trigger = newTrigger()
                .withIdentity(taskName)
                .startNow()
                .withSchedule(
                        CronScheduleBuilder.cronSchedule(cronExp))
                .build();

        // Tell quartz to schedule the job using our trigger
        try {
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

            org.quartz.Scheduler sched = schedFact.getScheduler();
            sched.start();
            sched.scheduleJob(job, trigger);
            Thread.sleep(2000);
            //sched.shutdown();
        }catch (Exception e){
            System.out.println("Exception: Problem in scheduling job with cron expression!!!");
            e.printStackTrace();
        }

    }
}
