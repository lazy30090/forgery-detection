package com.juntong.forgerydetection.enums;

import lombok.Getter;

/**
 * 检测任务状态枚举
 */
@Getter
public enum TaskStatusEnum {

    PENDING(0, "待检测"),
    PROCESSING(1, "检测中"),
    COMPLETED(2, "已完成"),
    FAILED(3, "检测失败");

    private final int code;
    private final String desc;

    TaskStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}