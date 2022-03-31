/*
 Navicat Premium Data Transfer

 Source Server         : whitebear
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : 47.107.48.184:3306
 Source Schema         : wage

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 08/06/2021 18:11:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jobs
-- ----------------------------
DROP TABLE IF EXISTS `jobs`;
CREATE TABLE `jobs`  (
  `jid` int NOT NULL AUTO_INCREMENT,
  `jno` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `jname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `jdept` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `jsalary` float NOT NULL,
  `jbonus` float NOT NULL,
  PRIMARY KEY (`jid`) USING BTREE,
  UNIQUE INDEX `jno`(`jno`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jobs
-- ----------------------------
INSERT INTO `jobs` VALUES (1, 'j1', '系统管理员', '开发部', 6000, 3000);
INSERT INTO `jobs` VALUES (2, 'j2', '会计', '财务部', 5000, 2000);
INSERT INTO `jobs` VALUES (3, 'j3', '部门经理', '开发部', 10000, 4000);
INSERT INTO `jobs` VALUES (4, 'j4', '部门秘书', '秘书部', 5000, 3000);
INSERT INTO `jobs` VALUES (6, 'j5', '总经理', '董事会', 100000, 50000);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `usercode` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `authority` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `wno` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `usercode`(`usercode`) USING BTREE,
  INDEX `fk_ws_user`(`wno`) USING BTREE,
  CONSTRAINT `fk_ws_user` FOREIGN KEY (`wno`) REFERENCES `workers` (`wno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '202cb962ac59075b964b07152d234b70', '系统管理员', '181549301');
INSERT INTO `user` VALUES (2, 'z1234', '202cb962ac59075b964b07152d234b70', '公司高层', '181549302');
INSERT INTO `user` VALUES (3, 'z9999', '202cb962ac59075b964b07152d234b70', '普通员工', '181549303');
INSERT INTO `user` VALUES (10, 'z1000', '202cb962ac59075b964b07152d234b70', '公司高层', '181549304');
INSERT INTO `user` VALUES (11, 'z8000', '202cb962ac59075b964b07152d234b70', '财务', '181549305');
INSERT INTO `user` VALUES (12, 'z9998', '202cb962ac59075b964b07152d234b70', '普通员工', '181549306');

-- ----------------------------
-- Table structure for wj
-- ----------------------------
DROP TABLE IF EXISTS `wj`;
CREATE TABLE `wj`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `wno` char(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `jno` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_ws_worker`(`wno`) USING BTREE,
  INDEX `fk_ws_jobs`(`jno`) USING BTREE,
  CONSTRAINT `fk_ws_jobs` FOREIGN KEY (`jno`) REFERENCES `jobs` (`jno`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ws_worker` FOREIGN KEY (`wno`) REFERENCES `workers` (`wno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wj
-- ----------------------------
INSERT INTO `wj` VALUES (1, '181549302', 'j3');
INSERT INTO `wj` VALUES (2, '181549302', 'j3');
INSERT INTO `wj` VALUES (3, '181549303', 'j4');
INSERT INTO `wj` VALUES (4, '181549304', 'j5');
INSERT INTO `wj` VALUES (5, '181549305', 'j2');
INSERT INTO `wj` VALUES (6, '181549306', 'j4');

-- ----------------------------
-- Table structure for workers
-- ----------------------------
DROP TABLE IF EXISTS `workers`;
CREATE TABLE `workers`  (
  `wid` int NOT NULL AUTO_INCREMENT,
  `wno` char(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `wname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `wsex` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `wnative` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `wphone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `weducation` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `wmar_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `wbirthday` date NULL DEFAULT NULL,
  `wcreatetime` date NULL DEFAULT NULL,
  PRIMARY KEY (`wid`) USING BTREE,
  UNIQUE INDEX `wno`(`wno`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of workers
-- ----------------------------
INSERT INTO `workers` VALUES (1, '181549301', 'a', '女', '广东广州', '17728969810', '本科', '未婚', '1996-02-18', '2016-04-18');
INSERT INTO `workers` VALUES (2, '181549302', 'b', '男', '广东广州', '17728698111', '硕士', '已婚', '1985-12-12', '2012-10-11');
INSERT INTO `workers` VALUES (3, '181549303', 'c', '女', '广东广州', '18828698128', '本科', '未婚', '1998-08-16', '2019-06-20');
INSERT INTO `workers` VALUES (5, '181549304', 'Tom', '男', '广东广州', '17728969819', '专科', '未婚', '1999-05-18', '2020-06-18');
INSERT INTO `workers` VALUES (6, '181549305', 'bear', '男', '广东广州', '17728969818', '硕士', '未婚', '1998-09-18', '2021-03-06');
INSERT INTO `workers` VALUES (7, '181549306', 'jack', '男', '广东广州', '17728969817', '本科', '未婚', '2000-11-18', '2021-04-11');
INSERT INTO `workers` VALUES (8, '181549307', '尚小', '男', '广东广州', '17728969812', '专科', '已婚', '2021-06-08', '2021-06-08');

-- ----------------------------
-- Table structure for wsalary
-- ----------------------------
DROP TABLE IF EXISTS `wsalary`;
CREATE TABLE `wsalary`  (
  `wsid` int NOT NULL AUTO_INCREMENT,
  `wno` char(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `wname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `jno` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `jname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `jdept` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `jsalary` float NOT NULL,
  `jbonus` float NOT NULL,
  `total` float NOT NULL,
  `settledate` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `isgrant` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '否',
  `grantdate` date NULL DEFAULT NULL,
  PRIMARY KEY (`wsid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wsalary
-- ----------------------------
INSERT INTO `wsalary` VALUES (1, '181549301', 'a', 'j1', '系统管理员', '开发部', 6000, 3000, 9000, '2021-04', '是', '2021-05-22');
INSERT INTO `wsalary` VALUES (2, '181549302', 'b', 'j3', '部门经理', '开发部', 10000, 4000, 14000, '2021-04', '是', '2021-05-22');
INSERT INTO `wsalary` VALUES (3, '181549303', 'c', 'j4', '部门秘书', '秘书部', 5000, 3000, 8000, '2021-04', '是', '2021-05-22');
INSERT INTO `wsalary` VALUES (4, '181549304', 'Tom', 'j5', '总经理', '董事会', 10000, 50000, 60000, '2021-04', '是', '2021-05-22');
INSERT INTO `wsalary` VALUES (5, '181549305', 'bear', 'j2', '会计', '财务部', 5000, 2000, 7000, '2021-04', '是', '2021-05-22');
INSERT INTO `wsalary` VALUES (18, '181549306', 'jack', 'j4', '部门秘书', '秘书部', 5000, 3000, 8000, '2021-04', '是', '2021-05-22');
INSERT INTO `wsalary` VALUES (19, '181549302', 'b', 'j3', '部门经理', '开发部', 10000, 4000, 14000, '', '是', '2021-06-08');
INSERT INTO `wsalary` VALUES (20, '181549303', 'c', 'j4', '部门秘书', '秘书部', 5000, 3000, 8000, '', '是', '2021-06-08');
INSERT INTO `wsalary` VALUES (21, '181549304', 'Tom', 'j5', '总经理', '董事会', 10000, 50000, 60000, '', '是', '2021-06-08');
INSERT INTO `wsalary` VALUES (22, '181549305', 'bear', 'j2', '会计', '财务部', 5000, 2000, 7000, '', '是', '2021-06-08');
INSERT INTO `wsalary` VALUES (23, '181549306', 'jack', 'j4', '部门秘书', '秘书部', 5000, 3000, 8000, '', '是', '2021-06-08');

SET FOREIGN_KEY_CHECKS = 1;
