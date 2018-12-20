package com.cbt.method.dao;

import java.util.List;
import java.util.Map;

import com.cbt.bean.OrderDetailsBean;

public interface IOrderDetailsDao {
	
	public List<OrderDetailsMethodBean> getOrderDetailsByOrderNo(int userid, String goodsids);

	public OrderDetailsBean getOrderDetails(int id);

	public int getAdminid(int userid);

	public void autoAddToCart(PreOrderList pol);

    public String getUserName(int id);

    /**
	 * 记录货源批量优惠价格
	 * @param orderid  销售订单
	 * @param goodsid   销售商品编号
	 * @param goods_url   销售商品ali链接
	 * @param goods_p_url  采购链接
	 * @param begin  批量采购起始数量
	 * @param end   批量采购结束数量
	 * @param price  批量采购价格
	 * @return   影响数据库行数
	 * @author 王宏杰  2017-06=20
	 */
    public int addPreferentialPrice(int pg_id, String goods_p_url, int begin, int end, double price, String goods_url);
    /**
	 * 记录preferential_goods
	 * @param goods_url  ali链接
	 * @param goods_p_url  实际采购链接
	 * @param goods_p_price  采购价格
	 * @return  主键
	 */
    public int addPreferentialGoods(String goods_url, String goods_p_url, double goods_p_price);

    public int addPreferentialPriceForSku(String orderid, int goodsid,
                                          String goods_url, String goods_p_url, String begin, String price, double goods_p_price);
	public int addPackgeParameters(String length1, String length2, String length3, String weight, String bagenum, String orderid);

	public void AddRecource(String type, int admid, int userid, int goodsdataid,
                            String goods_url, String googs_img, double goodsprice,
                            String goods_title, int googsnumber, String orderNo, int od_id,
                            int goodid, double price, String resource, int buycount,
                            String reason, String currency, String pname, boolean allReplcae, String shop_url, String shop_name, String address);
	
	List<Map<String,Object>> getOrderDetailByUser(int userid);
}
