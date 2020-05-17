package com.shang.scheduler.taskmanager;

import com.shang.scheduler.db.DbData;
import com.shang.scheduler.dto.TaskInfoDto;
import com.shang.scheduler.util.SchedulerManagerUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: shangjp
 * @Email: shangjp@163.com
 * @Date: 2020/5/17 13:46
 * @Description:
 */
public class ReschedulerJob implements Job {

   private Logger logger= LoggerFactory.getLogger(ReschedulerJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        refresh(jobExecutionContext);
    }

    /**
     * 属性定时任务
     * @param jobExecutionContext
     */
    private void refresh(JobExecutionContext jobExecutionContext){
        logger.debug("******进入动态刷新任务******");
        //[1] 读取静态变量实体类
        List<TaskInfoDto>  taskInfoDtoList = DbData.getInitDbData();
        logger.debug("加载任务数量: {}",taskInfoDtoList.size());

        //[2]批量处理任务,如果没有则添加
        taskInfoDtoList.stream().forEach(task->{
            try {
                //状态为1 :有效
                if (task.getStatus()==1){
                    Scheduler scheduler = jobExecutionContext.getScheduler();
                    JobKey jobKey = new JobKey(task.getName(), task.getGroup());
                    boolean exists = scheduler.checkExists(jobKey);
                    //判断任务调度是否在
                    if(exists){
                        //todo 任务已经存在,和运行的任务进行对比,判断是否发生变更 1删除重新加载 ,2不发生变化
                        //删除任务
//                        scheduler.deleteJob(jobKey);
                    }else {
                        //不存在-->加入任务调度
                        SchedulerManagerUtil.addJob(jobExecutionContext.getScheduler(),task);
                    }
                }
            }catch (Exception e){
                logger.error("动态添加任务异常!!!");
                e.printStackTrace();
            }
        });
        logger.debug("/*****退出动态刷新任务*****/");
    }
}
