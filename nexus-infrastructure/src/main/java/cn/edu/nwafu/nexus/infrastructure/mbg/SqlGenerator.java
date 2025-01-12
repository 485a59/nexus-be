package cn.edu.nwafu.nexus.infrastructure.mbg;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class SqlGenerator {
    public static void main(String[] args) {
        String packageName = "cn.edu.nwafu.nexus.domain.entity";
        String filePath = "sql";
        StringBuffer sqls = new StringBuffer();
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
        StringToSql(sqls.toString(), filePath + "/nexus.sql");
    }

    /**
     * 根据实体类生成建表语句。
     *
     * @param className 全类名
     * @param tableName 数据库名
     */
    public static String generateSql(String className, String tableName) throws ClassNotFoundException {
        Class<?> clz = Class.forName(className);
        className = clz.getSimpleName();

        if (tableName == null || tableName.isEmpty()) {
            tableName = clz.getName();
            tableName = tableName.substring(tableName.lastIndexOf(".") + 1);
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
            SqlColumn sqlColumn = field.getAnnotation(SqlColumn.class);
            String columnName = getColumnName(field);
            String columnType = getColumnType(field, sqlColumn);

            sql.append("`").append(columnName).append("` ").append(columnType).append(",\n");
            if (sqlColumn != null && sqlColumn.primaryKey()) {
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
        String packagePath = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<String> classList = new ArrayList<>();

        try {
            java.net.URL packageURL = classLoader.getResource(packagePath);

            if (packageURL == null) {
                log.error("Package not found: {}", packageName);
                return classList;
            }
            File directory = new File(packageURL.getFile());

            if (directory.exists()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".class")) {
                            String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                            classList.add(className);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error while listing classes in package: {}", packageName, e);
        }


        return classList;
    }

    /**
     * 将 string 写入 SQL 文件。
     *
     * @param str  SQL 语句
     * @param path 存储 SQL 文件的路径
     */
    public static void StringToSql(String str, String path) {
        try (FileOutputStream outStream = new FileOutputStream(path)) {
            byte[] sourceByte = str.getBytes();
            outStream.write(sourceByte);
            System.out.println("生成成功");
        } catch (IOException e) {
            log.error("Error writing SQL file: {}", path, e);
        }
    }

    /**
     * 将 Java 属性名转换为 SQL 列名。
     *
     * @param field 字段
     * @return SQL 列名
     */
    private static String getColumnName(Field field) {
        String column = field.getName();
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
     * 获取 SQL 列类型。
     *
     * @param field     字段
     * @param sqlColumn 注解信息
     * @return SQL 列类型
     */
    private static String getColumnType(Field field, SqlColumn sqlColumn) {
        if (sqlColumn != null && !sqlColumn.type().isEmpty()) {
            return sqlColumn.type();
        }


        Class<?> fieldType = field.getType();


        if (fieldType == Integer.class) {
            return "int";
        } else if (fieldType == Short.class) {
            return "tinyint(4)";
        } else if (fieldType == Long.class) {
            return "bigint";
        } else if (fieldType == java.math.BigDecimal.class) {
            return "decimal(20,4)";
        } else if (fieldType == Double.class) {
            return "double precision not null";
        } else if (fieldType == Float.class) {
            return "float";
        } else if (fieldType == Boolean.class) {
            return "tinyint(4)";
        } else if (fieldType == java.sql.Timestamp.class) {
            return "datetime";
        } else if (fieldType == java.util.Date.class) {
            return "datetime";
        } else if (fieldType == String.class) {
            return "varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL";
        }


        return "TEXT";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface SqlColumn {
        boolean primaryKey() default false;

        String type() default "";
    }
}
