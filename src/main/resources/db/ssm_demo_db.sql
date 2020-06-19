CREATE TABLE `bj_case` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '案件表主键',
  `case_code` varchar(63) DEFAULT NULL COMMENT '案号',
  `category` tinyint DEFAULT NULL COMMENT '案件类型 0-民事 1-刑事 2-行政 3-顾问 4-其他',
  `client_name` varchar(63) DEFAULT NULL COMMENT '第一个委托人姓名（冗余）',
  `client_count` int DEFAULT '1' COMMENT '委托人数量',
  `opponent_name` varchar(63) DEFAULT NULL COMMENT '第一个对方当事人姓名（冗余）',
  `opponent_count` int DEFAULT '1' COMMENT '对方当事人数量',
  `dealer` varchar(255) DEFAULT NULL COMMENT '承办人',
  `remarks` text COMMENT '备注',
  `status` tinyint DEFAULT '1' COMMENT '状态 1-未结案 2-结案',
  `create_id` int DEFAULT NULL COMMENT '创建人',
  `create_tel` varchar(63) DEFAULT NULL COMMENT '创建人电话',
  `create_name` varchar(63) DEFAULT NULL COMMENT '创建人姓名',
  `created_at` int DEFAULT NULL COMMENT '创建时间',
  `updated_at` int DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `case_code_idx` (`case_code`) USING BTREE COMMENT '案号索引',
  KEY `category_idx` (`category`) USING BTREE COMMENT '类型索引',
  KEY `create_idx` (`create_id`,`create_tel`,`create_name`) USING BTREE COMMENT '创建人索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='案件信息表';

INSERT INTO `bjcase`.`bj_case`(`id`, `case_code`, `category`, `client_name`, `client_count`, `opponent_name`, `opponent_count`, `dealer`, `remarks`, `status`, `create_id`, `create_tel`, `create_name`, `created_at`, `updated_at`, `delete_flag`) VALUES (1, 'BJM202006001', 0, '大骗子', 1, '受害者', 1, '我自己', NULL, 1, 2, '15812071547', '平民3', 1591079736, 1591079736, 0);
INSERT INTO `bjcase`.`bj_case`(`id`, `case_code`, `category`, `client_name`, `client_count`, `opponent_name`, `opponent_count`, `dealer`, `remarks`, `status`, `create_id`, `create_tel`, `create_name`, `created_at`, `updated_at`, `delete_flag`) VALUES (2, 'BJM202006002', 0, '超级骗子', 2, '受害者', 2, '正义律师', NULL, 1, 1, '000', 'admin', 1591079736, 1591079736, 0);
INSERT INTO `bjcase`.`bj_case`(`id`, `case_code`, `category`, `client_name`, `client_count`, `opponent_name`, `opponent_count`, `dealer`, `remarks`, `status`, `create_id`, `create_tel`, `create_name`, `created_at`, `updated_at`, `delete_flag`) VALUES (21, NULL, 0, '1', 2, '2', 1, '大律师', '备注', 2, 1, NULL, NULL, 0, 0, 2);
INSERT INTO `bjcase`.`bj_case`(`id`, `case_code`, `category`, `client_name`, `client_count`, `opponent_name`, `opponent_count`, `dealer`, `remarks`, `status`, `create_id`, `create_tel`, `create_name`, `created_at`, `updated_at`, `delete_flag`) VALUES (22, NULL, 2, 'lkaoda', 1, 'hahaha', 1, '大律师2', '22', 1, 1, 'admin', NULL, 0, 0, 2);
INSERT INTO `bjcase`.`bj_case`(`id`, `case_code`, `category`, `client_name`, `client_count`, `opponent_name`, `opponent_count`, `dealer`, `remarks`, `status`, `create_id`, `create_tel`, `create_name`, `created_at`, `updated_at`, `delete_flag`) VALUES (23, NULL, 4, 'dd', 1, 'ff', 1, '大律师3', 'dd', 1, 1, 'admin', '权限狗', 0, 0, 0);
INSERT INTO `bjcase`.`bj_case`(`id`, `case_code`, `category`, `client_name`, `client_count`, `opponent_name`, `opponent_count`, `dealer`, `remarks`, `status`, `create_id`, `create_tel`, `create_name`, `created_at`, `updated_at`, `delete_flag`) VALUES (24, NULL, 3, '大客户', 1, '啊啊', 1, '大律师', '备注', 2, 1, 'admin', '权限狗', 1591174084, 1591174084, 0);


CREATE TABLE `bj_client` (
  `client_id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `case_id` int DEFAULT NULL COMMENT '案件表主键',
  `client_name` varchar(63) DEFAULT NULL COMMENT '客户姓名',
  `identity` tinyint DEFAULT NULL COMMENT '客户类型0-委托人 1-对方当事人',
  `client_type` tinyint DEFAULT NULL COMMENT '身份0-原告1-被告2-第三人3-顾问单位',
  `case_code` varchar(63) DEFAULT NULL COMMENT '案号-冗余字段',
  `realname` varchar(127) DEFAULT NULL COMMENT '录入人-冗余字段',
  `status` tinyint(4) DEFAULT '0' COMMENT '是否有效0-有效1-已结案并置为无效的数据',
  `delete_flag` tinyint DEFAULT NULL COMMENT '删除标记',
  `created_at` int DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`client_id`),
  KEY `case_id_idx` (`case_id`) USING BTREE COMMENT '案件ID索引',
  KEY `client_name_idx` (`client_name`) USING BTREE COMMENT '客户姓名索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户（委托人和对方当事人）表';

CREATE TABLE `bj_menu` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '菜单主键',
  `title` varchar(63) DEFAULT NULL COMMENT '菜单标题',
  `parent_id` tinyint DEFAULT '0' COMMENT '父结点编号 0：无父节点',
  `page_url` varchar(255) DEFAULT NULL COMMENT '页面地址',
  `icon` varchar(63) DEFAULT NULL COMMENT '菜单图标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='菜单表';

INSERT INTO `bjcase`.`bj_menu`(`id`, `title`, `parent_id`, `page_url`, `icon`) VALUES (1, '系统管理', 0, '', 'icon-item');
INSERT INTO `bjcase`.`bj_menu`(`id`, `title`, `parent_id`, `page_url`, `icon`) VALUES (2, '成员列表', 1, 'userManage.jsp', 'icon-lxr');
INSERT INTO `bjcase`.`bj_menu`(`id`, `title`, `parent_id`, `page_url`, `icon`) VALUES (3, '案件管理', 0, '', 'icon-item');
INSERT INTO `bjcase`.`bj_menu`(`id`, `title`, `parent_id`, `page_url`, `icon`) VALUES (4, '案件信息', 3, 'caseManager.jsp', 'icon-lxr');
INSERT INTO `bjcase`.`bj_menu`(`id`, `title`, `parent_id`, `page_url`, `icon`) VALUES (5, '案件信息', 3, 'caseList.jsp', 'icon-lxr');


CREATE TABLE `bj_role` (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色表主键',
  `role_name` varchar(63) DEFAULT NULL COMMENT '角色名称',
  `created_at` int DEFAULT NULL COMMENT '创建时间',
  `updated_at` int DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint DEFAULT '0' COMMENT '删除标志位',
  `deleted_at` int DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='角色表';
INSERT INTO `bjcase`.`bj_role`(`role_id`, `role_name`, `created_at`, `updated_at`, `delete_flag`, `deleted_at`) VALUES (1, '管理员', 1, 1, 0, NULL);
INSERT INTO `bjcase`.`bj_role`(`role_id`, `role_name`, `created_at`, `updated_at`, `delete_flag`, `deleted_at`) VALUES (2, '普通用户', 1, 1, 0, NULL);


CREATE TABLE `bj_role_menu_rel` (
  `rel_id` int NOT NULL COMMENT '关系表ID',
  `role_id` int NOT NULL COMMENT '角色表ID',
  `menu_id` int NOT NULL COMMENT '菜单表ID',
  `delete_flag` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`rel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单节点和角色ID关联表';

INSERT INTO `bjcase`.`bj_role_menu_rel`(`rel_id`, `role_id`, `menu_id`, `delete_flag`) VALUES (1, 1, 1, 0);
INSERT INTO `bjcase`.`bj_role_menu_rel`(`rel_id`, `role_id`, `menu_id`, `delete_flag`) VALUES (2, 1, 2, 0);
INSERT INTO `bjcase`.`bj_role_menu_rel`(`rel_id`, `role_id`, `menu_id`, `delete_flag`) VALUES (3, 1, 3, 0);
INSERT INTO `bjcase`.`bj_role_menu_rel`(`rel_id`, `role_id`, `menu_id`, `delete_flag`) VALUES (4, 1, 4, 0);
INSERT INTO `bjcase`.`bj_role_menu_rel`(`rel_id`, `role_id`, `menu_id`, `delete_flag`) VALUES (5, 2, 3, 0);
INSERT INTO `bjcase`.`bj_role_menu_rel`(`rel_id`, `role_id`, `menu_id`, `delete_flag`) VALUES (6, 2, 5, 0);


CREATE TABLE `bj_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(100) NOT NULL DEFAULT '' COMMENT '登录名',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '加密后的密码字段',
  `realname` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `role_id` int DEFAULT NULL COMMENT '角色ID',
  `delete_flag` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';


INSERT INTO `bjcase`.`bj_user`(`id`, `user_name`, `password`, `salt`, `realname`, `role_id`, `delete_flag`) VALUES (1, 'admin', 'f9591e6850529ff314df7924acce6cf7', 'LGRB', '超管', 1, 0);

CREATE TABLE `bj_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_name` varchar(63) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '配置名',
  `config_value` varchar(63) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '配置值',
  `config_type` tinyint DEFAULT '1' COMMENT '配置类型 1-分类案件编号 2-月份',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置表';

INSERT INTO `bjcase`.`bj_config`(`config_id`, `config_name`, `config_value`, `config_type`) VALUES (1, 'm', 1, 1);
INSERT INTO `bjcase`.`bj_config`(`config_id`, `config_name`, `config_value`, `config_type`) VALUES (2, 'x', 1, 1);
INSERT INTO `bjcase`.`bj_config`(`config_id`, `config_name`, `config_value`, `config_type`) VALUES (3, 'xz', 1, 1);
INSERT INTO `bjcase`.`bj_config`(`config_id`, `config_name`, `config_value`, `config_type`) VALUES (4, 'g', 1, 1);
INSERT INTO `bjcase`.`bj_config`(`config_id`, `config_name`, `config_value`, `config_type`) VALUES (5, 'q', 1, 1);
INSERT INTO `bjcase`.`bj_config`(`config_id`, `config_name`, `config_value`, `config_type`) VALUES (6, 'month', 6, 2);

