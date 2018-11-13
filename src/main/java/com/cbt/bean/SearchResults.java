package com.cbt.bean;

import java.util.Date;

public class SearchResults {
    private Integer id;

    private Integer indexId;//index_id

    private String aliexpressCatid;//aliexpress类别id
    
    private String goodsPid;//产品id
    
    private String goodsUrl;//产品 url

    private String goodsName;//产品名称
    
    private String goodsNameEn;//产品名称（英文）
    
    private String goodsType;//产品规格
    
    private String  goodsTypeEn;//产品规格英文
    
    private String  goodsDetail;//产品明细
    
    private String goodsDetailEn;//产品明细英文
    
    private String goodsInfo;//详情（图片）
    
    private String goodsWeight;//产品重量

    private Double goodsPrice;//产品价格

    private Double goodsPriceRe;//ImportExpress产品价格
    
    private String goodsImg;//产品图片

    private String factoryName;//工厂名称

    private String factoryId;//工厂id
    
    private String factoryUrl;//工厂url
    
    private String trade;//产品成交额
    
    private Date createTime;//时间

    private Integer goodsValid;//产品状态
    
    private Integer count;//计数
    
    private Integer goodsSold;//已经售出数量
    
    private Integer goodsMorder;//最小订量
    
    private String downLoadImg;//下载图片
    
    private Integer downLoadFlag;//图片下载标志
    
    private  Integer updateFlag ;
    
	public String getDownLoadImg() {
		return downLoadImg;
	}

	public void setDownLoadImg(String downLoadImg) {
		this.downLoadImg = downLoadImg;
	}

	public Integer getDownLoadFlag() {
		return downLoadFlag;
	}

	public void setDownLoadFlag(Integer downLoadFlag) {
		this.downLoadFlag = downLoadFlag;
	}

	public Double getGoodsPriceRe() {
		return goodsPriceRe;
	}

	public void setGoodsPriceRe(Double goodsPriceRe) {
		this.goodsPriceRe = goodsPriceRe;
	}

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public Integer getGoodsSold() {
		return goodsSold;
	}

	public void setGoodsSold(Integer goodsSold) {
		this.goodsSold = goodsSold;
	}

	public Integer getGoodsMorder() {
		return goodsMorder;
	}

	public void setGoodsMorder(Integer goodsMorder) {
		this.goodsMorder = goodsMorder;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getAliexpressCatid() {
		return aliexpressCatid;
	}

	public void setAliexpressCatid(String aliexpressCatid) {
		this.aliexpressCatid = aliexpressCatid;
	}

	public String getFactoryUrl() {
		return factoryUrl;
	}

	public void setFactoryUrl(String factoryUrl) {
		this.factoryUrl = factoryUrl;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getGoodsNameEn() {
		return goodsNameEn;
	}

	public void setGoodsNameEn(String goodsNameEn) {
		this.goodsNameEn = goodsNameEn;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getGoodsTypeEn() {
		return goodsTypeEn;
	}

	public void setGoodsTypeEn(String goodsTypeEn) {
		this.goodsTypeEn = goodsTypeEn;
	}

	public String getGoodsDetail() {
		return goodsDetail;
	}

	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}

	public String getGoodsDetailEn() {
		return goodsDetailEn;
	}

	public void setGoodsDetailEn(String goodsDetailEn) {
		this.goodsDetailEn = goodsDetailEn;
	}

	public String getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(String goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	public String getGoodsWeight() {
		return goodsWeight;
	}

	public void setGoodsWeight(String goodsWeight) {
		this.goodsWeight = goodsWeight;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getGoodsPid() {
		return goodsPid;
	}

	public void setGoodsPid(String goodsPid) {
		this.goodsPid = goodsPid;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl == null ? null : goodsUrl.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg == null ? null : goodsImg.trim();
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName == null ? null : factoryName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getGoodsValid() {
        return goodsValid;
    }

    public void setGoodsValid(Integer goodsValid) {
        this.goodsValid = goodsValid;
    }

	public Integer getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(Integer updateFlag) {
		this.updateFlag = updateFlag;
	}
    
    
}