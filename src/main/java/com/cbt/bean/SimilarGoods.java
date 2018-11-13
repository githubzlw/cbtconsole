package com.cbt.bean;

/**
 * 
 * @ClassName SimilarGoods
 * @Description 相似商品
 * @author Jxw
 * @date 2018年2月6日
 */
public class SimilarGoods {
	private int id;
	private String mainPid;// 主商品pid
	private String similarPid;// 相似商品pid
	private String similarGoodsImg;// 相似商品主图
	private int adminId;// 创建人id
	private String createTime;// 创建时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMainPid() {
		return mainPid;
	}

	public void setMainPid(String mainPid) {
		this.mainPid = mainPid;
	}

	public String getSimilarPid() {
		return similarPid;
	}

	public void setSimilarPid(String similarPid) {
		this.similarPid = similarPid;
	}

	public String getSimilarGoodsImg() {
		return similarGoodsImg;
	}

	public void setSimilarGoodsImg(String similarGoodsImg) {
		this.similarGoodsImg = similarGoodsImg;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"mainPid\":\"" + mainPid + "\", \"similarPid\":\"" + similarPid
				+ "\", \"similarGoodsImg\":\"" + similarGoodsImg + "\", \"adminId\":\"" + adminId
				+ "\", \"createTime\":\"" + createTime + "\"}";
	}

}
