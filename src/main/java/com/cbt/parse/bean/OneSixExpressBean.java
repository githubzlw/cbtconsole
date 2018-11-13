package com.cbt.parse.bean;

public class OneSixExpressBean {
	private int id;
	//搜索
	private String url;//商品链接
	private String name;//商品名称（中文）
	private String enname;//商品名称（英文）
	private String sold;//商品已售出数量
	private String price;//商品价格（搜索页面）
	private String img;//商品图片（搜索页面）
	private String morder;//商品最小订量
	private String catid1;//商品对应aiexpress分类id1
	private String catid2;//商品对应aiexpress分类id2
	private String catid3;//商品对应aiexpress分类id3
	private String catid4;//商品对应aiexpress分类id4
	private String catid5;//商品对应aiexpress分类id5
	private String catid6;//商品对应aiexpress分类id6
	private String similar;//商品相似商品
	private String pvid;//商品对应aliexpress的类型id值
	private String same;//同款
	
	
	//商品单页
	private String wprice;//批发价格
	private String imgs;//图片集合
	private String imgsize;//图片尺寸
	private String detail;//明细（中文）
	private String endetail;//明细（英文）
	private String info;//详情（图片）
	private String entype;//规格（英文）
	private String type;//规格（中文）
	private String gunit;//商品售卖单位
	private String punit;//货币单位
	private String weight;//重量
	private String pid;//商品id
	private String sid;//商店id
	private String createtime;//入库时间
	private String feeprice;//运费
	private String fprice;//非免邮价格
	private String posttime;//快递运输时间
	private String method;//快递方式
	private String wprices;//调整后的批发价
	private String prices;//调整后的商品价格（搜索页面）
	
	private int valid;//数据valid
	private int total;//总数（搜索页面）
	
	
	public String getWprices() {
		return wprices;
	}
	public void setWprices(String wprices) {
		this.wprices = wprices;
	}
	public String getPrices() {
		return prices;
	}
	public void setPrices(String prices) {
		this.prices = prices;
	}
	public String getPosttime() {
		return posttime;
	}
	public void setPosttime(String posttime) {
		this.posttime = posttime;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getFeeprice() {
		return feeprice;
	}
	public void setFeeprice(String feeprice) {
		this.feeprice = feeprice;
	}
	public String getFprice() {
		return fprice;
	}
	public void setFprice(String fprice) {
		this.fprice = fprice;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getEndetail() {
		return endetail;
	}
	public void setEndetail(String endetail) {
		this.endetail = endetail;
	}
	public String getEntype() {
		return entype;
	}
	public void setEntype(String entype) {
		this.entype = entype;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getSame() {
		return same;
	}
	public void setSame(String same) {
		this.same = same;
	}
	public String getPvid() {
		return pvid;
	}
	public void setPvid(String pvid) {
		this.pvid = pvid;
	}
	public String getImgsize() {
		return imgsize;
	}
	public void setImgsize(String imgsize) {
		this.imgsize = imgsize;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSimilar() {
		return similar;
	}
	public void setSimilar(String similar) {
		this.similar = similar;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnname() {
		return enname;
	}
	public void setEnname(String enname) {
		this.enname = enname;
	}
	public String getSold() {
		return sold;
	}
	public void setSold(String sold) {
		this.sold = sold;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getMorder() {
		return morder;
	}
	public void setMorder(String morder) {
		this.morder = morder;
	}
	public String getWprice() {
		return wprice;
	}
	public void setWprice(String wprice) {
		this.wprice = wprice;
	}
	public String getImgs() {
		return imgs;
	}
	public void setImgs(String imgs) {
		this.imgs = imgs;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGunit() {
		return gunit;
	}
	public void setGunit(String gunit) {
		this.gunit = gunit;
	}
	public String getPunit() {
		return punit;
	}
	public void setPunit(String punit) {
		this.punit = punit;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	
	
	
	

}
