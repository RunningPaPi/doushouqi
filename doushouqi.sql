SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for game_record
-- ----------------------------
-- DROP TABLE IF EXISTS `game_record`;
CREATE TABLE `game_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `game_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '游戏类型：好友对战:FIGHT随机匹配:MATCH',
  `contest_num` int(11) NULL DEFAULT 0 COMMENT '参赛人数',
  `pk_times` int(11) NULL DEFAULT 0 COMMENT 'pk次数',
  `create_time` timestamp(0) NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp(0) NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for game_user_record
-- ----------------------------
-- DROP TABLE IF EXISTS `game_user_record`;
CREATE TABLE `game_user_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `game_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '游戏类型：好友对战:FIGHT随机匹配:MATCH',
  `user_id` bigint(20) NOT NULL COMMENT 'userId',
  `head_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `play_times` int(11) NULL DEFAULT NULL COMMENT '玩的次数',
  `win_times` int(11) NULL DEFAULT NULL COMMENT '赢的次数',
  `invite_times` int(11) NULL DEFAULT NULL COMMENT '邀战次数',
  `success_invite_times` int(11) NULL DEFAULT NULL COMMENT '成功邀战次数',
  `create_time` timestamp(0) NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`, `user_id`, `game_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '玩家游戏记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for schedule_job
-- ----------------------------
-- DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务分组名称',
  `cron_expression` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '触发时间表达式',
  `is_concurrent` tinyint(1) NULL DEFAULT NULL COMMENT '是否并发',
  `is_springBean` tinyint(1) NULL DEFAULT NULL COMMENT '是否是Spring中定义的Bean,如果不是需要设置全类名,对应class_name字段需要配置',
  `target_object` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '执行任务类名称',
  `target_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '执行任务类方法名称',
  `clazz_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '全类名，前置条件，当is_springBean为0时',
  `arguments` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '执行任务方法参数',
  `jobStatus` smallint(2) NULL DEFAULT 0 COMMENT '任务状态：-1.执行失败 0.就绪 1.执行中 2.执行成功',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务描述',
  `create_time` timestamp(0) NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统任务信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for schedule_job_fail_log
-- ----------------------------
-- DROP TABLE IF EXISTS `schedule_job_fail_log`;
CREATE TABLE `schedule_job_fail_log`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(11) NULL DEFAULT NULL COMMENT '任务ID',
  `job_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务组名称',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务失败原因',
  `create_time` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '任务失败日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_distribution
-- ----------------------------
-- DROP TABLE IF EXISTS `system_distribution`;
CREATE TABLE `system_distribution`  (
  `distr_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '发布Id',
  `app_type` smallint(6) NULL DEFAULT NULL COMMENT 'app类型.1-ios;2-android;3-小程序;4-h5',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '版本',
  `app_store_id` smallint(6) NULL DEFAULT NULL COMMENT '应用商店ID.1-安智;2-搜狗;3-西西软件;4-同步推;5-机锋;6-百度;7-360;8-应用宝.',
  `distr_state` smallint(6) NULL DEFAULT NULL COMMENT '发布状态.1-未发布;2已发布',
  `create_time` timestamp(0) NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`distr_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '应用发布状态' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
-- DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `gender` smallint(4) NULL DEFAULT NULL COMMENT '性别：1.男 2.女',
  `head_pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像路径',
  `openid` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '微信openid',
  `union_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '微信登录唯一标志ID',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户密码',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份令牌',
  `city_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '市编码',
  `province_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '省编码',
  `area_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地区编码',
  `token_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '系统分配用户的ID:暂定6位',
  `register_type` smallint(4) NULL DEFAULT 3 COMMENT '注册方式：1.手机 2.邮箱 3.微信 4.QQ',
  `status` smallint(4) NOT NULL DEFAULT 1 COMMENT '状态：-1冻结 0.无效 1.正常',
  `is_robot` tinyint(1) NULL DEFAULT 0 COMMENT '是否是机器人：0.否 1.是',
  `create_time` timestamp(0) NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 122 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '平台用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
-- DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `birthday` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '出生日期：格式如1992-02-03',
  `invite_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邀请码',
  `level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '等级',
  `diamond` int(11) NULL DEFAULT 0 COMMENT '钻石',
  `coin` int(11) NOT NULL DEFAULT 0 COMMENT '金币',
  `point` int(11) NOT NULL DEFAULT 0 COMMENT '积分',
  `balance` bigint(11) NOT NULL DEFAULT 0 COMMENT '红包余额:单位为分',
  `balance_withdrawable` bigint(11) NOT NULL DEFAULT 0 COMMENT '可提现红包金额',
  `balance_freezed` bigint(11) NOT NULL DEFAULT 0 COMMENT '冻结的红包金额  提现申请的金额被冻结',
  `alipay_account` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '绑定的支付宝帐号',
  `alipay_realname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付宝认证的实名',
  `phone_validated` tinyint(1) NULL DEFAULT 0 COMMENT '手机是否已验证：0.否 1.是',
  `realname_validated` tinyint(1) NULL DEFAULT 0 COMMENT '实名是否已验证：0.否 1.是',
  `alipay_account_validated` tinyint(1) NULL DEFAULT 0 COMMENT '支付宝账户是否已验证：0.否 1.是',
  `last_login_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
  `create_time` timestamp(0) NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_USER_ID`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 122 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户基本信息' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
