package com.juntong.forgerydetection.entity;

import jakarta.validation.constraints.NotBlank;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 新闻基础信息实体类
 * 对应数据库表: sys_news
 */
@Data
@TableName("sys_news")
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 原始数据集ID (对应 CSV: news_id)
     */
    @TableField("news_id")
    private String newsId;

    /**
     * 新闻标题 (对应 CSV: title)
     */
    @NotBlank(message = "新闻标题不能为空")
    private String title;

    /**
     * 新闻正文 (对应 CSV: content)
     */
    @NotBlank(message = "新闻正文不能为空")
    private String content;

    /**
     * 发布时间 (对应 CSV: publish_time)
     */
    @TableField("publish_time")
    private LocalDateTime publishTime;

    /**
     * 来源平台 (对应 CSV: platform)
     */
    private String platform;

    /**
     * 作者/发布者 (CSV中无此字段，保留作为扩展)
     */
    private String author;

    /**
     * 原文链接 (对应 CSV: url)
     */
    private String url;

    /**
     * 图片链接 (对应 CSV: pic_url)
     */
    @TableField("pic_url")
    private String picUrl;

    /**
     * [新增] 核查时间 (对应 CSV: check_time)
     * 说明：这是判断新闻何时被确认为谣言的关键时间
     */
    @TableField("check_time")
    private LocalDateTime checkTime;

    /**
     * [新增] 真伪标签 (对应 CSV: label)
     * 说明：核心字段，存储 "谣言" 或 "事实"
     */
    private String label;

    /**
     * [新增] 话题标签 (对应 CSV: hashtag)
     */
    private String hashtag;

    /**
     * 数据来源: 1-公开数据集, 2-用户上传/爬取
     */
    @TableField("data_source")
    private Integer dataSource;

    /**
     * 入库时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}