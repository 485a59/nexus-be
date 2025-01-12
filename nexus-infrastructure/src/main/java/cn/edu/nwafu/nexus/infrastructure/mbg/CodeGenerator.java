package cn.edu.nwafu.nexus.infrastructure.mbg;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
public class CodeGenerator {


    public static void main(String[] args) throws Exception {
        // MBG 执行过程中的警告信息
        List<String> warnings = new ArrayList<>();
        // 当生成的代码重复时，覆盖原代码
        boolean overwrite = true;
        
        // 动态生成配置文件内容
        String configContent = generateConfigContent();

        // 读取 MBG 配置文件
        InputStream is = new ByteArrayInputStream(configContent.getBytes(StandardCharsets.UTF_8));
        ConfigurationParser cp;
        Configuration config;
        try {
            cp = new ConfigurationParser(warnings);
            config = cp.parseConfiguration(is);
        } catch (Exception e) {
            System.err.println("Error parsing configuration file: " + e.getMessage());
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }


        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        // 创建 MBG
        MyBatisGenerator myBatisGenerator;
        try {
            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            // 执行生成代码
            myBatisGenerator.generate(null);
        } catch (InvalidConfigurationException | IOException | InterruptedException e) {
            System.err.println("Error generating code: " + e.getMessage());
            throw e;
        }


        // 输出警告信息
        for (String warning : warnings) {
            System.out.println(warning);
        }
    }


    private static String generateConfigContent() {
        StringBuilder configBuilder = new StringBuilder();
        configBuilder.append("<!DOCTYPE generatorConfiguration\n")
                .append("        PUBLIC \"-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN\"\n")
                .append("        \"http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd\">\n")
                .append("<generatorConfiguration>\n")
                .append("    <context id=\"context1\" targetRuntime=\"MyBatis3\">\n")
                .append("        <commentGenerator>\n")
                .append("            <property name=\"suppressDate\" value=\"true\"/>\n")
                .append("            <property name=\"addRemarkComments\" value=\"true\"/>\n")
                .append("        </commentGenerator>\n")
                .append("        <!-- 数据库连接信息 -->\n")
                .append("        <jdbcConnection driverClass=\"com.mysql.cj.jdbc.Driver\"\n")
                .append("                        connectionURL=\"jdbc:mysql://localhost:3306/your_database\"\n")
                .append("                        userId=\"root\"\n")
                .append("                        password=\"your_password\">\n")
                .append("        </jdbcConnection>\n")
                .append("        <javaTypeResolver>\n")
                .append("            <property name=\"forceBigDecimals\" value=\"false\"/>\n")
                .append("        </javaTypeResolver>\n")
                .append("        <!-- 生成 Java 模型类的包名和位置 -->\n")
                .append("        <javaModelGenerator targetPackage=\"com.yourpackage.model\"\n")
                .append("                           targetProject=\"src/main/java\">\n")
                .append("            <property name=\"enableSubPackages\" value=\"false\"/>\n")
                .append("            <property name=\"trimStrings\" value=\"true\"/>\n")
                .append("        </javaModelGenerator>\n")
                .append("        <!-- 生成 Mapper 接口的包名和位置 -->\n")
                .append("        <javaClientGenerator type=\"XMLMAPPER\"\n")
                .append("                           targetPackage=\"com.yourpackage.mapper\"\n")
                .append("                           targetProject=\"src/main/java\">\n")
                .append("            <property name=\"enableSubPackages\" value=\"false\"/>\n")
                .append("        </javaClientGenerator>\n")
                .append("        <!-- 生成 Mapper XML 文件的包名和位置 -->\n")
                .append("        <sqlMapGenerator targetPackage=\"com.yourpackage.mapper.xml\"\n")
                .append("                        targetProject=\"src/main/resources\">\n")
                .append("            <property name=\"enableSubPackages\" value=\"false\"/>\n")
                .append("        </sqlMapGenerator>\n");

        // 扫描 entity 包下的类
        addEntityTables(configBuilder);
        configBuilder.append("    </context>\n")
                .append("</generatorConfiguration>");


        return configBuilder.toString();
    }

    private static void addEntityTables(StringBuilder configBuilder) {
        try {
            // 假设 entity 类在 com.yourpackage.entity 包下
            Class<?>[] entityClasses = getClasses("cn.edu.nwafu.domain.entity");
            for (Class<?> entityClass : entityClasses) {
                configBuilder.append("        <table tableName=\"")
                        .append(getTableName(entityClass))
                        .append("\">\n");
                // 主键为 id
                configBuilder.append("            <generatedKey column=\"id\" sqlStatement=\"MySql\" identity=\"true\"/>\n");

                // 遍历实体类的字段，添加列覆盖
                for (Field field : entityClass.getDeclaredFields()) {
                    configBuilder.append("            <columnOverride column=\"")
                            .append(field.getName())
                            .append("\" javaType=\"")
                            .append(field.getType().getName())
                            .append("\" jdbcType=\"")
                            .append(getJdbcType(field.getType()))
                            .append("\"/>\n");
                }

                configBuilder.append("        </table>\n");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error scanning entity package: " + e.getMessage());
        }
    }


    private static Class<?>[] getClasses(String packageName) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        java.net.URL resource = classLoader.getResource(path);
        if (resource == null) {
            return new Class<?>[0];
        }

        java.io.File directory = new java.io.File(resource.getFile());
        if (!directory.exists()) {
            return new Class<?>[0];
        }

        java.io.File[] files = directory.listFiles();
        if (files == null) {
            return new Class<?>[0];
        }

        List<Class<?>> classes = new ArrayList<>();
        for (java.io.File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(className));
            }
        }

        return classes.toArray(new Class<?>[0]);
    }


    private static String getTableName(Class<?> entityClass) {
        // 简单地将类名作为表名，可根据需要修改
        return entityClass.getSimpleName();
    }

    private static String getJdbcType(Class<?> fieldType) {
        if (fieldType == String.class) {
            return "VARCHAR";
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return "INTEGER";
        } else if (fieldType == long.class || fieldType == Long.class) {
            return "BIGINT";
        } else if (fieldType == double.class || fieldType == Double.class) {
            return "DOUBLE";
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return "BOOLEAN";
        }
        // 更多类型可根据需要添加
        return "UNKNOWN";
    }
}