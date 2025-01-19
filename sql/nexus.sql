
/*========= 音乐信息表 ==========*/
DROP TABLE IF EXISTS `music`; 
CREATE TABLE `music` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` BIGINT NOT NULL COMMENT '音乐编号' primary key,
`file_id` BIGINT DEFAULT 0 NOT NULL COMMENT '文件 ID',
`track` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '音轨',
`artist` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '艺术家',
`title` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '标题',
`album` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '专辑',
`year` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '年份',
`genre` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '流派',
`comment` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '评论',
`lyrics` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '歌词',
`composer` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '作曲家',
`publisher` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '发布者',
`original_artist` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '原唱艺术家',
`album_artist` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '专辑艺术家',
`copyright` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '版权',
`url` VARCHAR(100) DEFAULT '' NOT NULL COMMENT 'URL',
`encoder` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '编码器',
`album_image` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '专辑图片',
`track_length` FLOAT DEFAULT 0 NOT NULL COMMENT '音轨长度'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='音乐信息表';

/*========= 存储信息表 ==========*/
DROP TABLE IF EXISTS `storage`; 
CREATE TABLE `storage` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` BIGINT NOT NULL COMMENT '存储编号' primary key,
`user_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户 ID',
`size` BIGINT DEFAULT 0 NOT NULL COMMENT '占用存储大小',
`total_size` BIGINT DEFAULT 0 NOT NULL COMMENT '总存储大小'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='存储信息表';

/*========= 文件权限信息表 ==========*/
DROP TABLE IF EXISTS `file_permission`; 
CREATE TABLE `file_permission` ( 
`id` BIGINT NOT NULL COMMENT '文件权限 ID' primary key,
`file_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '共享文件 ID',
`user_id` BIGINT DEFAULT 0 NOT NULL COMMENT '用户 ID',
`code` INT DEFAULT 0 NOT NULL COMMENT '用户对文件的权限码'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件权限信息表';

/*========= 文件类型表 ==========*/
DROP TABLE IF EXISTS `file_type`; 
CREATE TABLE `file_type` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` INT NOT NULL COMMENT '文件类型编号' primary key,
`name` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件类型名',
`order` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '次序'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件类型表';

/*========= 上传任务详细信息表 ==========*/
DROP TABLE IF EXISTS `upload_task_detail`; 
CREATE TABLE `upload_task_detail` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` BIGINT NOT NULL COMMENT '上传任务详细信息 ID' primary key,
`file_path` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件路径',
`file_name` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件名称',
`chunk_number` TEXT NOT NULL COMMENT '当前分片数',
`chunk_size` INT DEFAULT 0 NOT NULL COMMENT '当前分片大小',
`relative_path` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件相对路径',
`total_chunks` INT DEFAULT 0 NOT NULL COMMENT '文件总分片数',
`total_size` INT DEFAULT 0 NOT NULL COMMENT '文件总大小',
`identifier` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件 MD5 唯一标识'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='上传任务详细信息表';

/*========= 上传任务信息表 ==========*/
DROP TABLE IF EXISTS `upload_task`; 
CREATE TABLE `upload_task` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` BIGINT NOT NULL COMMENT '上传任务 ID' primary key,
`user_id` BIGINT DEFAULT 0 NOT NULL COMMENT '用户 ID',
`identifier` VARCHAR(100) DEFAULT '' NOT NULL COMMENT 'MD5 唯一标识',
`file_name` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件名称',
`file_path` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件路径',
`extension` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '扩展名',
`upload_time` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '上传时间',
`upload_status` INT DEFAULT 0 NOT NULL COMMENT '上传状态 (1 表示成功，0 表示失败或未完成)'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='上传任务信息表';

/*========= 图像表 ==========*/
DROP TABLE IF EXISTS `image`; 
CREATE TABLE `image` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` BIGINT NOT NULL COMMENT '图像编号' primary key,
`file_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件编号',
`width` INT DEFAULT 0 NOT NULL COMMENT '图片的宽度',
`图像的高` INT DEFAULT 0 NOT NULL COMMENT '图片的高度'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图像表';

/*========= 文件信息表 ==========*/
DROP TABLE IF EXISTS `file`; 
CREATE TABLE `file` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` VARCHAR(100) NOT NULL COMMENT '文件 ID' primary key,
`url` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件 URL',
`size` BIGINT DEFAULT 0 NOT NULL COMMENT '文件大小',
`status` INT DEFAULT 0 NOT NULL COMMENT '文件状态 (0-失效，1-生效)',
`storage_type` INT DEFAULT 0 NOT NULL COMMENT '存储类型',
`identifier` VARCHAR(100) DEFAULT '' NOT NULL COMMENT 'MD5 唯一标识',
`create_user_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '创建用户 ID',
`modify_user_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '修改用户 ID'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件信息表';

/*========= 文件扩展名信息表 ==========*/
DROP TABLE IF EXISTS `file_extension`; 
CREATE TABLE `file_extension` ( 
`id` VARCHAR(100) NOT NULL COMMENT '文件拓展名 ID' primary key,
`name` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件扩展名',
`desc` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件扩展名描述',
`img_url` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件扩展名预览图 URL'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件扩展名信息表';

/*========= 分享信息表 ==========*/
DROP TABLE IF EXISTS `share`; 
CREATE TABLE `share` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` VARCHAR(100) NOT NULL COMMENT '分享编号' primary key,
`user_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户 ID',
`share_time` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '分享时间',
`end_time` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '失效时间',
`extraction_code` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '提取码',
`share_batch_num` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '分享批次号',
`share_type` INT DEFAULT 0 NOT NULL COMMENT '分享类型 (0 公共, 1 私密, 2 好友)',
`share_status` INT DEFAULT 0 NOT NULL COMMENT '分享状态 (0 正常, 1 已失效, 2 已撤销)'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分享信息表';

/*========= 文件分类信息表 ==========*/
DROP TABLE IF EXISTS `file_category`; 
CREATE TABLE `file_category` ( 
`id` BIGINT NOT NULL COMMENT '文件分类 ID' primary key,
`type_id` INT DEFAULT 0 NOT NULL COMMENT '文件类型 ID',
`extension` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件扩展名'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件分类信息表';

/*========= 通用文件信息表 ==========*/
DROP TABLE IF EXISTS `common_file`; 
CREATE TABLE `common_file` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` VARCHAR(100) NOT NULL COMMENT '通用文件 ID' primary key,
`user_file_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户文件 ID'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通用文件信息表';

/*========= 用户文件信息表 ==========*/
DROP TABLE IF EXISTS `user_file`; 
CREATE TABLE `user_file` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` VARCHAR(100) NOT NULL COMMENT '用户文件 ID' primary key,
`user_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户 ID',
`file_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件 ID',
`file_name` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件名',
`file_path` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件路径',
`extension` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '扩展名',
`is_dir` INT DEFAULT 0 NOT NULL COMMENT '是否是目录 (0-否, 1-是)',
`upload_time` TIMESTAMP(3) NOT NULL COMMENT '修改时间',
`delete_time` TIMESTAMP(3) NOT NULL COMMENT '删除时间',
`delete_batch_num` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '删除批次号',
`create_user_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '创建用户 ID',
`modify_user_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '修改用户 ID'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户文件信息表';

/*========= 分享文件信息表 ==========*/
DROP TABLE IF EXISTS `share_file`; 
CREATE TABLE `share_file` ( 
`id` VARCHAR(100) NOT NULL COMMENT '分享文件编号' primary key,
`share_batch_num` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '分享批次号',
`user_file_id` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户文件 ID',
`share_file_path` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '分享文件路径'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分享文件信息表';

/*========= 图片文件信息表 ==========*/
DROP TABLE IF EXISTS `picture_file`; 
CREATE TABLE `picture_file` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`picture_file_id` BIGINT NOT NULL COMMENT '图片文件编号' primary key,
`url` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '图片文件 URL',
`size` BIGINT DEFAULT 0 NOT NULL COMMENT '文件大小',
`storage_type` INT DEFAULT 0 NOT NULL COMMENT '存储类型',
`user_id` BIGINT DEFAULT 0 NOT NULL COMMENT '用户 ID',
`fileName` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '文件名称',
`extension` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '扩展名'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片文件信息表';

/*========= 角色信息表 ==========*/
DROP TABLE IF EXISTS `admin_role`; 
CREATE TABLE `admin_role` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` BIGINT NOT NULL COMMENT '管理员角色 ID' primary key,
`name` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '角色名称',
`key` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '角色权限字符串',
`sort` INT DEFAULT 0 NOT NULL COMMENT '显示顺序',
`status` INT DEFAULT 0 NOT NULL COMMENT '角色状态（1正常 0停用）',
`remark` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '备注'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

/*========= 用户信息表 ==========*/
DROP TABLE IF EXISTS `admin`; 
CREATE TABLE `admin` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` BIGINT NOT NULL COMMENT '用户编号' primary key,
`role_id` BIGINT DEFAULT 0 NOT NULL COMMENT '角色id',
`username` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户账号',
`nickname` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户昵称',
`email` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户邮箱',
`phone_number` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '手机号码',
`sex` INT DEFAULT 0 NOT NULL COMMENT '用户性别（0男 1女 2未知）',
`avatar` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '头像地址',
`password` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '密码',
`status` INT DEFAULT 0 NOT NULL COMMENT '帐号状态（1正常 2停用 3冻结）',
`login_ip` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '最后登录IP',
`login_date` TIMESTAMP(3) NOT NULL COMMENT '最后登录时间',
`is_admin` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '超级管理员标志（1是，0否）',
`remark` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '备注'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

/*========= 用户信息表 ==========*/
DROP TABLE IF EXISTS `user`; 
CREATE TABLE `user` ( 
`create_time` TIMESTAMP(3) NOT NULL COMMENT '创建时间',
`update_time` TIMESTAMP(3) NOT NULL COMMENT '更新时间',
`deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '删除标志（0代表存在 1代表删除）',
`id` BIGINT NOT NULL COMMENT '用户 ID' primary key,
`role_id` BIGINT DEFAULT 0 NOT NULL COMMENT '角色 ID',
`username` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户账号',
`nickname` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户昵称',
`email` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '用户邮箱',
`phone_number` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '手机号码',
`sex` INT DEFAULT 0 NOT NULL COMMENT '用户性别（0男 1女 2未知）',
`avatar` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '头像地址',
`password` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '密码',
`department` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '所在院系',
`status` INT DEFAULT 0 NOT NULL COMMENT '帐号状态（1 正常 2 停用 3 冻结）',
`login_ip` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '最后登录IP',
`login_date` TIMESTAMP(3) NOT NULL COMMENT '最后登录时间',
`remark` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '备注'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';
