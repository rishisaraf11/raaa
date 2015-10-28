/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.controller.Model;

import java.util.Arrays;
import java.util.List;

import com.vmware.scheduler.domain.ExecutionStatus;
import com.vmware.scheduler.domain.TaskType;

public class TaskRoot {

    String id;
    String name;
    TaskType taskType;
    ExecutionStatus lastExecutionStatus;
    String lastExecutionTime;
    boolean active;
    long totalRun = 0;
    List<Long> runData = Arrays.asList(0l,0l);

    public TaskRoot(String id, String name, TaskType taskType, boolean active) {
        this.id = id;
        this.name = name;
        this.taskType = taskType;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public ExecutionStatus getLastExecutionStatus() {
        return lastExecutionStatus;
    }

    public TaskRoot withLastExecutionStatus(ExecutionStatus lastExecutionStatus) {
        this.lastExecutionStatus = lastExecutionStatus;
        return this;
    }

    public String getLastExecutionTime() {
        return lastExecutionTime;
    }

    public TaskRoot withLastExecutionTime(String lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getTotalRun() {
        return totalRun;
    }

    public TaskRoot withTotalRun(long totalRun) {
        this.totalRun = totalRun;
        return this;
    }

    public List<Long> getRunData() {
        return runData;
    }

    public TaskRoot withRunData(List<Long> runData) {
        this.runData = runData;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
