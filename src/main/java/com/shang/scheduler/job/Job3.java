package com.shang.scheduler.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: shangjp
 * @Email: shangjp@163.com
 * @Date: 2020/5/17 13:23
 * @Description:
 */
public class Job3 implements Job {

    private Logger logger = LoggerFactory.getLogger(Job1.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("-------------->Job3 begin<---------- ");

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("当前的业务时间是:" + dateFormat.format(date));

        logger.debug("-------------->Job3 end<-----------");
    }
}