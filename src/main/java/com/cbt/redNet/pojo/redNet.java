package com.cbt.redNet.pojo;

/**
 * 网红
 * @author admin
 *
 */
public class redNet {

	private     int     id ;
	private     String  redNetName ; //网红名称
	private     String  site ;           //合作发布站点或博客/YTB链接
	private     String  tomosonUrl ; //网红Tomoson资质评估链接
	private     String  pushSum;     //发文次数
	private     String  redNetOffer ; //红人报价
	private     String  email ;       //邮箱
	private     String  cooperationTime ; //'合作时间',
	private     String  bz ; //'备注'
	private    int  count ;
	private    int  totalPage ;
	private    int  currentPage;
	
	
	
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRedNetName() {
		return redNetName;
	}
	public void setRedNetName(String redNetName) {
		this.redNetName = redNetName;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getTomosonUrl() {
		return tomosonUrl;
	}
	public void setTomosonUrl(String tomosonUrl) {
		this.tomosonUrl = tomosonUrl;
	}
	public String getPushSum() {
		return pushSum;
	}
	public void setPushSum(String pushSum) {
		this.pushSum = pushSum;
	}
	public String getRedNetOffer() {
		return redNetOffer;
	}
	public void setRedNetOffer(String redNetOffer) {
		this.redNetOffer = redNetOffer;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCooperationTime() {
		return cooperationTime;
	}
	public void setCooperationTime(String cooperationTime) {
		this.cooperationTime = cooperationTime;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	
	
}
