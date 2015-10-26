package com.vmware.scheduler.repo;

import com.vmware.scheduler.domain.TaskExecution;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by djoshi on 10/26/2015.
 */
public interface TaskExecutionRepository extends MongoRepository<TaskExecution, String> {
}
