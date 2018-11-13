package com.cbt.util;

import com.cbt.website.util.JsonResult;

import org.slf4j.LoggerFactory;

/*
 * 使用线程上传文件
 * remoteFullFileName:远程文件全路径名称
 * localFullFileName:本地文件全路径名称
 */
public class FtpUploadThread implements Runnable {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(FtpUploadThread.class);

	private String remoteFullFileName;

	private String localFullFileName;

	private FtpConfig ftpConfig;

	public FtpUploadThread(String remoteFullFileName, String localFullFileName, FtpConfig ftpConfig) {
		super();
		this.remoteFullFileName = remoteFullFileName;
		this.localFullFileName = localFullFileName;
		this.ftpConfig = ftpConfig;
	}

	@Override
	public void run() {

		LOG.info("begin file upload,remoteFile:" + remoteFullFileName + ",localFile:" + localFullFileName);

		JsonResult json = new JsonResult();
		int count = 1;
		json.setOk(false);
		while (!(count > 5 || json.isOk())) {
			LOG.info("execute count :" + count);
			json = NewFtpUtil.uploadFileToRemoteSSM(remoteFullFileName, localFullFileName, ftpConfig);
			count++;
		}
		if (json.isOk()) {
			LOG.info("remoteFile:" + remoteFullFileName + ",localFile:" + localFullFileName + ",upload success!");
		} else {
			LOG.error("remoteFile:" + remoteFullFileName + ",localFile:" + localFullFileName + ",upload error!reason:"
					+ json.getMessage());
		}
		LOG.info("upload file remoteFile:" + remoteFullFileName + ",localFile:" + localFullFileName + " end!!");
	}

}
