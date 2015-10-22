/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vmware.scheduler.domain.Task;

public interface TaskRepository extends MongoRepository<Task, String> {
}
