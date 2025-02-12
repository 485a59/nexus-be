package cn.edu.nwafu.nexus.infrastructure.generator;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

/**
 * 实体转 MySQL 脚本。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class SqlDdlGenerator {

    private static final Map<Class<?>, String> TYPE_MAPPING = new HashMap<>();

    static {
        // Java 类型到 MySQL 类型的映射
        TYPE_MAPPING.put(Integer.class, "INT"); // 移除冗余的长度声明
        TYPE_MAPPING.put(Short.class, "SMALLINT");
        TYPE_MAPPING.put(Long.class, "BIGINT");
        TYPE_MAPPING.put(java.math.BigDecimal.class, "DECIMAL(20,2)"); // 调整精度
        TYPE_MAPPING.put(Double.class, "DOUBLE");
        TYPE_MAPPING.put(Float.class, "FLOAT");
        TYPE_MAPPING.put(Boolean.class, "TINYINT(1)");
        TYPE_MAPPING.put(java.sql.Timestamp.class, "TIMESTAMP(3)");
        TYPE_MAPPING.put(java.util.Date.class, "DATETIME(3)");
        TYPE_MAPPING.put(String.class, "VARCHAR(255)");
        TYPE_MAPPING.put(byte[].class, "LONGBLOB"); // 使用LONGBLOB替代BLOB
        TYPE_MAPPING.put(java.time.LocalDate.class, "DATE");
        TYPE_MAPPING.put(java.time.LocalDateTime.class, "DATETIME(3)");
        TYPE_MAPPING.put(java.time.LocalTime.class, "TIME(3)");
        TYPE_MAPPING.put(java.time.Instant.class, "TIMESTAMP(3)");
    }

    public static void main(String[] args) {
        String packageName = "cn.edu.nwafu.nexus.infrastructure.model.entity";
        StringBuilder sqls = new StringBuilder();
        List<String> classList = getAllClasses(packageName);

        for (String className : classList) {
            try {
                String sql = generateSql(className);
                sqls.append(sql);
            } catch (ClassNotFoundException e) {
                log.error("Error generating SQL for class: {}", className, e);
            }
        }
        StringToSql(sqls.toString(), "nexus.sql");
    }

    /**
     * 根据实体类生成建表语句。
     *
     * @param className 全类名
     * @return 生成的 SQL 语句
     */
    public static String generateSql(String className) throws ClassNotFoundException {
        Class<?> clz = Class.forName(className);
        StringBuilder sql = new StringBuilder(50);
        String tableName = getTableName(clz);
        String tableDescription = getTableDescription(clz);

        sql.append("\n/*========= ").append(tableDescription).append(" ==========*/\n");
        sql.append("DROP TABLE IF EXISTS `").append(tableName).append("`;\n");
        sql.append("CREATE TABLE `").append(tableName).append("` (\n");

        // 处理父类的字段
        processSuperClassFields(clz, sql);

        // 处理当前类的字段
        processClassFields(clz, sql);

        sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
        if (!tableDescription.isEmpty()) {
            sql.append(" COMMENT='").append(tableDescription).append("'");
        }
        sql.append(";\n");

        return sql.toString();
    }

    /**
     * 处理父类的字段。
     *
     * @param clz 实体类
     * @param sql SQL 语句构建器
     */
    private static void processSuperClassFields(Class<?> clz, StringBuilder sql) {
        Class<?> superClass = clz.getSuperclass();
        List<String> indexDefinitions = new ArrayList<>();

        while (superClass != null && !superClass.equals(Object.class)) {
            if (superClass.equals(Model.class)) {
                break;
            }
            processFields(superClass, sql, true, indexDefinitions);
            superClass = superClass.getSuperclass();
        }
    }

    /**
     * 处理当前类的字段。
     *
     * @param clz 实体类
     * @param sql SQL 语句构建器
     */
    private static void processClassFields(Class<?> clz, StringBuilder sql) {
        List<String> indexDefinitions = new ArrayList<>();
        processFields(clz, sql, false, indexDefinitions);

        // 添加所有索引定义
        if (!indexDefinitions.isEmpty()) {
            sql.append(String.join(",\n", indexDefinitions)).append("\n");
        } else {
            // 如果没有索引定义，移除最后一个字段的逗号
            int lastComma = sql.lastIndexOf(",");
            if (lastComma != -1) {
                sql.deleteCharAt(lastComma);
            }
        }
    }

    /**
     * 处理字段并生成 SQL。
     *
     * @param clz     实体类
     * @param sql     SQL 语句构建器
     * @param isSuper 是否是父类的字段
     */
    private static void processFields(Class<?> clz, StringBuilder sql, boolean isSuper, List<String> indexDefinitions) {
        Field[] fields = clz.getDeclaredFields();

        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }

            String columnName = getColumnName(field);
            String columnType = getColumnType(field);

            sql.append("    `").append(columnName).append("` ").append(columnType);

            // 处理主键字段
            TableId tableIdAnnotation = field.getAnnotation(TableId.class);
            if (tableIdAnnotation != null) {
                if (field.getType() == Long.class || field.getType() == Integer.class) {
                    sql.append(" AUTO_INCREMENT");
                }
                indexDefinitions.add("    PRIMARY KEY (`" + columnName + "`)");
            }

            // 处理非空约束和默认值
            Nullable nullableAnnotation = field.getAnnotation(Nullable.class);
            if (nullableAnnotation == null) {
                sql.append(" NOT NULL");
                if (tableIdAnnotation == null) {
                    String defaultValue = getDefaultValue(field);
                    if (!defaultValue.isEmpty()) {
                        sql.append(" DEFAULT ").append(defaultValue);
                    }
                }
            } else {
                sql.append(" NULL");
                if (field.getName().equals("deleteTime")) {
                    sql.append(" DEFAULT NULL");
                }
            }

            // 处理字段注释
            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
            if (apiModelProperty != null && !apiModelProperty.value().isEmpty()) {
                sql.append(" COMMENT '").append(apiModelProperty.value()).append("'");
            }

            // 始终添加逗号和换行，不再考虑是否是最后一个字段
            sql.append(",\n");

            // 处理普通索引
            processIndex(field, columnName, indexDefinitions);
        }
    }

    /**
     * 获取表名。
     *
     * @param clz 实体类
     * @return 表名
     */
    private static String getTableName(Class<?> clz) {
        TableName tableNameAnnotation = clz.getAnnotation(TableName.class);
        return tableNameAnnotation != null && !tableNameAnnotation.value().isEmpty()
                ? tableNameAnnotation.value()
                : convertCamelCaseToSnakeCase(clz.getSimpleName());
    }

    /**
     * 获取表描述。
     *
     * @param clz 实体类
     * @return 表描述
     */
    private static String getTableDescription(Class<?> clz) {
        ApiModel apiModelAnnotation = clz.getAnnotation(ApiModel.class);
        return apiModelAnnotation != null && !apiModelAnnotation.description().isEmpty()
                ? apiModelAnnotation.description()
                : "";
    }

    /**
     * 将 Java 属性名转换为 SQL 列名。
     *
     * @param field 字段
     * @return SQL 列名
     */
    private static String getColumnName(Field field) {
        TableField tableFieldAnnotation = field.getAnnotation(TableField.class);
        return tableFieldAnnotation != null && !tableFieldAnnotation.value().isEmpty()
                ? tableFieldAnnotation.value()
                : convertCamelCaseToSnakeCase(field.getName());
    }

    /**
     * 将驼峰命名转换为下划线命名。
     *
     * @param camelCase 驼峰命名字符串
     * @return 下划线命名字符串
     */
    private static String convertCamelCaseToSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * 获取列的类型。
     *
     * @param field 字段
     * @return MySQL 类型
     */
    private static String getColumnType(Field field) {
        return TYPE_MAPPING.getOrDefault(field.getType(), "TEXT");
    }

    /**
     * 获取默认值。
     *
     * @param field 字段
     * @return 默认值
     */
    private static String getDefaultValue(Field field) {
        Class<?> type = field.getType();

        // 处理特殊字段的默认值
        if (field.getName().equals("createTime") || field.getName().equals("updateTime")) {
            return "CURRENT_TIMESTAMP(3)";
        }
        if (field.getName().equals("deleteTime")) {
            return "NULL";
        }

        // 处理基本类型的默认值
        if (type == Integer.class || type == Long.class || type == Short.class) {
            return "0";
        } else if (type == Float.class || type == Double.class) {
            return "0.00";
        } else if (type == Boolean.class) {
            return "0";
        } else if (type == String.class) {
            return "''";
        } else if (type == java.math.BigDecimal.class) {
            return "0.00";
        }

        return "";
    }

    private static void processIndex(Field field, String columnName, List<String> indexDefinitions) {
        if (columnName.endsWith("_id") ||
                columnName.equals("user_id") ||
                columnName.equals("parent_id") ||
                columnName.equals("create_time") ||
                columnName.equals("update_time")) {
            String indexName = "idx_" + columnName;
            indexDefinitions.add("    KEY `" + indexName + "` (`" + columnName + "`)");  // 增加缩进空格
        }
    }

    /**
     * 获取包下的所有类名称。
     *
     * @param packageName 包名
     * @return 类名列表
     */
    public static List<String> getAllClasses(String packageName) {
        List<String> classList = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                findClasses(packageName, directory, classList);
            }
        } catch (IOException e) {
            log.error("Error finding classes in package: {}", packageName, e);
        }
        return classList;
    }

    /**
     * 递归查找类。
     *
     * @param packageName 包名
     * @param directory   目录
     * @param classList   类名列表
     */
    private static void findClasses(String packageName, File directory, List<String> classList) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findClasses(packageName + "." + file.getName(), file, classList);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classList.add(className);
                }
            }
        }
    }

    /**
     * 将 SQL 语句写入文件。
     *
     * @param sql      SQL 语句
     * @param fileName 文件名
     */
    public static void StringToSql(String sql, String fileName) {
        String projectRoot = System.getProperty("user.dir");
        File sqlFile = new File(projectRoot + "/sql/" + fileName);

        // 添加SQL文件头部注释
        StringBuilder finalSql = new StringBuilder();
        finalSql.append("-- ----------------------------\n");
        finalSql.append("-- 数据库表结构生成时间：").append(new java.util.Date()).append("\n");
        finalSql.append("-- ----------------------------\n\n");
        finalSql.append(sql);

        // 确保目录存在
        sqlFile.getParentFile().mkdirs();

        try (OutputStream outStream = new FileOutputStream(sqlFile)) {
            outStream.write(finalSql.toString().getBytes());
            log.info("SQL文件生成成功: {}", sqlFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("写入SQL文件时发生错误: {}", fileName, e);
        }
    }
}