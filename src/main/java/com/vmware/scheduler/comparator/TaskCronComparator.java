/*
 * Copyright (c) 2015 VMware, Inc. All Rights Reserved.
 */

package com.vmware.scheduler.comparator;

import com.vmware.scheduler.domain.Scheduler;
import java.util.Comparator;

public class TaskCronComparator implements Comparator<Scheduler> {

    /*@Override
    public int compare(QueryScheduler.Pair o1, QueryScheduler.Pair o2) {
        int result = o1.scheduler.timeStamp.compareTo(o2.scheduler.timeStamp);
        if(result<0)return 1;
        else return 0;
    }*/

    @Override
    public int compare(Scheduler o1, Scheduler o2) {
        return o1.getTimeStamp().compareTo(o2.getTimeStamp());
    }
}
