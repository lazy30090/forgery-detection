package com.juntong.forgerydetection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juntong.forgerydetection.entity.News;
import com.juntong.forgerydetection.mapper.NewsMapper;
import com.juntong.forgerydetection.service.NewsService;
import org.springframework.stereotype.Service;

/**
 * 新闻业务实现类
 * * ServiceImpl<Mapper, Entity> 是 MyBatis-Plus 提供的神器
 * 它自动帮你实现了 Service 接口里定义的所有基础方法
 */
@Service // 必须加这个注解，Spring 才能扫描到它
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
    // 这里暂时不需要写任何代码，父类都帮我们做好了
}