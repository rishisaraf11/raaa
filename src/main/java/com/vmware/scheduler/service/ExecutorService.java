package com.vmware.scheduler.service;

import com.vmware.scheduler.domain.ExecutionStatus;
import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.domain.TaskExecution;
import com.vmware.scheduler.repo.TaskExecutionRepository;
import com.vmware.scheduler.repo.TaskRepository;
import java.util.Date;

/**
 * Created by djoshi on 10/28/2015.
 */
public class ExecutorService implements Runnable {

    JobQueue jobQueue;

    TaskRepository taskRepository;

    RestService restService;

    CommandTaskService commandService;

    TaskExecutionRepository taskExecutionRepository;

    public ExecutorService(JobQueue jobQueue, TaskRepository taskRepository, RestService restService, CommandTaskService commandService, TaskExecutionRepository taskExecutionRepository) {
        this.jobQueue = jobQueue;
        this.taskRepository = taskRepository;
        this.restService = restService;
        this.commandService = commandService;
        this.taskExecutionRepository = taskExecutionRepository;
    }

    @Override
    public void run() {
        while(true){
            String msg;
            try {
                while(jobQueue.getQueue().isEmpty())Thread.sleep(10000);
                msg = jobQueue.getQueue().take();
                Task task = taskRepository.findOne(msg);
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
