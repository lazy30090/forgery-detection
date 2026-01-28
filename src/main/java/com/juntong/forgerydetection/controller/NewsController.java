package com.juntong.forgerydetection.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juntong.forgerydetection.common.ApiResponse;
import com.juntong.forgerydetection.entity.News;
import com.juntong.forgerydetection.enums.DataSourceEnum;
import com.juntong.forgerydetection.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 新闻管理接口
 */
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Tag(name = "1. 新闻管理模块", description = "新闻的增删改查与检索")
public class NewsController {

    private final NewsService newsService;

    /**
     * 1. 综合分页查询接口
     * 场景：首页列表、搜索结果页、按分类查看
     * URL: GET /api/news/list?page=1&size=10&title=地震&label=谣言
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询新闻列表", description = "支持按标题模糊搜索，按标签筛选，结果按创建时间倒序排列")
    public ApiResponse<IPage<News>> list(
            @Parameter(description = "页码 (默认1)") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页条数 (默认10)") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "搜索关键词 (匹配标题)") @RequestParam(required = false) String title,
            @Parameter(description = "筛选标签 (如: 谣言/事实)") @RequestParam(required = false) String label) {

        // 1. 构造分页对象
        Page<News> pageParam = new Page<>(page, size);

        // 2. 构造查询条件
        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<>();
        // 如果传了标题，就模糊查询
        wrapper.like(StringUtils.hasText(title), News::getTitle, title);
        // 如果传了标签，就精确查询
        wrapper.eq(StringUtils.hasText(label), News::getLabel, label);
        // 按创建时间倒序（最新的在前）
        wrapper.orderByDesc(News::getCreateTime);

        // 3. 执行查询
        IPage<News> result = newsService.page(pageParam, wrapper);
        return ApiResponse.success(result);
    }

    /**
     * 2. 获取新闻详情接口
     * 场景：点击列表进入详情页
     * URL: GET /api/news/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取新闻详情", description = "根据主键 ID 查询单条新闻详情")
    public ApiResponse<News> getDetail(@Parameter(description = "新闻主键ID") @PathVariable Long id) {
        News news = newsService.getById(id);
        return ApiResponse.success(news);
    }

    /**
     * 3. 新增新闻接口
     * 场景：用户手动输入一段文本进行检测
     * URL: POST /api/news/add
     */
    @PostMapping("/add")
    @Operation(summary = "新增/上传新闻", description = "用于用户手动上传待检测的新闻。系统会自动将标签设为'待检测'，来源设为'用户上传'。")
    public ApiResponse<Long> add(@RequestBody @Valid News news) {
        // 1. 强制设置来源
        news.setDataSource(DataSourceEnum.USER_UPLOAD.getCode());

        // 2. 强制覆盖标签 (去掉 if 判断)
        // 无论前端传什么，只要是新上传的，一律视为“待检测”
        news.setLabel("待检测");

        newsService.save(news);
        return ApiResponse.success(news.getId());
    }
}