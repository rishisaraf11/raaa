package com.vmware.scheduler.domain;

import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * Created by ppushkar on 10/27/2015.
 */
public class Alert {

    @Id
    String id;

    String taskId;

    AlertRule alertRule;

    Notification notification;


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

    public AlertRule getAlertRule() {
        return alertRule;
    }

    public void setAlertRule(AlertRule alertRule) {
        this.alertRule = alertRule;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
