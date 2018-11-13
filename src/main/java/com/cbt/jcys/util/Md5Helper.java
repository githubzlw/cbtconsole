package com.cbt.jcys.util;
//import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

//import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.codec.digest.DigestUtils;
//import cn.zto.log4j.Log4jConfigurator;
//import cn.zto.util.CodecUtil; 
public class Md5Helper {
	public static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	public static final String CHARSET = "utf-8";
	public static String md5(String data) {
	    return md5(data, CHARSET, true);
	}
	public static String md5(String data, String charset, boolean isBase64) {
		String digest = "";
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update((data).getBytes(charset));
			byte[] md5Bytes = mdTemp.digest();

			if (isBase64) {
				digest = CodecUtil.encodeForBase64(md5Bytes);
			} else {
				int k = 0;
				char str[] = new char[md5Bytes.length * 2];
				for (byte c : md5Bytes) {
					str[k++] = hexDigits[c >> 4 & 0xf];
					str[k++] = hexDigits[c & 0xf];
				}
				digest = new String(str);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return digest.replace("\r\n", "");
	}
}
