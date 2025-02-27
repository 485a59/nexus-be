package cn.edu.nwafu.nexus.ufop.operation.preview;

import cn.edu.nwafu.nexus.common.operation.ImageOperation;
import cn.edu.nwafu.nexus.ufop.domain.ThumbImage;
import cn.edu.nwafu.nexus.ufop.exception.operation.PreviewException;
import cn.edu.nwafu.nexus.ufop.operation.preview.domain.PreviewFile;
import cn.edu.nwafu.nexus.ufop.util.CharsetUtils;
import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import cn.hutool.http.HttpUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件预览抽象类，
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Data
public abstract class Previewer {
    public ThumbImage thumbImage;

    private static String findIco(String navUrl) {
        String body = HttpUtil.createGet(navUrl).execute().toString();
        String str = body.split("favicon\\d{0,3}.ico")[0];
        int http = str.indexOf("https://", str.length() - 100);
        if (http == -1) {
            http = str.indexOf("http://", str.length() - 100);
        }
        if (http == -1) {
            //说明没有指定 走拼接逻辑
            int i = navUrl.indexOf("/", 8);//获取网址 拼接 favicon.ico
            if (i > 0) {
                navUrl = navUrl.substring(0, i);
            }
        } else {
            navUrl = str.substring(http);
        }
        return navUrl + "/favicon.ico";
    }

    protected abstract InputStream getInputStream(PreviewFile previewFile);

    public void imageThumbnailPreview(HttpServletResponse httpServletResponse, PreviewFile previewFile) {
        String fileUrl = previewFile.getFileUrl();

        if (fileUrl.startsWith("http://") || fileUrl.startsWith("https://")) {
            String[] arr = fileUrl.replace("http://", "").replace("https://", "").split("/");
            String name = arr[0];
            String icoUrl = findIco(fileUrl);

            File cacheFile = UFOPUtils.getCacheFile(UFOPUtils.getUploadFileUrl(name, "ico"));
            if (cacheFile.exists()) {
                FileInputStream fis = null;
                OutputStream outputStream = null;
                try {
                    fis = new FileInputStream(cacheFile);
                    outputStream = httpServletResponse.getOutputStream();
                    IOUtils.copy(fis, outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(fis);
                    IOUtils.closeQuietly(outputStream);
                }
            } else {
                URL url = null;
                InputStream in = null;
                OutputStream outputStream = null;
                try {
                    url = new URL(icoUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputstream = connection.getInputStream();
                    try {
                        outputStream = httpServletResponse.getOutputStream();
                        in = ImageOperation.thumbnailsImageForScale(inputstream, cacheFile, 50);
                        IOUtils.copy(in, outputStream);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        IOUtils.closeQuietly(in);
                        IOUtils.closeQuietly(inputstream);
                        IOUtils.closeQuietly(outputStream);
                        if (previewFile.getOssClient() != null) {
                            previewFile.getOssClient().shutdown();
                        }
                    }
                } catch (IOException e) {
                    log.error("MalformedURLException, url is {}", icoUrl);
                    throw new RuntimeException(e);
                }
            }

            return;
        }


        boolean isVideo = UFOPUtils.isVideoFile(FilenameUtils.getExtension(fileUrl));
        String thumbnailImgUrl = previewFile.getFileUrl();
        if (isVideo) {
            thumbnailImgUrl = fileUrl.replace("." + FilenameUtils.getExtension(fileUrl), ".jpg");
        }


        File cacheFile = UFOPUtils.getCacheFile(thumbnailImgUrl);

        if (cacheFile.exists()) {
            FileInputStream fis = null;
            OutputStream outputStream = null;
            try {
                fis = new FileInputStream(cacheFile);
                outputStream = httpServletResponse.getOutputStream();
                IOUtils.copy(fis, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(fis);
                IOUtils.closeQuietly(outputStream);
            }

        } else {
            OutputStream outputStream = null;
            InputStream in = null;
            InputStream inputstream = null;
            try {
                inputstream = getInputStream(previewFile);
            } catch (PreviewException previewException) {
                log.error(previewException.getMessage());
                return;
            }

            try {
                outputStream = httpServletResponse.getOutputStream();
                in = ImageOperation.thumbnailsImageForScale(inputstream, cacheFile, 50);
                IOUtils.copy(in, outputStream);

            } catch (IOException e) {
                log.error("IOException, url is {}", thumbnailImgUrl);
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(inputstream);
                IOUtils.closeQuietly(outputStream);
                if (previewFile.getOssClient() != null) {
                    previewFile.getOssClient().shutdown();
                }
            }


        }
    }

    public void imageOriginalPreview(HttpServletResponse httpServletResponse, PreviewFile previewFile) {

        InputStream inputStream = null;

        OutputStream outputStream = null;

        try {
            inputStream = getInputStream(previewFile);
            outputStream = httpServletResponse.getOutputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            bytes = CharsetUtils.convertTxtCharsetToUTF8(bytes, FilenameUtils.getExtension(previewFile.getFileUrl()));
            outputStream.write(bytes);
        } catch (IOException e) {
            log.error("IOException, url is {}", previewFile.getFileUrl());
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            if (previewFile.getOssClient() != null) {
                previewFile.getOssClient().shutdown();
            }
        }
    }
}
