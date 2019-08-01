package com.cbt.website.bean;

import java.util.List;

public class PurchasesBean {

	private int userid;
	private String name;
	private int orderid;
	private String orderNo;
	private String orderaddress;// 订单地址
	private String deliveryTime;// 交期天数
	private String ordertime;// 下单时间
	private String paytime;// 付款时间
	private int purchase_number;// 已采购数量
	private int details_number;// 需采购数量
	private String mode_transport;// freeShiping
	private String fileimgname;
	private int od_state;
	private String goodssourcetype = "";// 淘宝规格
	private String cginfo;
	private String purchasetime;
	private String tip_info;
	private String inventory;// 库存
	private String cGoodstype;// 产品类型
	private int source_of_goods;
	private String admin;// 录入无货源的人员
	private String isDropshipOrder;// 是否是isDropshipOrder 订单 2/15
	private String child_order_no;// dropship的订单号
									// 单独在dropshiporder表中只能按照child_order_no来进行区分。
	private int offline_purchase;
	private int is_replenishment;

	private String companyName;// 货源的公司名字 seilUnit goodsUnit
	private String seilUnit;// 产品的单位比
	private String goodsUnit;// 产品的单位
	private int confirm_userid;// 采购人
	private String remaining;// 可库存数量
	private int allRemaining;// 总库存
	private String barcode;// 库存位置
	private int flag;
	private String new_remaining;
	private String lock_remaining;
	private String in_id;
	private String orderProblem;// 订单问题描述
	
	private String tb_1688_itemid;
	private String last_tb_1688_itemid;
	private String is_delivery;//该商品是否检测到卖家发货
	private String unquestionsRemarks;
	private String goods_info;//采购与销售沟通备注
	private String oistate;
	private String inventory_remark;//库存使用备注
	private String tb_orderid;//商品对于的采购订单号
	private int purchase_state;//采购状态
	private int invoice;
	private double freight;//购物车邮费
	private String importExUrl;//电商网站链接
	private String odstate;
	 private String shipstatus;//物流信息
	 private String confirm_time;
	 private String buyid;
	 private String support_info;//卖家承诺图片
	 private String goods_pid;
	 private String car_urlMD5;
	 private String ali_pid;
	 private String shop_id;
	 private String shop_ids;//录入的工厂链接
	 private List<String> bh_shop_id;
	 private int straight_flag;//广东直发标识
	 private String straight_address;//供应商发货地址
	 private String straight_time;//确认直发时间
	 
	 private String level;//店铺级别
	 private int refund_flag;//退货标识
	 private String source_shop_id;//本链接采样店铺ID
	private String tborderInfo;//销售订单入库关联的1688订单信息
	private String cbrWeight;//原始1688重量
	private String carWeight;//加入购物车重量
	//是否显示店铺商品打分按钮
	private String shopFlag;
	private String goodsShop;
	private String old_shopid;
	private String pidInventory;
	private String authorizedFlag;
	private String shopAddress;
	private int authorized_flag;
	private String tbSku;
	private String quality;
	private String noChnageRemark;
	private String shopInventory;
	private String returnTime;//退货时间
	private String shopName; //供应商名字
	private String shopGrade; //供应商评级
	private String morder; //最小moq
	private String replacementProduct; //客户录入替换产品
	private String shipno;
	private double goodsShopPrice;// 商品店铺总销售额
	private String tborderid;// 1688订单号
	private String shipnoid;// 运单号
	private String specid;
	private String skuid;
	private String inventorySkuId;//库存表id
	
	public String getInventorySkuId() {
		return inventorySkuId;
	}

	public void setInventorySkuId(String inventorySkuId) {
		this.inventorySkuId = inventorySkuId;
	}

	public String getSpecid() {
		return specid;
	}

	public void setSpecid(String specid) {
		this.specid = specid;
	}

	public String getSkuid() {
		return skuid;
	}

	public void setSkuid(String skuid) {
		this.skuid = skuid;
	}

	public String getShipnoid() {
		return shipnoid;
	}

	public void setShipnoid(String shipnoid) {
		this.shipnoid = shipnoid;
	}

	public String getTborderid() {
		return tborderid;
	}

	public void setTborderid(String tborderid) {
		this.tborderid = tborderid;
	}

	public double getGoodsShopPrice() {
		return goodsShopPrice;
	}

	public void setGoodsShopPrice(double goodsShopPrice) {
		this.goodsShopPrice = goodsShopPrice;
	}

	public String getShipno() {
		return shipno;
	}

	public void setShipno(String shipno) {
		this.shipno = shipno;
	}

	public String getReplacementProduct() {
		return replacementProduct;
	}

	public void setReplacementProduct(String replacementProduct) {
		this.replacementProduct = replacementProduct;
	}

	public String getMorder() {
		return morder;
	}

	public void setMorder(String morder) {
		this.morder = morder;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopGrade() {
		return shopGrade;
	}

	public void setShopGrade(String shopGrade) {
		this.shopGrade = shopGrade;
	}

	public String getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}

	public String getShopInventory() {
		return shopInventory;
	}

	public void setShopInventory(String shopInventory) {
		this.shopInventory = shopInventory;
	}

	public String getNoChnageRemark() {
		return noChnageRemark;
	}

	public void setNoChnageRemark(String noChnageRemark) {
		this.noChnageRemark = noChnageRemark;
	}

	public String getInventoryRemark() {
		return inventoryRemark;
	}

	public void setInventoryRemark(String inventoryRemark) {
		this.inventoryRemark = inventoryRemark;
	}

	private String inventoryRemark;
	public String getTbSku() {
		return tbSku;
	}

	public void setTbSku(String tbSku) {
		this.tbSku = tbSku;
	}


	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	private String type_name;
	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public int getAuthorized_flag() {
		return authorized_flag;
	}

	public void setAuthorized_flag(int authorized_flag) {
		this.authorized_flag = authorized_flag;
	}


	public String getAuthorizedFlag() {
		return authorizedFlag;
	}

	public void setAuthorizedFlag(String authorizedFlag) {
		this.authorizedFlag = authorizedFlag;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getPidInventory() {
		return pidInventory;
	}

	public void setPidInventory(String pidInventory) {
		this.pidInventory = pidInventory;
	}

	public String getOld_shopid() {
		return old_shopid;
	}

	public void setOld_shopid(String old_shopid) {
		this.old_shopid = old_shopid;
	}

	public String getGoodsShop() {
		return goodsShop;
	}

	public void setGoodsShop(String goodsShop) {
		this.goodsShop = goodsShop;
	}

	public String getShopFlag() {
		return shopFlag;
	}

	public void setShopFlag(String shopFlag) {
		this.shopFlag = shopFlag;
	}

	public String getExchange_rate() {
		return exchange_rate;
	}

	public void setExchange_rate(String exchange_rate) {
		this.exchange_rate = exchange_rate;
	}

	private String exchange_rate;
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	private String total;
	public String getCbrWeight() {
		return cbrWeight;
	}

	public void setCbrWeight(String cbrWeight) {
		this.cbrWeight = cbrWeight;
	}

	public String getCarWeight() {
		return carWeight;
	}

	public void setCarWeight(String carWeight) {
		this.carWeight = carWeight;
	}

	public String getTborderInfo() {
		return tborderInfo;
	}

	public void setTborderInfo(String tborderInfo) {
		this.tborderInfo = tborderInfo;
	}

	public String getSource_shop_id() {
		return source_shop_id;
	}

	public void setSource_shop_id(String source_shop_id) {
		this.source_shop_id = source_shop_id;
	}

	public int getRefund_flag() {
		return refund_flag;
	}

	public void setRefund_flag(int refund_flag) {
		this.refund_flag = refund_flag;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getStraight_time() {
		return straight_time;
	}

	public void setStraight_time(String straight_time) {
		this.straight_time = straight_time;
	}

	public String getStraight_address() {
		return straight_address;
	}

	public void setStraight_address(String straight_address) {
		this.straight_address = straight_address;
	}

	public int getStraight_flag() {
		return straight_flag;
	}

	public void setStraight_flag(int straight_flag) {
		this.straight_flag = straight_flag;
	}

	public List<String> getBh_shop_id() {
		return bh_shop_id;
	}

	public void setBh_shop_id(List<String> bh_shop_id) {
		this.bh_shop_id = bh_shop_id;
	}

	public String getShop_ids() {
		return shop_ids;
	}

	public void setShop_ids(String shop_ids) {
		this.shop_ids = shop_ids;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getAli_pid() {
		return ali_pid;
	}

	public void setAli_pid(String ali_pid) {
		this.ali_pid = ali_pid;
	}

	public String getGoods_pid() {
		return goods_pid;
	}

	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}

	public String getCar_urlMD5() {
		return car_urlMD5;
	}

	public void setCar_urlMD5(String car_urlMD5) {
		this.car_urlMD5 = car_urlMD5;
	}

	public String getSupport_info() {
		return support_info;
	}

	public void setSupport_info(String support_info) {
		this.support_info = support_info;
	}

	public String getBuyid() {
		return buyid;
	}

	public void setBuyid(String buyid) {
		this.buyid = buyid;
	}

	public String getConfirm_time() {
		return confirm_time;
	}

	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}

	public String getShipstatus() {
		return shipstatus;
	}

	public void setShipstatus(String shipstatus) {
		this.shipstatus = shipstatus;
	}

	public String getOdstate() {
		return odstate;
	}

	public void setOdstate(String odstate) {
		this.odstate = odstate;
	}

	public double getFreight() {
		return freight;
	}

	public void setFreight(double freight) {
		this.freight = freight;
	}

	public int getInvoice() {
		return invoice;
	}

	public void setInvoice(int invoice) {
		this.invoice = invoice;
	}

	public int getPurchase_state() {
		return purchase_state;
	}

	public void setPurchase_state(int purchase_state) {
		this.purchase_state = purchase_state;
	}

	public String getTb_orderid() {
		return tb_orderid;
	}

	public void setTb_orderid(String tb_orderid) {
		this.tb_orderid = tb_orderid;
	}

	public String getInventory_remark() {
		return inventory_remark;
	}

	public void setInventory_remark(String inventory_remark) {
		this.inventory_remark = inventory_remark;
	}

	public String getOistate() {
		return oistate;
	}

	public void setOistate(String oistate) {
		this.oistate = oistate;
	}

	public String getGoods_info() {
		return goods_info;
	}

	public void setGoods_info(String goods_info) {
		this.goods_info = goods_info;
	}

	public String getUnquestionsRemarks() {
		return unquestionsRemarks;
	}

	public void setUnquestionsRemarks(String unquestionsRemarks) {
		this.unquestionsRemarks = unquestionsRemarks;
	}

	public String getIs_delivery() {
		return is_delivery;
	}

	public void setIs_delivery(String is_delivery) {
		this.is_delivery = is_delivery;
	}

	public String getTb_1688_itemid() {
		return tb_1688_itemid;
	}

	public void setTb_1688_itemid(String tb_1688_itemid) {
		this.tb_1688_itemid = tb_1688_itemid;
	}

	public String getLast_tb_1688_itemid() {
		return last_tb_1688_itemid;
	}

	public void setLast_tb_1688_itemid(String last_tb_1688_itemid) {
		this.last_tb_1688_itemid = last_tb_1688_itemid;
	}


	public int getIs_replenishment() {
		return is_replenishment;
	}

	public void setIs_replenishment(int is_replenishment) {
		this.is_replenishment = is_replenishment;
	}

	public String getNew_remaining() {
		return new_remaining;
	}

	public void setNew_remaining(String new_remaining) {
		this.new_remaining = new_remaining;
	}

	public String getLock_remaining() {
		return lock_remaining;
	}

	public void setLock_remaining(String lock_remaining) {
		this.lock_remaining = lock_remaining;
	}

	public String getIn_id() {
		return in_id;
	}

	public void setIn_id(String in_id) {
		this.in_id = in_id;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getAllRemaining() {
		return allRemaining;
	}

	public void setAllRemaining(int allRemaining) {
		this.allRemaining = allRemaining;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getRemaining() {
		return remaining;
	}

	public void setRemaining(String remaining) {
		this.remaining = remaining;
	}

	public int getConfirm_userid() {
		return confirm_userid;
	}

	public void setConfirm_userid(int confirm_userid) {
		this.confirm_userid = confirm_userid;
	}

	public String getSeilUnit() {
		return seilUnit;
	}

	public void setSeilUnit(String seilUnit) {
		this.seilUnit = seilUnit;
	}

	public String getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getOffline_purchase() {
		return offline_purchase;
	}

	public void setOffline_purchase(int offline_purchase) {
		this.offline_purchase = offline_purchase;
	}

	public String getChild_order_no() {
		return child_order_no;
	}

	public void setChild_order_no(String child_order_no) {
		this.child_order_no = child_order_no;
	}

	public String getIsDropshipOrder() {
		return isDropshipOrder;
	}

	public void setIsDropshipOrder(String isDropshipOrder) {
		this.isDropshipOrder = isDropshipOrder;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public int getSource_of_goods() {
		return source_of_goods;
	}

	public void setSource_of_goods(int source_of_goods) {
		this.source_of_goods = source_of_goods;
	}

	public String getcGoodstype() {
		return cGoodstype;
	}

	public void setcGoodstype(String cGoodstype) {
		this.cGoodstype = cGoodstype;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getTip_info() {
		return tip_info;
	}

	public void setTip_info(String tip_info) {
		this.tip_info = tip_info;
	}

	public String getPurchasetime() {
		return purchasetime;
	}

	public void setPurchasetime(String purchasetime) {
		this.purchasetime = purchasetime;
	}

	public String getCginfo() {
		return cginfo;
	}

	public void setCginfo(String cginfo) {
		this.cginfo = cginfo;
	}

	public String getGoodssourcetype() {
		return goodssourcetype;
	}

	public void setGoodssourcetype(String goodssourcetype) {
		this.goodssourcetype = goodssourcetype;
	}

	public int getOd_state() {
		return od_state;
	}

	public void setOd_state(int od_state) {
		this.od_state = od_state;
	}

	public String getFileimgname() {
		return fileimgname;
	}

	public void setFileimgname(String fileimgname) {
		this.fileimgname = fileimgname;
	}

	private String profit; // 利润
	private String repertory;// 库存

	// private List<PurchaseDetailsBean> purchaseDetailsBean;//采购详情

	public String getRepertory() {
		return repertory;
	}

	public void setRepertory(String repertory) {
		this.repertory = repertory;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	private int goodsid;
	private int od_id;
	private int goodsdata_id;
	private String googs_img;
	private String goods_url;
	private String goods_type;
	private String goods_title;
	private String goods_price;
	private String currency;
	private int googs_number;
	private String remark;
	private String oldValue;
	private String newValue;
	private String lastValue;

	public String getLastValue() {
		return lastValue;
	}

	public void setLastValue(String lastValue) {
		this.lastValue = lastValue;
	}

	private String remarkpurchase;
	private String issure;
	private int purchaseCount;
	private String addtime;
	private String purchaseSure;
	private String purchaseEnd;
	private String yiruku;
	private String puechaseTime;
	private String rukuTime;// 入库时间
	private String img_type;
	private String saler;// 销售负责人
	private String productState;// order_change表，商品是否取消
	private String orderremark_btn;// 订单备注控制btn
	private String orderremark;// 订单备注
	private String originalGoodsUrl;// 替换商品原链接
	private String orderremarkNew;// 订单备注
	private String querneGoods;// 确认货源button

	private String position; // 仓库位置
	private String rkgoodstatus; // 入库状态

	private String remarkAgainBtn;// 再次备注按钮

	public String getOrderremarkNew() {
		return orderremarkNew;
	}

	public void setOrderremarkNew(String orderremarkNew) {
		this.orderremarkNew = orderremarkNew;
	}

	public String getRkgoodstatus() {
		return rkgoodstatus;
	}

	public void setRkgoodstatus(String rkgoodstatus) {
		this.rkgoodstatus = rkgoodstatus;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getQuerneGoods() {
		return querneGoods;
	}

	public void setQuerneGoods(String querneGoods) {
		this.querneGoods = querneGoods;
	}

	public String getYiruku() {
		return yiruku;
	}

	public void setYiruku(String yiruku) {
		this.yiruku = yiruku;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderaddress() {
		return orderaddress;
	}

	public void setOrderaddress(String orderaddress) {
		this.orderaddress = orderaddress;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public int getPurchase_number() {
		return purchase_number;
	}

	public void setPurchase_number(int purchase_number) {
		this.purchase_number = purchase_number;
	}

	public int getDetails_number() {
		return details_number;
	}

	public void setDetails_number(int details_number) {
		this.details_number = details_number;
	}

	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public int getOd_id() {
		return od_id;
	}

	public void setOd_id(int od_id) {
		this.od_id = od_id;
	}

	public int getGoodsdata_id() {
		return goodsdata_id;
	}

	public void setGoodsdata_id(int goodsdata_id) {
		this.goodsdata_id = goodsdata_id;
	}

	public String getGoogs_img() {
		return googs_img;
	}

	public void setGoogs_img(String googs_img) {
		this.googs_img = googs_img;
	}

	public String getGoods_url() {
		return goods_url;
	}

	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}

	public String getGoods_title() {
		return goods_title;
	}

	public void setGoods_title(String goods_title) {
		this.goods_title = goods_title;
	}

	public String getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getGoogs_number() {
		return googs_number;
	}

	public void setGoogs_number(int googs_number) {
		this.googs_number = googs_number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getRemarkpurchase() {
		return remarkpurchase;
	}

	public void setRemarkpurchase(String remarkpurchase) {
		this.remarkpurchase = remarkpurchase;
	}

	public String getIssure() {
		return issure;
	}

	public void setIssure(String issure) {
		this.issure = issure;
	}

	public int getPurchaseCount() {
		return purchaseCount;
	}

	public void setPurchaseCount(int purchaseCount) {
		this.purchaseCount = purchaseCount;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getPurchaseSure() {
		return purchaseSure;
	}

	public void setPurchaseSure(String purchaseSure) {
		this.purchaseSure = purchaseSure;
	}

	public String getPuechaseTime() {
		return puechaseTime;
	}

	public void setPuechaseTime(String puechaseTime) {
		this.puechaseTime = puechaseTime;
	}

	public String getRukuTime() {
		return rukuTime;
	}

	public void setRukuTime(String rukuTime) {
		this.rukuTime = rukuTime;
	}

	public String getImg_type() {
		return img_type;
	}

	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}

	public String getPurchaseEnd() {
		return purchaseEnd;
	}

	public void setPurchaseEnd(String purchaseEnd) {
		this.purchaseEnd = purchaseEnd;
	}

	public String getMode_transport() {
		return mode_transport;
	}

	public void setMode_transport(String mode_transport) {
		this.mode_transport = mode_transport;
	}

	public String getSaler() {
		return saler;
	}

	public void setSaler(String saler) {
		this.saler = saler;
	}

	public String getProductState() {
		return productState;
	}

	public void setProductState(String productState) {
		this.productState = productState;
	}

	public String getOrderremark_btn() {
		return orderremark_btn;
	}

	public void setOrderremark_btn(String orderremark_btn) {
		this.orderremark_btn = orderremark_btn;
	}

	public String getOrderremark() {
		return orderremark;
	}

	public void setOrderremark(String orderremark) {
		this.orderremark = orderremark;
	}

	public String getOriginalGoodsUrl() {
		return originalGoodsUrl;
	}

	public void setOriginalGoodsUrl(String originalGoodsUrl) {
		this.originalGoodsUrl = originalGoodsUrl;
	}

	public String getRemarkAgainBtn() {
		return remarkAgainBtn;
	}

	public void setRemarkAgainBtn(String remarkAgainBtn) {
		this.remarkAgainBtn = remarkAgainBtn;
	}

	public String getOrderProblem() {
		return orderProblem;
	}

	public void setOrderProblem(String orderProblem) {
		this.orderProblem = orderProblem;
	}
	
	

	public String getImportExUrl() {
		return importExUrl;
	}

	public void setImportExUrl(String importExUrl) {
		this.importExUrl = importExUrl;
	}

	@Override
	public String toString() {
		return "PurchasesBean [userid=" + userid + ", name=" + name + ", orderid=" + orderid + ", orderNo=" + orderNo
				+ ", orderaddress=" + orderaddress + ", deliveryTime=" + deliveryTime + ", ordertime=" + ordertime
				+ ", paytime=" + paytime + ", purchase_number=" + purchase_number + ", details_number=" + details_number
				+ ", mode_transport=" + mode_transport + ", fileimgname=" + fileimgname + ", od_state=" + od_state
				+ ", goodssourcetype=" + goodssourcetype + ", cginfo=" + cginfo + ", purchasetime=" + purchasetime
				+ ", tip_info=" + tip_info + ", inventory=" + inventory + ", cGoodstype=" + cGoodstype
				+ ", source_of_goods=" + source_of_goods + ", admin=" + admin + ", isDropshipOrder=" + isDropshipOrder
				+ ", child_order_no=" + child_order_no + ", offline_purchase=" + offline_purchase
				+ ", is_replenishment=" + is_replenishment + ", companyName=" + companyName + ", seilUnit=" + seilUnit
				+ ", goodsUnit=" + goodsUnit + ", confirm_userid=" + confirm_userid + ", remaining=" + remaining
				+ ", allRemaining=" + allRemaining + ", barcode=" + barcode + ", flag=" + flag + ", new_remaining="
				+ new_remaining + ", lock_remaining=" + lock_remaining + ", in_id=" + in_id + ", orderProblem="
				+ orderProblem + ", profit=" + profit + ", repertory=" + repertory + ", goodsid=" + goodsid + ", od_id="
				+ od_id + ", goodsdata_id=" + goodsdata_id + ", googs_img=" + googs_img + ", goods_url=" + goods_url
				+ ", goods_type=" + goods_type + ", goods_title=" + goods_title + ", goods_price=" + goods_price
				+ ", currency=" + currency + ", googs_number=" + googs_number + ", remark=" + remark + ", oldValue="
				+ oldValue + ", newValue=" + newValue + ", lastValue=" + lastValue + ", remarkpurchase="
				+ remarkpurchase + ", issure=" + issure + ", purchaseCount=" + purchaseCount + ", addtime=" + addtime
				+ ", purchaseSure=" + purchaseSure + ", purchaseEnd=" + purchaseEnd + ", yiruku=" + yiruku
				+ ", puechaseTime=" + puechaseTime + ", rukuTime=" + rukuTime + ", img_type=" + img_type + ", saler="
				+ saler + ", productState=" + productState + ", orderremark_btn=" + orderremark_btn + ", orderremark="
				+ orderremark + ", originalGoodsUrl=" + originalGoodsUrl + ", orderremarkNew=" + orderremarkNew
				+ ", querneGoods=" + querneGoods + ", position=" + position + ", rkgoodstatus=" + rkgoodstatus
				+ ", remarkAgainBtn=" + remarkAgainBtn + "]";
	}

}
