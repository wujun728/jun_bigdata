package com.easytoolsoft.easyreport.scheduler.task;

import java.util.TimerTask;

import com.easytoolsoft.easyreport.scheduler.util.TaskUtils;

/**
 * @author Wujun
 */
public class ReloadDataTask extends TimerTask {
    @Override
    public void run() {
        TaskUtils.reloadTasks();
    }
}
