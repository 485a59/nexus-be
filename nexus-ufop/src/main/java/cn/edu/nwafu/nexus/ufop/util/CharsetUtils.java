package cn.edu.nwafu.nexus.ufop.util;

import cn.edu.nwafu.nexus.common.util.UFOPUtils;
import cn.edu.nwafu.nexus.ufop.exception.UFOPException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 字符集工具类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class CharsetUtils {

    public static byte[] convertTxtCharsetToGBK(byte[] bytes, String extendName) {

        if (Arrays.asList(UFOPUtils.TXT_FILE).contains(extendName)) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            try {
                String str = new String(bytes, getFileCharsetName(byteArrayInputStream));
                return str.getBytes("GBK");
            } catch (IOException e) {
                throw new UFOPException(e);
            }
        }
        return bytes;
    }

    public static byte[] convertTxtCharsetToUTF8(byte[] bytes, String extendName) {

        if (Arrays.asList(UFOPUtils.TXT_FILE).contains(extendName)) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            try {
                String str = new String(bytes, getFileCharsetName(byteArrayInputStream));
                return str.getBytes(StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new UFOPException(e);
            }
        }
        return bytes;
    }


    public static String getFileCharsetName(InputStream inputStream) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset; // 文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; // 文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; // 文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; // 文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {// 双字节 (0xC0 - 0xDF)
                            // (0x80 - 0xBF),也可能在 GB 编码内
                        } else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) { // 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                            }
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return charset;
    }
}

