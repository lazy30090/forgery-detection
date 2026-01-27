package com.juntong.forgerydetection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juntong.forgerydetection.entity.News;

/**
 * 新闻业务接口
 * 继承 IService，获得批量增删改查能力
 */
public interface NewsService extends IService<News> {
    // 这里暂时留空，通用方法 IService 都包办了
}