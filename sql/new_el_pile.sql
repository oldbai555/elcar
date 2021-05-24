/*
Navicat MySQL Data Transfer

Source Server         : MyOld
Source Server Version : 50733
Source Host           : 47.106.234.164:3306
Source Database       : el_pile

Target Server Type    : MYSQL
Target Server Version : 50733
File Encoding         : 65001

Date: 2021-05-11 18:32:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for el_app_message
-- ----------------------------
DROP TABLE IF EXISTS `el_app_message`;
CREATE TABLE `el_app_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `version` int(255) NOT NULL DEFAULT '1' COMMENT '版本号，乐观锁',
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `isdelete` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0为不删除，1为删除）',
  `first_page` varchar(255) NOT NULL COMMENT '第一页的固定信息展示',
  `wash_page` varchar(255) NOT NULL COMMENT '其他页的固定性信息展示',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of el_app_message
-- ----------------------------

-- ----------------------------
-- Table structure for el_car
-- ----------------------------
DROP TABLE IF EXISTS `el_car`;
CREATE TABLE `el_car` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `version` int(255) NOT NULL DEFAULT '1' COMMENT '版本号，乐观锁',
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `isdelete` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0为不删除，1为删除）',
  `car_name` varchar(255) NOT NULL COMMENT '车辆的名称',
  `car_type_id` int(255) NOT NULL COMMENT '关联车辆类型表',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID，关联这是谁的车',
  PRIMARY KEY (`id`),
  UNIQUE KEY `car_name` (`car_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of el_car
-- ----------------------------
INSERT INTO `el_car` VALUES ('1', '1', '2021-04-26 17:35:12', '2021-04-26 17:35:12', '0', 'A-001', '1', '1');
INSERT INTO `el_car` VALUES ('2', '1', '2021-04-26 17:35:42', '2021-04-26 17:35:42', '0', 'A-002', '1', '1');
INSERT INTO `el_car` VALUES ('3', '5', '2021-04-26 17:41:05', '2021-04-26 17:48:12', '0', 'A-003', '2', '1');
INSERT INTO `el_car` VALUES ('4', '5', '2021-04-29 16:53:10', '2021-04-29 17:05:47', '0', 'A-004', '1', '1');
INSERT INTO `el_car` VALUES ('5', '1', '2021-05-10 10:09:00', '2021-05-10 10:09:00', '0', 'A-015', '1', '1');

-- ----------------------------
-- Table structure for el_car_type
-- ----------------------------
DROP TABLE IF EXISTS `el_car_type`;
CREATE TABLE `el_car_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `version` int(255) NOT NULL DEFAULT '1' COMMENT '版本号，乐观锁',
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `isdelete` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0为不删除，1为删除）',
  `type_name` varchar(255) NOT NULL COMMENT '车辆类型名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of el_car_type
-- ----------------------------
INSERT INTO `el_car_type` VALUES ('1', '1', '2021-04-26 10:30:03', '2021-04-26 10:30:03', '0', '电瓶车');
INSERT INTO `el_car_type` VALUES ('2', '1', '2021-04-26 10:30:15', '2021-04-26 10:30:15', '0', '电动车');
INSERT INTO `el_car_type` VALUES ('3', '3', '2021-04-26 11:05:24', '2021-04-26 11:46:02', '0', '面包车');

-- ----------------------------
-- Table structure for el_equipment
-- ----------------------------
DROP TABLE IF EXISTS `el_equipment`;
CREATE TABLE `el_equipment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `isdelete` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0为不删除，1为删除）',
  `version` int(255) NOT NULL DEFAULT '1' COMMENT '版本号，乐观锁',
  `place` varchar(255) NOT NULL COMMENT '设备地点',
  `eq_name` varchar(255) NOT NULL COMMENT '设备名称',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '设备状态（0可用，1已用，2禁用）',
  `car_type_id` int(11) NOT NULL COMMENT '关联车辆类型表',
  `pay_id` int(11) NOT NULL COMMENT '关联收费表，表示要收多少钱。',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of el_equipment
-- ----------------------------

-- ----------------------------
-- Table structure for el_order_table
-- ----------------------------
DROP TABLE IF EXISTS `el_order_table`;
CREATE TABLE `el_order_table` (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `version` int(255) NOT NULL DEFAULT '1' COMMENT '版本号，乐观锁',
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `isdelete` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0为不删除，1为删除）',
  `car_id` int(11) NOT NULL COMMENT '车辆Id',
  `eq_id` int(11) NOT NULL COMMENT '设备Id',
  `pay` varchar(255) NOT NULL COMMENT '合计支付金额',
  `state` int(5) NOT NULL COMMENT '订单状态（0未开始，正在进行，2结束，3未确认开始超时结束）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of el_order_table
-- ----------------------------

-- ----------------------------
-- Table structure for el_pay_table
-- ----------------------------
DROP TABLE IF EXISTS `el_pay_table`;
CREATE TABLE `el_pay_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `version` int(255) NOT NULL DEFAULT '1' COMMENT '版本号，乐观锁',
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `isdelete` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0为不删除，1为删除）',
  `car_type_id` int(11) NOT NULL COMMENT '关联车辆类型表，根据什么类型收什么样的费',
  `pay_time_name` varchar(255) NOT NULL COMMENT '时段名称',
  `charge_start` int(8) NOT NULL DEFAULT '0' COMMENT '收费开始时间',
  `charge_end` int(8) NOT NULL DEFAULT '0' COMMENT '收费结束时间',
  `pay_money` double(16,0) NOT NULL COMMENT '需要的金额（服务费）',
  `pay_fee` double(16,0) NOT NULL DEFAULT '0' COMMENT '电费(多少钱一度电)',
  `pay_count` double NOT NULL DEFAULT '0' COMMENT '服务费+电费的合计',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of el_pay_table
-- ----------------------------
INSERT INTO `el_pay_table` VALUES ('1', '1', '2021-05-11 16:52:14', '2021-05-11 16:52:16', '0', '1', '全日段', '0', '0', '1', '0', '0');
INSERT INTO `el_pay_table` VALUES ('2', '1', '2021-05-11 16:52:43', '2021-05-11 16:52:46', '0', '2', '全日段', '0', '0', '1', '0', '0');

-- ----------------------------
-- Table structure for el_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `el_refresh_token`;
CREATE TABLE `el_refresh_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `version` int(255) NOT NULL DEFAULT '1' COMMENT '版本号，乐观锁',
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `user_id` int(11) NOT NULL COMMENT '是哪个用户的token信息',
  `refresh_token` varchar(255) NOT NULL COMMENT '具体的token信息',
  `token_key` varchar(255) NOT NULL COMMENT 'token_key可以找到token',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of el_refresh_token
-- ----------------------------
INSERT INTO `el_refresh_token` VALUES ('36', '1', '2021-05-11 16:36:49', '2021-05-11 16:36:49', '1', 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjpudWxsLCJwaG9uZSI6IjE0Nzk2MTgzNzk5IiwiaWQiOjEsImV4cCI6MTYyMDgwODYwOH0.LBy5kv0s9t7ba8xK4DI0WiDeBsv2WINhsCo3AzHLnkI', '$2a$10$byWRFsxgrS0RxbeJQFKVfOo.2GChpx9oIcYgecsLTadlji4MUvhYS');

-- ----------------------------
-- Table structure for el_rose
-- ----------------------------
DROP TABLE IF EXISTS `el_rose`;
CREATE TABLE `el_rose` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `version` int(255) NOT NULL DEFAULT '1' COMMENT '版本号，乐观锁',
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `isdelete` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0为不删除，1为删除）',
  `name` varchar(32) NOT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of el_rose
-- ----------------------------
INSERT INTO `el_rose` VALUES ('1', '1', '2021-04-29 17:41:35', '2021-04-29 17:41:35', '0', '超级管理员');
INSERT INTO `el_rose` VALUES ('2', '1', '2021-04-29 17:41:46', '2021-04-29 17:41:46', '0', '学生');
INSERT INTO `el_rose` VALUES ('3', '1', '2021-04-29 17:41:53', '2021-04-29 17:41:53', '0', '教师');
INSERT INTO `el_rose` VALUES ('4', '1', '2021-04-29 17:42:02', '2021-04-29 17:42:02', '0', '普通社会人士');
INSERT INTO `el_rose` VALUES ('5', '3', '2021-04-29 17:43:29', '2021-04-29 17:43:59', '0', '测试角色');

-- ----------------------------
-- Table structure for el_user
-- ----------------------------
DROP TABLE IF EXISTS `el_user`;
CREATE TABLE `el_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `version` int(255) NOT NULL DEFAULT '1' COMMENT '版本号，乐观锁',
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `isdelete` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0为不删除，1为删除）',
  `phone` varchar(255) NOT NULL COMMENT '手机号',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `user_id_card` varchar(255) DEFAULT NULL COMMENT '教职工、学生、普通人的身份卡。比如身份证，学生证，教师证。',
  `role_id` int(11) NOT NULL COMMENT '角色ID，关联角色表，逻辑关联。',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of el_user
-- ----------------------------
INSERT INTO `el_user` VALUES ('1', '8', '2021-04-29 19:38:25', '2021-05-10 13:31:35', '0', '14796183799', 'e10adc3949ba59abbe56e057f20f883e', '123456789', '2');
INSERT INTO `el_user` VALUES ('2', '1', '2021-05-08 20:01:51', '2021-05-08 20:01:51', '0', '14796183798', '$2a$10$J7mYdH7hu5HoR4p3Jh4qS.L9NoyB2biItGgJnoIBVlx4u4ymI0RFe', '1234567890', '4');
INSERT INTO `el_user` VALUES ('3', '1', '2021-05-08 21:14:02', '2021-05-08 21:14:02', '0', '14796183791', '$2a$10$E0DOcsSMrsgx5klsopXZlOj8qyHuIeOs7/r6s8CgQyafXcI00bXYS', '1234567891', '4');
INSERT INTO `el_user` VALUES ('4', '1', '2021-05-08 21:15:07', '2021-05-08 21:15:07', '0', '14796183790', '$2a$10$aqG2YtUB9ibY0pIYYDvCjOfiJtlwGHkH2rZgn1KSPOnjs2qAR188G', '1234567892', '4');
INSERT INTO `el_user` VALUES ('5', '1', '2021-05-08 21:16:02', '2021-05-08 21:16:02', '0', '18878149218', '$2a$10$sLhEnllc9w4Xng7dnc.RV.TpMP1yhpCRHDXGmWf/M3hm/mFgjTPGS', '1234567893', '4');
