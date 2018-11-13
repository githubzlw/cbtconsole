package com.cbt.bean;

public class ShopGoodsLocalImg {
	private String pid;
	private String lpImg;
	private String imgMd5;
	private long imgSize;
	private int isCheck;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getLpImg() {
		return lpImg;
	}

	public void setLpImg(String lpImg) {
		this.lpImg = lpImg;
	}

	public String getImgMd5() {
		return imgMd5;
	}

	public void setImgMd5(String imgMd5) {
		this.imgMd5 = imgMd5;
	}

	public long getImgSize() {
		return imgSize;
	}

	public void setImgSize(long imgSize) {
		this.imgSize = imgSize;
	}

	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}

	@Override
	public String toString() {
		return "{\"pid\":\"" + pid + "\", \"lpImg\":\"" + lpImg + "\", \"imgMd5\":\"" + imgMd5 + "\", \"imgSize\":\""
				+ imgSize + "\", \"isCheck\":\"" + isCheck + "\"}";
	}

}
