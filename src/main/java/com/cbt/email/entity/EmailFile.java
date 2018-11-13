package com.cbt.email.entity;

/**
 * 邮件附件
 */
public class EmailFile {
	/** 附件id*/
	private int id;
	/** 邮件id*/
	private int eid;
	/** 附件文件名称*/
	private String fileName;
	/** 附件文件类型*/
	private String type;
	/** 用来标记附件文件是否为正文中的图片或其它*/
	private int flag;
	/** 用来标记附件文件是否已被替换*/
	private int displace;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public int getDisplace() {
		return displace;
	}
	public void setDisplace(int replace) {
		this.displace = replace;
	}
	@Override
	public String toString() {
		return "EmailFile [id=" + id + ", eid=" + eid + ", fileName="
				+ fileName + ", type=" + type + ", flag=" + flag + ", displace="
				+ displace + "]";
	}
	
}
