package com.juntong.forgerydetection.controller;

import com.juntong.forgerydetection.common.ApiResponse;
import com.juntong.forgerydetection.entity.DetectionTask;
import com.juntong.forgerydetection.service.DetectionTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 伪造检测接口
 */
@RestController
@RequestMapping("/api/detect")
@RequiredArgsConstructor
public class DetectionController {

    private final DetectionTaskService detectionTaskService;

    /**
     * 1. 提交检测任务
     * 场景：用户在详情页点击“立即检测”按钮
     * URL: POST /api/detect/submit?newsId=123
     */
    @PostMapping("/submit")
    public ApiResponse<Long> submit(@RequestParam Long newsId) {
        Long taskId = detectionTaskService.submitTask(newsId);
        return ApiResponse.success(taskId);
    }

    /**
     * 2. 查询检测结果 (前端轮询)
     * 场景：提交后，前端每隔 2秒 调用一次，直到 status 变为 2(完成)
     * URL: GET /api/detect/result/{taskId}
     */
    @GetMapping("/result/{taskId}")
    public ApiResponse<DetectionTask> getResult(@PathVariable Long taskId) {
        DetectionTask task = detectionTaskService.getById(taskId);

        if (task == null) {
            return ApiResponse.failed("检测任务不存在");
        }

        // 返回整个任务对象，前端根据 task.status 判断进度
        // status: 0-待检测, 1-检测中, 2-完成, 3-失败
        return ApiResponse.success(task);
    }
}