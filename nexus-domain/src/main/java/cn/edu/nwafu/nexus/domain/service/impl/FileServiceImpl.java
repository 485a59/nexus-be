package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.common.exception.ApiException;
import cn.edu.nwafu.nexus.common.operation.FileOperation;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import cn.edu.nwafu.nexus.domain.component.AsyncTaskHandler;
import cn.edu.nwafu.nexus.domain.response.FileDetailVo;
import cn.edu.nwafu.nexus.domain.service.FileService;
import cn.edu.nwafu.nexus.domain.util.SystemFileUtils;
import cn.edu.nwafu.nexus.infrastructure.mapper.FileMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.ImageMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.MusicMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.UserFileMapper;
import cn.edu.nwafu.nexus.infrastructure.model.entity.File;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Image;
import cn.edu.nwafu.nexus.infrastructure.model.entity.Music;
import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import cn.edu.nwafu.nexus.security.util.SessionUtils;
import cn.edu.nwafu.nexus.ufop.factory.UFOPFactory;
import cn.edu.nwafu.nexus.ufop.operation.download.Downloader;
import cn.edu.nwafu.nexus.ufop.operation.download.domain.DownloadFile;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Huang Z.Y.
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    public static Executor executor = Executors.newFixedThreadPool(20);
    @Resource
    FileMapper fileMapper;
    @Resource
    UserFileMapper userFileMapper;
    @Resource
    UFOPFactory ufopFactory;
    @Resource
    @Lazy
    AsyncTaskHandler asyncTaskHandler;
    @Resource
    MusicMapper musicMapper;
    @Resource
    ImageMapper imageMapper;

    @Override
    public Long getFilePointCount(String fileId) {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getFileId, fileId);
        return userFileMapper.selectCount(lambdaQueryWrapper);
    }

    @Override
    public void unzipFile(String userFileId, int unzipMode, String filePath) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        File file = fileMapper.selectById(userFile.getFileId());
        java.io.File destFile = new java.io.File(UFOPUtils.getStaticPath() + "temp" + java.io.File.separator + file.getUrl());

        Downloader downloader = ufopFactory.getDownloader(file.getStorageType());
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.setFileUrl(file.getUrl());
        InputStream inputStream = downloader.getInputStream(downloadFile);

        try {
            FileUtils.copyInputStreamToFile(inputStream, destFile);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        String extendName = userFile.getExtension();
        String unzipUrl = UFOPUtils.getTempFile(file.getUrl()).getAbsolutePath().replace("." + extendName, "");
        List<String> fileEntryNameList = new ArrayList<>();

        try {
            fileEntryNameList = FileOperation.unzip(destFile, unzipUrl);
        } catch (Exception e) {
            log.error("解压失败" + e);
            throw new ApiException("解压异常：" + e.getMessage());
        }
        if (destFile.exists()) {
            boolean success = destFile.delete();
            if (!success) {
                log.error("文件" + destFile.getName() + "删除失败");
            }
        }

        if (!fileEntryNameList.isEmpty() && unzipMode == 1) {
            UserFile dir = SystemFileUtils.getDir(userFile.getUserId(), userFile.getFilePath(), userFile.getFileName());
            userFileMapper.insert(dir);
        }
        for (String entryName : fileEntryNameList) {
            asyncTaskHandler.saveUnzipFile(userFile, file, unzipMode, entryName, filePath);
        }
    }

    @Override
    public void updateFileDetail(String userFileId, String identifier, long fileSize) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        File file = new File();
        file.setIdentifier(identifier);
        file.setSize(fileSize);
        file.setModifyUserId(SessionUtils.getUserId());
        file.setId(userFile.getFileId());
        fileMapper.updateById(file);
        userFile.setUploadTime(new Date());
        userFile.setModifyUserId(SessionUtils.getUserId());
        userFileMapper.updateById(userFile);
    }

    @Override
    public FileDetailVo getFileDetail(String userFileId) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        File file = fileMapper.selectById(userFile.getFileId());
        Music music = musicMapper.selectOne(new LambdaQueryWrapper<Music>().eq(Music::getFileId, userFile.getFileId()));
        Image image = imageMapper.selectOne(new LambdaQueryWrapper<Image>().eq(Image::getFileId, userFile.getFileId()));

        if ("mp3".equalsIgnoreCase(userFile.getExtension()) || "flac".equalsIgnoreCase(userFile.getExtension())) {
            if (music == null) {
//                fileHandler.parseMusicFile(userFile.getExtension(), file.getStorageType(), file.getUrl(), file.getId());
                music = musicMapper.selectOne(new LambdaQueryWrapper<Music>().eq(Music::getFileId, userFile.getFileId()));
            }
        }

        FileDetailVo fileDetailVo = new FileDetailVo();
        BeanUtil.copyProperties(userFile, fileDetailVo);
        BeanUtil.copyProperties(file, fileDetailVo);
        fileDetailVo.setMusic(music);
        fileDetailVo.setImage(image);
        return fileDetailVo;
    }
}
