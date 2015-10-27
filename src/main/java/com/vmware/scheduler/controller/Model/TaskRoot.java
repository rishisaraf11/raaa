/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.controller.Model;

import java.util.List;

import com.vmware.scheduler.domain.ExecutionStatus;
import com.vmware.scheduler.domain.TaskType;

public class TaskRoot {

    String name;
    TaskType taskType;
    ExecutionStatus lastExecutionStatus;
    String lastExecutionTime;
    boolean active;
    long totalRun;
    List<Long> runData;

    public TaskRoot(String name, TaskType taskType,
            ExecutionStatus lastExecutionStatus, String lastExecutionTime, boolean active,
            long totalRun, List<Long> runData) {
        this.name = name;
        this.taskType = taskType;
        this.lastExecutionStatus = lastExecutionStatus;
        this.lastExecutionTime = lastExecutionTime;
        this.active = active;
        this.totalRun = totalRun;
        this.runData = runData;
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

    public void setLastExecutionStatus(ExecutionStatus lastExecutionStatus) {
        this.lastExecutionStatus = lastExecutionStatus;
    }

    public String getLastExecutionTime() {
        return lastExecutionTime;
    }

    public void setLastExecutionTime(String lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
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

    public void setTotalRun(long totalRun) {
        this.totalRun = totalRun;
    }

    public List<Long> getRunData() {
        return runData;
    }

    public void setRunData(List<Long> runData) {
        this.runData = runData;
    }
}
