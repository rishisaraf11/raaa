package com.vmware.scheduler.controller;

import com.vmware.scheduler.repo.TaskExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by djoshi on 10/29/2015.
 */
@Controller
@RequestMapping(value = "/api/execution")
public class TaskExecutionController {

    @Autowired
    TaskExecutionRepository taskExecutionRepository;

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAllTaskExecution(){
        taskExecutionRepository.deleteAll();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{executionId}")
    public void deleteExecution(@PathVariable String executionId){
        taskExecutionRepository.delete(executionId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{taskId}")
    public void deleteAllExecutionOfTask(@PathVariable String taskId){
        taskExecutionRepository.deleteByTaskId(taskId);
    }
}
