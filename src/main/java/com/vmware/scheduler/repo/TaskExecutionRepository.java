/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.vmware.scheduler.domain.TaskExecution;

public interface TaskExecutionRepository extends MongoRepository<TaskExecution, String> {

    List<TaskExecution> findByTaskId(String taskId);

    public List<TaskExecution> findByTaskId(String taskId, Sort sort);
}
