package com.cbt.parse.service;

import com.importExpress.utli.ColorRGBUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ImgDownload {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ImgDownload.class);

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "https://img.import-express.com/importcsvimg/shopimg/1001616913/4426630628_1406535455.400x400.jpg";
		String fileName = "E:/cbtimg/editimg/importimg/777.jpg";
		System.err.println("downFromImgService:" + downFromImgService(url, fileName));
	}

	public static boolean downAndReTry(String imgUrl,String fileName){
		int count = 0;
		boolean isSu = false;
		while (count < 5 && !isSu){
			count ++;
			isSu = downFromImgService(imgUrl,fileName);
			if(!isSu){
				try {
					Thread.sleep(3000 + count * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return isSu;
	}


	public static boolean downFromImgService(String imgUrl,String fileName) {

		String ipUrl = imgUrl.replace("https://img.import-express.com","http://104.247.194.50");
		boolean isDown = false;
		File file = new File(fileName);
		if(checkDownFileByName(fileName)){
			return true;
		}
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		ByteArrayOutputStream output = null;
		try {
			OkHttpClient client = new OkHttpClient.Builder().connectTimeout(600, TimeUnit.SECONDS)
					.readTimeout(300, TimeUnit.SECONDS).writeTimeout(300, TimeUnit.SECONDS)
					.hostnameVerifier((s, sslSession) -> true)
					.build();
			Request request = new Request.Builder().addHeader("Connection", "close").addHeader("Accept", "*/*")
					.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)").get().url(ipUrl).build();
			Response response = client.newCall(request).execute();

			inputStream = response.body().byteStream();

			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();// 返回此抽象路径名父目录的抽象路径名；创建
			}else if(file.exists() && file.isFile()){
				file.delete();
			}

			fileOutputStream = new FileOutputStream(file);
			output = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int length;

			while ((length = inputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			fileOutputStream.write(output.toByteArray());

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("execute download[ " + imgUrl + "] error:" + e.getMessage());
			return false;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		isDown = checkDownFileByName(fileName);
		if(!isDown){
			System.err.println("down img [" + fileName + "],result:" + isDown);
		}
		return isDown;
	}


	/**
	 * 下载图片
	 * 
	 * @param url
	 * @param fileName
	 * @return
	 */
	public static Boolean execute(String url, String fileName) {
		try {
			byte[] btImg = getImageFromNetByUrl(url);
			if (btImg != null && btImg.length > 0) {
				File file = new File(fileName);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();// 返回此抽象路径名父目录的抽象路径名；创建
				}
				writeImageToDisk(btImg, fileName);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("execute download[ " + fileName + "] error:" + e.getMessage());
			return false;
		}
	}

	/**
	 * 将图片写入到磁盘
	 * 
	 * @param img
	 *            图片数据流
	 * @param fileName
	 *            文件保存时的名称
	 */
	public static void writeImageToDisk(byte[] img, String fileName) throws Exception {
		File file = new File(fileName);
		//判断文件存在需要删除
		if (file.exists()) {
			file.delete();
		} else {
			// 判断父目录是否存在，不存在则创建
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
		}
		FileOutputStream fops = new FileOutputStream(file);
		fops.write(img);
		fops.flush();
		fops.close();
	}
	
	/**
	 * 将图片写入到磁盘
	 * 
	 * @param img
	 *            图片数据流
	 * @param fileName
	 *            文件保存时的名称
	 */
	public static boolean writeImageToDisk1(byte[] img, String fileName) throws Exception {
		boolean flag=true;
		File file = new File(fileName);
		//判断文件存在需要删除
		if (file.exists()) {
			file.delete();
		} else {
			// 判断父目录是否存在，不存在则创建
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
				// return false;
			}
		}
		FileOutputStream fops = new FileOutputStream(file);
		fops.write(img);
		fops.flush();
		fops.close();
		return flag;
	}

	/**
	 * 根据地址获得数据的字节流
	 * 
	 * @param strUrl
	 *            网络连接地址
	 * @return
	 * @throws Exception
	 */
	private static byte[] getImageFromNetByUrl(String strUrl) throws Exception {
		URL url = new URL(strUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(20000);
		conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
		InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
		byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
		return btImg;
	}

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	private static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	public static boolean checkDownFileByName(String fileName) {
        boolean isDown = false;
        File downFlie = new File(fileName);

        if (downFlie.exists() && downFlie.length() > 512) {
			try {
				if (ColorRGBUtil.getInstance().checkImage(fileName)) {
					System.err.println("fileName:" + fileName + " gray img");
					logger.error("fileName:" + fileName + " gray img");
					// 灰图检测
					isDown = false;
					downFlie.delete();
				} else {
					isDown = true;
				}
			} catch (Exception e) {
				isDown = false;
				e.printStackTrace();
			}
		} else {
            if (downFlie.exists()) {
                downFlie.delete();
            }
            isDown = false;
        }
        return isDown;
    }

}
