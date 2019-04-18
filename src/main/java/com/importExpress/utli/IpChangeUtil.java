package com.importExpress.utli;

import java.util.ArrayList;
import java.util.List;

public class IpChangeUtil {

    /**
     * IP转Long
     * @param ip
     * @return
     */
    public static long ipToLong(String ip) {
        String[] ipArray = ip.split("\\.");
        List<Long> ipList = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            ipList.add(Long.parseLong(ipArray[i].trim()));
        }
        long ipLong = ipList.get(0) * 256L * 256L * 256L
                + ipList.get(1) * 256L * 256L + ipList.get(2) * 256L
                + ipList.get(3);

        return ipLong;
    }


    /**
     * Long转IP
     * @param ipaddr
     * @return
     */
    public static String longToIP(long ipaddr) {
        long y = ipaddr % 256;
        long m = (ipaddr - y) / (256 * 256 * 256);
        long n = (ipaddr - 256 * 256 * 256 * m - y) / (256 * 256);
        long x = (ipaddr - 256 * 256 * 256 * m - 256 * 256 * n - y) / 256;
        return m + "." + n + "." + x + "." + y;
    }

    /**
     * byte转Long
     * @param byteNum
     * @return
     */
    public static long bytesToLong(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

}
