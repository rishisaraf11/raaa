/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.repo;

import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.domain.TaskType;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {

    public List<Task> findTasksByTaskType(TaskType type);

    public List<Task> findTasksByTaskType(String type);
}
