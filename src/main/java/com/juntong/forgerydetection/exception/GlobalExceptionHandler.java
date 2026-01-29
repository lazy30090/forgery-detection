package com.juntong.forgerydetection.exception;

import com.juntong.forgerydetection.common.ApiResponse;
import com.juntong.forgerydetection.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器
 * 作用：拦截 Controller 层抛出的各种异常，统一封装成 ApiResponse 格式返回给前端
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 拦截参数校验异常
     * 场景：比如 @NotBlank 校验失败，抛出 MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<String> handleBindException(MethodArgumentNotValidException e) {
        // 从异常中提取出具体的错误信息（比如 "新闻标题不能为空"）
        BindingResult bindingResult = e.getBindingResult();
        String message = "参数校验失败";

        if (bindingResult.hasErrors()) {
            // 获取第一个具体的错误提示
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        }

        log.warn("参数校验异常: {}", message);

        return ApiResponse.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    /**
     * 2. 拦截所有其他未知的系统异常
     * 场景：空指针、数据库连接失败、代码逻辑错误等
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleException(Exception e) {
        // 打印堆栈日志，方便后端排查
        log.error("系统内部异常: ", e);

        // 告诉前端是系统故障，隐藏具体的代码报错细节
        return ApiResponse.failed(ResultCode.FAILED.getCode(), "系统繁忙，请稍后再试");
    }

    /**
     * 3. 拦截未登录异常 (Sa-Token)
     * 场景：未带 Token 或 Token 失效时，返回 401
     */
    @ExceptionHandler(cn.dev33.satoken.exception.NotLoginException.class)
    public ApiResponse<String> handleNotLoginException(cn.dev33.satoken.exception.NotLoginException nle) {
        log.warn("鉴权失败: {}", nle.getMessage());
        // 返回 401 状态码 (我们在 ResultCode 里定义过的)
        return ApiResponse.failed(ResultCode.UNAUTHORIZED.getCode(), "您尚未登录或 Token 已失效");
    }

    /**
     * 4. 拦截静态资源未找到异常 (Spring Boot 3.x)
     * 场景：请求了不存在的 /favicon.ico 或其他 js/css
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<String> handleNoResourceFoundException(NoResourceFoundException e) {
        // 不需要打印堆栈，简单的 WARN 记录一下路径即可
        log.warn("请求的静态资源不存在: {}", e.getResourcePath());
        // 返回 404 状态码
        return ApiResponse.failed(404, "资源不存在");
    }
}