package com.mogudiandian.util.ip;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * IP地址的工具类
 * @author sunbo
 */
public final class IpAddressUtils {

    /**
     * 根据网卡获取IP
     * @return IP集合
     */
    public static List<Inet4Address> getLocalIp4AddressFromNetworkInterface() {
        List<Inet4Address> addresses = new ArrayList<>(1);
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            if (e != null) {
                while (e.hasMoreElements()) {
                    NetworkInterface n = e.nextElement();
                    if (!isValidInterface(n)) {
                        continue;
                    }
                    Enumeration<InetAddress> ee = n.getInetAddresses();
                    while (ee.hasMoreElements()) {
                        InetAddress i = ee.nextElement();
                        if (isValidAddress(i)) {
                            addresses.add((Inet4Address) i);
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
        return addresses;
    }

    /**
     * 过滤回环网卡、点对点网卡、非活动网卡
     *
     * @param ni 网卡
     * @return 如果满足要求则true，否则false
     */
    private static boolean isValidInterface(NetworkInterface ni) {
        try {
            return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是否是IPv4，并且内网地址并过滤回环地址.
     */
    private static boolean isValidAddress(InetAddress address) {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }

    /**
     * 通过Socket获取IP
     *
     * @return IP
     */
    public static Optional<Inet4Address> getIpBySocket() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            if (socket.getLocalAddress() instanceof Inet4Address) {
                return Optional.of((Inet4Address) socket.getLocalAddress());
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取IP地址 如果本地只有一个网络地址就使用该的地址 否则(多个的情况) 获取外网地址
     * @return IP
     */
    public static Optional<Inet4Address> getLocalIp4Address() {
        final List<Inet4Address> ipByNi = getLocalIp4AddressFromNetworkInterface();
        if (ipByNi.size() != 1) {
            final Optional<Inet4Address> ipBySocketOpt = getIpBySocket();
            if (ipBySocketOpt.isPresent()) {
                return ipBySocketOpt;
            } else {
                return ipByNi.isEmpty() ? Optional.empty() : Optional.of(ipByNi.get(0));
            }
        }
        return Optional.of(ipByNi.get(0));
    }

}
