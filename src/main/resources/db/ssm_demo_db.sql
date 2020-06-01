CREATE TABLE `bj_case` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '案件表主键',
  `case_code` varchar(63) DEFAULT NULL COMMENT '案号',
  `category` tinyint DEFAULT NULL COMMENT '案件类型 0-民事 1-刑事 2-行政 3-顾问 4-其他',
  `dealer` varchar(255) DEFAULT NULL COMMENT '承办人',
  `remarks` text COMMENT '备注',
  `status` tinyint DEFAULT NULL COMMENT '状态 1-未结案 2-结案',
  `create_id` int DEFAULT NULL COMMENT '创建人',
  `created_at` int DEFAULT NULL COMMENT '创建时间',
  `updated_at` int DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `case_code_idx` (`case_code`) USING BTREE COMMENT '案号索引',
  KEY `category_idx` (`category`) USING BTREE COMMENT '类型索引',
  KEY `create_id_idx` (`id`) USING BTREE COMMENT '创建人索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='案件信息表';

CREATE TABLE `bj_client` (
  `client_id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `case_id` int DEFAULT NULL COMMENT '案件表主键',
  `client_name` varchar(63) DEFAULT NULL COMMENT '客户姓名',
  `identity` tinyint DEFAULT NULL COMMENT '客户类型0-委托人 1-对方当事人',
  `client_type` tinyint DEFAULT NULL COMMENT '身份0-原告1-被告2-第三人3-顾问单位',
  `case_code` varchar(63) DEFAULT NULL COMMENT '案号-冗余字段',
  `realname` varchar(127) DEFAULT NULL COMMENT '录入人-冗余字段',
  `status` tinyint DEFAULT NULL COMMENT '是否有效0-有效1-已结案并置为无效的数据',
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


CREATE TABLE `bj_role` (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色表主键',
  `role_name` varchar(63) DEFAULT NULL COMMENT '角色名称',
  `created_at` int DEFAULT NULL COMMENT '创建时间',
  `updated_at` int DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint DEFAULT '0' COMMENT '删除标志位',
  `deleted_at` int DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='角色表';
INSERT INTO `bjcase`.`bj_role`(`role_id`, `role_name`, `created_at`, `updated_at`, `delete_flag`, `deleted_at`) VALUES (1, '超级管理员', 1, 1, 0, NULL);
INSERT INTO `bjcase`.`bj_role`(`role_id`, `role_name`, `created_at`, `updated_at`, `delete_flag`, `deleted_at`) VALUES (2, '普通用户', 1, 1, 0, NULL);


CREATE TABLE `bj_role_menu_rel` (
  `rel_id` int NOT NULL COMMENT '关系表ID',
  `role_id` int NOT NULL COMMENT '角色表ID',
  `menu_id` int NOT NULL COMMENT '菜单表ID',
  `delete_flag` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`rel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单节点和角色ID关联表';

CREATE TABLE `bj_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(100) NOT NULL DEFAULT '' COMMENT '登录名',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '加密后的密码字段',
  `realname` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `role_id` int DEFAULT NULL COMMENT '角色ID',
  `delete_flag` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

INSERT INTO `bjcase`.`bj_user`(`id`, `user_name`, `password`, `realname`, `role_id`, `delete_flag`) VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '权限狗', 1, 0);
INSERT INTO `bjcase`.`bj_user`(`id`, `user_name`, `password`, `realname`, `role_id`, `delete_flag`) VALUES (2, '15812071547', 'e10adc3949ba59abbe56e057f20f883e', '平民1', 2, 0);
INSERT INTO `bjcase`.`bj_user`(`id`, `user_name`, `password`, `realname`, `role_id`, `delete_flag`) VALUES (3, '15877948460', 'c33367701511b4f6020ec61ded352059', '平民2', 2, 0);
