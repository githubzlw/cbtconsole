package com.cbt.parse.bean;

import com.cbt.parse.service.DownloadMain;

/**数据结构（用于搜索页面商品）
 * @author abc
 *
 */
public class SearchGoods implements Comparable<SearchGoods>{
	private String goods_url;//商品链接
	private String goods_name;//商品名
	private String goods_image;//商品图片
	private String goods_price;//商品价格
	private String goods_minOrder;//商品最小订单
	private String goods_dispatch;//48小时内派件
	private String goods_shop = "";//商店名称
	private String goods_business;//商品混批价
	private String goods_colors;//商品颜色数
	private String seller_online;//店家在线  0-不在线  1-在线
	private int seller_flag;//店家黑名单标志
	private String goods_free;//免邮
	private String goods_solder;//已经售出数量
	private String goods_similar;//相似商品标志  0-不存在任何相似商品  1-存在相似商品
	private String goods_pid;
	private String shop_id;
	
	//以下用于区别类别、类型、导航、页码等
	private String key_type;//参数
	private String key_name;//参数名称
	private String key_url;//链接
	private String key_img;//图片
	private String key_id;//id
	
	//请求ip
	private String ip;
	
	//2016-2-26  aliexpress cache
	private int id;//数据表id
	private int valid;//数据表有效性
	private String param_id;//
	private String createtime;
	private int total;
	
	
	
	public String getGoods_pid() {
		return goods_pid;
	}
	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getParam_id() {
		return param_id;
	}
	public void setParam_id(String param_id) {
		this.param_id = param_id;
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
	public int getSeller_flag() {
		return seller_flag;
	}
	public void setSeller_flag(int seller_flag) {
		this.seller_flag = seller_flag;
	}
	public String getSeller_online() {
		return seller_online;
	}
	public void setSeller_online(String seller_online) {
		this.seller_online = seller_online;
	}
	public String getGoods_similar() {
		return goods_similar;
	}
	public void setGoods_similar(String goods_similar) {
		this.goods_similar = goods_similar;
	}
	public String getKey_id() {
		return key_id;
	}
	public void setKey_id(String key_id) {
		this.key_id = key_id;
	}
	public String getKey_img() {
		return key_img;
	}
	public void setKey_img(String key_img) {
		this.key_img = key_img;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getGoods_colors() {
		return goods_colors;
	}
	public void setGoods_colors(String goods_colors) {
		this.goods_colors = goods_colors;
	}
	public String getGoods_shop() {
		return goods_shop;
	}
	public void setGoods_shop(String goods_shop) {
		if(goods_shop!=null){
			goods_shop = goods_shop.replace("\"", "\\\"");
		}
		this.goods_shop = goods_shop;
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
//		if(key_name!=null){
//			key_name = key_name.replace("\"", "&&");
//		}
		this.key_name = key_name;
	}
	
	public String getKey_url() {
		return key_url;
	}
	
	public void setKey_url(String key_url) {
		this.key_url = key_url;
	}
	
	public String getGoods_free() {
		return goods_free;
	}
	public void setGoods_free(String goods_free) {
		this.goods_free = goods_free;
	}
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
	//“Hot Sale, Free Shiping, 100% Brand New,
	//Brand New, Top Rated,Lowest Price, (ship from us)，
	//High Quality, 2015
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
	public void setGoods_business(String goods_business) {
		this.goods_business = goods_business;
	}
	public String getGoods_business() {
		return goods_business;
	}
	
	@Override
	public String toString() {
		return String
				.format("{\"goods_url\":\"%s\", \"goods_name\":\"%s\", "
						+ "\"goods_image\":\"%s\", \"goods_price\":\"%s\","
						+ " \"goods_minOrder\":\"%s\", \"goods_solder\":\"%s\","
						+ " \"goods_dispatch\":\"%s\", \"goods_fee\":\"%s\","
						+ "\"goods_shop\":\"%s\",\"key_type\":\"%s\", \"key_name\":\"%s\", "
						+ "\"key_url\":\"%s\"}",
						goods_url, goods_name, goods_image, goods_price,
						goods_minOrder, goods_solder, goods_dispatch, goods_free,goods_shop,key_type,
						key_name, key_url);
	}
	@Override
	public int compareTo(SearchGoods o) {
		SearchGoods s1 = (SearchGoods) o;
		String price1 = s1.getGoods_price();
		String price2 = this.goods_price;
//		String min1 = s1.getGoods_minOrder();
//		String min2 = this.goods_minOrder;
		
		if(price1!=null&&price2!=null){
			if(price1.indexOf("-")>0){
				price1 = price1.substring(0, price1.indexOf("-")).trim();
			}
			if(price2.indexOf("-")>0){
				price2 = price2.substring(0, price2.indexOf("-")).trim();
			}
			price1 = DownloadMain.getSpiderContext(price1, "(\\d+\\.*\\d*)");
			price2 = DownloadMain.getSpiderContext(price2, "(\\d+\\.*\\d*)");
//			min1 = DownloadMain.getSpiderContext(min1, "(\\d+)");
//			min2 = DownloadMain.getSpiderContext(min2, "(\\d+)");
			if(!price1.isEmpty()&&!price2.isEmpty()){
				Double priced1 = Double.valueOf(price1);
				Double priced2 = Double.valueOf(price2);
//				Double mind1 = Double.valueOf(min1);
//				Double mind2 = Double.valueOf(min2);
//				Double result1 = priced1*mind1;
//				Double result2 = priced2*mind2;
				return priced1.compareTo(priced2);
			}
			return 0;
		}
		  return 0;
	}
	

}
