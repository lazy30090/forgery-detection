package com.juntong.forgerydetection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juntong.forgerydetection.entity.DetectionTask;

/**
 * 检测任务业务接口
 */
public interface DetectionTaskService extends IService<DetectionTask> {

    /**
     * 核心业务：提交检测任务
     * @param newsId 新闻ID
     * @return 任务ID
     */
    Long submitTask(Long newsId);
}