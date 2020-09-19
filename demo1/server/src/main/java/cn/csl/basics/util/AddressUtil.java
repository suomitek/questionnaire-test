package cn.csl.basics.util;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class AddressUtil{

/**
  * 获取ip地址
  * 
  * @param request
  * @return
  */
    public static String getIpAddress(HttpServletRequest request) {
    // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
    } else if (ip.length() > 15) {
        String[] ips = ip.split(",");
        for (int index = 0; index < ips.length; index++) {
            String strIp = (String) ips[index];
            if (!("unknown".equalsIgnoreCase(strIp))) {
                ip = strIp;
                break;
            }
        }
    }
    return ip;
    }



    public static String getHostIp(){
        try
        {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements())
            {
                NetworkInterface netInterface = (NetworkInterface)allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements())
                {
                    InetAddress ip = (InetAddress)addresses.nextElement();
                    if ((ip != null) && ((ip instanceof Inet4Address))) {
                        if ((!ip.isLoopbackAddress()) &&
                                (ip.getHostAddress().indexOf(":") == -1)) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static String getPost(){
        try
        {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements())
            {
                NetworkInterface netInterface = (NetworkInterface)allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements())
                {
                    InetAddress ip = (InetAddress)addresses.nextElement();
                    if ((ip != null) && ((ip instanceof Inet4Address))) {
                        if ((!ip.isLoopbackAddress()) &&
                                (ip.getHostAddress().indexOf(":") == -1)) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}