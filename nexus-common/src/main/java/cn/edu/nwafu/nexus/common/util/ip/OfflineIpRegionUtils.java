package cn.edu.nwafu.nexus.common.util.ip;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@UtilityClass
public class OfflineIpRegionUtils {
    private static Searcher searcher;

    static {
        InputStream resourceAsStream = OfflineIpRegionUtils.class.getResourceAsStream("/ip2region.xdb");

        byte[] bytes = null;
        try {
            assert resourceAsStream != null;
            bytes = new byte[resourceAsStream.available()];
            IOUtils.read(resourceAsStream, bytes);
        } catch (IOException e) {
            log.error("读取本地Ip文件失败", e);
        }

        try {
            searcher = Searcher.newWithBuffer(bytes);
        } catch (Exception e) {
            log.error("构建本地Ip缓存失败", e);
        }

    }

    public IpRegion getIpRegion(String ip) {
        try {
            if (StrUtil.isBlank(ip) || IpUtils.isValidIpv6(ip)
                    || !IpUtils.isValidIpv4(ip)) {
                return null;
            }

            String rawRegion = searcher.search(ip);

            if (StrUtil.isEmpty(rawRegion)) {
                return null;
            }

            String[] split = rawRegion.split("\\|");
            return new IpRegion(split[0], split[1], split[2], split[3], split[4]);

        } catch (Exception e) {
            log.error("获取IP地理位置失败", e);
        }
        return null;
    }
}
