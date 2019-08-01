package com.cbt.pojo;

import java.io.Serializable;

public class TaoBaoOrderInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//序号
	private String tbOr1688;//0：淘宝 1 ：1688
	private String orderid;//淘宝订单号
	private String orderdate;//订单日期
	private String seller;//淘宝卖家名称
	private String totalprice;//订单总金额
	private String orderstatus;//订单状态
	private String itemname;//商品名称
	private String itemid;
	private String itemprice;//商品单价
	private String itemqty;//商品数量
	private int totalqty;//订单商品总数量
	private String sku;//商品规格
	private String shipno;//快递单号
	private String shipper;//快递公司
	private String shipstatus;//快递详情
	private String itemurl;//商品练级
	private String imgurl;//图片链接
	private String username;//采购人
	private String creatTime;//记录创建时间
	private int TaobaoStateE;//订单状态
	private String merchantorderid;//商户订单号
	private String paytreasureid;//支付宝交易号
	private String preferential;//运费
	private String paydata;
	private String page;
	private int opsid;
	private int iid;
	private String opsorderid;
	private int buycount;
	private String storageTime;
	private String barcode;//库位
	private String remark;
	private String delivery_date;
	private String records;
    private int is_processing;//采购处理标识
    private String goodsImgUrl;//aliexpress图片
    private int is_exit;//是否入库
    private String in_time;//入库时间
    private String operation_remark;//处理备注
    private String support_info;//卖家承诺图片
    private String operating;
    private String counts;//已退货数量
	private String id_state;//1688订单是否入库
	private String inspection_result;//验货结果
	private String sample_goods;//是否为样品采购
	private String return_goods;//是否取消商品
	private String sOrderid;
	private String bId;
	private String state;

	public String getOdid() {
		return odid;
	}

	public void setOdid(String odid) {
		this.odid = odid;
	}

	private String odid;
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getbId() {
		return bId;
	}

	public void setbId(String bId) {
		this.bId = bId;
	}

	public String getsOrderid() {
		return sOrderid;
	}

	public void setsOrderid(String sOrderid) {
		this.sOrderid = sOrderid;
	}

	public String getTb_1688_itemid() {
		return tb_1688_itemid;
	}

	public void setTb_1688_itemid(String tb_1688_itemid) {
		this.tb_1688_itemid = tb_1688_itemid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	private String tb_1688_itemid;
	private String createtime;
	public String getId_state() {
		return id_state;
	}

	public void setId_state(String id_state) {
		this.id_state = id_state;
	}

	public String getInspection_result() {
		return inspection_result;
	}

	public void setInspection_result(String inspection_result) {
		this.inspection_result = inspection_result;
	}

	public String getSample_goods() {
		return sample_goods;
	}

	public void setSample_goods(String sample_goods) {
		this.sample_goods = sample_goods;
	}

	public String getReturn_goods() {
		return return_goods;
	}

	public void setReturn_goods(String return_goods) {
		this.return_goods = return_goods;
	}

	public String getCounts() {
		return counts;
	}
	public void setCounts(String counts) {
		this.counts = counts;
	}
	public String getOperating() {
		return operating;
	}
	public void setOperating(String operating) {
		this.operating = operating;
	}
	public String getSupport_info() {
		return support_info;
	}
	public void setSupport_info(String support_info) {
		this.support_info = support_info;
	}
	public String getOperation_remark() {
		return operation_remark;
	}
	public void setOperation_remark(String operation_remark) {
		this.operation_remark = operation_remark;
	}
	public String getIn_time() {
		return in_time;
	}
	public void setIn_time(String in_time) {
		this.in_time = in_time;
	}
	public int getIs_exit() {
		return is_exit;
	}
	public void setIs_exit(int is_exit) {
		this.is_exit = is_exit;
	}
	public String getGoodsImgUrl() {
		return goodsImgUrl;
	}
	public void setGoodsImgUrl(String goodsImgUrl) {
		this.goodsImgUrl = goodsImgUrl;
	}
	public int getIs_processing() {
		return is_processing;
	}
	public void setIs_processing(int is_processing) {
		this.is_processing = is_processing;
	}
	public String getRecords() {
		return records;
	}
	public void setRecords(String records) {
		this.records = records;
	}
	public String getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getStorageTime() {
		return storageTime;
	}
	public void setStorageTime(String storageTime) {
		this.storageTime = storageTime;
	}
	public String getPaydata() {
		return paydata;
	}
	public void setPaydata(String paydata) {
		this.paydata = paydata;
	}
	public String getPreferential() {
		return preferential;
	}
	public void setPreferential(String preferential) {
		this.preferential = preferential;
	}
	public int getBuycount() {
		return buycount;
	}
	public void setBuycount(int buycount) {
		this.buycount = buycount;
	}
	public String getOpsorderid() {
		return opsorderid;
	}
	public void setOpsorderid(String opsorderid) {
		this.opsorderid = opsorderid;
	}
	public int getIid() {
		return iid;
	}
	public void setIid(int iid) {
		this.iid = iid;
	}
	public int getOpsid() {
		return opsid;
	}
	public void setOpsid(int opsid) {
		this.opsid = opsid;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTbOr1688() {
		return tbOr1688;
	}
	public void setTbOr1688(String tbOr1688) {
		this.tbOr1688 = tbOr1688;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(String totalprice) {
		this.totalprice = totalprice;
	}
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getItemprice() {
		return itemprice;
	}
	public void setItemprice(String itemprice) {
		this.itemprice = itemprice;
	}
	public String getItemqty() {
		return itemqty;
	}
	public void setItemqty(String itemqty) {
		this.itemqty = itemqty;
	}
	public int getTotalqty() {
		return totalqty;
	}
	public void setTotalqty(int totalqty) {
		this.totalqty = totalqty;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getShipno() {
		return shipno;
	}
	public void setShipno(String shipno) {
		this.shipno = shipno;
	}
	public String getShipper() {
		return shipper;
	}
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}
	public String getShipstatus() {
		return shipstatus;
	}
	public void setShipstatus(String shipstatus) {
		this.shipstatus = shipstatus;
	}
	public String getItemurl() {
		return itemurl;
	}
	public void setItemurl(String itemurl) {
		this.itemurl = itemurl;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	public int getTaobaoStateE() {
		return TaobaoStateE;
	}
	public void setTaobaoStateE(int taobaoStateE) {
		TaobaoStateE = taobaoStateE;
	}
	public String getMerchantorderid() {
		return merchantorderid;
	}
	public void setMerchantorderid(String merchantorderid) {
		this.merchantorderid = merchantorderid;
	}
	public String getPaytreasureid() {
		return paytreasureid;
	}
	public void setPaytreasureid(String paytreasureid) {
		this.paytreasureid = paytreasureid;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"tbOr1688\":\"")
				.append(tbOr1688).append('\"');
		sb.append(",\"orderid\":\"")
				.append(orderid).append('\"');
		sb.append(",\"itemid\":\"")
				.append(itemid).append('\"');
		sb.append(",\"itemurl\":\"")
				.append(itemurl).append('\"');
		sb.append('}');
		return sb.toString();
	}
}
