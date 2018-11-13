package com.cbt.bean;

import java.util.regex.Pattern;

/**
 * @author lizhanjun
 * 浏览器浏览记录
 */
public class RecentViewBean {
	private int id;
	
	/**
	 * 用户id
	 */
	private int uid;
	/**
	 * 用户session
	 */
	private String sessionid;
	/**
	 * 商品id
	 */
	private String pid;
	/**
	 * 商品名称
	 */
	private String pname;
	/**
	 * 商品详情链接地址
	 */
	private String purl;
	/**
	 * 商品图片链接地址
	 */
	private String imgUrl;
	/**
	 * 商品价格
	 */
	private String price;
	/**
	 * 最小订单
	 */
	private String minOrder;
	
	/**
	 * 搜索关键字
	 */
	private String keywords;
	
	/**
	 * google进入关键字
	 */
	private String seachwords;
	
	/**
	 * ip地址
	 */
	private String ip;
	
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
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPurl() {
		return purl;
	}
	public void setPurl(String purl) {
		this.purl = purl;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		if(imgUrl!=null){
			int imgUrl_length = imgUrl.length();
			if(imgUrl_length>3&&".".equals(imgUrl.substring(imgUrl_length-1))){
				imgUrl +=imgUrl+"jpg";
			}
			else if(Pattern.compile("(\\.jpg_*\\d+x\\d+.jpg)").matcher(imgUrl).find()){
				imgUrl = imgUrl.replaceAll("(\\.jpg_*\\d+x\\d+.jpg)",".jpg");
			}else if(Pattern.compile("(.32x32.jpg)").matcher(imgUrl).find()){
				imgUrl = imgUrl.replaceAll(".32x32.jpg",".310x310.jpg");
			}
		}
		this.imgUrl = imgUrl;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getMinOrder() {
		return minOrder;
	}
	public void setMinOrder(String minOrder) {
		this.minOrder = minOrder;
	}
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIp() {
		return ip;
	}
	
	
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getSeachwords() {
		return seachwords;
	}
	public void setSeachwords(String seachwords) {
		this.seachwords = seachwords;
	}
	@Override
	public String toString() {
		return "RecentViewBean [id=" + id + ", pid=" + pid + ", pname=" + pname
				+ ", purl=" + purl + ", imgUrl=" + imgUrl + ", price=" + price
				+ ", minOrder=" + minOrder + "]";
	}
}
