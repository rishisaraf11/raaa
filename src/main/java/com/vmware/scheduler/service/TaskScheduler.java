/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.service;

import com.vmware.scheduler.domain.Scheduler;
import com.vmware.scheduler.domain.Task;
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

    public class WorkerThread implements Runnable{
        private Task task;

        public WorkerThread(Task task){
            this.task=task;
        }

        @Override
        public void run() {
            System.out.println(task.getName()+" Start. Time = "+new Date());

            switch (task.getTaskType()) {
                case REST:
                    restService.execute(task.getRunInfo());
                    break;
                case COMMAND:
                    commandService.execute(task.getRunInfo());
                    break;
            }
            processCommand();
            System.out.println(task.getName()+" End. Time = "+new Date());
        }

        private void processCommand() {
            try {
                //restService.execute(task.getRunInfo());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
