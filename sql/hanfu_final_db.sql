/*
 Navicat MySQL Data Transfer
 Target Server Type    : MySQL
 Target Server Version : 80000
 File Encoding         : 65001

 Date: 2026-01-21
 Desc: 汉服租赁系统最终定稿数据库 - 11张表
*/

-- 1. 创建数据库 (如果不存在)
CREATE DATABASE IF NOT EXISTS `hanfu_final_db` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';
USE `hanfu_final_db`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =================================================================
-- 1. 用户表 (sys_user)
-- 设计亮点：宽表设计，集成角色、实名信息和默认地址，减少连表查询
-- =================================================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `role` varchar(20) NOT NULL DEFAULT 'renter' COMMENT '角色: admin(管理员)/staff(库管员)/renter(租客)',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  
  -- 实名认证
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  
  -- 默认地址 (宽表冗余)
  `default_receiver_name` varchar(50) DEFAULT NULL COMMENT '默认收货人',
  `default_receiver_phone` varchar(11) DEFAULT NULL COMMENT '默认收货电话',
  `default_address` varchar(255) DEFAULT NULL COMMENT '默认收货地址',
  
  -- 资产
  `credit_score` int(11) DEFAULT 100 COMMENT '信用分',
  `balance` decimal(10,2) DEFAULT 0.00 COMMENT '钱包余额',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态: 1正常 0禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 初始化数据：管理员(admin/123456) 和 库管员(staff/123456)
-- 密码使用BCrypt加密，明文均为：123456
INSERT INTO `sys_user` (`username`, `password`, `role`, `nickname`, `credit_score`, `balance`, `status`) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKHvsXdp5XKkn6cEWKzCz9qx5M6e', 'admin', '超级管理员', 100, 0.00, 1),
('staff', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKHvsXdp5XKkn6cEWKzCz9qx5M6e', 'staff', '仓库操作员', 100, 0.00, 1);

-- =================================================================
-- 2. 汉服分类表 (hanfu_category)
-- =================================================================
DROP TABLE IF EXISTS `hanfu_category`;
CREATE TABLE `hanfu_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) NOT NULL COMMENT '形制名称(如:明制,唐制)',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='汉服分类表';

INSERT INTO `hanfu_category` (`category_name`) VALUES ('明制'), ('唐制'), ('宋制'), ('晋制');

-- =================================================================
-- 3. 汉服款式表 (hanfu_spu)
-- 作用：展示商品信息
-- =================================================================
DROP TABLE IF EXISTS `hanfu_spu`;
CREATE TABLE `hanfu_spu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  `name` varchar(200) NOT NULL COMMENT '汉服名称',
  `main_image` varchar(255) DEFAULT NULL COMMENT '主图URL',
  `daily_rent` decimal(10,2) NOT NULL COMMENT '日租金',
  `deposit` decimal(10,2) NOT NULL COMMENT '押金',
  `detail_content` text COMMENT '图文详情',
  `is_publish` tinyint(1) DEFAULT 1 COMMENT '1上架 0下架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='汉服款式表(SPU)';

-- =================================================================
-- 4. 汉服库存表 (hanfu_sku)
-- 设计亮点：一物一码，管理具体每一件衣服的状态
-- =================================================================
DROP TABLE IF EXISTS `hanfu_sku`;
CREATE TABLE `hanfu_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `spu_id` bigint(20) NOT NULL COMMENT '关联款式ID',
  `sku_code` varchar(50) NOT NULL COMMENT '唯一编码(条形码)',
  `size` varchar(10) NOT NULL COMMENT '尺码(S/M/L)',
  `status` tinyint(4) DEFAULT 0 COMMENT '0在库, 1已租, 2清洗中, 3维修中',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`sku_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='汉服实物库存表(SKU)';

-- =================================================================
-- 5. 租赁订单表 (rental_order)
-- 设计亮点：地址快照 + 物流闭环
-- =================================================================
DROP TABLE IF EXISTS `rental_order`;
CREATE TABLE `rental_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_sn` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` bigint(20) NOT NULL COMMENT '租客ID',
  `sku_id` bigint(20) NOT NULL COMMENT '实物ID',
  
  -- 核心日期
  `start_date` date NOT NULL COMMENT '起租日期',
  `end_date` date NOT NULL COMMENT '应还日期',
  `real_return_date` datetime DEFAULT NULL COMMENT '实际归还时间',
  
  -- 金额
  `total_rent` decimal(10,2) NOT NULL COMMENT '总租金',
  `total_deposit` decimal(10,2) NOT NULL COMMENT '实付押金',
  
  -- 状态: 1待支付 2待发货 3租赁中 4待归还 5已完成 6已取消
  `status` tinyint(4) DEFAULT 1 COMMENT '订单状态',
  
  -- 地址快照 (下单时固化)
  `snapshot_name` varchar(50) NOT NULL COMMENT '收货人快照',
  `snapshot_phone` varchar(11) NOT NULL COMMENT '电话快照',
  `snapshot_address` varchar(255) NOT NULL COMMENT '地址快照',
  
  -- 物流
  `delivery_express_no` varchar(100) DEFAULT NULL COMMENT '发货快递单号',
  `return_express_no` varchar(100) DEFAULT NULL COMMENT '归还快递单号',
  
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sn` (`order_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租赁订单表';

-- =================================================================
-- 6. 归还验收表 (order_inspection)
-- 作用：处理定损、逾期扣费
-- =================================================================
DROP TABLE IF EXISTS `order_inspection`;
CREATE TABLE `order_inspection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL COMMENT '关联订单',
  `staff_id` bigint(20) NOT NULL COMMENT '操作员ID',
  `is_damaged` tinyint(1) DEFAULT 0 COMMENT '0完好 1破损',
  `damage_desc` varchar(255) DEFAULT NULL COMMENT '破损描述',
  `penalty_amount` decimal(10,2) DEFAULT 0.00 COMMENT '扣除金额(逾期+赔偿)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='归还验收表';

-- =================================================================
-- 7. 订单评价表 (order_comment)
-- =================================================================
DROP TABLE IF EXISTS `order_comment`;
CREATE TABLE `order_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `rating` tinyint(1) DEFAULT 5 COMMENT '评分1-5',
  `content` varchar(500) DEFAULT NULL COMMENT '评价内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单评价表';

-- =================================================================
-- 8. 信用日志表 (sys_credit_log)
-- =================================================================
DROP TABLE IF EXISTS `sys_credit_log`;
CREATE TABLE `sys_credit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `change_value` int(11) NOT NULL COMMENT '变动值(如 -10, +5)',
  `reason` varchar(100) DEFAULT NULL COMMENT '原因(如: 逾期归还)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='信用变更日志表';

-- =================================================================
-- 9. 钱包流水表 (sys_wallet_log)
-- =================================================================
DROP TABLE IF EXISTS `sys_wallet_log`;
CREATE TABLE `sys_wallet_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `type` tinyint(4) NOT NULL COMMENT '1租金支出 2押金冻结 3押金解冻 4赔偿扣款 5充值',
  `amount` decimal(10,2) NOT NULL COMMENT '变动金额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包流水表';

-- =================================================================
-- 10. 系统公告表 (sys_notice)
-- =================================================================
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `type` tinyint(1) DEFAULT 1 COMMENT '1公告 2轮播图',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

-- =================================================================
-- 11. 系统配置表 (sys_config)
-- 作用：存储全局参数，如商家地址、清洗周期天数
-- =================================================================
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) NOT NULL COMMENT '参数键',
  `param_value` varchar(500) NOT NULL COMMENT '参数值',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_key` (`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统参数配置表';

-- 初始化必要配置
INSERT INTO `sys_config` (`param_key`, `param_value`, `remark`) VALUES 
('warehouse_addr', '浙江省杭州市西湖区汉服文化园3号仓', '商家收货地址'),
('warehouse_phone', '13800138000', '商家收货电话'),
('cleaning_days', '2', '默认清洗缓冲期(天)');

SET FOREIGN_KEY_CHECKS = 1;
