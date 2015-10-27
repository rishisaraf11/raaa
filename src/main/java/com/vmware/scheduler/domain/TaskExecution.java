/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.domain;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

public class TaskExecution {
    @Id
    String id;
    @NotEmpty
    String taskId;
    ExecutionStatus executionStatus;
    String output;
    Date startedDate;
    Date completeDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(ExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }
}
