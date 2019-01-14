package com.importExpress.utli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang.StringUtils;

import okhttp3.*;
import net.sf.json.JSONArray;

/**
 * 图片分辨率压缩
 *
 */
public class ImageCompressionByNoteJs {

	private static String USEURL = "http://192.168.1.9:3000/zipImage?paths=";
	private static OkHttpClient client = new OkHttpClient();
	private static JSONArray jsAr = null;

	/**
	 * 
	 * @Title compressByOkHttp 
	 * @Description TODO
	 * @param dirPaths 压缩文件的全路径
	 * @param type 压缩类型 1:700x700 2:285x285 3:285x380 4:220x220 5:400x400 6:60x60
	 * @return
	 * @return boolean
	 */
	public static boolean compressByOkHttp(String dirPaths, int type) {
		boolean isSuccess = false;
		String url = USEURL + dirPaths + "&type=" + type;
		if (client == null) {
			client = new OkHttpClient();
		}
		Request request = new Request.Builder().url(url).build();
		Response response;
		try {
			response = client.newCall(request).execute();

			String resultStr = response.body().string();
			if(StringUtils.isBlank(resultStr) || "null".equals(resultStr)){
				System.err.println("compressByOkHttp[" + url + "],result:" + resultStr);
				isSuccess = false;
			}else{
				jsAr = JSONArray.fromObject(resultStr);
				if ("OK".equals(jsAr.getJSONObject(0).get("result"))) {
					isSuccess = true;
				} else {
					isSuccess = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("compressByOkHttp[" + url + "] error:" + e.getMessage());
			isSuccess = false;
		} finally {
			if (jsAr != null) {
				jsAr.clear();
			}
		}
		return isSuccess;
	}

	/**
	 * 
	 * @Title doCompress
	 * @Description TODO
	 * @param dirPaths
	 *            压缩文件的全路径
	 * @param type
	 *            压缩类型 1:700x700 2:285x285 3:285x380 4:220x220
	 * @return
	 * @return boolean
	 */
	public static boolean doCompress(String dirPaths, int type) {

		boolean isSuccess = false;
		URL realUrl = null;
		InputStream inputStream = null;
		InputStreamReader tempInputStreamReader = null;
		BufferedReader reader = null;
		StringBuilder builder = null;
		HttpURLConnection connection = null;
		String url = USEURL + dirPaths + "&type=" + type;
		int count = 1;
		// String content = "";

		try {

			realUrl = new URL(url);
			// 打开和URL之间的连接
			connection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			// connection.setRequestProperty("accept", "*/*");
			// connection.setRequestProperty("connection", "Keep-Alive");
			// connection.setRequestProperty("user-agent", "Mozilla/4.0
			// (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// connection.setConnectTimeout(100 * 1000);
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			// System.err.println("dirPaths：" + dirPaths + " connection size " +
			// map.size());
			// isSuccess = true;
			// count ++;

			if (200 == connection.getResponseCode()) {

				if (!checkIsEmpty(connection.getContentEncoding())) {
					String encode = connection.getContentEncoding().toLowerCase();
					if (!checkIsEmpty(encode) && encode.indexOf("gzip") >= 0) {
						inputStream = new GZIPInputStream(connection.getInputStream());
					}
				}

				if (null == inputStream) {
					inputStream = connection.getInputStream();
				}

				tempInputStreamReader = new InputStreamReader(inputStream, "utf-8");
				reader = new BufferedReader(tempInputStreamReader);
				builder = new StringBuilder();
				String line = null;
				System.err.print("path=" + dirPaths + ",type=" + type + ",result:");
				while ((line = reader.readLine()) != null) {
					builder.append(line);
					System.err.print(line);
				}
				System.err.println("");
				// content = builder.toString();
				tempInputStreamReader.close();
				reader.close();

			}

			count++;
			isSuccess = true;

		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println("path=" + dirPaths + ",type=" + type + ",质量压缩失败 :" + e.getMessage());
			// LOG.error("dirPath:" + dirPaths + ",质量压缩失败 :" + e.getMessage());
			try {
				System.err.println("第" + count + "执行暂停3秒操作");
				Thread.sleep(3 * 1000);
			} catch (InterruptedException e1) {
				// e1.printStackTrace();
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				if (tempInputStreamReader != null) {
					tempInputStreamReader.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}

		}
		// System.err.println("path=" + dirPaths + ",type=" + type+ ",result:["
		// + content + "]");
		realUrl = null;
		connection = null;
		return isSuccess;
	}

	/**
	 * 
	 * @Title checkIsEmpty
	 * @Description TODO
	 * @param s
	 * @return
	 * @return boolean
	 */
	private static boolean checkIsEmpty(final CharSequence s) {
		if (s == null) {
			return true;
		}
		return s.length() == 0;
	}

}
