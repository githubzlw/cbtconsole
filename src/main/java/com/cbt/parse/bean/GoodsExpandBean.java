package com.cbt.parse.bean;

/**goodsdata延伸表   goodsdata_expand.sql
 * @author abc
 *
 */
public class GoodsExpandBean {
	private int total;
	private String keywords;
	private String description;
	private int valid;//数据有效性标志
	private String time;//入库时间
	private int id;//
	private String cid;//网站来源
	private String pid;//商品id
	private String url;//商品链接
	private String catid1;//类别id
	private String catid2;//类别id
	private String catid3;//类别id
	private String catid4;//类别id
	private String catid5;//类别id
	private String catid6;//类别id
	private String noteurl;//用户反馈链接
	private double minprice;//价格
	private double maxprice;//
	private int morder;//最小订量
	private int sell;//已经售出数量
	private String img;//图片
	private String name;//名称
	private String punit;//货币单位
	private String gunit;//售卖单位
	private String pvid;//规格id
	private String sid;//商店id
	
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getPvid() {
		return pvid;
	}
	public void setPvid(String pvid) {
		this.pvid = pvid;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
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
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getMorder() {
		return morder;
	}
	public void setMorder(int morder) {
		this.morder = morder;
	}
	public int getSell() {
		return sell;
	}
	public void setSell(int sell) {
		this.sell = sell;
	}
	public String getNoteurl() {
		return noteurl;
	}
	public void setNoteurl(String noteurl) {
		this.noteurl = noteurl;
	}
	public String getCatid1() {
		return catid1;
	}
	public void setCatid1(String catid1) {
		this.catid1 = catid1;
	}
	public String getCatid2() {
		return catid2;
	}
	public void setCatid2(String catid2) {
		this.catid2 = catid2;
	}
	public String getCatid3() {
		return catid3;
	}
	public void setCatid3(String catid3) {
		this.catid3 = catid3;
	}
	public String getCatid4() {
		return catid4;
	}
	public void setCatid4(String catid4) {
		this.catid4 = catid4;
	}
	public String getCatid5() {
		return catid5;
	}
	public void setCatid5(String catid5) {
		this.catid5 = catid5;
	}
	public String getCatid6() {
		return catid6;
	}
	public void setCatid6(String catid6) {
		this.catid6 = catid6;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
