package com.vmware.scheduler.service;

import com.vmware.scheduler.repo.TaskExecutionRepository;
import com.vmware.scheduler.repo.TaskRepository;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by djoshi on 10/28/2015.
 */
@Component
public class JobQueue {

    public static BlockingQueue<String> queue = new ArrayBlockingQueue<>(10); //size of queue 10

    @Autowired
    public JobQueue(TaskRepository repository, RestService restService, CommandTaskService command, TaskExecutionRepository executionRepository,  AlertAndEmail alertAndEmail) {
        new Thread(new ExecutorService(this, repository, restService, command, executionRepository, alertAndEmail)).start();    //to consume task from jobqueue, run forever
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }


}
