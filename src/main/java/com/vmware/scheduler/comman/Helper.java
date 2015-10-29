package com.vmware.scheduler.comman;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by djoshi on 10/29/2015.
 */
public class Helper {

    final static Logger logger = LoggerFactory.getLogger(Helper.class);

    public static String getNextRun(String cron){
        CronExpression exp;
        Date date;
        try {
            exp = new CronExpression(cron);
            date = exp.getNextValidTimeAfter(new Date());
            String out = dateDiff(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()),
                    new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date));
            if(out != null)return out;
        } catch (ParseException e) {
            logger.error("Next run not working with cron expression [check Helper class]");
            e.printStackTrace();
        }
        return null;
    }

    static String dateDiff(String d1, String d2){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            Date date1 = format.parse(d1);
            Date date2 = format.parse(d2);

            long diff = date2.getTime() - date1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            String out = "";
            if(diffDays!=0){
                out = out + diffDays + " days ";
            }
            if(diffHours!=0){
                out = out + diffHours + " hours ";
            }
            if(diffMinutes!=0){
                out = out + diffMinutes + " minutes ";
            }
            if(diffSeconds!=0){
                out = out + diffSeconds + " seconds ";
            }
            System.out.println(out);
            return out;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
