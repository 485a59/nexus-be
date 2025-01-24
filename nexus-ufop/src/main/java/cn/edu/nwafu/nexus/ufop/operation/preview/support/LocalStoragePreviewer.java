package cn.edu.nwafu.nexus.ufop.operation.preview.support;

import cn.edu.nwafu.nexus.ufop.domain.ThumbImage;
import cn.edu.nwafu.nexus.ufop.exception.operation.PreviewException;
import cn.edu.nwafu.nexus.ufop.operation.preview.Previewer;
import cn.edu.nwafu.nexus.ufop.operation.preview.domain.PreviewFile;
import cn.edu.nwafu.nexus.ufop.util.UFOPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * 本地文件预览实现类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class LocalStoragePreviewer extends Previewer {
    public LocalStoragePreviewer() {
    }

    public LocalStoragePreviewer(ThumbImage thumbImage) {
        setThumbImage(thumbImage);
    }

    @Override
    protected InputStream getInputStream(PreviewFile previewFile) {
        //设置文件路径
        File file = UFOPUtils.getLocalSaveFile(previewFile.getFileUrl());
        if (!file.exists()) {
            throw new PreviewException("[UFOP] Failed to get the file stream because the file path does not exist! The file path is: " + file.getAbsolutePath());
        }
        InputStream inputStream = null;
        byte[] bytes = new byte[0];
        try {
            inputStream = new FileInputStream(file);
            bytes = IOUtils.toByteArray(inputStream);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return new ByteArrayInputStream(bytes);
    }
}

