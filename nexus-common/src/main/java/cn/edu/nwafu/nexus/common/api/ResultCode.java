package cn.edu.nwafu.nexus.common.api;

import org.springframework.util.Assert;

/**
 * 错误码合集。
 *
 * @author Huang Z.Y.
 */
public class ResultCode {
    public enum Http implements IErrorCode {
        SUCCESS(200, "操作成功", "Http.SUCCESS"),
        FAILED(500, "操作失败", "Http.FAILED"),
        VALIDATE_FAILED(404, "参数检验失败", "Http.VALIDATE_FAILED"),
        UNAUTHORIZED(401, "暂未登录或 token 已经过期", "Http.UNAUTHORIZED"),
        FORBIDDEN(403, "没有相关权限", "Http.FORBIDDEN");
        
        private final int code;
        private final String msg;
        private final String i18nKey;

        Http(int code, String msg, String i18nKey) {
            this.code = code;
            this.msg = msg;
            this.i18nKey = i18nKey;
        }

        @Override
        public int code() {
            return this.code;
        }

        @Override
        public String message() {
            return this.msg;
        }

        @Override
        public String i18nKey() {
            return this.i18nKey;
        }
    }

    /**
     * 业务逻辑错误码枚举，涵盖系统中各种业务操作可能出现的错误，范围为 10000~99999。
     * 采用模块+错误描述的命名方式，如 COMMON_OBJECT_NOT_FOUND 表示通用模块下的对象未找到错误。
     */
    public enum Business implements IErrorCode {
        COMMON_OBJECT_NOT_FOUND(10001, "找不到 ID 为 {} 的 {}", "Business.OBJECT_NOT_FOUND"),
        COMMON_UNSUPPORTED_OPERATION(10002, "不支持的操作", "Business.UNSUPPORTED_OPERATION"),
        COMMON_BULK_DELETE_IDS_IS_INVALID(10003, "批量参数 ID 列表为空", "Business.BULK_DELETE_IDS_IS_INVALID"),
        COMMON_FILE_NOT_ALLOWED_TO_DOWNLOAD(10004, "文件名称({}) 非法，不允许下载", "Business.FILE_NOT_ALLOWED_TO_DOWNLOAD"),
        PERMISSION_FORBIDDEN_TO_MODIFY_ADMIN(10101, "不允许修改管理员的信息", "Business.FORBIDDEN_TO_MODIFY_ADMIN"),
        PERMISSION_NOT_ALLOWED_TO_OPERATE(10202, "没有权限进行此操作，请联系管理员", "Business.NO_PERMISSION_TO_OPERATE"),
        LOGIN_WRONG_USER_PASSWORD(10201, "用户密码错误，请重输", "Business.LOGIN_WRONG_USER_PASSWORD"),
        LOGIN_ERROR(10202, "登录失败：{}", "Business.LOGIN_ERROR"),
        LOGIN_CAPTCHA_CODE_WRONG(10203, "验证码错误", "Business.LOGIN_CAPTCHA_CODE_WRONG"),
        LOGIN_CAPTCHA_CODE_EXPIRE(10204, "验证码过期", "Business.LOGIN_CAPTCHA_CODE_EXPIRE"),
        LOGIN_CAPTCHA_CODE_NULL(10205, "验证码为空", "Business.LOGIN_CAPTCHA_CODE_NULL"),
        UPLOAD_FILE_TYPE_NOT_ALLOWED(10401, "不允许上传的文件类型，仅允许：{}", "Business.UPLOAD_FILE_TYPE_NOT_ALLOWED"),
        UPLOAD_FILE_NAME_EXCEED_MAX_LENGTH(10402, "文件名长度超过：{}", "Business.UPLOAD_FILE_NAME_EXCEED_MAX_LENGTH"),
        UPLOAD_FILE_SIZE_EXCEED_MAX_SIZE(10403, "文件名大小超过：{} MB", "Business.UPLOAD_FILE_SIZE_EXCEED_MAX_SIZE"),
        UPLOAD_IMPORT_EXCEL_FAILED(10404, "导入 excel 失败：{}", "Business.UPLOAD_IMPORT_EXCEL_FAILED"),
        UPLOAD_FILE_IS_EMPTY(10405, "上传文件为空", "Business.UPLOAD_FILE_IS_EMPTY"),
        UPLOAD_FILE_FAILED(10406, "上传文件失败：{}", "Business.UPLOAD_FILE_FAILED"),
        CONFIG_VALUE_IS_NOT_ALLOW_TO_EMPTY(10601, "参数键值不允许为空", "Business.CONFIG_VALUE_IS_NOT_ALLOW_TO_EMPTY"),
        CONFIG_VALUE_IS_NOT_IN_OPTIONS(10602, "参数键值不存在列表中", "Business.CONFIG_VALUE_IS_NOT_IN_OPTIONS"),
        POST_NAME_IS_NOT_UNIQUE(10701, "岗位名称：{} 已存在", "Business.POST_NAME_IS_NOT_UNIQUE"),
        POST_CODE_IS_NOT_UNIQUE(10702, "岗位编号：{} 已存在", "Business.POST_CODE_IS_NOT_UNIQUE"),
        POST_ALREADY_ASSIGNED_TO_USER_CAN_NOT_BE_DELETED(10703, "职位已分配给用户，请先取消分配再删除", "Business.POST_ALREADY_ASSIGNED_TO_USER_CAN_NOT_BE_DELETED"),
        DEPT_NAME_IS_NOT_UNIQUE(10801, "部门名称：{} 已存在", "Business.DEPT_NAME_IS_NOT_UNIQUE"),
        DEPT_PARENT_ID_IS_NOT_ALLOWED_SELF(10802, "父级部门不能选择自己", "Business.DEPT_PARENT_ID_IS_NOT_ALLOWED_SELF"),
        DEPT_STATUS_ID_IS_NOT_ALLOWED_CHANGE(10803, "子部门还有正在启用的部门，暂时不能停用该部门", "Business.DEPT_STATUS_ID_IS_NOT_ALLOWED_CHANGE"),
        DEPT_EXIST_CHILD_DEPT_NOT_ALLOW_DELETE(10804, "该部门存在下级部门不允许删除", "Business.DEPT_EXIST_CHILD_DEPT_NOT_ALLOW_DELETE"),
        DEPT_EXIST_LINK_USER_NOT_ALLOW_DELETE(10805, "该部门存在关联的用户不允许删除", "Business.DEPT_EXIST_LINK_USER_NOT_ALLOW_DELETE"),
        DEPT_PARENT_DEPT_NO_EXIST_OR_DISABLED(10806, "该父级部门不存在或已停用", "Business.DEPT_PARENT_DEPT_NO_EXIST_OR_DISABLED"),
        MENU_NAME_IS_NOT_UNIQUE(10901, "新增菜单：{} 失败，菜单名称已存在", "Business.MENU_NAME_IS_NOT_UNIQUE"),
        MENU_EXTERNAL_LINK_MUST_BE_HTTP(10902, "菜单外链必须以 http(s):// 开头", "Business.MENU_EXTERNAL_LINK_MUST_BE_HTTP"),
        MENU_PARENT_ID_NOT_ALLOW_SELF(10903, "父级菜单不能选择自身", "Business.MENU_PARENT_ID_NOT_ALLOW_SELF"),
        MENU_EXIST_CHILD_MENU_NOT_ALLOW_DELETE(10904, "存在子菜单不允许删除", "Business.MENU_EXIST_CHILD_MENU_NOT_ALLOW_DELETE"),
        MENU_ALREADY_ASSIGN_TO_ROLE_NOT_ALLOW_DELETE(10905, "菜单已分配给角色，不允许删除", "Business.MENU_ALREADY_ASSIGN_TO_ROLE_NOT_ALLOW_DELETE"),
        MENU_NOT_ALLOWED_TO_CREATE_BUTTON_ON_IFRAME_OR_OUT_LINK(10906, "不允许在 Iframe 和外链跳转类型下创建按钮", "Business.MENU_ONLY_ALLOWED_TO_CREATE_BUTTON_ON_PAGE"),
        MENU_ONLY_ALLOWED_TO_CREATE_SUB_MENU_IN_CATALOG(10907, "只允许在目录类型下创建子菜单", "Business.MENU_ONLY_ALLOWED_TO_CREATE_SUB_MENU_IN_CATALOG"),
        MENU_CAN_NOT_CHANGE_MENU_TYPE(10908, "不允许更改菜单的类型", "Business.MENU_CAN_NOT_CHANGE_MENU_TYPE"),
        ROLE_NAME_IS_NOT_UNIQUE(11001, "角色名称：{} 已存在", "Business.ROLE_NAME_IS_NOT_UNIQUE"),
        ROLE_KEY_IS_NOT_UNIQUE(11002, "角色标识：{} 已存在", "Business.ROLE_KEY_IS_NOT_UNIQUE"),
        ROLE_DATA_SCOPE_DUPLICATED_DEPT(11003, "重复的部门 id", "Business.ROLE_DATA_SCOPE_DUPLICATED_DEPT"),
        ROLE_ALREADY_ASSIGN_TO_USER(11004, "角色已分配给用户，请先取消分配，再删除角色", "Business.ROLE_ALREADY_ASSIGN_TO_USER"),
        ROLE_IS_NOT_AVAILABLE(11005, "角色：{} 已禁用，无法分配给用户", "Business.ROLE_IS_NOT_AVAILABLE"),
        USER_NON_EXIST(10501, "登录用户：{} 不存在", "Business.USER_NON_EXIST"),
        USER_IS_DISABLE(10502, "对不起，您的账号：{} 已停用", "Business.USER_IS_DISABLE"),
        USER_CACHE_IS_EXPIRE(11003, "用户缓存信息已过期", "Business.USER_CACHE_IS_EXPIRE"),
        USER_FAIL_TO_GET_USER_ID(11004, "获取用户 ID 失败", "Business.USER_FAIL_TO_GET_USER_ID"),
        USER_FAIL_TO_GET_DEPT_ID(10504, "获取用户部门 ID 失败", "Business.USER_FAIL_TO_GET_DEPT_ID"),
        USER_FAIL_TO_GET_ACCOUNT(10505, "获取用户账户失败", "Business.USER_FAIL_TO_GET_ACCOUNT"),
        USER_FAIL_TO_GET_USER_INFO(10506, "获取用户信息失败", "Business.USER_FAIL_TO_GET_USER_INFO"),
        USER_IMPORT_DATA_IS_NULL(10507, "导入的用户为空", "Business.USER_IMPORT_DATA_IS_NULL"),
        USER_PHONE_NUMBER_IS_NOT_UNIQUE(10508, "该电话号码已被其他用户占用", "Business.USER_PHONE_NUMBER_IS_NOT_UNIQUE"),
        USER_EMAIL_IS_NOT_UNIQUE(10509, "该邮件地址已被其他用户占用", "Business.USER_EMAIL_IS_NOT_UNIQUE"),
        USER_PASSWORD_IS_NOT_CORRECT(10510, "用户密码错误", "Business.USER_PASSWORD_IS_NOT_CORRECT"),
        USER_NEW_PASSWORD_IS_THE_SAME_AS_OLD(10511, "用户新密码与旧密码相同", "Business.USER_NEW_PASSWORD_IS_THE_SAME_AS_OLD"),
        USER_UPLOAD_FILE_FAILED(10512, "用户上传文件失败", "Business.USER_UPLOAD_FILE_FAILED"),
        USER_NAME_IS_NOT_UNIQUE(10513, "用户名已被其他用户占用", "Business.USER_NAME_IS_NOT_UNIQUE"),
        USER_CURRENT_USER_CAN_NOT_BE_DELETE(10514, "当前用户不允许被删除", "Business.USER_CURRENT_USER_CAN_NOT_BE_DELETE"),
        USER_ADMIN_CAN_NOT_BE_MODIFY(10515, "管理员不允许做任何修改", "Business.USER_ADMIN_CAN_NOT_BE_MODIFY");


        private final int code;
        private final String msg;
        private final String i18nKey;

        Business(int code, String msg, String i18nKey) {
            Assert.isTrue(code > 10000 && code < 99999, "错误码 code 值范围应在 10000~99999 之间，请查看 ErrorCode.Business 类，当前为 " + name());
            String errorTypeName = this.getClass().getSimpleName();
            Assert.isTrue(i18nKey != null && i18nKey.startsWith(errorTypeName),
                    String.format("i18nKey 应以前缀 %s 开头，当前为 %s", errorTypeName, name()));
            this.code = code;
            this.msg = msg;
            this.i18nKey = i18nKey;
        }

        @Override
        public int code() {
            return this.code;
        }

        @Override
        public String message() {
            return this.msg;
        }

        @Override
        public String i18nKey() {
            return i18nKey;
        }
    }


    /**
     * 外部错误码枚举，适用于调用外部服务时出现的错误，范围为 1000~9999。
     * 例如支付服务调用失败等情况。
     */
    public enum External implements IErrorCode {
        FAIL_TO_PAY_ON_ALIPAY(1001, "支付宝调用失败", "External.FAIL_TO_PAY_ON_ALIPAY");

        private final int code;
        private final String msg;
        private final String i18nKey;

        External(int code, String msg, String i18nKey) {
            Assert.isTrue(code > 1000 && code < 9999, "错误码 code 值范围应在 1000~9999 之间，请查看 ErrorCode.External 类，当前为 " + name());
            String errorTypeName = this.getClass().getSimpleName();
            Assert.isTrue(i18nKey != null && i18nKey.startsWith(errorTypeName),
                    String.format("i18nKey 应以前缀 %s 开头，当前为 %s", errorTypeName, name()));
            this.code = code;
            this.msg = msg;
            this.i18nKey = i18nKey;
        }

        @Override
        public int code() {
            return this.code;
        }

        @Override
        public String message() {
            return this.msg;
        }

        @Override
        public String i18nKey() {
            return this.i18nKey;
        }
    }


    /**
     * 客户端错误码枚举，适用于客户端调用时出现的错误，范围为 100~999。
     * 包括参数错误、授权错误、调用频率过高等客户端常见错误。
     */
    public enum Client implements IErrorCode {
        COMMON_FORBIDDEN_TO_CALL(101, "禁止调用", "Client.COMMON_FORBIDDEN_TO_CALL"),
        COMMON_REQUEST_TOO_OFTEN(102, "调用太过频繁", "Client.COMMON_REQUEST_TOO_OFTEN"),
        COMMON_REQUEST_PARAMETERS_INVALID(103, "请求参数异常，{}", "Client.COMMON_REQUEST_PARAMETERS_INVALID"),
        COMMON_REQUEST_METHOD_INVALID(104, "请求方式：{} 不支持", "Client.COMMON_REQUEST_METHOD_INVALID"),
        COMMON_REQUEST_RESUBMIT(105, "请求重复提交", "Client.COMMON_REQUEST_RESUBMIT"),
        COMMON_NO_AUTHORIZATION(106, "请求接口：{} 失败，用户未授权", "Client.COMMON_NO_AUTHORIZATION"),
        INVALID_TOKEN(107, "token 异常", "Client.INVALID_TOKEN"),
        TOKEN_PROCESS_FAILED(108, "token 处理失败：{}", "Client.TOKEN_PROCESS_FAILED");


        private final int code;
        private final String msg;
        private final String i18nKey;

        Client(int code, String msg, String i18nKey) {
            Assert.isTrue(code > 100 && code < 999, "错误码 code 值范围应在 100~999 之间，请查看 ErrorCode.Client 类，当前为 " + name());
            String errorTypeName = this.getClass().getSimpleName();
            Assert.isTrue(i18nKey != null && i18nKey.startsWith(errorTypeName),
                    String.format("i18nKey 应以前缀 %s 开头，当前为 %s", errorTypeName, name()));
            this.code = code;
            this.msg = msg;
            this.i18nKey = i18nKey;
        }

        @Override
        public int code() {
            return this.code;
        }

        @Override
        public String message() {
            return this.msg;
        }

        @Override
        public String i18nKey() {
            return this.i18nKey;
        }
    }


    /**
     * 内部错误码枚举，适用于系统框架内部出现的错误，范围为 0~99。
     * 如参数异常、内部系统错误、数据库异常等。
     */
    public enum Internal implements IErrorCode {
        INVALID_PARAMETER(1, "参数异常：{}", "Internal.INVALID_PARAMETER"),
        INTERNAL_ERROR(2, "系统内部错误：{}", "Internal.INTERNAL_ERROR"),
        GET_ENUM_FAILED(3, "获取枚举类型失败，枚举类：{}", "Internal.GET_ENUM_FAILED"),
        GET_CACHE_FAILED(4, "获取缓存失败：{}", "Internal.GET_CACHE_FAILED"),
        DB_INTERNAL_ERROR(5, "数据库异常", "Internal.DB_INTERNAL_ERROR"),
        LOGIN_CAPTCHA_GENERATE_FAIL(7, "验证码生成失败", "Internal.LOGIN_CAPTCHA_GENERATE_FAIL"),
        EXCEL_PROCESS_ERROR(8, "excel 处理失败：{}", "Internal.EXCEL_PROCESS_ERROR");


        private final int code;
        private final String msg;
        private final String i18nKey;

        Internal(int code, String msg, String i18nKey) {
            Assert.isTrue(code < 100,
                    "错误码 code 值定义失败，Internal 错误码 code 值范围在 100~999 之间，请查看 ErrorCode.Internal类，当前错误码码为" + name());

            String errorTypeName = this.getClass().getSimpleName();
            Assert.isTrue(i18nKey != null && i18nKey.startsWith(errorTypeName),
                    String.format("错误码 i18nKey 值定义失败，%s错误码 i18nKey 值必须以 %s 开头，当前错误码为 %s", errorTypeName, errorTypeName, name()));
            this.code = code;
            this.msg = msg;
            this.i18nKey = i18nKey;
        }

        @Override
        public int code() {
            return this.code;
        }

        @Override
        public String message() {
            return this.msg;
        }

        @Override
        public String i18nKey() {
            return this.i18nKey;
        }
    }
}