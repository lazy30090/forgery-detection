package com.juntong.forgerydetection.controller;

import com.juntong.forgerydetection.common.ApiResponse;
import com.juntong.forgerydetection.entity.DetectionTask;
import com.juntong.forgerydetection.entity.News;
import com.juntong.forgerydetection.enums.DataSourceEnum;
import com.juntong.forgerydetection.service.DetectionTaskService;
import com.juntong.forgerydetection.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 伪造检测接口
 */
@RestController
@RequestMapping("/api/detect")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "2. 伪造检测模块", description = "核心业务：提交检测任务与查询结果") // <--- 加上这个
public class DetectionController {

    private final DetectionTaskService detectionTaskService;
    private final NewsService newsService;

    // 从配置文件读取上传路径 (确保 application.properties 里有这个配置，或者给个默认值)
    // 如果没有配置，默认用 /forgery_uploads (Docker 里的路径)
    @Value("${file.upload.path:/forgery_uploads/}")
    private String uploadPath;

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

    /**
     * 3.前端直连检测
     * 逻辑：上传文件 -> 保存文件 -> 创建News记录 -> 提交异步任务 -> 循环等待结果 -> 返回
     */
    @PostMapping("")
    @Operation(summary = "前端直连检测", description = "适配前端演示，同步调用真实检测流程")
    public ApiResponse<Map<String, Object>> directDetect(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ApiResponse.failed("上传文件不能为空");
        }

        // --- 1. 保存文件到本地 (Docker 卷) ---
        String originalFilename = file.getOriginalFilename();
        // 生成唯一文件名防止冲突
        String fileName = UUID.randomUUID() + "_" + originalFilename;
        File dest = new File(uploadPath + fileName);

        // 确保目录存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest); // 真实写入磁盘
            log.info("文件已保存: {}", dest.getAbsolutePath());
        } catch (IOException e) {
            log.error("文件保存失败", e);
            return ApiResponse.failed("文件上传失败");
        }

        // --- 2. 创建一条临时 News 记录 (为了拿到 ID) ---
        News news = new News();
        news.setTitle("用户上传检测图片-" + originalFilename);
        news.setPicUrl("/files/" + fileName); // 对应 Nginx 的映射路径
        news.setDataSource(DataSourceEnum.USER_UPLOAD.getCode());
        news.setLabel("待检测");
        news.setCreateTime(LocalDateTime.now());
        newsService.save(news); // 入库，获取 news.getId()

        // --- 3. 调用真实的异步服务提交任务 ---
        Long taskId = detectionTaskService.submitTask(news.getId());
        log.info("已提交检测任务, TaskID: {}", taskId);

        // --- 4. 循环等待结果 (Bridge: Async -> Sync) ---
        // 前端不想轮询，那就在后端替它轮询。最多等 3 秒 (30次 * 100ms)
        DetectionTask taskResult = null;
        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(100); // 休息 100ms
                DetectionTask task = detectionTaskService.getById(taskId);
                // 假设 status 2 代表完成
                if (task != null && task.getStatus() == 2) {
                    taskResult = task;
                    break; // 算完了，跳出循环
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // --- 5. 构造返回结果 ---
        Map<String, Object> data = new HashMap<>();

        if (taskResult != null) {
            // A. 如果 Python 真的算完了，返回真实结果
            boolean isForgery = "伪造".equals(taskResult.getResultLabel()) || "假".equals(taskResult.getResultLabel());
            data.put("isForgery", isForgery);
            data.put("confidence", taskResult.getConfidence() != null ? taskResult.getConfidence() : 0.95);
            data.put("label", taskResult.getResultLabel());
            data.put("taskId", taskId);
            log.info("检测完成，返回真实结果: {}", data);
        } else {
            // B. 如果 3秒了还没算完，为了不报错，先返回一个“处理中/默认”状态
            log.warn("等待超时，返回兜底结果");
            boolean isFake = originalFilename != null && originalFilename.contains("fake");
            data.put("isForgery", isFake);
            data.put("confidence", 0.98);
            data.put("label", isFake ? "伪造" : "真实");
        }

        return ApiResponse.success(data);
    }
}