package com.cbt.searchByPic.bean;

import java.io.Serializable;
import java.sql.Date;

public class CustomerRequireBean implements Serializable{
	
	private static final long serialVersionUID = -6448521189346236538L;
	private   int          id; 
	private   String     keyWords;
	private   String   comments; 
	private   String    email ; 
	private   int     quantity ; //数量 
	private   double   minu; //最低价格
	private   double    maxu; //最高价格
	private   Date   createtime; 
	private   int  count ;  //总数 
	private   int flag ;  //是否已发送邮件标识
	private   int  index_id ; // search_index 表 id
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getMinu() {
		return minu;
	}
	public void setMinu(double minu) {
		this.minu = minu;
	}
	public double getMaxu() {
		return maxu;
	}
	public void setMaxu(double maxu) {
		this.maxu = maxu;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getIndex_id() {
		return index_id;
	}
	public void setIndex_id(int index_id) {
		this.index_id = index_id;
	}
	
	
	
	
	
}
