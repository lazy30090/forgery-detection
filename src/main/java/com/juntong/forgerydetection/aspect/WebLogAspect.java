package com.juntong.forgerydetection.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

/**
 * 统一请求日志切面
 * 作用：自动打印所有 Controller 接口的入参、出参和耗时
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    /**
     * 定义切入点：扫描 controller 包下的所有方法
     */
    @Pointcut("execution(public * com.juntong.forgerydetection.controller..*.*(..))")
    public void webLog() {}

    /**
     * 在接口执行前：打印请求信息
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return;
        HttpServletRequest request = attributes.getRequest();

        // 打印请求头信息
        log.info("========== 请求开始 ==========");
        log.info("URL          : {}", request.getRequestURL().toString());
        log.info("HTTP Method  : {}", request.getMethod());
        log.info("Class Method : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("IP           : {}", request.getRemoteAddr());
        log.info("Request Args : {}", Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 环绕通知：统计耗时并打印响应结果
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 执行目标方法
        Object result = proceedingJoinPoint.proceed();

        // 计算耗时
        long timeCost = System.currentTimeMillis() - startTime;

        // 打印响应结果和耗时
        log.info("Response     : {}", result);
        log.info("Time Cost    : {} ms", timeCost);
        log.info("========== 请求结束 ==========");
        return result; // 必须返回结果，否则前端收不到数据
    }
}