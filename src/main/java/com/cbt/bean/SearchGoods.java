package com.cbt.bean;

/**数据结构（用于搜索页面商品）
 * @author abc
 *
 */
public class SearchGoods {
private static final long serialVersionUID = 5232349002237242229L;
	
	private String goods_url;//商品链接
	private String goods_name;//商品名
	private String goods_image;//商品图片
	private String goods_price;//商品价格
	private double goods_minPrice;//商品最小价格
	private String goods_minOrder;//商品最小订单
	private String goods_dispatch;//48小时内派件
	private String goods_shop = "";//商店名称
	public double getGoods_minPrice() {
		return goods_minPrice;
	}
	public void setGoods_minPrice(double goods_minPrice) {
		this.goods_minPrice = goods_minPrice;
	}
	public String getGoods_shop() {
		return goods_shop;
	}
	public void setGoods_shop(String goods_shop) {
		this.goods_shop = goods_shop;
	}
	public String getGoods_business() {
		return goods_business;
	}
	public void setGoods_business(String goods_business) {
		this.goods_business = goods_business;
	}
	public String getGoods_colors() {
		return goods_colors;
	}
	public void setGoods_colors(String goods_colors) {
		this.goods_colors = goods_colors;
	}
	public String getSeller_online() {
		return seller_online;
	}
	public void setSeller_online(String seller_online) {
		this.seller_online = seller_online;
	}
	public int getSeller_flag() {
		return seller_flag;
	}
	public void setSeller_flag(int seller_flag) {
		this.seller_flag = seller_flag;
	}
	public String getGoods_free() {
		return goods_free;
	}
	public void setGoods_free(String goods_free) {
		this.goods_free = goods_free;
	}
	public String getGoods_similar() {
		return goods_similar;
	}
	public void setGoods_similar(String goods_similar) {
		this.goods_similar = goods_similar;
	}
	public String getGoods_pid() {
		return goods_pid;
	}
	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}
	public String getGoods_unFreePrice() {
		return goods_unFreePrice;
	}
	public void setGoods_unFreePrice(String goods_unFreePrice) {
		this.goods_unFreePrice = goods_unFreePrice;
	}
	public String getKey_type() {
		return key_type;
	}
	public void setKey_type(String key_type) {
		this.key_type = key_type;
	}
	public String getKey_name() {
		return key_name;
	}
	public void setKey_name(String key_name) {
		this.key_name = key_name;
	}
	public String getKey_url() {
		return key_url;
	}
	public void setKey_url(String key_url) {
		this.key_url = key_url;
	}
	public String getKey_img() {
		return key_img;
	}
	public void setKey_img(String key_img) {
		this.key_img = key_img;
	}
	public String getKey_id() {
		return key_id;
	}
	public void setKey_id(String key_id) {
		this.key_id = key_id;
	}
	public String getCategory_level() {
		return category_level;
	}
	public void setCategory_level(String category_level) {
		this.category_level = category_level;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public String getParam_id() {
		return param_id;
	}
	public void setParam_id(String param_id) {
		this.param_id = param_id;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getStaticize_url() {
		return staticize_url;
	}
	public void setStaticize_url(String staticize_url) {
		this.staticize_url = staticize_url;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public String getRemotPath() {
		return remotPath;
	}
	public void setRemotPath(String remotPath) {
		this.remotPath = remotPath;
	}
	public String getGoodsWeight() {
		return goodsWeight;
	}
	public void setGoodsWeight(String goodsWeight) {
		this.goodsWeight = goodsWeight;
	}
	public String getGoodsVolum() {
		return goodsVolum;
	}
	public void setGoodsVolum(String goodsVolum) {
		this.goodsVolum = goodsVolum;
	}
	public String getGoodsBizDiscount() {
		return goodsBizDiscount;
	}
	public void setGoodsBizDiscount(String goodsBizDiscount) {
		this.goodsBizDiscount = goodsBizDiscount;
	}
	public double getFreight() {
		return freight;
	}
	public void setFreight(double freight) {
		this.freight = freight;
	}
	public double getoMoqFreight() {
		return oMoqFreight;
	}
	public void setoMoqFreight(double oMoqFreight) {
		this.oMoqFreight = oMoqFreight;
	}
	public double gettMoqFreight() {
		return tMoqFreight;
	}
	public void settMoqFreight(double tMoqFreight) {
		this.tMoqFreight = tMoqFreight;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getMoqFlag() {
		return moqFlag;
	}
	public void setMoqFlag(int moqFlag) {
		this.moqFlag = moqFlag;
	}
	public String getoMoqPrice() {
		return oMoqPrice;
	}
	public void setoMoqPrice(String oMoqPrice) {
		this.oMoqPrice = oMoqPrice;
	}
	public String gettMoqPrice() {
		return tMoqPrice;
	}
	public void settMoqPrice(String tMoqPrice) {
		this.tMoqPrice = tMoqPrice;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String goods_business;//商品混批价
	private String goods_colors;//商品颜色数
	private String seller_online;//店家在线  0-不在线  1-在线
	private int seller_flag;//店家黑名单标志
	private String goods_free;//免邮
	private String goods_solder;//已经售出数量
	private String goods_similar;//相似商品标志  0-不存在任何相似商品  1-存在相似商品
	private String goods_pid;//商品id
	private String goods_unFreePrice;//产品非免邮价格
	
	//以下用于区别类别、类型、导航、页码等
	private String key_type;//参数
	private String key_name;//参数名称
	private String key_url;//链接
	private String key_img;//图片
	private String key_id;//id
	private String category_level;//类别级别
	
	private String ip;//请求ip
	
	//2016-2-26  aliexpress cache
	private int id;//数据表id
	private int valid;//数据表有效性
	private String param_id;//
	private String createtime;
	private int total;
	
	//静态页面
	private String staticize_url;
	
	//本地上传产品数据用(1688)
	private String localPath;//本地图片服务器地址
	private String remotPath;//远程图片服务器地址
	
	//批量价格---2016-9-5-add
	private String goodsWeight;//产品重量
	private String  goodsVolum;//产品体积
	private String goodsBizDiscount;//批量折扣价格
	private double freight;//单个产品的运费
	private double oMoqFreight;//1moq的运费
	private double tMoqFreight;//2moq的运费
	private String category;//产品类别
	private int moqFlag;//批量价格标志
	private String oMoqPrice;//moq批量价格
	private String tMoqPrice;//2moq批量价格
	
	//drop-shipping用  产品原链接
	private String webUrl;//
	
	public String getGoods_url() {
		return goods_url;
	}
	public void setGoods_url(String url) {
		this.goods_url = url;
	}
	public String getGoods_solder() {
		return goods_solder;
	}
	public void setGoods_solder(String goods_solder) {
		this.goods_solder = goods_solder;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_image() {
		return goods_image;
	}
	public void setGoods_image(String goods_image) {
		this.goods_image = goods_image;
	}
	public String getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}
	public String getGoods_minOrder() {
		return goods_minOrder;
	}
	public void setGoods_minOrder(String goods_minOrder) {
		this.goods_minOrder = goods_minOrder;
	}
	
	public String getGoods_dispatch() {
		return goods_dispatch;
	}
	public void setGoods_dispatch(String goods_dispatch) {
		this.goods_dispatch = goods_dispatch;
	}
	@Override
	public String toString() {
		return " goods_name=" + goods_name + 
				"\n goods_image="+ goods_image + 
				"\n goods_url=" + goods_url+ 
				"\n goods_price=" + goods_price+ 
				"\n goods_minOrder=" + goods_minOrder+
				"\n goods_solder=" + goods_solder + 
				"\n goods_dispatch=" + goods_dispatch ;
	}

}
