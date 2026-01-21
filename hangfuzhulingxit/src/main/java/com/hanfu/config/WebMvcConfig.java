package com.hanfu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web MVC 配置
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Value("${file.upload-path:./uploads/}")
    private String uploadPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        // 登录注册
                        "/api/user/login",
                        "/api/user/register",
                        // 文件上传
                        "/api/file/**",
                        // 公开接口
                        "/api/notice/**"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取绝对路径并确保以/结尾
        String absolutePath = new File(uploadPath).getAbsolutePath();
        if (!absolutePath.endsWith(File.separator)) {
            absolutePath += File.separator;
        }
        
        // 配置上传文件的访问路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolutePath);
    }
}
