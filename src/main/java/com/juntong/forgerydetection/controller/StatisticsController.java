package com.juntong.forgerydetection.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juntong.forgerydetection.common.ApiResponse;
import com.juntong.forgerydetection.entity.News;
import com.juntong.forgerydetection.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "4. 数据统计模块", description = "首页大屏数据展示")
public class StatisticsController {

    private final NewsService newsService;

    @GetMapping("/dashboard")
    @Operation(summary = "获取首页仪表盘数据", description = "返回新闻总数、谣言数、事实数等统计指标")
    public ApiResponse<Map<String, Object>> getDashboardStats() {
        // 1. 统计总数
        long totalCount = newsService.count();

        // 2. 统计谣言数 (Label = "谣言")
        long fakeCount = newsService.count(new LambdaQueryWrapper<News>().eq(News::getLabel, "谣言"));

        // 3. 统计事实数 (Label = "事实")
        long realCount = newsService.count(new LambdaQueryWrapper<News>().eq(News::getLabel, "事实"));

        // 4. 统计今日新增 (这里简单演示，暂用待检测代替，或者你可以根据 create_time 统计)
        long pendingCount = newsService.count(new LambdaQueryWrapper<News>().eq(News::getLabel, "待检测"));

        // 5. 封装结果 Map
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalNews", totalCount);
        stats.put("fakeNews", fakeCount);
        stats.put("realNews", realCount);
        stats.put("pendingNews", pendingCount);
        // 计算一个简单的谣言比例
        String ratio = totalCount == 0 ? "0%" : String.format("%.1f%%", (double) fakeCount / totalCount * 100);
        stats.put("fakeRatio", ratio);

        return ApiResponse.success(stats);
    }
}