package com.hanfu.config;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hanfu.entity.SysNotice;
import com.hanfu.entity.SysUser;
import com.hanfu.mapper.SysNoticeMapper;
import com.hanfu.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器 - 应用启动时初始化默认数据
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SysUserMapper userMapper;
    private final SysNoticeMapper noticeMapper;

    @Override
    public void run(String... args) {
        initAdminPassword();
        initNoticeData();
    }

    /**
     * 初始化管理员密码（使用Hutool BCrypt重新加密）
     */
    private void initAdminPassword() {
        try {
            // 检查admin用户
            SysUser admin = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUsername, "admin"));
            
            if (admin != null) {
                // 用Hutool重新生成密码hash
                String newPassword = BCrypt.hashpw("123456");
                admin.setPassword(newPassword);
                userMapper.updateById(admin);
                log.info("管理员密码已重置: admin/123456");
            }

            // 检查staff用户
            SysUser staff = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUsername, "staff"));
            
            if (staff != null) {
                String newPassword = BCrypt.hashpw("123456");
                staff.setPassword(newPassword);
                userMapper.updateById(staff);
                log.info("库管员密码已重置: staff/123456");
            }
            
        } catch (Exception e) {
            log.error("初始化密码失败", e);
        }
    }

    /**
     * 初始化公告数据
     */
    private void initNoticeData() {
        try {
            // 检查是否已有公告数据
            Long count = noticeMapper.selectCount(null);
            if (count > 0) {
                log.info("公告数据已存在，跳过初始化");
                return;
            }

            // 添加默认公告
            SysNotice notice1 = new SysNotice();
            notice1.setTitle("欢迎使用汉服租赁系统");
            notice1.setContent("本店提供各种精美汉服租赁服务，包括明制、唐制、宋制、晋制等多种款式，欢迎选购！");
            notice1.setType(1);
            noticeMapper.insert(notice1);

            SysNotice notice2 = new SysNotice();
            notice2.setTitle("租赁须知");
            notice2.setContent("1. 租赁前请仔细阅读租赁协议\n2. 请爱护租赁服装，如有损坏需照价赔偿\n3. 请按时归还，逾期将收取滞纳金");
            notice2.setType(1);
            noticeMapper.insert(notice2);

            log.info("初始化公告数据成功：2条公告（轮播图请在后台手动上传）");
            
        } catch (Exception e) {
            log.error("初始化公告数据失败", e);
        }
    }
}
