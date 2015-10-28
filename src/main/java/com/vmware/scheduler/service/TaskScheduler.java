/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.service;

import com.vmware.scheduler.domain.ExecutionStatus;
import com.vmware.scheduler.domain.Scheduler;
import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.domain.TaskExecution;
import com.vmware.scheduler.repo.TaskExecutionRepository;
import com.vmware.scheduler.repo.TaskRepository;
import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    @Autowired
    CommandTaskService commandService;
    @Autowired
    TaskExecutionRepository taskExecutionRepository;

    public class WorkerThread implements Runnable{
        private Task task;

        public WorkerThread(Task task){
            this.task=task;
        }

        @Override
        public void run() {
            System.out.println(task.getName()+" Start. Time = "+new Date());
            TaskExecution execution = new TaskExecution();
            execution.setStartedDate(new Date());
            switch (task.getTaskType()) {
                case REST:{
                    String output = restService.execute(task.getRunInfo());
                    execution.setOutput(output);
                    if("failed".equals(output))execution.setExecutionStatus(ExecutionStatus.FAILED);
                    else execution.setExecutionStatus(ExecutionStatus.EXECUTED);
                    break;
                }
                case COMMAND: {
                    String output = commandService.execute(task.getRunInfo());
                    execution.setOutput(output);
                    if("0".equals(output))execution.setExecutionStatus(ExecutionStatus.EXECUTED);
                    else execution.setExecutionStatus(ExecutionStatus.FAILED);
                    break;
                }
            }
            execution.setCompleteDate(new Date());
            execution.setTaskId(task.getId());
            taskExecutionRepository.save(execution);
            System.out.println(task.getName()+" End. Time = "+new Date());
        }
    }

    private int getMinutesUntilTarget(int targetHour, int targetMinute) {
        int targetTime = targetHour*60 + targetMinute;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int actualTime = hour*60 + minute;
        return actualTime < targetTime ? targetTime - actualTime : targetTime - actualTime + 24*60;
    }

    @Scheduled(fixedDelay = 60000)
    public void doSchedule() throws InterruptedException {
        //fire query get latest record going to run in next 10 mins

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

        PriorityQueue<Scheduler> taskSet = queryScheduler.getTaskQueue();
        if (!taskSet.isEmpty()) {
            // check if it can be executed
            while(!taskSet.isEmpty()) {
                Scheduler remove = taskSet.remove();
                Task task = taskRepository.findOne(remove.getTaskId());

                String time = remove.getTimeStamp();
                String[] HM = time.split(" ");
                String[] HM1 = HM[1].split(":");
                int hour = Integer.parseInt(HM1[0]);
                int minute = Integer.parseInt(HM1[1]);
                System.out.println("Schedule Time: " + hour + ":" + minute);
                // can be implemented for seconds too
                int waiting = getMinutesUntilTarget(hour, minute);
                WorkerThread work = new WorkerThread(task);
                scheduler.schedule(work, waiting, TimeUnit.MINUTES);
            }

            Thread.sleep(30000);

            scheduler.shutdown();
            while(!scheduler.isTerminated()){
                //wait for all tasks to finish
            }
            System.out.println("Finished all threads");
        }
    }
}
