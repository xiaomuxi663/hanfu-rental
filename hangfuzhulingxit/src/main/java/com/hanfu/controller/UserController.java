package com.hanfu.controller;

import com.hanfu.common.Result;
import com.hanfu.dto.*;
import com.hanfu.entity.SysUser;
import com.hanfu.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final SysUserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDto dto) {
        return Result.success("登录成功", userService.login(dto));
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDto dto) {
        userService.register(dto);
        return Result.success("注册成功", null);
    }

    @GetMapping("/info")
    public Result<SysUser> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userService.getUserInfo(userId));
    }

    @PutMapping("/update")
    public Result<Void> updateUserInfo(HttpServletRequest request, @RequestBody UserUpdateDto dto) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateUserInfo(userId, dto);
        return Result.success("更新成功", null);
    }

    @PostMapping("/verify")
    public Result<Void> verifyUser(HttpServletRequest request, @Valid @RequestBody VerifyDto dto) {
        Long userId = (Long) request.getAttribute("userId");
        userService.verifyUser(userId, dto);
        return Result.success("实名认证成功", null);
    }
}
