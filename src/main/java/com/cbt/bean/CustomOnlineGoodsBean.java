package com.cbt.bean;

/**
 * 
 * @ClassName CustomOnlineGoodsBean
 * @Description 线上产品全部字段的bean
 * @author JXW
 * @date 2017年12月13日 下午3:29:36
 */
public class CustomOnlineGoodsBean {
	private int id;//
	private String catid1;// 1688类别id
	private String pid;// 1688商品id
	private double price;// 1688价格
	private String wprice;
	private String customMainImage;// 1688主图
	private String img;// 1688橱窗图片
	private String weight;// 1688重量
	private String feeprice;// 计算1688的运费
	private String fprice;// 计算的1688工厂价
	private String name;// 1688产品中文标题
	private String enname;// 1688产品名称
	private int morder;// 1688最小定量
	private String entype;// 1688规格
	private String sku;// 1688sku
	private String endetail;// 1688描述明细
	private String eninfo;// 1688详情

	private int aliSold;// ali销量
	private String aliPid;// ali产品id
	private String aliPrice;// ali价格
	private String aliWeight;// ali重量
	private String aliFreight;// ali运费
	private String aliSellunit;// ali重量使用的单位
	private String aliMorder;//
	private String aliUnit;// ali售卖单位
	private String aliName;// ali产品名称

	private String remotPath;//
	private int valid;//
	private String localPath;//
	private String createtime;//
	private String catid;// aliexpress第三级分类
	private String catidParenta;// aliexpress第一级分类
	private String catidParentb;// aliexpress第二级分类
	private String keyword;//
	private int sold;//
	private String catidb;// aliexpress分类b
	private String catpath;// aliexpress分类树
	private String originalCatid;// aliexpress商品原先分类
	private String originalCatpath;// 原先aliexpress分类树
	private String aliImg;// ali图片
	private int imgCheck;// 韩明距离值

	private String reviseWeight;// 人为修正重量
	private String finalWeight;// 商品使用的最终重量
	private String rangePrice;// 价格区间
	private String shopId;// 店铺id
	private String wholesalePrice;// 产品原批发价格
	private String fpriceStr;// 价格计算用工厂价
	private String pvids;// 规格属性

	private int infoReviseFlag;// 是否人工修改 0-未修改 1-标题修改 2-详情修改
	private int priceReviseFlag;// 是否人工修改价格 0-未修改 1-修改
	private int isBenchmark;// 是否对标产品 : 1:精确对标, 2:近似对标,0:没找到对标
	private int isNewCloud;// 是否新品云产品
	private String finalName;// 最终显示的产品标题
	private String sellUnit;// 1688售卖单位
	private String curTime;//

	private int bmFlag;// 是否人为对标货源,1:是， 2：不是 ,0:默认值
	private int sourceProFlag;// 货源属性 1:同店铺商品,2:同款商品 ,3:对标商品,4:1688商品(原始老数据)
	private int isSoldFlag;// 是否售卖 1:卖过，2：没有买过
	private int priorityFlag;// 商品优先级 1:核心,2：非核心
	private int isAddCarFlag;// 是否加入购物车 1:已加入购物车 ,2:未加入购物车, 3:已加入购物车后删除
	private int sourceUsedFlag;// 货源信息可用度 1:可用， 2：不可用'
	private int ocrMatchFlag;// OCR判断
								// 1：人工OCR判断，2：未判断，3：未判断(全禁)，4：未判断(全免)，5：无中文，6：有中文，但不删除
								// （因为图片比例少），7：有中文，部分图应被删除，8：没图
	private int isShowDetImgFlag;// 是否展现详情图片1:展示，0：不展示
	private int isShowDetTableFlag;// 是否展现详情表格1:展示，0：不展示
	private int flag;// 0未处理，1已处理,2历史价格为空，3sku数据错误
	private int goodsState;// 发布状态：1-发布中 2-产品下架 3-发布失败 4-发布成功 5-待发布
	private int imgDownFlag;//图片是否下载 0未下载 1已下载

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCatid1() {
		return catid1;
	}

	public void setCatid1(String catid1) {
		this.catid1 = catid1;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getWprice() {
		return wprice;
	}

	public void setWprice(String wprice) {
		this.wprice = wprice;
	}

	public String getCustomMainImage() {
		return customMainImage;
	}

	public void setCustomMainImage(String customMainImage) {
		this.customMainImage = customMainImage;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
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

	public int getMorder() {
		return morder;
	}

	public void setMorder(int morder) {
		this.morder = morder;
	}

	public String getEntype() {
		return entype;
	}

	public void setEntype(String entype) {
		this.entype = entype;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getEndetail() {
		return endetail;
	}

	public void setEndetail(String endetail) {
		this.endetail = endetail;
	}

	public String getEninfo() {
		return eninfo;
	}

	public void setEninfo(String eninfo) {
		this.eninfo = eninfo;
	}

	public int getAliSold() {
		return aliSold;
	}

	public void setAliSold(int aliSold) {
		this.aliSold = aliSold;
	}

	public String getAliPid() {
		return aliPid;
	}

	public void setAliPid(String aliPid) {
		this.aliPid = aliPid;
	}

	public String getAliPrice() {
		return aliPrice;
	}

	public void setAliPrice(String aliPrice) {
		this.aliPrice = aliPrice;
	}

	public String getAliWeight() {
		return aliWeight;
	}

	public void setAliWeight(String aliWeight) {
		this.aliWeight = aliWeight;
	}

	public String getAliFreight() {
		return aliFreight;
	}

	public void setAliFreight(String aliFreight) {
		this.aliFreight = aliFreight;
	}

	public String getAliSellunit() {
		return aliSellunit;
	}

	public void setAliSellunit(String aliSellunit) {
		this.aliSellunit = aliSellunit;
	}

	public String getAliMorder() {
		return aliMorder;
	}

	public void setAliMorder(String aliMorder) {
		this.aliMorder = aliMorder;
	}

	public String getAliUnit() {
		return aliUnit;
	}

	public void setAliUnit(String aliUnit) {
		this.aliUnit = aliUnit;
	}

	public String getAliName() {
		return aliName;
	}

	public void setAliName(String aliName) {
		this.aliName = aliName;
	}

	public String getRemotPath() {
		return remotPath;
	}

	public void setRemotPath(String remotPath) {
		this.remotPath = remotPath;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public String getCatidParenta() {
		return catidParenta;
	}

	public void setCatidParenta(String catidParenta) {
		this.catidParenta = catidParenta;
	}

	public String getCatidParentb() {
		return catidParentb;
	}

	public void setCatidParentb(String catidParentb) {
		this.catidParentb = catidParentb;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public String getCatidb() {
		return catidb;
	}

	public void setCatidb(String catidb) {
		this.catidb = catidb;
	}

	public String getCatpath() {
		return catpath;
	}

	public void setCatpath(String catpath) {
		this.catpath = catpath;
	}

	public String getOriginalCatid() {
		return originalCatid;
	}

	public void setOriginalCatid(String originalCatid) {
		this.originalCatid = originalCatid;
	}

	public String getOriginalCatpath() {
		return originalCatpath;
	}

	public void setOriginalCatpath(String originalCatpath) {
		this.originalCatpath = originalCatpath;
	}

	public String getAliImg() {
		return aliImg;
	}

	public void setAliImg(String aliImg) {
		this.aliImg = aliImg;
	}

	public int getImgCheck() {
		return imgCheck;
	}

	public void setImgCheck(int imgCheck) {
		this.imgCheck = imgCheck;
	}

	public String getReviseWeight() {
		return reviseWeight;
	}

	public void setReviseWeight(String reviseWeight) {
		this.reviseWeight = reviseWeight;
	}

	public String getFinalWeight() {
		return finalWeight;
	}

	public void setFinalWeight(String finalWeight) {
		this.finalWeight = finalWeight;
	}

	public String getRangePrice() {
		return rangePrice;
	}

	public void setRangePrice(String rangePrice) {
		this.rangePrice = rangePrice;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getWholesalePrice() {
		return wholesalePrice;
	}

	public void setWholesalePrice(String wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}

	public String getFpriceStr() {
		return fpriceStr;
	}

	public void setFpriceStr(String fpriceStr) {
		this.fpriceStr = fpriceStr;
	}

	public String getPvids() {
		return pvids;
	}

	public void setPvids(String pvids) {
		this.pvids = pvids;
	}

	public int getInfoReviseFlag() {
		return infoReviseFlag;
	}

	public void setInfoReviseFlag(int infoReviseFlag) {
		this.infoReviseFlag = infoReviseFlag;
	}

	public int getPriceReviseFlag() {
		return priceReviseFlag;
	}

	public void setPriceReviseFlag(int priceReviseFlag) {
		this.priceReviseFlag = priceReviseFlag;
	}

	public int getIsBenchmark() {
		return isBenchmark;
	}

	public void setIsBenchmark(int isBenchmark) {
		this.isBenchmark = isBenchmark;
	}

	public int getIsNewCloud() {
		return isNewCloud;
	}

	public void setIsNewCloud(int isNewCloud) {
		this.isNewCloud = isNewCloud;
	}

	public String getFinalName() {
		return finalName;
	}

	public void setFinalName(String finalName) {
		this.finalName = finalName;
	}

	public String getSellUnit() {
		return sellUnit;
	}

	public void setSellUnit(String sellUnit) {
		this.sellUnit = sellUnit;
	}

	public String getCurTime() {
		return curTime;
	}

	public void setCurTime(String curTime) {
		this.curTime = curTime;
	}

	public int getBmFlag() {
		return bmFlag;
	}

	public void setBmFlag(int bmFlag) {
		this.bmFlag = bmFlag;
	}

	public int getSourceProFlag() {
		return sourceProFlag;
	}

	public void setSourceProFlag(int sourceProFlag) {
		this.sourceProFlag = sourceProFlag;
	}

	public int getIsSoldFlag() {
		return isSoldFlag;
	}

	public void setIsSoldFlag(int isSoldFlag) {
		this.isSoldFlag = isSoldFlag;
	}

	public int getPriorityFlag() {
		return priorityFlag;
	}

	public void setPriorityFlag(int priorityFlag) {
		this.priorityFlag = priorityFlag;
	}

	public int getIsAddCarFlag() {
		return isAddCarFlag;
	}

	public void setIsAddCarFlag(int isAddCarFlag) {
		this.isAddCarFlag = isAddCarFlag;
	}

	public int getSourceUsedFlag() {
		return sourceUsedFlag;
	}

	public void setSourceUsedFlag(int sourceUsedFlag) {
		this.sourceUsedFlag = sourceUsedFlag;
	}

	public int getOcrMatchFlag() {
		return ocrMatchFlag;
	}

	public void setOcrMatchFlag(int ocrMatchFlag) {
		this.ocrMatchFlag = ocrMatchFlag;
	}

	public int getIsShowDetImgFlag() {
		return isShowDetImgFlag;
	}

	public void setIsShowDetImgFlag(int isShowDetImgFlag) {
		this.isShowDetImgFlag = isShowDetImgFlag;
	}

	public int getIsShowDetTableFlag() {
		return isShowDetTableFlag;
	}

	public void setIsShowDetTableFlag(int isShowDetTableFlag) {
		this.isShowDetTableFlag = isShowDetTableFlag;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getGoodsState() {
		return goodsState;
	}

	public void setGoodsState(int goodsState) {
		this.goodsState = goodsState;
	}

	public int getImgDownFlag() {
		return imgDownFlag;
	}

	public void setImgDownFlag(int imgDownFlag) {
		this.imgDownFlag = imgDownFlag;
	}
	
	

}
