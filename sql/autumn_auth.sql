/*
 Navicat Premium Dump SQL

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80100 (8.1.0)
 Source Host           : localhost:3306
 Source Schema         : autumn_auth

 Target Server Type    : MySQL
 Target Server Version : 80100 (8.1.0)
 File Encoding         : 65001

 Date: 14/05/2025 16:43:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authorization_user
-- ----------------------------
DROP TABLE IF EXISTS `authorization_user`;
CREATE TABLE `authorization_user`
(
    `id`                bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `account`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '账号',
    `password`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
    `nickname`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '昵称',
    `avatar`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '头像',
    `mobile`            varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '手机号',
    `email`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
    `remark`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '个人简介',
    `locked`            tinyint(1)                                             NULL DEFAULT 0 COMMENT '是否锁定 0:已锁定 1:未锁定',
    `status`            tinyint(1)                                             NULL DEFAULT 1 COMMENT '启用状态 0:未启用 1:已启用',
    `sex`               tinyint(1)                                             NULL DEFAULT NULL COMMENT '性别',
    `account_expire`    datetime                                               NULL DEFAULT NULL COMMENT '账号过期时间',
    `credential_expire` datetime                                               NULL DEFAULT NULL COMMENT '凭据过期时间',
    `birthday`          datetime                                               NULL DEFAULT NULL COMMENT '生日',
    `login_time`        datetime                                               NULL DEFAULT NULL COMMENT '上次登录时间',
    `create_time`       datetime                                               NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `flag`              tinyint(1)                                             NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:已删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `username` (`account` ASC) USING BTREE,
    UNIQUE INDEX `mobile` (`mobile` ASC) USING BTREE,
    UNIQUE INDEX `email` (`email` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authorization_user
-- ----------------------------
INSERT INTO `authorization_user`
VALUES (1, 'admin', '$2a$10$jRxGm1MJRSwxuTI17yFRZuRdJ4YbsH9SkqjL1/QGAoUavMEhFUd2a', 'admin',
        'http://60.205.234.52:9000/autumn-media/img/2025/05/14/dadfdcd6e6244a798006453cfdb6a66d.png', '18607160232', '1174504109@qq.com', NULL, 1, 1,
        0, '2099-12-31 23:59:59', '2099-12-31 23:59:59', '2025-05-13 16:00:00', '2025-05-14 16:25:28', '2025-05-12 14:51:21', '2025-05-14 16:37:29',
        0);

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict`
(
    `id`    bigint                                                 NOT NULL,
    `name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '属性名称',
    `code`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '字典code',
    `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NULL COMMENT '内容',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dict
-- ----------------------------
INSERT INTO `dict`
VALUES (1, '菜单类型', '100',
        '[{\"key\":\"101\",\"value\":\"directory\",\"des\":\"目录\"},{\"key\":\"102\",\"value\":\"menu\",\"des\":\"菜单\"},{\"key\":\"103\",\"value\":\"button\",\"des\":\"按钮\"}]');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`
(
    `id`               bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '菜单表id',
    `parent_id`        bigint                                                 NULL DEFAULT NULL COMMENT '父级id',
    `menu_type`        int                                                    NULL DEFAULT NULL COMMENT '菜单类型 `0`代表菜单、`1`代表`iframe`、`2`代表外链、`3`代表按钮 ',
    `title`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
    `name`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由名称',
    `path`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由路径',
    `component`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '组件路径',
    `sort`             int                                                    NULL DEFAULT NULL COMMENT '排序',
    `redirect`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由重定向',
    `icon`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图标',
    `extra_icon`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单名称右侧的额外图标',
    `enter_transition` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '进场动画（页面加载动画）',
    `leave_transition` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '离场动画（页面加载动画）',
    `active_path`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单激活',
    `auths`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '按钮权限',
    `frame_src`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '链接地址',
    `frame_loading`    tinyint(1)                                             NULL DEFAULT NULL COMMENT '加载动画',
    `keep_alive`       tinyint(1)                                             NULL DEFAULT NULL COMMENT '缓存页面',
    `hidden_tag`       tinyint(1)                                             NULL DEFAULT NULL COMMENT '标签页',
    `fixed_tag`        tinyint(1)                                             NULL DEFAULT NULL COMMENT '固定标签页',
    `show_link`        tinyint(1)                                             NULL DEFAULT NULL COMMENT '是否显示该菜单',
    `show_parent`      tinyint(1)                                             NULL DEFAULT NULL COMMENT '是否显示父级菜单',
    `create_time`      datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`      datetime                                               NULL DEFAULT NULL COMMENT '修改时间',
    `create_user`      bigint                                                 NULL DEFAULT NULL COMMENT '创建用户id',
    `update_user`      bigint                                                 NULL DEFAULT NULL COMMENT '修改用户id',
    `flag`             bit(1)                                                 NULL DEFAULT b'0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 136
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu`
VALUES (100, 0, 0, 'menus.pureSysManagement', 'SystemUserParent', '/system', '', 1, '', 'ri:settings-3-line', '', '', '', '', '', '', 1, 0, 0, 0, 1,
        0, NULL, '2025-05-06 14:54:47', NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (110, 100, 0, 'menus.pureUser', 'SystemUser', '/system/user/index', NULL, 1, NULL, 'ri:admin-line', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (111, 110, 3, 'buttons.pureUserAdd', '新增用户', NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 'system:user:add', NULL, NULL, NULL, NULL,
        NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (112, 110, 3, 'buttons.pureUserEdit', '编辑用户', NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, 'system:user:edit', NULL, NULL, NULL,
        NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (113, 110, 3, 'buttons.pureUserDelete', '删除用户', NULL, NULL, 3, NULL, NULL, NULL, NULL, NULL, NULL, 'system:user:delete', NULL, NULL, NULL,
        NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (114, 110, 3, 'buttons.pureUserStatus', '用户状态', '', '', 4, '', '', '', '', '', '', 'system:user:status', '', 1, 0, 0, 0, 1, 0, NULL, NULL,
        NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (115, 110, 3, 'buttons.pureUserAssignRole', '分配角色', NULL, NULL, 5, NULL, NULL, NULL, NULL, NULL, NULL, 'system:user:assignRole', NULL,
        NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (116, 110, 3, 'buttons.pureUserPasswd', '重置密码', NULL, NULL, 6, NULL, NULL, NULL, NULL, NULL, NULL, 'system:user:changePassword', NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (120, 100, 0, 'menus.pureRole', 'SystemRole', '/system/role/index', NULL, 2, NULL, 'ri:admin-fill', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (121, 120, 3, 'buttons.pureRoleAdd', '新增角色', NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 'system:role:add', NULL, NULL, NULL, NULL,
        NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (122, 120, 3, 'buttons.pureRoleEdit', '编辑角色', NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, 'system:role:edit', NULL, NULL, NULL,
        NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (123, 120, 3, 'buttons.pureRoleDelete', '删除角色', NULL, NULL, 3, NULL, NULL, NULL, NULL, NULL, NULL, 'system:role:delete', NULL, NULL, NULL,
        NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (124, 120, 3, 'buttons.pureRolePermission', '角色权限', NULL, NULL, 4, NULL, NULL, NULL, NULL, NULL, NULL, 'system:role:auth', NULL, NULL,
        NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (125, 120, 3, 'buttons.pureRoleIsLock', '角色锁定', NULL, NULL, 4, NULL, NULL, NULL, NULL, NULL, NULL, 'system:role:isLock', NULL, NULL, NULL,
        NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (130, 100, 0, 'menus.pureSystemMenu', 'SystemMenu', '/system/menu/index', NULL, 3, NULL, 'ep:menu', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (131, 130, 3, 'buttons.pureMenuAdd', '新增菜单', NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 'system:menu:add', NULL, NULL, NULL, NULL,
        NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (132, 130, 3, 'buttons.pureMenuEdit', '编辑菜单', NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, 'system:menu:edit', NULL, NULL, NULL,
        NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');
INSERT INTO `menu`
VALUES (133, 130, 3, 'buttons.pureMenuDelete', '删除菜单', NULL, NULL, 3, NULL, NULL, NULL, NULL, NULL, NULL, 'system:menu:delete', NULL, NULL, NULL,
        NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, b'0');

-- ----------------------------
-- Table structure for oauth2_authorization
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization`
(
    `id`                            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `registered_client_id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `principal_name`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `authorization_grant_type`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `authorized_scopes`             varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `attributes`                    text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `state`                         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL,
    `authorization_code_value`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `authorization_code_issued_at`  timestamp                                               NULL DEFAULT NULL,
    `authorization_code_expires_at` timestamp                                               NULL DEFAULT NULL,
    `authorization_code_metadata`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `access_token_value`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `access_token_issued_at`        timestamp                                               NULL DEFAULT NULL,
    `access_token_expires_at`       timestamp                                               NULL DEFAULT NULL,
    `access_token_metadata`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `access_token_type`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL,
    `access_token_scopes`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `refresh_token_value`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `refresh_token_issued_at`       timestamp                                               NULL DEFAULT NULL,
    `refresh_token_expires_at`      timestamp                                               NULL DEFAULT NULL,
    `refresh_token_metadata`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `oidc_id_token_value`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `oidc_id_token_issued_at`       timestamp                                               NULL DEFAULT NULL,
    `oidc_id_token_expires_at`      timestamp                                               NULL DEFAULT NULL,
    `oidc_id_token_metadata`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `oidc_id_token_claims`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `user_code_value`               text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `user_code_issued_at`           timestamp                                               NULL DEFAULT NULL,
    `user_code_expires_at`          timestamp                                               NULL DEFAULT NULL,
    `user_code_metadata`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `device_code_value`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `device_code_issued_at`         timestamp                                               NULL DEFAULT NULL,
    `device_code_expires_at`        timestamp                                               NULL DEFAULT NULL,
    `device_code_metadata`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth2_authorization
-- ----------------------------
-- ----------------------------
-- Table structure for oauth2_authorization_consent
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent`
(
    `registered_client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `principal_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `authorities`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NOT NULL,
    PRIMARY KEY (`registered_client_id`, `principal_name`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth2_authorization_consent
-- ----------------------------
INSERT INTO `oauth2_authorization_consent`
VALUES ('1', 'admin', 'SCOPE_openid,SCOPE_profile');
INSERT INTO `oauth2_authorization_consent`
VALUES ('1',
        '{\"createTime\":\"2024-11-24 22:55:50\",\"flag\":0,\"id\":1,\"nickname\":\"秋风\",\"password\":\"\",\"phone\":\"18607150232\",\"salt\":\"8cf59a89cdfb4c8d855b84ddf66cb2bc\",\"username\":\"admin\"}',
        'SCOPE_openid,SCOPE_profile');
INSERT INTO `oauth2_authorization_consent`
VALUES ('2',
        '{\"createTime\":\"2024-11-24 22:55:50\",\"flag\":0,\"id\":1,\"nickname\":\"秋风\",\"password\":\"\",\"phone\":\"18607150232\",\"salt\":\"8cf59a89cdfb4c8d855b84ddf66cb2bc\",\"username\":\"admin\"}',
        'SCOPE_message.read');
INSERT INTO `oauth2_authorization_consent`
VALUES ('3', '1', 'SCOPE_message.read');
INSERT INTO `oauth2_authorization_consent`
VALUES ('3', 'admin', 'SCOPE_openid,SCOPE_message.read,SCOPE_message.write,SCOPE_profile');
INSERT INTO `oauth2_authorization_consent`
VALUES ('3',
        '{\"email\":\"1174504109@qq.com\",\"flag\":0,\"id\":1,\"nickname\":\"秋风\",\"password\":\"\",\"phone\":\"18607160232\",\"username\":\"admin\"}',
        'SCOPE_openid,SCOPE_profile');

-- ----------------------------
-- Table structure for oauth2_registered_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client`
(
    `id`                            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `client_id`                     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `client_id_issued_at`           timestamp                                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `client_secret`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL,
    `client_secret_expires_at`      timestamp                                               NULL     DEFAULT NULL,
    `client_name`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `client_authentication_methods` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NOT NULL,
    `authorization_grant_types`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `redirect_uris`                 text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `post_logout_redirect_uris`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `scopes`                        text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NOT NULL,
    `client_settings`               text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NOT NULL,
    `token_settings`                text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth2_registered_client
-- ----------------------------
INSERT INTO `oauth2_registered_client`
VALUES ('04ad686e-fb84-4ff6-9cdd-ff7671e3183e', 'pkce-message-client', '2025-05-13 14:20:50', NULL, NULL, '04ad686e-fb84-4ff6-9cdd-ff7671e3183e',
        'none', 'refresh_token,authorization_code', 'http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc', '', 'message.read,message.write',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":true,\"settings.client.require-authorization-consent\":false}',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');
INSERT INTO `oauth2_registered_client`
VALUES ('3b894829-e0b5-4e51-8f6e-5c652e819352', 'device-message-client', '2025-05-13 14:20:50', NULL, NULL, '3b894829-e0b5-4e51-8f6e-5c652e819352',
        'none', 'refresh_token,urn:ietf:params:oauth:grant-type:device_code', '', '', 'message.read,message.write',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":false}',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');
INSERT INTO `oauth2_registered_client`
VALUES ('7a290668-3036-48c8-ba5c-ab1faf6e7408', 'messaging-client', '2025-05-13 14:20:50',
        '$2a$10$fTMRk5teBLP8MciFr3q97.MrBz1NUNSqGP.Hy5JFHur4XapcW70jW', NULL, '7a290668-3036-48c8-ba5c-ab1faf6e7408', 'client_secret_basic',
        'refresh_token,client_credentials,email_type,password_type,authorization_code,sms_type',
        'http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc', '', 'openid,profile,message.read,message.write',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`          bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '角色表id',
    `role_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色名称',
    `is_lock`     tinyint(1)                                             NULL DEFAULT 1 COMMENT '是否锁定',
    `permission`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标识，唯一',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role`
VALUES (1, '超级管理员', 1, 'admin', '2025-05-02 19:30:19', NULL);
INSERT INTO `role`
VALUES (4, '普通角色', 1, 'autumn', '2025-05-03 10:54:33', NULL);

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`
(
    `id`      bigint NOT NULL AUTO_INCREMENT,
    `role_id` bigint NULL DEFAULT NULL,
    `menu_id` bigint NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1919658226051534851
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu`
VALUES (1919658226026369025, 1, 100);
INSERT INTO `role_menu`
VALUES (1919658226034757633, 1, 110);
INSERT INTO `role_menu`
VALUES (1919658226034757634, 1, 111);
INSERT INTO `role_menu`
VALUES (1919658226034757635, 1, 112);
INSERT INTO `role_menu`
VALUES (1919658226034757636, 1, 113);
INSERT INTO `role_menu`
VALUES (1919658226034757637, 1, 120);
INSERT INTO `role_menu`
VALUES (1919658226043146241, 1, 121);
INSERT INTO `role_menu`
VALUES (1919658226043146242, 1, 122);
INSERT INTO `role_menu`
VALUES (1919658226043146243, 1, 123);
INSERT INTO `role_menu`
VALUES (1919658226043146244, 1, 124);
INSERT INTO `role_menu`
VALUES (1919658226043146245, 1, 130);
INSERT INTO `role_menu`
VALUES (1919658226043146246, 1, 131);
INSERT INTO `role_menu`
VALUES (1919658226043146247, 1, 132);
INSERT INTO `role_menu`
VALUES (1919658226043146248, 1, 133);
INSERT INTO `role_menu`
VALUES (1919658226043146249, 1, 114);
INSERT INTO `role_menu`
VALUES (1919658226043146250, 1, 125);
INSERT INTO `role_menu`
VALUES (1919658226043146251, 1, 115);
INSERT INTO `role_menu`
VALUES (1919658226051534850, 1, 116);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `id`      bigint NOT NULL AUTO_INCREMENT,
    `role_id` bigint NULL DEFAULT NULL,
    `user_id` bigint NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role`
VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
