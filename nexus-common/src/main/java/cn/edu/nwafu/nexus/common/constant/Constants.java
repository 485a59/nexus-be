package cn.edu.nwafu.nexus.common.constant;

/**
 * 通用常量信息
 *
 * @author Huang Z.Y.
 */
public class Constants {
    public static final int KB = 1024;
    public static final int MB = KB * 1024;
    public static final int GB = MB * 1024;
    /**
     * http请求
     */
    public static final String HTTP = "http://";
    /**
     * https请求
     */
    public static final String HTTPS = "https://";
    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "profile";
    public static final int DELETE_FILE_RANDOM_SIZE = 999999;

    public static final String SUPER_ADMIN_KEY = "super_admin";
    public static final String ADMIN_KEY = "admin";
    public static final String STUDENT_KEY = "student";
    public static final String TEACHER_KEY = "teacher";
    public static final String BEARER_PREFIX = "Bearer ";

    private Constants() {
    }

    public static class Token {
        /**
         * 令牌前缀
         */
        public static final String PREFIX = "Bearer ";
        /**
         * 令牌前缀
         */
        public static final String LOGIN_USER_KEY = "login_user_key";

        private Token() {
        }
    }

    public static class Captcha {

        /**
         * 令牌
         */
        public static final String MATH_TYPE = "math";
        /**
         * 令牌前缀
         */
        public static final String CHAR_TYPE = "char";

        private Captcha() {
        }

    }

    public static class UploadSubDir {
        public static final String IMPORT_PATH = "import";
        public static final String AVATAR_PATH = "avatar";
        public static final String DOWNLOAD_PATH = "download";
        public static final String UPLOAD_PATH = "upload";

        private UploadSubDir() {
        }
    }
}
