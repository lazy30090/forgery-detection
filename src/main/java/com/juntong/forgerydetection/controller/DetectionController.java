package com.juntong.forgerydetection.controller;

import com.juntong.forgerydetection.common.ApiResponse;
import com.juntong.forgerydetection.entity.DetectionTask;
import com.juntong.forgerydetection.service.DetectionTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 伪造检测接口
 */
@RestController
@RequestMapping("/api/detect")
@RequiredArgsConstructor
@Tag(name = "2. 伪造检测模块", description = "核心业务：提交检测任务与查询结果") // <--- 加上这个
public class DetectionController {

    private final DetectionTaskService detectionTaskService;

    /**
     * 1. 提交检测任务
     * 场景：用户在详情页点击“立即检测”按钮
     */
    @PostMapping("/submit")
    @Operation(summary = "提交检测任务", description = "传入新闻ID，后台创建一个异步检测任务，并返回任务ID")
    public ApiResponse<Long> submit(@Parameter(description = "新闻ID") @RequestParam Long newsId) {
        Long taskId = detectionTaskService.submitTask(newsId);
        return ApiResponse.success(taskId);
    }

    /**
     * 2. 查询检测结果 (前端轮询)
     * 场景：提交后，前端每隔 2秒 调用一次，直到 status 变为 2(完成)
     */
    @GetMapping("/result/{taskId}")
    @Operation(summary = "查询检测进度/结果", description = "前端需要轮询此接口。status: 0-待检测, 1-检测中, 2-完成。当 status=2 时，字段 confidence 和 explanation 会有值。")
    public ApiResponse<DetectionTask> getResult(@Parameter(description = "任务ID (submit接口返回的)") @PathVariable Long taskId) {
        DetectionTask task = detectionTaskService.getById(taskId);

        if (task == null) {
            return ApiResponse.failed("检测任务不存在");
        }
        return ApiResponse.success(task);
    }
}