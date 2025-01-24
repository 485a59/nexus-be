package cn.edu.nwafu.nexus.domain.domain;

import cn.edu.nwafu.nexus.common.util.file.UFOPUtils;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

/**
 * Nexus 系统中的文件或目录。
 *
 * @author Huang Z.Y.
 */
public class SystemFile {
    public static final String separator = "/";
    @Getter
    private final String path;
    private final boolean isDirectory;

    public SystemFile(String pathname, boolean isDirectory) {
        this.path = formatPath(pathname);
        this.isDirectory = isDirectory;
    }

    public SystemFile(String parent, String child, boolean isDirectory) {
        if (parent != null) {
            String parentPath = separator.equals(formatPath(parent)) ? "" : formatPath(parent);
            String childPath = formatPath(child);
            if (childPath.startsWith(separator)) {
                childPath = childPath.replaceFirst(separator, "");
            }
            this.path = parentPath + separator + childPath;
        } else {
            this.path = formatPath(child);
        }
        this.isDirectory = isDirectory;
    }

    public static String formatPath(String path) {
        path = UFOPUtils.pathSplitFormat(path);
        if ("/".equals(path)) {
            return path;
        }
        if (!path.startsWith(separator)) {
            path = separator + path;
        }
        if (path.endsWith("/")) {
            int length = path.length();
            return path.substring(0, length - 1);
        }

        return path;
    }

    public String getParent() {
        if (separator.equals(this.path)) {
            return null;
        }
        if (!this.path.contains("/")) {
            return null;
        }
        int index = path.lastIndexOf(separator);
        if (index == 0) {
            return separator;
        }
        return path.substring(0, index);
    }

    public SystemFile getParentFile() {
        String parentPath = this.getParent();
        return new SystemFile(parentPath, true);
    }

    public String getName() {
        int index = path.lastIndexOf(separator);
        if (!path.contains(separator)) {
            return path;
        }
        return path.substring(index + 1);
    }

    public String getExtension() {
        return FilenameUtils.getExtension(getName());
    }

    public String getNameNotExtend() {
        return FilenameUtils.removeExtension(getName());
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isFile() {
        return !isDirectory;
    }
}

