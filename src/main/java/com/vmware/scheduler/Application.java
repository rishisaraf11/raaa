/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler;

import java.util.Calendar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
public class Application {

    private int getMinutesUntilTarget(int targetHour, int targetMinute) {
        int targetTime = targetHour*60 + targetMinute;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int actualTime = hour*60 + minute;
        return actualTime < targetTime ? targetTime - actualTime : targetTime - actualTime + 24*60;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
