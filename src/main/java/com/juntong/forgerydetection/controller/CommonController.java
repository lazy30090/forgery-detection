package com.juntong.forgerydetection.controller;

import com.juntong.forgerydetection.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/common")
@Tag(name = "3. 通用工具模块", description = "文件上传等基础功能")
public class CommonController {

    @Value("${file.upload.path}")
    private String uploadPath;

    @PostMapping("/upload")
    @Operation(summary = "图片上传接口", description = "上传图片文件，返回可访问的 URL 地址")
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.failed("上传文件不能为空");
        }

        // 1. 获取原始文件名 (例如: test.jpg)
        String originalFilename = file.getOriginalFilename();
        // 2. 获取后缀名 (例如: .jpg)
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 3. 生成唯一文件名 (防止文件名冲突覆盖)
        String newFileName = UUID.randomUUID().toString() + suffix;

        // 4. 创建文件对象
        File dest = new File(uploadPath + newFileName);
        // 如果目录不存在，自动创建
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            // 5. 保存文件到硬盘
            file.transferTo(dest);

            // 6. 拼接返回 URL
            // 假设后端运行在 localhost:8080，映射路径是 /files/
            // 注意：正式环境这里应该拼完整的域名，这里简化处理返回相对路径
            String fileUrl = "http://localhost:8080/files/" + newFileName;

            return ApiResponse.success(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.failed("文件上传失败: " + e.getMessage());
        }
    }
}