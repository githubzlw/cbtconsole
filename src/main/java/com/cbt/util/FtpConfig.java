package com.cbt.util;

public class FtpConfig {
	public static final String REMOTE_LOCAL_PATH = "/usr/local/goodsimg/importcsvimg/";

	private String ftpURL;
	private String ftpPort;
	private String ftpUserName;
	private String ftpPassword;
	private String localDiskPath;
	private String localShowPath;
	private String remoteShowPath;
	private boolean ok;

	public String getFtpURL() {
		return ftpURL;
	}

	public void setFtpURL(String ftpURL) {
		this.ftpURL = ftpURL;
	}

	public String getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUserName() {
		return ftpUserName;
	}

	public void setFtpUserName(String ftpUserName) {
		this.ftpUserName = ftpUserName;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getLocalDiskPath() {
		return localDiskPath;
	}

	public void setLocalDiskPath(String localDiskPath) {
		this.localDiskPath = localDiskPath;
	}

	public String getLocalShowPath() {
		return localShowPath;
	}

	public void setLocalShowPath(String localShowPath) {
		this.localShowPath = localShowPath;
	}

	public String getRemoteShowPath() {
		return remoteShowPath;
	}

	public void setRemoteShowPath(String remoteShowPath) {
		this.remoteShowPath = remoteShowPath;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	

}
