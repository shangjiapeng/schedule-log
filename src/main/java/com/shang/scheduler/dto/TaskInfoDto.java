package com.shang.scheduler.dto;

/**
 * @Author: shangjp
 * @Email: shangjp@163.com
 * @Date: 2020/5/17 13:26
 * @Description:
 */
public class TaskInfoDto {

    String group;
    String name;
    String className;
    int intervalInSeconds;
    int delayedSeconds;
    int status;

    /**
     *
     * @param group 任务所属的组
     * @param name 任务名称
     * @param className 类名称
     * @param intervalInSeconds 重复间隔
     * @param delayedSeconds 指定时间间隔启动调度
     * @param status 状态码
     */
    public TaskInfoDto(String group, String name, String className, int intervalInSeconds, int delayedSeconds, int status) {
        this.group = group;
        this.name = name;
        this.className = className;
        this.intervalInSeconds = intervalInSeconds;
        this.delayedSeconds = delayedSeconds;
        this.status = status;
    }



    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getIntervalInSeconds() {
        return intervalInSeconds;
    }

    public void setIntervalInSeconds(int intervalInSeconds) {
        this.intervalInSeconds = intervalInSeconds;
    }

    public int getDelayedSeconds() {
        return delayedSeconds;
    }

    public void setDelayedSeconds(int delayedSeconds) {
        this.delayedSeconds = delayedSeconds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}

