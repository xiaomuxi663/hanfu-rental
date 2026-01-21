package com.hanfu.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String nickname;
    private String avatar;
    private String phone;
    private String defaultReceiverName;
    private String defaultReceiverPhone;
    private String defaultAddress;
}
