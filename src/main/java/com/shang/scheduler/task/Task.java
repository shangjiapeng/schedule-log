package com.shang.scheduler.task;

import com.shang.scheduler.dto.TaskInfoDto;

/**
 * @Author: shangjp
 * @Email: shangjp@163.com
 * @Date: 2020/5/17 13:43
 * @Description:
 */
public interface Task {

    public boolean run(TaskInfoDto taskInfo);

}
