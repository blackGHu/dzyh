/*
Navicat MySQL Data Transfer

Source Server         : dzyh
Source Server Version : 50560
Source Host           : localhost:3306
Source Database       : db_dzyh

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2021-07-07 14:17:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_class
-- ----------------------------
DROP TABLE IF EXISTS `tb_class`;
CREATE TABLE `tb_class` (
  `class_id` varchar(20) NOT NULL COMMENT '班级ID',
  `class_name` varchar(20) NOT NULL COMMENT '班级名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_college
-- ----------------------------
DROP TABLE IF EXISTS `tb_college`;
CREATE TABLE `tb_college` (
  `college_id` int(10) NOT NULL COMMENT '学院ID',
  `college_name` varchar(20) NOT NULL COMMENT '学院名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`college_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_course
-- ----------------------------
DROP TABLE IF EXISTS `tb_course`;
CREATE TABLE `tb_course` (
  `course_id` int(10) NOT NULL COMMENT '课程ID',
  `course_name` varchar(20) NOT NULL COMMENT '课程名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_goods
-- ----------------------------
DROP TABLE IF EXISTS `tb_goods`;
CREATE TABLE `tb_goods` (
  `goods_id` int(10) NOT NULL COMMENT '物品ID（主键）',
  `goods_name` varchar(20) NOT NULL COMMENT '物品名称',
  `goods_size` varchar(20) NOT NULL COMMENT '规格',
  `goods_model` varchar(20) NOT NULL COMMENT '型号',
  `goods_price` double(10,0) NOT NULL COMMENT '价格',
  `goods_numbers` int(11) NOT NULL COMMENT '物品剩余数量',
  `goods_address` varchar(20) NOT NULL COMMENT '物品存放地',
  `purpose_id` tinyint(4) NOT NULL COMMENT '物品用途ID',
  `user_id` varchar(11) NOT NULL COMMENT '购买人ID',
  `goods_approval_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '物品申请审核状态：\r\n0——未申请（默认）\r\n1——待审核\r\n2——通过\r\n3——拒绝',
  `goods_use_status` tinyint(4) NOT NULL COMMENT '物品使用状态：\r\n0——借用\r\n1——领用\r\n2——归还\r\n3——报废',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_goods_purpose
-- ----------------------------
DROP TABLE IF EXISTS `tb_goods_purpose`;
CREATE TABLE `tb_goods_purpose` (
  `purpose_id` tinyint(4) NOT NULL COMMENT '物品用途ID',
  `purpose_name` varchar(20) NOT NULL COMMENT '物品用途名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`purpose_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_major
-- ----------------------------
DROP TABLE IF EXISTS `tb_major`;
CREATE TABLE `tb_major` (
  `major_id` int(10) NOT NULL COMMENT '专业ID',
  `major_name` varchar(20) NOT NULL COMMENT '专业名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`major_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order` (
  `order_id` int(10) NOT NULL COMMENT '料单ID（主键）',
  `college_id` int(10) NOT NULL COMMENT '学院ID',
  `major_id` int(10) NOT NULL COMMENT '专业ID',
  `course_id` int(10) NOT NULL COMMENT '课程ID',
  `class_id` varchar(20) NOT NULL COMMENT '班级ID',
  `user_id` varchar(11) NOT NULL COMMENT '用户ID（限定教师）',
  `order_describe` varchar(255) DEFAULT NULL COMMENT '料单描述',
  `goods_id` int(10) NOT NULL COMMENT '物品ID——用来获得物品申请审核状态：\r\n物品申请审核状态：\r\n0——未申请（默认）\r\n1——待审核\r\n2——通过\r\n3——拒绝',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role` (
  `role_id` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0——访客（默认）\r\n1——学生\r\n2——教师\r\n3——管理员\r\n4——超级管理员\r\n5——管理员，教师\r\n还有其他根据后面讨论进行更改',
  `role_name` varchar(10) NOT NULL COMMENT '角色名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `user_id` varchar(11) CHARACTER SET utf8mb4 NOT NULL COMMENT '登录账号（主键）学生学号/教师工号',
  `user_password` varchar(20) NOT NULL COMMENT '登录密码',
  `user_name` varchar(20) NOT NULL COMMENT '姓名',
  `user_phone` int(11) NOT NULL COMMENT '手机账号',
  `role_id` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0——访客（默认）\r\n1——学生\r\n2——教师\r\n3——管理员\r\n4——超级管理员\r\n5——管理员，教师\r\n还有其他根据后面讨论进行更改',
  `user_regist_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户注册管理状态：\r\n0——未注册（默认）\r\n1——待审核\r\n2——通过\r\n3——拒绝',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
