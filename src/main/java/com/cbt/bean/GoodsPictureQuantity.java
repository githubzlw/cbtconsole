package com.cbt.bean;

public class GoodsPictureQuantity {

	private int id;
	private String pid;// 商品pid
	private int imgSize;// 橱窗图片剩余数量
	private int imgOriginalSize;// 橱窗图片原始数量
	private int imgDeletedSize;// 橱窗图片已删除数量
	private int typeSize;// 规格图片剩余数量
	private int typeOriginalSize;// 规格图片原始数量
	private int typeDeletedSize;// 规格图片已删除数量
	private int infoSize;// 详情图片原始数量
	private int infoOriginalSize;// 详情图片剩余数量
	private int infoDeletedSize;// 详情图片已删除数量

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getImgSize() {
		return imgSize;
	}

	public void setImgSize(int imgSize) {
		this.imgSize = imgSize;
	}

	public int getImgOriginalSize() {
		return imgOriginalSize;
	}

	public void setImgOriginalSize(int imgOriginalSize) {
		this.imgOriginalSize = imgOriginalSize;
	}

	public int getImgDeletedSize() {
		return imgDeletedSize;
	}

	public void setImgDeletedSize(int imgDeletedSize) {
		this.imgDeletedSize = imgDeletedSize;
	}

	public int getTypeSize() {
		return typeSize;
	}

	public void setTypeSize(int typeSize) {
		this.typeSize = typeSize;
	}

	public int getTypeOriginalSize() {
		return typeOriginalSize;
	}

	public void setTypeOriginalSize(int typeOriginalSize) {
		this.typeOriginalSize = typeOriginalSize;
	}

	public int getTypeDeletedSize() {
		return typeDeletedSize;
	}

	public void setTypeDeletedSize(int typeDeletedSize) {
		this.typeDeletedSize = typeDeletedSize;
	}

	public int getInfoSize() {
		return infoSize;
	}

	public void setInfoSize(int infoSize) {
		this.infoSize = infoSize;
	}

	public int getInfoOriginalSize() {
		return infoOriginalSize;
	}

	public void setInfoOriginalSize(int infoOriginalSize) {
		this.infoOriginalSize = infoOriginalSize;
	}

	public int getInfoDeletedSize() {
		return infoDeletedSize;
	}

	public void setInfoDeletedSize(int infoDeletedSize) {
		this.infoDeletedSize = infoDeletedSize;
	}

	@Override
	public String toString() {
		return "GoodsPictureQuantity [id=" + id + ", pid=" + pid + ", imgSize=" + imgSize + ", imgOriginalSize="
				+ imgOriginalSize + ", imgDeletedSize=" + imgDeletedSize + ", typeSize=" + typeSize
				+ ", typeOriginalSize=" + typeOriginalSize + ", typeDeletedSize=" + typeDeletedSize + ", infoSize="
				+ infoSize + ", infoOriginalSize=" + infoOriginalSize + ", infoDeletedSize=" + infoDeletedSize + "]";
	}

}
