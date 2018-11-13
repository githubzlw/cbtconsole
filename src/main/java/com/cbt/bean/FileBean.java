package com.cbt.bean;

import java.util.List;

public class FileBean {
	private int fileLevel;//
	private String fileParent;//
	private String filePath;//
	private int isFile;//
	private int isImg;//
	private int index;
	List<FileBean> list;
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public List<FileBean> getList() {
		return list;
	}
	public void setList(List<FileBean> list) {
		this.list = list;
	}
	
	public int getFileLevel() {
		return fileLevel;
	}
	public void setFileLevel(int fileLevel) {
		this.fileLevel = fileLevel;
	}
	public String getFileParent() {
		return fileParent;
	}
	public void setFileParent(String fileParent) {
		this.fileParent = fileParent;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getIsFile() {
		return isFile;
	}
	public void setIsFile(int isFile) {
		this.isFile = isFile;
	}
	public int getIsImg() {
		return isImg;
	}
	public void setIsImg(int isImg) {
		this.isImg = isImg;
	}
	@Override
	public String toString() {
		return String
				.format("FileBean [fileLevel=%s, fileParent=%s, filePath=%s, isFile=%s, isImg=%s]",
						fileLevel, fileParent, filePath, isFile, isImg);
	}
	
	

}
