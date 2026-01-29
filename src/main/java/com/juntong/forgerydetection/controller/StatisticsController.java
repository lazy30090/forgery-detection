package com.juntong.forgerydetection.controller;

import com.juntong.forgerydetection.common.ApiResponse;
import com.juntong.forgerydetection.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "4. 数据统计模块", description = "首页大屏数据展示")
public class StatisticsController {

    private final NewsService newsService;

    @GetMapping("/dashboard")
    @Operation(summary = "获取首页仪表盘数据", description = "返回新闻总数、谣言数、事实数等统计指标 (Redis缓存)")
    public ApiResponse<Map<String, Object>> getDashboardStats() {
        // 直接调用 Service，缓存逻辑在 Service 内部自动处理
        return ApiResponse.success(newsService.getDashboardStats());
    }
}