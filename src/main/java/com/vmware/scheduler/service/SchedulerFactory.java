package com.vmware.scheduler.service;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by djoshi on 10/28/2015.
 */
public class SchedulerFactory {
    static org.quartz.SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

    public static final Logger logger = LoggerFactory.getLogger(SchedulerFactory.class);

    public static org.quartz.Scheduler getScheduler() {
        try {
            return schedFact.getScheduler();
        } catch (SchedulerException e) {
            logger.error("Quarts scheduler factory does not initialize scheduler properly!!!");
            e.printStackTrace();
        }
        return null;
    }

}
