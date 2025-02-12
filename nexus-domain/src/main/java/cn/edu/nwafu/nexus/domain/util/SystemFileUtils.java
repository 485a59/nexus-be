package cn.edu.nwafu.nexus.domain.util;

import cn.edu.nwafu.nexus.common.domain.SystemFile;
import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import cn.edu.nwafu.nexus.security.util.SessionUtils;

import java.util.Date;

/**
 * 系统文件工具类。
 *
 * @author Huang Z.Y.
 */
public class SystemFileUtils {
    public static UserFile getDir(String userId, String filePath, String fileName) {
        UserFile userFile = new UserFile();
        userFile.setUserId(userId);
        userFile.setFileId(null);
        userFile.setFileName(fileName);
        userFile.setFilePath(SystemFile.formatPath(filePath));
        userFile.setExtension(null);
        userFile.setIsDir(1);
        userFile.setUploadTime(new Date());
        userFile.setCreateUserId(SessionUtils.getUserId());
        userFile.setDeleteBatchNum(null);
        return userFile;
    }

    public static UserFile getFile(String userId, String fileId, String filePath, String fileName, String extendName) {
        UserFile userFile = new UserFile();
        userFile.setUserId(userId);
        userFile.setFileId(fileId);
        userFile.setFileName(fileName);
        userFile.setFilePath(SystemFile.formatPath(filePath));
        userFile.setExtension(extendName);
        userFile.setIsDir(0);
        userFile.setUploadTime(new Date());
        userFile.setCreateUserId(SessionUtils.getUserId());
        userFile.setDeleteBatchNum(null);
        return userFile;
    }

    public static UserFile searchFileParam(UserFile userFile) {
        UserFile param = new UserFile();
        param.setFilePath(SystemFile.formatPath(userFile.getFilePath()));
        param.setFileName(userFile.getFileName());
        param.setExtension(userFile.getExtension());
        param.setUserId(userFile.getUserId());
        param.setIsDir(0);
        return param;
    }

    public static String formatLikePath(String filePath) {
        String newFilePath = filePath.replace("'", "\\'");
        newFilePath = newFilePath.replace("%", "\\%");
        newFilePath = newFilePath.replace("_", "\\_");
        return newFilePath;
    }
}
