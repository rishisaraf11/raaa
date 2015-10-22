package com.vmware.scheduler.repo;

import com.vmware.scheduler.domain.Scheduler;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by djoshi on 10/22/2015.
 */
public interface SchedulerRepository extends MongoRepository<Scheduler,String> {
}
