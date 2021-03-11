/*
Navicat MySQL Data Transfer

Source Server         : 47.92.103.143
Source Server Version : 50711
Source Host           : 47.92.103.143:3306
Source Database       : efo

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2019-02-19 13:38:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for auth
-- ----------------------------
DROP TABLE IF EXISTS `auth`;
CREATE TABLE `auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_uploadable` int(11) NOT NULL DEFAULT '1' COMMENT '是否可以上传（需要判断对应的文件是否是文件夹），0不可以，1可以',
  `is_deletable` int(11) NOT NULL DEFAULT '1' COMMENT '是否可以删除，0不可以，1可以',
  `is_updatable` int(11) NOT NULL DEFAULT '1' COMMENT '是否可以更新，0不可以，1可以',
  `user_id` int(11) NOT NULL,
  `file_id` bigint(20) NOT NULL,
  `is_visible` int(11) NOT NULL DEFAULT '1' COMMENT '是否可以查看，0不可以，1可以',
  `is_downloadable` int(11) NOT NULL DEFAULT '1' COMMENT '用户是否可以下载，0不可以，1可以',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_auth_user1_idx` (`user_id`),
  KEY `fk_auth_file1_idx` (`file_id`),
  CONSTRAINT `fk_auth_file1` FOREIGN KEY (`file_id`) REFERENCES `file` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_auth_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='用户对应指定文件的权限表，覆盖用户表的权限';

-- ----------------------------
-- Records of auth
-- ----------------------------
INSERT INTO `auth` VALUES ('3', '1', '1', '1', '1', '4', '1', '1', '2019-02-19 13:24:46');
INSERT INTO `auth` VALUES ('4', '1', '1', '1', '1', '3', '1', '1', '2019-02-19 13:24:46');
INSERT INTO `auth` VALUES ('5', '1', '1', '1', '1', '5', '1', '1', '2019-02-19 13:24:46');
INSERT INTO `auth` VALUES ('7', '1', '1', '1', '1', '7', '1', '1', '2019-02-19 13:26:45');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `cat_id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='文件分类';

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('1', '图片', '2019-02-19 11:39:56');
INSERT INTO `category` VALUES ('2', '我的作业', '2019-02-19 11:44:43');
INSERT INTO `category` VALUES ('3', '软件安装包', '2019-02-19 11:44:56');
INSERT INTO `category` VALUES ('4', '测试分类', '2019-02-19 13:23:41');

-- ----------------------------
-- Table structure for download
-- ----------------------------
DROP TABLE IF EXISTS `download`;
CREATE TABLE `download` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下载时间',
  `user_id` int(11) NOT NULL,
  `file_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_download_user1_idx` (`user_id`),
  KEY `fk_download_file1_idx` (`file_id`),
  CONSTRAINT `fk_download_file1` FOREIGN KEY (`file_id`) REFERENCES `file` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_download_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='下载历史表';

-- ----------------------------
-- Records of download
-- ----------------------------
INSERT INTO `download` VALUES ('4', '2019-02-19 13:27:58', '1', '7');

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(256) DEFAULT '' COMMENT '文件名',
  `suffix` varchar(16) NOT NULL DEFAULT '' COMMENT '文件后缀',
  `local_url` varchar(1024) NOT NULL DEFAULT '' COMMENT '本地路径',
  `visit_url` varchar(1024) NOT NULL DEFAULT '' COMMENT '客户端访问路径',
  `size` bigint(20) NOT NULL DEFAULT '0' COMMENT '文件大小，单位bit',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `description` varchar(1024) DEFAULT '' COMMENT '文件描述',
  `check_times` int(11) NOT NULL DEFAULT '0' COMMENT '查看次数',
  `download_times` int(11) NOT NULL DEFAULT '0' COMMENT '下载次数',
  `tag` varchar(45) DEFAULT '' COMMENT '文件标签',
  `user_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `is_downloadable` int(11) NOT NULL DEFAULT '1' COMMENT '（全局权限）文件是否可以下载，0不可以，1可以',
  `is_uploadable` int(11) NOT NULL DEFAULT '1' COMMENT '（全局权限）文件夹是否允许上传（需要判断文件是否是文件夹），0不可以，1可以',
  `is_visible` int(11) NOT NULL DEFAULT '1' COMMENT '（全局权限）文件是否可见，0不可以，1可以',
  `is_deletable` int(11) NOT NULL DEFAULT '1' COMMENT '（全局权限）文件是否可以删除，0不可以，1可以',
  `is_updatable` int(11) NOT NULL DEFAULT '1' COMMENT '（全局权限）文件是否可以更新，0不可以，1可以',
  `last_modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近一次修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `file_id_UNIQUE` (`id`),
  UNIQUE KEY `local_url_UNIQUE` (`local_url`),
  UNIQUE KEY `visit_url_UNIQUE` (`visit_url`),
  KEY `fk_file_user_idx` (`user_id`),
  KEY `fk_file_category1_idx` (`category_id`),
  CONSTRAINT `fk_file_category1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_file_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='文件列表';

-- ----------------------------
-- Records of file
-- ----------------------------
INSERT INTO `file` VALUES ('3', 'CvoBNFh1hvyADFH0AAG1VxF-haU686.jpg', 'jpg', 'C:\\Users\\Administrator\\Desktop\\upload\\20190219\\CvoBNFh1hvyADFH0AAG1VxF-haU686.jpg', '/file/2019/02/19/942022178.jpg', '36828', '2019-02-19 13:24:46', '这是描述这是描述这是描述', '0', '0', '测试', '1', '4', '1', '1', '1', '1', '1', '2019-02-19 13:24:46');
INSERT INTO `file` VALUES ('4', 'CvoBM1hFMCOAcXlVAAGlnYsvo5s738.jpg', 'jpg', 'C:\\Users\\Administrator\\Desktop\\upload\\20190219\\CvoBM1hFMCOAcXlVAAGlnYsvo5s738.jpg', '/file/2019/02/19/184887448.jpg', '34354', '2019-02-19 13:24:46', '这是描述这是描述这是描述', '0', '0', '测试', '1', '4', '1', '1', '1', '1', '1', '2019-02-19 13:24:46');
INSERT INTO `file` VALUES ('5', 'CvoBNFkJVICAY9_0AACZnzgCTvo950.jpg', 'jpg', 'C:\\Users\\Administrator\\Desktop\\upload\\20190219\\CvoBNFkJVICAY9_0AACZnzgCTvo950.jpg', '/file/2019/02/19/372200235.jpg', '9198', '2019-02-19 13:24:46', '这是描述这是描述这是描述', '0', '0', '测试', '1', '4', '1', '1', '1', '1', '1', '2019-02-19 13:24:46');
INSERT INTO `file` VALUES ('7', 'efo-master.zip', 'zip', 'C:\\Users\\Administrator\\Desktop\\upload\\20190219\\efo-master.zip', '/file/2019/02/19/931705290.zip', '5090534', '2019-02-19 13:26:45', '111111111111111111111111', '0', '1', '软件安装包', '1', '3', '1', '1', '1', '1', '1', '2019-02-19 13:26:45');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(16) NOT NULL DEFAULT '' COMMENT '用户名',
  `real_name` varchar(45) DEFAULT '' COMMENT '真实姓名',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱地址',
  `password` varchar(128) NOT NULL DEFAULT '' COMMENT '登录密码',
  `permission` int(11) NOT NULL DEFAULT '1' COMMENT '0（禁止登录），1（正常，普通用户），2（正常，管理员），3（正常，超级管理员）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `last_login_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次登录时间',
  `is_downloadable` int(11) NOT NULL DEFAULT '1' COMMENT '（全局权限）用户是否可以下载，0不可以，1可以',
  `is_uploadable` int(11) NOT NULL DEFAULT '1' COMMENT '（全局权限）用户是否可以上传，0不可以，1可以',
  `is_visible` int(11) NOT NULL DEFAULT '1' COMMENT '（全局权限）用户是否可以查看文件，0不可以，1可以',
  `is_deletable` int(11) NOT NULL DEFAULT '0' COMMENT '（全局权限）用户可以删除文件，0不可以，1可以',
  `is_updatable` int(11) NOT NULL DEFAULT '0' COMMENT '（全局权限）用户是否可以更新文件，0不可以，1可以',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `create_time_UNIQUE` (`create_time`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '系统', 'system@local.host', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '3', '2019-02-19 11:39:56', '2019-02-19 13:22:52', '1', '1', '1', '1', '1', '');
INSERT INTO `user` VALUES ('2', 'cookie', '', '2118119@qq.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '1', '2019-02-19 13:22:18', '2019-02-19 13:22:28', '1', '1', '1', '0', '0', '');
