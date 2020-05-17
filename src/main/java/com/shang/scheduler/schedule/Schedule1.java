package com.shang.scheduler.schedule;

import com.shang.scheduler.job.Job1;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @Author: shangjp
 * @Email: shangjp@163.com
 * @Date: 2020/5/17 13:24
 * @Description:
 */
public class Schedule1 {

    public static void main(String[] args) throws SchedulerException {

        //创建一个detail实例，将实例和HelloJob.class 进行绑定
        JobDetail jobDetail= JobBuilder.newJob(Job1.class).withIdentity("Job1").build();

        //创建一个Trigger触发器的实例，定义该job立即执行，并且每2秒执行一次，一直执行
        SimpleTrigger simpleTrigger= TriggerBuilder.newTrigger().withIdentity("myTrigger").startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever())
                .build();

        //复杂规则
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 08 0 * * ? ")).build();

        //创建 schedule实例
        StdSchedulerFactory stdSchedulerFactory=new StdSchedulerFactory();
        Scheduler scheduler=stdSchedulerFactory.getScheduler();
        scheduler.start();
//        scheduler.scheduleJob(jobDetail,simpleTrigger);
        scheduler.scheduleJob(jobDetail,cronTrigger);

        /**
         *
         *   1、创建调度工厂();   　　 //工厂模式
         2、根据工厂取得调度器实例();  　　//工厂模式
         3、Builder模式构建子组件<Job,Trigger> 　　 // builder模式, 如JobBuilder、TriggerBuilder、DateBuilder
         4、通过调度器组装子组件   调度器.组装<子组件1,子组件2...>  　　//工厂模式
         5、调度器.start();　　 //工厂模式
         */

    }

}
