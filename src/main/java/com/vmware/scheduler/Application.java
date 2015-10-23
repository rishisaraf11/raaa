/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler;

import java.util.Calendar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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

        //ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        //service.scheduleAtFixedRate(daemonThread, waiting, 24*60, TimeUnit.MINUTES);
    }
}
