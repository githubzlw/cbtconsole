package com.cbt.warehouse.pojo;

import java.io.Serializable;
import java.sql.Date;

/**
 * 热卖商品类别
 * 
 * @author jxw
 *
 */
public class HotSellingCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9988442356L;

	private int id;
	private String categoryId;// 商品分类id
	private String categoryName;// 商品分类名称
	private String showName;// 显示名称
	private String showImg;// 显示图片
	private String isOn;// 状态: 0:关闭 1:启用
	private int sorting;// 排序号
	private String remark;// 备注
	private int createAdmid;// 创建人id
	private Date createTime;// 创建时间
	private int updateAdmid;// 更新人id
	private Date updateTime;// 更新时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
	

	public String getShowImg() {
		return showImg;
	}

	public void setShowImg(String showImg) {
		this.showImg = showImg;
	}

	public String getIsOn() {
		return isOn;
	}

	public void setIsOn(String isOn) {
		this.isOn = isOn;
	}

	public int getSorting() {
		return sorting;
	}

	public void setSorting(int sorting) {
		this.sorting = sorting;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getCreateAdmid() {
		return createAdmid;
	}

	public void setCreateAdmid(int createAdmid) {
		this.createAdmid = createAdmid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getUpdateAdmid() {
		return updateAdmid;
	}

	public void setUpdateAdmid(int updateAdmid) {
		this.updateAdmid = updateAdmid;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "HotSellingCategory [id=" + id + ", categoryId=" + categoryId + ", categoryName=" + categoryName
				+ ", showName=" + showName + ", isOn=" + isOn + ", sorting=" + sorting + ", remark=" + remark
				+ ", createAdmid=" + createAdmid + ", createTime=" + createTime + ", updateAdmid=" + updateAdmid
				+ ", updateTime=" + updateTime + "]";
	}

}
