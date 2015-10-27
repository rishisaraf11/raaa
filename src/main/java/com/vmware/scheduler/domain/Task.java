/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.domain;

import java.util.List;
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

    String method;

    String expressionType;

    String expression;

    List<Map<String,String>> headers;

    List<Map<String,String>> params;

    String url;

    String payload;

    String date;

    // Scheduler scheduler;

    public Task() {

    }

    public Task(TaskType type, String name, String expressionType) {
        this.taskType = type;
        this.name = name;
        this.expressionType = expressionType;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public List<Map<String, String>> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Map<String, String>> headers) {
        this.headers = headers;
    }

    public List<Map<String, String>> getParams() {
        return params;
    }

    public void setParams(List<Map<String, String>> params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
