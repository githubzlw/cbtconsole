package com.cbt.bean;

import java.util.List;

/**
 * 
 * @ClassName ShopInfoBean
 * @Description 店铺类别关键词重量信息
 * @author Jxw
 * @date 2018年2月23日
 */
public class ShopInfoKeyWeight extends ShopInfoBean {
	private List<ShopCatidWeight> ctwts;// 类别关键词重量
	private int tooLightNum;
	private int tooHeavyNum;
	private int weightZoneNum;

	public List<ShopCatidWeight> getCtwts() {
		return ctwts;
	}

	public void setCtwts(List<ShopCatidWeight> ctwts) {
		this.ctwts = ctwts;
	}

	public int getTooLightNum() {
		return tooLightNum;
	}

	public void setTooLightNum(int tooLightNum) {
		this.tooLightNum = tooLightNum;
	}

	public int getTooHeavyNum() {
		return tooHeavyNum;
	}

	public void setTooHeavyNum(int tooHeavyNum) {
		this.tooHeavyNum = tooHeavyNum;
	}

	public int getWeightZoneNum() {
		return weightZoneNum;
	}

	public void setWeightZoneNum(int weightZoneNum) {
		this.weightZoneNum = weightZoneNum;
	}

}
