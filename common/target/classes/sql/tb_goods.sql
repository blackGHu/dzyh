/*
Navicat MySQL Data Transfer

Source Server         : dzyh
Source Server Version : 50560
Source Host           : localhost:3306
Source Database       : db_dzyh

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2021-08-07 15:22:49
*/

SET FOREIGN_KEY_CHECKS=0;

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
  `category_id` int(10) NOT NULL COMMENT '物品类别ID',
  `purpose_id` tinyint(4) NOT NULL COMMENT '物品用途ID',
  `buy_user_name` varchar(11) NOT NULL COMMENT '购买人ID——对应user_id',
  `role_id` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0——访客（默认）\r\n1——学生\r\n2——教师\r\n3——管理员\r\n4——超级管理员\r\n5——管理员，教师\r\n还有其他根据后面讨论进行更改',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `version` int(11) NOT NULL COMMENT 'Mybatis plus  乐观锁版本控制\r\n',
  `deleted` tinyint(4) NOT NULL COMMENT 'Mybatis plus 软删除控制\r\n0——未删除\r\n1——已删除',
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
