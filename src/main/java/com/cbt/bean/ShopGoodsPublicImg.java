package com.cbt.bean;

/**
 * 
 * @ClassName ShopGoodsPublicImg
 * @Description 店铺商品公共图片展示
 * @author Jxw
 * @date 2018年3月2日
 */
public class ShopGoodsPublicImg {
	private String imgUrl;// 图片路径
	private String pids;// pid组合，逗号分开
	private int totalNum;// 图片对应pid的个数

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	@Override
	public String toString() {
		return "{\"imgUrl\":\"" + imgUrl + "\", \"pids\":\"" + pids + "\", \"totalNum\":\"" + totalNum + "\"}";
	}

}
