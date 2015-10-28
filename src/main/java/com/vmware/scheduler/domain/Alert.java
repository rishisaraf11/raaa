package com.vmware.scheduler.domain;

import org.springframework.data.annotation.Id;

/**
 * Created by ppushkar on 10/27/2015.
 */
public class Alert {

    @Id
    String id;

    String schedulerId;

    String date;

    String executionStatus;

    public String getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(String schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(String executionStatus) {
        this.executionStatus = executionStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
