/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.domain;

public enum TaskType {
    REST("Rest") , COMMAND("command");

    public String logicalName;

     TaskType(String logicalName) {
        this.logicalName = logicalName;
    }

  }
