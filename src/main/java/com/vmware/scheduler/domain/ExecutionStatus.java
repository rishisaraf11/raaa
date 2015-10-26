package com.vmware.scheduler.domain;

/**
 * Created by djoshi on 10/26/2015.
 */
public enum ExecutionStatus {
    SCHEDULED("schedule") ,NOT_SCHEDULED("not scheduled"), SKIPPED("skipped"), FAILED("failed") , EXECUTED("executed");

    public String logicalName;

    ExecutionStatus(String logicalName) {
        this.logicalName = logicalName;
    }
}
