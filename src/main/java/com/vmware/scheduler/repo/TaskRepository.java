/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.repo;

import com.vmware.scheduler.domain.Task;
import com.vmware.scheduler.domain.TaskType;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TaskRepository extends MongoRepository<Task, String> {


    @Query(value = "'name' : ?0",fields="{ 'name' : 1}")
    public List<Task> getAllTasks(String temp);

    public Long countByActive(boolean isActive);

    public List<Task> findTasksByTaskType(TaskType type);

    public List<Task> findTasksByTaskType(String type);

    public List<Task> findTasksByActiveAndExpressionType(boolean active, String expressionType);
}
