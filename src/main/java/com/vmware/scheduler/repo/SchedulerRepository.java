package com.vmware.scheduler.repo;

import com.vmware.scheduler.domain.Scheduler;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by djoshi on 10/22/2015.
 */
public interface SchedulerRepository extends MongoRepository<Scheduler,String> {

    @Query("{'timeStamp' : {'$gt' : ?0, '$lt' : ?1}}")
    public List<Scheduler> getUpcomingTask(String gt, String lt);

    public List<Scheduler> findByTaskId(String taskId);
}
