package com.cbt.bean;

import java.io.Serializable;
import java.util.List;


public class OrderDetailsBean implements Cloneable,Serializable {

	/**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = 1L;
	//zp新增  将读取到账确认信息  合并 到订单详情
	private int dzId;
	private String dzOrderno;
	private String dzConfirmname;
	private String dzConfirmtime;
	private float sumGoods_p_price; //订单金额
	private float sumGoods_price; 
	private int orsstate;//order_product_source  purchase_state  
	private int od_state;//替换标识
	public int getOd_state() {
		return od_state;
	}
	public void setOd_state(int od_state) {
		this.od_state = od_state;
	}
	private int id;
	private int userid;
	private int goodsid;
	private String goodsname;
	private String goods_url;
	private String goods_img;
	private String goods_type;
	private String orderid;
	private String delivery_time;
	private int checkprice_fee;//询价的费用
	private int checkproduct_fee;//检查产品的费用
	private int state;//状态（0-购买中，1-产品买了并已经到我们仓库）
	private String fileupload;//拍照图片
	private double actual_price;//实际采购价格
	private double actual_freight;//实际国内运费
	private String actual_weight;//实际重量(KG)
	private String actual_volume;//实际体积（长*宽*高）（平方米）
	private String goods_freight;//商品国内运费
	private String paytime;//支付时间
	private int yourorder;
	private String goodsprice;
	private SpiderBean spider;
	private String createtime;//
	private String freight;//国内运费
	
	private String od_bulk_volume; //购物车总体积
	private double od_total_weight; //购物车总重量
	private String country; //国家id
	//交期 和 价格变动 变动后的值
	private String change_delivery;
	private String change_price;
	private String change_freight;
	private String change_number;
	private List<String> change_communication;
	
	private int ropType; // order_change沟通状态
	private int del_state; //order_change删除状态
	//二次货源地址
	private String oldsourceurl;
	//新资源地址
	private String newsourceurl;
	//改商品被取消 0没有取消 1已取消
	private int iscancel;
	//商品总费用
	private String product_cost;
	private int freight_free;//是否免邮，0-否，1-是
	private String remaining_price;//订单所欠费用
	private int goodsdata_id;
	private int preferential;//是否是优惠申请 0-否
	private int purchase_state;//货源确认状态 0-未确认，1-已确认，2-用户未确认不能改过状态
	private String purchase_time;//货源确认时间
	private String purchase_confirmation;//货源确认人员
	private String remark;
	private String currency;//货币单位
	private int goods_class;//商品类型
	private String img_type;//商品类型选择图片
	
	private String oldUrl;//数据库表中没有相应的
    private double extra_freight;//额外运费金额
    private String gtotal_weight;//购物车总重量
    private String gbulk_volume;//购物车总体积
    private double preferential_price;//批量优惠金额--
    
    private int buycount;//替代购买数量
    private String exporttime;//出货时间
    private String sourc_price;
    private double discount_ratio;//混批优惠折扣比例
    
    
    private String dropshipid;//新增dropshipid 
	private int checked;//入库校验 0未检验 1已校验
    private String oremark;//采购备注
    
    //拆分采购备注
    private  String  bargainRemark ;
    private  String deliveryRemark;
    private  String  colorReplaceRemark;
    private  String sizeReplaceRemark;
    private  String orderNumRemarks;
    private  String questionsRemarks;
    private  String unquestionsRemarks;
    private  String  againRemarks ;

    private int goodstatus;//入库状态
    private double ffreight;//咱们网站算出来运费
    private String goodscatid;//商品最小类别
    private String seilUnit;//商品的单位转换比
    private String goodsUnit;//商品的单位
    private String shipstatus;//物流信息
    private String tb_1688_itemid;
    private String last_tb_1688_itemid;
    private String confirm_time;
    private String confirm_userid;
    private String shipno;
    private String goods_info;//销售沟通备注
    private String car_urlMD5;
    private String goods_pid;
    private String goods_p_price;
    private int offline_purchase;
    private String shop_id;
    private List<String> bh_shop_id;
	private int isDropshipOrder;
	private String match_url;//上架的1688产品链接
	private String ali_price;//ali价格
	private int groupBuyId;//团购活动ID
	private String final_weight;//产品重量
	private String dfinal_weight;
	private double es_price;//1688预计采购价格

	private String cbrShopid;
	private String cbrdShop_id;

	//采购货源标题
	private String goodsPName;
	//商品有库存但规格不一致
	private String pidInventory;
	private int isBenchmark;
	private String noChnageRemark;
	public String getNoChnageRemark() {
		return noChnageRemark;
	}

	public void setNoChnageRemark(String noChnageRemark) {
		this.noChnageRemark = noChnageRemark;
	}

	public int getIsBenchmark() {
		return isBenchmark;
	}

	public void setIsBenchmark(int isBenchmark) {
		this.isBenchmark = isBenchmark;
	}

	public String getPidInventory() {
		return pidInventory;
	}

	public void setPidInventory(String pidInventory) {
		this.pidInventory = pidInventory;
	}

	public String getGoodsPName() {
		return goodsPName;
	}

	public void setGoodsPName(String goodsPName) {
		this.goodsPName = goodsPName;
	}
	public String getInventoryRemark() {
		return inventoryRemark;
	}

	public void setInventoryRemark(String inventoryRemark) {
		this.inventoryRemark = inventoryRemark;
	}

	private String inventoryRemark;

	public String getAli_sellunit() {
		return ali_sellunit;
	}

	public void setAli_sellunit(String ali_sellunit) {
		this.ali_sellunit = ali_sellunit;
	}

	private String ali_sellunit;
	public int getIs_sold_flag() {
		return is_sold_flag;
	}

	public void setIs_sold_flag(int is_sold_flag) {
		this.is_sold_flag = is_sold_flag;
	}

	private int is_sold_flag;
	public String getShopFlag() {
		return shopFlag;
	}

	public void setShopFlag(String shopFlag) {
		this.shopFlag = shopFlag;
	}

	private String shopFlag;

	public int getBm_flag() {
		return bm_flag;
	}

	public void setBm_flag(int bm_flag) {
		this.bm_flag = bm_flag;
	}

	private int bm_flag;
	public String getCbrShopid() {
		return cbrShopid;
	}

	public void setCbrShopid(String cbrShopid) {
		this.cbrShopid = cbrShopid;
	}

	public String getCbrdShop_id() {
		return cbrdShop_id;
	}

	public void setCbrdShop_id(String cbrdShop_id) {
		this.cbrdShop_id = cbrdShop_id;
	}
	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	private String countryId;
	public String getDfinal_weight() {
		return dfinal_weight;
	}

	public void setDfinal_weight(String dfinal_weight) {
		this.dfinal_weight = dfinal_weight;
	}



	public String getCbrdPrice() {
		return cbrdPrice;
	}

	public void setCbrdPrice(String cbrdPrice) {
		this.cbrdPrice = cbrdPrice;
	}

	private String cbrdPrice;
	public String getGoods_typeimg() {
		return goods_typeimg;
	}

	public void setGoods_typeimg(String goods_typeimg) {
		this.goods_typeimg = goods_typeimg;
	}

	private String goods_typeimg;
	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	private String oldValue;
	public String getGoodsfreight() {
		return goodsfreight;
	}

	public void setGoodsfreight(String goodsfreight) {
		this.goodsfreight = goodsfreight;
	}

	private String goodsfreight;
	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	private int oid;
	public String getCbrPrice() {
		return cbrPrice;
	}

	public void setCbrPrice(String cbrPrice) {
		this.cbrPrice = cbrPrice;
	}

	private String cbrPrice;
	public String getCbrWeight() {
		return cbrWeight;
	}

	public void setCbrWeight(String cbrWeight) {
		this.cbrWeight = cbrWeight;
	}

	private String cbrWeight;
	public double getEs_price() {
		return es_price;
	}

	public void setEs_price(double es_price) {
		this.es_price = es_price;
	}

	public double getPid_amount() {
		return pid_amount;
	}

	public void setPid_amount(double pid_amount) {
		this.pid_amount = pid_amount;
	}

	private double pid_amount;
	public String getFinal_weight() {
		return final_weight;
	}

	public void setFinal_weight(String final_weight) {
		this.final_weight = final_weight;
	}


	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	private String video_url;
	public String getAli_price() {
		return ali_price;
	}
	public void setAli_price(String ali_price) {
		this.ali_price = ali_price;
	}
	/**
	 * 1688重量
	 */
	private String weight1688;
	/**
	 * 1688价格
	 */
	private String price1688;
	/**
	 * ali 连接
	 * @return
	 */
	private String alipid;

	public String getNew_zid() {
		return new_zid;
	}

	public void setNew_zid(String new_zid) {
		this.new_zid = new_zid;
	}

	private String new_zid;

	public int getIs_replenishment() {
		return is_replenishment;
	}

	public void setIs_replenishment(int is_replenishment) {
		this.is_replenishment = is_replenishment;
	}

	private int is_replenishment;
	public int getGroupBuyId() {
		return groupBuyId;
	}

	public void setGroupBuyId(int groupBuyId) {
		this.groupBuyId = groupBuyId;
	}

	public String getAlipid() {
		return alipid;
	}
	public void setAlipid(String alipid) {
		this.alipid = alipid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getWeight1688() {
        return weight1688;
    }
    public void setWeight1688(String weight1688) {
        this.weight1688 = weight1688;
    }
    public String getPrice1688() {
        return price1688;
    }
    public void setPrice1688(String price1688) {
        this.price1688 = price1688;
    }
    public String getMatch_url() {
		return match_url;
	}
	public void setMatch_url(String match_url) {
		this.match_url = match_url;
	}
	public List<String> getBh_shop_id() {
		return bh_shop_id;
	}
	public void setBh_shop_id(List<String> bh_shop_id) {
		this.bh_shop_id = bh_shop_id;
	}
    public int getIsDropshipOrder() {
		return isDropshipOrder;
	}
	public void setIsDropshipOrder(int isDropshipOrder) {
		this.isDropshipOrder = isDropshipOrder;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public int getOffline_purchase() {
		return offline_purchase;
	}
	public void setOffline_purchase(int offline_purchase) {
		this.offline_purchase = offline_purchase;
	}
	public String getGoods_p_price() {
		return goods_p_price;
	}
	public void setGoods_p_price(String goods_p_price) {
		this.goods_p_price = goods_p_price;
	}
	public String getCar_urlMD5() {
		return car_urlMD5;
	}
	public void setCar_urlMD5(String car_urlMD5) {
		this.car_urlMD5 = car_urlMD5;
	}
	public String getGoods_pid() {
		return goods_pid;
	}
	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}
    public String getGoods_info() {
		return goods_info;
	}
	public void setGoods_info(String goods_info) {
		this.goods_info = goods_info;
	}
	public String getShipno() {
		return shipno;
	}
	public void setShipno(String shipno) {
		this.shipno = shipno;
	}
	public String getConfirm_userid() {
		return confirm_userid;
	}
	public void setConfirm_userid(String confirm_userid) {
		this.confirm_userid = confirm_userid;
	}
	public String getConfirm_time() {
		return confirm_time;
	}
	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
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
    
    public String getShipstatus() {
		return shipstatus;
	}
	public void setShipstatus(String shipstatus) {
		this.shipstatus = shipstatus;
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
    public String getOldsourceurl() {
		return oldsourceurl;
	}
	public void setOldsourceurl(String oldsourceurl) {
		this.oldsourceurl = oldsourceurl;
	}
    /**Drop Ship订单新添加的字段*/
    private String car_url;
    private String car_img;
    private String car_type;
    private String sprice;
    private int buy_for_me;
    private int flag;
    private int isAuto;
    private int isFeight;
    private String  dropShipState;//dropship子订单状态

	public String getLastValue() {
		return lastValue;
	}

	public void setLastValue(String lastValue) {
		this.lastValue = lastValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	private String lastValue;
	private String newValue;
    public String getGoodscatid() {
		return goodscatid;
	}
	public void setGoodscatid(String goodscatid) {
		this.goodscatid = goodscatid;
	}
	public double getFfreight() {
		return ffreight;
	}
	public void setFfreight(double d) {
		this.ffreight = d;
	}
	public int getGoodstatus() {
		return goodstatus;
	}
	public void setGoodstatus(int goodstatus) {
		this.goodstatus = goodstatus;
	}
	public String getOremark() {
		return oremark;
	}
	public void setOremark(String oremark) {
		this.oremark = oremark;
	}
    
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	public String getDropshipid() {
		return dropshipid;
	}
	public void setDropshipid(String dropshipid) {
		this.dropshipid = dropshipid;
	}
	public String getSourc_price() {
		return sourc_price;
	}
	public void setSourc_price(String sourc_price) {
		this.sourc_price = sourc_price;
	}
	public int getOrsstate() {
		return orsstate;
	}
	public void setOrsstate(int orsstate) {
		this.orsstate = orsstate;
	}

	public double getOd_bulk_volume() {
		return od_bulk_volume;
	}
	public void setOd_bulk_volume(double od_bulk_volume) {
		this.od_bulk_volume = od_bulk_volume;
	}
	public double getOd_total_weight() {
		return od_total_weight;
	}
	public void setOd_total_weight(double od_total_weight) {
		this.od_total_weight = od_total_weight;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public float getSumGoods_price() {
		return sumGoods_price;
	}
	public void setSumGoods_price(float sumGoods_price) {
		this.sumGoods_price = sumGoods_price;
	}
	public float getSumGoods_p_price() {
		return sumGoods_p_price;
	}
	public void setSumGoods_p_price(float sumGoods_p_price) {
		this.sumGoods_p_price = sumGoods_p_price;
	}
	public int getDzId() {
		return dzId;
	}
	public void setDzId(int dzId) {
		this.dzId = dzId;
	}
	public String getDzOrderno() {
		return dzOrderno;
	}
	public void setDzOrderno(String dzOrderno) {
		this.dzOrderno = dzOrderno;
	}
	public String getDzConfirmname() {
		return dzConfirmname;
	}
	public void setDzConfirmname(String dzConfirmname) {
		this.dzConfirmname = dzConfirmname;
	}
	public String getDzConfirmtime() {
		return dzConfirmtime;
	}
	public void setDzConfirmtime(String dzConfirmtime) {
		this.dzConfirmtime = dzConfirmtime;
	}
	public void setGoodsdata_id(int goodsdata_id) {
		this.goodsdata_id = goodsdata_id;
	}
	public int getGoodsdata_id() {
		return goodsdata_id;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public String getPaytime() {
		return paytime;
	}
	
	public SpiderBean getSpider() {
		return spider;
	}
	public void setSpider(SpiderBean spider) {
		this.spider = spider;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}
	public int getCheckprice_fee() {
		return checkprice_fee;
	}
	public void setCheckprice_fee(int checkprice_fee) {
		this.checkprice_fee = checkprice_fee;
	}
	public int getCheckproduct_fee() {
		return checkproduct_fee;
	}
	public void setCheckproduct_fee(int checkproduct_fee) {
		this.checkproduct_fee = checkproduct_fee;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getFileupload() {
		return fileupload;
	}
	public void setFileupload(String fileupload) {
		this.fileupload = fileupload;
	}
	
	public int getYourorder() {
		return yourorder;
	}
	public void setYourorder(int yourorder) {
		this.yourorder = yourorder;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getGoodsprice() {
		return goodsprice;
	}
	public void setGoodsprice(String goodsprice) {
		this.goodsprice = goodsprice;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getCreatetime() {
		return createtime;
	}
	
 
	public double getActual_price() {
		return actual_price;
	}
	public void setActual_price(double actual_price) {
		this.actual_price = actual_price;
	}
	public double getActual_freight() {
		return actual_freight;
	}
	public void setActual_freight(double actual_freight) {
		this.actual_freight = actual_freight;
	}
	public String getActual_weight() {
		return actual_weight;
	}
	public void setActual_weight(String actual_weight) {
		this.actual_weight = actual_weight;
	}
	public String getActual_volume() {
		return actual_volume;
	}
	public void setActual_volume(String actual_volume) {
		this.actual_volume = actual_volume;
	}
	public String getChange_delivery() {
		return change_delivery;
	}
	public void setChange_delivery(String change_delivery) {
		this.change_delivery = change_delivery;
	}
	public String getChange_price() {
		return change_price;
	}
	public void setChange_price(String change_price) {
		this.change_price = change_price;
	}
	public String getNewsourceurl() {
		return newsourceurl;
	}
	public void setNewsourceurl(String newsourceurl) {
		this.newsourceurl = newsourceurl;
	}
	public int getIscancel() {
		return iscancel;
	}
	public void setIscancel(int iscancel) {
		this.iscancel = iscancel;
	}
	public void setProduct_cost(String product_cost) {
		this.product_cost = product_cost;
	}
	public String getProduct_cost() {
		return product_cost;
	}
	
	public String getChange_freight() {
		return change_freight;
	}
	public void setChange_freight(String change_freight) {
		this.change_freight = change_freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getFreight() {
		return freight;
	}
	
	public int getFreight_free() {
		return freight_free;
	}
	public void setFreight_free(int freight_free) {
		this.freight_free = freight_free;
	}
	public String getRemaining_price() {
		return remaining_price;
	}
	public void setRemaining_price(String remaining_price) {
		this.remaining_price = remaining_price;
	}
	
	public void setPreferential(int preferential) {
		this.preferential = preferential;
	}
	public int getPreferential() {
		return preferential;
	}
	
	public int getPurchase_state() {
		return purchase_state;
	}
	public void setPurchase_state(int purchase_state) {
		this.purchase_state = purchase_state;
	}
	public String getPurchase_time() {
		return purchase_time;
	}
	public void setPurchase_time(String purchase_time) {
		this.purchase_time = purchase_time;
	}
	public String getPurchase_confirmation() {
		return purchase_confirmation;
	}
	public void setPurchase_confirmation(String purchase_confirmation) {
		this.purchase_confirmation = purchase_confirmation;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrency() {
		return currency;
	}
	public void setGoods_class(int goods_class) {
		this.goods_class = goods_class;
	}
	public int getGoods_class() {
		return goods_class;
	}
	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}
	public String getImg_type() {
		return img_type;
	}
	public String getOldUrl() {
		return oldUrl;
	}
	public void setOldUrl(String oldUrl) {
		this.oldUrl = oldUrl;
	}
	public void setExtra_freight(double extra_freight) {
		this.extra_freight = extra_freight;
	}
	public double getExtra_freight() {
		return extra_freight;
	}
	
	public String getGoods_url() {
		return goods_url;
	}
	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	public String getGoods_type() {
		return goods_type;
	}
	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}
	public void setGoods_freight(String goods_freight) {
		this.goods_freight = goods_freight;
	}
	public String getGoods_freight() {
		return goods_freight;
	}
	public String getGbulk_volume() {
		return gbulk_volume;
	}
	public void setGbulk_volume(String gbulk_volume) {
		this.gbulk_volume = gbulk_volume;
	}
	public String getGtotal_weight() {
		return gtotal_weight;
	}
	public void setGtotal_weight(String gtotal_weight) {
		this.gtotal_weight = gtotal_weight;
	}
	
	public String getExporttime() {
		return exporttime;
	}
	public void setExporttime(String exporttime) {
		this.exporttime = exporttime;
	}
	
	public List<String> getChange_communication() {
		return change_communication;
	}
	public void setChange_communication(List<String> change_communication) {
		this.change_communication = change_communication;
	}
	public String getChange_number() {
		return change_number;
	}
	public void setChange_number(String change_number) {
		this.change_number = change_number;
	}
	
	public void setPreferential_price(double preferential_price) {
		this.preferential_price = preferential_price;
	}
	public double getPreferential_price() {
		return preferential_price;
	}
	public void setDiscount_ratio(double discount_ratio) {
		this.discount_ratio = discount_ratio;
	}
	public double getDiscount_ratio() {
		return discount_ratio;
	}
	
	
	public int getRopType() {
		return ropType;
	}
	public void setRopType(int ropType) {
		this.ropType = ropType;
	}
	public int getDel_state() {
		return del_state;
	}
	public void setDel_state(int del_state) {
		this.del_state = del_state;
	}
	/**
	 * @return 替代购买数量
	 */
	public int getBuycount() {
		return buycount;
	}
	/**
	 * @param 替代购买数量
	 */
	public void setBuycount(int buycount) {
		this.buycount = buycount;
	}
	
	public String getCar_url() {
		return car_url;
	}
	public void setCar_url(String car_url) {
		this.car_url = car_url;
	}
	public String getCar_img() {
		return car_img;
	}
	public void setCar_img(String car_img) {
		this.car_img = car_img;
	}
	public String getCar_type() {
		return car_type;
	}
	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}
	
	public String getSprice() {
		return sprice;
	}
	public void setSprice(String sprice) {
		this.sprice = sprice;
	}
	
	public int getBuy_for_me() {
		return buy_for_me;
	}
	public void setBuy_for_me(int buy_for_me) {
		this.buy_for_me = buy_for_me;
	}
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public int getIsAuto() {
		return isAuto;
	}
	public void setIsAuto(int isAuto) {
		this.isAuto = isAuto;
	}
	public int getIsFeight() {
		return isFeight;
	}
	public void setIsFeight(int isFeight) {
		this.isFeight = isFeight;
	}
	
	public String getDropShipState() {
		return dropShipState;
	}
	public void setDropShipState(String dropShipState) {
		this.dropShipState = dropShipState;
	}
	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"userid\":\"%s\", \"goodsid\":\"%s\", \"goodsname\":\"%s\", \"orderid\":\"%s\", \"delivery_time\":\"%s\", \"checkprice_fee\":\"%s\", \"checkproduct_fee\":\"%s\", \"state\":\"%s\", \"fileupload\":\"%s\", \"yourorder\":\"%s\", \"goodsprice\":\"%s\", \"freight\":\"%s\"}",
						id, userid, goodsid, goodsname, orderid, delivery_time,
						checkprice_fee, checkproduct_fee, state, fileupload,
						yourorder, goodsprice, freight);
	}
	
	
    /* 为实现OrderDetailsBean对象的克隆
     */
    @Override  
	public Object clone() {  
          OrderDetailsBean stu = null;  
	         try{  
	             stu = (OrderDetailsBean)super.clone();   //浅复制  
	         }catch(CloneNotSupportedException e) {  
	             e.printStackTrace();  
	         }  
	         return stu;  
	}
	public String getBargainRemark() {
		return bargainRemark;
	}
	public void setBargainRemark(String bargainRemark) {
		this.bargainRemark = bargainRemark;
	}
	public String getDeliveryRemark() {
		return deliveryRemark;
	}
	public void setDeliveryRemark(String deliveryRemark) {
		this.deliveryRemark = deliveryRemark;
	}
	public String getColorReplaceRemark() {
		return colorReplaceRemark;
	}
	public void setColorReplaceRemark(String colorReplaceRemark) {
		this.colorReplaceRemark = colorReplaceRemark;
	}
	public String getSizeReplaceRemark() {
		return sizeReplaceRemark;
	}
	public void setSizeReplaceRemark(String sizeReplaceRemark) {
		this.sizeReplaceRemark = sizeReplaceRemark;
	}
	public String getOrderNumRemarks() {
		return orderNumRemarks;
	}
	public void setOrderNumRemarks(String orderNumRemarks) {
		this.orderNumRemarks = orderNumRemarks;
	}
	public String getQuestionsRemarks() {
		return questionsRemarks;
	}
	public void setQuestionsRemarks(String questionsRemarks) {
		this.questionsRemarks = questionsRemarks;
	}
	public String getUnquestionsRemarks() {
		return unquestionsRemarks;
	}
	public void setUnquestionsRemarks(String unquestionsRemarks) {
		this.unquestionsRemarks = unquestionsRemarks;
	}
	public String getAgainRemarks() {
		return againRemarks;
	}
	public void setAgainRemarks(String againRemarks) {
		this.againRemarks = againRemarks;
	}  
    
    
    
}
