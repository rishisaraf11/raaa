package com.vmware.scheduler.service;

import com.vmware.scheduler.domain.AlertRule;
import com.vmware.scheduler.domain.Remail;
import com.vmware.scheduler.domain.Scheduler;
import com.vmware.scheduler.repo.AlertRepository;
/**
 * Created by ppushkar on 10/28/2015.
 */
public class AlertAndEmail {
    AlertRepository alertRepository ;
    public void execAndstatus (Scheduler sch,String taskId)
    {
        AlertRule alertRule = new AlertRule();
        alertRule=alertRepository.findOne(taskId);
        if(alertRule.getFieldName()=="status"){
            if (sch.getExecutionStatus().toString().equals(alertRule.getFieldValue().toString())) {
                Remail user = new Remail();
                user.execute("hi","hell","how");
            }
        }
    }
}
