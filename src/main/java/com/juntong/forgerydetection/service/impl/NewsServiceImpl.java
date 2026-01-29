package com.juntong.forgerydetection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juntong.forgerydetection.entity.News;
import com.juntong.forgerydetection.mapper.NewsMapper;
import com.juntong.forgerydetection.service.NewsService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 新闻业务实现类
 * * ServiceImpl<Mapper, Entity> 是 MyBatis-Plus 提供的神器
 * 它自动帮你实现了 Service 接口里定义的所有基础方法
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    /**
     * @Cacheable: 开启缓存
     * value = "dashboardStats": 缓存的名称 (文件夹)
     * key = "'summary'": 缓存的 key (文件名)
     * 效果：第一次运行后，结果会存入 Redis。下次再调用，直接从 Redis 取，不走下面的代码。
     */
    @Override
    @Cacheable(value = "dashboardStats", key = "'summary'")
    public Map<String, Object> getDashboardStats() {
        // 模拟耗时 (演示时可以证明缓存生效，实际上线请删掉)
        // try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // 1. 统计总数
        long totalCount = this.count();

        // 2. 统计谣言数
        long fakeCount = this.count(new LambdaQueryWrapper<News>().eq(News::getLabel, "谣言"));

        // 3. 统计事实数
        long realCount = this.count(new LambdaQueryWrapper<News>().eq(News::getLabel, "事实"));

        // 4. 统计待检测数
        long pendingCount = this.count(new LambdaQueryWrapper<News>().eq(News::getLabel, "待检测"));

        // 5. 封装结果
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalNews", totalCount);
        stats.put("fakeNews", fakeCount);
        stats.put("realNews", realCount);
        stats.put("pendingNews", pendingCount);

        String ratio = totalCount == 0 ? "0%" : String.format("%.1f%%", (double) fakeCount / totalCount * 100);
        stats.put("fakeRatio", ratio);

        return stats;
    }
}