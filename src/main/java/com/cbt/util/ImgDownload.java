package com.cbt.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**图片下载模块（指定图片链接，下载图片到本地）
 * @author abc
 *
 */
public class ImgDownload {
	 
	 /**下载图片
	 * @param url
	 * @param fileName
	 * @return
	 */
	public static Boolean execute(String url,String fileName){
		try{
		  byte[] btImg = getImageFromNetByUrl(url);
		  if(btImg != null && btImg.length > 0){
				   File file = new File(fileName);
				   if(!file.exists()){
					   file.getParentFile().mkdirs();//返回此抽象路径名父目录的抽象路径名；创建
					  file.createNewFile();
				   }
				   if(file.exists()){
					   writeImageToDisk(btImg, fileName);
					   return true;
				   }
				    return false;
		  }else{
		     return false;
		  }
		}catch(Exception e){
			return false;
		}
	 }
	 
	 
	 /**
	  * 将图片写入到磁盘
	  * @param img 图片数据流
	  * @param fileName 文件保存时的名称
	  */
	 private static void writeImageToDisk(byte[] img,String fileName)throws Exception{
		 File file = new File(fileName);
		 FileOutputStream fops = new FileOutputStream(file);
		 fops.write(img);
		 fops.flush();
		 fops.close();
	 }
	 /**
	  * 根据地址获得数据的字节流
	  * @param strUrl 网络连接地址
	  * @return
	 * @throws Exception 
	  */
	 private static byte[] getImageFromNetByUrl(String strUrl) throws Exception{
		 URL url = new URL(strUrl);
		 HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		 conn.setRequestMethod("GET");
		 conn.setConnectTimeout(30000);
		 InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
		 byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
		 return btImg;
	 }
	 /**
	  * 从输入流中获取数据
	  * @param inStream 输入流
	  * @return
	  * @throws Exception
	  */
	 private static byte[] readInputStream(InputStream inStream) throws Exception{
		  ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		  byte[] buffer = new byte[1024];
		  int len = 0;
		  while( (len=inStream.read(buffer)) != -1 ){
		   outStream.write(buffer, 0, len);
		  }
		  inStream.close();
		  return outStream.toByteArray();
	 }

}
