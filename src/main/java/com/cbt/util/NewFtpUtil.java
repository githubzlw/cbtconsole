package com.cbt.util;

import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.util.JsonResult;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

public class NewFtpUtil {

	// 枚举类UploadStatus代码

	public enum UploadStatus {
		Create_Directory_Fail, // 远程服务器相应目录创建失败
		Create_Directory_Success, // 远程服务器闯将目录成功
		Upload_New_File_Success, // 上传新文件成功
		Upload_New_File_Failed, // 上传新文件失败
		File_Exits, // 文件已经存在
		Remote_Bigger_Local, // 远程文件大于本地文件
		Upload_From_Break_Success, // 断点续传成功
		Upload_From_Break_Failed, // 断点续传失败
		Delete_Remote_Faild; // 删除远程文件失败
	}

	// 枚举类DownloadStatus代码
	public enum DownloadStatus {
		Remote_File_Noexist, // 远程文件不存在
		Local_Bigger_Remote, // 本地文件大于远程文件
		Download_From_Break_Success, // 断点下载文件成功
		Download_From_Break_Failed, // 断点下载文件失败
		Download_New_Success, // 全新下载文件成功
		Download_New_Failed; // 全新下载文件失败
	}

	/**
	 * 
	 * @param saveFileName
	 *            : 保存全路径(含文件名)
	 * @param localFilePath
	 *            : 本地文件全路径(含文件名)
	 * @return
	 */
	public static synchronized JsonResult uploadFileToRemoteSSM(String saveFileName, String localFilePath,
			FtpConfig ftpConfig) {
		JsonResult json = new JsonResult();

		FTPClient ftpClient = new FTPClient();

		// 判断获取的配置信息是否有效
		if (ftpConfig == null || !ftpConfig.isOk()) {
			json.setOk(false);
			json.setMessage("获取配置文件失败");
			return json;
		} else {
			if (StringUtil.isBlank(ftpConfig.getFtpURL())) {
				json.setOk(false);
				json.setMessage("获取ftpURL失败");
				return json;
			}
			if (StringUtil.isBlank(ftpConfig.getFtpPort())) {
				json.setOk(false);
				json.setMessage("获取ftpPort失败");
				return json;
			}
			if (StringUtil.isBlank(ftpConfig.getFtpUserName())) {
				json.setOk(false);
				json.setMessage("获取ftpUserName失败");
				return json;
			}
			if (StringUtil.isBlank(ftpConfig.getFtpPassword())) {
				json.setOk(false);
				json.setMessage("获取ftpPassword失败");
				return json;
			}
			if (StringUtil.isBlank(ftpConfig.getRemoteShowPath())) {
				json.setOk(false);
				json.setMessage("获取remoteShowPath失败");
				return json;
			}

			if (StringUtil.isBlank(ftpConfig.getLocalDiskPath())) {
				json.setOk(false);
				json.setMessage("获取localDiskPath失败");
				return json;
			}

			if (StringUtil.isBlank(ftpConfig.getLocalShowPath())) {
				json.setOk(false);
				json.setMessage("获取localShowPath失败");
				return json;
			}

			try {
				boolean isConnect = false;
				// 初始化参数
				ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
				// 连接ftp
				isConnect = connect(ftpConfig.getFtpURL(), Integer.valueOf(ftpConfig.getFtpPort()),
						ftpConfig.getFtpUserName(), ftpConfig.getFtpPassword(), ftpClient);
				if (isConnect) {
					// 上传文件
					UploadStatus resultSt = upload(localFilePath, saveFileName, ftpClient);
					if (resultSt == null) {
						json.setOk(true);
						json.setMessage("上传成功");
						json.setData(ftpConfig.getRemoteShowPath());
						return json;
					} else {
						json.setOk(false);
						json.setMessage("上传失败,ftp断点续传不成功");
						return json;
					}
				} else {
					json.setOk(false);
					json.setMessage("ftp connect error");
					return json;
				}
			} catch (Exception e) {
				e.getStackTrace();
				json.setOk(false);
				json.setMessage("上传文件错误,请重试");
				System.err.println("上传文件错误:" + e.getMessage());
			} finally {
				// 断开连接
				try {
					disconnect(ftpClient);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}

	/**
	 * 
	 * @param host
	 *            连接主机IP
	 * @param port
	 *            端口号
	 * @param username
	 *            FTP用户名
	 * @param password
	 *            FTP密码
	 * @param saveFilePath
	 *            保存文件的路径(字符串尾部带上'/')
	 * @param saveFileName
	 *            保存文件名称
	 * @param localFilePath
	 *            本地文件路径(全路径,含有带后缀的文件名)
	 * @return
	 */
	public static boolean uploadFileToRemote(String host, int port, String username, String password,
			String saveFilePath, String saveFileName, String localFilePath) {
		FTPClient ftpClient = new FTPClient();
		try {
			boolean isConnect = false;
			// 初始化参数
			ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
			// 连接ftp
			isConnect = connect(host, port, username, password, ftpClient);
			if (isConnect) {
				// 上传文件
				upload(localFilePath, saveFilePath + saveFileName, ftpClient);
			} else {
				return false;
			}

		} catch (Exception e) {
			e.getStackTrace();
			return false;
		} finally {
			// 断开连接
			try {
				disconnect(ftpClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 连接到FTP服务器
	 * 
	 * @param hostname
	 *            主机名
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 是否连接成功
	 * @throws IOException
	 */
	private static boolean connect(String hostname, int port, String username, String password, FTPClient ftpClient)
			throws IOException {
		ftpClient.connect(hostname, port);
		ftpClient.setControlEncoding("UTF-8");
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(username, password)) {
				return true;
			}
		}
		disconnect(ftpClient);
		return false;
	}

	/**
	 * 断开与远程服务器的连接
	 * 
	 * @throws IOException
	 */
	private static void disconnect(FTPClient ftpClient) throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	/**
	 * 上传文件到FTP服务器，支持断点续传
	 * 
	 * @param local
	 *            本地文件名称，绝对路径
	 * @param remote
	 *            远程文件路径，使用/home/directory1/subdirectory/file.ext或是
	 *            http://www.guihua.org /subdirectory/file.ext
	 *            按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
	 * @return 上传结果
	 * @throws IOException
	 */
	private static UploadStatus upload(String local, String remote, FTPClient ftpClient) throws IOException {
		// 设置PassiveMode传输
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制流的方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("GBK");
		ftpClient.setBufferSize(1024);// 设置每次读取文件流时缓存数组的大小
		UploadStatus result;
		// 对远程目录的处理
		String remoteFileName = remote;
		if (remote.contains("/")) {
			remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
			// 创建服务器远程目录结构，创建失败直接返回
			if (CreateDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
				return UploadStatus.Create_Directory_Fail;
			}
		}

		// 检查远程是否存在文件
		FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes("GBK"), "UTF-8"));
		if (files.length == 1) {
			long remoteSize = files[0].getSize();
			File f = new File(local);
			long localSize = f.length();
			if (remoteSize == localSize) {
				System.err.println("File " + remoteFileName + " is exits");
				return null;
			} else if (remoteSize > localSize) {
				ftpClient.deleteFile(remoteFileName);
				// return UploadStatus.Remote_Bigger_Local;
			}

			// 尝试移动文件内读取指针,实现断点续传
			result = uploadFile(remoteFileName, f, ftpClient, remoteSize);

			// 如果断点续传没有成功，则删除服务器上文件，重新上传
			if (result == UploadStatus.Upload_From_Break_Failed) {
				if (!ftpClient.deleteFile(remoteFileName)) {
					return UploadStatus.Delete_Remote_Faild;
				}
				result = uploadFile(remoteFileName, f, ftpClient, 0);
			}
		} else {
			result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
		}
		return result;
	}

	/**
	 * 递归创建远程服务器目录
	 * 
	 * @param remote
	 *            远程服务器文件绝对路径
	 * @param ftpClient
	 *            FTPClient 对象
	 * @return 目录创建是否成功
	 * @throws IOException
	 */
	private static UploadStatus CreateDirecroty(String remote, FTPClient ftpClient) throws IOException {
		UploadStatus status = UploadStatus.Create_Directory_Success;
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		if (!directory.equalsIgnoreCase("/")
				&& !ftpClient.changeWorkingDirectory(new String(directory.getBytes("GBK"), "UTF-8"))) {
			// 如果远程目录不存在，则递归创建远程服务器目录
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "UTF-8");
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						System.out.println("创建目录失败");
						return UploadStatus.Create_Directory_Fail;
					}
				}

				start = end + 1;
				end = directory.indexOf("/", start);

				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return status;
	}

	/** */
	/**
	 * 上传文件到服务器,新上传和断点续传
	 * 
	 * @param remoteFile
	 *            远程文件名，在上传之前已经将服务器工作目录做了改变
	 * @param localFile
	 *            本地文件 File句柄，绝对路径
	 * @param processStep
	 *            需要显示的处理进度步进值
	 * @param ftpClient
	 *            FTPClient 引用
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private static UploadStatus uploadFile(String remoteFile, File localFile, FTPClient ftpClient, long remoteSize)
			throws IOException {
		System.out.println("本地文件是否存在:" + localFile.exists());

		RandomAccessFile raf = new RandomAccessFile(localFile, "r");
		OutputStream out = null;
		FileInputStream is = null;
		try {
			ftpClient.changeWorkingDirectory(new String(remoteFile.getBytes("GBK"), "UTF-8"));
			if (remoteSize > 0) {
				out = ftpClient.appendFileStream(new String(remoteFile.getBytes("GBK"), "UTF-8"));
				ftpClient.setRestartOffset(remoteSize);
				raf.seek(remoteSize);
			} else {
				out = ftpClient.storeFileStream(remoteFile);
			}
			is = new FileInputStream(localFile);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = raf.read(bytes)) != -1) {
				out.write(bytes, 0, c);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				out.close();
				is.close();
				ftpClient.logout();
				if (ftpClient.isConnected()) {
					ftpClient.disconnect();
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}

		return null;
	}

	public static void main(String[] args) {
		String ip = "104.247.194.50";// 192.168.1.29
		String user = "importweb";// ftp29
		String passw = "importftp@123";// 29@123
		String remotePath = "/inspectionImg/2018-07/";// 2017-09-12/
		String fileName = "Q710728956160596_962827_1532680838468.jpg";
		String oriFilePath = "D:product/2018-07/Q710728956160596_962827_1532680838468.jpg";
		boolean success = false;
		success = uploadFileToRemote(ip, 21, user, passw, remotePath, fileName, oriFilePath);
		System.out.println("success :" + success);
	}

}
