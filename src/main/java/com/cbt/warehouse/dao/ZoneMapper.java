package com.cbt.warehouse.dao;

import com.cbt.warehouse.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface ZoneMapper {

	public List<ZoneBean> getAllZone();

	public List<StateName> getStateName();

	public List<String> getShippingType(@Param("countryid") int countryid, @Param("shiptype") String shiptype);

	public List<Float> getFedexiePrice(@Param("weight") float weight, @Param("name") String name,
                                       @Param("type") String type);

	public List<Map<String, Float>> getEpacketPrice(String name);

	public List<Map<String, Float>> getDhlfbaPrice();

	public List<Map<String, Float>> getChinapostsalPrice(String name);

	public List<Map<String, Float>> getChinapostsurfacePrice(String name);

	public List<Map<String, Integer>> getSweden(String name);

	public List<Map<String, Float>> getTNT(String name);

	public List<Map<String, Float>> getKyd(String name);

	public List<Map<String, Float>> getEMS(String name);

	public List<Map<String, Float>> getEfast(String name);

	public List<Float> getFedexground(@Param("name") String name, @Param("weight") float weight);

	public List<Map<String, Object>> getPortpickup(String name);

	public Map<String, Float> getAirportpickup(int countryid);

	public List<Map<String, Float>> getAramex(String name);

	public List<Map<String, Float>> getDHL(String name);

	public List<Map<String, Float>> getDhlfbaother(String name);

	public Double getCheckfreePrice(int countryid);

	public List<Map<String, Object>> getVat_Duty(@Param("type_prices") List<String> type_prices);

	public List<Map<String, String>> getCheckfreeType(@Param("typeIds") String[] typeIds);

	/**
	 * 每个国家的 建议单包最大重量 和 单包免查报关金额(用来计算运费)
	 *
	 * @return
	 */
	public List<TransitCheckfree> getCheckFree();

	/**
	 * 获取交期信息
	 *
	 * @return
	 */
	public List<DeliveryDate> getDeliveryDate();

	/**
	 * 获取运费和运输方式列表
	 *
	 * @return
	 */
	public List<TransitPricecost> getPriceCost();

	/**
	 * 获取购物车计算的运费和运输方式列表
	 *
	 * @return
	 */
	public List<TransitPricecost> getPriceCost1();

	/**
	 * 查询类别是否带电(用来计算运费),是否抛货
	 *
	 * @return
	 */
	public List<TransitType> getTransitType(@Param("type") int type);

	public Map<String, Object> getCheckfreeLimit(int countryid);

	public List<Map<String, Object>> getFreightFates(@Param("countryid") int countryid,
                                                     @Param("sqlWeights") List<Double> sqlWeights, @Param("isweights1kg") int isweights1kg);

	public List<Map<String, Object>> getDHL_FedEX(@Param("countryid") int countryid,
                                                  @Param("sqlWeight") double sqlWeight, @Param("isDHL") int isDHL, @Param("isFex") int isFex);

	public List<Map<String, Object>> getAirport_SeaAir();

	/**
	 * 保存用户提交的审核重量
	 *
	 * @return
	 */
	public int addVerifyWeight(@Param("userid") int userid, @Param("remark") String remark,
                               @Param("packaging") int packaging);

	/**
	 * 单个商品运费
	 * 
	 * @return
	 */
	public List<Map<String, Object>> spidertFreight(@Param("countryid") int countryid, @Param("weight") double weight);

	public List<Integer> getIdFormCountry(@Param("country") String country);

	public List<CountryEpacketjcexBean> getCountryEpacket();

	/**
	 * 查询是drop shipping国家的英文名
	 * 
	 * @return
	 */
	public List<String> queryDropshippingCountry();
}
