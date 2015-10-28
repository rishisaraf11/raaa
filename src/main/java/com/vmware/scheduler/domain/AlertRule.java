package com.vmware.scheduler.domain;

/**
 * Created by ppushkar on 10/27/2015.
 */
public class AlertRule {
    String fieldName;
    String fieldValue;

    public AlertRule() {
    }

    public AlertRule(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
