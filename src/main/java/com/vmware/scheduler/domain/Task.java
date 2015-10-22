/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.domain;

import java.util.Map;
import org.springframework.data.annotation.Id;

public class Task {

    @Id
    String id;

    TaskType taskType;

    String name;

    Map<String,String> payload;

    public Task(TaskType taskType, String name, Object payload) {
        this.taskType = taskType;
        this.name = name;
        this.payload = (Map) payload;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
