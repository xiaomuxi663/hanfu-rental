package com.hanfu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hanfu.dto.*;
import com.hanfu.entity.SysUser;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {
    Map<String, Object> login(LoginDto dto);
    void register(RegisterDto dto);
    SysUser getUserInfo(Long userId);
    void updateUserInfo(Long userId, UserUpdateDto dto);
    void verifyUser(Long userId, VerifyDto dto);
    Page<SysUser> getUserList(Integer pageNum, Integer pageSize, String keyword);
    void updateUserStatus(Long userId, Integer status);
}
