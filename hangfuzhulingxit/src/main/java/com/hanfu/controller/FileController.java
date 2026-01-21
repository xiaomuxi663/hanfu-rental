package com.hanfu.controller;

import com.hanfu.common.BusinessException;
import com.hanfu.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${file.upload-path:./uploads/}")
    private String uploadPath;

    /**
     * 上传图片
     */
    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("请选择要上传的文件");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException("只能上传图片文件");
        }

        // 检查文件大小（10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BusinessException("文件大小不能超过10MB");
        }

        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename != null ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFilename = UUID.randomUUID().toString().replace("-", "") + ext;

            // 创建上传目录（使用绝对路径）
            File uploadDir = new File(uploadPath).getAbsoluteFile();
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 保存文件
            File destFile = new File(uploadDir, newFilename);
            file.transferTo(destFile);
            
            log.info("文件上传成功: {} -> {}", newFilename, destFile.getAbsolutePath());

            // 返回访问路径
            String fileUrl = "/uploads/" + newFilename;
            return Result.success("上传成功", fileUrl);
            
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败");
        }
    }
}
