/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.controller.Model;

import java.util.ArrayList;
import java.util.List;

import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.domain.TaskExecution;

public class TaskFullDetail {
    Task task;
    List<TaskExecution> taskExecutions = new ArrayList();

    public TaskFullDetail(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<TaskExecution> getTaskExecutions() {
        return taskExecutions;
    }

    public void setTaskExecutions(List<TaskExecution> taskExecutions) {
        this.taskExecutions = taskExecutions;
    }
}
