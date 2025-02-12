package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.domain.util.TreeNode;
import cn.edu.nwafu.nexus.infrastructure.model.dto.file.*;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.FileListVo;
import cn.edu.nwafu.nexus.infrastructure.model.vo.file.SearchFileVo;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public interface FileApplicationService {
    void createFile(CreateFileDto createFileDto);

    void createFolder(CreateFolderDto createFoldDto);

    List<SearchFileVo> searchFile(SearchFileDto searchFileDto);

    void renameFile(RenameFileDto renameFileDto);

    List<FileListVo> getFileList(FileListDto fileListDto, Long pageSize, Long pageNum);

    void batchDeleteFiles(BatchDeleteFileDto batchDeleteFileDto);

    void copyFile(CopyFileDto copyFileDto);

    void moveFile(MoveFileDto moveFileDto);

    void batchMoveFile(BatchMoveFileDto batchMoveFileDto);

    TreeNode getFileTree();

    void updateFile(UpdateFileDto updateFileDto);

}
