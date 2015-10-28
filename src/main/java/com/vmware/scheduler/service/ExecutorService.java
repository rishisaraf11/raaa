package com.vmware.scheduler.service;

import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.repo.TaskRepository;

/**
 * Created by djoshi on 10/28/2015.
 */
public class ExecutorService implements Runnable {

    JobQueue jobQueue;

    TaskRepository taskRepository;

    RestService restService;

    public ExecutorService(JobQueue jobQueue, TaskRepository taskRepository, RestService restService) {
        this.jobQueue = jobQueue;
        this.taskRepository = taskRepository;
        this.restService = restService;
    }

    @Override
    public void run() {
        while(true){
            String msg;
            try {
                while(jobQueue.getQueue().isEmpty())Thread.sleep(10000);
                msg = jobQueue.getQueue().take();
                Task task = taskRepository.findOne(msg);
                switch (task.getTaskType()) {
                    case REST:
                        restService.execute(task.getRunInfo());
                        break;
                    case COMMAND:
                        //command service to execute
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
