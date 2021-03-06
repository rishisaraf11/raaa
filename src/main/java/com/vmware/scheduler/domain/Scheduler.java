package com.vmware.scheduler.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

/**
 * Created by djoshi on 10/22/2015.
 */
public class Scheduler {

    @Id
    public String id;

    @NotEmpty
    public String taskId;

    public String timeZone;

    public String timeStamp;

    ExecutionStatus executionStatus;

    public Scheduler(String taskId, String timeZone, String timeStamp, ExecutionStatus executionStatus){
        this.taskId = taskId;
        this.timeZone = timeZone;
        this.timeStamp = timeStamp;
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

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(ExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

}
