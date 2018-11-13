package com.cbt.parse.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImgDownload {

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://img1.yiwugou.com/i004/2015/03/09/94/f728fdcf32df463ad75c7f90a8f876d3.jpg@350w.jpg";
		String imgName = DownloadMain.getSpiderContext(url, "(/([0-9a-zA-Z]){6,100})");
		String fileName = "C:/Users/abc/Desktop/yiwu/" + "12345" + "/" + imgName + ".jpg";
		execute(url, fileName);
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
			e.getStackTrace();
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
				//file.getParentFile().mkdirs();
				return false;
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
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

}
