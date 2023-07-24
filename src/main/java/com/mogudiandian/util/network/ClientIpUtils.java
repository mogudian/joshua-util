package com.mogudiandian.util.network;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * 客户端IP地址获取工具
 * @author Joshua Sun
 */
public final class ClientIpUtils {

    /**
     * 代理可能加入的header
     */
    private static final List<String> POSSIBLE_PROXY_HEADER_KEYS;

    static {
        POSSIBLE_PROXY_HEADER_KEYS = new LinkedList<>();
        POSSIBLE_PROXY_HEADER_KEYS.add("X-Forwarded-For");
        POSSIBLE_PROXY_HEADER_KEYS.add("Proxy-Client-IP");
        POSSIBLE_PROXY_HEADER_KEYS.add("WL-Proxy-Client-IP");
        POSSIBLE_PROXY_HEADER_KEYS.add("HTTP_X_FORWARDED_FOR");
        POSSIBLE_PROXY_HEADER_KEYS.add("HTTP_X_FORWARDED");
        POSSIBLE_PROXY_HEADER_KEYS.add("HTTP_X_CLUSTER_CLIENT_IP");
        POSSIBLE_PROXY_HEADER_KEYS.add("HTTP_CLIENT_IP");
        POSSIBLE_PROXY_HEADER_KEYS.add("HTTP_FORWARDED_FOR");
        POSSIBLE_PROXY_HEADER_KEYS.add("HTTP_FORWARDED");
        POSSIBLE_PROXY_HEADER_KEYS.add("HTTP_VIA");
        POSSIBLE_PROXY_HEADER_KEYS.add("REMOTE_ADDR");
    }

    private ClientIpUtils() {}

    /**
     * 获取客户端IP
     * @param request servlet的request
     * @return 客户端IP 兜底是 request.getRemoteAddr()
     */
    public static String getClientIp(HttpServletRequest request) {
        String realIp = request.getRemoteAddr();
        for (String key : POSSIBLE_PROXY_HEADER_KEYS) {
            String header = request.getHeader(key);
            if (StringUtils.isNotEmpty(header) && !"unknown".equalsIgnoreCase(header)) {
                realIp = header;
                break;
            }
        }
        int index = realIp.indexOf(',');
        if (index >= 0) {
            realIp = realIp.substring(0, index);
        }
        return realIp;
    }


}
