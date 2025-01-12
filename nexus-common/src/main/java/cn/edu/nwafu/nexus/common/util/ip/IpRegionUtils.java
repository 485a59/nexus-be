package cn.edu.nwafu.nexus.common.util.ip;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

/**
 * @author Huang Z.Y.
 */
@UtilityClass
public class IpRegionUtils {
    public IpRegion getIpRegion(String ip) {
        if (StrUtil.isEmpty(ip)) {
            return new IpRegion();
        }

        if (IpUtils.isInnerIp(ip)) {
            return new IpRegion("", "内网IP");
        }

        IpRegion ipRegionOffline = OfflineIpRegionUtils.getIpRegion(ip);
        if (ipRegionOffline != null) {
            return ipRegionOffline;
        }

        IpRegion ipRegionOnline = OnlineIpRegionUtils.getIpRegion(ip);
        if (ipRegionOnline != null) {
            return ipRegionOnline;
        }

        return new IpRegion();
    }

    public static String getBriefLocationByIp(String ip) {
        return getIpRegion(ip).briefLocation();
    }
}
