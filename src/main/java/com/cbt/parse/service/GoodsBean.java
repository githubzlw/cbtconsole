package com.cbt.parse.service;

import com.cbt.parse.bean.SearchWordBean;
import com.cbt.parse.bean.SupplierGoods;
import com.cbt.parse.bean.TypeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**商品类
 * @author abc
 */
public class GoodsBean {
//	private String keywords;//seo关键词
//	private String description;//seo description
	private int id;
	private String ip;
	private int cid;//网站来源
	private int valid = 1;
	private String ctime; //数据入库时间
	private float extra_freight;//额外运费
	private int dtime = 0;//商品价格优惠失效时间
	private String pUrl;
	private String title;//网页title
	private String pName;//商品名称
	private String pID;//商品id
	private String sID;//卖家id
	private String sName;//卖家名称（可能为公司名）
	private ArrayList<String> pWprice;//商品批发价格
	private String bPrice;//bulk price
	private String pOprice;//商品原价
	private String pSprice;//商品现价
	private String pPriceUnit;//货币单位
	private String pGoodsUnit;//商品单位
	private String minOrder;//最小订单
//	private String minUnit;//最小订单单位
//	private String maxOrder;//最大订单
	private String sell;//已经售出数量
	private String category;//商品类别
//	private String cateurl;//商品类别链接
	private String free;//免邮标志，为“1”是免邮,"2"去请求免邮信息，“3”非免邮
	private String method;//免邮的快递方式
	private String time;//快递时间
	private String weight;//packing 重量
	private String width;//packing 长*宽*高
	private String volume;//计算后的商品体积
	private String sellUnits;//packing 计数单位
	private String perWeight;
	private String supplierUrl;
	
	private String[] imgSize;//图片后缀，小图在前  大图在后
	private ArrayList<TypeBean> type;//商品规格
	private ArrayList<String> pImage;//商品图片
	private String pTime;//订单处理时间
	private String pFreight;//运费
	private boolean pFreightChange = false;//运费是否随价格变动
	private HashMap<String, String> pInfo;//商品详细信息
	private ArrayList<String> pDetail;//
	private String info_ori;//商品详细（图片+文字）
	private List<SupplierGoods> supplier;//供应商推荐的其他商品
	private List<SearchWordBean> relate;//相关关键词
	private String apiItemDesc;
	private String pDescription;//获取Product Description内容信息
	private String com;//数据来源(web or sql)
	private String packages;//包装信息
	private String infourl;//详情url
	private String flag="1";//详情标志
	private String skuProducts;
	private String fprice;//去除免邮运费后的商品价格
	private String feeprice;//运费（被免去的运费）
	private String noteUrl;//用户反馈
	private String categps;//头部导航
	private String catid1;//分级类别id
	private String catid2;
	private String catid3;
	private String catid4;
	private String catid5;
	private String catid6;//最高级别catid
	private String pvid;//规格明细id
	
	
	
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


	public GoodsBean(){
	}
	
	
	public ArrayList<String> getpDetail() {
		return pDetail;
	}


	public void setpDetail(ArrayList<String> pDetail) {
		this.pDetail = pDetail;
	}


	public float getExtra_freight() {
		return extra_freight;
	}


	public void setExtra_freight(float extra_freight) {
		this.extra_freight = extra_freight;
	}


	public String getPvid() {
		return pvid;
	}
	public void setPvid(String pvid) {
		this.pvid = pvid;
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


	public String getCategps() {
		return categps;
	}


	public void setCategps(String categps) {
		this.categps = categps;
	}


	public int getDtime() {
		return dtime;
	}
	public void setDtime(int dtime) {
		this.dtime = dtime;
	}
	public String getNoteUrl() {
		return noteUrl;
	}

	public void setNoteUrl(String noteUrl) {
		this.noteUrl = noteUrl;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
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
	public String getSkuProducts() {
		return skuProducts;
	}
	public void setSkuProducts(String skuProducts) {
		this.skuProducts = skuProducts;
	}
	public ArrayList<TypeBean> getType() {
		return type;
	}
	public void setType(ArrayList<TypeBean> type) {
		this.type = type;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getInfourl() {
		return infourl;
	}
	public void setInfourl(String infourl) {
		this.infourl = infourl;
	}
	public String getCom() {
		return com;
	}
	public void setCom(String com) {
		this.com = com;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getSell() {
		return sell;
	}
	public void setSell(String sell) {
		this.sell = sell;
	}
	public String getSupplierUrl() {
		return supplierUrl;
	}
	public void setSupplierUrl(String supplierUrl) {
		this.supplierUrl = supplierUrl;
	}
	public List<SearchWordBean> getRelate() {
		return relate;
	}
	public void setRelate(List<SearchWordBean> relate) {
		this.relate = relate;
	}
	public String getPerWeight() {
		return perWeight;
	}
	public void setPerWeight(String perWeight) {
		this.perWeight = perWeight;
	}
	public String getSellUnits() {
		return sellUnits;
	}
	public void setSellUnits(String sellUnits) {
		this.sellUnits = sellUnits;
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
	public String getFree() {
		return free;
	}
	public void setFree(String free) {
		this.free = free;
	}
	public void setpDescription(String pDescription) {
		this.pDescription = pDescription;
	}
	public String getpDescription() {
		return pDescription;
	}
	public List<SupplierGoods> getSupplier() {
		return supplier;
	}
	public void setSupplier(List<SupplierGoods> supplier) {
		this.supplier = supplier;
	}
	public String getInfo_ori() {
		return info_ori;
	}
	public void setInfo_ori(String info_ori) {
		this.info_ori = info_ori;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getpPriceUnit() {
		return pPriceUnit;
	}
	public void setpPriceUnit(String pPriceUnit) {
		this.pPriceUnit = pPriceUnit;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getpID() {
		return pID;
	}
	public void setpID(String pID) {
		this.pID = pID;
	}
	public String getsID() {
		return sID;
	}
	public void setsID(String sID) {
		this.sID = sID;
	}
	public String getsName() {
		return sName;
	}
	public String getpUrl() {
		return pUrl;
	}
	public void setpUrl(String pUrl) {
		this.pUrl = pUrl;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getMinOrder() {
		return minOrder;
	}
	public void setMinOrder(String minOrder) {
		this.minOrder = minOrder;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public ArrayList<String> getpImage() {
		return pImage;
	}
	public void setpImage(ArrayList<String> pImage) {
		this.pImage = pImage;
	}
	public HashMap<String, String> getpInfo() {
		return pInfo;
	}
	public void setpInfo(HashMap<String, String> pInfo) {
		this.pInfo = pInfo;
	}
	public String getpTime() {
		return pTime;
	}
	public void setpTime(String pTime) {
		this.pTime = pTime;
	}
	public String getpFreight() {
		return pFreight;
	}
	public void setpFreight(String sCost) {
		this.pFreight = sCost;
	}
	
	public String getpOprice() {
		return pOprice;
	}
	public void setpOprice(String pOprice) {
		this.pOprice = pOprice;
	}
	public String getpSprice() {
		return pSprice;
	}
	public void setpSprice(String pSprice) {
		this.pSprice = pSprice;
	}
	public ArrayList<String> getpWprice() {
		return pWprice;
	}
	public void setpWprice(ArrayList<String> pWprice) {
		this.pWprice = pWprice;
	}
	
	public String getApiItemDesc() {
		return apiItemDesc;
	}
	public void setApiItemDesc(String apiItemDesc) {
		this.apiItemDesc = apiItemDesc;
	}
	public String[] getImgSize() {
		return imgSize;
	}
	public void setImgSize(String[] imgSize) {
		this.imgSize = imgSize;
	}
	public void setpGoodsUnit(String pGoodsUnit) {
		this.pGoodsUnit = pGoodsUnit;
	}
	public String getpGoodsUnit() {
		return pGoodsUnit;
	}
	public boolean getpFreightChange() {
		return pFreightChange;
	}
	public void setpFreightChange(boolean pFreightChange) {
		this.pFreightChange = pFreightChange;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getbPrice() {
		return bPrice;
	}
	public void setbPrice(String bPrice) {
		if(bPrice == null || bPrice.isEmpty() || "".equals(bPrice.trim())){
			this.bPrice = "0";
		}else{
			this.bPrice = bPrice;
		}
	}


	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"ip\":\"%s\", \"cid\":\"%s\", \"valid\":\"%s\", \"ctime\":\"%s\", \"extra_freight\":\"%s\", \"dtime\":\"%s\", \"pUrl\":\"%s\", \"title\":\"%s\", \"pName\":\"%s\", \"pID\":\"%s\", \"sID\":\"%s\", \"sName\":\"%s\", \"pWprice\":\"%s\", \"bPrice\":\"%s\", \"pOprice\":\"%s\", \"pSprice\":\"%s\", \"pPriceUnit\":\"%s\", \"pGoodsUnit\":\"%s\", \"minOrder\":\"%s\", \"sell\":\"%s\", \"category\":\"%s\", \"free\":\"%s\", \"method\":\"%s\", \"time\":\"%s\", \"weight\":\"%s\", \"width\":\"%s\", \"volume\":\"%s\", \"sellUnits\":\"%s\", \"perWeight\":\"%s\", \"supplierUrl\":\"%s\", \"imgSize\":\"%s\", \"type\":\"%s\", \"pImage\":\"%s\", \"pTime\":\"%s\", \"pFreight\":\"%s\", \"pFreightChange\":\"%s\", \"pInfo\":\"%s\", \"pDetail\":\"%s\", \"info_ori\":\"%s\", \"supplier\":\"%s\", \"relate\":\"%s\", \"apiItemDesc\":\"%s\", \"pDescription\":\"%s\", \"com\":\"%s\", \"packages\":\"%s\", \"infourl\":\"%s\", \"flag\":\"%s\", \"skuProducts\":\"%s\", \"fprice\":\"%s\", \"feeprice\":\"%s\", \"noteUrl\":\"%s\", \"categps\":\"%s\", \"catid1\":\"%s\", \"catid2\":\"%s\", \"catid3\":\"%s\", \"catid4\":\"%s\", \"catid5\":\"%s\", \"catid6\":\"%s\", \"pvid\":\"%s\"}",
						id, ip, cid, valid, ctime, extra_freight, dtime, pUrl,
						title, pName, pID, sID, sName, pWprice, bPrice,
						pOprice, pSprice, pPriceUnit, pGoodsUnit, minOrder,
						sell, category, free, method, time, weight, width,
						volume, sellUnits, perWeight, supplierUrl, imgSize,
						type, pImage, pTime, pFreight, pFreightChange, pInfo,
						pDetail, info_ori, supplier, relate, apiItemDesc,
						pDescription, com, packages, infourl, flag,
						skuProducts, fprice, feeprice, noteUrl, categps,
						catid1, catid2, catid3, catid4, catid5, catid6, pvid);
	}


	public Boolean isEmpty() {
		Boolean result = false;
		if(pName == null || pName.isEmpty()){
			result = true;	
		}
		return result;
	}
	 
}
