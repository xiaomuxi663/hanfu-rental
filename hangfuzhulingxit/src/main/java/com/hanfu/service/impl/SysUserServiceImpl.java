package com.hanfu.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanfu.common.BusinessException;
import com.hanfu.dto.*;
import com.hanfu.entity.SysUser;
import com.hanfu.mapper.SysUserMapper;
import com.hanfu.service.SysUserService;
import com.hanfu.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private final JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(LoginDto dto) {
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername()));
        if (user == null) throw new BusinessException("账号或密码错误");
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) throw new BusinessException("账号或密码错误");
        if (user.getStatus() == 0) throw new BusinessException("账号已被禁用");
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        user.setPassword(null);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    @Override
    public void register(RegisterDto dto) {
        if (this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername())) > 0)
            throw new BusinessException("该账号已被注册");
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setRole("renter");
        user.setCreditScore(100);
        user.setBalance(BigDecimal.ZERO);
        user.setStatus(1);
        this.save(user);
    }

    @Override
    public SysUser getUserInfo(Long userId) {
        SysUser user = this.getById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        user.setPassword(null);
        return user;
    }

    @Override
    public void updateUserInfo(Long userId, UserUpdateDto dto) {
        SysUser user = this.getById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        if (StringUtils.hasText(dto.getNickname())) user.setNickname(dto.getNickname());
        if (StringUtils.hasText(dto.getAvatar())) user.setAvatar(dto.getAvatar());
        if (StringUtils.hasText(dto.getPhone())) user.setPhone(dto.getPhone());
        if (StringUtils.hasText(dto.getDefaultReceiverName())) user.setDefaultReceiverName(dto.getDefaultReceiverName());
        if (StringUtils.hasText(dto.getDefaultReceiverPhone())) user.setDefaultReceiverPhone(dto.getDefaultReceiverPhone());
        if (StringUtils.hasText(dto.getDefaultAddress())) user.setDefaultAddress(dto.getDefaultAddress());
        this.updateById(user);
    }

    @Override
    public void verifyUser(Long userId, VerifyDto dto) {
        SysUser user = this.getById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        if (StringUtils.hasText(user.getIdCard())) throw new BusinessException("您已完成实名认证");
        if (this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getIdCard, dto.getIdCard())) > 0)
            throw new BusinessException("该身份证号已被使用");
        user.setRealName(dto.getRealName());
        user.setIdCard(dto.getIdCard());
        this.updateById(user);
    }

    @Override
    public Page<SysUser> getUserList(Integer pageNum, Integer pageSize, String keyword) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysUser::getUsername, keyword).or().like(SysUser::getNickname, keyword).or().like(SysUser::getPhone, keyword);
        }
        wrapper.orderByDesc(SysUser::getCreateTime);
        Page<SysUser> result = this.page(page, wrapper);
        result.getRecords().forEach(u -> u.setPassword(null));
        return result;
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        SysUser user = this.getById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        if ("admin".equals(user.getRole())) throw new BusinessException("不能修改管理员状态");
        user.setStatus(status);
        this.updateById(user);
    }
}
