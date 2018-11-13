package com.cbt.fee.dao;

import com.cbt.bean.StateName;
import com.cbt.bean.TransitPricecost;
import com.cbt.bean.TransitType;
import com.cbt.bean.ZoneBean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IZoneDao {
	/*计算运费的分区表--wanyang*/
	public List<ZoneBean> getAllZone();
	/**
	 * wanyang
	 * 获取州名
	 */
	public List<StateName> getStateName();
	//countryid为zone表的id，shiptype为4种运费计算方式
	public String getShippingType(int countryid, String shiptype);
	//根据映射获取对应国家在FedexIE方式下的价格,name为国家名，对应数据库字段；type为运费方式的表名
	public float getFedexiePrice(float weight, String name, String type);
	//根据映射获取对应国家在epacket方式下的价格,name为国家名
	public float getEpacketPrice(String name, float weight);
	//根据映射获取对应国家在dhlfba方式下的价格,name为国家名
	public float getDhlfbaPrice(String name, float weight);
	//根据映射获取对应国家在China Post Sal方式下的价格,name为国家名
	public float getChinapostsalPrice(String name, float weight);
	//根据映射获取对应国家在China Post Surface方式下的价格,name为国家名
	public float getChinapostsurfacePrice(String name, float weight);
	//根据映射获取对应国家在Sweden瑞典邮政小包方式下的价格,name为地区名
	public float getSweden(String name, float weight);
	//根据映射获取对应国家在TNT荷兰小包价格方式下的价格,name为地区id
	public float getTNT(String name, float weight);
	//根据映射获取对应国家在KYD 国际小包价格方式下的价格,name为地区id
	public float getKyd(String name, float weight);
	//根据映射获取对应国家在EMS价格方式下的价格,name为地区id
	public float getEMS(String name, float weight);
	//根据映射获取对应国家在E特快价格方式下的价格,name为地区id
	public float getEfast(String name, float weight);
	//根据映射获取对应国家在FedexGround价格方式下的价格,name为地区id
	public float getFedexground(String name, float weight);
	//根据映射获取对应国家在Port Pickup价格方式下的价格,name为地区id
	public float getPortpickup(String name, float weight);
	//根据映射获取对应国家在Airport Pickup价格方式下的价格,name为地区id
	public float getAirportpickup(int countryid, float weight);
	//根据映射获取对应国家在Aramex中东价格方式下的价格,name为地区id

	public float getAramex(String name, float weight);
	//根据映射获取对应国家在香港DHL价格方式下的价格,name为地区id
	public float getDHL(String name, float weight);
	//根据映射获取对应国家在香港DHLFBA--OTHER价格方式下的价格,name为地区id
	public float getDhlfbaother(String name, float weight);

	/**
	 * 通过国家获取建议单包最大重量 和 单包免查报关金额
	 * @return
	 */
	public double[] getCheckfreeLimit(int countryid);

	/**
	 * 运输类别表
	 * @return
	 */
	public List<TransitType> getCheckfreeType(String[] typeIds);

	/**
	 * 获取运价表
	 * 重量，国家，是否带电,是否带电包裹的总重量-第一个是不带电值-第二个是带电值
	 * sqlWeights-sql重量查询条件
	 */
	public List<TransitPricecost> getFreightFates(int countryid);
	/**
	 * 获取运价表DHL和Fex
	 * 重量，国家，是否带电,是否带电包裹的总重量-第一个是不带电值-第二个是带电值
	 * sqlWeights-sql重量查询条件
	 */
	public LinkedHashMap<String, List<Object[]>> getDHL_FedEX(int countryid, double sqlWeight, int isDHL, int isFex);
	/**
	 * 获取国家最大免查金额
	 * @return
	 */
	public double getCheckfreePrice(int countryid);

	/**
	 * 获取运价表
	 * @return
	 * 
	 */
	public Map<String, List<TransitPricecost>> getTransitPricecost();
	
	/**
	 * 获取运价表附表
	 * @return
	 * 
	 */
	public void addTransitPricecost(List<TransitPricecost> transitPricecost);
}
