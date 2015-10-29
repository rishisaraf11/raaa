/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.repo;

import com.vmware.scheduler.domain.TaskExecution;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskExecutionRepository extends MongoRepository<TaskExecution, String> {

    List<TaskExecution> findByTaskId(String taskId);

    public List<TaskExecution> findByTaskId(String taskId, Sort sort);

    public List<TaskExecution> deleteByTaskId(String taskId);
}
