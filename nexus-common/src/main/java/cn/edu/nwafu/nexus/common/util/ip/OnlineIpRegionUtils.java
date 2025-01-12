package cn.edu.nwafu.nexus.common.util.ip;

import cn.edu.nwafu.nexus.common.config.NexusConfig;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@UtilityClass
public class OnlineIpRegionUtils {
    public static final String ADDRESS_QUERY_SITE = "http://whois.pconline.com.cn/ipJson.jsp";


    public static IpRegion getIpRegion(String ip) {
        if (StrUtil.isBlank(ip) || IpUtils.isValidIpv6(ip) || !IpUtils.isValidIpv4(ip)) {
            return null;
        }

        if (NexusConfig.isAddressEnabled()) {
            try {
                String rspStr = HttpUtil.get(ADDRESS_QUERY_SITE + "?ip=" + ip + "&json=true",
                        CharsetUtil.CHARSET_GBK);

                if (StrUtil.isEmpty(rspStr)) {
                    log.error("获取地理位置异常 {}", ip);
                    return null;
                }

                String province = JacksonUtils.getAsString(rspStr, "pro");
                String city = JacksonUtils.getAsString(rspStr, "city");
                return new IpRegion(province, city);
            } catch (Exception e) {
                log.error("获取地理位置异常 {}", ip);
            }
        }
        return null;
    }
}
