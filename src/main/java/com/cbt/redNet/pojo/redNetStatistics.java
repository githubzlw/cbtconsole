package com.cbt.redNet.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 网红统计
 * @author admin
 *
 */
public class redNetStatistics {

	  private  int   id ;
	  private  String  shareId ;  //分享shareId',
	  private  String  publishAddress ;   //发文地址'
	  private  int   redNetId ; //网红id',
	  private  int   clickSum ;  //'点击次数',
	  private  int    addGoodCarSum ; //加入购物车次数',
	  private  int    createOrderSum; //'下单次数',
	  private  String     createTime ;  //创建时间',
	  private  String    pushTime ;  //'发文时间',
	  private   String   bz ;  //'备注',
	  private   String  redNetName ;
	 
	  private   int  count ; 
	  private   int  totalPage ;
	  private   int   currentPage;
	  
	  
	  
	  
	  private   List<redNet>  list  = new ArrayList<redNet>();
	  
	  
	  
	public String getRedNetName() {
		return redNetName;
	}
	public void setRedNetName(String redNetName) {
		this.redNetName = redNetName;
	}
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShareId() {
		return shareId;
	}
	public void setShareId(String shareId) {
		this.shareId = shareId;
	}
	public String getPublishAddress() {
		return publishAddress;
	}
	public void setPublishAddress(String publishAddress) {
		this.publishAddress = publishAddress;
	}
	public int getRedNetId() {
		return redNetId;
	}
	public void setRedNetId(int redNetId) {
		this.redNetId = redNetId;
	}
	public int getClickSum() {
		return clickSum;
	}
	public void setClickSum(int clickSum) {
		this.clickSum = clickSum;
	}
	public int getAddGoodCarSum() {
		return addGoodCarSum;
	}
	public void setAddGoodCarSum(int addGoodCarSum) {
		this.addGoodCarSum = addGoodCarSum;
	}
	public int getCreateOrderSum() {
		return createOrderSum;
	}
	public void setCreateOrderSum(int createOrderSum) {
		this.createOrderSum = createOrderSum;
	}
	 
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPushTime() {
		return pushTime;
	}
	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public List<redNet> getList() {
		return list;
	}
	public void setList(List<redNet> list) {
		this.list = list;
	}
	  
	  
	  
}
