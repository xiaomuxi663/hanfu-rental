package com.hanfu.controller;

import com.hanfu.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("汉服租赁系统运行正常");
    }
}
