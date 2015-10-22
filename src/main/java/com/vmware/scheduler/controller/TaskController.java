/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.domain.TaskType;
import com.vmware.scheduler.repo.TaskRepository;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @RequestMapping(method = RequestMethod.POST)
    public Task createTask(@RequestBody Map<String, String> taskDetails) {
        Task task = new Task(TaskType.valueOf(taskDetails.get("taskType")),
                taskDetails.get("name"));
        Task persisted = taskRepository.save(task);
        return persisted;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{taskId}")
    public Task getTask(@PathVariable String taskId) {
        return taskRepository.findOne(taskId);
    }
}
