package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.common.domain.SystemFile;
import cn.edu.nwafu.nexus.common.exception.Asserts;
import cn.edu.nwafu.nexus.domain.component.AsyncTaskHandler;
import cn.edu.nwafu.nexus.domain.component.FileHandler;
import cn.edu.nwafu.nexus.domain.service.FileApplicationService;
import cn.edu.nwafu.nexus.domain.service.FileService;
import cn.edu.nwafu.nexus.domain.service.UserFileService;
import cn.edu.nwafu.nexus.domain.util.SystemFileUtils;
import cn.edu.nwafu.nexus.domain.util.TreeNode;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.*;
import cn.edu.nwafu.nexus.infrastructure.model.entity.File;
import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.FileListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.SearchFileVo;
import cn.edu.nwafu.nexus.infrastructure.param.FileSearch;
import cn.edu.nwafu.nexus.security.util.SessionUtils;
import cn.edu.nwafu.nexus.ufop.factory.UFOPFactory;
import cn.edu.nwafu.nexus.ufop.operation.copy.Copier;
import cn.edu.nwafu.nexus.ufop.operation.copy.domain.CopyFile;
import cn.hutool.core.bean.BeanUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HighlighterEncoder;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Huang Z.Y.
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FileApplicationServiceImpl implements FileApplicationService {
    public static Executor executor = Executors.newFixedThreadPool(20);

    @Resource
    private UFOPFactory ufopFactory;
    @Resource
    private FileHandler fileHandler;
    @Resource
    private AsyncTaskHandler asyncTaskHandler;
    @Resource
    private UserFileService userFileService;
    @Resource
    private FileService fileService;
    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Value("${ufop.storage-type}")
    private Integer storageType;

    @Override
    public void createFile(CreateFileDto createFileDto) {
        String userId = SessionUtils.getUserId();
        String filePath = createFileDto.getPath();
        String fileName = createFileDto.getName();
        String extendName = createFileDto.getExtension();

        List<UserFile> userFiles = userFileService.selectSameUserFile(fileName, filePath, extendName, userId);
        if (!userFiles.isEmpty()) {
            Asserts.fail("文件名已存在");
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        String templateFilePath = getString(extendName);
        String url2 = Objects.requireNonNull(Objects.requireNonNull(
                ClassUtils.getDefaultClassLoader()).getResource("static/" + templateFilePath)).getPath();
        url2 = URLDecoder.decode(url2, StandardCharsets.UTF_8);
        try {
            File file = getFile(url2, extendName, uuid);
            boolean saveFlag = fileService.save(file);
            UserFile userFile = new UserFile();
            if (saveFlag) {
                userFile.setUserId(userId);
                userFile.setFileName(fileName);
                userFile.setFilePath(filePath);
                userFile.setIsDir(0);
                userFile.setExtension(extendName);
                userFile.setUploadTime(new Date());
                userFile.setFileId(file.getId());
                userFile.setCreateUserId(userId);
                userFile.setModifyUserId(userId);
                userFileService.save(userFile);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Asserts.fail(e.getMessage());
        }
    }

    //    @Override
//    public void createFolder(CreateFoldDto createFoldDto) {
//        String userId = SessionUtils.getUserId();
//        String filePath = createFoldDto.getPath();
//        boolean isDirExist = fileHandler.isDirExist(createFoldDto.getName(), createFoldDto.getPath(), userId);
//        if (isDirExist) {
//            Asserts.fail("存在同名文件夹");
//        }
//        UserFile userFile = SystemFileUtils.getDir(userId, filePath, createFoldDto.getName());
//        userFileService.save(userFile);
//        fileHandler.uploadESByUserFileId(userFile.getId());
//    }
    public void createFolder(CreateFolderDto createFoldDto) {
        String userId = SessionUtils.getUserId();
        String filePath = createFoldDto.getPath();
        String folderName = createFoldDto.getName();

        // 检查是否存在同名文件夹
        boolean isDirExist = fileHandler.isDirExist(folderName, filePath, userId);
        if (isDirExist) {
            Asserts.fail("存在同名文件夹");
        }

        // 处理多级目录的创建
        String[] folders = folderName.split("/");
        String currentPath = filePath;

        for (String folder : folders) {
            // 检查当前路径下是否存在同名文件夹
            isDirExist = fileHandler.isDirExist(folder, currentPath, userId);
            if (isDirExist) {
                // 如果存在同名文件夹，继续到下一级目录
                currentPath = currentPath + "/" + folder;
                continue;
            }

            // 创建当前目录
            UserFile userFile = SystemFileUtils.getDir(userId, currentPath, folder);
            userFileService.save(userFile);
            fileHandler.uploadESByUserFileId(userFile.getId());

            // 更新当前路径
            currentPath = currentPath + "/" + folder;
        }
    }

    @Override
    public List<SearchFileVo> searchFile(SearchFileDto searchFileDto) {
        try {
            int currentPage = (int) searchFileDto.getPageNum() - 1;
            int pageCount = (int) (searchFileDto.getPageNum() == 0 ? 10 : searchFileDto.getPageSize());
            SearchResponse<FileSearch> search = buildSearchQuery(searchFileDto, currentPage, pageCount);

            List<SearchFileVo> searchFileVOList = search.hits().hits().stream()
                    .map(hit -> {
                        SearchFileVo searchFileVo = new SearchFileVo();
                        BeanUtil.copyProperties(hit.source(), searchFileVo);
                        searchFileVo.setHighLight(hit.highlight());
                        asyncTaskHandler.checkESUserFileId(searchFileVo.getId());
                        return searchFileVo;
                    })
                    .collect(Collectors.toList());
            return searchFileVOList;
        } catch (IOException e) {
            log.error("文件搜索失败: {}", e.getMessage(), e);
            Asserts.fail(e.getMessage());
        }
        return List.of();
    }

    @Override
    public void renameFile(RenameFileDto renameFileDto) {
        UserFile userFile = userFileService.getById(renameFileDto.getId());
        List<UserFile> userFiles = userFileService.selectUserFileByNameAndPath(renameFileDto.getName(),
                userFile.getFilePath(), SessionUtils.getUserId());
        if (userFiles != null && !userFiles.isEmpty()) {
            Asserts.fail("同名文件已存在");
        }
        LambdaUpdateWrapper<UserFile> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(UserFile::getFileName, renameFileDto.getName())
                .set(UserFile::getUploadTime, new Date())
                .eq(UserFile::getId, renameFileDto.getId());
        userFileService.update(lambdaUpdateWrapper);
        if (userFile.getIsDir() == 1) {
            List<UserFile> list = userFileService.selectUserFileByLikeRightFilePath(new SystemFile(userFile.getFilePath(),
                    userFile.getFileName(), true).getPath(), SessionUtils.getUserId());
            for (UserFile newUserFile : list) {
                String escapedPattern = Pattern.quote(new SystemFile(userFile.getFilePath(), userFile.getFileName(), userFile.getIsDir() == 1).getPath());
                newUserFile.setFilePath(newUserFile.getFilePath().replaceFirst(escapedPattern,
                        new SystemFile(userFile.getFilePath(), renameFileDto.getName(), userFile.getIsDir() == 1).getPath()));
                userFileService.updateById(newUserFile);
            }
        }
        fileHandler.uploadESByUserFileId(renameFileDto.getId());
    }

    @Override
    public List<FileListVo> getFileList(FileListDto fileListDto, Long pageSize, Long pageNum) {
        List<FileListVo> fileList;
        if (fileListDto.getType().equals(0)) {
            fileList = userFileService.userFileList(null, fileListDto.getPath(), pageNum, pageSize);
        } else {
            fileList = userFileService.getFileByFileType(fileListDto.getType(), pageNum, pageNum,
                    SessionUtils.getUserId());
        }
        return fileList;
    }

    @Override
    public void batchDeleteFiles(BatchDeleteFileDto batchDeleteFileDto) {
        List<String> userFileIdList = batchDeleteFileDto.getIds();
        userFileService.update(new UpdateWrapper<UserFile>().set("delete_time",
                new Date()).in("id", Collections.singletonList(userFileIdList)));
        for (String userFileId : userFileIdList) {
            executor.execute(() -> {
                userFileService.deleteUserFile(userFileId, SessionUtils.getUserId());
            });
            fileHandler.deleteESByUserFileId(userFileId);
        }
    }

    @Override
    public void copyFile(CopyFileDto copyFileDto) {
        String userId = SessionUtils.getUserId();
        String filePath = copyFileDto.getPath();
        List<String> userFileIds = copyFileDto.getIds();
        for (String userFileId : userFileIds) {
            UserFile userFile = userFileService.getById(userFileId);
            String oldFilePath = userFile.getFilePath();
            String fileName = userFile.getFileName();
            if (userFile.isDirectory()) {
                SystemFile systemFileFie = new SystemFile(oldFilePath, fileName, true);
                if (filePath.startsWith(systemFileFie.getPath() + SystemFile.separator)
                        || filePath.equals(systemFileFie.getPath())) {
                    Asserts.fail("原路径与目标路径冲突，不能复制");
                }
            }
            userFileService.userFileCopy(SessionUtils.getUserId(), userFileId, filePath);
            fileHandler.deleteRepeatSubDirFile(filePath, userId);
        }
    }

    @Override
    public void moveFile(MoveFileDto moveFileDto) {
        UserFile userFile = userFileService.getById(moveFileDto.getId());
        String oldFilePath = userFile.getFilePath();
        String newFilePath = moveFileDto.getPath();
        String fileName = userFile.getFileName();
        String extendName = userFile.getExtension();
        if (StringUtil.isEmpty(extendName)) {
            SystemFile systemFile = new SystemFile(oldFilePath, fileName, true);
            if (newFilePath.startsWith(systemFile.getPath() + SystemFile.separator)
                    || newFilePath.equals(systemFile.getPath())) {
                Asserts.fail("原路径与目标路径冲突，不能移动");
            }
        }

        userFileService.updateFilepathByUserFileId(moveFileDto.getId(), newFilePath, SessionUtils.getUserId());
        fileHandler.deleteRepeatSubDirFile(newFilePath, SessionUtils.getUserId());

    }

    @Override
    public void batchMoveFile(BatchMoveFileDto batchMoveFileDto) {
        String newFilePath = batchMoveFileDto.getPath();
        List<String> userFileIds = batchMoveFileDto.getIds();

        for (String userFileId : userFileIds) {
            UserFile userFile = userFileService.getById(userFileId);
            if (StringUtil.isEmpty(userFile.getExtension())) {
                SystemFile systemFile = new SystemFile(userFile.getFilePath(), userFile.getFileName(), true);
                if (newFilePath.startsWith(systemFile.getPath() + SystemFile.separator) || newFilePath.equals(systemFile.getPath())) {
                    Asserts.fail("原路径与目标路径冲突，不能移动");
                }
            }
            userFileService.updateFilepathByUserFileId(userFile.getId(), newFilePath, SessionUtils.getUserId());
        }
    }

    @Override
    public TreeNode getFileTree() {
        List<UserFile> userFileList = userFileService.selectFilePathTreeByUserId(SessionUtils.getUserId());
        TreeNode resultTreeNode = new TreeNode();
        resultTreeNode.setLabel(SystemFile.separator);
        resultTreeNode.setId(0L);
        long id = 1;
        for (UserFile userFile : userFileList) {
            SystemFile systemFile = new SystemFile(userFile.getFilePath(), userFile.getFileName(), false);
            String filePath = systemFile.getPath();
            Queue<String> queue = new LinkedList<>();
            String[] strArr = filePath.split(SystemFile.separator);
            for (String s : strArr) {
                if (!"".equals(s) && s != null) {
                    queue.add(s);
                }
            }
            if (queue.isEmpty()) {
                continue;
            }
            resultTreeNode = fileHandler.insertTreeNode(resultTreeNode, id++, SystemFile.separator, queue);
        }
        List<TreeNode> treeNodeList = resultTreeNode.getChildren();
        treeNodeList.sort((o1, o2) -> {
            long i = o1.getId() - o2.getId();
            return (int) i;
        });
        return resultTreeNode;
    }

    @Override
    public void updateFile(UpdateFileDto updateFileDto) {
        UserFile userFile = userFileService.getById(updateFileDto.getId());
        File file = fileService.getById(userFile.getFileId());
        Long pointCount = fileService.getFilePointCount(userFile.getFileId());
        String fileUrl = file.getUrl();
        if (pointCount > 1) {
            fileUrl = fileHandler.copyFile(file, userFile);
        }
        String content = updateFileDto.getContent();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        try {
            int fileSize = byteArrayInputStream.available();
            fileHandler.saveFileInputStream(file.getStorageType(), fileUrl, byteArrayInputStream);
            String md5Str = fileHandler.getIdentifierByFile(fileUrl, file.getStorageType());

            fileService.updateFileDetail(userFile.getId(), md5Str, fileSize);
        } catch (Exception e) {
            Asserts.fail(e.getMessage());
        } finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @NotNull
    private String getString(String extendName) {
        String templateFilePath = "";
        if ("docx".equals(extendName)) {
            templateFilePath = "template/Word.docx";
        } else if ("xlsx".equals(extendName)) {
            templateFilePath = "template/Excel.xlsx";
        } else if ("pptx".equals(extendName)) {
            templateFilePath = "template/PowerPoint.pptx";
        } else if ("txt".equals(extendName)) {
            templateFilePath = "template/Text.txt";
        } else if ("drawio".equals(extendName)) {
            templateFilePath = "template/Drawio.drawio";
        }
        return templateFilePath;
    }

    @NotNull
    private File getFile(String url, String extendName, String uuid) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(url);
        Copier copier = ufopFactory.getCopier();
        String userId = SessionUtils.getUserId();
        CopyFile copyFile = new CopyFile();
        copyFile.setExtension(extendName);
        String fileUrl = copier.copy(fileInputStream, copyFile);
        File file = new File();
        file.setSize(0L);
        file.setUrl(fileUrl);
        file.setStorageType(storageType);
        file.setIdentifier(uuid);
        file.setCreateUserId(userId);
        file.setModifyUserId(userId);
        file.setStatus(1);
        return file;
    }

    private SearchResponse<FileSearch> buildSearchQuery(SearchFileDto searchFileDto, int currentPage, int pageCount) throws IOException {
        return elasticsearchClient.search(s -> s
                        .index("filesearch")
                        .query(_1 -> _1
                                .bool(_2 -> _2
                                        .must(buildSearchConditions(searchFileDto.getName()))
                                        .must(_3 -> _3
                                                .term(_4 -> _4
                                                        .field("userId")
                                                        .value(SessionUtils.getUserId())))
                                ))
                        .from(currentPage)
                        .size(pageCount)
                        .highlight(h -> h
                                .fields("fileName", f -> f
                                        .type("plain")
                                        .preTags("<span class='keyword'>")
                                        .postTags("</span>"))
                                .encoder(HighlighterEncoder.Html)),
                FileSearch.class);
    }

    private Query buildSearchConditions(String searchTerm) {
        return Query.of(q -> q
                .bool(b -> b
                        .should(s -> s.match(m -> m.field("fileName").query(searchTerm)))
                        .should(s -> s.wildcard(w -> w.field("fileName").wildcard("*" + searchTerm + "*")))
                        .should(s -> s.match(m -> m.field("content").query(searchTerm)))
                        .should(s -> s.wildcard(w -> w.field("content").wildcard("*" + searchTerm + "*")))
                )
        );
    }
}
