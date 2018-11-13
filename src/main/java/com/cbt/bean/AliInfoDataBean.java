package com.cbt.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 工厂核心商品产品实体类
 * @ClassName AliInfoDataBean 
 * @Description TODO
 * @author whj
 * @date 2018年2月11日 上午11:27:12
 */
public class AliInfoDataBean implements Serializable{

	/**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = 1L;
	private int id;//ID
    private String goods_pid;//商品id
    private String goods_name;//商品名称
    private String goods_price;//商品价格
    private String price_min;//
    private String goods_img;//图片
    private String goods_url;//商品链接
    private String goods_weight;//产品重量
    private String goods_width;//体积
    private String goods_sellunits;//计数单位
    private String pUtil;
    private String goods_freight;//运费
    private String goods_method;//运输方式
    private String goods_posttime;//运输时间
    private String goods_moq;//最小订量
    private int goods_sold;//商品售出
    private String ali_catid1;//ali类别
    private String ali_catid2;
    private String ali_catid3;
    private String ali_catid4;
    private String ali_catid5;
    private String ali_catid6;
    private int img_check;
    private String url_1688;
    private String pid_1688;
    private String shop_id;
    private String price_1688;//1688工厂价
    private String cat_1688;//1688类别
    private String freight;//运费
    private int mark_flag;//货源对标情况 ：（1）精确对标 （2）近似对标 （3） 没找到对标 （理论上 线上 商品 不会有此状态）（4）成功卖过  (5) 未对标直接上传 （一般是同店商品上传）
    private int bm_flag;//是人工判断的货源（1）是  （2）否
    private int cat_flag;//ali和1688类别标识，1：类别一样，2：类别不一样
    private int img_flag;//橱窗图片标识
    private int remark;//0:未处理 1：处理成功 3：未解析到页面信息 6：异常 7：已存在该数据
    private String serviceid;//
    private String update_time;//
    private int flag;//数据标识0新,1老数据
    private int handle_flag;//0:人为未筛选1：人为已筛选
    private String source_tbl;//关联数据来源表
    private int sold_flag;//1卖过，2没卖过
    private int nobench_flag;//1：人为对标完成，尚未验证库存,2:人为对标完成，已经验证库存，尚未上线3:人为对标完成，已经验证库存，上线4:有对标，404， 我们下架5:有对标，1688下架，我们下架6:有对标，无库存，我们下架
    private int priority_flag;//0默认1核心商品 ，2非核心
    private int source_pro_flag;//货源属性 1:同店铺商品,2:同款商品 ,3:对标商品
    private String source_ylpid;//原1688pid
    private String img_1688;//1688图片
    private List<TypeBean> entype;//1688规格信息
    private List<TypeBean> type;//商品规格
    private String list_type;
    private String type_msg;
    private String sku_inventory;//规格库存信息
    private String sku;
	private String r_shop_id;
	private String r_pid;
	private String remarks;
	private String sku_prop_ids;
	public String getSku_prop_ids() {
		return sku_prop_ids;
	}

	public void setSku_prop_ids(String sku_prop_ids) {
		this.sku_prop_ids = sku_prop_ids;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getR_shop_id() {
		return r_shop_id;
	}

	public void setR_shop_id(String r_shop_id) {
		this.r_shop_id = r_shop_id;
	}

	public String getR_pid() {
		return r_pid;
	}

	public void setR_pid(String r_pid) {
		this.r_pid = r_pid;
	}

	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getSku_inventory() {
		return sku_inventory;
	}
	public void setSku_inventory(String sku_inventory) {
		this.sku_inventory = sku_inventory;
	}
	public String getType_msg() {
		return type_msg;
	}
	public void setType_msg(String type_msg) {
		this.type_msg = type_msg;
	}
	public String getList_type() {
		return list_type;
	}
	public void setList_type(String list_type) {
		this.list_type = list_type;
	}
	public List<TypeBean> getType() {
		return type;
	}
	public void setType(List<TypeBean> type) {
		this.type = type;
	}
	public List<TypeBean> getEntype() {
		return entype;
	}
	public void setEntype(List<TypeBean> entype) {
		this.entype = entype;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGoods_pid() {
		return goods_pid;
	}
	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}
	public String getPrice_min() {
		return price_min;
	}
	public void setPrice_min(String price_min) {
		this.price_min = price_min;
	}
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	public String getGoods_url() {
		return goods_url;
	}
	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}
	public String getGoods_weight() {
		return goods_weight;
	}
	public void setGoods_weight(String goods_weight) {
		this.goods_weight = goods_weight;
	}
	public String getGoods_width() {
		return goods_width;
	}
	public void setGoods_width(String goods_width) {
		this.goods_width = goods_width;
	}
	public String getGoods_sellunits() {
		return goods_sellunits;
	}
	public void setGoods_sellunits(String goods_sellunits) {
		this.goods_sellunits = goods_sellunits;
	}
	public String getpUtil() {
		return pUtil;
	}
	public void setpUtil(String pUtil) {
		this.pUtil = pUtil;
	}
	public String getGoods_freight() {
		return goods_freight;
	}
	public void setGoods_freight(String goods_freight) {
		this.goods_freight = goods_freight;
	}
	public String getGoods_method() {
		return goods_method;
	}
	public void setGoods_method(String goods_method) {
		this.goods_method = goods_method;
	}
	public String getGoods_posttime() {
		return goods_posttime;
	}
	public void setGoods_posttime(String goods_posttime) {
		this.goods_posttime = goods_posttime;
	}
	public String getGoods_moq() {
		return goods_moq;
	}
	public void setGoods_moq(String goods_moq) {
		this.goods_moq = goods_moq;
	}
	public int getGoods_sold() {
		return goods_sold;
	}
	public void setGoods_sold(int goods_sold) {
		this.goods_sold = goods_sold;
	}
	public String getAli_catid1() {
		return ali_catid1;
	}
	public void setAli_catid1(String ali_catid1) {
		this.ali_catid1 = ali_catid1;
	}
	public String getAli_catid2() {
		return ali_catid2;
	}
	public void setAli_catid2(String ali_catid2) {
		this.ali_catid2 = ali_catid2;
	}
	public String getAli_catid3() {
		return ali_catid3;
	}
	public void setAli_catid3(String ali_catid3) {
		this.ali_catid3 = ali_catid3;
	}
	public String getAli_catid4() {
		return ali_catid4;
	}
	public void setAli_catid4(String ali_catid4) {
		this.ali_catid4 = ali_catid4;
	}
	public String getAli_catid5() {
		return ali_catid5;
	}
	public void setAli_catid5(String ali_catid5) {
		this.ali_catid5 = ali_catid5;
	}
	public String getAli_catid6() {
		return ali_catid6;
	}
	public void setAli_catid6(String ali_catid6) {
		this.ali_catid6 = ali_catid6;
	}
	public int getImg_check() {
		return img_check;
	}
	public void setImg_check(int img_check) {
		this.img_check = img_check;
	}
	public String getUrl_1688() {
		return url_1688;
	}
	public void setUrl_1688(String url_1688) {
		this.url_1688 = url_1688;
	}
	public String getPid_1688() {
		return pid_1688;
	}
	public void setPid_1688(String pid_1688) {
		this.pid_1688 = pid_1688;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getPrice_1688() {
		return price_1688;
	}
	public void setPrice_1688(String price_1688) {
		this.price_1688 = price_1688;
	}
	public String getCat_1688() {
		return cat_1688;
	}
	public void setCat_1688(String cat_1688) {
		this.cat_1688 = cat_1688;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public int getMark_flag() {
		return mark_flag;
	}
	public void setMark_flag(int mark_flag) {
		this.mark_flag = mark_flag;
	}
	public int getBm_flag() {
		return bm_flag;
	}
	public void setBm_flag(int bm_flag) {
		this.bm_flag = bm_flag;
	}
	public int getCat_flag() {
		return cat_flag;
	}
	public void setCat_flag(int cat_flag) {
		this.cat_flag = cat_flag;
	}
	public int getImg_flag() {
		return img_flag;
	}
	public void setImg_flag(int img_flag) {
		this.img_flag = img_flag;
	}
	public int getRemark() {
		return remark;
	}
	public void setRemark(int remark) {
		this.remark = remark;
	}
	public String getServiceid() {
		return serviceid;
	}
	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getHandle_flag() {
		return handle_flag;
	}
	public void setHandle_flag(int handle_flag) {
		this.handle_flag = handle_flag;
	}
	public String getSource_tbl() {
		return source_tbl;
	}
	public void setSource_tbl(String source_tbl) {
		this.source_tbl = source_tbl;
	}
	public int getSold_flag() {
		return sold_flag;
	}
	public void setSold_flag(int sold_flag) {
		this.sold_flag = sold_flag;
	}
	public int getNobench_flag() {
		return nobench_flag;
	}
	public void setNobench_flag(int nobench_flag) {
		this.nobench_flag = nobench_flag;
	}
	public int getPriority_flag() {
		return priority_flag;
	}
	public void setPriority_flag(int priority_flag) {
		this.priority_flag = priority_flag;
	}
	public int getSource_pro_flag() {
		return source_pro_flag;
	}
	public void setSource_pro_flag(int source_pro_flag) {
		this.source_pro_flag = source_pro_flag;
	}
	public String getSource_ylpid() {
		return source_ylpid;
	}
	public void setSource_ylpid(String source_ylpid) {
		this.source_ylpid = source_ylpid;
	}
	public String getImg_1688() {
		return img_1688;
	}
	public void setImg_1688(String img_1688) {
		this.img_1688 = img_1688;
	}
}
