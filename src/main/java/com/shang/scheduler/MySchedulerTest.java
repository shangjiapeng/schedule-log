package com.shang.scheduler;

import com.shang.scheduler.dto.TaskInfoDto;
import com.shang.scheduler.util.SchedulerManagerUtil;
import org.quartz.Scheduler;

/**
 * @Author: shangjp
 * @Email: shangjp@163.com
 * @Date: 2020/5/17 17:27
 * @Description:
 */
public class MySchedulerTest {

    public static void main(String[] args) throws Exception {
        //先实现一下这个基本的Quartz的任务,再来介绍一下Quartz的3个重要组成,JobDetail,Trigger,Scheduler
        //任务启动管理

        //SchedulerFactory 获得Scheduler
        Scheduler scheduler= SchedulerManagerUtil.getScheduler();

        //动态刷新任务
        SchedulerManagerUtil.addReschedulerJob(scheduler,new TaskInfoDto("group1",
                "resource","com.shang.scheduler.taskmanager.ReschedulerJob",
                20,10,1));

        SchedulerManagerUtil.run(scheduler,0);

    }
}
