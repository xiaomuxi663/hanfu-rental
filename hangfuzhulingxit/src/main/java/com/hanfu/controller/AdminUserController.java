package com.hanfu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanfu.common.BusinessException;
import com.hanfu.common.Result;
import com.hanfu.entity.SysUser;
import com.hanfu.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    private final SysUserService userService;

    @GetMapping("/list")
    public Result<Page<SysUser>> getUserList(HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        checkAdminRole(request);
        return Result.success(userService.getUserList(pageNum, pageSize, keyword));
    }

    @GetMapping("/{id}")
    public Result<SysUser> getUserDetail(HttpServletRequest request, @PathVariable Long id) {
        checkAdminRole(request);
        return Result.success(userService.getUserInfo(id));
    }

    @PutMapping("/status")
    public Result<Void> updateUserStatus(HttpServletRequest request, @RequestParam Long userId, @RequestParam Integer status) {
        checkAdminRole(request);
        userService.updateUserStatus(userId, status);
        return Result.success("状态修改成功", null);
    }

    private void checkAdminRole(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) throw new BusinessException(403, "无权限访问");
    }
}
