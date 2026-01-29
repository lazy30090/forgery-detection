package com.juntong.forgerydetection.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juntong.forgerydetection.common.ApiResponse;
import com.juntong.forgerydetection.entity.User;
import com.juntong.forgerydetection.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "0. 用户认证模块", description = "登录、注销与状态查询")
public class AuthController {

    private final UserService userService;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "传入账号密码，返回 Token 信息")
    public ApiResponse<Map<String, Object>> login (@RequestBody Map<String, String> params) {
        // 从 Map 中获取参数
        String username = params.get("username");
        String password = params.get("password");

        // 1. 查数据库
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));

        // 2. 校验用户是否存在
        if (user == null) {
            return ApiResponse.failed("账号不存在");
        }

        // 3. 校验密码 (这里为了比赛方便用明文)
        if (!user.getPassword().equals(password)) {
            return ApiResponse.failed("密码错误");
        }

        // 4. Sa-Token 登录
        // 这一句代码会自动生成 Token，并标记当前会话为已登录
        StpUtil.login(user.getId());

        // 5. 获取 Token 信息返回给前端
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("tokenName", tokenInfo.tokenName); // Token 的名字 (satoken)
        result.put("tokenValue", tokenInfo.tokenValue); // Token 的值 (eyJ...)
        result.put("role", user.getRole()); // 返回角色，方便前端跳转不同页面
        result.put("nickname", user.getNickname());

        return ApiResponse.success(result, "登录成功");
    }

    /**
     * 注销接口
     */
    @PostMapping("/logout")
    @Operation(summary = "用户注销", description = "退出登录，销毁 Token")
    public ApiResponse<String> logout() {
        StpUtil.logout();
        return ApiResponse.success("注销成功");
    }

    /**
     * 查询登录状态 (测试用)
     */
    @GetMapping("/isLogin")
    @Operation(summary = "查询当前登录状态", description = "无需参数，自动从 Header 获取 Token 判断")
    public ApiResponse<Boolean> isLogin() {
        return ApiResponse.success(StpUtil.isLogin());
    }
}