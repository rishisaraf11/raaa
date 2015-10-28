package com.vmware.scheduler.controller;

import com.vmware.scheduler.controller.Model.TaskRoot;
import com.vmware.scheduler.domain.*;
import com.vmware.scheduler.repo.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by ppushkar on 10/27/2015.
 */

@RestController
@RequestMapping("/api/alert")
public class AlertController {

    @Autowired
    AlertRepository alertRepository;

    @RequestMapping(method = RequestMethod.POST)
    public AlertRule createAlert(@RequestBody Map<String, Object> alertDetails) throws Exception {
        AlertRule alertRule = new AlertRule();
        if (alertDetails.get("taskId") != null) {
            alertRule.setTaskId(alertDetails.get("taskId").toString());
        } else {
            throw new Exception("Task id is mandatory");
        }
        if (alertDetails.get("fieldName") != null) {
            alertRule.setFieldName(alertDetails.get("fieldName").toString());
        } else {
            throw new Exception("Field Name is missing");
        }
        if (alertDetails.get("fieldValue") != null) {
            alertRule.setFieldValue(alertDetails.get("fieldValue").toString());
        } else {
            throw new Exception("FieldValue is missing");
        }
        if (alertDetails.get("notification") != null) {
            Map<String, String> data = (Map) alertDetails.get("notification");
            alertRule.setNotification(new Notification(data.get("emailId"), data.get("body"), data.get("subject")));
        } else {
            throw new Exception("Notification is Missing");
        }
        alertRepository.save(alertRule);
        return alertRule;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{taskid}/status")
    public void sendMail(@RequestBody Map<Object, Object> mailDetails) {
        Map<String, String> data = (Map) mailDetails.get("payload");
        Remail remail = new Remail();
        remail.execute(data.get("to").toString(), data.get("subject").toString(), data.get("body").toString());
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AlertRule> getAllTasks() {
        List<AlertRule> taskList = alertRepository.findAll();
        return taskList;
//        List<TaskRoot> responseList = new ArrayList();
//        taskList.forEach(task -> {
//            List<Scheduler> schedulers = alertRepository.findByTaskId(task.getId(), new Sort(Sort.Direction.DESC, "date"));
//            responseList.add(taskRoot);
//        });
//        return responseList;
    }
}

