package com.vmware.scheduler.controller;

import com.vmware.scheduler.domain.Alert;
import com.vmware.scheduler.domain.AlertRule;
import com.vmware.scheduler.domain.Notification;
import com.vmware.scheduler.domain.Remail;
import com.vmware.scheduler.repo.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by ppushkar on 10/27/2015.
 */

@RestController
@RequestMapping("/api/alert")
public class AlertController {

    @Autowired
    AlertRepository alertRepository ;

    @RequestMapping(method = RequestMethod.POST)
    public Alert createAlert(@RequestBody Map<String, Object> alertDetails) throws Exception{
        Alert alert = new Alert();
        if (alertDetails.get("taskId") != null) {
            alert.setTaskId(alertDetails.get("taskId").toString());
        } else  {
            throw new Exception("Task id is mandatory");
        }
        if(alertDetails.get("alertRule")!=null) {
            Map<String, String> data = (Map) alertDetails.get("alertRule");
            alert.setAlertRule(new AlertRule(data.get("fieldName"), data.get("fieldValue")));
        } else  {
            throw new Exception("Alert Rule is Manadatory");
        }
        if(alertDetails.get("notification")!=null) {
            Map<String, String> data = (Map) alertDetails.get("notification");
            alert.setNotification(new Notification(data.get("emailId"), data.get("body"), data.get("subject")));
        } else {
            throw new Exception("Notification is Missing");
        }
        alertRepository.save(alert);
        return alert;
    }

    @RequestMapping(method=RequestMethod.POST,value="/{taskid}/status")
    public void sendMail (@RequestBody Map<Object, Object> mailDetails) {
        Remail remail = new Remail((Map)mailDetails.get("payload"));
        remail.execute();
    }




    }

