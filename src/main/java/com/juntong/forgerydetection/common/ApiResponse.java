package com.juntong.forgerydetection.common;

import lombok.Data;

/**
 * 通用API响应封装类
 * @param <T> 数据泛型
 */
@Data
public class ApiResponse<T> {

    /**
     * 状态码
     */
    private long code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 数据封装
     */
    private T data;

    protected ApiResponse() {
    }

    protected ApiResponse(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     * @param data 获取的数据
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果（自定义提示信息）
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码枚举
     */
    public static <T> ApiResponse<T> failed(ResultCode errorCode) {
        return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果（自定义提示信息）
     */
    public static <T> ApiResponse<T> failed(String message) {
        return new ApiResponse<>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果（自定义错误码和提示信息）
     * 用于全局异常处理，比如校验失败时返回 404 + "标题不能为空"
     */
    public static <T> ApiResponse<T> failed(long code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}