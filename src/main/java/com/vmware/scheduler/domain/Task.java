/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.domain;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class Task {

    @Id
    String id;

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    TaskType taskType;

    @Field
    String name;

    String expressionType;

    String expression;

    Map<String, Object> runInfo = new HashMap<>();

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    String date;

    // Scheduler scheduler;

    public Task() {

    }

    public Task(TaskType type, String name) {
        this.taskType = type;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(String expressionType) {
        this.expressionType = expressionType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Map<String, Object> getRunInfo() {
        return runInfo;
    }

    public void setRunInfo(Map<String, Object> runInfo) {
        this.runInfo = runInfo;
    }
}
