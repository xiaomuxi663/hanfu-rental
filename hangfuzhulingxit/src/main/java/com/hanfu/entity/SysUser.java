package com.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String role;
    private String avatar;
    private String phone;
    private String realName;
    private String idCard;
    private String defaultReceiverName;
    private String defaultReceiverPhone;
    private String defaultAddress;
    private Integer creditScore;
    private BigDecimal balance;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
