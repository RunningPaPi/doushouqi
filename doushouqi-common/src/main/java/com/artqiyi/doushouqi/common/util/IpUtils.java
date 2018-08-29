package com.artqiyi.doushouqi.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * IP地址工具类
 * 
 * @author woosam
 *
 */
public class IpUtils {
    
    /**
     * 
     * 获取客户端ip地址
     * 
     * @param request
     * @return String
     */
    public static String getClientIp(HttpServletRequest request) {
        
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        return ip;
        
    }

}
