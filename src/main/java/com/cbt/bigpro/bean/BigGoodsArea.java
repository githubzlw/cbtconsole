package com.cbt.bigpro.bean;

import com.cbt.parse.service.TypeUtils;

public class BigGoodsArea {

	private int id; //id
    private String   goodsId;  //商品id
    private String   goodsurl; // 商品url
    private  double  num;  //商品数量
    private  double  discount; //折扣
    private  double  price ;   //单个商品价格
    private   String title ;   //标题
    private   int  total;   //统计该数据总数
    private   int  flag;   //0:正在卖|1:下架
    private   String  img ;  //图片
    private   String   weight;  //重量
    private   String   category ;//小类别
    private   String   category1 ; //大类别
    private   String    catid; // 类别id
    private   String     catid1;//小类别
    private   String  keyWord;   //关键字
    private   String   localpath ;  
    
    public String getUrl(){
		if(!"".equals(goodsurl) && goodsurl!=null){
			return TypeUtils.encodeGoods(goodsurl);
		}else{
			return "";
		}
		
	}

	public String getCategory1() {
		return category1;
	}

	public void setCategory1(String category1) {
		this.category1 = category1;
	}

	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getGoodsurl() {
		return goodsurl;
	}
	public void setGoodsurl(String goodsurl) {
		this.goodsurl = goodsurl;
	}
	public double getNum() {
		return num;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public void setNum(double num) {
		this.num = num;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getCategory() {
		return category;
	}
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getLocalpath() {
		return localpath;
	}

	public void setLocalpath(String localpath) {
		this.localpath = localpath;
	}

	public String getCatid1() {
		return catid1;
	}

	public void setCatid1(String catid1) {
		this.catid1 = catid1;
	}
	
	
    
    
}
