package com.cbt.warehouse.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 等级折扣表数据
 * @author Administrator
 *
 */
public class GradeDiscountBean implements Serializable{
	private static final long serialVersionUID = -5227524598492632785L;
	
	private int id;// int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
	private int gid;// int(2) DEFAULT NULL COMMENT '等级',
	private String gname;// varchar(50) DEFAULT NULL COMMENT '等级名',
	private double discount;// double(4,2) DEFAULT NULL COMMENT '折扣',
	private int minGradeValue;// int(11) DEFAULT NULL COMMENT '等级最小积分',
	private int maxGradeValue;// int(11) DEFAULT NULL COMMENT '等级最大积分',
	private int times;// int(4) DEFAULT NULL COMMENT '使用次数',
	private int valid;// int(2) NOT NULL DEFAULT '1' COMMENT '数据有效性',
	private Date createtime;// datetime DEFAULT NULL COMMENT '创建时间',
	
	private String grade;
	
	/**
	 * 主键id
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 主键id
	 * @return
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return
	 */
	public int getMaxGradeValue() {
		return maxGradeValue;
	}
	/**
	 * 等级最大积分
	 * @return
	 */
	public void setMaxGradeValue(int maxGradeValue) {
		this.maxGradeValue = maxGradeValue;
	}
	
	/**
	 * 折扣使用次数
	 * @return
	 */
	public int getTimes() {
		return times;
	}
	
	/**
	 * 折扣使用次数
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
	public Date getCreatetime() {
		return createtime;
	}
	
	/**
	 * 创建时间
	 * @return
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
	
}
