package com.shang.scheduler.db;

import com.shang.scheduler.dto.TaskInfoDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shangjp
 * @Email: shangjp@163.com
 * @Date: 2020/5/17 13:23
 * @Description:
 */
public class DbData {

    public static List<TaskInfoDto> getInitDbData() {
        List<TaskInfoDto> list = new ArrayList<>();
        list.add(new TaskInfoDto("group2","job1",
                "com.shang.scheduler.job.Job1",2,2,1));

        list.add(new TaskInfoDto("group2","job2",
                "com.shang.scheduler.job.Job1",4,2,1));

        list.add(new TaskInfoDto("group2", "job3",
                "com.shang.scheduler.job.Job1", 10, 10, 1));

        return list;
    }
}
