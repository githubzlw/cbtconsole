package com.cbt.parse.bean;


/**对应yiwu.sql
 * @author abc
 *
 */
public class YiWuBean {
	private String product_id;//商品id
	private String product_name;//商品名称
	private String product_url;//商品url
	private String store_name;//商店名称
	private String category_name;//类别名称
	private String category_id;//类别id
	private String product_minprice="0";//最小价格
	private String product_maxprice="0";//最大价格
	private String product_desc;//商品简介
	private String minorder;//最小订量
	private String update_time;//原网站更新时间
	private String store_number;//商店编码
	private String imgs;//图片
	private int valid=1;//数据有效性
	private String time;//数据入库时间
	private String info;//商品详情
	private String wPrice;//批发价格
	private String type;//规格参数
	private String ctitle;//
	private String pUnit;//货币单位
	private String gUnit;//商品单位
	
	
	
	
	public String getwPrice() {
		return wPrice;
	}
	public void setwPrice(String wPrice) {
		this.wPrice = wPrice;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return ctitle;
	}
	public void setTitle(String ctitle) {
		this.ctitle = ctitle;
	}
	public String getpUnit() {
		return pUnit;
	}
	public void setpUnit(String pUnit) {
		this.pUnit = pUnit;
	}
	public String getgUnit() {
		return gUnit;
	}
	public void setgUnit(String gUnit) {
		this.gUnit = gUnit;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getProductId(){
		 return product_id;
	}
	public void setProductId(String product_id){
		this.product_id = product_id;
	}
	
	
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getProductName() {
		return product_name;
	}
	public void setProductName(String product_name) {
		this.product_name = product_name;
	}
	public String getProductUrl() {
		return product_url;
	}
	public void setProductUrl(String product_url) {
		this.product_url = product_url;
	}
	public String getStoreName() {
		return store_name;
	}
	public void setStoreName(String store_name) {
		this.store_name = store_name;
	}
	public String getCategoryName() {
		return category_name;
	}
	public void setCategoryName(String category_name) {
		this.category_name = category_name;
	}
	public String getProductMinPrice() {
		return product_minprice;
	}
	public void setProductMinPrice(String product_minprice) {
		this.product_minprice = product_minprice;
	}
	public String getProductMaxPrice() {
		return product_maxprice;
	}
	public void setProductMaxPrice(String product_maxprice) {
		this.product_maxprice = product_maxprice;
	}
	public String getProductDesc() {
		return product_desc;
	}
	public void setProductDesc(String product_desc) {
		this.product_desc = product_desc;
	}
	public String getMinorder() {
		return minorder;
	}
	public void setMinorder(String minorder) {
		this.minorder = minorder;
	}
	public String getUpdateTime() {
		return update_time;
	}
	public void setUpdateTime(String update_time) {
		this.update_time = update_time;
	}
	public String getStoreNumber() {
		return store_number;
	}
	public void setStoreNumber(String store_number) {
		this.store_number = store_number;
	}
	public String getImgs() {
		return imgs;
	}
	public void setImgs(String imgs) {
		this.imgs = imgs;
	}
	
	public Boolean isEmpty(){
		if(product_name!=null&&!product_name.isEmpty()){
			 return false;
		}
			return true;
	}
	
	

}
