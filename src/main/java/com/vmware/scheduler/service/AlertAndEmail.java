package com.vmware.scheduler.service;

import com.vmware.scheduler.domain.AlertRule;
import com.vmware.scheduler.domain.EmailService;
import com.vmware.scheduler.domain.Scheduler;
import com.vmware.scheduler.repo.AlertRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ppushkar on 10/28/2015.
 */
@Service
public class AlertAndEmail {

    @Autowired
    AlertRuleRepository alertRuleRepository;

    public void checkAlert(Scheduler scheduler) {
        List<AlertRule> alertRules = alertRuleRepository.findByTaskId(scheduler.getTaskId());

        if (alertRules != null && !alertRules.isEmpty()) {
            AlertRule alertRule = alertRules.get(0);
            try {
                if ("executionStatus".equals(alertRule.getFieldName())) {
                    if (scheduler.getExecutionStatus().toString().equals(alertRule.getFieldValue().toString()) && alertRule.getNotification() != null) {

                        EmailService user = new EmailService();
                        user.send(alertRule.getNotification().getEmailId(), alertRule.getNotification().getSubject(), alertRule.getNotification().getBody());
                    }
                }
            } catch (Exception e) {
                System.out.println("Task not exist");
            }

        }
    }
}
