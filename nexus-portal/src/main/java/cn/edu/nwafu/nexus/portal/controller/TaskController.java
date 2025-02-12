package cn.edu.nwafu.nexus.portal.controller;

import cn.edu.nwafu.nexus.common.domain.SystemFile;
import cn.edu.nwafu.nexus.domain.component.FileHandler;
import cn.edu.nwafu.nexus.domain.service.ShareFileService;
import cn.edu.nwafu.nexus.domain.service.UserFileService;
import cn.edu.nwafu.nexus.infrastructure.model.entity.ShareFile;
import cn.edu.nwafu.nexus.infrastructure.model.entity.UserFile;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Controller
public class TaskController {
    @Resource
    ShareFileService shareFileService;
    @Resource
    private UserFileService userFileService;
    @Resource
    private FileHandler fileHandler;
    @Resource
    private ElasticsearchClient elasticsearchClient;


    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void updateElasticSearch() {
        List<UserFile> userfileList = userFileService.list(new QueryWrapper<UserFile>().eq("deleteFlag", 0));
        for (int i = 0; i < userfileList.size(); i++) {
            try {
                SystemFile ufopFile = new SystemFile(userfileList.get(i).getFilePath(), userfileList.get(i).getFileName(), userfileList.get(i).getIsDir() == 1);
                fileHandler.restoreParentFilePath(ufopFile, userfileList.get(i).getUserId());
                if (i % 1000 == 0 || i == userfileList.size() - 1) {
                    log.info("目录健康检查进度：{}/{}", i + 1, userfileList.size());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        userfileList = userFileService.list(new QueryWrapper<UserFile>().eq("deleteFlag", 0));
        for (UserFile userFile : userfileList) {
            fileHandler.uploadESByUserFileId(userFile.getId());
        }

    }

    @Scheduled(fixedRate = Long.MAX_VALUE)
    public void updateFilePath() {
        List<UserFile> list = userFileService.list();
        for (UserFile userFile : list) {
            try {
                String path = SystemFile.formatPath(userFile.getFilePath());
                if (!userFile.getFilePath().equals(path)) {
                    userFile.setFilePath(path);
                    userFileService.updateById(userFile);
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    @Scheduled(fixedRate = Long.MAX_VALUE)
    public void updateShareFilePath() {
        List<ShareFile> list = shareFileService.list();
        for (ShareFile shareFile : list) {
            try {
                String path = SystemFile.formatPath(shareFile.getShareFilePath());
                shareFile.setShareFilePath(path);
                shareFileService.updateById(shareFile);
            } catch (Exception e) {
                // ignore
            }
        }
    }
}
