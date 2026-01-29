package com.juntong.forgerydetection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.juntong.forgerydetection.entity.News;
import org.apache.ibatis.annotations.Mapper;

/**
 * 新闻数据仓库接口
 * 继承 BaseMapper 后，自动拥有 CRUD 能力
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {
    // MyBatis-Plus 已经写好了增删改查
}