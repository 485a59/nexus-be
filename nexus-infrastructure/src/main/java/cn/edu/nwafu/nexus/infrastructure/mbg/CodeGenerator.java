package cn.edu.nwafu.nexus.infrastructure.mbg;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.IOException;
import java.io.InputStream;
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

        // 读取 MBG 配置文件
        InputStream is = CodeGenerator.class.getResourceAsStream("/generator-config.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(is);
        assert is != null;
        is.close();

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