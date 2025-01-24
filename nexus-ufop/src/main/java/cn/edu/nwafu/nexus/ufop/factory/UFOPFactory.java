package cn.edu.nwafu.nexus.ufop.factory;

import cn.edu.nwafu.nexus.ufop.autoconfiguration.UFOPProperties;
import cn.edu.nwafu.nexus.ufop.config.AliyunConfig;
import cn.edu.nwafu.nexus.ufop.config.MinioConfig;
import cn.edu.nwafu.nexus.ufop.config.QiniuyunConfig;
import cn.edu.nwafu.nexus.ufop.constant.StorageTypeEnum;
import cn.edu.nwafu.nexus.ufop.domain.ThumbImage;
import cn.edu.nwafu.nexus.ufop.operation.copy.Copier;
import cn.edu.nwafu.nexus.ufop.operation.copy.support.*;
import cn.edu.nwafu.nexus.ufop.operation.delete.Deleter;
import cn.edu.nwafu.nexus.ufop.operation.delete.support.*;
import cn.edu.nwafu.nexus.ufop.operation.download.Downloader;
import cn.edu.nwafu.nexus.ufop.operation.download.support.*;
import cn.edu.nwafu.nexus.ufop.operation.preview.Previewer;
import cn.edu.nwafu.nexus.ufop.operation.preview.support.*;
import cn.edu.nwafu.nexus.ufop.operation.read.Reader;
import cn.edu.nwafu.nexus.ufop.operation.read.support.*;
import cn.edu.nwafu.nexus.ufop.operation.upload.Uploader;
import cn.edu.nwafu.nexus.ufop.operation.upload.support.*;
import cn.edu.nwafu.nexus.ufop.operation.write.Writer;
import cn.edu.nwafu.nexus.ufop.operation.write.support.*;

import javax.annotation.Resource;

/**
 * UFOPFactory 类是一个工厂类，用于根据不同的存储类型创建各种文件操作对象，
 * 支持多种存储方式，包括本地存储、阿里云 OSS、FastDFS、MinIO 和七牛云 Kodo 等。
 * 通过该工厂类，可以方便地获取文件上传、下载、删除、读取、写入、预览和复制等操作的实例，
 * 实现了存储操作的解耦和可扩展性，便于在不同存储系统之间进行切换。
 *
 * @author Huang Z.Y.
 */
public class UFOPFactory {
    private String storageType;
    private AliyunConfig aliyunConfig;
    private ThumbImage thumbImage;
    private MinioConfig minioConfig;
    private QiniuyunConfig qiniuyunConfig;
    @Resource
    private FastDfsCopier fastDFSCopier;
    @Resource
    private FastDfsUploader fastDFSUploader;
    @Resource
    private FastDfsDownloader fastDFSDownloader;
    @Resource
    private FastDfsDeleter fastDFSDeleter;
    @Resource
    private FastDfsReader fastDFSReader;
    @Resource
    private FastDfsPreviewer fastDFSPreviewer;
    @Resource
    private FastDFSWriter fastDFSWriter;
    @Resource
    private AliyunOssUploader aliyunOSSUploader;
    @Resource
    private MinioUploader minioUploader;
    @Resource
    private QiniuyunKodoUploader qiniuyunKodoUploader;

    public UFOPFactory() {
    }

    public UFOPFactory(UFOPProperties ufopProperties) {
        this.storageType = ufopProperties.getStorageType();
        this.aliyunConfig = ufopProperties.getAliyun();
        this.thumbImage = ufopProperties.getThumbImage();
        this.minioConfig = ufopProperties.getMinio();
        this.qiniuyunConfig = ufopProperties.getQiniuyun();
    }

    public Uploader getUploader() {

        int type = Integer.parseInt(storageType);
        Uploader uploader = null;
        if (StorageTypeEnum.LOCAL.getCode() == type) {
            uploader = new LocalStorageUploader();
        } else if (StorageTypeEnum.ALIYUN_OSS.getCode() == type) {
            uploader = aliyunOSSUploader;
        } else if (StorageTypeEnum.FAST_DFS.getCode() == type) {
            uploader = fastDFSUploader;
        } else if (StorageTypeEnum.MINIO.getCode() == type) {
            uploader = minioUploader;
        } else if (StorageTypeEnum.QINIUYUN_KODO.getCode() == type) {
            uploader = qiniuyunKodoUploader;
        }
        return uploader;
    }


    public Downloader getDownloader(int storageType) {
        Downloader downloader = null;
        if (StorageTypeEnum.LOCAL.getCode() == storageType) {
            downloader = new LocalStorageDownloader();
        } else if (StorageTypeEnum.ALIYUN_OSS.getCode() == storageType) {
            downloader = new AliyunOssDownloader(aliyunConfig);
        } else if (StorageTypeEnum.FAST_DFS.getCode() == storageType) {
            downloader = fastDFSDownloader;
        } else if (StorageTypeEnum.MINIO.getCode() == storageType) {
            downloader = new MinioDownloader(minioConfig);
        } else if (StorageTypeEnum.QINIUYUN_KODO.getCode() == storageType) {
            downloader = new QiniuyunKodoDownloader(qiniuyunConfig);
        }
        return downloader;
    }


    public Deleter getDeleter(int storageType) {
        Deleter deleter = null;
        if (StorageTypeEnum.LOCAL.getCode() == storageType) {
            deleter = new LocalStorageDeleter();
        } else if (StorageTypeEnum.ALIYUN_OSS.getCode() == storageType) {
            deleter = new AliyunOssDeleter(aliyunConfig);
        } else if (StorageTypeEnum.FAST_DFS.getCode() == storageType) {
            deleter = fastDFSDeleter;
        } else if (StorageTypeEnum.MINIO.getCode() == storageType) {
            deleter = new MinioDeleter(minioConfig);
        } else if (StorageTypeEnum.QINIUYUN_KODO.getCode() == storageType) {
            deleter = new QiniuyunKodoDeleter(qiniuyunConfig);
        }
        return deleter;
    }

    public Reader getReader(int storageType) {
        Reader reader = null;
        if (StorageTypeEnum.LOCAL.getCode() == storageType) {
            reader = new LocalStorageReader();
        } else if (StorageTypeEnum.ALIYUN_OSS.getCode() == storageType) {
            reader = new AliyunOssReader(aliyunConfig);
        } else if (StorageTypeEnum.FAST_DFS.getCode() == storageType) {
            reader = fastDFSReader;
        } else if (StorageTypeEnum.MINIO.getCode() == storageType) {
            reader = new MinioReader(minioConfig);
        } else if (StorageTypeEnum.QINIUYUN_KODO.getCode() == storageType) {
            reader = new QiniuyunKodoReader(qiniuyunConfig);
        }
        return reader;
    }

    public Writer getWriter(int storageType) {
        Writer writer = null;
        if (StorageTypeEnum.LOCAL.getCode() == storageType) {
            writer = new LocalStorageWriter();
        } else if (StorageTypeEnum.ALIYUN_OSS.getCode() == storageType) {
            writer = new AliyunOssWriter(aliyunConfig);
        } else if (StorageTypeEnum.FAST_DFS.getCode() == storageType) {
            writer = fastDFSWriter;
        } else if (StorageTypeEnum.MINIO.getCode() == storageType) {
            writer = new MinioWriter(minioConfig);
        } else if (StorageTypeEnum.QINIUYUN_KODO.getCode() == storageType) {
            writer = new QiniuyunKodoWriter(qiniuyunConfig);
        }
        return writer;
    }

    public Previewer getPreviewer(int storageType) {
        Previewer previewer = null;
        if (StorageTypeEnum.LOCAL.getCode() == storageType) {
            previewer = new LocalStoragePreviewer(thumbImage);
        } else if (StorageTypeEnum.ALIYUN_OSS.getCode() == storageType) {
            previewer = new AliyunOssPreviewer(aliyunConfig, thumbImage);
        } else if (StorageTypeEnum.FAST_DFS.getCode() == storageType) {
            previewer = fastDFSPreviewer;
        } else if (StorageTypeEnum.MINIO.getCode() == storageType) {
            previewer = new MinioPreviewer(minioConfig, thumbImage);
        } else if (StorageTypeEnum.QINIUYUN_KODO.getCode() == storageType) {
            previewer = new QiniuyunKodoPreviewer(qiniuyunConfig, thumbImage);
        }
        return previewer;
    }

    public Copier getCopier() {
        int type = Integer.parseInt(storageType);
        Copier copier = null;
        if (StorageTypeEnum.LOCAL.getCode() == type) {
            copier = new LocalStorageCopier();
        } else if (StorageTypeEnum.ALIYUN_OSS.getCode() == type) {
            copier = new AliyunOssCopier(aliyunConfig);
        } else if (StorageTypeEnum.FAST_DFS.getCode() == type) {
            copier = fastDFSCopier;
        } else if (StorageTypeEnum.MINIO.getCode() == type) {
            copier = new MinioCopier(minioConfig);
        } else if (StorageTypeEnum.QINIUYUN_KODO.getCode() == type) {
            copier = new QiniuyunKodoCopier(qiniuyunConfig);
        }
        return copier;
    }
}

