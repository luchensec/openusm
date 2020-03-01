/**
 *
 */
package com.openusm.web.util;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 获取请求IP地址
 * @author xingfu_xiaohai@163.com
 * @since 2019-09-21 22:29
 */
public class IpUtils {
    /**
     * 获取客户端IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 将ip地址转成数字
     *
     * @param strIp
     * @return
     */
    public static long ipToLong(String strIp) {
        try {
            long[] ip = new long[4];
            // 先找到IP地址字符串中.的位置
            int position1 = strIp.indexOf(".");
            int position2 = strIp.indexOf(".", position1 + 1);
            int position3 = strIp.indexOf(".", position2 + 1);
            // 将每个.之间的字符串转换成整型
            ip[0] = Long.parseLong(strIp.substring(0, position1));
            ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
            ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
            ip[3] = Long.parseLong(strIp.substring(position3 + 1));
            return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 将十进制整数形式转换成点分十进制形式的ip地址
     *
     * @param longIp
     * @return
     */
    public static String longToIP(long longIp) {
        StringBuilder sb = new StringBuilder("");
        try {
            // 直接右移24位
            sb.append(String.valueOf(longIp >>> 24));
            sb.append(".");
            // 将高8位置0，然后右移16位
            sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
            sb.append(".");
            // 将高16位置0，然后右移8位
            sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
            sb.append(".");
            // 将高24位置0
            sb.append(String.valueOf(longIp & 0x000000FF));
            return sb.toString();
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 将十进制整数形式转换成点分十进制形式的ip地址
     *
     * @param stringIp
     * @return
     */
    public static String longToIP(String stringIp) {
        StringBuilder sb = new StringBuilder("");
        try {
            // 直接右移24位
            sb.append(Long.parseLong(stringIp) >>> 24);
            sb.append(".");
            // 将高8位置0，然后右移16位
            sb.append((Long.parseLong(stringIp) & 0x00FFFFFF) >>> 16);
            sb.append(".");
            // 将高16位置0，然后右移8位
            sb.append((Long.parseLong(stringIp) & 0x0000FFFF) >>> 8);
            sb.append(".");
            // 将高24位置0
            sb.append(Long.parseLong(stringIp) & 0x000000FF);
            return sb.toString();
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 判断是否是合法IP
     *
     * @param ip
     * @return
     */
    public static boolean isIp(String ip) {

        if (ip.length() < 7 || ip.length() > 15 || "".equals(ip)) {
            return false;
        }
        String rexp = "([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])(\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])){3}";

        return ip.matches(rexp);
    }
//-----------------------------------------------------------------------------------------------------------------

    /**
     * 获取某ip段内的所有ip
     * @param ipScope ip范围 192.168.10.126-192.168.10.130或者192.168.10.126-130
     * @return ip集合
     */
    public static List<String> getIpListByScope(String ipScope) {
        List<String> ipList = new ArrayList<>();
        String[] sa = ipScope.split("-");
        String[] result;

        if (sa.length == 1) {
            return Collections.singletonList(ipScope);
        }

        if (sa[1].contains(".")) {
            result = new String[(int) (ipToLong(sa[1]) - ipToLong(sa[0])) + 1];
        } else {
            result = new String[(int) (ipToLong(sa[0].substring(0, sa[0].lastIndexOf(".")) + "." + sa[1]) - ipToLong(sa[0])) + 1];
        }

        for (int i = 0; i < result.length; i++) {
            String ip = longToIP(ipToLong(sa[0]) + i);
            String[] ipArr = ip.split("\\.");
            if ("0".equals(ipArr[3]) || "255".equals(ipArr[3])) {
                continue;
            }
            ipList.add(ip);
        }

        return ipList;
    }

}
