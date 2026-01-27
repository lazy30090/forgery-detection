package com.juntong.forgerydetection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 伪造检测任务实体类
 * 对应数据库表: biz_detection_task
 */
@Data
@TableName("biz_detection_task")
public class DetectionTask {

    /**
     * 任务ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联的新闻内部ID (对应 news_id)
     */
    @TableField("news_id")
    private Long newsId;

    /**
     * 状态: 0-待检测, 1-检测中, 2-完成, 3-失败
     */
    private Integer status;

    /**
     * 检测结论: 真/假/存疑 (对应 result_label)
     */
    @TableField("result_label")
    private String resultLabel;

    /**
     * 置信度 (对应 confidence)
     */
    private BigDecimal confidence;

    /**
     * AI解释报告 (对应 explanation)
     */
    private String explanation;

    /**
     * 模型版本 (对应 model_version)
     */
    @TableField("model_version")
    private String modelVersion;

    /**
     * 耗时(ms) (对应 cost_time)
     */
    @TableField("cost_time")
    private Long costTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}