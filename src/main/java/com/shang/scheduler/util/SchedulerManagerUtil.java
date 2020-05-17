package com.shang.scheduler.util;

import com.shang.scheduler.dto.TaskInfoDto;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.List;

/**
 * @Author: shangjp
 * @Email: shangjp@163.com
 * @Date: 2020/5/17 13:39
 * @Description:
 */
public class SchedulerManagerUtil {

    private static final String CURRENT_JOB_INFO_KEY = "current_job_info_key";

    /**
     * ScheduleFactory 用于Scheduler 的创建和管理
     * getScheduler() : 获取调度者
     *
     * @return
     * @throws Exception
     */
    public static Scheduler getScheduler() throws Exception {
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        return schedulerFactory.getScheduler();
    }

    /**
     * 初始化scheduler 获取scheduler 增加job任务
     *
     * @param scheduler
     * @param taskInfoDtoList
     * @throws Exception
     */
    public static void init(Scheduler scheduler, List<TaskInfoDto> taskInfoDtoList) throws Exception {
        for (TaskInfoDto taskInfoDto : taskInfoDtoList) {
            if (taskInfoDto.getStatus() == 1) {
                addJob(scheduler, taskInfoDto);
            }
        }
    }

    /**
     * 添加job
     *
     * @param scheduler
     * @param taskInfoDto
     * @throws Exception
     */
    public static void addJob(Scheduler scheduler, TaskInfoDto taskInfoDto) throws Exception {
        Class<Job> clz = (Class<Job>) Class.forName(taskInfoDto.getClassName());
        //增加job执行任务,JobBuilder--建造者模式,链式建造
        JobDetail jobDetail = JobBuilder.newJob(clz) ////newJob(): 定义job 任务,每个JobInfoDto是真正执行逻辑所在
                .withIdentity(taskInfoDto.getName(), taskInfoDto.getGroup())  //定义name /group
                .build();
        //实现了java.util.Map 接口。可以向 JobDataMap 中存入键/值对，
        //那些数据对可在的 Job 类中传递和进行访问。这是一个向 Job 传送配置的信息便捷方法
        jobDetail.getJobDataMap().put(CURRENT_JOB_INFO_KEY, taskInfoDto);

        //定义触发器。
        Trigger trigger = TriggerBuilder.newTrigger()//TriggerBuilder --建造者模式。链式建造。
                .withIdentity(taskInfoDto.getName(), taskInfoDto.getGroup())   //定义name/group
                .startAt(getStartTime(taskInfoDto.getIntervalInSeconds(), taskInfoDto.getDelayedSeconds()))   //根据job任务间隔时间来触发执行job任务 startNow():一旦加入scheduler，立即生效
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()) //增加Schedule 使用SimpleSchedule简单调度器，指定时间间隔触发。
                .build();
        //注册Trigger和job并进行调度
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 刷新job属性
     * 	 *  JobKey：JobKey是表明Job身份的一个对象
     * 	 *  getJobDetail();根据jobkey获取job详情
     * 	 *  getJobDataMap():根据创建时放入的map的key查询job配置信息
     * 	 * 重新创建触发器
     * @param scheduler
     * @param taskInfoDto
     * @throws Exception
     */
    public static void rescheduleJob(Scheduler scheduler, TaskInfoDto taskInfoDto) throws Exception {
        JobKey jobKey = JobKey.jobKey(taskInfoDto.getName(), taskInfoDto.getGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        TaskInfoDto oldJobInfo = (TaskInfoDto) jobDetail.getJobDataMap().get(CURRENT_JOB_INFO_KEY);
        oldJobInfo.setIntervalInSeconds(taskInfoDto.getIntervalInSeconds());
        oldJobInfo.setDelayedSeconds(taskInfoDto.getDelayedSeconds());

        TriggerKey triggerKey = TriggerKey.triggerKey(taskInfoDto.getName(), taskInfoDto.getGroup());
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(taskInfoDto.getName(), taskInfoDto.getGroup())
                .startAt(getStartTime(taskInfoDto.getIntervalInSeconds(), taskInfoDto.getDelayedSeconds()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(taskInfoDto.getIntervalInSeconds()).repeatForever())
                .build();
        scheduler.rescheduleJob(triggerKey,trigger);


    }

    /**
     * 停止job任务  unscheduleJob针对TriggerKey【deleteJob针对jobKey】
     * @param scheduler
     * @param taskInfoDto
     * @throws Exception
     */
    public static void unscheduleJob(Scheduler scheduler, TaskInfoDto taskInfoDto) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(taskInfoDto.getName(), taskInfoDto.getGroup());
        scheduler.unscheduleJob(triggerKey);
    }

    /**
     * 获取开始时间
     * @param delayedSeconds
     * @return
     */
    private static Date getStartTime(int delayedSeconds) {
        Date date = new Date(System.currentTimeMillis() + 1000 * delayedSeconds);
        return date;
    }

    private static Date getStartTime(int intervalInSeconds, int delayedSeconds) {
        Date date = new Date(System.currentTimeMillis() + 1000 * (delayedSeconds + intervalInSeconds));
        return date;
    }

    /**
     * 获取jobDetail
     * @param scheduler
     * @param taskInfoDto
     * @return
     * @throws Exception
     */
    public static JobDetail getJobDetail(Scheduler scheduler, TaskInfoDto taskInfoDto) throws Exception {
        JobKey jobKey = JobKey.jobKey(taskInfoDto.getName(), taskInfoDto.getGroup());
        return scheduler.getJobDetail(jobKey);
    }

    /**
     * 获取job信息
     * @param jobDetail
     * @return
     */
    public static TaskInfoDto getJobInfo(JobDetail jobDetail) {
        return (TaskInfoDto) jobDetail.getJobDataMap().get(CURRENT_JOB_INFO_KEY);
    }

    public static void addReschedulerJob(Scheduler scheduler, TaskInfoDto taskInfoDto) throws Exception {
        Class<Job> cls = (Class<Job>) Class.forName("com.shang.scheduler.taskmanager.ReschedulerJob");
        JobDetail jobDetail = JobBuilder.newJob(cls)
                .withIdentity(taskInfoDto.getName(), taskInfoDto.getGroup())
                .build();
        jobDetail.getJobDataMap().put(CURRENT_JOB_INFO_KEY, taskInfoDto);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(taskInfoDto.getName(), taskInfoDto.getGroup())
                .startAt(getStartTime(taskInfoDto.getDelayedSeconds()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(taskInfoDto.getIntervalInSeconds()).repeatForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }
    /**
     * 指定时间间隔启动任务
     *
     * @param scheduler
     * @param delayedSeconds
     */
    public static void run(Scheduler scheduler, int delayedSeconds) throws Exception {
        scheduler.startDelayed(delayedSeconds);
    }

}