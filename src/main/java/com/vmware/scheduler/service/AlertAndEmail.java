package com.vmware.scheduler.service;

import com.vmware.scheduler.domain.Alert;
import com.vmware.scheduler.domain.AlertRule;
import com.vmware.scheduler.domain.EmailService;
import com.vmware.scheduler.domain.TaskExecution;
import com.vmware.scheduler.repo.AlertRepository;
import com.vmware.scheduler.repo.AlertRuleRepository;
import com.vmware.scheduler.repo.TaskRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ppushkar on 10/28/2015.
 */
@Service
public class AlertAndEmail {

    @Autowired
    AlertRuleRepository alertRuleRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    AlertRepository alertRepository;

    public void checkAlert(TaskExecution execution) {
        List<AlertRule> alertRules = alertRuleRepository.findByTaskId(execution.getTaskId());

        if (alertRules != null && !alertRules.isEmpty()) {
            AlertRule alertRule = alertRules.get(0);
            try {
                if ("executionStatus".equals(alertRule.getFieldName())) {
                    if (execution.getExecutionStatus().toString()
                            .equals(alertRule.getFieldValue().toString())
                            && alertRule.getNotification() != null) {
                        Alert alert = new Alert();
                        alert.setTaskId(execution.getTaskId());
                        alert.setTaskExecutionId(execution.getId());
                        alert.setExecutionStatus(execution.getExecutionStatus());
                        alert.setTaskName(taskRepository.findOne(execution.getTaskId()).getName());
                        alert.setDate(execution.getStartedDate());
                        alertRepository.save(alert);
                        EmailService user = new EmailService();
                        user.send(alertRule.getNotification().getEmailId(),
                                alertRule.getNotification().getSubject(),
                                alertRule.getNotification().getBody());
                    }
                }
            } catch (Exception e) {
                System.out.println("Task not exist");
            }

        }
    }
}
