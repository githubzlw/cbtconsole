package com.cbt.website.bean;

import java.util.Arrays;
import java.util.List;

public class SearchResultInfo {

	private String tbOrderIdPositions; //订单所有商品的仓库存放位置
	private String position;    //仓库位置
	private String taobao_itemid;
	private String taobao_orderid;//淘宝订单id
	private int userid;
	private String orderid;
	private int goodsid;
	private int goodsdataid;
	private String goods_name;
	private String goods_url;
	private String goods_p_url;
	private String goods_img_url;
	private String[] img;
	private String currency;
	private Double goods_price;
	private Double goods_p_price;
	private int purchase_state;
	private int usecount;
	private int buycount;
	private int ordercount;
	private int orderbuycount;
	// car_type
	private String strcar_type;
	//是否补货
	private String goodsType;
	private String remark;
	private List<SearchTaobaoInfo> imgList;
	//入库商品状态1.到货了;2.该到没到;3.破损;4.有疑问;5.数量不够
	private String goodstatus;
	private String orderRemark;//订单备注
	private List<Object[]> orderremark;//订单备注对内

	//来源  是亚马逊还是ebay2
	private String storeName;

	private int checked;//是否验货

	private String seilUnit;

	private String gcUnit;

	private String odRemark;//客户商品备注

	private String goods_pid;//销售商品编号

	private String invoice;

	private String isDropshipOrder;
	private String shop_id;
	private List<String> bh_shop_id;


	private String weight;
	private String createtime;
	private String catid;
	private String evaluation;
	private int order_num;//1688包裹对应的销售订单数量
	private String isExitPhone;
	public String getIsExitPhone() {
		return isExitPhone;
	}

	public void setIsExitPhone(String isExitPhone) {
		this.isExitPhone = isExitPhone;
	}

	public String getAuthorizedFlag() {
		return authorizedFlag;
	}

	public void setAuthorizedFlag(String authorizedFlag) {
		this.authorizedFlag = authorizedFlag;
	}

	private String authorizedFlag;
	public String getOdid() {
		return odid;
	}

	public void setOdid(String odid) {
		this.odid = odid;
	}

	private String odid;
	public int getOrder_num() {
		return order_num;
	}

	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}
	public List<String> getBh_shop_id() {
		return bh_shop_id;
	}
	public void setBh_shop_id(List<String> bh_shop_id) {
		this.bh_shop_id = bh_shop_id;
	}
	public String getIsDropshipOrder() {
		return isDropshipOrder;
	}
	public void setIsDropshipOrder(String isDropshipOrder) {
		this.isDropshipOrder = isDropshipOrder;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getGoods_pid() {
		return goods_pid;
	}
	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}
	public String getOdRemark() {
		return odRemark;
	}
	public void setOdRemark(String odRemark) {
		this.odRemark = odRemark;
	}
	public String getGcUnit() {
		return gcUnit;
	}
	public void setGcUnit(String gcUnit) {
		this.gcUnit = gcUnit;
	}
	public String getSeilUnit() {
		return seilUnit;
	}
	public void setSeilUnit(String seilUnit) {
		this.seilUnit = seilUnit;
	}
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	public String getTbOrderIdPositions() {
		return tbOrderIdPositions;
	}
	public void setTbOrderIdPositions(String tbOrderIdPositions) {
		this.tbOrderIdPositions = tbOrderIdPositions;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getTaobao_orderid() {
		return taobao_orderid;
	}
	public void setTaobao_orderid(String taobao_orderid) {
		this.taobao_orderid = taobao_orderid;
	}
	public String getTaobao_itemid() {
		return taobao_itemid;
	}
	public void setTaobao_itemid(String taobao_itemid) {
		this.taobao_itemid = taobao_itemid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public int getGoodsdataid() {
		return goodsdataid;
	}
	public void setGoodsdataid(int goodsdataid) {
		this.goodsdataid = goodsdataid;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_url() {
		return goods_url;
	}
	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}
	public String getGoods_p_url() {
		return goods_p_url;
	}
	public void setGoods_p_url(String goods_p_url) {
		this.goods_p_url = goods_p_url;
	}
	public String getGoods_img_url() {
		return goods_img_url;
	}
	public void setGoods_img_url(String goods_img_url) {
		this.goods_img_url = goods_img_url;
	}
	public String[] getImg() {
		return img;
	}
	public void setImg(String[] img) {
		this.img = img;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(Double goods_price) {
		this.goods_price = goods_price;
	}
	public Double getGoods_p_price() {
		return goods_p_price;
	}
	public void setGoods_p_price(Double goods_p_price) {
		this.goods_p_price = goods_p_price;
	}
	public int getPurchase_state() {
		return purchase_state;
	}
	public void setPurchase_state(int purchase_state) {
		this.purchase_state = purchase_state;
	}
	public int getUsecount() {
		return usecount;
	}
	public void setUsecount(int usecount) {
		this.usecount = usecount;
	}
	public int getBuycount() {
		return buycount;
	}
	public void setBuycount(int buycount) {
		this.buycount = buycount;
	}
	public int getOrdercount() {
		return ordercount;
	}
	public void setOrdercount(int ordercount) {
		this.ordercount = ordercount;
	}
	public int getOrderbuycount() {
		return orderbuycount;
	}
	public void setOrderbuycount(int orderbuycount) {
		this.orderbuycount = orderbuycount;
	}
	public String getStrcar_type() {
		return strcar_type;
	}
	public void setStrcar_type(String strcar_type) {
		this.strcar_type = strcar_type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<SearchTaobaoInfo> getImgList() {
		return imgList;
	}
	public void setImgList(List<SearchTaobaoInfo> imgList) {
		this.imgList = imgList;
	}
	/**
	 * @return 入库商品状态1.到货了;2.该到没到;3.破损;4.有疑问;5.数量不够
	 */
	public String getGoodstatus() {
		return goodstatus;
	}
	/**
	 * @return the 订单备注
	 */
	public String getOrderRemark() {
		return orderRemark;
	}
	/**
	 * @param orderRemark the 订单备注
	 */
	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}
	/**
	 * @param 入库商品状态1.到货了;2.该到没到;3.破损;4.有疑问;5.数量不够
	 */
	public void setGoodstatus(String goodstatus) {
		this.goodstatus = goodstatus;
	}
	/**
	 * @return 订单备注对内
	 */
	public List<Object[]> getOrderremark() {
		return orderremark;
	}
	/**
	 * @param orderremark 订单备注对内
	 */
	public void setOrderremark(List<Object[]> orderremark) {
		this.orderremark = orderremark;
	}
	/**
	 * @return 是否补货
	 */
	public String getGoodsType() {
		return goodsType;
	}
	/**
	 * @param goodsType 是否补货
	 */
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	/**
	 * @return 亚马逊还是ebay2
	 */
	public String getStoreName() {
		return storeName;
	}
	/**
	 * @param 亚马逊还是ebay2
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	@Override
	public String toString() {
		return "SearchResultInfo [taobao_itemid=" + taobao_itemid + ", userid="
				+ userid + ", orderid=" + orderid + ", goodsid=" + goodsid
				+ ", goodsdataid=" + goodsdataid + ", goods_name=" + goods_name
				+ ", goods_url=" + goods_url + ", goods_p_url=" + goods_p_url
				+ ", goods_img_url=" + goods_img_url + ", img="
				+ Arrays.toString(img) + ", currency=" + currency
				+ ", goods_price=" + goods_price + ", goods_p_price="
				+ goods_p_price + ", purchase_state=" + purchase_state
				+ ", usecount=" + usecount + ", buycount=" + buycount
				+ ", ordercount=" + ordercount + ", orderbuycount="
				+ orderbuycount + ", strcar_type=" + strcar_type + ", remark="
				+ remark + ", imgList=" + imgList + "]";
	}




}


    
    
    
    