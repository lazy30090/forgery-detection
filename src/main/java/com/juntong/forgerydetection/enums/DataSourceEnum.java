package com.juntong.forgerydetection.enums;

import lombok.Getter;

/**
 * 数据来源枚举
 */
@Getter
public enum DataSourceEnum {

    DATASET(1, "公开数据集"),
    USER_UPLOAD(2, "用户手动上传");

    private final int code;
    private final String desc;

    DataSourceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}