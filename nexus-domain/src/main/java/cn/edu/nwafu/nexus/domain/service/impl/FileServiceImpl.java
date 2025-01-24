package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.common.exception.ApiException;
import cn.edu.nwafu.nexus.common.util.file.UFOPUtils;
import cn.edu.nwafu.nexus.domain.domain.SystemFile;
import cn.edu.nwafu.nexus.domain.response.FileDetailVo;
import cn.edu.nwafu.nexus.domain.service.FileService;
import cn.edu.nwafu.nexus.infrastructure.entity.File;
import cn.edu.nwafu.nexus.infrastructure.entity.Image;
import cn.edu.nwafu.nexus.infrastructure.entity.Music;
import cn.edu.nwafu.nexus.infrastructure.entity.UserFile;
import cn.edu.nwafu.nexus.infrastructure.mapper.FileMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.ImageMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.MusicMapper;
import cn.edu.nwafu.nexus.infrastructure.mapper.UserFileMapper;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Huang Z.Y.
 */
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    public static Executor executor = Executors.newFixedThreadPool(20);
    @Resource
    FileMapper fileMapper;
    @Resource
    UserFileMapper userFileMapper;
    @Resource
    UFOPFactory ufopFactory;
    @Resource
    AsyncTaskComp asyncTaskComp;
    @Resource
    MusicMapper musicMapper;
    @Resource
    ImageMapper imageMapper;
    @Resource
    FileDealComp fileDealComp;
    @Value("${ufop.storage-type}")
    private Integer storageType;

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

        Downloader downloader = ufopFactory.getDownloader(fileBean.getStorageType());
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.setFileUrl(fileBean.getFileUrl());
        InputStream inputStream = downloader.getInputStream(downloadFile);

        try {
            FileUtils.copyInputStreamToFile(inputStream, destFile);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        String extendName = userFile.getExtension();
        String unzipUrl = UFOPUtils.getTempFile(fileBean.getFileUrl()).getAbsolutePath().replace("." + extendName, "");
        List<String> fileEntryNameList = new ArrayList<>();

        try {
            fileEntryNameList = FileOperation.unzip(destFile, unzipUrl);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解压失败" + e);
            throw new ApiException(500001, "解压异常：" + e.getMessage());
        }
        if (destFile.exists()) {
            destFile.delete();
        }

        if (!fileEntryNameList.isEmpty() && unzipMode == 1) {
            UserFile qiwenDir = SystemFile.getDir(userFile.getUserId(), userFile.getFilePath(), userFile.getFileName());
            userFileMapper.insert(qiwenDir);
        }
        for (String entryName : fileEntryNameList) {
            asyncTaskComp.saveUnzipFile(userFile, fileBean, unzipMode, entryName, filePath);
        }
    }

    @Override
    public void updateFileDetail(String userFileId, String identifier, long fileSize) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        String currentTime = DateUtil.getCurrentTime();
        File fileBean = new File();
        fileBean.setIdentifier(identifier);
        fileBean.setSize(fileSize);
        fileBean.setModifyUserId(SessionUtil.getUserId());
        fileBean.setId(userFile.getFileId());
        fileMapper.updateById(fileBean);
        userFile.setUploadTime(currentTime);
        userFile.setModifyUserId(SessionUtil.getUserId());
        userFileMapper.updateById(userFile);
    }

    @Override
    public FileDetailVo getFileDetail(String userFileId) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        File file = fileMapper.selectById(userFile.getFileId());
        Music music = musicMapper.selectOne(new QueryWrapper<Music>().eq("fileId", userFile.getFileId()));
        Image image = imageMapper.selectOne(new QueryWrapper<Image>().eq("fileId", userFile.getFileId()));

        if ("mp3".equalsIgnoreCase(userFile.getExtension()) || "flac".equalsIgnoreCase(userFile.getExtension())) {
            if (music == null) {
                fileDealComp.parseMusicFile(userFile.getExtension(), file.getStorageType(), file.getUrl(), file.getId());
                music = musicMapper.selectOne(new QueryWrapper<Music>().eq("fileId", userFile.getFileId()));
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
