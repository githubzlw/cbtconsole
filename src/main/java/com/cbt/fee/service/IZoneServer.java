package com.cbt.fee.service;

import com.cbt.bean.ShippingBean;
import com.cbt.bean.StateName;
import com.cbt.bean.ZoneBean;

import java.util.List;

public interface IZoneServer {
	public List<ZoneBean> getAllZone() ;
	public List<StateName> getStateName();
	/*获取不同国家在不同运输方式下的运费*/
	/*countryid:国家id;weight:重量;type:运输方式;volume:体积大小;*/
	public float getCost(int countryid, float weight, int type, float volume, float singleweightmax);
	public List<ShippingBean> getShippingList(int countryid, float weight, float volume, float singleweightmax, int count);

	/**
	 * 获取运费和运输方式列表
	 * @param countryid国家ID
	 * @param weights单价商品总重量
	 * @param prices单价商品总价格
	 * @param volumes单价商品总体积
	 * @param types商品类别
	 * @param number商品数量
	 * @return
	 */
	public List<ShippingBean> getShippingBeans(int countryid, double weights[], double prices[], double volumes[], String types[], int[] number);

	/**
	 * 生成1-100公斤重量运价表
	 * @return
	 */
	public void copyPricecost();
}
