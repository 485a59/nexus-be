-- ----------------------------
-- 数据库表结构生成时间：Sat Mar 08 11:01:24 CST 2025
-- ----------------------------


/*========= 音乐信息表 ==========*/
DROP TABLE IF EXISTS `music`;
CREATE TABLE `music` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '音乐编号',
    `file_id` BIGINT NOT NULL DEFAULT 0 COMMENT '文件 ID',
    `track` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '音轨',
    `artist` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '艺术家',
    `title` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '标题',
    `album` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '专辑',
    `year` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '年份',
    `genre` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '流派',
    `comment` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '评论',
    `lyrics` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '歌词',
    `composer` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '作曲家',
    `publisher` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '发布者',
    `original_artist` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '原唱艺术家',
    `album_artist` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '专辑艺术家',
    `copyright` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '版权',
    `url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'URL',
    `encoder` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '编码器',
    `album_image` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '专辑图片',
    `track_length` FLOAT NOT NULL DEFAULT 0.00 COMMENT '音轨长度',
    PRIMARY KEY (`id`),
    KEY `idx_file_id` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='音乐信息表';

/*========= 存储信息表 ==========*/
DROP TABLE IF EXISTS `storage`;
CREATE TABLE `storage` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '存储编号',
    `user_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户 ID',
    `size` BIGINT NOT NULL DEFAULT 0 COMMENT '占用存储大小',
    `total_size` BIGINT NOT NULL DEFAULT 0 COMMENT '总存储大小',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='存储信息表';

/*=========  ==========*/
DROP TABLE IF EXISTS `storage_builder`;
CREATE TABLE `storage_builder` (
    `id` BIGINT NOT NULL DEFAULT 0,
    `user_id` VARCHAR(255) NOT NULL DEFAULT '',
    `storage_size` BIGINT NOT NULL DEFAULT 0,
    `total_storage_size` BIGINT NOT NULL DEFAULT 0,
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*========= 文件权限信息表 ==========*/
DROP TABLE IF EXISTS `file_permission`;
CREATE TABLE `file_permission` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '文件权限 ID',
    `common_file_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '共享文件 ID',
    `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `code` INT NOT NULL DEFAULT 0 COMMENT '用户对文件的权限码',
    PRIMARY KEY (`id`),
    KEY `idx_common_file_id` (`common_file_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件权限信息表';

/*========= 文件类型表 ==========*/
DROP TABLE IF EXISTS `file_type`;
CREATE TABLE `file_type` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` INT AUTO_INCREMENT NOT NULL COMMENT '文件类型编号',
    `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件类型名',
    `order` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '次序',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件类型表';

/*========= 用户信息表 ==========*/
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` VARCHAR(255) NOT NULL COMMENT '用户ID',
    `dept_id` INT NOT NULL DEFAULT 0 COMMENT '部门ID',
    `username` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户账号',
    `nickname` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户昵称',
    `email` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户邮箱',
    `phone_number` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '手机号码',
    `sex` INT NOT NULL DEFAULT 0 COMMENT '用户性别（0男 1女 2未知）',
    `avatar` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '头像地址',
    `password` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '密码',
    `status` INT NOT NULL DEFAULT 0 COMMENT '帐号状态（1 正常 2 停用 3 冻结）',
    `login_ip` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '最后登录IP',
    `login_time` DATETIME(3) NOT NULL COMMENT '最后登录时间',
    `remark` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

/*========= 上传任务详细信息表 ==========*/
DROP TABLE IF EXISTS `upload_task_detail`;
CREATE TABLE `upload_task_detail` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '上传任务详细信息 ID',
    `file_path` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件路径',
    `file_name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件名称',
    `chunk_number` TEXT NOT NULL COMMENT '当前分片数',
    `chunk_size` INT NOT NULL DEFAULT 0 COMMENT '当前分片大小',
    `relative_path` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件相对路径',
    `total_chunks` INT NOT NULL DEFAULT 0 COMMENT '文件总分片数',
    `total_size` INT NOT NULL DEFAULT 0 COMMENT '文件总大小',
    `identifier` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件 MD5 唯一标识',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='上传任务详细信息表';

/*========= 回收站文件 ==========*/
DROP TABLE IF EXISTS `recovery_file`;
CREATE TABLE `recovery_file` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '回收站文件编号',
    `user_file_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户文件 ID',
    `delete_batch_num` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '删除批次号',
    PRIMARY KEY (`id`),
    KEY `idx_user_file_id` (`user_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='回收站文件';

/*========= 上传任务信息表 ==========*/
DROP TABLE IF EXISTS `upload_task`;
CREATE TABLE `upload_task` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '上传任务 ID',
    `user_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户 ID',
    `file_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件 ID',
    `identifier` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'MD5 唯一标识',
    `file_name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件名称',
    `file_path` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件路径',
    `extension` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '扩展名',
    `upload_time` DATETIME(3) NOT NULL COMMENT '上传时间',
    `upload_status` INT NOT NULL DEFAULT 0 COMMENT '上传状态 (1 表示成功，0 表示失败或未完成)',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_file_id` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='上传任务信息表';

/*========= 图像表 ==========*/
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '图像编号',
    `file_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件编号',
    `width` INT NOT NULL DEFAULT 0 COMMENT '图片的宽度',
    `height` INT NOT NULL DEFAULT 0 COMMENT '图片的高度',
    PRIMARY KEY (`id`),
    KEY `idx_file_id` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图像表';

/*========= 文件信息表 ==========*/
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` VARCHAR(255) NOT NULL COMMENT '文件 ID',
    `url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件 URL',
    `size` BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小',
    `status` INT NOT NULL DEFAULT 0 COMMENT '文件状态 (0-失效，1-生效)',
    `storage_type` INT NOT NULL DEFAULT 0 COMMENT '存储类型',
    `identifier` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'MD5 唯一标识',
    `create_user_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '创建用户 ID',
    `modify_user_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '修改用户 ID',
    PRIMARY KEY (`id`),
    KEY `idx_create_user_id` (`create_user_id`),
    KEY `idx_modify_user_id` (`modify_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件信息表';

/*========= 部门表 ==========*/
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` INT AUTO_INCREMENT NOT NULL COMMENT '部门编号',
    `parent_id` INT NOT NULL DEFAULT 0 COMMENT '父部门id',
    `ancestors` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '祖级列表',
    `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '部门名称',
    `order_num` INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `phone` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '联系电话',
    `email` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '邮箱',
    `status` INT NOT NULL DEFAULT 0 COMMENT '部门状态（0正常 1停用）',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

/*========= 系统参数表 ==========*/
DROP TABLE IF EXISTS `sys_param`;
CREATE TABLE `sys_param` (
    `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '参数编号',
    `group_name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '组名',
    `key` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '键名',
    `value` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '值',
    `description` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '描述',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统参数表';

/*========= 章节信息表 ==========*/
DROP TABLE IF EXISTS `chapter`;
CREATE TABLE `chapter` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` INT AUTO_INCREMENT NOT NULL COMMENT '章节ID',
    `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '章节名称',
    `parent_id` INT NOT NULL DEFAULT 0 COMMENT '父章节号',
    `order_num` INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    `description` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '描述',
    `status` INT NOT NULL DEFAULT 0 COMMENT '状态(0 停用,1 启用)',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节信息表';

/*========= 文件扩展名信息表 ==========*/
DROP TABLE IF EXISTS `file_extension`;
CREATE TABLE `file_extension` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` VARCHAR(255) NOT NULL COMMENT '文件拓展名 ID',
    `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件扩展名',
    `desc` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件扩展名描述',
    `img_url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件扩展名预览图 URL',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件扩展名信息表';

/*========= 分享信息表 ==========*/
DROP TABLE IF EXISTS `share`;
CREATE TABLE `share` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` VARCHAR(255) NOT NULL COMMENT '分享编号',
    `user_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户 ID',
    `share_time` DATETIME(3) NOT NULL COMMENT '分享时间',
    `end_time` DATETIME(3) NOT NULL COMMENT '失效时间',
    `extraction_code` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '提取码',
    `share_batch_num` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '分享批次号',
    `share_type` INT NOT NULL DEFAULT 0 COMMENT '分享类型 (0 公共, 1 私密, 2 好友)',
    `share_status` INT NOT NULL DEFAULT 0 COMMENT '分享状态 (0 正常, 1 已失效, 2 已撤销)',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分享信息表';

/*========= 文件分类信息表 ==========*/
DROP TABLE IF EXISTS `file_category`;
CREATE TABLE `file_category` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '文件分类 ID',
    `type_id` INT NOT NULL DEFAULT 0 COMMENT '文件类型 ID',
    `extension` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件扩展名',
    PRIMARY KEY (`id`),
    KEY `idx_type_id` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件分类信息表';

/*========= 通用文件信息表 ==========*/
DROP TABLE IF EXISTS `common_file`;
CREATE TABLE `common_file` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` VARCHAR(255) NOT NULL COMMENT '通用文件 ID',
    `user_file_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户文件 ID',
    PRIMARY KEY (`id`),
    KEY `idx_user_file_id` (`user_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通用文件信息表';

/*========= 课程资源信息表 ==========*/
DROP TABLE IF EXISTS `course_resource`;
CREATE TABLE `course_resource` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` VARCHAR(255) NOT NULL COMMENT '资源ID',
    `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '资源名称',
    `chapter_id` INT NOT NULL DEFAULT 0 COMMENT '章节ID',
    `file_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户文件ID',
    `type` INT NOT NULL DEFAULT 0 COMMENT '资源类型(1-教材,2-视频,3-软件,4-课件)',
    `meta` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '元数据(JSON格式)',
    `description` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '描述',
    PRIMARY KEY (`id`),
    KEY `idx_chapter_id` (`chapter_id`),
    KEY `idx_file_id` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程资源信息表';

/*========= 文章表 ==========*/
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '文章ID',
    `title` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文章标题',
    `date` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '发布日期',
    `url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文章链接',
    `description` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文章描述',
    `source` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文章来源',
    `authors` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '作者列表',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

/*========= 用户文件信息表 ==========*/
DROP TABLE IF EXISTS `user_file`;
CREATE TABLE `user_file` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` VARCHAR(255) NOT NULL COMMENT '用户文件 ID',
    `user_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户 ID',
    `file_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件 ID',
    `file_name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件名',
    `file_path` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件路径',
    `extension` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '扩展名',
    `is_dir` INT NOT NULL DEFAULT 0 COMMENT '是否是目录 (0-否, 1-是)',
    `upload_time` DATETIME(3) NOT NULL COMMENT '上传时间',
    `delete_batch_num` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '删除批次号',
    `create_user_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '创建用户 ID',
    `modify_user_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '修改用户 ID',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_file_id` (`file_id`),
    KEY `idx_create_user_id` (`create_user_id`),
    KEY `idx_modify_user_id` (`modify_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户文件信息表';

/*========= 分享文件信息表 ==========*/
DROP TABLE IF EXISTS `share_file`;
CREATE TABLE `share_file` (
    `id` VARCHAR(255) NOT NULL COMMENT '分享文件编号',
    `share_batch_num` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '分享批次号',
    `user_file_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户文件 ID',
    `share_file_path` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '分享文件路径',
    PRIMARY KEY (`id`),
    KEY `idx_user_file_id` (`user_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分享文件信息表';

/*========= 图片文件信息表 ==========*/
DROP TABLE IF EXISTS `picture_file`;
CREATE TABLE `picture_file` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `picture_file_id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '图片文件编号',
    `url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '图片文件 URL',
    `size` BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小',
    `storage_type` INT NOT NULL DEFAULT 0 COMMENT '存储类型',
    `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `fileName` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件名称',
    `extension` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '扩展名',
    PRIMARY KEY (`picture_file_id`),
    KEY `idx_picture_file_id` (`picture_file_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片文件信息表';

/*========= 角色信息表 ==========*/
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` INT AUTO_INCREMENT NOT NULL COMMENT '角色编号',
    `role_name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '角色名称',
    `role_key` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '角色标识',
    `remark` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '备注',
    `status` INT NOT NULL DEFAULT 0 COMMENT '角色状态（0：禁用，1：启用）',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

/*=========  ==========*/
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `delete_time` DATETIME(3) NULL DEFAULT NULL COMMENT '删除时间',
    `id` VARCHAR(255) NOT NULL COMMENT '用户ID',
    `user_id` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户编号',
    `role_id` INT NOT NULL DEFAULT 0 COMMENT '角色编号',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
