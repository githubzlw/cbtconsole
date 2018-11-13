package com.cbt.jcys.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

public class CodecUtil {
    // 将字符串 UTF-8 编码
    public static String encodeForUTF8(String str) {
        String target;
        try {
            target = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return target;
    }
    // 将字符串 UTF-8 解码
    public static String decodeForUTF8(String str) {
        String target = "";
        try {
            target = URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
            e.getMessage();
        }
        return target;
    }

    // 将字符串 Base64 编码
    public static String encodeForBase64(String str) {
        return Base64.encodeBase64String(str.getBytes());
    }
    //将字符串 Base64 编码
    public static String encodeForBase64(byte[] str) {
        return Base64.encodeBase64String(str);
    }
    // 将字符串 Base64 解码
    public static String decodeForBase64(String str) {
        return new String(Base64.decodeBase64(str.getBytes()));
    }

    // 将字符串 MD5 加密
    public static String encryptForMD5(String str) {
        return DigestUtils.md5Hex(str);
    }

    // 创建随机数
    public static String createRandomNumber(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    // 获取UUID（32位）
    public static String createUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static void main(String args[]){
	String data="{\"tradeId\": \"000\",\"mailNo\": \"000\"}";
	String key="123456";
	String str=String.format("%s%s", data,key);
	System.out.println(str);
	System.out.println("base64及MD5加密："+Md5Helper.md5(str, "gbk", true));
	System.out.println("base64及MD5加密："+Md5Helper.md5(str, "utf-8", true));
	System.out.println("MD5加密："+Md5Helper.md5(str, "utf-8", false));
    }
}
