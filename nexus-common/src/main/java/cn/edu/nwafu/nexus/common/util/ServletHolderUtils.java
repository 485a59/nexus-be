package cn.edu.nwafu.nexus.common.util;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 客户端工具类。
 *
 * @author Huang Z.Y.
 */
@Slf4j
@UtilityClass
public class ServletHolderUtils {
    /**
     * 获取请求。
     */
    public HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取响应。
     */
    public HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }


    public ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 将字符串渲染到客户端。
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     */
    public void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            log.error("返回response失败", e);
        }
    }

    /**
     * 获取仅含有项目根路径的 URL。
     * 比如 host:port/root/a/b，返回 host:port/root
     */
    public String getContextUrl() {
        HttpServletRequest request = getRequest();
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        String strip = StrUtil.strip(url, null, request.getRequestURI());
        return strip + contextPath;
    }
}
