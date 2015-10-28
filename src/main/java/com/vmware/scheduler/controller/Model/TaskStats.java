/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.controller.Model;

public class TaskStats {
    long totalTasks;
    long totalExecution;
    long passExecution;
    long failExecution;

    public TaskStats(long totalTasks, long totalExecution, long passExecution, long failExecution) {
        this.totalTasks = totalTasks;
        this.totalExecution = totalExecution;
        this.passExecution = passExecution;
        this.failExecution = failExecution;
    }

    public TaskStats(long totalTasks, long totalExecution) {
        this.totalTasks = totalTasks;
        this.totalExecution = totalExecution;
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public long getTotalExecution() {
        return totalExecution;
    }

    public void setTotalExecution(long totalExecution) {
        this.totalExecution = totalExecution;
    }

    public long getPassExecution() {
        return passExecution;
    }

    public void setPassExecution(long passExecution) {
        this.passExecution = passExecution;
    }

    public long getFailExecution() {
        return failExecution;
    }

    public void setFailExecution(long failExecution) {
        this.failExecution = failExecution;
    }
}
