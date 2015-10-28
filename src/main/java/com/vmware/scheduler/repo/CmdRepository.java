package com.vmware.scheduler.repo;

import com.vmware.scheduler.domain.Command;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ppushkar on 10/23/2015.
 */
    public interface CmdRepository extends MongoRepository<Command,String> {
    }
