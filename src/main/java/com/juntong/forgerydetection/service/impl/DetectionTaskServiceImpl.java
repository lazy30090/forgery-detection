package com.juntong.forgerydetection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juntong.forgerydetection.entity.DetectionTask;
import com.juntong.forgerydetection.enums.TaskStatusEnum;
import com.juntong.forgerydetection.mapper.DetectionTaskMapper;
import com.juntong.forgerydetection.service.DetectionTaskService;
import org.springframework.stereotype.Service;

@Service
public class DetectionTaskServiceImpl extends ServiceImpl<DetectionTaskMapper, DetectionTask> implements DetectionTaskService {

    @Override
    public Long submitTask(Long newsId) {
        // 1. 创建一个新的任务对象
        DetectionTask task = new DetectionTask();
        task.setNewsId(newsId);

        // 2. 设置初始状态
        task.setStatus(TaskStatusEnum.PENDING.getCode());

        // 3. 保存到数据库 (MyBatis-Plus 提供的方法)
        this.save(task);

        // 4. 返回生成的任务ID
        return task.getId();
    }
}