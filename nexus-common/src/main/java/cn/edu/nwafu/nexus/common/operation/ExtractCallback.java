package cn.edu.nwafu.nexus.common.operation;

import lombok.extern.slf4j.Slf4j;
import net.sf.sevenzipjbinding.*;

import java.io.*;

/**
 * @author Huang Z.Y.
 */
@Slf4j
public class ExtractCallback implements IArchiveExtractCallback {
    private int index;
    private IInArchive inArchive;
    private String ourDir;

    public ExtractCallback(IInArchive inArchive, String ourDir) {
        this.inArchive = inArchive;
        this.ourDir = ourDir;
    }

    public static boolean save2File(File file, byte[] msg) {
        OutputStream fos = null;
        try {
            File parent = file.getParentFile();
            if ((!parent.exists()) && (!parent.mkdirs())) {
                return false;
            }
            fos = new FileOutputStream(file, true);
            fos.write(msg);
            fos.flush();
            return true;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return false;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void setCompleted(long arg0) throws SevenZipException {
    }

    @Override
    public void setTotal(long arg0) throws SevenZipException {
    }

    @Override
    public ISequentialOutStream getStream(int index, ExtractAskMode extractAskMode) throws SevenZipException {
        this.index = index;
        final String path = (String) inArchive.getProperty(index, PropID.PATH);
        final boolean isFolder = (boolean) inArchive.getProperty(index, PropID.IS_FOLDER);
        return data -> {
            try {
                if (!isFolder) {
                    File file = new File(ourDir + File.separator + path);
                    save2File(file, data);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return data.length;
        };
    }

    @Override
    public void prepareOperation(ExtractAskMode arg0) throws SevenZipException {
    }

    @Override
    public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {
    }
}

