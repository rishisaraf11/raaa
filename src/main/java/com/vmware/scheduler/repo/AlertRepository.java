package com.vmware.scheduler.repo;

import com.vmware.scheduler.domain.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ppushkar on 10/27/2015.
 */
public interface AlertRepository extends MongoRepository<Alert,String> {
}
