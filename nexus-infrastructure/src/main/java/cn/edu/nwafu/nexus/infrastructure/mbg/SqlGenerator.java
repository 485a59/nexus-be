package cn.edu.nwafu.nexus.infrastructure.mbg;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 根据 Java 实体生成 SQL。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class SqlGenerator {
    private static final Map<Class<?>, String> TYPE_MAPPING = new HashMap<>();

    static {
        TYPE_MAPPING.put(Integer.class, "INT");
        TYPE_MAPPING.put(Short.class, "SMALLINT");
        TYPE_MAPPING.put(Long.class, "BIGINT");
        TYPE_MAPPING.put(BigDecimal.class, "DECIMAL(20,4)");
        TYPE_MAPPING.put(Double.class, "DOUBLE");
        TYPE_MAPPING.put(Float.class, "FLOAT");
        TYPE_MAPPING.put(Boolean.class, "TINYINT(1)");
        TYPE_MAPPING.put(Timestamp.class, "TIMESTAMP");
        TYPE_MAPPING.put(Date.class, "TIMESTAMP");
        TYPE_MAPPING.put(String.class, "VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
    }

    public static void main(String[] args) {
        String packageName = "cn.edu.nwafu.nexus.domain.entity.*";
        StringBuilder sqls = new StringBuilder();
        List<String> classList = getAllClasses(packageName);
        for (String className : classList) {
            try {
                String sql = generateSql(className, "");
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
    public static String generateSql(String className, String tableName) throws ClassNotFoundException {
        Class<?> clz = Class.forName(className);
        TableName tableNameAnnotation = clz.getAnnotation(TableName.class);
        if (tableNameAnnotation != null && !tableNameAnnotation.value().isEmpty()) {
            tableName = tableNameAnnotation.value();
        } else {
            tableName = clz.getSimpleName();
        }


        StringBuilder sql = new StringBuilder(50);


        sql.append("\n\n/*========= ").append(tableName).append(" ==========*/\n");
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

            sql.append("`").append(columnName).append("` ").append(columnType).append(",\n");
            TableId tableIdAnnotation = field.getAnnotation(TableId.class);
            if (tableIdAnnotation != null) {
                primaryKey = columnName;
            }
        }

        if (primaryKey != null) {
            sql.append("PRIMARY KEY (`").append(primaryKey).append("`) USING BTREE,\n");
        }

        // Remove the trailing comma
        sql.deleteCharAt(sql.length() - 2);

        sql.append("\n) ENGINE = INNODB DEFAULT CHARSET=utf8;");

        return sql.toString();
    }

    /**
     * 获取包下的所有类名称。
     *
     * @param packageName 包名
     * @return 类名列表
     */
    public static List<String> getAllClasses(String packageName) {
        // 处理通配符
        String patternStr = packageName.replace('.', '/').replace("*", ".*");
        Pattern pattern = Pattern.compile(patternStr);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<String> classList = new ArrayList<>();

        try {
            // 获取所有的资源路径
            Enumeration<URL> resources = classLoader.getResources("");
            while (resources.hasMoreElements()) {
                URL packageURL = resources.nextElement();
                File directory = new File(packageURL.getFile());
                if (directory.exists()) {
                    findClasses(directory, pattern, packageName, classList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    private static void findClasses(File directory, Pattern pattern, String packageName, List<String> classList) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findClasses(file, pattern, packageName + "." + file.getName(), classList);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    // 检查包名是否匹配模式
                    if (pattern.matcher(className.replace('.', '/')).matches()) {
                        classList.add(className);
                    }
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
        TableField tableFieldAnnotation = field.getAnnotation(TableField.class);
        String column = tableFieldAnnotation != null && !tableFieldAnnotation.value().isEmpty() ? tableFieldAnnotation.value() : field.getName();

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
}