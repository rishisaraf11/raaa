/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.controller;

import com.vmware.scheduler.controller.Model.TaskFullDetail;
import com.vmware.scheduler.controller.Model.TaskRoot;
import com.vmware.scheduler.controller.Model.TaskStats;
import com.vmware.scheduler.domain.Command;
import com.vmware.scheduler.domain.ExecutionStatus;
import com.vmware.scheduler.domain.Scheduler;
import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.domain.TaskExecution;
import com.vmware.scheduler.domain.TaskType;
import com.vmware.scheduler.repo.CmdRepository;
import com.vmware.scheduler.repo.SchedulerRepository;
import com.vmware.scheduler.repo.TaskExecutionRepository;
import com.vmware.scheduler.repo.TaskRepository;
import com.vmware.scheduler.service.QueryScheduler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.quartz.CronExpression;
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
    TaskExecutionRepository taskExecutionRepository;

    @Autowired
    SchedulerRepository schedulerRepository;

    @Autowired
    QueryScheduler queryScheduler;

    @Autowired
    CmdRepository cmdRepository;

    @RequestMapping(method = RequestMethod.POST)
    public Task createTask(@RequestBody Map<String, Object> taskDetails) throws Exception{
        Long activatedTasks = taskRepository.countByActive(true);
        if(activatedTasks>10)throw new Exception("You are exceeding 10 activated tasks limit.");
        Task task = new Task(TaskType.valueOf(taskDetails.get("type").toString()), taskDetails.get("name").toString());
        task.setExpressionType(taskDetails.get("expressionType").toString());
        if(taskDetails.get("active")==null){
            task.setActive(true);
        }else {
            task.setActive((Boolean) taskDetails.get("active"));
        }
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
            if(CronExpression.isValidExpression(taskDetails.get("expression").toString()))
                task.setExpression(taskDetails.get("expression").toString());
            else
                throw new Exception("Not valid Cron Expression");
        } else {
            LocalDateTime dateTime = LocalDateTime.parse(taskDetails.get("date").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            task.setDate(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        Task persisted = taskRepository.save(task);
        if("cron".equals(task.getExpressionType())){
            queryScheduler.scheduleCronTask(task.getExpression(),task.getId());
        }
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
            List<TaskExecution> schedulers = taskExecutionRepository.findByTaskId(task.getId(),
                    new Sort(Sort.Direction.DESC, "date"));
            TaskRoot taskRoot = new TaskRoot(task.getId(), task.getName(), task.getTaskType(), task.isActive());
            if (schedulers != null && !schedulers.isEmpty()) {
                taskRoot.withLastExecutionStatus(schedulers.get(0).getExecutionStatus());
                taskRoot.withLastExecutionTime(schedulers.get(0).getCompleteDate().toString());
                taskRoot.withTotalRun(schedulers.size());
                long passCount = schedulers.stream().filter(
                        s -> s.getExecutionStatus().equals(ExecutionStatus.EXECUTED)).count();
                long failCount = schedulers.stream().filter(
                        s -> s.getExecutionStatus().equals(ExecutionStatus.FAILED)).count();
                taskRoot.withRunData(Arrays.asList(passCount, failCount));
            }
            responseList.add(taskRoot);
        });
        return responseList;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{taskId}")
    public TaskFullDetail getTask(@PathVariable String taskId) {
        Task task = taskRepository.findOne(taskId);
        TaskFullDetail detail = new TaskFullDetail(task);
        List<TaskExecution> executions = taskExecutionRepository.findByTaskId(taskId);
        if (executions != null && !executions.isEmpty()) {
            detail.setTaskExecutions(executions);
        }
        return detail;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{taskId}")
    public void deleteTask(@PathVariable String taskId) {
         taskRepository.delete(taskId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/all")
    public void deleteAllTask() {
        taskRepository.deleteAll();
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

    @RequestMapping(method = RequestMethod.GET, value = "/stats")
    public TaskStats getStats() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskExecution> executions = taskExecutionRepository.findAll();
        if (executions != null) {
            long passCount = executions.stream().filter(execution -> execution.getExecutionStatus().equals(ExecutionStatus.EXECUTED)).count();
            long failCount = executions.stream().filter(execution -> execution.getExecutionStatus().equals(ExecutionStatus.FAILED)).count();
            return new TaskStats(tasks.size(), executions.size(), passCount, failCount);
        } else {
            return new TaskStats(tasks.size(), executions.size());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/activate/{taskId}/{active}")
    public void activateTask(@PathVariable String taskId, @PathVariable Boolean active) {
        Task task = taskRepository.findOne(taskId);
        task.setActive(active);
        taskRepository.save(task);
        if("cron".equals(task.getExpressionType())){
            queryScheduler.scheduleCronTask(task.getExpression(),task.getId());
        }
    }
}
