package com.importExpress.pojo;

public class GoodsInfoSpiderPO {

	private long id;

	private String pid;

	private String aliPid;

	private String price;

	private String aliPrice;
	
	private String imgPath;

	private String aliImgPath;

	private int flag;

	// 0-默认值 待对标和不对标；1-对标；2-不对标；
	private int marked = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAliPid() {
		return aliPid;
	}

	public void setAliPid(String aliPid) {
		this.aliPid = aliPid;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAliPrice() {
		return aliPrice;
	}

	public void setAliPrice(String aliPrice) {
		this.aliPrice = aliPrice;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getAliImgPath() {
		return aliImgPath;
	}

	public void setAliImgPath(String aliImgPath) {
		this.aliImgPath = aliImgPath;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getMarked() {
		return marked;
	}

	public void setMarked(int marked) {
		this.marked = marked;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", pid=" + pid + ", aliPid=" + aliPid + ", price=" + price
				+ ", aliPrice=" + aliPrice + ", imgPath=" + imgPath + ", aliImgPath=" + aliImgPath + ", flag=" + flag
				+ ", marked=" + marked + "]";
	}

}