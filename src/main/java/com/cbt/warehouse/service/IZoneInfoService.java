package com.cbt.warehouse.service;

import com.cbt.warehouse.pojo.*;

import java.util.List;


/**
 * 获取运费相关数据表数据
 */
public interface IZoneInfoService {

	/**
	 * 每个国家的 建议单包最大重量 和 单包免查报关金额(用来计算运费)
	 * @return
	 */
	public List<TransitCheckfree> getCheckFree();
	
	/**
	 * 每个国家的 建议单包最大重量 和 单包免查报关金额(用来计算运费)
	 * @return
	 */
	public List<DeliveryDate> getDeliveryDate();
	
	/**
	 *  获取运费和运输方式列表
	 * @return
	 */
	public List<TransitPricecost> getPriceCost();

	/**
	 *  获取购物计算的运费和运输方式列表
	 * @return
	 */
	public List<TransitPricecost> getPriceCost1();
	/**
	 * 查询类别是否带电(用来计算运费),是否是抛货
	 * type-0查询带电类别列表，1查询抛货类别列表
	 * @return
	 */
	public List<TransitType> getTransitType(int type);

	/**
	 * 查询存在E邮宝的国家
	 * @return
	 */
	public List<CountryEpacketjcexBean> getCountryEpacket();
}
