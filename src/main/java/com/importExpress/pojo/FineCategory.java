package com.importExpress.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 品类精研的细分类
 * @ClassName FineCategory 
 * @Description TODO
 * @author Administrator
 * @date 2018年3月13日 下午2:09:45
 */
public class FineCategory implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String detailProductName;//产品线简述:
	private String positivekeywords;//英文正关键词:
	private String reversekeywords;//英文排除关键词:
	private int sid;//关键词对应的id
	private String catId1688;//搜索用的 1688类别ID:
	private int status;//0:可用,1:删除
	private Date createTime;//创建时间
	private String fileName;//图片地址
	private String imageUrl;
	private int isPublish;
	private String searchUrl;
	
	
	
	
	
	public String getSearchUrl() {
		return searchUrl;
	}
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
	
	
	public int getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(int isPublish) {
		this.isPublish = isPublish;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDetailProductName() {
		return detailProductName;
	}
	public void setDetailProductName(String detailProductName) {
		this.detailProductName = detailProductName;
	}
	public String getPositivekeywords() {
		return positivekeywords;
	}
	public void setPositivekeywords(String positivekeywords) {
		this.positivekeywords = positivekeywords;
	}
	public String getReversekeywords() {
		return reversekeywords;
	}
	public void setReversekeywords(String reversekeywords) {
		this.reversekeywords = reversekeywords;
	}
	
	
	public String getCatId1688() {
		return catId1688;
	}
	public void setCatId1688(String catId1688) {
		this.catId1688 = catId1688;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
	
}
