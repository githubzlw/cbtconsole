package com.cbt.warehouse.service;

import com.cbt.bean.SearchGoods;
import com.cbt.bean.ShippingBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ZoneShippingService {
	/*获取不同国家在不同运输方式下的运费*/
	/*countryid:国家id;weight:重量;type:运输方式;volume:体积大小;*/
	public float getCost(int countryid, float weight, int type, float volume, float singleweightmax);
	public List<ShippingBean> getShippingList(int countryid, float weight, float volume, float singleweightmax, int count);



	/**
	 * 2016/10/21
	 * 运费计算方法： 1.7KG 以下用 Epacket 美国运费算,  1.7KG 以上用佳成美国运费算。 另外把 Epacket 的 首重价从 7元左右 改到 15元
	 * 如果当前 方式是 Epacket			8-12天
	给客户选择	1	5-9天	增加金额 = Epacket 运费 *0.4
		2	3-5天	增加金额 = Epacket 运费


	如果当前 方式是 JCEX			5-9天
	给客户选择	1	3-5天	增加金额 = 7美元 + JCEX运费*0.2
		2	5-9天 Air Shipping  		如果价格比 原来还贵就不显示
		3	30-40天 Ocean Shipping  		如果价格比 原来还贵就不显示

	(欧洲怎么说		就不要再 按包裹拆分的 逻辑来算了，直接 按 重量算，但 说明会交 20%的 VAT Tax + Import Duty)--不拆包，全部国家都交vat
			在弹框 界面上 直接显示
			另外在 最后付款界面上面 也要 提醒一下
	 * @param isVlo 是否计算体积重
	 * @return
	 */
	public List<ShippingBean> getShippingBeans2(int countryid, double weights[], double prices[], double volumes[], String types[], int[] number, int isVlo);


	/**
	 * 获取每个类别的VAT 税率 和Duty税率
	 * 国家,产品总金额，product_type_price(产品金额,类别)，包裹数量
	 * @return
	 * 类别名称，VAT税率,Duty税率，该类别总金额
	 */
	public List<Object[]> getVatDuty(int countryid, double productPrice, List<Object[]> product_type_price, int packageNumber);

	/**
	 * 获取到美国 by air(3 days)   by sea(30 days) 的运费
	 * @param 重量
	 *
	 */
	public List<ShippingBean> getAirport_SeaAir(double weight);

	/**
	 * 保存用户提交的审核重量
	 * @return
	 */
	public int addVerifyWeight(@Param("userid") int userid, @Param("remark") String remark, int packaging);

	/**
	 * 单个商品运费
	 * @param countryid国家
	 * @param weight重量
	 * @param volumes体积
	 * @param goods_catid商品类别ID
	 * @param method选定的运输方式，如果=null则取最便宜的运费
	 * @return
	 */
	public double  getSpidertFreight(int countryid, double weight, double volumes, String goods_catid, String method);


	/**
	 * 计算多个商品运费(搜索页面)
	 * @return
	 */
	public List<SearchGoods> getProductFreight(int countryid, List<SearchGoods> searchGoods);

	/**
	 * 获取订单运费金额(excel表格生成的订单支付)
	 * @return
	 */
	public ShippingBean getOrderFreight(int countryid, List<Double> weights, List<String> type);

	/**
	 * 当前国家运费
	 * @return
	 */
	public double  getSpidertFreight(int countryid_now, double weight, double volumes, String goods_catid);

	/**
	 *获取其他国家的运费
	 * @param countryid
	 * @param weight
	 * @param method epaket还是佳成
	 * @param weight_three 产品单页重量在 300克以下则首重价*2
	 * @return fright运费
	 */
	public double[] getTransitJCEXFeight(int countryid, double[] weight, int weight_three);

	/**
	 *获取其他国家的运费
	 * @param countryid
	 * @param weight
	 * @param method epaket还是佳成
	 * @return fright运费
	 */
	public double getTransitJCEXFeight(int countryid, double weight);

	/**
	 *获取该国家是否存在E邮宝，存在=0，不存在=1
	 * @param
	 * @return
	 */
	public int exsitCountryEpacket(int countryid);
	/**
	 *获取其他国家的ePacket运费
	 * @param weight_three 产品单页重量在 300克以下则首重价*2
	 * @param
	 * @return
	 */
	public double getEpacketFreight(int countryid, double weight, int weight_three);
}
