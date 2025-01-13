package cn.edu.nwafu.nexus.domain.mbg;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;


@Slf4j
public class SqlGenerator {
    private static final Map<Class<?>, String> TYPE_MAPPING = new HashMap<>();

    static {
        TYPE_MAPPING.put(Integer.class, "INT");
        TYPE_MAPPING.put(Short.class, "SMALLINT");
        TYPE_MAPPING.put(Long.class, "BIGINT");
        TYPE_MAPPING.put(java.math.BigDecimal.class, "DECIMAL(20,4)");
        TYPE_MAPPING.put(Double.class, "DOUBLE");
        TYPE_MAPPING.put(Float.class, "FLOAT");
        TYPE_MAPPING.put(Boolean.class, "TINYINT(1)");
        TYPE_MAPPING.put(java.sql.Timestamp.class, "TIMESTAMP(3)");
        TYPE_MAPPING.put(java.util.Date.class, "TIMESTAMP(3)");
        TYPE_MAPPING.put(String.class, "VARCHAR(100)");
    }

    public static void main(String[] args) {
        String packageName = "cn.edu.nwafu.nexus.domain.entity";
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
        
        System.out.println(sqls.toString());
        StringToSql(sqls.toString(), "nexus.sql");
    }


    /**
     * 根据实体类生成建表语句。
     *
     * @param className 全类名
     * @param tableName 数据库名
     */
    public static String generateSql(String className) throws ClassNotFoundException {
        Class<?> clz = Class.forName(className);
        String tableName = "";
        TableName tableNameAnnotation = clz.getAnnotation(TableName.class);
        if (tableNameAnnotation != null && !tableNameAnnotation.value().isEmpty()) {
            tableName = tableNameAnnotation.value();
        } else {
            tableName = clz.getSimpleName();
        }
        String tableDescription = "";
        ApiModel apiModelAnnotation = clz.getAnnotation(ApiModel.class);
        if (apiModelAnnotation != null && !apiModelAnnotation.description().isEmpty()) {
            tableDescription = apiModelAnnotation.description();
        }

        StringBuilder sql = new StringBuilder(50);

        sql.append("\n/*========= ").append(tableDescription).append(" ==========*/\n");
        sql.append("DROP TABLE IF EXISTS `").append(tableName).append("`; \n");
        sql.append("CREATE TABLE `").append(tableName).append("` ( \n");
        Field[] fields = clz.getDeclaredFields();
        String primaryKey = null;

        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            String columnName = getColumnName(field);
            String columnType = getColumnType(field);

            // 检查字段是否有 @TableField 注解，获取注解中的默认值
            TableField tableFieldAnnotation = field.getAnnotation(TableField.class);
            String defaultValue = "";
            if (tableFieldAnnotation != null && !tableFieldAnnotation.value().isEmpty()) {
                defaultValue = getDefaultValue(field.getType());
            }

            sql.append("`").append(columnName).append("` ").append(columnType);
            if (!defaultValue.isEmpty()) {
                sql.append(" DEFAULT ").append(defaultValue);
            }
            sql.append(" NOT NULL COMMENT '");
            // 获取 @ApiModelProperty 中的描述
            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
            if (apiModelProperty != null && !apiModelProperty.value().isEmpty()) {
                sql.append(apiModelProperty.value());
            } else {
                sql.append("");
            }
            sql.append("',\n");

            TableId tableIdAnnotation = field.getAnnotation(TableId.class);
            if (tableIdAnnotation != null) {
                primaryKey = columnName;
            }
        }
        if (primaryKey != null) {
            sql.append("PRIMARY KEY pk_").append(tableName).append("(`").append(primaryKey).append("`)");
        }

        sql.append("\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='").append(tableDescription).append("';\n");

        return sql.toString();
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
            // 获取资源枚举
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                // 调用辅助方法查找类
                findClasses(packageName, directory, classList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classList;
    }


    private static void findClasses(String packageName, File directory, List<String> classList) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归调用查找子目录下的类
                    findClasses(packageName + "." + file.getName(), file, classList);
                } else if (file.getName().endsWith(".class")) {
                    // 处理.class 文件，构建类名
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classList.add(className);
                }
            }
        }
    }


    /**
     * 将 string 写入 SQL 文件。
     *
     * @param str      SQL 语句
     * @param fileName 存储 SQL 文件的路径
     */
    public static void StringToSql(String str, String fileName) {
        // 获取当前类的类加载器
        ClassLoader classLoader = SqlGenerator.class.getClassLoader();
        // 获取资源目录的路径
        File resourceDirectory = new File(classLoader.getResource(".").getFile());
        // 构建 SQL 文件的路径，假设存储在 resources/sql 目录下
        File sqlFile = new File(resourceDirectory.getAbsolutePath() + "/sql/" + fileName);


        try (OutputStream outStream = new FileOutputStream(sqlFile)) {
            byte[] sourceByte = str.getBytes();
            outStream.write(sourceByte);
            System.out.printf("生成成功，文件路径：%s", sqlFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error writing SQL file: {}", fileName, e);
        }
    }


    /**
     * 将 Java 属性名转换为 SQL 列名。
     *
     * @param field 字段
     * @return SQL 列名
     */
    private static String getColumnName(Field field) {
        TableId tableIdAnnotation = field.getAnnotation(TableId.class);
        TableField tableFieldAnnotation = field.getAnnotation(TableField.class);
        String column;
        if (tableIdAnnotation != null) {
            column = tableIdAnnotation.value();
        } else if (tableFieldAnnotation != null && !tableFieldAnnotation.value().isEmpty()) {
            column = tableFieldAnnotation.value();
        } else {
            column = field.getName();
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < column.length(); i++) {
            char c = column.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                result.append("_").append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }


    /**
     * 获取列的类型。
     *
     * @param field 字段属性
     * @return 字段对应 MySQL 类型
     */
    public static String getColumnType(Field field) {
        Class<?> fieldType = field.getType();
        return TYPE_MAPPING.getOrDefault(fieldType, "TEXT");
    }


    /**
     * 获取不同类型的默认值。
     *
     * @param type 类型
     * @return 默认值
     */
    private static String getDefaultValue(Class<?> type) {
        if (type == Integer.class || type == Long.class || type == Short.class || type == Float.class || type == Double.class) {
            return "0";
        } else if (type == Boolean.class) {
            return "0";
        } else if (type == String.class) {
            return "''";
        } else {
            return "";
        }
    }
}