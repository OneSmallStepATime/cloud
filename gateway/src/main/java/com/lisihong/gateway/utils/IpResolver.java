package com.lisihong.gateway.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;

public record IpResolver() {

    public static String getIpAddress(ServerHttpRequest request) {
        // 根据 HttpHeaders 获取 请求 IP地址
        String ip = request.getHeaders().getFirst("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("x-forwarded-for");
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                if (ip.contains(",")) {
                    ip = ip.split(",")[0];
                }
            }
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("X-Real-IP");
        }
        //兼容k8s集群获取ip
        if ((ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
                && request.getRemoteAddress() != null &&
                request.getRemoteAddress().getAddress() != null) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        if ("127.0.0.1".equalsIgnoreCase(ip)
                || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
            //根据网卡取本机配置的IP
            InetAddress iNet = null;
            try {
                iNet = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                System.out.println("can't get InetAddress.getLocalHost !");
            }
            if (iNet != null)
                ip = iNet.getHostAddress();
        }
        return ip;
    }
}
