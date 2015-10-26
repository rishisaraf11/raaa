package com.vmware.scheduler.domain;

import org.springframework.data.annotation.Id;

/**
 * Created by djoshi on 10/26/2015.
 */
public class TaskExecution {

    @Id
    String id;
    String taskId;
    ExecutionStatus executionStatus;

    public TaskExecution(String id, ExecutionStatus executionStatus){
        this.id=id;
        this.executionStatus=executionStatus;
    }

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
}
