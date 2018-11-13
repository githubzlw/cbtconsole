package com.cbt.website.util;


import org.slf4j.LoggerFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileTool {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(FileTool.class);
	/**
	 * Description: 向FTP服务器上传文件
	 * @Version      1.0
	 * @param url FTP服务器hostname
	 * @param port  FTP服务器端口
	 * @param username FTP登录账号
	 * @param password  FTP登录密码
	 * @param path  FTP服务器保存目录
	 * @param filename  上传到FTP服务器上的文件名
	 * @param input   输入流
	 * @return 成功返回true，否则返回false *
	 */
	public static boolean uploadFile(String url,// FTP服务器hostname
			int port,// FTP服务器端口
			String username, // FTP登录账号
			String password, // FTP登录密码
			String path, // FTP服务器保存目录
			String filename, // 上传到FTP服务器上的文件名
			InputStream input // 输入流
	){
		boolean success = false;
		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("GBK");
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.makeDirectory(path);
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(filename, input);
			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			LOG.debug(e.getMessage());
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}
	/**
	 * 将本地文件上传到FTP服务器上 *
	 */
	public static boolean upLoadFromProduction(
			String filename, // 上传到FTP服务器上的文件名
			String orginfilename // 输入流文件名
	   ) {
		boolean flag = false;
		try {
			/*FileInputStream in = new FileInputStream(new File(orginfilename));*/
			  // 构造URL  
	        URL img_url = new URL(orginfilename);
	        // 打开连接  
	        URLConnection con = img_url.openConnection();
	        //设置请求超时为5s  
	        con.setConnectTimeout(5*1000);  
	        // 输入流  
	        InputStream in = con.getInputStream();
			flag = uploadFile("ftp.china-clothing-wholesale.com", 21, "hotproduct@china-clothing-wholesale.com", "hotproduct", "", filename, in);
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	//删除文件
    public static boolean deleteFile(String path) {  
        File file = new File(path);  
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
        }else{
        	return false;
        }
		return true;  
    }  
    
    
    //删除文件夹及文件夹下的文件
    public static boolean deleteDirectory(String path) {  
        File file = new File(path);  
        if (!file.exists() || !file.isDirectory()) {  
            return false;  
        }  
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = file.listFiles();  
//        File subFile = null;
        for (int i = 0; i < files.length; i++) {
        	if (files[i].isFile()) {
//        		subFile.delete();
        		deleteFile(files[i].getAbsolutePath());
        	}else{
        		deleteDirectory(files[i].getAbsolutePath());
        	}
        }
        file.delete();
		return true;  
    }  
	
	
     //测试
	public static void main(String[] args) {
		 
		upLoadFromProduction("c1c.img", "http://g01.a.alicdn.com/kf/HTB1bnubIFXXXXXFaXXXq6xXFXXXZ/360-dgree-Universal-Car-CD-Player-Slot-Mount-Cradle-Holder-For-iPhone-Mobile-Phone-GPS-Free.jpg_220x220.jpg");
	}
}
