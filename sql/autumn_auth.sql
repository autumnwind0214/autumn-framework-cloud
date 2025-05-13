create TABLE basic_user
(
    id                bigint primary key auto_increment comment '用户id',
    username          varchar(255) not null unique comment '用户名',
    password          varchar(255) not null comment '密码',
    nickname          varchar(255) not null comment '昵称',
    avatar            varchar(255) comment '头像',
    mobile            varchar(11) unique comment '手机号',
    email             varchar(255) unique comment '邮箱',
    create_time       datetime   default now() comment '创建时间',
    update_time       datetime comment '更新时间',
    remark            varchar(100) comment '个人简介',
    locked            tinyint(1) default 0 comment '是否锁定 0:未锁定 1:已锁定',
    enabled           tinyint(1) default 1 comment '启用状态 0:未启用 1:已启用',
    account_expire    datetime comment '账号过期时间',
    credential_expire datetime comment '凭据过期时间',
    login_time        datetime comment '上次登录时间'
);
INSERT INTO `autumn_auth`.`basic_user` (`id`, `username`, `password`, `nickname`, `avatar`, `mobile`, `email`, `create_time`, `update_time`, `remark`,
                                        `locked`, `enabled`, `account_expire`, `credential_expire`, `login_time`)
VALUES (1, 'admin', '$2a$10$jRxGm1MJRSwxuTI17yFRZuRdJ4YbsH9SkqjL1/QGAoUavMEhFUd2a', 'admin', NULL, '18607160232', '1174504109@qq.com',
        '2025-05-12 14:51:21', NULL, NULL, 0, 1, '2099-12-31 23:59:59', '2099-12-31 23:59:59', NULL);


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

 Date: 12/05/2025 17:49:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authorization
-- ----------------------------
DROP TABLE IF EXISTS `authorization`;
CREATE TABLE `authorization`
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
-- Records of authorization
-- ----------------------------
INSERT INTO `authorization`
VALUES ('22d55f3e-dda9-4e21-b5d8-6ffa6463dc73', '3', 'messaging-client', 'sms_type', '', '{\"@class\":\"java.util.Collections$UnmodifiableMap\"}',
        NULL, NULL, NULL, NULL, NULL,
        'eyJraWQiOiI4MWY5MjdkNC1iNjM1LTQ2MmYtYWUwZi00YTQxYzIxZjRjZWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJuaWNrbmFtZVwiOlwi566h55CG5ZGYXCIsXCJhdmF0YXJcIjpcIlwifSIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJuYmYiOjE3NDY3NzQyNjksInNjb3BlIjpbIm1lc3NhZ2Uud3JpdGUiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo5MDEwIiwiZXhwIjoxNzQ2Nzc3ODY5LCJpYXQiOjE3NDY3NzQyNjksImp0aSI6ImYzMjQyNDNiLTYyMTEtNDNiZS05ODY5LWM2OWY0NGFiNmQ3MiIsInVuaXF1ZUlkIjoie1wiaWRcIjoxLFwibmlja25hbWVcIjpcIueuoeeQhuWRmFwiLFwiYXZhdGFyXCI6XCJcIn0iLCJhdXRob3JpdGllcyI6WyJzeXN0ZW06cm9sZTpkZWxldGUiLCJzeXN0ZW06dXNlcjpjaGFuZ2VQYXNzd29yZCIsInN5c3RlbTptZW51OmFkZCIsInN5c3RlbTp1c2VyOmRlbGV0ZSIsInN5c3RlbTpyb2xlOmF1dGgiLCJzeXN0ZW06dXNlcjplZGl0Iiwic3lzdGVtOnJvbGU6aXNMb2NrIiwic3lzdGVtOnJvbGU6ZWRpdCIsInN5c3RlbTp1c2VyOmFkZCIsInN5c3RlbTptZW51OmRlbGV0ZSIsInN5c3RlbTp1c2VyOmFzc2lnblJvbGUiLCJzeXN0ZW06cm9sZTphZGQiLCJzeXN0ZW06dXNlcjpzdGF0dXMiLCJzeXN0ZW06bWVudTplZGl0Il19.XDS0iMglSqui368-JgKRi4C8sqw-KF14z9DzFe7XbztPHb8IYFga_tucb-PBH3PNLsiyZzKmUXtjOVOwI32SCzuQMPj8EI275zOsyc0S0iW7ndfdIcJTxDux7u1USiQXImnrU0lgL9ZR0mFPLUEyngq-Z-ZysZom5vFX_YECLxDqmxxAbWCdZiiMQW27EoeItWfRz0uCL6VqHLTLwrUQGIblnCeU2tV-VqfoKzYD6LsPoskiTgiXZWu_1T9A1dH13AofdVc4cle2jKdcqt3jQFrFq9vIpF1GzgtPtpWU83GgxVvElR2QRZlQuvKlArvNxNpRnLQsemJZJmNOdu273g',
        '2025-05-09 15:04:29', '2025-05-09 16:04:29',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.claims\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"sub\":\"{\\\"id\\\":1,\\\"nickname\\\":\\\"管理员\\\",\\\"avatar\\\":\\\"\\\"}\",\"aud\":[\"java.util.Collections$SingletonList\",[\"messaging-client\"]],\"nbf\":[\"java.time.Instant\",1746774269.305955700],\"scope\":[\"java.util.LinkedHashSet\",[\"message.write\"]],\"iss\":[\"java.net.URL\",\"http://127.0.0.1:9010\"],\"exp\":[\"java.time.Instant\",1746777869.305955700],\"iat\":[\"java.time.Instant\",1746774269.305955700],\"jti\":\"f324243b-6211-43be-9869-c69f44ab6d72\",\"uniqueId\":\"{\\\"id\\\":1,\\\"nickname\\\":\\\"管理员\\\",\\\"avatar\\\":\\\"\\\"}\",\"authorities\":[\"java.util.HashSet\",[\"system:role:delete\",\"system:user:changePassword\",\"system:menu:add\",\"system:user:delete\",\"system:role:auth\",\"system:user:edit\",\"system:role:isLock\",\"system:role:edit\",\"system:user:add\",\"system:menu:delete\",\"system:user:assignRole\",\"system:role:add\",\"system:user:status\",\"system:menu:edit\"]]},\"metadata.token.invalidated\":false}',
        NULL, 'message.write',
        'b41zlaWJvtmq4PUTggliEFk3BXNrfz0AUZEZiwOGHPMW2nyD8nPo56yIh_dljLcvXanYOiGrEiI2_UrDF69nJIQzsMxxKOsZKJwRgSmhLOi4uFwOJWxGtMeiX-BQF6sC',
        '2025-05-09 15:04:29', '2025-05-17 01:04:29', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":false}',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `authorization`
VALUES ('ac3a6206-10b3-49ab-b9b6-120b6065bbb4', '3', 'messaging-client', 'sms_type', '', '{\"@class\":\"java.util.Collections$UnmodifiableMap\"}',
        NULL, NULL, NULL, NULL, NULL,
        'eyJraWQiOiI4MWY5MjdkNC1iNjM1LTQ2MmYtYWUwZi00YTQxYzIxZjRjZWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJuaWNrbmFtZVwiOlwi566h55CG5ZGYXCIsXCJhdmF0YXJcIjpcIlwifSIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJuYmYiOjE3NDY3NzQxNjksInNjb3BlIjpbIm1lc3NhZ2Uud3JpdGUiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo5MDEwIiwiZXhwIjoxNzQ2Nzc3NzY5LCJpYXQiOjE3NDY3NzQxNjksImp0aSI6IjVmOWQ2ODE3LWQ2MTEtNDY1Yy05NDI1LTMyMjE0NWZjYmNlOSIsInVuaXF1ZUlkIjoie1wiaWRcIjoxLFwibmlja25hbWVcIjpcIueuoeeQhuWRmFwiLFwiYXZhdGFyXCI6XCJcIn0iLCJhdXRob3JpdGllcyI6WyJzeXN0ZW06cm9sZTpkZWxldGUiLCJzeXN0ZW06dXNlcjpjaGFuZ2VQYXNzd29yZCIsInN5c3RlbTptZW51OmFkZCIsInN5c3RlbTp1c2VyOmRlbGV0ZSIsInN5c3RlbTpyb2xlOmF1dGgiLCJzeXN0ZW06dXNlcjplZGl0Iiwic3lzdGVtOnJvbGU6aXNMb2NrIiwic3lzdGVtOnJvbGU6ZWRpdCIsInN5c3RlbTp1c2VyOmFkZCIsInN5c3RlbTptZW51OmRlbGV0ZSIsInN5c3RlbTp1c2VyOmFzc2lnblJvbGUiLCJzeXN0ZW06cm9sZTphZGQiLCJzeXN0ZW06dXNlcjpzdGF0dXMiLCJzeXN0ZW06bWVudTplZGl0Il19.KgN1tYg_fCTRMwjgm0mvEP-FlVNTBfqwpsR1Owq-N0p8xZjbSaxPzj2O4S5OpYtiEfkyprlstIyUFOEnzShY1q9pEBlunlfzq5fnhklUDVs_pRnoMxDv0qh1A_yqycca2xC7xjHp7Cq5Owg3zgdDrn9XAbLVKUY9hX1aAnp7mavdNzRYlQXewJN8LLKnISt3DPb-QYkzBwNTm_FSvMLPXoDJghqXE_L37pYTCyabAU-hdmHzHMgVd7y_ec6DYCXQT8psvwMgqqR6zgrnlfx1tHuZMfUksIxUy0xBelcrOqUx3_CkTp4bkKKkmbsOIf5E6A6CDYns6qwS1xV4Nmmhgg',
        '2025-05-09 15:02:50', '2025-05-09 16:02:50',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.claims\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"sub\":\"{\\\"id\\\":1,\\\"nickname\\\":\\\"管理员\\\",\\\"avatar\\\":\\\"\\\"}\",\"aud\":[\"java.util.Collections$SingletonList\",[\"messaging-client\"]],\"nbf\":[\"java.time.Instant\",1746774169.529696800],\"scope\":[\"java.util.LinkedHashSet\",[\"message.write\"]],\"iss\":[\"java.net.URL\",\"http://127.0.0.1:9010\"],\"exp\":[\"java.time.Instant\",1746777769.529696800],\"iat\":[\"java.time.Instant\",1746774169.529696800],\"jti\":\"5f9d6817-d611-465c-9425-322145fcbce9\",\"uniqueId\":\"{\\\"id\\\":1,\\\"nickname\\\":\\\"管理员\\\",\\\"avatar\\\":\\\"\\\"}\",\"authorities\":[\"java.util.HashSet\",[\"system:role:delete\",\"system:user:changePassword\",\"system:menu:add\",\"system:user:delete\",\"system:role:auth\",\"system:user:edit\",\"system:role:isLock\",\"system:role:edit\",\"system:user:add\",\"system:menu:delete\",\"system:user:assignRole\",\"system:role:add\",\"system:user:status\",\"system:menu:edit\"]]},\"metadata.token.invalidated\":false}',
        NULL, 'message.write',
        '9fjLaL0u0JdsVAfecQF8M4uePKND-DzcZMTV5e3GcBc5nyIpMp3ewIhk7WntwVO7BiGHEA2kypBsUKZB-FhFutFPs3xEvcuWFuQTJHml6WL9jJwWF1cXJpx5ikrnGCK9',
        '2025-05-09 15:02:50', '2025-05-17 01:02:50', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":false}',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `authorization`
VALUES ('fe514091-b68c-4c35-8e01-b014c5ffa196', '3', 'messaging-client', 'sms_code', '', '{\"@class\":\"java.util.Collections$UnmodifiableMap\"}',
        NULL, NULL, NULL, NULL, NULL,
        'eyJraWQiOiI4MWY5MjdkNC1iNjM1LTQ2MmYtYWUwZi00YTQxYzIxZjRjZWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJuaWNrbmFtZVwiOlwi566h55CG5ZGYXCIsXCJhdmF0YXJcIjpcIlwifSIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJuYmYiOjE3NDY3NTc2OTgsInNjb3BlIjpbIm1lc3NhZ2Uud3JpdGUiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo5MDEwIiwiZXhwIjoxNzQ2NzYxMjk4LCJpYXQiOjE3NDY3NTc2OTgsImp0aSI6ImM2ZDczZDJiLTQyZmEtNGY2Zi05MmY0LWY4M2NlODEwZDA0NCIsInVuaXF1ZUlkIjoie1wiaWRcIjoxLFwibmlja25hbWVcIjpcIueuoeeQhuWRmFwiLFwiYXZhdGFyXCI6XCJcIn0iLCJhdXRob3JpdGllcyI6WyJzeXN0ZW06cm9sZTpkZWxldGUiLCJzeXN0ZW06dXNlcjpjaGFuZ2VQYXNzd29yZCIsInN5c3RlbTptZW51OmFkZCIsInN5c3RlbTp1c2VyOmRlbGV0ZSIsInN5c3RlbTpyb2xlOmF1dGgiLCJzeXN0ZW06dXNlcjplZGl0Iiwic3lzdGVtOnJvbGU6aXNMb2NrIiwic3lzdGVtOnJvbGU6ZWRpdCIsInN5c3RlbTp1c2VyOmFkZCIsInN5c3RlbTptZW51OmRlbGV0ZSIsInN5c3RlbTp1c2VyOmFzc2lnblJvbGUiLCJzeXN0ZW06cm9sZTphZGQiLCJzeXN0ZW06dXNlcjpzdGF0dXMiLCJzeXN0ZW06bWVudTplZGl0Il19.JcTLl882yFw15Dp11dawgO_YJjxO7F4DqdKnR3um_tpxNy8shsOJ5iAn6y4mT9twD2V9poNcj09P8d1fElWNAPwcrTKKMD6o54g3OJRz0D_cAmUn2j3f9-QUO9PnOwffLc3ljgQYFiDtOak1D4UgcuJfwvtA78BAbH70640Rlz31uW3CLyH4xMrCvXWTMN9hNbF3GcrIWipJxCoT92L0B_cPeIQrv_1qc7NvXtHZ7CEd0t9i1XTeJySAnBQopKlA5d-0sG9diVbzVhHDUCKyHgZ0kWEFDXXSLV6j04cI1_d3kdkUQVclBqdOwV3Wb5joXx_x1GhFtb1bvD6ytO8c_A',
        '2025-05-09 10:28:18', '2025-05-09 11:28:18',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.claims\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"sub\":\"{\\\"id\\\":1,\\\"nickname\\\":\\\"管理员\\\",\\\"avatar\\\":\\\"\\\"}\",\"aud\":[\"java.util.Collections$SingletonList\",[\"messaging-client\"]],\"nbf\":[\"java.time.Instant\",1746757698.163977400],\"scope\":[\"java.util.LinkedHashSet\",[\"message.write\"]],\"iss\":[\"java.net.URL\",\"http://127.0.0.1:9010\"],\"exp\":[\"java.time.Instant\",1746761298.163977400],\"iat\":[\"java.time.Instant\",1746757698.163977400],\"jti\":\"c6d73d2b-42fa-4f6f-92f4-f83ce810d044\",\"uniqueId\":\"{\\\"id\\\":1,\\\"nickname\\\":\\\"管理员\\\",\\\"avatar\\\":\\\"\\\"}\",\"authorities\":[\"java.util.HashSet\",[\"system:role:delete\",\"system:user:changePassword\",\"system:menu:add\",\"system:user:delete\",\"system:role:auth\",\"system:user:edit\",\"system:role:isLock\",\"system:role:edit\",\"system:user:add\",\"system:menu:delete\",\"system:user:assignRole\",\"system:role:add\",\"system:user:status\",\"system:menu:edit\"]]},\"metadata.token.invalidated\":false}',
        NULL, 'message.write',
        'sCG5126CgkiqhAmij9vn_a6edDN6DE8Osvv8V97t41t4p4QfXP7vkUond0QzaPkqlsW54VXUvhlucCIE9BiEb81PvvH8uMz2dGwJrtqcjFywIQ7CUhd1jk5Rmj0qu5xT',
        '2025-05-09 10:28:18', '2025-05-16 20:28:18', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":false}',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for authorization_client
-- ----------------------------
DROP TABLE IF EXISTS `authorization_client`;
CREATE TABLE `authorization_client`
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
-- Records of authorization_client
-- ----------------------------
INSERT INTO `authorization_client`
VALUES ('1', 'oidc-client', '2024-12-21 22:24:20', '$2a$10$.J0Rfg7y2Mu8AN8Dk2vL.eBFa9NGbOYCPOAFEw.QhgGLVXjO7eFDC', '2024-12-31 23:28:50', '测试',
        'client_secret_basic', 'refresh_token,client_credentials,authorization_code',
        'http://spring-oauth-client:8000/login/oauth2/code/messaging-client-oidc,http://spring-oauth-client:9001/login/oauth2/code/messaging-client-oidc,http://spring-oauth-client:9001/authorized,http://spring-oauth-client:8000/authorized',
        '', 'openid,profile,message.read,message.write',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.x509-certificate-bound-access-tokens\":false,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');
INSERT INTO `authorization_client`
VALUES ('2', 'device-message-client', '2024-12-31 20:03:02', NULL, NULL, '设备授权码', 'none',
        'refresh_token,urn:ietf:params:oauth:grant-type:device_code', NULL, NULL, 'message.read,message.write',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":false}',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');
INSERT INTO `authorization_client`
VALUES ('3', 'messaging-client', '2024-12-25 09:38:10', '$2a$10$uqTcvKEWwGijdEjDDBe4Rex2rLy2E5WsJSr4/Hz4ezVfxmG/tWsB.', NULL,
        'c08d388c-91fd-4853-8fb5-47a86b5921fc', 'client_secret_basic',
        'refresh_token,client_credentials,authorization_code,sms_type,email_type,password_type',
        'http://127.0.0.1:9999/codeRedirect,http://127.0.0.1:5173/PkceRedirect,http://127.0.0.1:9000/login/oauth2/code/message-client', '',
        'openid,profile,message.read,message.write',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":false}',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":false,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",640800.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');
INSERT INTO `authorization_client`
VALUES ('923172a4-5cf3-4786-accb-738d8871d980', 'pkce-message-client', '2023-06-05 05:57:44', NULL, NULL, '8cf18281-d5ca-4fe8-8bfb-25e485cdc23d',
        'none', 'refresh_token,authorization_code', 'http://127.0.0.1:9999/codeRedirect,http://127.0.0.1:5173/PkceRedirect', '',
        'message.read,message.write',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":true,\"settings.client.require-authorization-consent\":false}',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');

-- ----------------------------
-- Table structure for authorization_consent
-- ----------------------------
DROP TABLE IF EXISTS `authorization_consent`;
CREATE TABLE `authorization_consent`
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
-- Records of authorization_consent
-- ----------------------------
INSERT INTO `authorization_consent`
VALUES ('1', 'admin', 'SCOPE_openid,SCOPE_profile');
INSERT INTO `authorization_consent`
VALUES ('1',
        '{\"createTime\":\"2024-11-24 22:55:50\",\"flag\":0,\"id\":1,\"nickname\":\"秋风\",\"password\":\"\",\"phone\":\"18607150232\",\"salt\":\"8cf59a89cdfb4c8d855b84ddf66cb2bc\",\"username\":\"admin\"}',
        'SCOPE_openid,SCOPE_profile');
INSERT INTO `authorization_consent`
VALUES ('2',
        '{\"createTime\":\"2024-11-24 22:55:50\",\"flag\":0,\"id\":1,\"nickname\":\"秋风\",\"password\":\"\",\"phone\":\"18607150232\",\"salt\":\"8cf59a89cdfb4c8d855b84ddf66cb2bc\",\"username\":\"admin\"}',
        'SCOPE_message.read');
INSERT INTO `authorization_consent`
VALUES ('3', '1', 'SCOPE_message.read');
INSERT INTO `authorization_consent`
VALUES ('3', 'admin', 'SCOPE_openid,SCOPE_message.read,SCOPE_message.write,SCOPE_profile');
INSERT INTO `authorization_consent`
VALUES ('3',
        '{\"email\":\"1174504109@qq.com\",\"flag\":0,\"id\":1,\"nickname\":\"秋风\",\"password\":\"\",\"phone\":\"18607160232\",\"username\":\"admin\"}',
        'SCOPE_openid,SCOPE_profile');

SET FOREIGN_KEY_CHECKS = 1;
