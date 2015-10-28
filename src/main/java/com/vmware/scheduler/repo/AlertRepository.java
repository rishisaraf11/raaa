/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.vmware.scheduler.domain.Alert;

public interface AlertRepository extends MongoRepository<Alert, String>{


}
