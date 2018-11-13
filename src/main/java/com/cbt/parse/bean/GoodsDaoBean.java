package com.cbt.parse.bean;

/**对应goodsdata.sql
 * @author abc
 *
 */
public class GoodsDaoBean {
	private int total;
	private int id;
	private String valid;//数据有效性
	private String time;//入库时间
	private String cID;//网站来源
	private String pID;//商品id
	private String name;//商品名称
	private String url;//商品链接
	private String oPrice;//商品原价
	private String sPrice;//商品现价
	private String wPrice;//商品批发价
	private String pUnit;//货币单位
	private String gUnit;//售卖单位
	private String img;//图片集合
	private String imgSize;//图片尺寸
	private String mOrder;//最小订量
	private String pTime;//订单处理时间
	private String sellUnit;//
	private String weight;//重量
	private String width;//体积
	private String perWeight;//单位重量
	private String free;//免邮标志 1 免邮  2设置请求运费  3非免邮
	private String category;//类别导航
	private String cateurl;//
	private String sID;//商店id
	private String title;//商品title
	private String sUrl;//商店链接
	private String sName;//商店名称
	private String sGoods;//
	private String types;//规格参数
	private String info;//详情
	private String detail;//明细
	private String data;//
	private String sell;//已经售出数量
	private String method;//快递方式
	private String posttime;//快递时间
	private String infourl;//详情链接
	private String packages;//包裹信息
	private String bprice;//
	private String sku;//规格对应价格参数
	private String fPrice;//非免邮价格
	private String feePrice;//免邮最小不为0 的运费
	private String imgfile;//
	private String noteurl;//
	private int dtime;//促销标志
	private String uuid;//url  md5取值
	
	
	private String remotPath;
	private String localPath;
	
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/** 
	 * @return id 
	 */
	
	public int getId() {
		return id;
	}
	/** 
	 * @param id will set with id 
	 */
	
	public void setId(int id) {
		this.id = id;
	}
	public String getRemotPath() {
		return remotPath;
	}
	public void setRemotPath(String remotPath) {
		this.remotPath = remotPath;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public int getDtime() {
		return dtime;
	}
	public void setDtime(int dtime) {
		this.dtime = dtime;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getNoteurl() {
		return noteurl;
	}
	public void setNoteurl(String noteurl) {
		this.noteurl = noteurl;
	}
	public String getImgfile() {
		return imgfile;
	}
	public void setImgfile(String imgfile) {
		this.imgfile = imgfile;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getcID() {
		return cID;
	}
	public void setcID(String cID) {
		this.cID = cID;
	}
	public String getpID() {
		return pID;
	}
	public void setpID(String pID) {
		this.pID = pID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getoPrice() {
		return oPrice;
	}
	public void setoPrice(String oPrice) {
		this.oPrice = oPrice;
	}
	public String getsPrice() {
		return sPrice;
	}
	public void setsPrice(String sPrice) {
		this.sPrice = sPrice;
	}
	public String getwPrice() {
		return wPrice;
	}
	public void setwPrice(String wPrice) {
		this.wPrice = wPrice;
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
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getImgSize() {
		return imgSize;
	}
	public void setImgSize(String imgSize) {
		this.imgSize = imgSize;
	}
	public String getmOrder() {
		return mOrder;
	}
	public void setmOrder(String mOrder) {
		this.mOrder = mOrder;
	}
	public String getpTime() {
		return pTime;
	}
	public void setpTime(String pTime) {
		this.pTime = pTime;
	}
	public String getSellUnit() {
		return sellUnit;
	}
	public void setSellUnit(String sellUnit) {
		this.sellUnit = sellUnit;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getPerWeight() {
		return perWeight;
	}
	public void setPerWeight(String perWeight) {
		this.perWeight = perWeight;
	}
	public String getFree() {
		return free;
	}
	public void setFree(String free) {
		this.free = free;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCateurl() {
		return cateurl;
	}
	public void setCateurl(String cateurl) {
		this.cateurl = cateurl;
	}
	public String getsID() {
		return sID;
	}
	public void setsID(String sID) {
		this.sID = sID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getsUrl() {
		return sUrl;
	}
	public void setsUrl(String sUrl) {
		this.sUrl = sUrl;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getsGoods() {
		return sGoods;
	}
	public void setsGoods(String sGoods) {
		this.sGoods = sGoods;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSell() {
		return sell;
	}
	public void setSell(String sell) {
		this.sell = sell;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getPosttime() {
		return posttime;
	}
	public void setPosttime(String posttime) {
		this.posttime = posttime;
	}
	public String getInfourl() {
		return infourl;
	}
	public void setInfourl(String infourl) {
		this.infourl = infourl;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public String getBprice() {
		return bprice;
	}
	public void setBprice(String bprice) {
		this.bprice = bprice;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getfPrice() {
		return fPrice;
	}
	public void setfPrice(String fPrice) {
		this.fPrice = fPrice;
	}
	public String getFeePrice() {
		return feePrice;
	}
	public void setFeePrice(String feePrice) {
		this.feePrice = feePrice;
	}
	
	

}
