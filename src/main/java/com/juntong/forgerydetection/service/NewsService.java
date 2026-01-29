package com.juntong.forgerydetection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juntong.forgerydetection.entity.News;

import java.util.Map;

/**
 * 新闻业务接口
 * 继承 IService，获得批量增删改查能力
 */
public interface NewsService extends IService<News> {
    /**
     * 获取首页仪表盘统计数据 (带缓存)
     * @return 统计结果 Map
     */
    Map<String, Object> getDashboardStats();
}