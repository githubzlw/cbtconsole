package com.cbt.parse.bean;

/**热搜商品    hotsale.sql
 * @author abc
 *
 */
public class HotWordBean {
	private String hotwords;//热搜词
	private String url;//商品链接
	private String img;//图片
	private double minprice;//价格
	private double maxprice;//
	private int morder;//订量
	private String punit;//货币单位
	private String gunit;//售卖单位
	private int valid ;//数据有效性
	private String time;//入库时间
	
	private String name;//商品名称
	private String wprice;//批发价
	private String pid;//商品id
	private String type;//规格参数
	private String info;//详情
	private String weight;//重量
	private String pweight;//单位重量
	private String width;//体积
	
	
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getPweight() {
		return pweight;
	}
	public void setPweight(String pweight) {
		this.pweight = pweight;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWprice() {
		return wprice;
	}
	public void setWprice(String wprice) {
		this.wprice = wprice;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getMorder() {
		return morder;
	}
	public void setMorder(int morder) {
		this.morder = morder;
	}
	public String getHotwords() {
		return hotwords;
	}
	public void setHotwords(String hotwords) {
		this.hotwords = hotwords;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public double getMinprice() {
		return minprice;
	}
	public void setMinprice(double minprice) {
		this.minprice = minprice;
	}
	public double getMaxprice() {
		return maxprice;
	}
	public void setMaxprice(double maxprice) {
		this.maxprice = maxprice;
	}
	public String getPunit() {
		return punit;
	}
	public void setPunit(String punit) {
		this.punit = punit;
	}
	public String getGunit() {
		return gunit;
	}
	public void setGunit(String gunit) {
		this.gunit = gunit;
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
	
	
	
	

}
