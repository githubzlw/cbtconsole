package com.cbt.warehouse.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户等级信息
 * @author Administrator
 *
 */
public class UserGradeGrowthBean implements Serializable{

	private static final long serialVersionUID = -6835079195381074234L;
	private int id;// int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
	private int userId;// int(11) NOT NULL COMMENT '用户id',
	private int gradeDisId;// int(11) DEFAULT NULL COMMENT '等级折扣id',
	private int grade;//int(2) DEFAULT NULL COMMENT '等级',
	private int growthValue;// int(11) DEFAULT NULL COMMENT '总积分',
	private double discount;// double(4,2) DEFAULT NULL,
	private int times;// int(4) DEFAULT NULL COMMENT '折扣使用次数',
	private int valid;// int(2) NOT NULL DEFAULT '1' COMMENT '是否有效',
	private Date createTime;// datetime DEFAULT NULL COMMENT '创建时间',
	private Date updateTime;// datetime DEFAULT NULL COMMENT '修改时间',
	
	private int gid;//等级
	private String gname;//等级名
	private int minGradeValue;//等级最小积分
	private int maxGradeValue;//等级最大积分
	/**
	 * 主键ID
	 * @return
	 */
	public int getId() {
		return id;
	}
	/**
	 * 主键ID
	 * @return
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 用户id
	 * @return
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * 用户id
	 * @return
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * 等级折扣id
	 * @return
	 */
	public int getGradeDisId() {
		return gradeDisId;
	}
	/**
	 * 等级折扣id
	 * @return
	 */
	public void setGradeDisId(int gradeDisId) {
		this.gradeDisId = gradeDisId;
	}
	/**
	 * 总积分
	 * @return
	 */
	public int getGrowthValue() {
		return growthValue;
	}
	/**
	 * 总积分
	 * @return
	 */
	public void setGrowthValue(int growthValue) {
		this.growthValue = growthValue;
	}
	/**
	 * 折扣
	 * @return
	 */
	public double getDiscount() {
		return discount;
	}
	
	/**
	 * 折扣
	 * @return
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	/**
	 * 折扣使用剩余次数
	 * @return
	 */
	public int getTimes() {
		return times;
	}
	/**
	 * 折扣使用剩余次数
	 * @return
	 */
	public void setTimes(int times) {
		this.times = times;
	}
	/**
	 * 是否有效
	 * @return
	 */
	public int getValid() {
		return valid;
	}
	/**
	 * 是否有效
	 * @return
	 */
	public void setValid(int valid) {
		this.valid = valid;
	}
	/**
	 * 创建时间
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 创建时间
	 * @return
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 修改时间
	 * @return
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 修改时间
	 * @return
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 等级名
	 * @return
	 */
	public String getGname() {
		return gname;
	}
	/**
	 * 等级名
	 * @return
	 */
	public void setGname(String gname) {
		this.gname = gname;
	}
	/**
	 * 等级
	 * @return
	 */
	public int getGid() {
		return gid;
	}
	/**
	 * 等级
	 * @return
	 */
	public void setGid(int gid) {
		this.gid = gid;
	}
	/**
	 * 等级最小积分
	 * @return
	 */
	public int getMinGradeValue() {
		return minGradeValue;
	}
	/**
	 * 等级最小积分
	 * @return
	 */
	public void setMinGradeValue(int minGradeValue) {
		this.minGradeValue = minGradeValue;
	}
	/**
	 * 等级最大积分
	 * @param minGradeValue
	 */
	public int getMaxGradeValue() {
		return maxGradeValue;
	}
	/**
	 * 等级最大积分
	 * @param minGradeValue
	 */
	public void setMaxGradeValue(int maxGradeValue) {
		this.maxGradeValue = maxGradeValue;
	}
	/**
	 * 等级
	 * @return
	 */
	public int getGrade() {
		return grade;
	}
	/**
	 * 等级
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}

}
