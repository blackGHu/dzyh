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
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category` (
  `category_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '类别ID',
  `category_name` varchar(20) NOT NULL COMMENT '类别名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
  `college_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '学院ID',
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
-- Table structure for tb_repertory
-- ----------------------------
DROP TABLE IF EXISTS `tb_repertory`;
CREATE TABLE `tb_repertory` (
  `repertory_id` int NOT NULL AUTO_INCREMENT COMMENT '物品ID（主键）',
  `repertory_type` varchar(20) NOT NULL COMMENT '物品类型',
  `repertory_name` varchar(20) NOT NULL COMMENT '物品名称',
  `repertory_size` varchar(20) NOT NULL COMMENT '规格',
  `repertory_model` varchar(20) NOT NULL COMMENT '型号',
  `repertory_numbers` int NOT NULL COMMENT '物品剩余数量',
  `repertory_use` varchar(20) NOT NULL COMMENT '物品用途',
  `repertory_autho` varchar(20) NOT NULL COMMENT '物品权限',
  `repertory_address` varchar(20) NOT NULL COMMENT '物品存放地址',
  `repertory_price` double(10,0) NOT NULL COMMENT '价格',
  `repertory_buyname` varchar(11) NOT NULL COMMENT '购买人姓名',
  `repertory_message` varchar(11) NOT NULL COMMENT '备注信息',
  `storage_time` datetime NOT NULL COMMENT '入库时间',
  `version` int NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`repertory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ---------------------------
-- Table structure for re_understock
-- ----------------------------
DROP TABLE IF EXISTS `re_understock`;
CREATE TABLE `re_understock` (
  `us_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '缺货记录ID（主键）',
  `us_type` varchar(20) NOT NULL COMMENT '物品类型',
  `us_name` varchar(20) NOT NULL COMMENT '物品名称',
  `us_size` varchar(20) NOT NULL COMMENT '规格',
  `us_model` varchar(20) NOT NULL COMMENT '型号',
  `us_numbers` int(11) NOT NULL COMMENT '物品申请数量',
  `user_Name` varchar(20) NOT NULL COMMENT '申请人的姓名',
  `read_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '记录是否被读，未读0，已读1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`us_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for tb_goods
-- ----------------------------
DROP TABLE IF EXISTS `tb_goods`;
CREATE TABLE `tb_goods` (
  `goods_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '物品ID（主键）',
  `goods_name` varchar(20) NOT NULL COMMENT '物品名称',
  `goods_size` varchar(20) NOT NULL COMMENT '规格',
  `goods_model` varchar(20) NOT NULL COMMENT '型号',
  `goods_price` double(10,0) NOT NULL COMMENT '价格',
  `goods_numbers` int(11) NOT NULL COMMENT '物品剩余数量',
  `goods_address` varchar(20) NOT NULL COMMENT '物品存放地',
  `category_name` varchar(20) NOT NULL COMMENT '物品类别',
  `buy_user_name` varchar(11) NOT NULL COMMENT '购买人ID——对应user_id',
  `role_name` varchar(10) NOT NULL DEFAULT '0' COMMENT '0——访客（默认）\r\n1——学生\r\n2——教师\r\n3——管理员\r\n4——超级管理员\r\n5——管理员，教师\r\n还有其他根据后面讨论进行更改',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  `purpose_name` varchar(20) NOT NULL COMMENT '物品用途',
  `storage_time` datetime NOT NULL COMMENT '入库时间',
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for tb_goods_apply
-- ----------------------------
DROP TABLE IF EXISTS `tb_goods_apply`;
CREATE TABLE `tb_goods_apply` (
  `order_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '料单ID（主键）',
  `borrow_time` datetime DEFAULT NULL COMMENT '创建时间',
  `return_time` datetime DEFAULT NULL COMMENT '修改时间',
  `goods_approval_status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '物品申请审核状态：\r\n-1——未申请（默认）\r\n0——待审核\r\n1——通过\r\n2——拒绝\r\n',
  `goods_use_status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '物品使用状态：\r\n-1——未使用（默认）\r\n0——借用\r\n1——领用\r\n',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  `category_name` varchar(20) NOT NULL COMMENT '物品类别',
  `repertory_name` varchar(20) NOT NULL COMMENT '物品名称',
  `repertory_size` varchar(20) NOT NULL COMMENT '规格',
  `repertory_model` varchar(20) NOT NULL COMMENT '型号',
  `repertory_numbers` int(11) NOT NULL COMMENT '物品剩余数量',
  `apply_user_name` varchar(11) NOT NULL COMMENT '申请人ID——对应user_id',
  `approval_user_name` varchar(11) DEFAULT '' COMMENT '审批人ID——user_id',
  `purpose_name` varchar(20) NOT NULL COMMENT '物品用途',
  `repertory_address` varchar(20) NOT NULL COMMENT '物品存放地',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_major
-- ----------------------------
DROP TABLE IF EXISTS `tb_major`;
CREATE TABLE `tb_major` (
  `major_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '专业ID',
  `major_name` varchar(20) NOT NULL COMMENT '专业名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`major_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_material_list
-- ----------------------------
DROP TABLE IF EXISTS `tb_material_list`;
CREATE TABLE `tb_material_list` (
  `material_list_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '料单ID（主键）',
  `college_name` varchar(20) NOT NULL COMMENT '学院ID',
  `major_name` varchar(20) NOT NULL COMMENT '专业ID',
  `course_name` varchar(20) NOT NULL COMMENT '课程ID',
  `class_name` varchar(20) NOT NULL COMMENT '班级ID',
  `order_describe` varchar(255) DEFAULT NULL COMMENT '料单列表：\r\n存储各个goods对象（tjk）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  `teacher_name` varchar(11) NOT NULL COMMENT '教师ID——对应user_id',
  PRIMARY KEY (`material_list_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_material_list_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_material_list_order`;
CREATE TABLE `tb_material_list_order` (
  `material_list_order_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '料单申请的ID（主键）',
  `apply_number` int(11) NOT NULL COMMENT '申请的数量',
  `goods_approval_statue` int(10) NOT NULL DEFAULT '-1' COMMENT '物品ID——用来获得料单申请审核状态：\r\n物品申请审核状态：\r\n-1——未申请（默认）\r\n0——待审核\r\n1——通过\r\n2——拒绝',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  `apply_user_name` varchar(11) NOT NULL COMMENT '申请人ID——对应user_id',
  `college_name` varchar(20) NOT NULL COMMENT '学院ID',
  `major_name` varchar(20) NOT NULL COMMENT '专业ID',
  `course_name` varchar(20) NOT NULL COMMENT '课程ID',
  `class_name` varchar(20) NOT NULL COMMENT '班级ID',
  `order_describe` varchar(255) DEFAULT NULL COMMENT '料单列表：\r\n存储各个goods对象（tjk）',
  `teacher_name` varchar(11) NOT NULL COMMENT '教师ID——对应user_id',
  PRIMARY KEY (`material_list_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(11) CHARACTER SET utf8mb4 NOT NULL COMMENT '登录账号（主键）学生学号/教师工号',
  `role_id` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0——访客（默认）\r\n1——学生\r\n2——教师\r\n3——管理员\r\n4——超级管理员\r\n5——管理员，教师\r\n还有其他根据后面讨论进行更改',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_name` varchar(20) NOT NULL COMMENT '0——访客（默认）\r\n1——学生\r\n2——教师\r\n3——管理员\r\n4——超级管理员\r\n5——管理员，教师\r\n还有其他根据后面讨论进行更改',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE `role_permissions` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0——访客（默认）\r\n1——学生\r\n2——教师\r\n3——管理员\r\n4——超级管理员\r\n5——管理员，教师\r\n还有其他根据后面讨论进行更改',
  `permissions_id` int(10) NOT NULL COMMENT '权限id，id对应于permissions表中的resource',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `id` int(10) NOT NULL COMMENT 'id',
  `permissions_name` varchar(20) NOT NULL COMMENT '对应的资源名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(15) NOT NULL COMMENT '登录账号（主键）学生学号/教师工号',
  `user_password` varchar(20) NOT NULL COMMENT '登录密码',
  `user_name` varchar(20) NOT NULL COMMENT '姓名',
  `user_phone` varchar(15) NOT NULL COMMENT '手机账号',
  `role_id` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0——访客（默认）\r\n1——学生\r\n2——教师\r\n3——管理员\r\n4——超级管理员\r\n5——管理员，教师\r\n还有其他根据后面讨论进行更改',
  `regist_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户注册管理状态：\r\n0——未注册（默认）\r\n1——待审核\r\n2——通过\r\n3——拒绝',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
