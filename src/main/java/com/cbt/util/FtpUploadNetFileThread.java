package com.cbt.util;

import com.cbt.parse.service.ImgDownload;
import com.cbt.website.util.JsonResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * 使用线程上传网络文件
 * remoteFullFileName:远程文件全路径名称
 * localFullFileName:本地文件全路径名称
 * netUrl:网络文件链接
 */
public class FtpUploadNetFileThread implements Runnable {
	private static final Log LOG = LogFactory.getLog(FtpUploadNetFileThread.class);

	private String remoteFullFileName;

	private String localFullFileName;

	private String netUrl;

	private FtpConfig ftpConfig;

	public FtpUploadNetFileThread(String remoteFullFileName, String localFullFileName, String netUrl,
			FtpConfig ftpConfig) {
		super();
		this.remoteFullFileName = remoteFullFileName;
		this.localFullFileName = localFullFileName;
		this.netUrl = netUrl;
		this.ftpConfig = ftpConfig;
	}

	@Override
	public void run() {

		LOG.info("begin net-file upload,remoteFile:" + remoteFullFileName + ",localFile:" + localFullFileName);

		int dlCount = 1;
		int count = 1;
		JsonResult json = new JsonResult();
		json.setOk(false);

		while (!(dlCount > 5 || count > 5 || json.isOk())) {
			LOG.info("execute count :" + count);
			// 下载网络图片到本地
			boolean is = ImgDownload.execute(netUrl, localFullFileName);
			if (is) {
				json = NewFtpUtil.uploadFileToRemoteSSM(remoteFullFileName, localFullFileName, ftpConfig);
				count++;
			} else {
				dlCount++;
			}

		}
		if (dlCount > 5) {
			LOG.info("net-file: " + netUrl + " download error!");
		} else {
			LOG.info("net-file: " + netUrl + " download success!");
		}
		if (json.isOk()) {
			LOG.info("remoteFile:" + remoteFullFileName + ",upload success!");
		} else {
			LOG.error("localFile:" + localFullFileName + ",upload error!reason:" + json.getMessage());
		}
		LOG.info("upload net-file remoteFile:" + remoteFullFileName + ",localFile:" + localFullFileName + " end!!");
	}

}
