package com.vmware.scheduler.repo;

import com.vmware.scheduler.domain.AlertRule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by ppushkar on 10/27/2015.
 */
public interface AlertRuleRepository extends MongoRepository<AlertRule,String> {

    public List<AlertRule> findByTaskId(String taskId);
}
