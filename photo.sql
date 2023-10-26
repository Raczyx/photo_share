/*
 Navicat Premium Data Transfer

 Source Server         : 180
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : 172.16.77.180:3306
 Source Schema         : pythondb

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 08/10/2023 19:55:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `pid` int NULL DEFAULT NULL COMMENT '用户收藏的帖子的ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `collect_photogroup_null_fk`(`pid` ASC) USING BTREE,
  INDEX `collect_email_pid_index`(`email` ASC, `pid` ASC) USING BTREE,
  CONSTRAINT `collect_photogroup_null_fk` FOREIGN KEY (`pid`) REFERENCES `photogroup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `collect_user_null_fk` FOREIGN KEY (`email`) REFERENCES `user` (`email`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for images
-- ----------------------------
DROP TABLE IF EXISTS `images`;
CREATE TABLE `images`  (
  `pid` int NOT NULL COMMENT '帖子ID',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片网络地址',
  PRIMARY KEY (`pid`, `url`) USING BTREE,
  CONSTRAINT `images_photogroup_null_fk` FOREIGN KEY (`pid`) REFERENCES `photogroup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for photogroup
-- ----------------------------
DROP TABLE IF EXISTS `photogroup`;
CREATE TABLE `photogroup`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布者用户名',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布者邮箱',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '详细描述信息',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `photogroup_email_index`(`email` ASC) USING BTREE,
  CONSTRAINT `photogroup_user_null_fk` FOREIGN KEY (`email`) REFERENCES `user` (`email`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户邮箱',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '账户状态',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账户创建时间',
  PRIMARY KEY (`email`) USING BTREE,
  INDEX `user_username_index`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for records
-- ----------------------------
DROP VIEW IF EXISTS `records`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `records` AS select `photogroup`.`id` AS `id`,`photogroup`.`username` AS `user`,`photogroup`.`email` AS `email`,`photogroup`.`description` AS `description`,`photogroup`.`title` AS `title`,`photogroup`.`cover` AS `cover`,`c`.`email` AS `uemail` from (`photogroup` left join `collect` `c` on((`photogroup`.`id` = `c`.`pid`)));

SET FOREIGN_KEY_CHECKS = 1;
